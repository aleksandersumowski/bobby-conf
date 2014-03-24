(ns bobby-conf.core
  (:refer-clojure :exclude [load])
  (:import [java.io File]))

(defn- nested-merge
  [a b]
  (merge-with
    (fn [a b]
      (if (and (map? a)
               (map? b))
        (nested-merge a b) b))
    a b))

(defn- config
  [filename & qualifiers]
  (reduce (fn [c q] (nested-merge (:default c {})
                                  (c q)))
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

(defn- env
  []
  (keyword (get (System/getenv)
                "APP_ENV"
                "development")))

(defn- predicate-name
  [env]
  (symbol (str (name env) "?")))

(defmacro init
  "Initialize configuration in the current namespace.
   Creates a `def` called `env` which returns the value of
   the `APP_ENV` environment variable as a Clojure keyword,
   defaulting to `:development` if it is not set.

   Takes a keyed set of parameters in the following format:
  `(init :environments [:development :staging :production]
         :scoped [\"config/db.clj\" \"config/s3.clj\"]
         :shared [\"config/airbrake.clj\"])`

   Where:

  `:environments` is a list of the environments your
   program will be run in (a predicate value will be defined
   for each one, and will be set to true if it matches the
   value of the `APP_ENV` environment variable.

  `:scoped` is a list of config files that are scoped by
   environment (that is, have top level keys matching the names
   of the possible environments, plus an optional `:default` key
   which acts as you would expect).

  `:shared` is a list of config files that are not scoped -
   their values are shared between all environments in which the
   program is run."
  [& {envs   :environments
      scoped :scoped
      shared :shared}]
  `(do
     (intern *ns* ~''env ~(env))

     (doseq [e# ~envs]
       (intern *ns*
               (~predicate-name e#)
               (= e# ~'env)))

     (doseq [c# ~scoped]
       (load c# ~(env)))

     (doseq [c# ~shared]
       (load c#))))
