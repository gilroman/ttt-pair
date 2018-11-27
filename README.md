## Impossible TTT
### Clojure Implementation

A functional programming style implementation of Impossible TTT on the command line, in Clojure.

#### Setup

* If you don't have Clojure / Leiningen installed, you can download / install from here: https://leiningen.org/
* Clone the Impossible TTT repository
* Change directories into the `ttt-clojure` directory

#### Game Play

* Run the program with the `lein run` command.
* Follow on-screen instructions for gameplay.

#### Running API Server

* Run the server with the `lein ring server` command.

#### Running Clojurescript Front End App

* Run the clojurescript app with the `lein build-dev` command.

#### Testing

* For unit tests of the API and main Console application run the `lein test-refresh` command.
* For unit tests of the Front End:
  1. Make sure to run the command `npm install` or `yarn install` to download the necessary karma dependencies.
  2. Start the Karma server with the command `lein doo chrome-headless test` command.
  3. To show the test results save the `main_test.cljs` file once.
