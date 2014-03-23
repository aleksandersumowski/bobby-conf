(ns bobby-conf.core-test-become
  (:require [bobby-conf.core :as bc]
            [expectations :refer :all]))

;; Test setup to ensure the test config file has been created
;; with expected values.
(spit "config/locales.clj"
      (str {:en {:cat "I am a cat!"}
            :fr {:cat "Je suis un chat!"}}))

;; In some cases you might not want to namespace qualify the values from each file.
;; Instead you can make the current namespace `become` the clojure incarnation of
;; the config file. This creates defs that match the keys in the file without any
;; prefix, and as such it is advised that you use a single namespace for each file
;; you want to use with `become`.
(bc/become "config/locales.clj" :fr)

(expect "Je suis un chat!"
        cat)
