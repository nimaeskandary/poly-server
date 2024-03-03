# db

## interface

### nimaeskandary.db.interface.sql

* provides a function to create a sql db connection pool, e.g.
```clojure
(create-sql-db
    {:db-spec
     {:dbtype "postgres", :dbname "app", :host "localhost", :port "55432"},
     :pool-config {:username "postgres", :password "password"}
     :migratus-config {:migrations-dir "db/migrations/app_db"
                       :migration-table-name "migrations"}})
```



* if `migratus-config` is provided in the component config, then migrations will be run on component start
