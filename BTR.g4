grammar BTR;

prog : dcls;         			

dcls : (robonameAssign | initBlock | behaviorBlock | eventDcl | funcDcl |  dataStructDef )+;

robonameAssign : 'roboname' ':=' TextLit;

initBlock : 'robot' 'initialization' '(' ')' block;

behaviorBlock : 'robot' 'behavior' '(' ')'  block;

block : '{' stmts '}' ;

dataStructDef : 'container' Ident '{' (varDcl | dataStructDcl | arrayDcl | assign)+ '}' ;

dataStructDcl : Ident Ident;

arrayDcl 	: type Ident '[' expr']'  		# basicArrayDcl
   			| dataStructDcl '[' expr ']' 	# structArrayDcl
   			;
   			
sizelessArrayDcl 	: type Ident '[' ']'	# basicSizelessDcl
					| dataStructDcl '[' ']'	# structSizelessDcl
					;

eventDcl : 'event' Ident '(' eventParam ')' block ;

funcDcl : 'func' typeList Ident '(' paramList ')' block ;

paramList : ( param (',' param)*)? ;

param :  generalType Ident ;

eventParam : eventType Ident;

funcCall : Ident '(' argList ')' ; 

argList : (expr (',' expr)*)?;

var : type Ident;

varDcl : var (',' var)* ':=' expr;

baseIdent 	: funcCall ('[' expr ']')?   	# funcBaseIdent
			| Ident ('[' expr ']')?			# identBaseIdent
			;
			
generalIdent : baseIdent ('.' baseIdent)*;

callStmt : (generalIdent '.')? funcCall;

assignHelp : (var | generalIdent | dataStructDcl | arrayDcl | sizelessArrayDcl);

assign : assignHelp (',' assignHelp)* assignmentOp expr;

assignmentOp : op=(':=' | '+:=' | '-:=' | '*:=' | '/:=' | '%:=');

expr : logicalORExpr ;

stmts  : (varDcl | dataStructDcl | arrayDcl | assign | callStmt | ifStmt | iterStmt | returnStmt )*;

ifStmt : 'if' '(' expr ')' block									# ifThenStmt
		|'if' '(' expr ')' ifblock=block 'else' elseblock=block		# ifElseStmt
		|'if' '(' expr ')' block 'else' ifStmt						# elseIfStmt
		;

iterStmt : 'while' '(' expr ')' block												# whileStmt
		| 'for' '(' assign ',' second=expr ',' third=callStmt ')' block					# forStmt
		| 'for' '(' first=callStmt ',' second=expr ',' third=callStmt ')' block				# forStmt
		| 'for' '(' assign ',' second=expr ',' assign ')' block						# forStmt
		| 'for' '(' first=callStmt ',' second=expr ',' assign ')' block					# forStmt
		;

returnStmt 	: 'return' expr	(',' expr)*	# retValStmt
			| 'return'					# retVoidStmt
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
          	| '-' unaryExpr			# negUnExpr
          	| '!' unaryExpr			# notUnExpr
			;
			
primaryExpr	: generalIdent			# generalPrimary
			| NumLit				# numLitPrimary
			| BoolLit				# boolLitPrimary
			| TextLit				# textLitPrimary
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

BoolLit : 'true' | 'false';

Ident : [a-zA-Z]+ ([a-zA-Z0-9])*;

TextLit : '"'.*?'"';
//TextLit : '"'~('"')*?'"';   //Needs work  

/*
NumLit : ( [0-9]*) '.' (( [0-9]* [1-9]) | '0')
                | ( '0' | [1-9] [0-9]*);     
*/

NumLit : [0-9]+ ('.' [0-9]+)?;

WS : [ \t\r\n]->skip;

COMMENT : '//' ~('\r'|'\n')* -> skip;

BLOCKCOMMENT : '/*' .*? '*/'->skip;


