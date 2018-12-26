// generated with ast extension for cup
// version 0.8
// 26/11/2018 14:48:51


package rs.ac.bg.etf.pp1.ast;

public class EnumFields extends EnumFieldList {

    private EnumFieldList EnumFieldList;
    private EnumField EnumField;

    public EnumFields (EnumFieldList EnumFieldList, EnumField EnumField) {
        this.EnumFieldList=EnumFieldList;
        if(EnumFieldList!=null) EnumFieldList.setParent(this);
        this.EnumField=EnumField;
        if(EnumField!=null) EnumField.setParent(this);
    }

    public EnumFieldList getEnumFieldList() {
        return EnumFieldList;
    }

    public void setEnumFieldList(EnumFieldList EnumFieldList) {
        this.EnumFieldList=EnumFieldList;
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
        if(EnumFieldList!=null) EnumFieldList.accept(visitor);
        if(EnumField!=null) EnumField.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(EnumFieldList!=null) EnumFieldList.traverseTopDown(visitor);
        if(EnumField!=null) EnumField.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(EnumFieldList!=null) EnumFieldList.traverseBottomUp(visitor);
        if(EnumField!=null) EnumField.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EnumFields(\n");

        if(EnumFieldList!=null)
            buffer.append(EnumFieldList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumField!=null)
            buffer.append(EnumField.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [EnumFields]");
        return buffer.toString();
    }
}
