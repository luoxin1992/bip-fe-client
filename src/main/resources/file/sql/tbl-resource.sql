CREATE TABLE IF NOT EXISTS tbl_resource(id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT NOT NULL, url TEXT NOT NULL, path TEXT NOT NULL, length INTEGER NOT NULL, modify TEXT NOT NULL, timestamp INTEGER NOT NULL, status INTEGER DEFAULT 1 NOT NULL);
CREATE INDEX IF NOT EXISTS tbl_resource_idx_type_url ON tbl_resource (type, url);