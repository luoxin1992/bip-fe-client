CREATE TABLE IF NOT EXISTS tbl_message(id INTEGER PRIMARY KEY AUTOINCREMENT, uid INTEGER NOT NULL, type TEXT NOT NULL, body TEXT NOT NULL, timestamp INTEGER NOT NULL, status INTEGER DEFAULT 1 NOT NULL);
CREATE INDEX IF NOT EXISTS tbl_message_idx_uid ON tbl_message (uid);
CREATE INDEX IF NOT EXISTS tbl_message_idx_timestamp ON tbl_message (timestamp);