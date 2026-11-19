import express from 'express';
import { randomUUID } from 'node:crypto';
import { fileURLToPath } from 'node:url';
import { dao } from './dao/index.js';

const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());
app.use(express.static(fileURLToPath(new URL('./frontend/dist/', import.meta.url))));

const PRIORITIES = new Set(['URGENTE', 'NORMALE', 'FAIBLE']);

app.get('/api/v1/todos', (req, res) => {
    res.json(dao.list());
});

app.post('/api/v1/todos', (req, res) => {
    const { title, priority } = req.body || {};

    if (typeof title !== 'string' || title.trim() === '' || title.trim().length > 255) {
        return res.status(400).json({
            error: 'Titre de 1 à 255 caractères requis'
        });
    }

    if (!PRIORITIES.has(priority)) {
        return res.status(400).json({
            error: 'priority must be URGENTE | NORMALE | FAIBLE'
        });
    }

    const todo = {
        id: randomUUID(),
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

// Les erreurs de parsing et les erreurs inattendues restent au format JSON.
app.use((error, req, res, next) => {
    if (res.headersSent) return next(error);
    if (error.type === 'entity.parse.failed') {
        return res.status(400).json({ error: 'JSON invalide' });
    }
    console.error(error);
    return res.status(500).json({ error: 'Erreur serveur' });
});

app.listen(PORT, '127.0.0.1', error => {
    if (error) {
        console.error('Impossible de démarrer le serveur :', error.message);
        process.exitCode = 1;
        return;
    }
    console.log(`API prête sur http://127.0.0.1:${PORT}`);
});