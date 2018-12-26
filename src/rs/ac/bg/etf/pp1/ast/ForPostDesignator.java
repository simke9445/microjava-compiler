// generated with ast extension for cup
// version 0.8
// 26/11/2018 14:48:52


package rs.ac.bg.etf.pp1.ast;

public class ForPostDesignator implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public ForPostDesignator () {
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
        buffer.append("ForPostDesignator(\n");

        buffer.append(tab);
        buffer.append(") [ForPostDesignator]");
        return buffer.toString();
    }
}
