// generated with ast extension for cup
// version 0.8
// 30/11/2018 22:51:48


package rs.ac.bg.etf.pp1.ast;

public class ClassFieldDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private ClassFieldDeclChain ClassFieldDeclChain;

    public ClassFieldDecl (Type Type, ClassFieldDeclChain ClassFieldDeclChain) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ClassFieldDeclChain=ClassFieldDeclChain;
        if(ClassFieldDeclChain!=null) ClassFieldDeclChain.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public ClassFieldDeclChain getClassFieldDeclChain() {
        return ClassFieldDeclChain;
    }

    public void setClassFieldDeclChain(ClassFieldDeclChain ClassFieldDeclChain) {
        this.ClassFieldDeclChain=ClassFieldDeclChain;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(ClassFieldDeclChain!=null) ClassFieldDeclChain.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ClassFieldDeclChain!=null) ClassFieldDeclChain.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ClassFieldDeclChain!=null) ClassFieldDeclChain.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassFieldDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassFieldDeclChain!=null)
            buffer.append(ClassFieldDeclChain.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassFieldDecl]");
        return buffer.toString();
    }
}
