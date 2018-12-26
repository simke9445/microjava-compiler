// generated with ast extension for cup
// version 0.8
// 26/11/2018 14:48:52


package rs.ac.bg.etf.pp1.ast;

public class ActualParam extends ActualParamList {

    private ActualPar ActualPar;

    public ActualParam (ActualPar ActualPar) {
        this.ActualPar=ActualPar;
        if(ActualPar!=null) ActualPar.setParent(this);
    }

    public ActualPar getActualPar() {
        return ActualPar;
    }

    public void setActualPar(ActualPar ActualPar) {
        this.ActualPar=ActualPar;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActualPar!=null) ActualPar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActualPar!=null) ActualPar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActualPar!=null) ActualPar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActualParam(\n");

        if(ActualPar!=null)
            buffer.append(ActualPar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActualParam]");
        return buffer.toString();
    }
}
