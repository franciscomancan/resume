/* Anthony Francis, Assignment 9, affil.#85216 */

/* The following is the factbase for the family relationships.
	There are six basic fact types, the rest of the relationships
	will be inferred with rules */

father-of(greg,napoleon).
father-of(adam,jake).
father-of(audra,jake).
father-of(aaron,jake).
father-of(anthony,aaron).

mother-of(adam,mo).
mother-of(audra,mo).
mother-of(aaron,mo).
mother-of(mo,amelia).
mother-of(jake,blanch).
mother-of(ayca,josephina).
mother-of(julian,ayca).
mother-of(anthony,linda).
mother-of(greg,amelia).
mother-of(josephina,amelia).
mother-of(patsy,esparanza).
mother-of(gregory,patsy).
mother-of(nappy,patsy).

sibling-of(aaron,adam).
sibling-of(mo,josephina).
sibling-of(mo,barbara).
sibling-of(greg,mo).
sibling-of(greg,ray).
sibling-of(ray,dennis).

female(audra).
female(mo).
female(amelia).
female(blanch).
female(ayca).
female(josephina).
female(linda).
female(patsy).
female(esparanza).
female(barbara).

male(greg).
male(napoleon).
male(adam).
male(jake).
male(aaron).
male(julian).
male(anthony).
male(gregory).
male(nappy).

/* Following are the rules for the family relationships.
	Each one will conclude a particular relationship from
	the given facts.  The dependency of the relationships
	builds progressively; so, the creation of the specified
	rules implies the need for other basic rules
	(i.e. male, female, sister..). */

mother-of(X,Y) :- parent-of(X,Y), female(Y).
father-of(X,Y) :- parent-of(X,Y), male(Y).
parent-of(X,Y) :- mother-of(X,Y); father-of(X,Y).
grandmother-of(X,Y) :- parent-of(X,Z), mother-of(Z,Y).
grandfather-of(X,Y) :- parent-of(X,Z), father-of(Z,Y).
grandparent-of(X,Y) :- parent-of(X,Z), parent-of(Z,Y).
aunt-of(X,Y) :- parent-of(X,Z), sibling-of(Z,Y).
uncle-of(X,Y) :- parent-of(X,Z), sibling-of(Z,Y).
cousin-of(A,B) :- parent-of(A,X), sibling-of(X,Y), parent-of(B,Y).
nephew-of(X,Y) :- sibling-of(X,Z), parent-of(Y,Z), male(Y).

/* This rule is a set of queries to the family factbase,
	a driver to test prolog */
	
familyquestions :- 
mother-of(nappy,A),
write('Mother of Nappy is '), write(A), nl,
mother-of(gregory,B),
write('Mother of Gregory is '), write(B), nl,
mother-of(julian,C),
write('Mother of Julian is '), write(C), nl,
mother-of(ayca,D),
write('Mother of Ayca is '), write(D), nl,
grandmother-of(julian,E),
write('Grandmother of Julian is '), write(E), nl,
aunt-of(F,G),
write('aunt of '),write(F),write(' is '), write(G), nl,
cousin-of(H,I),
write('cousin of '),write(H),write(' is '),write(I), nl,
uncle-of(J,K),
write('nephew of '),write(J),write(' is '),write(K), nl,
grandparent-of(X,amelia),
write('grandparent of '),write(X),write(' is '),write('amelia'), nl.
