package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;

public class ExtStruct extends Struct {
    public ExtStruct(int kind) {
        super(kind);
    }

    public ExtStruct(int kind, Struct elemType) {
        super(kind, elemType);
    }

    public ExtStruct(int kind, SymbolDataStructure members) {
        super(kind, members);
    }

    public boolean equals(Struct other) {
        if (getKind() == Enum) {
            return other.getKind() == Enum && getNumberOfFields() == other.getNumberOfFields()
                    && Obj.equalsCompleteHash(getMembersTable(), other.getMembersTable());
        } else {
            return super.equals(other);
        }
    }
}
