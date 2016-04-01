grammar Hello;

prog : dcls;         			

dcls : (robonameAssign | initBlock | behaviorBlock | eventDcl | funcDcl | varDcl | dataStructDef | dataStructDcl |  arrayDcl)+;

robonameAssign : 'roboname' ':=' TextLit;

initBlock : 'robot' 'initialization' '(' ')' block;

behaviorBlock : 'robot' 'behavior' '(' ')'  block;

block : '{' stmts '}' ;

dataStructDef : 'container' Ident '{' (varDcl | dataStructDcl | arrayDcl )+ '}' ;

dataStructDcl : Ident Ident ;

arrayDcl 	: type Ident '[' expr']'  		# basicArrayDcl
   			| dataStructDcl '[' expr ']' 	# structArrayDcl
   			;

eventDcl : 'event' Ident '(' eventParam ')' block ;

funcDcl : 'func' typeList Ident '(' paramList ')' block ;

paramList : ( param (',' param)*)? ;

param :  type Ident ;

eventParam : eventType Ident;

funcCall : Ident '(' argList ')' ; 

argList : (expr (',' expr)*)?;

varDcl : type basicAssignment;

baseIdent 	: funcCall ('[' expr ']')?   	# funcBaseIdent
			| Ident ('[' expr ']')?			# identBaseIdent
			;
			
generalIdent : baseIdent ('.' baseIdent)*;

callStmt : (generalIdent '.')? funcCall;

assign : generalIdent assignmentOp expr;

basicAssignment : Ident ':=' expr;

assignmentOp : op=(':=' | '+:=' | '-:=' | '*:=' | '/:=' | '%:=');

expr : logicalORExpr ;

stmts  : (varDcl | assign | callStmt | ifStmt | iterStmt | returnStmt)*;

ifStmt : 'if' '(' expr ')' block									# ifThenStmt
		|'if' '(' expr ')' ifblock=block 'else' elseblock=block		# ifElseStmt
		|'if' '(' expr ')' block 'else' ifStmt						# elseIfStmt
		;

iterStmt : 'while' '(' expr ')' block												# whileStmt
		| 'for' '(' basicAssignment ',' second=expr ',' third=expr ')' block		# forAssignStmt
		| 'for' '(' varDcl ',' second=expr ',' third=expr ')' block					# forDclStmt
		| 'for' '(' first=expr ',' second=expr ',' third=expr ')' block				# forStmt
		;

returnStmt 	: 'return' expr			# retValStmt
			| 'return'				# retVoidStmt
			;
	
logicalORExpr : logicalANDExpr							# emptyLogORExpr
              | logicalANDExpr '|' logicalORExpr 		# logORExpr
              ;

logicalANDExpr : equalityExpr							# emptyLogANDExpr
		       | equalityExpr '&' logicalANDExpr		# logANDExpr
		       ;

equalityExpr  : relationalExpr										# emptyEqualExpr
		      | relationalExpr op=( '=' | '!=' ) equalityExpr		# equalExpr
		      ;

relationalExpr 	: additiveExpr														# emptyRelExpr
  				| additiveExpr op=( '<' | '>' | '<=' | '>=' ) relationalExpr		# relExpr
  				;

additiveExpr : multiplicationExpr										# emptyAddExpr
		     | multiplicationExpr op=( '+' | '-' ) additiveExpr		# addExpr
		     ;

multiplicationExpr 	: unaryExpr												# emptyMultExpr
		 			| unaryExpr op=( '*' | '/' | '%' ) multiplicationExpr	# multExpr
					;

unaryExpr 	: primaryExpr			# emptyUnExpr
          	| '-' unaryExpr			# unExpr
			;
			
primaryExpr	: generalIdent			# generalPrimary
			| TextLit				# textLitPrimary
			| NumLit				# numLitPrimary
			| BoolLit				# boolLitPrimary
			| '(' expr ')'			# parenPrimary
			;

typeList 	: 'void'							# voidTypeList
	      	| generalType (',' generalType)*	# generalTypeList
			;
generalType : type 				# typeGeneralType
			| complexType		# complexGeneralType
			;

type 	: 'num' 		# numType	
		| 'number' 		# numberType
		| 'text' 		# textType
		| 'bool' 		# boolType
		| 'boolean' 	# booleanType
		;

complexType : Ident				# structType 
			| type '[' ']' 		# arrayType
			| Ident '[' ']'		# structArrayType
			;

eventType 	: 'BulletHitEvent' | 'BulletHitBulletEvent' | 'BulletMissedEvent' | 'DeathEvent'
			| 'HitByBulletEvent' | 'HitRobotEvent' | 'HitWallEvent' | 'RobotDeathEvent'
            | 'ScannedRobotEvent' | 'StatusEvent' | 'WinEvent' | 'BattleEndedEvent'
			| 'RoundEndedEvent' ;

OP_ADD : '+';
OP_SUB : '-';
OP_MUL : '*';
OP_DIV : '/';
OP_MOD : '%';
OP_EQ  : '=';
OP_NEQ : '!=';
OP_GT  : '>';
OP_LT  : '<';
OP_GTE : '>=';
OP_LTE : '<=';
OP_ASSIGN		: ':=';
OP_ADD_ASSIGN	: '+:=';
OP_SUB_ASSIGN   : '-:=';
OP_MUL_ASSIGN	: '*:=';
OP_DIV_ASSIGN	: '/:=';
OP_MOD_ASSIGN	: '%:=';

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


