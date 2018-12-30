// generated with ast extension for cup
// version 0.8
// 30/11/2018 22:51:48


package rs.ac.bg.etf.pp1.ast;

public class InterfaceMethodDeclarations extends InterfaceMethodDeclList {

    private InterfaceMethodDeclList InterfaceMethodDeclList;
    private MethodSig MethodSig;

    public InterfaceMethodDeclarations (InterfaceMethodDeclList InterfaceMethodDeclList, MethodSig MethodSig) {
        this.InterfaceMethodDeclList=InterfaceMethodDeclList;
        if(InterfaceMethodDeclList!=null) InterfaceMethodDeclList.setParent(this);
        this.MethodSig=MethodSig;
        if(MethodSig!=null) MethodSig.setParent(this);
    }

    public InterfaceMethodDeclList getInterfaceMethodDeclList() {
        return InterfaceMethodDeclList;
    }

    public void setInterfaceMethodDeclList(InterfaceMethodDeclList InterfaceMethodDeclList) {
        this.InterfaceMethodDeclList=InterfaceMethodDeclList;
    }

    public MethodSig getMethodSig() {
        return MethodSig;
    }

    public void setMethodSig(MethodSig MethodSig) {
        this.MethodSig=MethodSig;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceMethodDeclList!=null) InterfaceMethodDeclList.accept(visitor);
        if(MethodSig!=null) MethodSig.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceMethodDeclList!=null) InterfaceMethodDeclList.traverseTopDown(visitor);
        if(MethodSig!=null) MethodSig.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceMethodDeclList!=null) InterfaceMethodDeclList.traverseBottomUp(visitor);
        if(MethodSig!=null) MethodSig.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InterfaceMethodDeclarations(\n");

        if(InterfaceMethodDeclList!=null)
            buffer.append(InterfaceMethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodSig!=null)
            buffer.append(MethodSig.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceMethodDeclarations]");
        return buffer.toString();
    }
}
