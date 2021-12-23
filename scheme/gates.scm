; 	These are definintions of primitive logic gates
; 	to be used in math operations on binary lists.
;	The procuedures will be composed to form a software
;	simulation of an ALU (Arithmetic Logic Unit).
;
;	author: anthony.francis


;-----------------------------------------------------
;	definition of the basic gates (i.e. and, or, not...)

(define (and-gate a b)
  (if (and (equal? a 1) (equal? b 1))
  1
  0))

(define (or-gate a b)
  (if (or (equal? a 1) (equal? b 1))
      1
      0))

(define (not-gate a)
       (if (equal? 0 a)
           1
           0))

(define (xor-gate a b)
  (if (and
      (or (equal? a 1) (equal? b 1))
      (not (and (equal? a 1) (equal? b 1))))
      1
      0
      ))


;--------------------------------------------------------
; the gates are used here to operate as a half adder

(define (halfadder-sum a b)
  (xor-gate a b))
(define (halfadder-carry a b)
  (and-gate a b))

; two definitions are used here for the full adder,
; which is constructed by cascading half adders

(define (fulladder-sum a b carry)
       (halfadder-sum
        (halfadder-sum a b) carry))
(define (fulladder-carry a b carry)
  (or-gate
   (halfadder-carry a b)
   (halfadder-carry (halfadder-sum a b) carry)))


;------------------------------------------------------------------
; here lyes a recursive procedure to add binary numbers; however, 
; its binary arguments must be reversed prior to its operations.

