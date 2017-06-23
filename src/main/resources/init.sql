CREATE TABLE IF NOT EXISTS tbl_fingerprint(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL, content TEXT NOT NULL, timestamp TEXT NOT NULL, status INTEGER DEFAULT 0 NOT NULL);
CREATE INDEX IF NOT EXISTS tbl_fingerprint_idx_type ON tbl_fingerprint (type);
CREATE INDEX IF NOT EXISTS tbl_fingerprint_idx_timestamp ON tbl_fingerprint (timestamp);

CREATE TABLE IF NOT EXISTS tbl_message(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL, body TEXT NOT NULL, timestamp TEXT NOT NULL, status INTEGER DEFAULT 0 NOT NULL);
CREATE INDEX IF NOT EXISTS tbl_message_idx_type ON tbl_message (type);
CREATE INDEX IF NOT EXISTS tbl_message_idx_timestamp ON tbl_message (timestamp);

CREATE TABLE IF NOT EXISTS tbl_resource(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL, url TEXT NOT NULL, path TEXT NOT NULL, timestamp TEXT NOT NULL, filename TEXT NOT NULL, md5 TEXT NOT NULL, status INTEGER DEFAULT 0 NOT NULL);
CREATE INDEX IF NOT EXISTS tbl_resource_idx_type_filename ON tbl_resource (type, filename);
CREATE INDEX IF NOT EXISTS tbl_resource_idx_timestamp ON tbl_resource (timestamp);