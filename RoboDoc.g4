grammar RoboDoc;

prog : dcls;         			

dcls : (method)*;

method : type Ident '(' params?')' Text;

params : type Ident (',' type Ident)* ;

type 	: 'void'		#voidType
		| 'int'			#intType
		| 'long'		#longType
		| 'double'		#doubleType
		| 'String'		#stringType
		| 'boolean'     #booleanType
		;

Ident : [a-zA-Z]+ ([a-zA-Z0-9])*;

Text : '"'.*?'"';

WS : [ \t\r\n]->skip;