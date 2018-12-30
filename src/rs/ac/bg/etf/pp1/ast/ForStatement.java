// generated with ast extension for cup
// version 0.8
// 30/11/2018 18:21:28


package rs.ac.bg.etf.pp1.ast;

public class ForStatement implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private OptionalDesignatorStatement OptionalDesignatorStatement;
    private ForPreCondition ForPreCondition;
    private OptionalCondition OptionalCondition;
    private ForPreDesignator ForPreDesignator;
    private OptionalDesignatorStatement OptionalDesignatorStatement1;
    private ForPostDesignator ForPostDesignator;

    public ForStatement (OptionalDesignatorStatement OptionalDesignatorStatement, ForPreCondition ForPreCondition, OptionalCondition OptionalCondition, ForPreDesignator ForPreDesignator, OptionalDesignatorStatement OptionalDesignatorStatement1, ForPostDesignator ForPostDesignator) {
        this.OptionalDesignatorStatement=OptionalDesignatorStatement;
        if(OptionalDesignatorStatement!=null) OptionalDesignatorStatement.setParent(this);
        this.ForPreCondition=ForPreCondition;
        if(ForPreCondition!=null) ForPreCondition.setParent(this);
        this.OptionalCondition=OptionalCondition;
        if(OptionalCondition!=null) OptionalCondition.setParent(this);
        this.ForPreDesignator=ForPreDesignator;
        if(ForPreDesignator!=null) ForPreDesignator.setParent(this);
        this.OptionalDesignatorStatement1=OptionalDesignatorStatement1;
        if(OptionalDesignatorStatement1!=null) OptionalDesignatorStatement1.setParent(this);
        this.ForPostDesignator=ForPostDesignator;
        if(ForPostDesignator!=null) ForPostDesignator.setParent(this);
    }

    public OptionalDesignatorStatement getOptionalDesignatorStatement() {
        return OptionalDesignatorStatement;
    }

    public void setOptionalDesignatorStatement(OptionalDesignatorStatement OptionalDesignatorStatement) {
        this.OptionalDesignatorStatement=OptionalDesignatorStatement;
    }

    public ForPreCondition getForPreCondition() {
        return ForPreCondition;
    }

    public void setForPreCondition(ForPreCondition ForPreCondition) {
        this.ForPreCondition=ForPreCondition;
    }

    public OptionalCondition getOptionalCondition() {
        return OptionalCondition;
    }

    public void setOptionalCondition(OptionalCondition OptionalCondition) {
        this.OptionalCondition=OptionalCondition;
    }

    public ForPreDesignator getForPreDesignator() {
        return ForPreDesignator;
    }

    public void setForPreDesignator(ForPreDesignator ForPreDesignator) {
        this.ForPreDesignator=ForPreDesignator;
    }

    public OptionalDesignatorStatement getOptionalDesignatorStatement1() {
        return OptionalDesignatorStatement1;
    }

    public void setOptionalDesignatorStatement1(OptionalDesignatorStatement OptionalDesignatorStatement1) {
        this.OptionalDesignatorStatement1=OptionalDesignatorStatement1;
    }

    public ForPostDesignator getForPostDesignator() {
        return ForPostDesignator;
    }

    public void setForPostDesignator(ForPostDesignator ForPostDesignator) {
        this.ForPostDesignator=ForPostDesignator;
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
        if(OptionalDesignatorStatement!=null) OptionalDesignatorStatement.accept(visitor);
        if(ForPreCondition!=null) ForPreCondition.accept(visitor);
        if(OptionalCondition!=null) OptionalCondition.accept(visitor);
        if(ForPreDesignator!=null) ForPreDesignator.accept(visitor);
        if(OptionalDesignatorStatement1!=null) OptionalDesignatorStatement1.accept(visitor);
        if(ForPostDesignator!=null) ForPostDesignator.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalDesignatorStatement!=null) OptionalDesignatorStatement.traverseTopDown(visitor);
        if(ForPreCondition!=null) ForPreCondition.traverseTopDown(visitor);
        if(OptionalCondition!=null) OptionalCondition.traverseTopDown(visitor);
        if(ForPreDesignator!=null) ForPreDesignator.traverseTopDown(visitor);
        if(OptionalDesignatorStatement1!=null) OptionalDesignatorStatement1.traverseTopDown(visitor);
        if(ForPostDesignator!=null) ForPostDesignator.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalDesignatorStatement!=null) OptionalDesignatorStatement.traverseBottomUp(visitor);
        if(ForPreCondition!=null) ForPreCondition.traverseBottomUp(visitor);
        if(OptionalCondition!=null) OptionalCondition.traverseBottomUp(visitor);
        if(ForPreDesignator!=null) ForPreDesignator.traverseBottomUp(visitor);
        if(OptionalDesignatorStatement1!=null) OptionalDesignatorStatement1.traverseBottomUp(visitor);
        if(ForPostDesignator!=null) ForPostDesignator.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForStatement(\n");

        if(OptionalDesignatorStatement!=null)
            buffer.append(OptionalDesignatorStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForPreCondition!=null)
            buffer.append(ForPreCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalCondition!=null)
            buffer.append(OptionalCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForPreDesignator!=null)
            buffer.append(ForPreDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalDesignatorStatement1!=null)
            buffer.append(OptionalDesignatorStatement1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForPostDesignator!=null)
            buffer.append(ForPostDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForStatement]");
        return buffer.toString();
    }
}
