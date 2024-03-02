(ns nimaeskandary.db.interface.sql-test
  (:require [nimaeskandary.testing.system.db :as db]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as result-set]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [com.stuartsierra.component :as component]
            [clojure.test :refer [deftest testing is]]))

(deftest sql-db-test
  (let [system
        (-> (component/system-map :db (db/create-in-memory-postgres-db "test"))
            (component/system-using {:db []})
            component/start)
        db-component (:db system)]
    (testing "component start adds datasource"
      (is (some? (:datasource db-component))))
    (testing "jdbcUrl gets options"
      (is (some? (re-find #".*MODE=PostgreSQL$" (:jdbcUrl db-component)))))
    (testing "can execute statements"
      (jdbc/execute! db-component ["DROP ALL OBJECTS"])
      (->> (-> (h/create-table :test)
               (h/with-columns [:id :int] [:name :text])
               (sql/format))
           (jdbc/execute! db-component))
      (->> (-> (h/insert-into :test)
               (h/values [{:id 1, :name "test-name"}])
               (sql/format))
           (jdbc/execute! db-component))
      (is (= [{:id 1, :name "test-name"}]
             (jdbc/execute! db-component
                            (-> (h/select :*)
                                (h/from :test)
                                (sql/format))
                            {:builder-fn
                             result-set/as-unqualified-lower-maps}))))
    (testing "component stop removes datasource"
      (is (nil? (-> (component/stop system)
                    :db
                    :datasource))))))
