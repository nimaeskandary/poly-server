(ns nimaeskandary.lambda.core
  (:require [clojure.pprint :refer [pprint]])
  (:gen-class :implements com.amazonaws.services.lambda.runtime.RequestHandler)
  (:import com.amazonaws.services.lambda.runtime.Context ;
           com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent ;
           com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse
           (java.util HashMap)))

(defn -handleRequest
  [^APIGatewayV2HTTPEvent event ^Context context]
  (println "here")
  (let [logger (.getLogger context)] (.log logger "test logger"))
  (pprint event)
  (pprint context)
  (-> (APIGatewayV2HTTPResponse.)
      (.setIsBase64Encoded false)
      (.setStatusCode 200)
      (.setHeaders (HashMap. {"Content-Type" "text/html"}))
      (.setBody "hello world!")))
