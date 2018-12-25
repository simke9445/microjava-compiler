// generated with ast extension for cup
// version 0.8
// 25/11/2018 23:8:33


package rs.ac.bg.etf.pp1.ast;

public class SingleConstDeclIdent extends ConstDeclChain {

    private ConstDeclIdent ConstDeclIdent;

    public SingleConstDeclIdent (ConstDeclIdent ConstDeclIdent) {
        this.ConstDeclIdent=ConstDeclIdent;
        if(ConstDeclIdent!=null) ConstDeclIdent.setParent(this);
    }

    public ConstDeclIdent getConstDeclIdent() {
        return ConstDeclIdent;
    }

    public void setConstDeclIdent(ConstDeclIdent ConstDeclIdent) {
        this.ConstDeclIdent=ConstDeclIdent;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclIdent!=null) ConstDeclIdent.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclIdent!=null) ConstDeclIdent.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclIdent!=null) ConstDeclIdent.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleConstDeclIdent(\n");

        if(ConstDeclIdent!=null)
            buffer.append(ConstDeclIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleConstDeclIdent]");
        return buffer.toString();
    }
}
