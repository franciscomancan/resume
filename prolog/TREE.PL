/*  Anthony Francis #85216, Project 4, lab# 53495 */

/* Family Tree: a factbase representing relationships within a family.
*	Overall structure for the tree is a directed graph.  All relationships
*	will later be inferred from directed edges.
*/

edge(napoleon0,tommy1).
edge(napoleon0,jack2).
edge(napoleon0,sarah3).
edge(napoleon0,george4).
edge(tommy1,ellen5).
edge(jack2,sam6).
edge(sarah3,sedona7).
edge(george4,chicken8).
edge(ellen5,dave9).
edge(ellen5,bill10).
edge(ellen5,skip11).
edge(sedona7,tim12).
edge(sedona7,trevor13).
edge(chicken8,dog14).
edge(chicken8,cat15).
edge(chicken8,mouse16).
edge(dave9,kelly17).
edge(bill10,julie18).
edge(skip11,trudy19).
edge(tim12,john20).
edge(trevor13,kyle21).
edge(dog14,pig22).
edge(cat15,louse23).
edge(mouse16,rat24).
edge(kelly17,juice25).
edge(kelly17,scratch26).
edge(trudy19,zorn27).
edge(trudy19,caffrey28).
edge(kyle21,lisa29).
edge(kyle21,jacky30).
edge(louse23,spider31).
edge(louse23,bot32).
edge(rat24,cow33).
edge(zorn27,julian34).
edge(spider31,goat35).
edge(cow33,hyena36).
edge(cow33,horse37).
edge(julian34,ashley38).
edge(julian34,lindsey39).
edge(julian34,gretta40).
edge(julian34,riley41).
edge(goat35,walrus42).
edge(goat35,elephant43).

/*	Following are the relationships, or tree operations, that may 
*	be constructed from the graph data structure represented above.  
*	The procedures, in general, are comprehensive and rely on one 
*	before it.
*/

parent(Node1,Node2) :- 
	edge(Node2,Node1).

/*  a rule sibling(X,Y) which returns yes if X and Y have the same parent. */

sibling(X,Y) :- 
	parent(X,Z), parent(Y,Z), X \== Y.

/* a rule depth(D, X) where D is the number of levels from the root to X. The depth of the root is 0. 
*	(recursive)		
*/

depth(_,Node1) :- parent(Node1,_),/* terminates if no parent */ !.
depth(Dep,Node) :-
	parent(Node,B),
	depth(NewDep,B),
	Dep is NewDep + 1.

/* a rule cousin(X, Y) which returns yes if X and Y have the same depth (recursive) */

cousin(X,Y) :- X == Y, !.
cousin(X,Y) :-
	parent(X,W),
	parent(Y,Z),
	cousin(W,Z),
	X \== Y.

/*  a rule height(H, X) where H is the length of the longest path from node X to a leaf under X. 
*	The height of a leaf is 0. (recursive)
*/

height(0,_) :- !.
height(H,X) :-
	parent(Another,X),
	Hplus is H + 1,
	height(Hplus,Another),
	H = Hplus.

leaf(Node) :- height(0,Node).

/*  ..queries from the directed graph to test rulebase.  */

treequestions :-
	write('tommy1 is the parent of ellen5.'),parent(ellen5,tommy1),nl,
	parent(dog14,A),write('parent of dog14 is '), write(A),nl,
	parent(B,sedona7),write('sedona7 is parent of '),write(B),nl,
	write('dave9 is the sibling of skip11.'),sibling(dave9,skip11),nl,
	sibling(scratch26,C),write('sibling of scratch26 is '), write(C),nl,
	sibling(D,cat15),write('cat15 is sibling of '),write(D),nl,
	depth(E,cow33),write('depth of cow33 is '),write(E),nl,
	cousin(sedona7,sam6),write('sedona7 is cousin of sam6'),nl,
	cousin(dog14,F),write('cousin of dog14 is '), write(F),nl,
	cousin(G,sedona7),write('sedona7 is cousin of '),write(G),nl,
	height(H,napoleon0),write('height of napoleon0 is '),write(H),nl.
	
