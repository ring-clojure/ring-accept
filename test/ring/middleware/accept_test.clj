(ns ring.middleware.accept-test
  (:use clojure.test
        ring.middleware.accept)
  (:require [ring.mock.request :as mock]
            [ring.util.response :refer [get-header]]))

;; http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
;;
;; Accept         = "Accept" ":"
;; #( media-range [ accept-params ] )
;; media-range    = ( "*/*"
;;                    | ( type "/" "*" )
;;                    | ( type "/" subtype )
;;                    ) *( ";" parameter )
;; accept-params  = ";" "q" "=" qvalue *( accept-extension )
;; accept-extension = ";" token [ "=" ( token | quoted-string ) ]

(defn build-request [accept]
  (-> (mock/request :get "/")
      (mock/header "accept" accept)))

(defn get-accept [req]
  (:accept ((wrap-accept identity) req)))

(deftest test-wrap-accept
  (are [s m] (= (get-accept (build-request s)) m)
       "*/*" {"*/*" 1.0}
       "text/plain; charset=utf-8" {"text/plain; charset=utf-8" 1.0}
       "text/plain, */*" {"text/plain" 1.0, "*/*", 1.0}
       "application/json" {"application/json" 1.0}
       "application/vnd.ring.v1+json" {"application/vnd.ring.v1+json" 1.0}))
