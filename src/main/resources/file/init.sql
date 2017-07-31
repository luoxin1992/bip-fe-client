CREATE TABLE IF NOT EXISTS tbl_fingerprint(id INTEGER PRIMARY KEY AUTOINCREMENT, event TEXT NOT NULL, extra TEXT NOT NULL, timestamp INTEGER NOT NULL, status INTEGER DEFAULT 1 NOT NULL);
CREATE INDEX IF NOT EXISTS tbl_fingerprint_idx_timestamp ON tbl_fingerprint (timestamp);

CREATE TABLE IF NOT EXISTS tbl_message(id INTEGER PRIMARY KEY AUTOINCREMENT, uid INTEGER NOT NULL, type TEXT NOT NULL, body TEXT NOT NULL, timestamp INTEGER NOT NULL, status INTEGER DEFAULT 1 NOT NULL);
CREATE INDEX IF NOT EXISTS tbl_message_idx_timestamp ON tbl_message (timestamp);

CREATE TABLE IF NOT EXISTS tbl_resource(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL, url TEXT NOT NULL, path TEXT NOT NULL, length INTEGER NOT NULL, modify TEXT NOT NULL, timestamp INTEGER NOT NULL, status INTEGER DEFAULT 1 NOT NULL);
CREATE INDEX IF NOT EXISTS tbl_resource_idx_type_url ON tbl_resource (type, url);
CREATE INDEX IF NOT EXISTS tbl_resource_idx_timestamp ON tbl_resource (timestamp);

CREATE TABLE IF NOT EXISTS tbl_preference(id INTEGER PRIMARY KEY AUTOINCREMENT, key TEXT NOT NULL, value TEXT, timestamp INTEGER NOT NULL, status INTEGER DEFAULT 1 NOT NULL);
CREATE INDEX IF NOT EXISTS tbl_preference_idx_key ON tbl_preference (key);
INSERT OR IGNORE INTO tbl_preference VALUES(1, 'backend-api', null, 0, 1);
INSERT OR IGNORE INTO tbl_preference VALUES(2, 'backend-ws', null, 0, 1);
INSERT OR IGNORE INTO tbl_preference VALUES(3, 'bind-nic-index', null, 0, 1);
INSERT OR IGNORE INTO tbl_preference VALUES(4, 'bind-counter-id', null, 0, 1);
INSERT OR IGNORE INTO tbl_preference VALUES(5, 'session-token', null, 0, 1);
INSERT OR IGNORE INTO tbl_preference VALUES(6, 'service-state', null, 0, 1);
INSERT OR IGNORE INTO tbl_preference VALUES(7, 'service-type', null, 0, 1);