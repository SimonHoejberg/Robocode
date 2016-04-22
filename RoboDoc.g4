grammar RoboDoc;

prog : dcls;         			

dcls : (method)*;

method : type Ident '(' params? ')' description;

params : type Ident (',' type Ident)* ;

type 	: 'void'		#voidType
		| 'int'			#intType
		| 'long'		#longType
		| 'double'		#doubleType
		| 'String'		#stringType
		| 'boolean'     #booleanType
		| robocode		#robocodeType
		;

robocode : 'Color'		#ColorType
		| 'Runnable'    #RunnableType
		| 'IPaintEvents' #IPaintEventsType
		| 'IInteractiveEvents' #IInteractiveEventsType
		| 'Graphics2D'	#GraphicsTwoDType
		| 'IBasicEvents' #IBasicEventsType
		| 'Bullet'		#BulletType
		| 'WinEvent'    #WinEventType
		| 'StatusEvent' #StatusEventType
		| 'ScannedRobotEvent' #ScannedRobotEventType
		| 'RoundEndedEvent' #RoundEndedEventType
		| 'RobotDeathEvent' #RobotDeathEventType
		| 'MouseWheelEvent' #MouseWheelEventType
		| 'MouseEvent' #MouseEventType
		| 'KeyEvent' #KeyEventType
		| 'HitWallEvent' #HitWallEventType
		| 'HitRobotEvent' #HitRobotEventType
		| 'HitByBulletEvent' #HitByBulletEventType
		| 'DeathEvent' #DeathEventType
		| 'BulletMissedEvent' #BulletMissedEventType
		| 'BulletHitBulletEvent' #BulletHitBulletEventType
		| 'BulletHitEvent' #BulletHitEventType
		| 'BattleEndedEvent' #BattleEndedEventType
		;

description : Text;

Ident : [a-zA-Z]+ ([a-zA-Z0-9])*;

Text : '"'.*?'"';

WS : [ \t\r\n]->skip;