;; 4Clojure

(= (- 10  (* 2 3)) 4)
(= "HELLO"  (.toUpperCase "hello"))
(= (list :a :b :c) '(:a :b :c))      ;; function or quoted form
(= '(1 2 3 4) (conj '(2 3 4) 1))     ;; insert to head
(= '(2 1 3 4) (conj '(3 4) 1 2))     ;; (recursion?)
(= [4 1 2 3] (conj '(1 2 3) 4))      ;; conj on seq (polymorphism)
(= [4 1 2 3] '(4 1 2 3))             ;; seq polymorphic truth
(= [1 2 3 4] (conj [1 2] 3 4))       ;; conj on vector adds to end!?
(= #{:a :b :c :d} (clojure.set/union #{:a :b :c} #{:b :c :d}))
(= #{1 2 3 4} (conj #{1 4 3} 2))     ;; conj on set adds keys
(= 20 ((hash-map :a 10, :b 20, :c 30) :b))
(= 20 (:b {:a 10, :b 20, :c 30}))

;; all clj colls support sequencing ops
; sequences are logical views of collections
; Java collections, Clj colls, strings, streams, dir stucts, XML trees
; many clj funcs rtrn lazy seq
;; first, second, last
(= 3 (first '(3 2 1)))
(= 3 (second [2 3 4]))
(= 3 (last (list 1 2 3)))
(= [20 30 40] (rest [10 20 30 40]))
(= '(6 7 8) (map #(+ % 5) '(1 2 3)))
(= '(6 7) (filter #(> % 5) '(3 4 5 6 7)))
(= (last [1 2 3 4 5]) 5)
(= (last '(5 4 3)) 3)
(= (last ["b" "c" "d"]) "d")

(map #(println %) [1 2 3]) ; nil vals are rtrn vals from 3 fnc calls
; REPL always fully evaluates, when run as script nothing is output
; force eval: first, second, nth and last dorun doall
; for rtrns lazy
; retain eval results doall / discard and only cause side effects dorun
(dorun (map #(println %) [1 2 3]))
(doseq [i [1 2 3]] (println i))         ; nil
(dorun (map #(println %) [1 2 3]))      ; nil
(doall (map #(do (println %) %) [1 2 3])) ; (1 2 3)

(defn f
  "square arg and divide by 2"
  [x]
  (println "calculating f of" x)
  (/ (* x x) 2.0))
(def f-seq (map f (iterate inc 0)))
(println "first is" (first f-seq))
(doall (take 3 f-seq))
(println (nth f-seq 2))

; diff syntax - since seq is local binding eval items are cached and
; then gced
(defn consumer [seq]
  (println (first seq))
  (println (nth seq 2)))

(consumer (map f (iterate inc 0)))

;; Lists
; efficient to add at beg
(def loyses (list "greg" "angela" "naomi"))
(def loyses (quote "greg" "angela" "naomi"))
(def loyses '("greg" "angela" "naomi"))

(some #(= % "angela") loyses)  ;true
(some #(= % "Natalia") loyses) ; nil
(some #{"greg"} loyses)        ; "greg" using set
(contains? (set loyses) "greg")

(def some '(a b c d))
(def more (conj some 'e 'f 'g))
(def some-again (remove #(=  % 'e) more))
(def all (into some some-again))  ; into superset
;; peek and pop treat list as stack op on beg/head list

;; Paredit Play
(a c d e f g)
("this is a string")
(a b c [ d e] f g) ; bar

;; vectors
; conj more efficient, finding/changing by index
; funcs use vectors to specify parameter list
; preferred over lists when add/rm front not significant
; mainly due to vec syntax being more appealing (no confusion w/ func call)
(def loyses (vector "greg" "naomi" "angela"))
(def loyses [vector "greg" "naomi" "angela"])
(get loyses 1 "dflt")    ;; NOT zero indexed
(assoc loyses 3 "sandra") ;; rtrns new vec w/ replaces or appends at
;; index. also works on maps
;; all ops on lists work on vects. peek & pop op on tail

;; sets
; usable as funcs on their items - rtrn item or nil
(def loyses (hash-set "greg" "naomi" "angela"))
(def loyses #{"greg" "naomi" "angela"})
(def loyses (sorted-set "angela" "greg" "naomi"))

(contains? loyses "greg")  ;; wrks on sets & maps
(loyses "greg")
(loyses "natalia")
(println (if (loyses person) "loyse" "reg_person"))


;; Funcs
; defn macro args: fname, [doc str] param_list fbody
; result of last expr in body is rtrnd
; all funcs rtrn val, may be nil
(= 8 ((fn add-five [x] (+ x 5)) 3))
(= 8 ((fn [x] (+ x 5)) 3))
(= 8 (#(+ % 5) 3))
(= true (#(= % "greg") "greg"))
(= 8 ((partial + 5) 3))

(defn parting
  "rtrns a String parting"
  [name]
  (str "Goodbye, " name))    ; define func
(println (parting "greg"))   ; call func
; func defs must appear before first use.
;(declare func-names)
; defn macro funcs are private - only in ns

; optional params at end
; & gathered into a list
(defn power [base & exponents]
; Using java.lang.Math static method pow.
  (reduce #(Math/pow %1 %2) base exponents)
  (power 2 3 4) ; 2 to the 3rd = 8; 8 to the 4th = 4096
  )

;; dflt vals
(defn parting
  "rtrns a String parting in a given lang"
  ([] (parting "World"))
  ([name] (parting name "en"))
  ([name language]
     (condp = language
       "en" (str "Goodby, " name)
       "ed" (str "Adios, " name)
       (throw (IllegalARgumentException))

       (= (fn [x] (* x 2)) 4))))
(=  (fn [x] (+ "Hello, " x) ())) ;; ??

(= 10 (let [z 1, y 3, x 7] (+ x y)))
(= 10 (let [z 1, y 3, x 7] (+ y z)))
(= 10 (let [z 1, y 3, x 7] z))

(= "ABC" (apply str (re-seq #"[A-Z]+" "bA1B3Ce"))) ;; why??

(= 15 (reduce + [1 2 3 4 5]))
(= 0 (reduce + []))
(= 6 (reduce + 1 [2 3]))  ;; why?

;; simple recursion
(= '(5 4 3 2 1) ((fn foo [x]
                   (when (> x 0)
                     (conj (foo (dec x))
                           x)))
                 5))

;; rearranging code - readability
;; ->   macro threads expression through variable nmbr of forms
(= (last (sort (rest (reverse [2 5 4 1 3 6]))))
   (-> [2 5 4 1 3 6] reverse rest sort last)
   5)
;; --> 
;;(= ((fn [x] (reduce + x)) (map inc (take 3 (drop 2 [2 5 4 1 3 6]))))   (->> [2 5 4 1 3 6] (drop 2) (take 3) (map inc) (__)) 11)

;; recur - only non-stack consuming looping construct
;; recursion
;(= __ (loop [x 5 result []]     (if (> x 0)       (recur (dec x) (conj result (+ 2 x)))       result)))

;; maps
(true? ((fn [x] ) :a {:a nil :b 2}))


;; for
;; excellent for seq -> seq wrangling
(= (for [x (range 40)
         :when (= 1 (rem x 4))]
     x))
(= __ (for [x (iterate #(+ 4 %) 0)
            :let [z (inc x)]
            :while (< z 40)]
        z))
(= __ (for [[x y] (partition 2 (range 20))]
        (+ x y)))  ; list comp

;; comparisons
;; semantically defined for any orderable data type
(= :gt ((fn [x y z] (<))  < 5 1))

;; Collection Abstraction
;; Core types: Maps Vectors Lists Sets
;; ops: = count conj empty seq


(type (str "greg" 1 2 3))  ; java.lang.String

; list comp - macros: for doseq
; opt filtering :when :while expressions

; dorun func forces eval of lazy seq rtrnd by for macro
(dorun
 (for [col "ABCD" :when (not= col \B)
       row (range 1 4) :while (< row 3)]
   (println (str col row))))

(doseq [col "ABCD" :when (not= col \B)
        row (range 1 4) :while (< row 3)]
  (println (str col row)))
; Metadata

; Predicate funcs - rtrn true or nil
; eval to nil returns nil everything else evals to true
; Type obj predicates: class? coll? decimal? macro? isa? string?
; seq? set? vector? number?
; other non predicate funcs perform reflection: ancestors, bases,
; class, nspublics, parents
; predicate funcs:
; test relationships between values  < <= = not= == > >= compare
; distinc? identical?
; test seqs empty? not-empty every? not-every? some not-any?
; test numbers: even? neg? odd? pos? zero?

