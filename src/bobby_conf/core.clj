(ns bobby-conf.core
  (:import [java.io File]))

(defn- def-accessors
  [ns config prefix]
  (doseq [k (keys config)]
    (intern ns
            (symbol (str prefix (name k)))
            (config k))

    (when (map? (config k))
      (def-accessors ns (config k) (str prefix (name k) "-")))))

(defn load-config
  [filename ns]
  (let [ns       (create-ns ns)
        filename (str "config" File/separator filename)
        config   (load-file filename)]
    (def-accessors ns config "")))
