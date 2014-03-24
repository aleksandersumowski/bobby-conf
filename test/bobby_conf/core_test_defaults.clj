(ns bobby-conf.core-test-defaults
  (:require [bobby-conf.core :as bc]
            [expectations :refer :all]))

;; Test setup to ensure the test config file has been created
;; with expected values.
(spit "config/db-with-defaults.clj"
      (str {:default {:host "127.0.0.1"
                      :port 8008
                      :credentials {:user "root"}}
            :development {:port 7777
                          :credentials {:password "Pa55word1"}
                          :metadata {:x 14 :y 12}}
            :production {:credentials {:password "r34allyS3cur3P455w0rd"}}}))

(bc/init :environments [:development :production])

(bc/become "config/db-with-defaults.clj" env)

;; Values in the default map are created as `def`s
(expect "127.0.0.1" host)

;; If a value is in both the default and the specific map,
;; the specifi version wins.
(expect 7777 port)

;; Nested maps are merged in the same way as the top-level one.
(expect "root" credentials-user)
(expect "Pa55word1" credentials-password)
(expect 14 metadata-x)
(expect 12 metadata-y)
