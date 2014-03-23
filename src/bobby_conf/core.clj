(ns bobby-conf.core
  (:refer-clojure :exclude [load])
  (:import [java.io File]))

(defn- filepath
  [filename]
  (str "config" File/separator filename))

(defn- config
  [filename & qualifiers]
  (reduce (fn [c q] (c q))
          (load-file (filepath filename))
          qualifiers))

(defn- prefix
  [filename]
  (str (clojure.string/replace filename #"\.[^\.]+$" "") "-"))

(defn- create-defs!
  [ns config prefix]
  (doseq [k (keys config)]
    (intern ns
            (symbol (str prefix (name k)))
            (config k))

    (when (map? (config k))
      (create-defs! ns (config k) (str prefix (name k) "-")))))

(defmacro load
  [filename & qualifiers]
  `(~create-defs! *ns*
                  (~config ~filename ~@qualifiers)
                  (~prefix ~filename)))

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
