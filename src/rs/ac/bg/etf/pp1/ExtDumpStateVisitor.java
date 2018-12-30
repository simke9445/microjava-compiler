package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ExtDumpStateVisitor extends SymbolTableVisitor {

	protected StringBuilder output = new StringBuilder();
	protected final String indent = "   ";
	protected StringBuilder currentIndent = new StringBuilder();

	private boolean prettyPrint = false;

	protected void nextIndentationLevel() {
		currentIndent.append(indent);
	}

	protected void previousIndentationLevel() {
		if (currentIndent.length() > 0)
			currentIndent.setLength(currentIndent.length()-indent.length());
	}

	/* (non-Javadoc)
	 * @see rs.etf.pp1.symboltable.test.SymbolTableVisitor#visitObjNode(symboltable.Obj)
	 */
	@Override
	public void visitObjNode(Obj objToVisit) {
		//output.append("[");
		switch (objToVisit.getKind()) {
			case Obj.Con:  output.append("Con "); break;
			case Obj.Var:  output.append("Var "); break;
			case Obj.Type: output.append("Type "); break;
			case Obj.Meth: output.append("Meth "); break;
			case Obj.Fld:  output.append("Fld "); break;
			case Obj.Prog: output.append("Prog "); break;
		}

		output.append(objToVisit.getName());
		output.append(": ");

		if ((Obj.Var == objToVisit.getKind()) && "this".equalsIgnoreCase(objToVisit.getName()))
			output.append("");
		else
			objToVisit.getType().accept(this);

		output.append(", ");
		output.append(objToVisit.getAdr());
		output.append(", ");
		output.append(objToVisit.getLevel() + " ");



		if (prettyPrint) {
			if (objToVisit.getKind() == Obj.Prog || objToVisit.getKind() == Obj.Meth) {
				output.append("\n");
				nextIndentationLevel();
			}

			for (Obj o : objToVisit.getLocalSymbols()) {
				output.append(currentIndent.toString());
				o.accept(this);
				output.append("\n");
			}

			if (objToVisit.getKind() == Obj.Prog || objToVisit.getKind() == Obj.Meth)
				previousIndentationLevel();
		}

		//output.append("]");
	}

	/* (non-Javadoc)
	 * @see rs.etf.pp1.symboltable.test.SymbolTableVisitor#visitScopeNode(symboltable.Scope)
	 */
	@Override
	public void visitScopeNode(Scope scope) {
		for (Obj o : scope.values()) {
			o.accept(this);
			output.append("\n");
		}
	}

	/* (non-Javadoc)
	 * @see rs.etf.pp1.symboltable.test.SymbolTableVisitor#visitStructNode(symboltable.Struct)
	 */
	@Override
	public void visitStructNode(Struct structToVisit) {
		Map<Integer, String> structNameMap = new HashMap<Integer, String>(){{
			put(Struct.Class, "Class");
			put(Struct.Interface, "Interface");
			put(Struct.Enum, "Enum");
		}};

		Consumer<Integer> visitCompositeNode = (struct) -> {
			String structName = structNameMap.get(struct);
			output.append(structName + " [");
			if (prettyPrint) {
				output.append("\n");
				nextIndentationLevel();
				for (Obj obj : structToVisit.getMembers()) {
					output.append(currentIndent.toString());
					obj.accept(this);
					output.append("\n");
				}
				previousIndentationLevel();
				output.append(currentIndent.toString());
			}
			output.append("]");
		};

		switch (structToVisit.getKind()) {
			case Struct.None:
				output.append("notype");
				break;
			case Struct.Int:
				output.append("int");
				break;
			case Struct.Char:
				output.append("char");
				break;
			case Struct.Array:
				output.append("Arr of ");

				switch (structToVisit.getElemType().getKind()) {
					case Struct.None:
						output.append("notype");
						break;
					case Struct.Int:
						output.append("int");
						break;
					case Struct.Char:
						output.append("char");
						break;
					case Struct.Class:
						output.append("Class");
						break;
					case Struct.Interface:
						output.append("Interface");
						break;
					case Struct.Bool:
						output.append("bool");
						break;
				}
				break;
			case Struct.Bool:
				output.append("bool");
				break;
			case Struct.Class:
				visitCompositeNode.accept(Struct.Class);
				break;
			case Struct.Interface:
				visitCompositeNode.accept(Struct.Interface);
				break;
			case Struct.Enum:
				visitCompositeNode.accept(Struct.Enum);
				break;
		}

	}

	public String getOutput() {
		return output.toString();
	}


}
