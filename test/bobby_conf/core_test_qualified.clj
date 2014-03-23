(ns bobby-conf.core-test-qualified
  (:require [bobby-conf.core :as bc]
            [expectations :refer :all]))

;; Test setup to ensure the test config file has been created
;; with expected values.
(spit "config/email.clj"
      (str {:development {:from "test@bobby-conf.com"
                          :smtp "smtp.localhost"}
            :production {:from "noreply@bobby-conf.com"
                         :smtp "smtp.bobby-conf.com"}}))

;; Providing a qualifier keyword loads only the config contained in the
;; submap identified by that key
(bc/load "email.clj" :production)

(expect "noreply@bobby-conf.com"
        email-from)

(expect "smtp.bobby-conf.com"
        email-smtp)
