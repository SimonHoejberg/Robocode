grammar RoboDoc;

prog : dcls;         			

dcls : libname (method)*;

libname : Text;

method : type Ident '(' param (',' param)* ')' description;

param : type Ident;

type 	: 'void'		#voidType
		| 'int'			#intType
		| 'long'		#longType
		| 'double'		#doubleType
		| 'String'		#stringType
		;

description : Text*;

Text : .*?;

Ident : [a-zA-Z]+ ([a-zA-Z0-9])*;

WS : [ \t\r\n]->skip;