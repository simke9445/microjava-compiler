// generated with ast extension for cup
// version 0.8
// 30/11/2018 22:51:48


package rs.ac.bg.etf.pp1.ast;

public class InterfaceImplementations extends InterfaceImplList {

    private InterfaceImplList InterfaceImplList;
    private InterfaceImpl InterfaceImpl;

    public InterfaceImplementations (InterfaceImplList InterfaceImplList, InterfaceImpl InterfaceImpl) {
        this.InterfaceImplList=InterfaceImplList;
        if(InterfaceImplList!=null) InterfaceImplList.setParent(this);
        this.InterfaceImpl=InterfaceImpl;
        if(InterfaceImpl!=null) InterfaceImpl.setParent(this);
    }

    public InterfaceImplList getInterfaceImplList() {
        return InterfaceImplList;
    }

    public void setInterfaceImplList(InterfaceImplList InterfaceImplList) {
        this.InterfaceImplList=InterfaceImplList;
    }

    public InterfaceImpl getInterfaceImpl() {
        return InterfaceImpl;
    }

    public void setInterfaceImpl(InterfaceImpl InterfaceImpl) {
        this.InterfaceImpl=InterfaceImpl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceImplList!=null) InterfaceImplList.accept(visitor);
        if(InterfaceImpl!=null) InterfaceImpl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceImplList!=null) InterfaceImplList.traverseTopDown(visitor);
        if(InterfaceImpl!=null) InterfaceImpl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceImplList!=null) InterfaceImplList.traverseBottomUp(visitor);
        if(InterfaceImpl!=null) InterfaceImpl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("InterfaceImplementations(\n");

        if(InterfaceImplList!=null)
            buffer.append(InterfaceImplList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(InterfaceImpl!=null)
            buffer.append(InterfaceImpl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceImplementations]");
        return buffer.toString();
    }
}
