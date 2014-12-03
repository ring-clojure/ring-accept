(ns ring.middleware.accept
  (:require [clojure.string :as str]
            [ring.util.parsing :as p]))

(def ^:private re-parameter
  (re-pattern (str "\\s*;\\s*(" p/re-token ")=(" p/re-value ")")))

(def ^:private re-wildcard #"\*")

(def ^:private re-media-range
  (re-pattern
   (str "(" re-wildcard "/" re-wildcard
        "|" p/re-token  "/" re-wildcard
        "|" p/re-token  "/" p/re-token ")"
        "((?:" re-parameter ")*)")))

(defn- parse-parameters [params]
  (into {} (for [[_ name value] (re-seq re-parameter params)]
             [name value])))

(defn- parse-media-range [media-range]
  (let [[_ range params] (re-matches re-media-range media-range)]
    [range (parse-parameters params)]))

(defn wrap-accept [handler]
  (fn [request]
    (handler request)))
