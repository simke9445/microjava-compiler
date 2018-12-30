// generated with ast extension for cup
// version 0.8
// 30/11/2018 22:51:48


package rs.ac.bg.etf.pp1.ast;

public class ClassImpl extends OptionalClassImpl {

    private InterfaceImplList InterfaceImplList;

    public ClassImpl (InterfaceImplList InterfaceImplList) {
        this.InterfaceImplList=InterfaceImplList;
        if(InterfaceImplList!=null) InterfaceImplList.setParent(this);
    }

    public InterfaceImplList getInterfaceImplList() {
        return InterfaceImplList;
    }

    public void setInterfaceImplList(InterfaceImplList InterfaceImplList) {
        this.InterfaceImplList=InterfaceImplList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceImplList!=null) InterfaceImplList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceImplList!=null) InterfaceImplList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceImplList!=null) InterfaceImplList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassImpl(\n");

        if(InterfaceImplList!=null)
            buffer.append(InterfaceImplList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassImpl]");
        return buffer.toString();
    }
}
