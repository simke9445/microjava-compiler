// generated with ast extension for cup
// version 0.8
// 29/11/2018 3:12:51


package rs.ac.bg.etf.pp1.ast;

public class ClassDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private ClassName ClassName;
    private OptionalClassExt OptionalClassExt;
    private OptionalClassImpl OptionalClassImpl;
    private VarDeclList VarDeclList;
    private OptionalClassMethDecl OptionalClassMethDecl;

    public ClassDecl (ClassName ClassName, OptionalClassExt OptionalClassExt, OptionalClassImpl OptionalClassImpl, VarDeclList VarDeclList, OptionalClassMethDecl OptionalClassMethDecl) {
        this.ClassName=ClassName;
        if(ClassName!=null) ClassName.setParent(this);
        this.OptionalClassExt=OptionalClassExt;
        if(OptionalClassExt!=null) OptionalClassExt.setParent(this);
        this.OptionalClassImpl=OptionalClassImpl;
        if(OptionalClassImpl!=null) OptionalClassImpl.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.OptionalClassMethDecl=OptionalClassMethDecl;
        if(OptionalClassMethDecl!=null) OptionalClassMethDecl.setParent(this);
    }

    public ClassName getClassName() {
        return ClassName;
    }

    public void setClassName(ClassName ClassName) {
        this.ClassName=ClassName;
    }

    public OptionalClassExt getOptionalClassExt() {
        return OptionalClassExt;
    }

    public void setOptionalClassExt(OptionalClassExt OptionalClassExt) {
        this.OptionalClassExt=OptionalClassExt;
    }

    public OptionalClassImpl getOptionalClassImpl() {
        return OptionalClassImpl;
    }

    public void setOptionalClassImpl(OptionalClassImpl OptionalClassImpl) {
        this.OptionalClassImpl=OptionalClassImpl;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public OptionalClassMethDecl getOptionalClassMethDecl() {
        return OptionalClassMethDecl;
    }

    public void setOptionalClassMethDecl(OptionalClassMethDecl OptionalClassMethDecl) {
        this.OptionalClassMethDecl=OptionalClassMethDecl;
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
        if(ClassName!=null) ClassName.accept(visitor);
        if(OptionalClassExt!=null) OptionalClassExt.accept(visitor);
        if(OptionalClassImpl!=null) OptionalClassImpl.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(OptionalClassMethDecl!=null) OptionalClassMethDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassName!=null) ClassName.traverseTopDown(visitor);
        if(OptionalClassExt!=null) OptionalClassExt.traverseTopDown(visitor);
        if(OptionalClassImpl!=null) OptionalClassImpl.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(OptionalClassMethDecl!=null) OptionalClassMethDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassName!=null) ClassName.traverseBottomUp(visitor);
        if(OptionalClassExt!=null) OptionalClassExt.traverseBottomUp(visitor);
        if(OptionalClassImpl!=null) OptionalClassImpl.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(OptionalClassMethDecl!=null) OptionalClassMethDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDecl(\n");

        if(ClassName!=null)
            buffer.append(ClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalClassExt!=null)
            buffer.append(OptionalClassExt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalClassImpl!=null)
            buffer.append(OptionalClassImpl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalClassMethDecl!=null)
            buffer.append(OptionalClassMethDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDecl]");
        return buffer.toString();
    }
}
