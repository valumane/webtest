import Database from 'better-sqlite3';
import { readFileSync } from 'node:fs';
import { join, resolve as pathResolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const __dirname = fileURLToPath(new URL('.', import.meta.url));

const DB_PATH = process.env.DB_PATH
  ? pathResolve(process.env.DB_PATH)
  : join(__dirname, 'data.db');

const db = new Database(DB_PATH, {
  verbose: process.env.SQL_DEBUG ? console.log : undefined,
});

const schemaPath = join(__dirname, 'schema.sql');
const schema = readFileSync(schemaPath, 'utf8');
db.exec(schema);

export default db;