/* The rest of the rulebase is involved with a user interface,
*	a menu to interact with the previous rules.
*/

go :-
	display_menu,
	read_inquiry_type(Type),
	goal(Type,Goal),
	call(Goal),
	nl,nl,nl,
	write('Do you want to query the database again? (y/n) '),
	read_sentence(StringResponse),
	continue(Response).

goal(1,one).
goal(2,two).
goal(3,three).
goal(4,four).

continue(Response) :-
	member(Response, ['y',y,'YES','Yes',yes]),
	go,
	!.

continue(Respone) :-
	nl,nl,nl,
	write('  Thanks a lot').

display_menu :-
	nl,
	write('                 Database Inquiries'),
	nl,nl,
	write('Enter the number of the type of inquiry you wish to make.'),
	nl,nl,nl,
	write('                 1. find the sibling of a node'),nl,
	write('                 2. find the cousin of a node'),nl,
	write('                 3. find the depth of a node'),nl,
	write('                 4. find the height of a node'),nl,
	nl,nl,
	write('What type of qeury do you wish to make: ').

read_inquiry_type(Type) :-
read_sentence(StringType).

one :-
	nl,
	write('For whom do want the sibling? '),nl,
	read_sentence(StringPerson),
	nl,
	write('This is it '),
	write(':  '), nl,
	sibling(Bla,Person), write(Bla).

two :-
	nl,
	write('For whom do want the cousin? '),nl,
	read_sentence(StringPerson),
	nl,
	write('This is it '),
	write(':  '), nl,
	cousin(Bla,Person),write(Bla).

three :-
	nl,
	write('For whom do want the depth? '),nl,
	read_sentence(StringPerson),
	nl,
	write('This is it '),
	write(':  '), nl,
	depth(Vari,Person),write(Vari).

four :-
	nl,
	write('For whom do want the height? '),nl,
	read_sentence(StringPerson),
	nl,
	write('This is it '),
	write(':  '), nl,
	height(Varia,Person), write(Varia),nl.


read_sentence([Word|Word_list]) :-
	nl,
	get0(Ch),
	name(Char1, [Ch]),
	readword(Char1, Word, Char2),
	restsent(Word, Char2, Word_list).

restsent(Word, _, []) :-
	lastword(Word), !.

restsent(Word, Char1, [Word1|Word_list]) :-
	readword(Char1, Word1, Char2),
	restsent(Word1, Char2, Word_list).

readword(Ch, Word, Ch1) :-
	one_char_word(Ch), !,
	Word = Ch,
	get0(C),
	name(Ch1, [C]).


readword(Ch, Word, Ch2) :-
	inword(Ch, NewChar), !,
	get0(C),
	name(Ch1, [C]),
	restword(Ch1, Char_list, Ch2),
	Word = [NewChar | Char_list].

readword(Ch, Word, Ch2) :-
	get0(C),
	name(Ch1, [C]),
	restword(Ch1, Word, Ch2).

readword(Ch, [NewCh|Char_list], Ch2) :-
	inword(Ch, NewCh), !,
	get0(C),
	name(Ch1, [C]),
	restword(Ch1, Char_list, Ch2).

restword(Ch, [NewCh|Char_list], Ch2) :-
	inword(Ch, NewCh), !,
	get0(C),
	name(Ch1, [C]),
	restword(Ch1, Char_list, Ch2).

restword(Ch, [], Ch).

inword(Char, NewChar) :-
	"Char" = [26],
	NewChar = ''.

inword(Char, Char) :-
	Char @>= 'a', Char @=< 'z'.

inword(Char, LCChar) :-
	Char @>= 'A', Char @=< 'Z',
	name(Char, [Ascii]),
	NewAscii is Ascii + 32,
	name(LCChar, [NewAscii]).

inword(Char, Char) :-
	Char @>= '0', Char @=< '9'.

inword(Char, Char) :-
	Char = ''''.

inword(Char, Char) :-
	Char = '-'.

lastword('.').
lastword('!').
lastword('?').

one_char_word('.').
one_char_word(',').
one_char_word(';').
one_char_word(' ').
one_char_word(':').
one_char_word('?').
one_char_word('!').
one_char_word('''').
