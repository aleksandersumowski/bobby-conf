(ns bobby-conf.core-test-init
  (:require [bobby-conf.core :as bc]
            [expectations :refer :all]))

;; It is possible to define the list of environments, scoped configs and
;; shared configs all in one go using `init`.
(bc/init :scoped ["config/email.clj" "config/db-with-defaults.clj"]
         :shared ["config/db.clj"]
         :environments [:development :staging :production])

(expect true (development?))
(expect false (staging?))

(expect "127.0.0.1" db-host)
(expect "test@bobby-conf.com" email-from)
