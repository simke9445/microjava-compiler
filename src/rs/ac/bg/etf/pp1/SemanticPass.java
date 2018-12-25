package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

public class SemanticPass extends VisitorAdaptor {

	boolean errorDetected = false;
	int printCallCount = 0;
	Obj currentMethod = null;
	ArrayList<Obj> currentMethodFormalParams = new ArrayList<>();
	Stack<ArrayList<Struct>> actualParamsStructStack = new Stack<>();
	Type currentType = null;
	boolean returnFound = false;
	int nVars;
	Obj currentEnum = null;
	Obj prevEnumField = null;
	Obj currentDesignator = null;

	Logger log = Logger.getLogger(getClass());

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

	public void report_node(String message, Obj obj, SyntaxNode info) {
		ExtDumpStateVisitor visitor = new ExtDumpStateVisitor();
		visitor.visitObjNode(obj);

		this.report_info("[Detektovan Simbol] " + message + " | " + visitor.getOutput(), info);
	}

	private boolean assignableTo(Struct from, Struct to) {
		boolean isEnumAssignable = from.getKind() == Struct.Int && to.getKind() == Struct.Enum;

		if (isEnumAssignable) {
			// TODO: figure out how to allow enum to integer compatibility as a function argument
			// with keeping argument types strict

			return true;
		} else {
			return from.assignableTo(to);
		}
	}

	private void matchActualAndFormalParams(int formParamNum, ArrayList<Struct> currentActParamStructs, Obj obj, SyntaxNode node) {
		if (formParamNum == currentActParamStructs.size()) {
			// get local function params - first n are formal params
			ArrayList<Obj> methLocalParams = new ArrayList<>(obj.getLocalSymbols());

			// recursion - we need to get current scope arguments
			// instead of currentMethod reference
			if (obj.equals(currentMethod)) {
				methLocalParams = new ArrayList<>(SymbolTable.currentScope().values());
			}

			// we match first n meth local pars with n act pars
			for (int i = 0; i < currentActParamStructs.size(); i++) {
				Struct actParStruct = currentActParamStructs.get(i);
				Struct formParStruct = methLocalParams.get(i).getType();

				if (!this.assignableTo(actParStruct, formParStruct)) {
					report_error("U pozivu funkcije " + obj.getName() + " nisu u skladu proslednji stvarni argumenti po tipu ", node);
					break;
				}
			}
		} else {
			report_error("U pozivu funkcije " + obj.getName() + " nisu u skladu proslednji stvarni argumenti po tipu ", node);
		}

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

	private void insertVarToSymbolTable(String varName, Struct varStruct, SyntaxNode varNode) {
		Obj symTableVar = SymbolTable.currentScope().findSymbol(varName);

		// shadowing allowed so we check only in current scope
		if (symTableVar != null) {
			report_error("Redeklarisanje promenljive " + varName + " u tabeli simbola", varNode);
		} else {
			Obj varObj = SymbolTable.insert(Obj.Var, varName, varStruct);
			report_node("Deklarisana promenljiva "+ varName, varObj, varNode);
		}
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
			report_error("Redeklarisanje promenljive " + symTableConst.getName() + " u tabeli simbola", constDeclIdent);
		} else if (!constDeclIdent.getConstValue().obj.getType().equals(currentType.struct)) {
			report_error("Pogresan tip dodeljen konstanti " + constDeclIdent.getConstName(), constDeclIdent);
		} else {
			Obj constNode = SymbolTable.insert(Obj.Con, constDeclIdent.getConstName(), currentType.struct);
			constNode.setAdr(constDeclIdent.getConstValue().obj.getAdr());
			report_node("Deklarisana konstanta " + constDeclIdent.getConstName() + " tipa " + currentType.getTypeName(), constNode, constDeclIdent);
		}
	}

	public void visit(EnumName enumName) {
		Obj node = SymbolTable.find(enumName.getName());

		if (node != SymbolTable.noObj) {
			report_error("Redeklarisanje funkcije " + enumName.getName() + " u tabeli simbola", enumName);
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
			report_error("Redeklarisana vrednost " + enumField.getValue() + " nabrajanja " + enumField.getName(), enumField);
		} else {
			enumFields.insertKey(enumFieldObj);
			prevEnumField = enumFieldObj;
		}
	}

	public void visit(EnumDecl enumDecl) {
		report_node("Deklarisana enumeracija " + enumDecl.getEnumName().getName(), currentEnum, enumDecl);

		currentEnum = null;
		prevEnumField = null;
	}

