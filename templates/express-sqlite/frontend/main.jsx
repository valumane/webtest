import { useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import './style.css';

const endpoint = '/api/v1/todos';

async function request(url, options) {
  const response = await fetch(url, options);
  if (!response.ok) throw new Error('HTTP ' + response.status);
  if (response.status !== 204) return response.json();
}

function App() {
  const [todos, setTodos] = useState([]);
  const [busy, setBusy] = useState(true);
  const [error, setError] = useState('');

  async function perform(action) {
    setBusy(true);
    setError('');
    try {
      await action();
    } catch (failure) {
      setError(failure.message);
    } finally {
      setBusy(false);
    }
  }

  useEffect(() => {
    perform(async () => setTodos(await request(endpoint)));
  }, []);

  function add(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const todo = Object.fromEntries(new FormData(form));
    perform(async () => {
      const created = await request(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(todo)
      });
      setTodos(previous => [created, ...previous]);
      form.reset();
    });
  }

  function remove(id) {
    perform(async () => {
      await request(endpoint + '/' + encodeURIComponent(id), { method: 'DELETE' });
      setTodos(previous => previous.filter(todo => todo.id !== id));
    });
  }

  return (
    <main>
      <h1>Liste de tâches</h1>
      <form onSubmit={add}>
        <label htmlFor="title">Tâche</label>
        <input id="title" name="title" maxLength={255} required />
        <label htmlFor="priority">Priorité</label>
        <select id="priority" name="priority">
          <option>NORMALE</option><option>URGENTE</option><option>FAIBLE</option>
        </select>
        <button disabled={busy}>Ajouter</button>
      </form>
      <p role="status">{busy ? 'Chargement…' : error || (!todos.length && 'Aucune tâche.')}</p>
      <ul>
        {todos.map(todo => (
          <li key={todo.id}>
            <span>{todo.title} · {todo.priority}</span>
            <button disabled={busy} onClick={() => remove(todo.id)}>Supprimer</button>
          </li>
        ))}
      </ul>
    </main>
  );
}

createRoot(document.querySelector('#root')).render(<App />);
