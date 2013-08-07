(ns cljr-expr.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn parse-args [args]
  (into {} (map (fn [[k v]] [(keyword (.replace k "--" "")) v])
                (partition 2 args))))

(defn parse-args-two [args]
  (apply hash-map args))

(defn keywordize [kvp]
  (let [[k v] kvp]
    [(keyword (.replace k "--" "")) v]))





;; from http://java.ociweb.com/mark/clojure/article.html
(def vowel? (set "aeiou"))

(defn pig-latin [word]
  (let [first-letter (first word)]
    (if (vowel? first-letter)
      (str word "ay")
      (str (subs word 1) first-letter "ay"))))

(println (pig-latin "red"))
(println (pig-latin "orange"))

;; (doc <name>)
;; (find-doc "str_2_match")
(require 'clojure.string)
(doc clojure.string/join)  ; execute me
(clojure.string/join '(a b c))

;; Special forms - not impl in clj

;; def - bind value to symbol & define metadata
(def me 'greg)
(println me)
;; eg :dynamic allows thread-local value within scope of binding call
;; let - creates binding to vars bound to scope within stmnt -
;; name/expr pairs
;; binding - let + new thread-local values to existing global bindings

(def ^:dynamic v 1) ; v is a global binding

(defn f1 []
  (println "f1: v:" v))

(defn f2 []
  (println "f2: before let v:" v)
  ; creates local binding v that shadows global one
  (let [v 2]
    ; local binding only within this let statement
    (println "f2: in let, v:" v)
    (f1))
  ; outside of this let, v refers to global binding
  (println "f2: after let v:" v))

(defn f3 []
  (println "f3: before binding v:" v)
  ; same global binding with new, temporary value
  (binding [v 3]
    ; global binding, new value
    (println "f3: within binding function v: " v)
    (f1)) ; calling f1 with new value to v
  ; outside of binding v refers to first global value
  (println "f3: after binding v:" v))

(defn f4 []
 (def v 4)) ; changes the value of v in the global scope

(println "(= v 1) => " (= v 1))
(println "Calling f2: ")
(f2)
(println)
(println "Calling f3: ")
(f3)
(println)
(println "Calling f4: ")
(f4)
(println "after calling f4, v =" v)

;; to run code: $ clj vars.clj

;;Notice in the first call to f2, the let function's binding to v did not change its originally declared value, as is shown in the call to f1 within the let statement. The value of v in f1 is 1, not 2.
;; Next, inside f3 within the scope of the binding call, the value of v was re-assigned within f1 since f1 was called within the execution thread of binding call's scope. Once f3's function execution thread exits from the binding call, v is bound to the initially declared binding, 1.
;; When f4 is called, the binding of v is not within the context of a
;new execution thread so v is bound to the new value, 4, in the global
;scope. Remember that changing a global value is not necessarily a
;best practice. It is presented in f4's definition for demonstration
;purposes.
