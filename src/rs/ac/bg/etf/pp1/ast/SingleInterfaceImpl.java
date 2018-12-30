// generated with ast extension for cup
// version 0.8
// 30/11/2018 18:21:28


package rs.ac.bg.etf.pp1.ast;

public class SingleInterfaceImpl extends InterfaceImplList {

    private InterfaceImpl InterfaceImpl;

    public SingleInterfaceImpl (InterfaceImpl InterfaceImpl) {
        this.InterfaceImpl=InterfaceImpl;
        if(InterfaceImpl!=null) InterfaceImpl.setParent(this);
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
        if(InterfaceImpl!=null) InterfaceImpl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceImpl!=null) InterfaceImpl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceImpl!=null) InterfaceImpl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleInterfaceImpl(\n");

        if(InterfaceImpl!=null)
            buffer.append(InterfaceImpl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleInterfaceImpl]");
        return buffer.toString();
    }
}
