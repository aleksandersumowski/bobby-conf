(ns bobby-conf.core-test
  (:require [bobby-conf.core :as bc]
            [expectations :refer :all]))

;; Test setup to ensure the test config file has been created
;; with expected values.
(spit "config/test-db.clj"
      (str {:host "127.0.0.1"
            :port 6666
            :credentials {:access-key "as10on29et48uh"
                          :secret-key "XeXXHOooe849uhdk"}}))

;; Load the test config into the `'bc-test.db` namespace,
;; ready to assert against.
(bc/load-config "test-db.clj" 'bc-test.db)

;; A `def` is created for each top level key in the configuration map.
(expect "127.0.0.1"
        bc-test.db/host)

(expect 6666
        bc-test.db/port)

(expect {:access-key "as10on29et48uh"
         :secret-key "XeXXHOooe849uhdk"}
        bc-test.db/credentials)

;; For nested maps in the configuration file, defs named
;; after the hypen-separated keys of each nested value are also created.
(expect "as10on29et48uh"
        bc-test.db/credentials-access-key)

(expect "XeXXHOooe849uhdk"
        bc-test.db/credentials-secret-key)
