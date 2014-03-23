(ns bobby-conf.core-test-qualified
  (:require [bobby-conf.core :as bc]
            [expectations :refer :all]))

;; Test setup to ensure the test config file has been created
;; with expected values.
(spit "config/locales.clj"
      (str {:en {:cat "I am a cat!"}
            :fr {:cat "Je suis un chat!"}}))

;; Providing a qualifier keyword loads only the config contained in the
;; submap identified by that key
(bc/load "config/locales.clj" :fr)

(expect "Je suis un chat!"
        locales-cat)
