// generated with ast extension for cup
// version 0.8
// 30/11/2018 18:21:29


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(Unmatched Unmatched);
    public void visit(Mulop Mulop);
    public void visit(InterfaceMethodDeclList InterfaceMethodDeclList);
    public void visit(OptionalClassImpl OptionalClassImpl);
    public void visit(Matched Matched);
    public void visit(OptionalClassMethDecl OptionalClassMethDecl);
    public void visit(Relop Relop);
    public void visit(ConditionList ConditionList);
    public void visit(FormalParamDecl FormalParamDecl);
    public void visit(InterfaceImplList InterfaceImplList);
    public void visit(StatementList StatementList);
    public void visit(Addop Addop);
    public void visit(EnumFieldList EnumFieldList);
    public void visit(Factor Factor);
    public void visit(CondTerm CondTerm);
    public void visit(Designator Designator);
    public void visit(Term Term);
    public void visit(VarDeclIdent VarDeclIdent);
    public void visit(ConstValue ConstValue);
    public void visit(EnumField EnumField);
    public void visit(IfCondition IfCondition);
    public void visit(OptionalClassExt OptionalClassExt);
    public void visit(ActualParamList ActualParamList);
    public void visit(GlobalDeclList GlobalDeclList);
    public void visit(VarDeclList VarDeclList);
    public void visit(FormalParamList FormalParamList);
    public void visit(Expr Expr);
    public void visit(MethodTypeName MethodTypeName);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(ActualPars ActualPars);
    public void visit(OptionalCondition OptionalCondition);
    public void visit(ConstDeclChain ConstDeclChain);
    public void visit(OptionalDesignatorStatement OptionalDesignatorStatement);
    public void visit(Statement Statement);
    public void visit(CondFact CondFact);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(VarDeclChain VarDeclChain);
    public void visit(GlobalDecl GlobalDecl);
    public void visit(FormPars FormPars);
    public void visit(LesserOrEqualRelop LesserOrEqualRelop);
    public void visit(GreaterOrEqualRelop GreaterOrEqualRelop);
    public void visit(LesserRelop LesserRelop);
    public void visit(GreaterRelop GreaterRelop);
    public void visit(NotEqualRelop NotEqualRelop);
    public void visit(IsEqualRelop IsEqualRelop);
    public void visit(ModMulOp ModMulOp);
    public void visit(DivMulop DivMulop);
    public void visit(MulMulop MulMulop);
    public void visit(SubAddop SubAddop);
    public void visit(AddAddop AddAddop);
    public void visit(ExprCondFact ExprCondFact);
    public void visit(RelopCondFact RelopCondFact);
    public void visit(FactCondTerm FactCondTerm);
    public void visit(AndCondTerm AndCondTerm);
    public void visit(TermCondition TermCondition);
    public void visit(OrCondition OrCondition);
    public void visit(Condition Condition);
    public void visit(PreCondition PreCondition);
    public void visit(ElseCondition ElseCondition);
    public void visit(ErrorIfCond ErrorIfCond);
    public void visit(IfCondClause IfCondClause);
    public void visit(NoOptCondition NoOptCondition);
    public void visit(OptCondition OptCondition);
    public void visit(PreArrayIndex PreArrayIndex);
    public void visit(IdentDesignator IdentDesignator);
    public void visit(FieldDesignator FieldDesignator);
    public void visit(ArrayDesignator ArrayDesignator);
    public void visit(ActualPar ActualPar);
    public void visit(ActualParam ActualParam);
    public void visit(ActualParams ActualParams);
    public void visit(NoActuals NoActuals);
    public void visit(Actuals Actuals);
    public void visit(PreActualPars PreActualPars);
    public void visit(FunctionCall FunctionCall);
    public void visit(FuncCall FuncCall);
    public void visit(Var Var);
    public void visit(ArrayAlloc ArrayAlloc);
    public void visit(Alloc Alloc);
    public void visit(ParenExprFactor ParenExprFactor);
    public void visit(ConstValFactor ConstValFactor);
    public void visit(FactorTerm FactorTerm);
    public void visit(MulTerm MulTerm);
    public void visit(TermExpr TermExpr);
    public void visit(NegExpr NegExpr);
    public void visit(AddExpr AddExpr);
    public void visit(ForStatement ForStatement);
    public void visit(ForPostDesignator ForPostDesignator);
    public void visit(ForPreDesignator ForPreDesignator);
    public void visit(ForPreCondition ForPreCondition);
    public void visit(ScopeStmt ScopeStmt);
    public void visit(ContinueStmt ContinueStmt);
    public void visit(BreakStmt BreakStmt);
    public void visit(MatchedIf MatchedIf);
    public void visit(MatchedFor MatchedFor);
    public void visit(ReturnNoExpr ReturnNoExpr);
    public void visit(ReturnExpr ReturnExpr);
    public void visit(ReadStmt ReadStmt);
    public void visit(NumConstPrintStmt NumConstPrintStmt);
    public void visit(PrintStmt PrintStmt);
    public void visit(ErrAssignment ErrAssignment);
    public void visit(DesignatorStmt DesignatorStmt);
    public void visit(NoOptDesignStmt NoOptDesignStmt);
    public void visit(OptDesignStmt OptDesignStmt);
    public void visit(Decrement Decrement);
    public void visit(Increment Increment);
    public void visit(ProcCall ProcCall);
    public void visit(Assignment Assignment);
    public void visit(UnmatchedFor UnmatchedFor);
    public void visit(UnmatchedIfElse UnmatchedIfElse);
    public void visit(UnmatchedIf UnmatchedIf);
    public void visit(UnmachedStmt UnmachedStmt);
    public void visit(MatchedStmt MatchedStmt);
    public void visit(NoStmt NoStmt);
    public void visit(Statements Statements);
    public void visit(ErrorFormParamDecl ErrorFormParamDecl);
    public void visit(ArrayFormParamDecl ArrayFormParamDecl);
    public void visit(SimpleFormParamDecl SimpleFormParamDecl);
    public void visit(SingleFormalParamDecl SingleFormalParamDecl);
    public void visit(FormalParamDecls FormalParamDecls);
    public void visit(NoFormParam NoFormParam);
    public void visit(FormParams FormParams);
    public void visit(RetTypeMethodTypeName RetTypeMethodTypeName);
    public void visit(VoidMethodTypeName VoidMethodTypeName);
    public void visit(MethodSig MethodSig);
    public void visit(MethodDecl MethodDecl);
    public void visit(MethodBeforeFirstStmt MethodBeforeFirstStmt);
    public void visit(NoMethodDecl NoMethodDecl);
    public void visit(MethodDeclarations MethodDeclarations);
    public void visit(NoInterfaceMethodDecl NoInterfaceMethodDecl);
    public void visit(InterfaceMethodDeclarations InterfaceMethodDeclarations);
    public void visit(InterfaceDecl InterfaceDecl);
    public void visit(InterfaceName InterfaceName);
    public void visit(EmptyClassMethDecl EmptyClassMethDecl);
    public void visit(ClassMethDecl ClassMethDecl);
    public void visit(EmptyClassImpl EmptyClassImpl);
    public void visit(ClassImpl ClassImpl);
    public void visit(InterfaceImpl InterfaceImpl);
    public void visit(SingleInterfaceImpl SingleInterfaceImpl);
    public void visit(InterfaceImplementations InterfaceImplementations);
    public void visit(EmptyClassExt EmptyClassExt);
    public void visit(ClassExt ClassExt);
    public void visit(ClassDecl ClassDecl);
    public void visit(ClassName ClassName);
    public void visit(Type Type);
    public void visit(ValueEnumField ValueEnumField);
    public void visit(DefaultEnumField DefaultEnumField);
    public void visit(EnumFields EnumFields);
    public void visit(SingleEnumField SingleEnumField);
    public void visit(EnumDecl EnumDecl);
    public void visit(EnumName EnumName);
    public void visit(CharConst CharConst);
    public void visit(BoolConst BoolConst);
    public void visit(IntConst IntConst);
    public void visit(ConstDeclIdent ConstDeclIdent);
    public void visit(SingleConstDeclIdent SingleConstDeclIdent);
    public void visit(ConstDeclarationsChain ConstDeclarationsChain);
    public void visit(ConstDecl ConstDecl);
    public void visit(ErrorVarDecl ErrorVarDecl);
    public void visit(ArrayVarDeclIdent ArrayVarDeclIdent);
    public void visit(SimpleVarDeclIdent SimpleVarDeclIdent);
    public void visit(SingleVarDeclIdent SingleVarDeclIdent);
    public void visit(VarDeclarationsChain VarDeclarationsChain);
    public void visit(VarDecl VarDecl);
    public void visit(NoVarDecl NoVarDecl);
    public void visit(VarDeclarations VarDeclarations);
    public void visit(InterfaceGlobalDecl InterfaceGlobalDecl);
    public void visit(ClassGlobalDecl ClassGlobalDecl);
    public void visit(EnumGlobalDecl EnumGlobalDecl);
    public void visit(ConstGlobalDecl ConstGlobalDecl);
    public void visit(VarGlobalDecl VarGlobalDecl);
    public void visit(NoGlobalDecl NoGlobalDecl);
    public void visit(GlobalDeclarations GlobalDeclarations);
    public void visit(ProgName ProgName);
    public void visit(Program Program);

}
