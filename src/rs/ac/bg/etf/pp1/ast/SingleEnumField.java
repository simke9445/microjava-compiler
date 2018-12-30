// generated with ast extension for cup
// version 0.8
// 29/11/2018 3:12:51


package rs.ac.bg.etf.pp1.ast;

public class SingleEnumField extends EnumFieldList {

    private EnumField EnumField;

    public SingleEnumField (EnumField EnumField) {
        this.EnumField=EnumField;
        if(EnumField!=null) EnumField.setParent(this);
    }

    public EnumField getEnumField() {
        return EnumField;
    }

    public void setEnumField(EnumField EnumField) {
        this.EnumField=EnumField;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(EnumField!=null) EnumField.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumField!=null) EnumField.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumField!=null) EnumField.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleEnumField(\n");

        if(EnumField!=null)
            buffer.append(EnumField.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleEnumField]");
        return buffer.toString();
    }
}
