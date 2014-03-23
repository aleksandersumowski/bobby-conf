# bobby-conf

Hold back the inexorable advance of [satanic forces](http://youtu.be/4mRpdbclR34)... by managing the configuration of your Clojure apps with Bobby-Conf.

## Basic usage

Add `[bobby-conf "0.0.1"]` to your `project.clj`.

Create a namespace (or namespaces) to hold your configuration, and `require` and `init` `bobby-conf` in them.

```clojure
(ns my-app.config
  (require '[bobby-conf.core :as bc]))

(bc/init :environments [:development :staging :production])

(bc/load "config/db.clj")
```

This will populate the current namespace, in this case `my-app.config`, with `def`s for each of the values in the configuration map contained in `"$PROJECT_ROOT/config/db.clj"`, with the name of each `def` given a prefix based on the name of the file.

To give a concrete example, let's say the `config/db.clj` file you loaded looks like this:

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

## Environment specific configuration

Calling `bc/init` creates a `def` called `env` in the current namespace, set to the value of the environment variable `APP_ENV` (as a keyword), or `:development` if it is not set.

Also, a predicate `def` will be defined for each of the environments specified in `init`, and will be set to true if it matches the value of `env`:

```clojure
;; Assuming APP_ENV is set to "production"
(bc/init :environments [:development :staging :production])
development? ;; => false
staging? ;; => false
production? ;; => true
```

`env` can also be used to load environment specific configuration by passing it as a qualifier to `load`:

```clojure
(bc/load "config/email.clj" env)
```

This expects the given file to have a set of submaps keyed on the environment to which they should apply, and will load configuration for the matching environment only.
So, given a `config/email.clj` that looks like this:

```clojure
{:development {:from "test@bobby-conf.com"
               :smtp "smtp.localhost"}
 :production {:from "noreply@bobby-conf.com"
              :smtp "smtp.bobby-conf.com"}}))
```

and an `APP_ENV` of `production`, the following `def`s will be created:

```clojure
email-from ;; => "noreply@bobby-conf.com"
email-smtp ;; => "smtp.bobby-conf.com"
```

The qualifier doesn't have to be `env`; it could just as easily be any other Clojure keyword, such as one representing the current locale (eg. `:en` or `:fr`).

(bc/load "config/locales.clj" :fr)

## Becoming

Sometimes, you might not want to prefix each configuration key with the name of the file.
In the locales case, for example, it might make more sense to use a namespace for that file and that file only.
If you want to do this, use `become` instead of `load` - this will add the `def`s to the current namespace without prefixing them with the filename.
If you do this with multiple config files in one namespace the chance of clashes will be much higher, so it's advised to stick to "one file, one namespace" when using `become`.

## License

Copyright © 2014 Russell Dunphy

Distributed under the Eclipse Public License
