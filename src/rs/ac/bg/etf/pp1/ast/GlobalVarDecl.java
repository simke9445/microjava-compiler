// generated with ast extension for cup
// version 0.8
// 30/11/2018 22:51:48


package rs.ac.bg.etf.pp1.ast;

public class GlobalVarDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private GlobalVarDeclChain GlobalVarDeclChain;

    public GlobalVarDecl (Type Type, GlobalVarDeclChain GlobalVarDeclChain) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.GlobalVarDeclChain=GlobalVarDeclChain;
        if(GlobalVarDeclChain!=null) GlobalVarDeclChain.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public GlobalVarDeclChain getGlobalVarDeclChain() {
        return GlobalVarDeclChain;
    }

    public void setGlobalVarDeclChain(GlobalVarDeclChain GlobalVarDeclChain) {
        this.GlobalVarDeclChain=GlobalVarDeclChain;
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
        if(GlobalVarDeclChain!=null) GlobalVarDeclChain.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(GlobalVarDeclChain!=null) GlobalVarDeclChain.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(GlobalVarDeclChain!=null) GlobalVarDeclChain.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalVarDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(GlobalVarDeclChain!=null)
            buffer.append(GlobalVarDeclChain.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalVarDecl]");
        return buffer.toString();
    }
}
