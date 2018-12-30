package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.structure.HashTableDataStructure;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public class SemanticPass extends VisitorAdaptor {

    private boolean errorDetected = false;
    private Obj currentMethod = null;
    private ArrayList<Obj> currentMethodFormalParams = new ArrayList<>();
    private Stack<ArrayList<Struct>> actualParamsStructStack = new Stack<>();
    private Type currentType = null;
    private boolean returnFound = false;
    int nVars;
    private Obj currentEnum = null;
    private Obj prevEnumField = null;
    private Obj currentClass = null;
    private Obj currentInterface = null;
    private Stack<Boolean> isInLoopStack = new Stack<>();

    private Logger log = Logger.getLogger(getClass());

    // TODO: add semantic check for using Type Obj (already defined keywords)

    private void report_error(String message, SyntaxNode info) {
        errorDetected = true;
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" na liniji ").append(line);
        else if (info.getParent() != null)
            msg.append(" na liniji ").append(info.getParent().getLine());
        log.error(msg.toString());
    }

    private void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" na liniji ").append(line);

        log.info(msg.toString());
    }

    private void report_node(String message, Obj obj, SyntaxNode info) {
        ExtDumpStateVisitor visitor = new ExtDumpStateVisitor();
        visitor.visitObjNode(obj);

        this.report_info("[Detektovan Simbol] " + message + " | " + visitor.getOutput(), info);
    }

    private boolean isClassSubstitution(Struct ref, Struct impl) {
        if (impl == null) {
            return false;
        }

        if (!(ref.getKind() == Struct.Class && impl.getKind() == Struct.Class)) {
            return false;
        }

        if (ref.equals(impl)) {
            return true;
        }

        return isClassSubstitution(ref, impl.getElemType());
    }

    private boolean isInterfaceImpl(Struct iface, Struct impl) {
        if (impl == null) {
            return false;
        }

        if (!(impl.getKind() == Struct.Class && iface.getKind() == Struct.Interface)) {
            return false;
        }

        if (impl.getImplementedInterfaces().stream().anyMatch(x -> x.equals(iface))) {
            return true;
        }

        return isInterfaceImpl(iface, impl.getElemType());
    }

    private boolean assignableTo(Struct from, Struct to) {
        boolean isEnumAssignable = from.getKind() == Struct.Int && to.getKind() == Struct.Enum;
        boolean isSubstitution = isClassSubstitution(to, from);
        boolean isImpl = isInterfaceImpl(to, from);

        if (isEnumAssignable || isSubstitution || isImpl) {
            return true;
        }

        return from.assignableTo(to);
    }

    private void matchActualAndFormalParams(int formParamNum, ArrayList<Struct> currentActParamStructs, Obj obj, SyntaxNode node) {
        Collection<Obj> localSymbols;

        // recursion - we need to get current scope arguments since we
        // didn't close the scope & chained symbols yet
        if (obj.equals(currentMethod)) {
            localSymbols = SymbolTable.currentScope().values();
        } else {
            localSymbols = obj.getLocalSymbols();
        }

        // get local function params - first n are formal params
        ArrayList<Obj> methLocalParams = new ArrayList<>(localSymbols);

        // check class method for "this" arg offset
        boolean isClassMethod = localSymbols.stream()
                .anyMatch(symbol -> symbol.getName().equals(SymbolTable.THIS_OBJ_NAME));
        int thisArgOffset = isClassMethod ? 1 : 0;

        if (formParamNum == currentActParamStructs.size() + thisArgOffset) {
            // we match first n meth local pars with n act pars
            for (int i = 0; i < currentActParamStructs.size(); i++) {
                Struct actParStruct = currentActParamStructs.get(i);
                Struct formParStruct = methLocalParams.get(i + thisArgOffset).getType();

                if (!this.assignableTo(actParStruct, formParStruct)) {
                    report_error("Greska: u pozivu metode" + obj.getName() + " nisu u skladu proslednji stvarni argumenti po tipu ", node);
                    break;
                }
            }
        } else {
            report_error("Greska: u pozivu metode " + obj.getName() + " nisu u skladu proslednji stvarni argumenti po broju ", node);
        }

    }

    private void updateClassMembers(Struct classStruct, Obj newObj) {
        SymbolDataStructure classMembers = classStruct.getMembersTable();

        // overwrite existing entries if any
        classMembers.insertKey(newObj);
        classStruct.setMembers(classMembers);
    }

    private void insertVarToSymbolTable(String varName, Struct varStruct, SyntaxNode varNode) {
        Obj symTableVar = SymbolTable.currentScope().findSymbol(varName);

        // shadowing allowed so we check only in current scope
        if (symTableVar != null) {
            report_error("Greska: Redeklarisanje promenljive " + varName, varNode);
        } else {
            if (currentClass != null && currentMethod == null) {
                Obj fldObj = new Obj(Obj.Fld, varName, varStruct, currentClass.getType().getNumberOfFields(), 0);
                this.updateClassMembers(currentClass.getType(), fldObj);
            } else {
                Obj varObj = SymbolTable.insert(Obj.Var, varName, varStruct);
                report_node("Deklarisana promenljiva " + varName, varObj, varNode);
            }
        }
    }

    private void insertFormParamToSymbolTable(String formParamName, Struct formParamStruct, SyntaxNode formParamNode) {
        boolean containsFormParam = currentMethodFormalParams.stream()
                .anyMatch(x -> x.getName().equals(formParamName));

        if (containsFormParam) {
            report_error("Greska: redeklarisan formalni argument " + formParamName, formParamNode);
        } else {
            Obj formParam = SymbolTable.insert(Obj.Var, formParamName, formParamStruct);
            currentMethodFormalParams.add(formParam);

//            report_node("Deklarisan formalni argument " + formParamName, formParam, formParamNode);
        }
    }

    private void insertMethodToSymbolTable(String methodTypeName, Struct methodStruct, SyntaxNode methodNode) {
        Obj meth = SymbolTable.find(methodTypeName);

        // Improvement here would be not to search by name, but to search by
        // Obj, and as such allow function overloading, as the feature
        // isn't support by current approach.
        if (meth != SymbolTable.noObj) {
            report_error("Greska: redeklarisanje metode " + methodTypeName, methodNode);
        } else {
            if (currentClass != null) {
                Obj methObj = new Obj(Obj.Meth, methodTypeName, methodStruct, 0, 0);
                currentMethod = methObj;
                currentClass.getType().getMembersTable().insertKey(currentMethod);
            } else {
                currentMethod = SymbolTable.insert(Obj.Meth, methodTypeName, methodStruct);
            }

            SymbolTable.openScope();

            // are we inside class/interface declaration?
            if (currentClass != null) {
                this.insertFormParamToSymbolTable(SymbolTable.THIS_OBJ_NAME, currentClass.getType(), null);
            }

//            report_node("Deklaracija metode " + methodTypeName, currentMethod, methodNode);
        }
    }

    private Obj getFieldObj(Struct struct, String fieldName, SyntaxNode node) {
        if (struct == null) {
            report_error("Greska: objekat/enumeracija ne sadrzi ime " + fieldName, node);
            return SymbolTable.noObj;
        }

        Obj fieldObj = struct.getMembersTable().searchKey(fieldName);

        if (fieldObj != null) {
            return fieldObj;
        }

        return getFieldObj(struct.getElemType(), fieldName, node);
    }

    // TODO: add class hierarchy traverse generic method

    private boolean alreadyImplementsInterface(Struct classStruct, Struct interfaceStruct) {
        if (classStruct == null) {
            return false;
        }

        if (classStruct.getImplementedInterfaces().contains(interfaceStruct)) {
            return true;
        }

        return alreadyImplementsInterface(classStruct.getElemType(), interfaceStruct);
    }

    public void visit(Program program) {
        nVars = SymbolTable.currentScope.getnVars();
        SymbolTable.chainLocalSymbols(program.getProgName().obj);
        SymbolTable.closeScope();
    }

    public void visit(ProgName progName) {
        progName.obj = SymbolTable.insert(Obj.Prog, progName.getPName(), SymbolTable.noType);
        SymbolTable.openScope();
    }

    public void visit(SimpleVarDeclIdent varDeclIdent) {
        insertVarToSymbolTable(varDeclIdent.getVarName(), currentType.struct, varDeclIdent);
    }

    public void visit(ArrayVarDeclIdent varDeclIdent) {
        insertVarToSymbolTable(varDeclIdent.getVarName(), new Struct(Struct.Array, currentType.struct), varDeclIdent);
    }

    public void visit(ConstDeclIdent constDeclIdent) {
        Obj symTableConst = SymbolTable.find(constDeclIdent.getConstName());

        // only global constants allowed
        if (symTableConst != SymbolTable.noObj) {
            report_error("Greska: redeklarisanje promenljive " + symTableConst.getName(), constDeclIdent);
        } else if (!constDeclIdent.getConstValue().obj.getType().equals(currentType.struct)) {
            report_error("Greska: pogresan tip dodeljen konstanti " + constDeclIdent.getConstName(), constDeclIdent);
        } else {
            Obj constNode = SymbolTable.insert(Obj.Con, constDeclIdent.getConstName(), currentType.struct);
            constNode.setAdr(constDeclIdent.getConstValue().obj.getAdr());
            report_node("Deklaracija konstante " + constDeclIdent.getConstName(), constNode, constDeclIdent);
        }
    }

    public void visit(ClassName className) {
        Obj obj = SymbolTable.find(className.getName());

        if (obj != SymbolTable.noObj) {
            // class already exists
        } else {
            currentClass = SymbolTable.insert(Obj.Type, className.getName(), new Struct(Struct.Class));
            SymbolTable.openScope();

            // TODO: think if it's possible to fully remove meth Obj's from class SymbolDataStructure
            // we add vftPtr as first field
            Obj vftPtrObj = new Obj(Obj.Fld, "vftPtr", SymbolTable.intType, currentClass.getType().getNumberOfFields(), 0);
            this.updateClassMembers(currentClass.getType(), vftPtrObj);

            SymbolTable.addClass(currentClass.getType());
        }

        className.obj = currentClass;
    }

    public void visit(ClassDecl classDecl) {
        // finalize class declaration
        // TODO: check if this is necessary
        SymbolTable.chainLocalSymbols(currentClass);
        SymbolTable.closeScope();
        SymbolTable.chainParentMethods(currentClass.getType());

        // check if class implements specified interfaces
        // we dont check inherited interfaces
        // since they already passed this check
        currentClass.getType().getImplementedInterfaces().forEach(iface -> {
            List<Obj> classMethods = SymbolTable.getClassMethods(currentClass.getType());

            iface.getMembers().forEach(ifaceMethod -> {
                if (classMethods
                        .stream()
                        .noneMatch(method -> {
                            // check if interface method signatures are the same
                            boolean sameNames = method.getName().equals(ifaceMethod.getName());
                            boolean sameReturnType = method.getType().equals(ifaceMethod.getType());
                            boolean sameFormalArgs = method.getLevel() == ifaceMethod.getLevel();

                            if (!(sameNames && sameReturnType && sameFormalArgs)) {
                                return false;
                            }

                            // compare each formal arg  pair
                            List<Obj> ifaceArgList = new ArrayList<>(ifaceMethod.getLocalSymbols());
                            List<Obj> methodArgList = new ArrayList<>(method.getLocalSymbols());

                            // start from 1, we avoid comparing "this"
                            for (int i = 1; i < ifaceMethod.getLevel(); i++) {
                                if (!ifaceArgList.get(i).equals(methodArgList.get(i))) {
                                    sameFormalArgs = false;
                                }
                            }

                            return sameFormalArgs;
                        })
                ) {
                    report_error("Greska: klasa " + currentClass.getName() + " ne implementira sve metode specificiranih interfjesa", classDecl);
                }
            });
        });

        classDecl.obj = currentClass;
        currentClass = null;
    }

    public void visit(ClassExt classExt) {
        if (currentType.struct.getKind() != Struct.Class) {
            report_error("Greska: " + currentType.getTypeName() + " nije klasa", classExt);
            return;
        }

        // add class ext
        currentClass.getType().setElementType(currentType.struct);

        // add extended fields first
        currentType.struct.getMembers().stream()
                .filter(member -> member.getKind() == Obj.Fld)
                .forEach(fld -> this.updateClassMembers(currentClass.getType(), fld));
    }

    public void visit(InterfaceImpl InterfaceImpl) {
         if (currentType.struct.getKind() != Struct.Interface) {
            report_error("Greska: " + currentType.getTypeName() + " nije interfejs", InterfaceImpl);
            return;
         }

        // check if one of the classes already implements
        if (alreadyImplementsInterface(currentClass.getType(), currentType.struct)) {
            report_error("Greska: klasa vec nasledjuje " + currentType.getTypeName() + " interfejs", InterfaceImpl);
            return;
        }

        currentClass.getType().addImplementedInterface(currentType.struct);
    }

    public void visit(InterfaceName interfaceName) {
        Obj obj = SymbolTable.find(interfaceName.getName());

        if (obj != SymbolTable.noObj) {
            // obj node with same name already exists
            report_error("Greska: ime " + interfaceName.getName() + " vec deklarisano", interfaceName);
        } else {
            // TODO: check if we can hold interface and class on the same variable
            currentClass = SymbolTable.insert(Obj.Type, interfaceName.getName(), new ExtStruct(Struct.Interface));
            currentInterface = currentClass;
        }
    }

    public void visit(InterfaceDecl interfaceDecl) {
        currentInterface = null;
    }

    public void visit(EnumName enumName) {
        Obj node = SymbolTable.find(enumName.getName());

        if (node != SymbolTable.noObj) {
            report_error("Greska: ime" + enumName.getName() + " vec deklarisano", enumName);
        } else {
            currentEnum = SymbolTable.insert(Obj.Type, enumName.getName(), new Struct(Struct.Enum));

            enumName.obj = currentEnum;
        }
    }

    public void visit(DefaultEnumField enumField) {
        SymbolDataStructure enumFields = currentEnum.getType().getMembersTable();
        int enumValue = 0; // default value in case no enums are specified

        if (enumFields.numSymbols() != 0) {
            enumValue = prevEnumField.getAdr() + 1;
        }

        Obj enumFieldObj = new Obj(Obj.Con, enumField.getName(), SymbolTable.intType, enumValue, Obj.NO_VALUE);

        enumFields.insertKey(enumFieldObj);
        prevEnumField = enumFieldObj;
    }

    public void visit(ValueEnumField enumField) {
        SymbolDataStructure enumFields = currentEnum.getType().getMembersTable();

        Obj enumFieldObj = new Obj(Obj.Con, enumField.getName(), SymbolTable.intType, enumField.getValue(), Obj.NO_VALUE);

        if (enumFields.symbols().stream().anyMatch(x -> x.getAdr() == enumField.getValue())) {
            report_error("Greska: redeklarisana vrednost " + enumField.getValue() + " nabrajanja " + enumField.getName(), enumField);
        } else {
            enumFields.insertKey(enumFieldObj);
            prevEnumField = enumFieldObj;
        }
    }

    public void visit(EnumDecl enumDecl) {
        report_node("Deklaracija enumeracije " + enumDecl.getEnumName().getName(), currentEnum, enumDecl);

        currentEnum = null;
        prevEnumField = null;
    }

    public void visit(SimpleFormParamDecl formParamDecl) {
        insertFormParamToSymbolTable(formParamDecl.getVarName(), formParamDecl.getType().struct, formParamDecl);
    }

    public void visit(ArrayFormParamDecl formParamDecl) {
        insertFormParamToSymbolTable(formParamDecl.getVarName(), formParamDecl.getType().struct, formParamDecl);
    }

    public void visit(PreActualPars preActualPars) {
        actualParamsStructStack.push(new ArrayList<>());
    }

    public void visit(ActualPar param) {
        actualParamsStructStack.peek().add(param.getExpr().struct);
    }

    public void visit(VarDecl varDecl) {
        currentType = null;
    }

    public void visit(ConstDecl constDecl) {
        currentType = null;
    }

    public void visit(Type type) {
        Obj typeNode = SymbolTable.find(type.getTypeName());
        if (typeNode == SymbolTable.noObj) {
            report_error("Greska: nije pronadjen tip " + type.getTypeName() + " u tabeli simbola", type);
            type.struct = SymbolTable.noType;
        } else {
            if (Obj.Type == typeNode.getKind()) {
                type.struct = typeNode.getType();

                // cast enum type declarations to int
                if (type.struct.getKind() == Struct.Enum) {
                    type.struct = SymbolTable.intType;
                }
            } else {
                report_error("Greska: ime " + type.getTypeName() + " ne predstavlja tip ", type);
                type.struct = SymbolTable.noType;
            }
        }

        // this is same as the currentMethod trick
        currentType = type;
    }

    public void visit(MethodDecl methodDecl) {
        // 1. check void return type policy
        // 2. check return type with return type expression
        // 3. check formal args

        if (!returnFound && currentMethod.getType() != SymbolTable.noType) {
            report_error("Greska: metoda " + currentMethod.getName() + " nema return iskaz!", methodDecl);
        }

        // always bound to an openScope caller
        // i.e. methName opens the scope for function variable
        // declarations, and methDecl closes it as it is the
        // last swap in the state machine.
        SymbolTable.chainLocalSymbols(currentMethod);
        SymbolTable.closeScope();

        methodDecl.obj = currentMethod;

        if (currentClass != null) {
            SymbolTable.addClassMethod(currentClass.getType(), currentMethod);
        }

        returnFound = false;
        currentMethod = null;
        currentMethodFormalParams.clear();
    }

    public void visit(MethodSig pars) {
        currentMethod.setLevel(currentMethodFormalParams.size());

        pars.obj = currentMethod;

        // if in interface, reset the currentMethod as it's
        // reset is in the methodDecl otherwise & chain locals
        if (currentInterface != null) {
            SymbolTable.chainLocalSymbols(currentMethod);
            SymbolTable.closeScope();
            currentMethodFormalParams.clear();

            currentMethod = null;
        }
    }

    public void visit(RetTypeMethodTypeName methodTypeName) {
        this.insertMethodToSymbolTable(methodTypeName.getMethName(), methodTypeName.getType().struct, methodTypeName);
    }

    public void visit(VoidMethodTypeName methodTypeName) {
        // TODO: check if void main method is defined
        // TODO: count methods in the program
        // TODO: count global variables
        // TODO: count global constants

        this.insertMethodToSymbolTable(methodTypeName.getMethName(), SymbolTable.noType, methodTypeName);
    }

    public void visit(Assignment assignment) {
        if (!this.assignableTo(assignment.getExpr().struct, assignment.getDesignator().obj.getType()))
            report_error("Greska: nekompatibilni tipovi u dodeli vrednosti ", assignment);
    }

    public void visit(Increment increment) {
        Obj obj = increment.getDesignator().obj;

        boolean isCorrectType = obj.getType().equals(SymbolTable.intType);
        boolean isCorrectRole = obj.getKind() == Obj.Elem ||
                obj.getKind() == Obj.Fld ||
                obj.getKind() == Obj.Var;

        if (!isCorrectType || !isCorrectRole) {
            report_error("Greska: nedozvoljen tip designatora", increment);
        }
    }

    public void visit(Decrement decrement) {
        Obj obj = decrement.getDesignator().obj;

        boolean isCorrectType = obj.getType().equals(SymbolTable.intType);
        boolean isCorrectRole = obj.getKind() == Obj.Elem ||
                obj.getKind() == Obj.Fld ||
                obj.getKind() == Obj.Var;

        if (!isCorrectType || !isCorrectRole) {
            report_error("Greska: nedozvoljen tip designatora", decrement);
        }
    }

    public void visit(ProcCall procCall) {
        Obj func = procCall.getFunctionCall().getDesignator().obj;

        if (Obj.Meth == func.getKind()) {
            int formParamNum = func.getLevel();
            ArrayList<Struct> currentActParamStructs = actualParamsStructStack.pop();

            this.matchActualAndFormalParams(formParamNum, currentActParamStructs, func, procCall);

            report_node("Pronadjen poziv metode " + func.getName(), func, procCall);
        } else {
            report_error("Greska: ime " + func.getName() + " nije metoda!", procCall);
        }
    }

    public void visit(ReturnExpr returnExpr) {
        returnFound = true;
        Struct currMethType = currentMethod.getType();

        if (!currMethType.compatibleWith(returnExpr.getExpr().struct)) {
            report_error("Greska: tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti metode " + currentMethod.getName(), returnExpr);
        }
    }

    public void visit(ReturnNoExpr returnExpr) {
        returnFound = true;
        Struct currMethType = currentMethod.getType();

        // check for void scenario
        if (currMethType != SymbolTable.noType) {
            report_error("Greska: praznu return naredbu moze imati samo void metoda, za razliku od trenutne " + currentMethod.getName(), returnExpr);
        }
    }

    public void visit(AddExpr addExpr) {
        Struct te = addExpr.getExpr().struct;
        Struct t = addExpr.getTerm().struct;

        boolean isIntAdd = te.equals(t) && te == SymbolTable.intType;

        if (isIntAdd)
            addExpr.struct = te;
        else {
            report_error("Greska: nekompatibilni tipovi u izrazu za sabiranje", addExpr);
            addExpr.struct = SymbolTable.noType;
        }
    }

    public void visit(NegExpr negExpr) {
        Struct termStruct = negExpr.getTerm().struct;

        if (termStruct.equals(SymbolTable.intType)) {
            negExpr.struct = termStruct;
        } else {
            report_error("Greska: nekompatibilan tip izraza za negaciju.", negExpr);
            negExpr.struct = SymbolTable.noType;
        }
    }

    public void visit(TermExpr termExpr) {
        termExpr.struct = termExpr.getTerm().struct;
    }

    public void visit(FactorTerm factorTerm) {
        factorTerm.struct = factorTerm.getFactor().struct;
    }

    public void visit(MulTerm mulTerm) {
        Struct termStruct = mulTerm.getTerm().struct;
        Struct factorStruct = mulTerm.getFactor().struct;

        boolean isIntMul = termStruct.equals(SymbolTable.intType) && termStruct.equals(factorStruct);

        if (isIntMul) {
            mulTerm.struct = SymbolTable.intType;
        } else {
            report_error("Greska: nekompatibilni tipovi u izrazu za mnozenje", mulTerm);
            mulTerm.struct = SymbolTable.noType;
        }
    }

    public void visit(BoolConst cnst) {
        cnst.obj = new Obj(Obj.Con, "", SymbolTable.boolType, Boolean.valueOf(cnst.getB1()) ? 1 : 0, Obj.NO_VALUE);
    }

    public void visit(IntConst cnst) {
        cnst.obj = new Obj(Obj.Con, "", SymbolTable.intType, cnst.getN1(), Obj.NO_VALUE);
    }

    public void visit(CharConst cnst) {
        cnst.obj = new Obj(Obj.Con, "", SymbolTable.charType, cnst.getC1(), Obj.NO_VALUE);
    }

    public void visit(Var var) {
        var.struct = var.getDesignator().obj.getType();
    }

    public void visit(ParenExprFactor exprFactor) {
        exprFactor.struct = exprFactor.getExpr().struct;
    }

    public void visit(ConstValFactor constValFactor) {
        constValFactor.struct = constValFactor.getConstValue().obj.getType();
    }

    public void visit(Alloc alloc) {
        if (alloc.getType().struct.getKind() != Struct.Class) {
            report_error("Greska: tip operator new nije klasa", alloc);
        } else {
            Obj obj = SymbolTable.find(alloc.getType().getTypeName());

            report_node("Pronadjeno pravljenje objekta klase " + alloc.getType().getTypeName(), obj, alloc);
        }

        alloc.struct = alloc.getType().struct;
    }

    public void visit(ArrayAlloc arrayAlloc) {
        Struct struct = new Struct(Struct.Array, SymbolTable.noType);

        if (!arrayAlloc.getExpr().struct.equals(SymbolTable.intType)) {
            report_error("Greska: vrednost kojom se alocira niz nije int ", arrayAlloc);
        } else {
            struct = new Struct(Struct.Array, arrayAlloc.getType().struct);
        }

        arrayAlloc.struct = struct;
    }

    public void visit(FuncCall funcCall) {
        Obj func = funcCall.getFunctionCall().getDesignator().obj;

        if (Obj.Meth == func.getKind()) {
            int formParamNum = func.getLevel();
            ArrayList<Struct> currentActParamStructs = actualParamsStructStack.pop();

            this.matchActualAndFormalParams(formParamNum, currentActParamStructs, func, funcCall);

            report_node("Pronadjen poziv metode " + func.getName(), func, funcCall);
            funcCall.struct = func.getType();
        } else {
            report_error("Greska: ime " + func.getName() + " nije metoda!", funcCall);
            funcCall.struct = SymbolTable.noType;
        }
    }

    public void visit(IdentDesignator designator) {
        Obj obj = SymbolTable.find(designator.getName());

        if (obj == SymbolTable.noObj) {
            if (currentClass != null) {
                // act as we reference "this" in current class definition
                obj = getFieldObj(currentClass.getType(), designator.getName(), designator);
            } else {
                report_error("Greska: ime " + designator.getName() + " nije deklarisano! ", designator);
            }
        } else {
            // check formal param usage
            if (currentMethodFormalParams.contains(obj)) {
                report_node("Pronadjeno koriscenje formalnog argumenta " + designator.getName(), obj, designator);
            }
        }

        designator.obj = obj;
    }

    public void visit(ArrayDesignator arrayDesignator) {
        Obj designatorObj = arrayDesignator.getDesignator().obj;
        arrayDesignator.obj = SymbolTable.noObj;

        if (designatorObj == SymbolTable.noObj) {
            report_error("Greska: ime " + designatorObj.getName() + " nije deklarisano! ", arrayDesignator);
        } else if (designatorObj.getType().getKind() != Struct.Array) {
            report_error("Greska: element " + designatorObj.getName() + " nije nizovnog tipa", arrayDesignator);
        } else if (!arrayDesignator.getExpr().struct.equals(SymbolTable.intType)) {
            report_error("Greska: vrednost kojom se pristupa elementu nizovnog tipa " + designatorObj.getName() + " nije int ", arrayDesignator);
        } else {
            // check formal param usage
            if (currentMethodFormalParams.contains(designatorObj)) {
                report_node("Pronadjeno koriscenje formalnog argumenta " + designatorObj.getName(), designatorObj, arrayDesignator);
            }

            arrayDesignator.obj = new Obj(Obj.Elem, designatorObj.getName() + "Elem", designatorObj.getType().getElemType());
            report_node("Pronadjen pristup nizu " + designatorObj.getName(), designatorObj, arrayDesignator);
        }

        // proper return of an errornous case
        if (arrayDesignator.obj.equals(SymbolTable.noObj)) {
            arrayDesignator.obj = new Obj(SymbolTable.noObj.getKind(), designatorObj.getName() + "Elem", SymbolTable.noType);
        }
    }

    public void visit(FieldDesignator designator) {
        Obj leftObj = designator.getDesignator().obj;

        switch (leftObj.getType().getKind()) {
            case Struct.None:
                report_error("Greska: ime " + designator.getName() + " nije deklarisano! ", designator);
                break;
            case Struct.Enum:
            case Struct.Class:
            case Struct.Interface:
                leftObj = this.getFieldObj(leftObj.getType(), designator.getName(), designator);

                if (leftObj != SymbolTable.noObj) {
                    report_node("Pronadjen pristup polju " + leftObj.getName(), leftObj, designator);
                }

                break;
            default:
                report_error("Greska: ime " + designator.getName() + " nije objekat/enumeracija! ", designator);
                break;
        }

        designator.obj = leftObj;
    }

    public void visit(ForPreCondition forPreCondition) {
        isInLoopStack.push(true);
    }

    public void visit(BreakStmt breakStmt) {
        if (isInLoopStack.empty()) {
            report_error("Greska: break naredba van petlje", breakStmt);
        }
    }

    public void visit(ContinueStmt continueStmt) {
        if (isInLoopStack.empty()) {
            report_error("Greska: continue naredba van petlje", continueStmt);
        }
    }

    public void visit(MatchedFor matchedFor) {
        isInLoopStack.pop();
    }

    public void visit(UnmatchedFor unmatchedFor) {
        isInLoopStack.pop();
    }

    public void visit(RelopCondFact relopCondFact) {
        Struct struct1 = relopCondFact.getExpr().struct;
        Struct struct2 = relopCondFact.getExpr1().struct;

        if (!struct1.compatibleWith(struct2)) {
            report_error("Greska: nekompatibilni tipovi u condition iskazu", relopCondFact);
        }

        boolean isClassExpr = struct1.getKind() == Struct.Class || struct2.getKind() == Struct.Class;
        boolean isAllowedClassExprRelop =
                relopCondFact.getRelop() instanceof IsEqualRelop ||
                        relopCondFact.getRelop() instanceof  NotEqualRelop;

        if (isClassExpr && !isAllowedClassExprRelop) {
            report_error("Greska: nedozovljena operacija komparacije", relopCondFact);
        }
    }

    public void visit(ExprCondFact exprCondFact) {
        if (!exprCondFact.getExpr().struct.equals(SymbolTable.boolType)) {
            report_error("Greska: tip condition iskaza nije bool", exprCondFact);
        }
    }

    public void visit(PrintStmt printStmt) {
        Struct struct = printStmt.getExpr().struct;

        if (!(struct == SymbolTable.intType ||
                struct == SymbolTable.boolType ||
                struct == SymbolTable.charType)) {
            report_error("Greska: nedozvoljen tip expr", printStmt);
        }
    }

    public void visit(ReadStmt readStmt) {
        Struct struct = readStmt.getDesignator().obj.getType();
        Obj obj = readStmt.getDesignator().obj;

        boolean isCorrectType = struct == SymbolTable.intType ||
                struct == SymbolTable.boolType ||
                struct == SymbolTable.charType;

        boolean isCorrectRole = obj.getKind() == Obj.Fld ||
                obj.getKind() == Obj.Var ||
                obj.getKind() == Obj.Elem;

        if (!isCorrectType || !isCorrectRole) {
            report_error("Greska: nedozvoljen tip designatora", readStmt);
        }
    }

    public boolean passed() {
        return !errorDetected;
    }
}
