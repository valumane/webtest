CREATE TABLE IF NOT EXISTS todos (
  id TEXT PRIMARY KEY,
  title TEXT NOT NULL,
  priority TEXT NOT NULL CHECK (priority IN ('URGENTE','NORMALE','FAIBLE')),
  createdAt TEXT NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_todos_createdAt ON todos(createdAt);