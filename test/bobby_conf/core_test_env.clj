(ns bobby-conf.core-test-env
  (:require [bobby-conf.core :as bc]
            [expectations :refer :all]))

;; Initialize bobby-conf for the listed environments
(bc/init :environments [:development :staging :production])

;; The environment under which the config will be evaluated
;; is read from the `APP_ENV` environment variable (and converted
;; to a Clojure keyword). If not present, it defaults to `:development`.

(expect :development
        env)

;; For each of the environments specified as arguments to `init`, a
;; predicate `def` is created, and set to `true` if it matches the value of
;; the `APP_ENV` environment variable, `false` otherwise.
(expect true
        development?)

(expect false
        staging?)

(expect false
        production?)
