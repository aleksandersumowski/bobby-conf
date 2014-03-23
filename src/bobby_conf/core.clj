(ns bobby-conf.core
  (:refer-clojure :exclude [load])
  (:import [java.io File]))

(defn- config
  [filename & qualifiers]
  (reduce (fn [c q] (c q))
          (load-file filename)
          qualifiers))

(defn- basename
  [filename]
  (clojure.string/replace filename #"(.*/|.*\\|^)([^/\\]+)" "$2"))

(defn- strip-extension
  [filename]
  (clojure.string/replace filename #"\.[^\.]+$" ""))

(defn- prefix
  [filename]
  (-> filename
      basename
      strip-extension
      (str "-")))

(defn- create-defs!
  [ns config prefix]
  (doseq [k (keys config)]
    (intern ns
            (symbol (str prefix (name k)))
            (config k))

    (when (map? (config k))
      (create-defs! ns
                    (config k)
                    (str prefix (name k) "-")))))

(defmacro load
  [filename & qualifiers]
  `(~create-defs! *ns*
                  (~config ~filename ~@qualifiers)
                  (~prefix ~filename)))

(defmacro become
  [filename & qualifiers]
  `(~create-defs! *ns*
                  (~config ~filename ~@qualifiers)
                  ""))

(defn env
  []
  (keyword (get (System/getenv)
                "APP_ENV"
                "development")))

(defn predicate-name
  [env]
  (symbol (str (name env) "?")))

(defmacro init
  [& {envs :environments}]
  `(do
     (intern *ns* ~''env ~(env))
     (doseq [e# ~envs]
       (intern *ns*
               (~predicate-name e#)
               (= e# ~'env)))))
