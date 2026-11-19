import express from 'express';
import { dao } from './dao/index.js';

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());
app.use(express.static('public'));

const PRIORITIES = new Set(['URGENTE', 'NORMALE', 'FAIBLE']);

function uid() {
    return Math.random().toString(36).slice(2, 10);
}

app.get('/api/v1/todos', (req, res) => {
    res.json(dao.list());
});

app.post('/api/v1/todos', (req, res) => {
    const { title, priority } = req.body || {};

    if (typeof title !== 'string' || title.trim() === '') {
        return res.status(400).json({
            error: 'title is required (non-empty string)'
        });
    }

    if (!PRIORITIES.has(priority)) {
        return res.status(400).json({
            error: 'priority must be URGENTE | NORMALE | FAIBLE'
        });
    }

    const todo = {
        id: uid(),
        title: title.trim(),
        priority,
        createdAt: new Date().toISOString()
    };

    try {
        dao.create(todo);
        return res.status(201).json(todo);
    } catch (e) {
        console.error(e);
        return res.status(500).json({ error: 'DB insert failed' });
    }
});

app.delete('/api/v1/todos/:id', (req, res) => {
    try {
        const changes = dao.delete(req.params.id);

        if (changes === 0) {
            return res.status(404).json({ error: 'Not Found' });
        }

        return res.status(204).end();
    } catch (e) {
        console.error(e);
        return res.status(500).json({ error: 'DB delete failed' });
    }
});

app.listen(PORT, () => {
    console.log(`API ready on http://localhost:${PORT}`);
});