// generated with ast extension for cup
// version 0.8
// 25/11/2018 23:8:33


package rs.ac.bg.etf.pp1.ast;

public class EnumGlobalDecl extends GlobalDecl {

    private EnumDecl EnumDecl;

    public EnumGlobalDecl (EnumDecl EnumDecl) {
        this.EnumDecl=EnumDecl;
        if(EnumDecl!=null) EnumDecl.setParent(this);
    }

    public EnumDecl getEnumDecl() {
        return EnumDecl;
    }

    public void setEnumDecl(EnumDecl EnumDecl) {
        this.EnumDecl=EnumDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumDecl!=null) EnumDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumDecl!=null) EnumDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumDecl!=null) EnumDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumGlobalDecl(\n");

        if(EnumDecl!=null)
            buffer.append(EnumDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumGlobalDecl]");
        return buffer.toString();
    }
}
