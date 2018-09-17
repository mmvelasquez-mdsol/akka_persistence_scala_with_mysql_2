# Akka Persistence example with MySql akka-persistence-jdbc Plugin
Akka Persistence with MySQL configured as Event Store.
 - Akka Persistence (Write): YES
 - Akka Persistence Query (Read): YES  

Akka Persistence Plugin
- Name: aakka-persistence-jdbc
- Version: 3.4.0
- URL: https://index.scala-lang.org/dnvriend/akka-persistence-jdbc/akka-persistence-jdbc/3.4.0
- GitHub: https://github.com/dnvriend/akka-persistence-jdbc

# Create Database
```
CREATE DATABASE akka_persistence_jdbc DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON akka_persistence_jdbc.* TO akka@localhost IDENTIFIED BY 'akka' WITH GRANT OPTION;
FLUSH PRIVILEGES;
```

# Create Event Store tables    
```
DROP TABLE IF EXISTS journal;

CREATE TABLE IF NOT EXISTS journal (
  ordering SERIAL,
  persistence_id VARCHAR(255) NOT NULL,
  sequence_number BIGINT NOT NULL,
  deleted BOOLEAN DEFAULT FALSE,
  tags VARCHAR(255) DEFAULT NULL,
  message BLOB NOT NULL,
  PRIMARY KEY(persistence_id, sequence_number)
);

CREATE UNIQUE INDEX journal_ordering_idx ON journal(ordering);

DROP TABLE IF EXISTS snapshot;

CREATE TABLE IF NOT EXISTS snapshot (
  persistence_id VARCHAR(255) NOT NULL,
  sequence_number BIGINT NOT NULL,
  created BIGINT NOT NULL,
  snapshot BLOB NOT NULL,
  PRIMARY KEY (persistence_id, sequence_number)
);
```

