// generated with ast extension for cup
// version 0.8
// 30/11/2018 18:21:28


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclarationsChain extends ConstDeclChain {

    private ConstDeclChain ConstDeclChain;
    private ConstDeclIdent ConstDeclIdent;

    public ConstDeclarationsChain (ConstDeclChain ConstDeclChain, ConstDeclIdent ConstDeclIdent) {
        this.ConstDeclChain=ConstDeclChain;
        if(ConstDeclChain!=null) ConstDeclChain.setParent(this);
        this.ConstDeclIdent=ConstDeclIdent;
        if(ConstDeclIdent!=null) ConstDeclIdent.setParent(this);
    }

    public ConstDeclChain getConstDeclChain() {
        return ConstDeclChain;
    }

    public void setConstDeclChain(ConstDeclChain ConstDeclChain) {
        this.ConstDeclChain=ConstDeclChain;
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
        if(ConstDeclChain!=null) ConstDeclChain.accept(visitor);
        if(ConstDeclIdent!=null) ConstDeclIdent.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclChain!=null) ConstDeclChain.traverseTopDown(visitor);
        if(ConstDeclIdent!=null) ConstDeclIdent.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclChain!=null) ConstDeclChain.traverseBottomUp(visitor);
        if(ConstDeclIdent!=null) ConstDeclIdent.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclarationsChain(\n");

        if(ConstDeclChain!=null)
            buffer.append(ConstDeclChain.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclIdent!=null)
            buffer.append(ConstDeclIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclarationsChain]");
        return buffer.toString();
    }
}
