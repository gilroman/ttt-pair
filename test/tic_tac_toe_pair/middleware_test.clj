(ns tic-tac-toe-pair.middleware-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [tic-tac-toe-pair.api :refer [app]]
            [tic-tac-toe-pair.middleware :refer :all]))

(deftest wrap-500-catchall-test
  (testing "when a handler throws an exception"
    (let [response (app (mock/request :post "/move" "x@1"))]
      (testing "the status code is 500"
        (is (= 500 (:status response))))
      (testing "and the body only contains the exception message"
        (is (= "No method in multimethod 'get-api-move' for dispatch value: null" (:body response)))))))