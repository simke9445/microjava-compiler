// generated with ast extension for cup
// version 0.8
// 30/11/2018 22:51:48


package rs.ac.bg.etf.pp1.ast;

public class ClassFieldDeclarationChain extends ClassFieldDeclChain {

    private ClassFieldDeclChain ClassFieldDeclChain;
    private ClassFieldDeclIdent ClassFieldDeclIdent;

    public ClassFieldDeclarationChain (ClassFieldDeclChain ClassFieldDeclChain, ClassFieldDeclIdent ClassFieldDeclIdent) {
        this.ClassFieldDeclChain=ClassFieldDeclChain;
        if(ClassFieldDeclChain!=null) ClassFieldDeclChain.setParent(this);
        this.ClassFieldDeclIdent=ClassFieldDeclIdent;
        if(ClassFieldDeclIdent!=null) ClassFieldDeclIdent.setParent(this);
    }

    public ClassFieldDeclChain getClassFieldDeclChain() {
        return ClassFieldDeclChain;
    }

    public void setClassFieldDeclChain(ClassFieldDeclChain ClassFieldDeclChain) {
        this.ClassFieldDeclChain=ClassFieldDeclChain;
    }

    public ClassFieldDeclIdent getClassFieldDeclIdent() {
        return ClassFieldDeclIdent;
    }

    public void setClassFieldDeclIdent(ClassFieldDeclIdent ClassFieldDeclIdent) {
        this.ClassFieldDeclIdent=ClassFieldDeclIdent;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassFieldDeclChain!=null) ClassFieldDeclChain.accept(visitor);
        if(ClassFieldDeclIdent!=null) ClassFieldDeclIdent.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassFieldDeclChain!=null) ClassFieldDeclChain.traverseTopDown(visitor);
        if(ClassFieldDeclIdent!=null) ClassFieldDeclIdent.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassFieldDeclChain!=null) ClassFieldDeclChain.traverseBottomUp(visitor);
        if(ClassFieldDeclIdent!=null) ClassFieldDeclIdent.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassFieldDeclarationChain(\n");

        if(ClassFieldDeclChain!=null)
            buffer.append(ClassFieldDeclChain.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassFieldDeclIdent!=null)
            buffer.append(ClassFieldDeclIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassFieldDeclarationChain]");
        return buffer.toString();
    }
}
