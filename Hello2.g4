/**
 * Define a grammar called Hello
 */
grammar Hello2;

options{
	output=AST;
}



prog : dcls;         // match keyword hello followed by an identifier

dcls : (robonameAssign | initBlock | behaviorBlock | eventDcl | funcDcl | varDcl | dataStructDef | dataStructDcl |  arrayDcl)+;

robonameAssign : 'roboname' ':=' TextLit { System.out.println("roboname := " + $TextLit.text);};

initBlock : 'robot' 'initialization' '(' ')' block { System.out.print("robot initialization () ");};

behaviorBlock : 'robot' 'behavior' '(' ')' block  { System.out.print("robot behavior () ");};

block : '{' stmts '}' { System.out.print("{" + $stmts.text + "}");};

dataStructDef : 'container' Ident '{' (varDcl | dataStructDcl | arrayDcl )+ '}' ;

dataStructDcl : Ident Ident ;

arrayDcl : type Ident '[' expr']'  
   | dataStructDcl '[' expr ']' ;

eventDcl : 'event' Ident '(' eventParam ')' block ;

funcDcl : 'func' typeList Ident '(' paramList ')' block ;

paramList : ( param (',' param)*)? ;

param :  type Ident ;

eventParam : eventType Ident;

funcCall :Ident '(' argList ')' { System.out.println($Ident.text + "("+ $argList.text +")");}; 

argList : (expr (',' expr)*)?;

varDcl : type basicAssignment;

baseIdent : (Ident         {System.out.println($Ident.text); }
	        | funcCall     //{System.out.println($funcCall.text); }
            ) 
            ('[' expr ']' {System.out.println("[" + $expr.text + "]"); }
            
            )?
            ; 

generalIdent : baseIdent ('.' baseIdent)*;

assign : generalIdent assignmentOp expr;

basicAssignment : Ident ':=' expr;

assignmentOp : ':=' | '+:=' | '-:=' | '*:=' | '/:=' | '%:=';

expr : logicalORExpr ;

stmts  : (varDcl | assign | expr | ifStmt | iterStmt | returnStmt)*;

ifStmt : 'if' '(' expr ')' block {System.out.println("if (" + $expr.text + ")" + $block.text);}
		|'if' '(' expr ')' block 'else' block {System.out.println("if (" + $expr.text + ")" + $block.text +"else"+ $block.text);}
		|'if' '(' expr ')' block 'else' ifStmt;
		

iterStmt : 'while' '(' expr ')' block
		| 'for' '(' ( basicAssignment | varDcl ) ',' expr ',' expr ')' block
		| 'for' '(' expr ',' expr ',' expr ')' block;

returnStmt :  'return' expr                                               
		| 'return';

logicalORExpr : logicalANDExpr
     |  logicalANDExpr  '|' logicalORExpr ;

logicalANDExpr : equalityExpr
		     |  equalityExpr '&' logicalANDExpr;

equalityExpr  : relationalExpr
		      | relationalExpr ( '=' | '!=' ) equalityExpr ;

relationalExpr : additiveExpr
  | additiveExpr( '<' | '>' | '<=' | '>=' ) relationalExpr ;

additiveExpr : multExpr
		| multExpr ( '+' | '-' ) additiveExpr ;

multExpr : unaryExpr
		| unaryExpr ( '*' | '/' | '%' ) multExpr;

unaryExpr : primaryExpr
        | '-' unaryExpr	;

primaryExpr : generalIdent	
| TextLit
| NumLit
| BoolLit
| '(' expr ')';

typeList : 'void'
	      | type (',' type)* ;

type : 'num' | 'number' |'text' | 'bool' | 'boolean' ;

eventType : 'BulletHitEvent' | 'BulletHitBulletEvent' | 'BulletMissedEvent' | 'DeathEvent'
		| 'HitByBulletEvent' | 'HitRobotEvent' | 'HitWallEvent' | 'RobotDeathEvent'
                        | 'ScannedRobotEvent' | 'StatusEvent' | 'WinEvent' | 'BattleEndedEvent'
		| 'RoundEndedEvent' ;

Ident : [a-zA-Z]+ ([a-zA-Z0-9])*;

TextLit : '"'.*?'"';   //Needs work

/*
NumLit : ( [0-9]*) '.' (( [0-9]* [1-9]) | '0')
                | ( '0' | [1-9] [0-9]*);     
*/

NumLit : [0-9]+ ('.' [0-9]+)?;
                 
BoolLit : 'true' | 'false';

WS : [ \t\r\n]->skip;

COMMENT : '//' ~('\r'|'\n')* -> skip;

BLOCKCOMMENT : '/*' .*? '*/'->skip;


