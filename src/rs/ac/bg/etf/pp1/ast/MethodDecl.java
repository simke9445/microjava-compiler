// generated with ast extension for cup
// version 0.8
// 30/11/2018 22:51:48


package rs.ac.bg.etf.pp1.ast;

public class MethodDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private MethodSig MethodSig;
    private VarDeclList VarDeclList;
    private MethodBeforeFirstStmt MethodBeforeFirstStmt;
    private StatementList StatementList;

    public MethodDecl (MethodSig MethodSig, VarDeclList VarDeclList, MethodBeforeFirstStmt MethodBeforeFirstStmt, StatementList StatementList) {
        this.MethodSig=MethodSig;
        if(MethodSig!=null) MethodSig.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.MethodBeforeFirstStmt=MethodBeforeFirstStmt;
        if(MethodBeforeFirstStmt!=null) MethodBeforeFirstStmt.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public MethodSig getMethodSig() {
        return MethodSig;
    }

    public void setMethodSig(MethodSig MethodSig) {
        this.MethodSig=MethodSig;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public MethodBeforeFirstStmt getMethodBeforeFirstStmt() {
        return MethodBeforeFirstStmt;
    }

    public void setMethodBeforeFirstStmt(MethodBeforeFirstStmt MethodBeforeFirstStmt) {
        this.MethodBeforeFirstStmt=MethodBeforeFirstStmt;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
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
        if(MethodSig!=null) MethodSig.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(MethodBeforeFirstStmt!=null) MethodBeforeFirstStmt.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodSig!=null) MethodSig.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(MethodBeforeFirstStmt!=null) MethodBeforeFirstStmt.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodSig!=null) MethodSig.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(MethodBeforeFirstStmt!=null) MethodBeforeFirstStmt.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDecl(\n");

        if(MethodSig!=null)
            buffer.append(MethodSig.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodBeforeFirstStmt!=null)
            buffer.append(MethodBeforeFirstStmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDecl]");
        return buffer.toString();
    }
}
