CREATE TABLE IF NOT EXISTS tbl_fingerprint_log(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL, content TEXT NOT NULL, timestamp INTEGER NOT NULL, status INTEGER DEFAULT 0 NOT NULL);
CREATE INDEX IF NOT EXISTS tbl_fingerprint_log_idx_timestamp ON tbl_fingerprint_log (timestamp);

CREATE TABLE IF NOT EXISTS tbl_message_log(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL, body TEXT NOT NULL, timestamp INTEGER NOT NULL, status INTEGER DEFAULT 0 NOT NULL);
CREATE INDEX IF NOT EXISTS tbl_message_log_idx_timestamp ON tbl_message_log (timestamp);

CREATE TABLE IF NOT EXISTS tbl_resource(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL, url TEXT NOT NULL, path TEXT NOT NULL, filename TEXT NOT NULL, md5 TEXT NOT NULL, timestamp INTEGER NOT NULL, status INTEGER DEFAULT 0 NOT NULL);
CREATE INDEX IF NOT EXISTS tbl_resource_idx_type_filename ON tbl_resource (type, filename);
CREATE INDEX IF NOT EXISTS tbl_resource_idx_timestamp ON tbl_resource (timestamp);