(ns tic-tac-toe-pair.doo-runner
  (:require [cljs.test :as test]
            [doo.runner :refer-macros [doo-all-tests doo-tests]]
            [tic-tac-toe-pair.main-test]))

(doo-tests 'tic-tac-toe-pair.main-test)
