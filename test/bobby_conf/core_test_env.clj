(ns bobby-conf.core-test-env
  (:require [bobby-conf.core :as bc]
            [expectations :refer :all]))

;; Test setup to ensure the test config file has been created
;; with expected values.
(spit "config/email.clj"
      (str {:development {:from "test@bobby-conf.com"
                          :smtp "smtp.localhost"}
            :production {:from "noreply@bobby-conf.com"
                         :smtp "smtp.bobby-conf.com"}}))

;; Initialize bobby-conf for the listed environments
(bc/init :environments [:development :staging :production])

;; The environment under which the config will be evaluated
;; is read from the `APP_ENV` environment variable (and converted
;; to a Clojure keyword). If not present, it defaults to `:development`.

(expect :development
        env)

;; In this way, once `init` has been called, `env` can easily be used as
;; a qualifier when loading config files, like this:
(bc/init :environments [:development :staging :production])
(bc/load "config/email.clj" env)

(expect "test@bobby-conf.com"
        email-from)

(expect "smtp.localhost"
        email-smtp)

;; For each of the environments specified as arguments to `init`, a
;; predicate `def` is created, and set to `true` if it matches the value of
;; the `APP_ENV` environment variable, `false` otherwise.
(expect true
        development?)

(expect false
        staging?)

(expect false
        production?)
