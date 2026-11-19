# React + Node.js + SQLite

React et Vite pour l’interface, Express sur Node.js pour l’API et SQLite pour les données. Utilise Node 20.19+ ou 22.12+.

## Démarrer en développement

Depuis ce dossier, lance l’API dans un premier terminal.

```bash
npm ci
npm run dev
```

Dans un second terminal, lance React.

```bash
cd frontend
npm ci
npm run dev
```

Ouvre http://localhost:5173. Vite relaie `/api` à Express sur le port 3000. SQLite crée `db/data.db` au lancement. Tu peux changer le fichier avec `DB_PATH`.

## Servir l’application avec Express

Depuis la racine du modèle, construis le front puis lance le serveur.

```bash
npm --prefix frontend run build
npm start
```

Ouvre http://127.0.0.1:3000.

## Fichiers à adapter

- `frontend/main.jsx` gère le formulaire et la liste React.
- `frontend/vite.config.js` configure le proxy de développement.
- `server.js` valide les requêtes et renvoie les réponses HTTP.
- `dao/index.js` exécute le SQL préparé. `db/schema.sql` définit la table.

`GET /api/v1/todos` liste les tâches. `POST /api/v1/todos` reçoit par exemple `{"title":"Réviser","priority":"NORMALE"}` et renvoie 201. `DELETE /api/v1/todos/:id` renvoie 204 si la tâche a été supprimée, 404 si elle est absente.
