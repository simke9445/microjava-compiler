// generated with ast extension for cup
// version 0.8
// 29/11/2018 3:12:52


package rs.ac.bg.etf.pp1.ast;

public class NoOptCondition extends OptionalCondition {

    public NoOptCondition () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoOptCondition(\n");

        buffer.append(tab);
        buffer.append(") [NoOptCondition]");
        return buffer.toString();
    }
}
