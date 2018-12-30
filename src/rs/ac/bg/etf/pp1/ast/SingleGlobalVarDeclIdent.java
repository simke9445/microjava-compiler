// generated with ast extension for cup
// version 0.8
// 30/11/2018 22:51:48


package rs.ac.bg.etf.pp1.ast;

public class SingleGlobalVarDeclIdent extends GlobalVarDeclChain {

    private GlobalVarDeclIdent GlobalVarDeclIdent;

    public SingleGlobalVarDeclIdent (GlobalVarDeclIdent GlobalVarDeclIdent) {
        this.GlobalVarDeclIdent=GlobalVarDeclIdent;
        if(GlobalVarDeclIdent!=null) GlobalVarDeclIdent.setParent(this);
    }

    public GlobalVarDeclIdent getGlobalVarDeclIdent() {
        return GlobalVarDeclIdent;
    }

    public void setGlobalVarDeclIdent(GlobalVarDeclIdent GlobalVarDeclIdent) {
        this.GlobalVarDeclIdent=GlobalVarDeclIdent;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(GlobalVarDeclIdent!=null) GlobalVarDeclIdent.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GlobalVarDeclIdent!=null) GlobalVarDeclIdent.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GlobalVarDeclIdent!=null) GlobalVarDeclIdent.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleGlobalVarDeclIdent(\n");

        if(GlobalVarDeclIdent!=null)
            buffer.append(GlobalVarDeclIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleGlobalVarDeclIdent]");
        return buffer.toString();
    }
}
