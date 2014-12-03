(ns ring.middleware.accept)

(defn wrap-accept [handler]
  (fn [request]
    (handler request)))
