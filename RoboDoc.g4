grammar RoboDoc;

prog : dcls;         			

dcls : (method)*;

method : type Ident '(' param (',' param)* ')' description;

param : type Ident;

type : 'hey' | 'hej';

description : Text;

Ident : [a-zA-Z]+ ([a-zA-Z0-9])*;

Text : .*?;

WS : [ \t\r\n]->skip;