	private void insertFormParamToSymbolTable(String formParamName, Struct formParamStruct, SyntaxNode formParamNode) {
		boolean containsFormParam = currentMethodFormalParams.stream()
				.anyMatch(x -> x.getName().equals(formParamName));

		if (containsFormParam) {
			report_error("Redeklarisan formalni argument " + formParamName, formParamNode);
		} else {
			Obj formParam = SymbolTable.insert(Obj.Var, formParamName, formParamStruct);
			currentMethodFormalParams.add(formParam);

			report_node("Deklarisan formalni argument " + formParamName, formParam, formParamNode);
		}
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
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola", type);
			type.struct = SymbolTable.noType;
		}
		else {
			if (Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();

				// cast enum type declarations to int
				if (type.struct.getKind() == Struct.Enum) {
					type.struct = SymbolTable.intType;
				}
			}
			else {
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip ", type);
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
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName() + " nema return iskaz!", null);
		}

		// always bound to an openScope caller
		// i.e. methName opens the scope for function variable
		// declarations, and methDecl closes it as it is the
		// last swap in the state machine.
		SymbolTable.chainLocalSymbols(currentMethod);
		SymbolTable.closeScope();

		returnFound = false;
		currentMethod = null;
		currentMethodFormalParams.clear();
	}

	public void visit(MethodSig pars) {
		currentMethod.setLevel(currentMethodFormalParams.size());

		pars.obj = currentMethod;
	}

	public void visit(RetTypeMethodTypeName methodTypeName) {
		Obj meth = SymbolTable.find(methodTypeName.getMethName());

		if (meth != SymbolTable.noObj) {
			report_error("Redeklarisanje funkcije " + methodTypeName.getMethName() + " u tabeli simbola", methodTypeName);
		} else {
			currentMethod = SymbolTable.insert(Obj.Meth, methodTypeName.getMethName(), methodTypeName.getType().struct);
			methodTypeName.obj = currentMethod;
			SymbolTable.openScope();
			report_node("Deklaracija funkcije " + methodTypeName.getMethName(), currentMethod, methodTypeName);
		}
	}

	public void visit(VoidMethodTypeName methodTypeName) {
		// TODO: check if void main method is defined
		// TODO: count methods in the program
		// TODO: count global variables
		// TODO: count global constants

		Obj meth = SymbolTable.find(methodTypeName.getMethName());

		if (meth != SymbolTable.noObj) {
			report_error("Redeklarisanje funkcije " + methodTypeName.getMethName() + " u tabeli simbola", methodTypeName);
		} else {
			currentMethod = SymbolTable.insert(Obj.Meth, methodTypeName.getMethName(), SymbolTable.noType);
			methodTypeName.obj = currentMethod;
			SymbolTable.openScope();
			report_node("Deklaracija funkcije " + methodTypeName.getMethName(), currentMethod, methodTypeName);
		}
	}

	public void visit(Assignment assignment) {
		if (!this.assignableTo(assignment.getExpr().struct, assignment.getDesignator().obj.getType()))
			report_error("Greska na liniji " + assignment.getLine() + " : " + " nekompatibilni tipovi u dodeli vrednosti ", null);
	}

	public void visit(ProcCall procCall){
		Obj func = procCall.getDesignator().obj;

		if (Obj.Meth == func.getKind()) {
			int formParamNum = func.getLevel();
			ArrayList<Struct> currentActParamStructs = actualParamsStructStack.pop();

			this.matchActualAndFormalParams(formParamNum, currentActParamStructs, func, procCall);

			report_node("Pronadjen poziv funkcije " + func.getName(), func, procCall);
		} else {
			report_error("Greska na liniji " + procCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
		}
	}

	public void visit(ReturnExpr returnExpr){
		returnFound = true;
		Struct currMethType = currentMethod.getType();

		if (!currMethType.compatibleWith(returnExpr.getExpr().struct)) {
			report_error("Greska na liniji " + returnExpr.getLine() + " : " + "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
		}
	}

	public void visit(ReturnNoExpr returnExpr){
		returnFound = true;
		Struct currMethType = currentMethod.getType();

		// check for void scenario
		if (currMethType != SymbolTable.noType) {
			report_error("Greska na liniji " + returnExpr.getLine() + " : " + "praznu return naredbu moze imati samo void funkcija, za razliku od trenutne " + currentMethod.getName(), null);
		}
	}

	public void visit(AddExpr addExpr) {
		Struct te = addExpr.getExpr().struct;
		Struct t = addExpr.getTerm().struct;

		boolean isIntAdd = te.equals(t) && te == SymbolTable.intType;

		if (isIntAdd)
			addExpr.struct = te;
		else {
			report_error("Greska na liniji "+ addExpr.getLine()+" : nekompatibilni tipovi u izrazu za sabiranje.", null);
			addExpr.struct = SymbolTable.noType;
		}
	}

	public void visit(NegExpr negExpr) {
		Struct termStruct = negExpr.getTerm().struct;

		if (termStruct.equals(SymbolTable.intType)) {
			negExpr.struct = termStruct;
		} else {
			report_error("Greska na liniji "+ negExpr.getLine()+" : nekompatibilan tip izraza za negaciju.", null);
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
			report_error("Greska na liniji "+ mulTerm.getLine()+" : nekompatibilni tipovi u izrazu za mnozenje.", null);
			mulTerm.struct = SymbolTable.noType;
		}
	}

	public void visit(BoolConst cnst){
		cnst.obj = new Obj(Obj.Con, "", SymbolTable.boolType, Boolean.valueOf(cnst.getB1()) ? 1 : 0, Obj.NO_VALUE);
	}

	public void visit(IntConst cnst){
		cnst.obj = new Obj(Obj.Con, "", SymbolTable.intType, cnst.getN1(), Obj.NO_VALUE);
	}

	public void visit(CharConst cnst){
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
		alloc.struct = alloc.getType().struct;
	}

	public void visit(ArrayAlloc arrayAlloc) {
		Struct struct = new Struct(Struct.Array, SymbolTable.noType);

		if (!arrayAlloc.getExpr().struct.equals(SymbolTable.intType)) {
			report_error("Vrednost kojom se alocira niz nije int ", arrayAlloc);
		} else {
			struct = new Struct(Struct.Array, arrayAlloc.getType().struct);
		}

		arrayAlloc.struct = struct;
	}

	public void visit(FuncCall funcCall) {
		Obj func = funcCall.getDesignator().obj;

		if (Obj.Meth == func.getKind()) {
			int formParamNum = func.getLevel();
			ArrayList<Struct> currentActParamStructs = actualParamsStructStack.pop();

			this.matchActualAndFormalParams(formParamNum, currentActParamStructs, func, funcCall);

			report_node("Pronadjen poziv funkcije " + func.getName(), func, funcCall);
			funcCall.struct = func.getType();
		}
		else {
			report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
			funcCall.struct = SymbolTable.noType;
		}
	}

	public void visit(IdentDesignator designator) {
		Obj obj = SymbolTable.find(designator.getName());

		if (obj == SymbolTable.noObj) {
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+designator.getName()+" nije deklarisano! ", null);
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
			report_error("Greska na liniji " + arrayDesignator.getLine()+ " : ime "+ designatorObj.getName()+" nije deklarisano! ", null);
		} else if (designatorObj.getType().getKind() != Struct.Array) {
			report_error("Element " + designatorObj.getName() + " nije nizovnog tipa", arrayDesignator);
		} else if (!arrayDesignator.getExpr().struct.equals(SymbolTable.intType)) {
			report_error("Vrednost kojom se pristupa elementu nizovnog tipa " + designatorObj.getName() +" nije int ", arrayDesignator);
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
		Obj obj = SymbolTable.find(((IdentDesignator)designator.getDesignator()).getName());

		if (obj == SymbolTable.noObj) {
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+ designator.getName() +" nije deklarisano! ", null);
		} else {
			// check if it is an enum
			// TODO: refactor this in case we add classes
			if (obj.getType().getKind() != Struct.Enum) {
				report_error("Greska na liniji " + designator.getLine()+ " : ime "+ designator.getName() +" nije enumeracija! ", null);
			} else {
				String accessName = designator.getName();
				Obj fieldObj = obj.getType().getMembersTable().searchKey(accessName);

				if (fieldObj != null) {
					obj = fieldObj;
				} else {
					report_error("Greska na liniji " + designator.getLine()+ " : enumeracija "+ designator.getName() +" ne sadrzi ime " + accessName, null);
				}
			}
		}

		designator.obj = obj;
	}

	public boolean passed() {
		return !errorDetected;
	}
}
