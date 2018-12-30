package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.*;

public class CodeGenerator extends VisitorAdaptor {
    boolean errorDetected = false;
    Logger log = Logger.getLogger(getClass());

    private int mainPc;

    private boolean isClassScope = false;
    private boolean isMain = false;
    private Obj currentMethod = null;

    private static List<Byte> MethodTable = new ArrayList<>();
    private static Map<Struct, Integer> classStructToVftAdrMap = new HashMap<>();

    private Stack<Integer> addop = new Stack<>();
    private Stack<Integer> mulop = new Stack<>();
    private Stack<Integer> relop = new Stack<>();

    private Stack<Integer> preCondAdrStack = new Stack<>();
    private Stack<Integer> preCondFixupAdrStack = new Stack<>();
    private Stack<Integer> preDesignAdrStack = new Stack<>();
    private Stack<Integer> preDesignFixupAdrStack = new Stack<>();
    private Stack<List<Integer>> continueFixupAdrsStack = new Stack<>();
    private Stack<List<Integer>> breakFixupAdrsStack = new Stack<>();

    private List<Integer> condTermFixupAdrs = new ArrayList<>();
    private List<Integer> condFactFixupAdrs = new ArrayList<>();

    private Stack<Boolean> isVirtualCallStack = new Stack<>();

    public int getMainPc() {
        return mainPc;
    }

    public CodeGenerator() {
        // 		ord - convert char to int
        // 		chr - convert int to char
        Obj chrObj = SymbolTable.find("chr");
        Obj ordObj = SymbolTable.find("ord");
        Obj lenObj = SymbolTable.find("len");

        chrObj.setAdr(Code.pc);
        ordObj.setAdr(Code.pc);

        // chr & ord method definitions
        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);
        // put the actual param on the stack
        Code.put(Code.load_n);
        Code.put(Code.exit);
        Code.put(Code.return_);

        lenObj.setAdr(Code.pc);

