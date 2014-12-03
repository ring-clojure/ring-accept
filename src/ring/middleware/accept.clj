(ns ring.middleware.accept
  (:require [clojure.string :as str]
            [ring.util.parsing :as p]))

(def ^:private re-wildcard #"\*")

(def ^:private re-qvalue #"0(?:\.\d{0,3})?|1(?:\.0{0,3})?")

(def ^:private re-parameter
  (re-pattern (str "\\s*;\\s*" p/re-token "=" p/re-value)))

(def ^:private re-accept-params
  (re-pattern (str "\\s*;\\s*" "q=(" re-qvalue ")")))

(def ^:private re-media-range
  (re-pattern
   (str "(" re-wildcard "/" re-wildcard
        "|" p/re-token  "/" re-wildcard
        "|" p/re-token  "/" p/re-token
        "(?:" re-parameter ")*?" ")"
        "(?:" re-accept-params ")?")))

(defn- parse-media-range [media-range]
  (let [[_ range _ qvalue] (re-matches re-media-range media-range)]
    {range (Double/parseDouble qvalue)}))

(defn wrap-accept [handler]
  (fn [request]
    (handler request)))
