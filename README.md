# bobby-conf

Hold back the inexorable advance of [satanic forces](http://youtu.be/4mRpdbclR34)... by managing the configuration of your Clojure apps with Bobby-Conf.

## Usage

Add `[bobby-conf "0.0.1"]` to your `project.clj`.

Load your config files on application init, like this:

```clojure
(require '[bobby-conf.core :as bc])
(bc/load-config "db.clj" 'my-app.conf.db)
```

This will create the namespace specified by `load`'s second argument, in this case `my-app.conf.db`, and populate it with accessor functions for the values in the configuration map contained in `"$PROJECT_ROOT/config/db.clj"`.

To give a concrete example, let's say you have a configuration file like the following:

```clojure
;; config/db.clj
{:host "127.0.0.1"
 :port 6666
 :credentials {:access-key "as10on29et48uh"
               :secret-key "XeXXHOooe849uhdk"}}
```

...which you load using `bobby-conf` like this:

```clojure
(bc/load-config "db.clj" 'my-app.conf.db)
```

You now have available to you the following values (shown with the values they will be bound to, given the current config):

```clojure
my-app.conf.db/host ;; => "127.0.0.1"
my-app.conf.db/port ;; => 6666
my-app.conf.db/credentials ;; => {:access-key "as10on29et48uh" :secret-key "XeXXHOooe849uhdk"}
my-app.conf.db/credentials-access-key ;; => "as10on29et48uh"
my-app.conf.db/credentials-secret-key ;; => "XeXXHOooe849uhdk"
```

As you can see, nested maps can be accessed both by their top level key (returning the entire sub-map) and by joining nested key names with hyphens (to return single values).

## License

Copyright © 2014 Russell Dunphy

Distributed under the Eclipse Public License
