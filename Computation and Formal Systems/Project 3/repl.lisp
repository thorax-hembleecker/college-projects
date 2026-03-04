;;;;
;;;; File: repl.lisp
;;;; Creator: George F.
;;;;
;;;; This implementation does not use LET, SETQ, or LOOP.
;;;; Variable bindings are established via function calls.
;;;; You can use the TEST function to run the required example(s),
;;;; and then use the REPL function to let us test a function.
;;;; Enjoy.
;;;;

(defun test (f args)
  "Evaluate the function whose name is the symbol F on list of arguments
ARGS and print an informative message."
  (format t "~S => ~S~%" (cons f args) (apply f args)))

(defun repl (f)
  "Run the function whose name is the symbol F in a Read-Eval-Print Loop."
  (format t "Testing ~A~%" (symbol-name f))
  (repl1 f))

(defun repl1 (f)
  (format t "Enter arguments for ~A (enter a list, or ! to stop): " (symbol-name f))
  (finish-output t)
  (repl2 f (read)))

(defun repl2 (f args)
  (cond
    ((eq args '!)
     t)
    (t
     (test f args)
     (repl1 f))))

  
