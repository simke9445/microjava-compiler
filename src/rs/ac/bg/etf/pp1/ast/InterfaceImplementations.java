// generated with ast extension for cup
// version 0.8
// 29/11/2018 3:12:51


package rs.ac.bg.etf.pp1.ast;

public class InterfaceImplementations extends InterfaceImplList {

    private InterfaceImplList InterfaceImplList;
    private Type Type;

    public InterfaceImplementations (InterfaceImplList InterfaceImplList, Type Type) {
        this.InterfaceImplList=InterfaceImplList;
        if(InterfaceImplList!=null) InterfaceImplList.setParent(this);
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
    }

    public InterfaceImplList getInterfaceImplList() {
        return InterfaceImplList;
    }

    public void setInterfaceImplList(InterfaceImplList InterfaceImplList) {
        this.InterfaceImplList=InterfaceImplList;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(InterfaceImplList!=null) InterfaceImplList.accept(visitor);
        if(Type!=null) Type.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(InterfaceImplList!=null) InterfaceImplList.traverseTopDown(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(InterfaceImplList!=null) InterfaceImplList.traverseBottomUp(visitor);
        if(Type!=null) Type.traverseBottomUp(visitor);
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

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [InterfaceImplementations]");
        return buffer.toString();
    }
}
