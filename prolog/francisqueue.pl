/*
*	anthony francis
*	993-46-7608
*	cse340 @ 12:40
*	project 2
*	algebraic semantics of a queue
*
*
*  base axioms:
*	empty(create) = true
*  	empty(enqueue(q, x)) = false
*  	front(create) = error
*  	front(enqueue(q, x)) = 
*		if empty(q) then x 
*		else front(q)
*  	dequeue(create) = error
*  	dequeue(enqueue(q, x)) = 
*		if empty(q) then q 
*		else enqueue(dequeue(q), x)
*
*
*/

/*********************************************************
* Allows the insertion of an element into a queue
* and returns the new queue.
*/
	
enqueue(create,X,New) :-
	New = enqueue(create,X).


/* predicate forces placeholder for Q to be of the form 
of 'enqueue' structure. */

enqueue(Q,X,New) :-		/*working*/
	enqueue(_,_) = Q,	
	New = enqueue(Q,X).


/*********************************************************
* Allows the 'popping' of an element from the
* top of the queue and returns the remaining
* elemtnts(queue).
*/
	
dequeue(create) :-
	write('Error, cannot dequeue when queue is empty').
	
dequeue(Q,Remains) :-
	enqueue(A,_) = Q,
	A == create,
	Remains = create.
				/*working*/
dequeue(Q,Remains) :-
	enqueue(A,E) = Q,
	dequeue(A,B),
	Remains = enqueue(B,E).
	
/*********************************************************
* A predicate that will return the front element
* of a given queue.
*/
	
front(create) :-
	write('Error, no front if queue is empty').

					/*working*/
front(Q,E) :-
	enqueue(Front,E) = Q,
	Front == create.

front(Q,E) :-
	enqueue(Front, _) = Q,
	front(Front, E).


/*********************************************************
	* A function that indicates whether or not the 
	* 	given queue has any elements in it.
	*/
	
empty(create) :-
	write('The queue is empty').	/* working */

empty(Q) :-
	enqueue(_,_) = Q,
	fail.


/*********************************************************
	* A function that takes a queue and a proposed
	*	element and returns a boolean indicating
	*	whether or not that element is in the queue
	*	you pass it.
	*/

isin(Q,E) :-
	enqueue(_,B) = Q,
	B == E.
				/*working*/
isin(Q,E) :-
	enqueue(Front,_) = Q,
	isin(Front,E).


/*********************************************************
* This is the primary point of execution for the
* program.  After this file has been consulted, 
* the function here will accept input from "input2"
* and begin processing as specified.
*/


go :-   see('input2'),
	read(X),
	notCreated(X, null).


	
/*************************************************************/
/** represents an invalid op, */

process(create, Queue) :-
	read(X),
	write('Cannot create new queue, one already exists...'),nl,nl,
	process(X, Queue).
	

/*************************************************************/
/** represents operations when the enqueue token is read */

process(enqueue, Queue) :-
	read(A),
	write('Enqueue '),write(A),nl,
	enqueue(Queue, A, New),
	write('New queue -> '),write(New),nl,nl,
        read(X),
	process(X, New).


/*************************************************************/
/** represents base case when dequeue token is read */

process(dequeue, create) :-
	write('Queue is empty, dequeue cannot occur...'),nl,nl,
	read(X),
	process(X, create).

	
/*************************************************************/
/** represents operations when the dequeue token is read */

process(dequeue, Queue) :-
	write('Dequeue...'),nl,nl,
	dequeue(Queue, New),
	read(X),
	process(X, New).


/*************************************************************/
/** represents base case when front token is read */

process(front, create) :-
	write('Queue is empty, front cannot exist... '),nl,nl,
	read(X),
	process(X, create).

/*************************************************************/
/** represents operations that take place when front called
	and a queue actually exists */

process(front, Queue) :-
	front(Queue, A),
	write('Front: '),write(A),nl,nl,
	read(X),
	process(X, Queue).


/*************************************************************/
/** represents the moment when the empty token is read */

process(empty, Queue) :-
	empty(Queue),nl,nl,
	read(X),
	process(X, Queue);
	write('The queue is not empty.'),nl,nl,
	read(X),
	process(X, Queue).


/*************************************************************/
/** represents the operations that take place when
	the isin token is read */
	
process(isin, Queue) :-
	read(A),
	isin(Queue, A),
	write(A),
	write(' isin the queue.'),nl,
	write('Current queue -> '),write(Queue),nl,nl,
	read(X),
	process(X, Queue);
	write('Element is not in the queue.'),nl,nl,
	read(X),
	process(X, Queue).


/*************************************************************/
/** the predicate that is called when the "eof" is reached */

process(bye, Queue) :- 
	write('Current queue -> '),write(Queue),nl, 
	write('End of input...'),nl,
	seen.


/*************************************************************/	
/** a predicate that will succeed when no other valid operation
	can, important that it appears at the bottom */

process(S,Q) :-
	write(S),
	write(' is not a valid op.'),nl,nl,
	read(X),
	process(X,Q).

		
/*********************************************************
* The following predicates is defined for the state when
* a queue is created and one does not already exist.
*/


notCreated(create, null) :-
	write('Queue created...'),nl,nl,
	read(X),
	process(X, create).


/*********************************************************
* The following set of predicates have the same function,
* to recognize when a queue has not yet been created.
* From that standpoint, there are only two types of
* operations that require recognition.
*	1) those that accept an argument and should ignore
*	the next token
*	2) those that are stand-alone and may proceed
*	without reading in another token
*
* A predicate is defined for every possible op to provide
* for proper error messages.
*/



notCreated(enqueue, null) :-
	read(_),
	write('Enqueue op when queue does not exist yet...'),nl,nl,
	read(X),
	notCreated(X, null).
	
notCreated(dequeue, null) :-
	write('Dequeue op when queue does not exist yet...'),nl,nl,
	read(X),
	notCreated(X, null).
	
notCreated(front, null) :-
	write('Front op when queue does not exist yet...'),nl,nl,
	read(X),
	notCreated(X, null).
	
notCreated(empty, null) :-
	write('Empty op when queue does not exist yet...'),nl,nl,
	read(X),
	notCreated(X, null).


/* not needed---------------------------------

notCreated(_, null) :-
	read(_), 
	write('Queue does not exist yet...'), nl,
	read(X),
	notCreated(X, null).
	
---------------------------------------------*/

notCreated(_, null) :-
	write('Bad op when queue does not exist yet...'),nl,nl,
	read(X),
	notCreated(X, null).
	
notCreated(isin, null) :-
	read(_),
	write('Isin op when queue does not exist yet...'),nl,nl,
	read(X),
	notCreated(X, null).