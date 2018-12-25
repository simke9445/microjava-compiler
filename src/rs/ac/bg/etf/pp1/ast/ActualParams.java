// generated with ast extension for cup
// version 0.8
// 25/11/2018 23:8:33


package rs.ac.bg.etf.pp1.ast;

public class ActualParams extends ActualParamList {

    private ActualParamList ActualParamList;
    private ActualPar ActualPar;

    public ActualParams (ActualParamList ActualParamList, ActualPar ActualPar) {
        this.ActualParamList=ActualParamList;
        if(ActualParamList!=null) ActualParamList.setParent(this);
        this.ActualPar=ActualPar;
        if(ActualPar!=null) ActualPar.setParent(this);
    }

    public ActualParamList getActualParamList() {
        return ActualParamList;
    }

    public void setActualParamList(ActualParamList ActualParamList) {
        this.ActualParamList=ActualParamList;
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
        if(ActualParamList!=null) ActualParamList.accept(visitor);
        if(ActualPar!=null) ActualPar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActualParamList!=null) ActualParamList.traverseTopDown(visitor);
        if(ActualPar!=null) ActualPar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActualParamList!=null) ActualParamList.traverseBottomUp(visitor);
        if(ActualPar!=null) ActualPar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActualParams(\n");

        if(ActualParamList!=null)
            buffer.append(ActualParamList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActualPar!=null)
            buffer.append(ActualPar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActualParams]");
        return buffer.toString();
    }
}
