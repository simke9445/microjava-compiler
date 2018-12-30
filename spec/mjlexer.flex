package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"interface" { return new_symbol(sym.INTERFACE, yytext()); }
"implements" { return new_symbol(sym.IMPLEMENTS, yytext()); }
"extends"   { return new_symbol(sym.EXTENDS, yytext()); }
"class"     { return new_symbol(sym.CLASS, yytext()); }
"const"     { return new_symbol(sym.CONST, yytext()); }
"continue"  { return new_symbol(sym.CONTINUE, yytext()); }
"for"       { return new_symbol(sym.FOR, yytext()); }
"enum"      { return new_symbol(sym.ENUM, yytext()); }
"new"       { return new_symbol(sym.NEW, yytext()); }
"program"   { return new_symbol(sym.PROG, yytext()); }
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"read"      { return new_symbol(sym.READ, yytext()); }
"return" 	{ return new_symbol(sym.RETURN, yytext()); }
"void" 		{ return new_symbol(sym.VOID, yytext()); }
"break"     { return new_symbol(sym.BREAK, yytext()); }
"if"   		{ return new_symbol(sym.IF, yytext()); }
"else"		{ return new_symbol(sym.ELSE, yytext()); }
"++"        { return new_symbol(sym.INCREMENT, yytext()); }
"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"--"        { return new_symbol(sym.DECREMENT, yytext()); }
"-"         { return new_symbol(sym.MINUS, yytext()); }
"*"         { return new_symbol(sym.ASTERISK, yytext()); }
"%"         { return new_symbol(sym.PERCENT, yytext()); }
"=="        { return new_symbol(sym.IS_EQUAL, yytext()); }
">="        { return new_symbol(sym.GREATER_OR_EQUAL, yytext()); }
"<="        { return new_symbol(sym.LESSER_OR_EQUAL, yytext()); }
"!="        { return new_symbol(sym.NOT_EQUAL, yytext()); }
">"         { return new_symbol(sym.GREATER, yytext()); }
"<"         { return new_symbol(sym.LESSER, yytext()); }
"=" 		{ return new_symbol(sym.EQUAL, yytext()); }
";" 		{ return new_symbol(sym.SEMI, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"."         { return new_symbol(sym.DOT, yytext()); }
"(" 		{ return new_symbol(sym.LPAREN, yytext()); }
")" 		{ return new_symbol(sym.RPAREN, yytext()); }
"{" 		{ return new_symbol(sym.LBRACE, yytext()); }
"}"			{ return new_symbol(sym.RBRACE, yytext()); }
"["         { return new_symbol(sym.LBRACKET, yytext()); }
"]"         { return new_symbol(sym.RBRACKET, yytext()); }
"&&"        { return new_symbol(sym.AND, yytext()); }
"||"		{ return new_symbol(sym.OR, yytext()); }

<YYINITIAL> "//" 		     { yybegin(COMMENT); }
<COMMENT> .      { yybegin(COMMENT); }
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

"/" { return new_symbol(sym.SLASH, yytext()); }

[0-9]+  { return new_symbol(sym.NUMBER, new Integer(yytext())); }
(true|false) { return new_symbol(sym.BOOL, yytext()); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{ return new_symbol(sym.IDENT, yytext()); }
'[ -~]' { return new_symbol(sym.CHAR, new Character(yytext().charAt(1))); }


. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1) + ", u koloni " + yycolumn); }






