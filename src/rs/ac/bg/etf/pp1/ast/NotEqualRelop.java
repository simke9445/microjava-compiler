// generated with ast extension for cup
// version 0.8
// 30/11/2018 22:51:49


package rs.ac.bg.etf.pp1.ast;

public class NotEqualRelop extends Relop {

    public NotEqualRelop () {
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
        buffer.append("NotEqualRelop(\n");

        buffer.append(tab);
        buffer.append(") [NotEqualRelop]");
        return buffer.toString();
    }
}
