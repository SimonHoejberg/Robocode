/**
 * Define a grammar called Hello
 */
grammar Hello;

prog : dcls;         // match keyword hello followed by an identifier

dcls : (robonameAssign | initBlock | behaviorBlock | eventDcl | funcDcl | varDcl | dataStructDef | dataStructDcl |  arrayDcl)+;

robonameAssign : 'roboname' ':=' '"'TextLit'"';

initBlock : 'robot' 'initialization''()' block;

behaviorBlock : 'robot' 'behavior''()' block;

block : '{' stmts '}' ;

dataStructDef : 'container' Ident '{' (varDcl | dataStructDcl | arrayDcl )+ '}' ;

dataStructDcl : Ident Ident ;

arrayDcl : type Ident '[' expr']'  
   | dataStructDcl '[' expr ']' ;

eventDcl : 'event' Ident '(' eventParam ')' block ;

funcDcl : 'func' typeList Ident '(' paramList ')' block ;

paramList : ( param (',' param)*)? ;

param :  type Ident ;

eventParam : eventType Ident;

argList : (expr (',' expr)*)?;

varDcl : type basicAssignment;

basicAssignment : Ident ('.' Ident)* ':=' expr;

complexAssignment : Ident ('[' expr ']')? assignmentOp expr
			 | Ident ('.' Ident)* assignmentOp expr;

assignmentOp : ':=' | '+:=' | '-:=' | '*:=' | '/:=' | '%:=';

expr : logicalORExpr ;

stmts  : (varDcl | complexAssignment | expr | ifStmt | iterStmt | returnStmt)*;

ifStmt : 'if' '(' expr ')' block
		|    'if' '(' expr ')' block 'else' block;

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

primaryExpr : Ident ( '.' Ident )*		
| TextLit
| NumLit
| BoolLit
| Ident '(' argList ')'
| '(' expr ')'
| Ident '[' expr ']';

typeList : 'void'
	      | type (',' type)* ;

type : 'num' | 'number' |'text' | 'bool' | 'boolean' ;

eventType : 'BulletHitEvent' | 'BulletHitBulletEvent' | 'BulletMissedEvent' | 'DeathEvent'
		| 'HitByBulletEvent' | 'HitRobotEvent' | 'HitWallEvent' | 'RobotDeathEvent'
                        | 'ScannedRobotEvent' | 'StatusEvent' | 'WinEvent' | 'BattleEndedEvent'
		| 'RoundEndedEvent' ;

TextLit : [a-z]+;   

NumLit : ( '0' | [1-9] [0-9]*) '.' (( [0-9]* [1-9]) | '0')
                | ( '0' | [1-9] [0-9]*);     

Ident :[a-zA-Z]+ /*([a-zA-Z0-9])**/;
                   
BoolLit : 'true' | 'false';

WS : [ \t\r\n]->skip;



