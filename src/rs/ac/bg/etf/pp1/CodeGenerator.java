package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	boolean errorDetected = false;
	Logger log = Logger.getLogger(getClass());

	private int mainPc;

	Stack<Integer> addop = new Stack<>();
	Stack<Integer> mulop = new Stack<>();
	Stack<Integer> relop = new Stack<>();

	public int getMainPc() {
		return mainPc;
	}

	public CodeGenerator() {
		// TODO: add predefined functions code:
		// 		ord - convert char to int
		// 		len - Code.arraylength
		// 		chr - convert int to char
	}

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}

	public void visit(PrintStmt print) {
		Struct exprStruct = print.getExpr().struct;

		if (exprStruct == SymbolTable.charType || exprStruct == SymbolTable.boolType) {
			Code.loadConst(1);
			Code.put(Code.bprint);
		} else if (exprStruct == SymbolTable.intType) {
			// TODO: check if we can print 4 bytes here instead (1 word size)
			Code.loadConst(5);
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

//	public void visit(FieldDesignator field) {
//		// TODO: refactor this for classes
//		if (field.getDesignator().obj.getType().getKind() == Struct.Enum) {
//			Obj value = field.getDesignator().obj.getType().getMembersTable().searchKey(field.getName());
//
//			Code.load(value);
//		}
//	}

	public void visit(PreArrayIndex preArrayIndex) {
		// load the array address to estack, followed by index load after expr is resolved
		Obj array = ((ArrayDesignator)preArrayIndex.getParent()).getDesignator().obj;
		Code.load(array);
	}

	public void visit(ArrayAlloc arrayAlloc) {
		Code.put(Code.newarray);
		Code.put(arrayAlloc.getType().struct.equals(SymbolTable.intType) ? 1 : 0);
	}

	public void visit(Assignment assig) {
		Code.store(assig.getDesignator().obj);
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

		// TODO: check if this is the right time to do setAdr
		// set adr of first instruction
		meth.setAdr(Code.pc);

		if ("main".equalsIgnoreCase(meth.getName())) {
			mainPc = Code.pc;
		}

		Code.put(Code.enter);
		// set the number of formal args
		Code.put(meth.getLevel());
		// set the number of locals
		Code.put(meth.getLocalSymbols().size());
	}

	public void visit(MethodDecl methDecl) {
		if (methDecl.getMethodSig().obj.getType().equals(SymbolTable.noType)) {
			Code.put(Code.exit);
			Code.put(Code.return_);
		} else {
			// if we got to here, it means no return statement has occured
			// thus we generate a runtime error
			Code.put(Code.trap);
			Code.put(1);
		}

	}

	public void visit(ReturnExpr returnExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(ReturnNoExpr returnNoExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(ProcCall procCall) {
		// relative address
		int destAdr = procCall.getDesignator().obj.getAdr() - Code.pc;

		Code.put(Code.call);
		Code.put2(destAdr);

		// other than void, last value on stack is the return value of the function call,
		// and as we're not assigning the result, we should just remove it
		if (!procCall.getDesignator().obj.getType().equals(SymbolTable.noType)) {
			Code.put(Code.pop);
		}
	}

	public void visit(FuncCall funcCall) {
		// relative address
		int destAdr = funcCall.getDesignator().obj.getAdr() - Code.pc;

		Code.put(Code.call);
		Code.put2(destAdr);
	}

	private List<Integer> continueAdrs = new ArrayList<>();
	private List<Integer> breakAdrs = new ArrayList<>();

	private Integer preCondAdr;
	private Integer preCondFixup;
	private Integer preDesignAdr;
	private Integer preDesignFixup;

	// TODO: Support nesting of for loops

	public void visit(ForPreCondition forPreCondition) {
		preCondAdr = Code.pc;
	}

	public void visit(ForPreDesignator forPreDesignator) {
		// false jump to loop end
		if (relop.empty()) {
			Code.putJump(0);
		} else {
			Code.putFalseJump(relop.pop(), 0);
		}

		preCondFixup = Code.pc - 2;

		// jump to after designator statement - loop start
		Code.putJump(0);
		preDesignFixup = Code.pc - 2;
		preDesignAdr = Code.pc;
	}

	public void visit(ForPostDesignator forPostDesignator) {
		// fixup designator statement skip
		Code.putJump(preCondAdr);
		Code.fixup(preDesignFixup);
	}

	public void visit(ForStatement forStatement) {
		// fixup continue jumps
		continueAdrs.forEach(adr -> Code.fixup(adr));
		continueAdrs.clear();

		Code.putJump(preDesignAdr);

		// fixup condition jump
		Code.fixup(preCondFixup);

		// fixup break jumps - shifted
		breakAdrs.forEach(adr -> Code.fixup(adr));
		breakAdrs.clear();
	}

	public void visit(BreakStmt breakStmt) {
		Code.putJump(0);
		breakAdrs.add(Code.pc - 2);
	}

	public void visit(ContinueStmt continueStmt) {
		Code.putJump(0);
		continueAdrs.add(Code.pc - 2);
	}

	// TODO: Add if statements
	// TODO: Support nesting of if statements

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

	public void visit(RelopCondFact relopCondFact) {
		Code.put(relop.pop());
	}

	// TODO: implement AND and OR terms

	public void visit(AndCondTerm andCondTerm) {
//		Code.put(Code.and)
	}

	public void visit(OrCondition orCondition) {
//		Code.put(Code.or)
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
}
