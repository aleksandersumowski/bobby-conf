# bobby-conf

Hold back the inexorable advance of [satanic forces](http://youtu.be/4mRpdbclR34)... by managing the configuration of your Clojure apps with Bobby-Conf.

## Usage

Add `[bobby-conf "0.0.1"]` to your `project.clj`.

Load your config files on application init, like this:

```clojure
(ns my-app.config
  (require '[bobby-conf.core :as bc]))

(bc/load-config "db.clj")
```

This will populate the current namespace, in this case `my-app.config`, with `def`s for each of the values in the configuration map contained in `"$PROJECT_ROOT/config/db.clj"`, with the name of each `def` given a prefix based on the name of the file.

To give a concrete example, let's say the configuration file you loaded looks like this:

```clojure
;; config/db.clj
{:host "127.0.0.1"
 :port 6666
 :credentials {:access-key "as10on29et48uh"
               :secret-key "XeXXHOooe849uhdk"}}
```

You now have available to you the following `def`s (shown with the values they will be bound to, given the above config):

```clojure
my-app.config/db-host ;; => "127.0.0.1"
my-app.config/db-port ;; => 6666
my-app.config/db-credentials ;; => {:access-key "as10on29et48uh" :secret-key "XeXXHOooe849uhdk"}
my-app.config/db-credentials-access-key ;; => "as10on29et48uh"
my-app.config/db-credentials-secret-key ;; => "XeXXHOooe849uhdk"
```

As you can see, nested maps can be accessed both by their top level key (returning the entire sub-map) and by joining nested key names with hyphens (to return single values).

## License

Copyright © 2014 Russell Dunphy

Distributed under the Eclipse Public License
