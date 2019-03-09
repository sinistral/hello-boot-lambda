# Hello, ([Boot]) [lambda][aws lambda]

A minimal "hello, world" project using [AWS Lambda], [ClojureScript]
and [Boot].  It illustrates the barest minimum needed to produce a `.zip`
package that can be used to deploy a functional AWS Lambda function.

For a more comprehensive [leiningen-based][lein] alternative, see
[cljs-lambda].

## Build and test

```
$ boot +test
$ boot +build
```

## Configuration

In the configuration for the AWS Lambda function, the **Handler** should be
defined as `hellolambda.handler`.  The function can be tested from the AWS
Lambda console using input data of the form: `{"name":"lambda"}`.

## License

Published under the [2-clause BSD license][license].

[clojurescript]: http://clojure.org/about/clojurescript
[aws lambda]:    https://aws.amazon.com/lambda/
[boot]:          http://boot-clj.com
[cljs-lambda]:   https://github.com/nervous-systems/cljs-lambda
[lein]:          http://leiningen.org
[license]:       https://opensource.org/licenses/BSD-2-Clause
