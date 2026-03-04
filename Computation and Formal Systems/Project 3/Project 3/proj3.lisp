
; (.length L): Returns the number of elements in the list L.
(defun .length (L) (cond ((equalp L nil) 0) ('t (+ (.length (cdr L)) 1))))

; (.append L1 L2): Returns a list that contains the elements of L1, in the same order, followed by the elements of L2, also in the same order
(defun .append (L1 L2) (cond ((equalp L1 nil) L2) ((= (.length L1) 1) (cons (car L1) L2)) ('t (.append (cons (car L1) '()) (.append (cdr L1) L2)))))

; (.foldr L F Z): Fold-right: for list L, binary function F, and initial value Z: If the list is empty, the result is the initial value Z. If not, apply F to the first element of L and the result of folding the rest of L using F and Z
(defun .foldr (L F Z) (cond ((equalp L nil) Z) ('t (funcall F (car L) (.foldr (cdr L) F Z)))))

; (.butlast L N): Returns a list that contains all of the elements of list L other than the last N of them (that is, all the elements but the last N)
(defun .butlast (L N) (cond ((<= (.length L) N) '()) ((> (.length (cdr L)) N) (cons (car L) (.butlast (cdr L) N))) ('t (cons (car L) '()))))

; (.contains S X): Returns true (non-nil) if the set S contains the element X (test elements with equalp), otherwise false (nil)
(defun .contains (S X) (cond ((equalp S nil) nil) ((cond ((equalp (car S) X) 't) ('t (.contains (cdr S) X))))))

; (.set-equal S1 S2): Returns true (non-nil) if set S1 contains the same elements as set S2 (test elements with equalp), otherwise false (nil)
(defun .set-equal (S1 S2) (cond ((and (.subseteq S1 S2) (.subseteq S2 S1)) 't) ('t nil)))

; (.difference S1 S2): Returns the set that is the difference of sets S1 and S2 (that is: S1 \ S2, also written simply S1 − S2: the set containing all the elements of S1 that are not also elements of S2); test elements with equalp
(defun .difference (S1 S2) (cond ((.shareElements S1 S2) (cond ((.contains S2 (car S1)) (.difference (cdr S1) S2)) ('t (.difference (.append (cdr S1) (cons (car S1) '())) S2)))) ('t S1)))
        ; ...and a quick lil helper function
    (defun .shareElements (S1 S2) (cond ((equalp S1 nil) nil) ('t (cond ((.contains S2 (car S1)) 't) ('t (.shareElements (cdr S1) S2))))))

; (.subseteq S1 S1): Returns true (non-nil) if S1 is a subset of or equal to set S2 (that is, if S1 ⊆ S2), otherwise return false (nil); test elements with equalp
(defun .subseteq (S1 S2) (cond ((equalp S1 nil) 't) ((cond ((.contains S2 (car S1)) (.subseteq (cdr S1) S2)) ('t nil)))))

; (.abs N): Returns the absolute value of the given number N
(defun .abs (N) (cond ((<= N 0) (* -1 N)) ('t N)))

; (.right-tri A B C): Returns true (non-nil) if positive integers A, B, and C can be the lengths of the two sides and the hypoteneuse of a right triangle (in that order)
(defun .right-tri (A B C) (cond ((= (+ (* A A) (* B B)) (* C C)) 't) ('t nil)))

; (.mod X Y): Returns the remainder when positive integer X is divided by positive integer Y
(defun .mod (X Y) (cond ((>= X Y) (.mod (- X Y) Y)) ('t X)))

; (.nth-fibo N): Returns the Nth Fibonacci number for any non-negative integer N
(defun .nth-fibo (N) (cond ((= 0 N) 0) ((= 1 N) 1) ('t (+ (.nth-fibo (- N 1)) (.nth-fibo (- N 2))))))