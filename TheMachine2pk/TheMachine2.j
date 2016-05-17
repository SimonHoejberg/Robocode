.class TheMachine2pk/TheMachine2
.super robocode/Robot

.field private turnDirection D = 1.0
.field private moving Z = 0
.field private remainingDistance D = 0.0
.method public <init>()V
aload_0 
invokespecial robocode/Robot/<init>()V 
return 
.end method
null
.method public run()V



null
null
null
null
WHILE:
nullifeq	IF0
dcmpg
ifgt	REL0
iconst_0
goto	REL1
REL0: iconst_1
REL1: ifeq	IF1
null
null
goto	IF2
IF1: null
null
null
IF2: 
goto	IF3
IF0: null
IF3: 
goto WHILE
return 
.end method

.method public onScannedRobot(Lrobocode/ScannedRobotEvent;)V
invokevirtual TheMachine2pk/TheMachine2/
.end method

.method private _onScannedRobot(Lrobocode/ScannedRobotEvent;)V
nullifeq	IF4
null
goto	IF5
IF4: null
dcmpg
ifge	REL2
iconst_0
goto	REL3
REL2: iconst_1
REL3: ifeq	IF6
null
goto	IF7
IF6: null
IF7: 
difference Dnullnulldsub


dcmpg
ifgt	REL4
iconst_0
goto	REL5
REL4: iconst_1
REL5: ifeq	IF8
null
goto	IF9
IF8: null
IF9: 
null
null
dcmpg
ifgt	REL6
iconst_0
goto	REL7
REL6: iconst_1
REL7: ifeq	IF10
null
IF10: 
null
IF5: 
.end method


.method public CleverFire(D)V
dcmpg
iflt	REL8
iconst_0
goto	REL9
REL8: iconst_1
REL9: ifeq	IF11
null
goto	IF12
IF11: dcmpg
iflt	REL10
iconst_0
goto	REL11
REL10: iconst_1
REL11: ifeq	IF13
null
goto	IF14
IF13: dcmpg
iflt	REL12
iconst_0
goto	REL13
REL12: iconst_1
REL13: ifeq	IF15
null
IF15: IF14: IF12: 
return
.end method

.method public onHitRobot(Lrobocode/HitRobotEvent;)V
invokevirtual TheMachine2pk/TheMachine2/
.end method

.method private _onHitRobot(Lrobocode/HitRobotEvent;)V
dcmpg
ifge	REL14
iconst_0
goto	REL15
REL14: iconst_1
REL15: ifeq	IF16
null
goto	IF17
IF16: null
IF17: 
null
null
dcmpg
ifgt	REL16
iconst_0
goto	REL17
REL16: iconst_1
REL17: ifeq	IF18
null
goto	IF19
IF18: dcmpg
ifgt	REL18
iconst_0
goto	REL19
REL18: iconst_1
REL19: ifeq	IF20
null
goto	IF21
IF20: dcmpg
ifgt	REL20
iconst_0
goto	REL21
REL20: iconst_1
REL21: ifeq	IF22
null
goto	IF23
IF22: dcmpg
ifgt	REL22
iconst_0
goto	REL23
REL22: iconst_1
REL23: ifeq	IF24
null
goto	IF25
IF24: dcmpg
ifgt	REL24
iconst_0
goto	REL25
REL24: iconst_1
REL25: ifeq	IF26
null
IF26: IF25: IF23: IF21: IF19: 
null
.end method