        // len method definition
        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);
        // put the actual param on the stack
        Code.put(Code.load_n);
        Code.put(Code.arraylength);
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void report_error(String message, SyntaxNode info) {
        errorDetected = true;
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" na liniji ").append(line);
        log.error(msg.toString());
    }

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" na liniji ").append(line);
        log.info(msg.toString());
    }

    private void addWordToStaticData(int value, int address) {
        MethodTable.add((byte) Code.const_);
        MethodTable.add((byte) ((value >> 16) >> 8));
        MethodTable.add((byte) (value >> 16));
        MethodTable.add((byte) (value >> 8));
        MethodTable.add((byte) value);
        MethodTable.add((byte) Code.putstatic);
        MethodTable.add((byte) (address >> 8));
        MethodTable.add((byte) address);
    }

    private void addNameTerminator() {
        addWordToStaticData(-1, Code.dataSize++);
    }

    private void addTableTerminator() {
        addWordToStaticData(-2, Code.dataSize++);
    }

    private void addFunctionAddress(int functionAddress) {
        addWordToStaticData(functionAddress, Code.dataSize++);
    }

    private void addFunctionEntry(String name, int functionAddressInCodeBuffer) {
        for (int j = 0; j < name.length(); j++) {
            addWordToStaticData((int) (name.charAt(j)), Code.dataSize++);
        }
        addNameTerminator();
        addFunctionAddress(functionAddressInCodeBuffer);
    }

    private Map<String, Obj> getClassMethodsMap(Struct classStruct) {
        Map<String, Obj> methodsMap = new HashMap<>();

        if (classStruct == null) {
            // end of inheritance chain
            return methodsMap;
        }

        for (Obj obj : classStruct.getMembersTable().symbols()) {
            if (obj.getKind() == Obj.Meth) {
                methodsMap.put(obj.getName(), obj);
            }
        }

        for (Map.Entry<String, Obj> entry : getClassMethodsMap(classStruct.getElemType()).entrySet()) {
            if (!methodsMap.containsKey(entry.getKey())) {
                methodsMap.put(entry.getKey(), entry.getValue());
            }
        }

        return methodsMap;
    }

    private void addClassEntryToVft(Obj classObj) {
        // get class methods (overriden and inherited ones)
        Map<String, Obj> classMethodsMap = getClassMethodsMap(classObj.getType());

        classStructToVftAdrMap.put(classObj.getType(), Code.dataSize);

        report_info("Class = " + classObj.getName() + " start address " + Code.dataSize, null);

        classMethodsMap.values().forEach(methodObj -> addFunctionEntry(methodObj.getName(), methodObj.getAdr()));

        classMethodsMap.values().forEach(methodObj -> report_info("Method = " + methodObj.getName() + " start address " + methodObj.getAdr(), null));

        this.addTableTerminator();
    }

    private void createVft() {
        // Store vft-creation byte code before the first instruction
        // in main function.
        for (Byte byteCode : MethodTable)
            Code.buf[Code.pc++] = byteCode;

        MethodTable.clear();
    }

    private void afterForFixup() {
        // fixup continue jumps
        List<Integer> continueFixupAdrs = continueFixupAdrsStack.pop();
        continueFixupAdrs.forEach(adr -> Code.fixup(adr));
        continueFixupAdrs.clear();

        Code.putJump(preDesignAdrStack.pop());

        // fixup condition jump
        Code.fixup(preCondFixupAdrStack.pop());

        // fixup break jumps
        List<Integer> breakFixupAdrs = breakFixupAdrsStack.pop();
        breakFixupAdrs.forEach(adr -> Code.fixup(adr));
        breakFixupAdrs.clear();
    }

    private void afterIfFixup() {
        Code.fixup(ifCondFixupAdrStack.pop());
    }

    private void afterCondTermFixup() {
        // jump to END - set jump args to true
        Code.put(Code.pop);
        Code.loadConst(1);
        Code.putJump(0);
        condTermFixupAdrs.add(Code.pc - 2);

        condFactFixupAdrs.forEach(adr -> Code.fixup(adr));
        condFactFixupAdrs.clear();
    }

    private void afterCondFactFixup() {
        // jump to next OR
        Code.putFalseJump(relop.pop(), 0);
        condFactFixupAdrs.add(Code.pc - 2);
    }

    public void visit(Alloc alloc) {
        // new has it's arg in number of bytes, and our word
        // size is 4 bytes
        Code.put(Code.new_);
        Code.put2(alloc.struct.getNumberOfFields() * 4);

        // initialize object vftPtr
        Code.put(Code.dup);
        Code.loadConst(classStructToVftAdrMap.get(alloc.getType().struct));
        Code.put(Code.putfield);
        Code.put2(0);
    }

    public void visit(ClassName className) {
        isClassScope = true;
    }

    public void visit(ClassDecl classDecl) {
        this.addClassEntryToVft(classDecl.obj);

        isClassScope = false;
    }

    public void visit(PrintStmt print) {
        Struct exprStruct = print.getExpr().struct;

        if (exprStruct == SymbolTable.charType || exprStruct == SymbolTable.boolType) {
            Code.loadConst(1);
            Code.put(Code.bprint);
        } else {
            // TODO: check if we can print 4 bytes here instead (1 word size)
            Code.loadConst(4);
            Code.put(Code.print);
        }
    }

    public void visit(ReadStmt read) {
        Struct type = read.getDesignator().obj.getType();

        if (type.equals(SymbolTable.intType)) {
            Code.put(Code.read);
        }

        if (type.equals(SymbolTable.boolType) || type.equals(SymbolTable.charType)) {
            Code.put(Code.bread);
        }

        Code.store(read.getDesignator().obj);
    }

    public void visit(ConstValFactor factor) {
        Code.load(factor.getConstValue().obj);
    }

    public void visit(PreArrayIndex preArrayIndex) {
        // load the array address to estack, followed by index load after expr is resolved
        Obj array = ((ArrayDesignator) preArrayIndex.getParent()).getDesignator().obj;
        Code.load(array);
    }

    public void visit(ArrayAlloc arrayAlloc) {
        Code.put(Code.newarray);
        int isWordArray = 1;

        if (arrayAlloc.getType().struct.equals(SymbolTable.boolType)
                || arrayAlloc.getType().struct.equals(SymbolTable.charType)) {
            isWordArray = 0;
        }

        Code.put(isWordArray);
    }

    public void visit(Assignment assig) {
        Code.store(assig.getDesignator().obj);
    }

    public void visit(IdentDesignator identDesignator) {
        boolean isMethodVar = currentMethod.getLocalSymbols().stream()
                .anyMatch(var -> var.getName().equals(identDesignator.getName()));

        boolean isGlobalVar = SymbolTable.find(identDesignator.getName()) != SymbolTable.noObj;

        if (isClassScope && !isMethodVar && !isGlobalVar) {
            // load "this" arg
            // TODO: check if this is correct "this" impl => does it generate correct code
            Code.put(Code.load_n);
        }
    }

    public void visit(FieldDesignator fieldDesignator) {
        if (fieldDesignator.getDesignator().obj.getType().getKind() == Struct.Enum) {
            return;
        }

        // TODO: inspect this | disasm simple examples
        Code.load(fieldDesignator.getDesignator().obj);
    }

    public void visit(Increment inc) {
        Code.load(inc.getDesignator().obj);
        Code.loadConst(1);
        Code.put(Code.add);
        Code.store(inc.getDesignator().obj);
    }

    public void visit(Decrement dec) {
        Code.load(dec.getDesignator().obj);
        Code.loadConst(1);
        Code.put(Code.sub);
        Code.store(dec.getDesignator().obj);
    }

    public void visit(Var var) {
        Code.load(var.getDesignator().obj);
    }

    public void visit(MethodSig methodSig) {
        Obj meth = methodSig.obj;

        if ("main".equalsIgnoreCase(meth.getName())) {
            mainPc = Code.pc;
            isMain = true;
        }

        // TODO: check if this is the right time to do setAdr
        // set adr of first instruction
        meth.setAdr(Code.pc);

        Code.put(Code.enter);
        // set the number of formal args
        Code.put(meth.getLevel());
        // set the number of locals
        Code.put(meth.getLocalSymbols().size());
    }

    public void visit(MethodBeforeFirstStmt methodBeforeFirstStmt) {
        if (isMain) {
            // add bytecode for VFT init
            this.createVft();
        }

        currentMethod = ((MethodDecl)methodBeforeFirstStmt.getParent()).obj;
    }

    public void visit(MethodDecl methDecl) {
        if (isMain) {
            isMain = false;
        }

        if (methDecl.getMethodSig().obj.getType().equals(SymbolTable.noType)) {
            Code.put(Code.exit);
            Code.put(Code.return_);
        } else {
            // if we got to here, it means no return statement has occured
            // thus we generate a runtime error
            Code.put(Code.trap);
            Code.put(1);
        }

        currentMethod = null;
    }

    public void visit(ReturnExpr returnExpr) {
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(ReturnNoExpr returnNoExpr) {
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(FunctionCall functionCall) {
        boolean isClassMethod = functionCall.getDesignator().obj.getLocalSymbols()
                .stream()
                .anyMatch(symbol -> symbol.getName().equals(SymbolTable.THIS_OBJ_NAME));

        boolean isInterfaceMethod = false;

        if (functionCall.getDesignator() instanceof FieldDesignator) {
            isInterfaceMethod = ((FieldDesignator) functionCall.getDesignator())
                    .getDesignator().obj.getType().getKind() == Struct.Interface;
        }

        if (isClassMethod || isInterfaceMethod) {
            // here we already have "this" object adr on the estack
            // so we fetch the vtp from 0th pos
            Code.put(Code.getfield);
            Code.put2(0);
            Code.put(Code.invokevirtual);

            for (char c : functionCall.getDesignator().obj.getName().toCharArray()) {
                Code.put4((int)c);
            }

            Code.put4(-1);
        } else {
            // relative address
            int destAdr = functionCall.getDesignator().obj.getAdr() - Code.pc;

            Code.put(Code.call);
            Code.put2(destAdr);
        }

        isVirtualCallStack.pop();
    }

    public void visit(PreActualPars preActualPars) {
        Designator designator = ((FunctionCall)preActualPars.getParent()).getDesignator();

        boolean isClassMethod = designator.obj.getLocalSymbols()
                .stream()
                .anyMatch(symbol -> symbol.getName().equals(SymbolTable.THIS_OBJ_NAME));

        boolean isInterfaceMethod = false;

        if (designator instanceof FieldDesignator) {
            isInterfaceMethod = ((FieldDesignator) designator)
                    .getDesignator().obj.getType().getKind() == Struct.Interface;
        }

        if (isClassMethod || isInterfaceMethod) {
            // we expect referencing objects address on estack so we're
            // duplicating it to provide vtp access before invokevirtual
            Code.put(Code.dup);
        }

        isVirtualCallStack.push(isClassMethod);
    }

    public void visit(ActualPar actualPar) {
        if (isVirtualCallStack.peek()) {
            Code.put(Code.dup_x1);
            Code.put(Code.pop);
        }
    }

    public void visit(ProcCall procCall) {
        // other than void, last value on stack is the return value of the function call,
        // and as we're not assigning the result, we should just remove it
        if (!procCall.getFunctionCall().getDesignator().obj.getType().equals(SymbolTable.noType)) {
            Code.put(Code.pop);
        }
    }

    // TODO: Check if placeholder grammar is necessary
    public void visit(ForPreCondition forPreCondition) {
        preCondAdrStack.push(Code.pc);

        continueFixupAdrsStack.push(new ArrayList<>());
        breakFixupAdrsStack.push(new ArrayList<>());
    }

    public void visit(ForPreDesignator forPreDesignator) {
        // false jump to loop end, with jump args properly placed
        // on estack from condition statement
        Code.putFalseJump(Code.eq, 0);

        preCondFixupAdrStack.push(Code.pc - 2);

        // jump to after designator statement - loop start
        Code.putJump(0);
        preDesignFixupAdrStack.push(Code.pc - 2);
        preDesignAdrStack.push(Code.pc);
    }

    public void visit(ForPostDesignator forPostDesignator) {
        // fixup designator statement skip
        Code.putJump(preCondAdrStack.pop());
        Code.fixup(preDesignFixupAdrStack.pop());
    }

    public void visit(BreakStmt breakStmt) {
        Code.putJump(0);
        breakFixupAdrsStack.peek().add(Code.pc - 2);
    }

    public void visit(ContinueStmt continueStmt) {
        Code.putJump(0);
        continueFixupAdrsStack.peek().add(Code.pc - 2);
    }

    public void visit(MatchedFor matchedFor) {
        this.afterForFixup();
    }

    public void visit(UnmatchedFor unmatchedFor) {
        this.afterForFixup();
    }

    private Stack<Integer> ifCondFixupAdrStack = new Stack<>();

    public void visit(IfCondClause ifCondition) {
        // false jump to clause end, with jump args properly placed
        // on estack from condition statement
        Code.putFalseJump(Code.eq, 0);
        ifCondFixupAdrStack.push(Code.pc - 2);
    }

    public void visit(ElseCondition elseCondition) {
        Code.putJump(0);

        Code.fixup(ifCondFixupAdrStack.pop());
        ifCondFixupAdrStack.push(Code.pc - 2);
    }

    public void visit(MatchedIf matchedIf) {
        this.afterIfFixup();
    }

    public void visit(UnmatchedIf unmatchedIf) {
        this.afterIfFixup();
    }

    public void visit(UnmatchedIfElse unmatchedIfElse) {
        this.afterIfFixup();
    }

    public void visit(PreCondition preCondition) {
        // assume that the condition will be false
        // provide arguments for proper jeq
        Code.loadConst(1);
        Code.loadConst(0);
    }

    public void visit(Condition condition) {
        condTermFixupAdrs.forEach(adr -> Code.fixup(adr));
        condTermFixupAdrs.clear();
    }

    public void visit(OrCondition orCondition) {
        this.afterCondTermFixup();
    }

    public void visit(TermCondition termCondition) {
        this.afterCondTermFixup();
    }

    public void visit(RelopCondFact relopCondFact) {
        this.afterCondFactFixup();
    }

    public void visit(ExprCondFact exprCondFact) {
        Code.loadConst(1);
        relop.push(Code.eq);

        this.afterCondFactFixup();
    }

    public void visit(IsEqualRelop is) {
        relop.push(Code.eq);
    }

    public void visit(NotEqualRelop ne) {
        relop.push(Code.ne);
    }

    public void visit(GreaterRelop ge) {
        relop.push(Code.gt);
    }

    public void visit(LesserRelop le) {
        relop.push(Code.lt);
    }

    public void visit(GreaterOrEqualRelop ge) {
        relop.push(Code.ge);
    }

    public void visit(LesserOrEqualRelop le) {
        relop.push(Code.le);
    }

    public void visit(AddExpr addExpr) {
        Code.put(addop.pop());
    }

    public void visit(NegExpr negExpr) {
        Code.put(Code.neg);
    }

    public void visit(MulTerm mulTerm) {
        Code.put(mulop.pop());
    }

    public void visit(AddAddop add) {
        addop.push(Code.add);
    }

    public void visit(SubAddop sub) {
        addop.push(Code.sub);
    }

    public void visit(MulMulop mul) {
        mulop.push(Code.mul);
    }

    public void visit(DivMulop div) {
        mulop.push(Code.div);
    }

    public void visit(ModMulOp mod) {
        mulop.push(Code.rem);
    }
}
