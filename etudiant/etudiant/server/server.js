import express from "express";
import session from "express-session";
// randomUUID (module natif de Node) peut servir à générer les identifiants
import { randomUUID } from "node:crypto";

const app = express();
const PORT = 3000;

// Valeurs autorisées (utiles pour la validation)
const GENRES = ["roman", "science-fiction", "policier", "essai"];

// Lecture du corps des requêtes au format JSON
app.use(express.json());

// TODO 1 : configurer la session (express-session)
// TODO 2 : initialiser req.session.livres avec un tableau vide s'il n'existe pas encore


// GET /api/livres?genre=...
// Ne renvoyer que : id, titre, auteur, lu
app.get("/api/livres", (req, res) => {
    // TODO
});

// GET /api/livres/:id
app.get("/api/livres/:id", (req, res) => {
    // TODO
});

// POST /api/livres
app.post("/api/livres", (req, res) => {
    // TODO : valider le corps, créer le livre (id, lu, dateAjout), le stocker en session
});

// PATCH /api/livres/:id/lu
app.patch("/api/livres/:id/lu", (req, res) => {
    // TODO
});

// DELETE /api/livres/:id
app.delete("/api/livres/:id", (req, res) => {
    const id = req.params.id;
    // TODO
});

// TODO 3 : démarrer le serveur sur PORT