(define (add-bit-lists a b carry)
  (cond ((and (null? a) (null? b))
         (list carry))
        ((null? a)
         (cons (fulladder-sum
                0 (car b) carry)
               (add-bit-lists
                '() (cdr b) carry)))
        ((null? b)
         (cons (fulladder-sum
                (car a) 0 carry)
               (add-bit-lists
                (cdr a)
                '() (fulladder-carry
                    (car a) 0 carry))))
        (else
         (cons (fulladder-sum
                (car a) (car b) carry)
               (add-bit-lists
                (cdr a)
                (cdr b)
                (fulladder-carry
                 (car a) (car b) carry))))))


; Utility procedures, used to place binary lists into an orientation that begins with 
; the least significant digit

(define (trim x)
  (if (equal? x null)
      ()
      (if (equal? (car x) 0)
          (trim (cdr x))
          x)))

(define (reverse x)
   (if (null? x)
       ()
       (append (reverse (cdr x))
               (list (car x))))
  )

;--------------------------------------------
; 	alu-adder: adds two binary lists. (calls to 'reverse' are negated)

(define (alu-adder a b)
  (trim (reverse (add-bit-lists (reverse  a) (reverse b) 0))))


(define (and-bit-lists a b)
  (cond ((and (null? a) (null? b))
         ())
        ((null? a)
         (cons (and-gate 0 (car b))
               (and-bit-lists '() (cdr b))))
        ((null? b)
         (cons (and-gate (car a) 0)
               (and-bit-lists (cdr a) '())))
        (else
         (cons (and-gate (car a) (car b))
               (and-bit-lists (cdr a) (cdr b))))))

;------------------------------------------
;	proc. to 'and' two elements.
;	Lisp also provides a built-in procedure for this function
(define (binary-and a b)
  (reverse (and-bit-lists (reverse  a) (reverse b))))


;--------------------------------
;	'or' of the two given bit lists

(define (or-bit-lists a b)
  (cond ((and (null? a) (null? b))
         ())
        ((null? a)
         (cons (or-gate 0 (car b))
               (or-bit-lists '() (cdr b))))
        ((null? b)
         (cons (or-gate (car a) 0)
               (or-bit-lists (cdr a) '())))
        (else
         (cons (or-gate (car a) (car b))
               (or-bit-lists (cdr a) (cdr b))))))

;-------------------------------------------------
;a proc. to logically 'or' two elements of a list
(define (binary-or a b)
  (reverse (or-bit-lists (reverse  a) (reverse b))))


;-------------------------------------------------
;	a simple method to complement a binary list
(define (not-bit-list a)
  (cond ((null? a)
         ())
        (else
         (cons (not-gate (car a))
               (not-bit-list (cdr a)))))) 

;--------------------------------------------
;this method will subtract bit lists by using the alu-adder procedure

(define (sub-bit-lists a b)
  (if (or (null? a) (null? b))
      ()
      (trim (cdr (alu-adder a (alu-adder '(0 1) (not-bit-list b)))))
      ))


;-----------------------------------------------------------------
; the remainder of the form deals with testing each of the alu operations on the given binary lists

(display "adder ops")
(alu-adder '(0 1 0 1 0 1 0 1 0 1 0 1) '(0 0 1 0 1 0 1 0 1 0 0))
(alu-adder '(0 1 1 1 1 1 1 1 1 1 1 1) '(0 0 1 0 1 0 1 0 1 0 1))
(alu-adder '(0 0 0 0 0 0 0 0 0 0 0 0) '(0 1 0 1 0 1 0 1 0 1))
(alu-adder '(0 1 1 0 0 0 0 0 0 0 0 0 0) '(0 0 0 0 0 0 0 0))
(alu-adder '(0 1 1 0 1 1 1 1 1 1 1 1) '(0 1))
(newline) (display "subtraction ops")
(sub-bit-lists '(0 1 0 1 0 1 0 1 0 1 0 1) '(0 0 1 0 1 0 1 0 1 0 0))
(sub-bit-lists '(0 1 1 1 1 1 1 1 1 1 1 1) '(0 0 1 0 1 0 1 0 1 0 1))
(sub-bit-lists '(0 0 0 0 0 0 0 0 0 0 0 0) '(0 1 0 1 0 1 0 1 0 1))
(sub-bit-lists '(0 1 1 0 0 0 0 0 0 0 0 0 0) '(0 0 0 0 0 0 0 0))
(sub-bit-lists '(0 1 1 0 1 1 1 1 1 1 1 1) '(0 1))
(newline)(display "and ops")
(binary-and '(0 1 0 1 0 1 0 1 0 1 0 1) '(0 0 1 0 1 0 1 0 1 0 0))
(binary-and '(0 1 1 1 1 1 1 1 1 1 1 1) '(0 0 1 0 1 0 1 0 1 0 1))
(binary-and '(0 0 0 0 0 0 0 0 0 0 0 0) '(0 1 0 1 0 1 0 1 0 1))
(binary-and '(0 1 1 0 0 0 0 0 0 0 0 0 0) '(0 0 0 0 0 0 0 0))
(binary-and '(0 1 1 0 1 1 1 1 1 1 1 1) '(0 1))
(newline)(display "or ops")
(binary-or '(0 1 0 1 0 1 0 1 0 1 0 1) '(0 0 1 0 1 0 1 0 1 0 0))
(binary-or '(0 1 1 1 1 1 1 1 1 1 1 1) '(0 0 1 0 1 0 1 0 1 0 1))
(binary-or '(0 0 0 0 0 0 0 0 0 0 0 0) '(0 1 0 1 0 1 0 1 0 1))
(binary-or '(0 1 1 0 0 0 0 0 0 0 0 0 0) '(0 0 0 0 0 0 0 0))
(binary-or '(0 1 1 0 1 1 1 1 1 1 1 1) '(0 1))
(newline) (display "not ops")
(not-bit-list '(0 1 0 1 0 1 0 1 0 1 0 1))
(not-bit-list '(0 1 1 1 1 1 1 1 1 1 1 1))
(not-bit-list '(0 0 0 0 0 0 0 0 0 0 0 0))
(not-bit-list '(0 1 1 0 0 0 0 0 0 0 0 0 0))
(not-bit-list '(0 1 1 0 1 1 1 1 1 1 1 1))
