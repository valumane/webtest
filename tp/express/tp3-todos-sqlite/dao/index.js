import db from '../db/index.js';

const stmtList = db.prepare(`
  SELECT id, title, priority, createdAt
  FROM todos
  ORDER BY datetime(createdAt) DESC
`);

const stmtGet = db.prepare('SELECT id FROM todos WHERE id = ?');
const stmtInsert = db.prepare('INSERT INTO todos (id, title, priority, createdAt) VALUES (?,?,?,?)');
const stmtDelete = db.prepare('DELETE FROM todos WHERE id = ?');

export const dao = {
  list() { return stmtList.all(); },
  get(id) { return stmtGet.get(id); },
  create(todo) { stmtInsert.run(todo.id, todo.title, todo.priority, todo.createdAt); },
  delete(id) { return stmtDelete.run(id).changes; }
};