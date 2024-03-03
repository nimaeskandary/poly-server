(ns nimaeskandary.db.interface.sql-db-test
  (:require [nimaeskandary.testing.system.db :as db]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as result-set]
            [honey.sql :as sql]
            [honey.sql.helpers :as h]
            [com.stuartsierra.component :as component]
            [clojure.test :refer [deftest testing is]]))

(deftest sql-db-test-no-migrations
  (let [system
        (-> (component/system-map :db (db/create-in-memory-db "test"))
            (component/system-using {:db []})
            component/start)
        db-component (:db system)]
    (testing "component start adds datasource and jdbc url"
      (is (some? (:datasource db-component)))
      (is (some? (:jdbcUrl db-component))))
    (testing "does not get migration settings because no migratus config provided"
      (is (nil? (:migratus-settings db-component))))
    (testing "jdbcUrl gets extra options in url from db spec"
      (is (some? (re-find #".*MODE=PostgreSQL$" (:jdbcUrl db-component)))))
    (testing "")
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
                             result-set/as-unqualified-lower-maps})))
      (jdbc/execute! db-component ["DROP ALL OBJECTS"]))
    (testing "component stop removes datasource"
      (is (nil? (-> (component/stop system)
                    :db
                    :datasource))))))

(deftest sql-db-test-with-migrations
  (let [system
        (-> (component/system-map :db (db/create-in-memory-db "test" {:migration-table-name "migrations" :migrations-dir "db/resources/test_migrations"}))
            (component/system-using {:db []})
            component/start)
        db-component (:db system)]
    (testing "does get migration settings because migratus config provided"
      (is (some? (:migratus-settings db-component))))
    (testing "can execute statements against created table"
      (->> (-> (h/insert-into :testing)
               (h/values [{:id 1}])
               (sql/format))
           (jdbc/execute! db-component)))))
