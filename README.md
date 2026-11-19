# TP d'entraînement : Bibliothèque (Node.js + Express + React/Vite)

**Simulation d'examen — durée : 2 heures** — mêmes conditions que le TP noté : documents de cours autorisés, pas d'IA générative.

## Objectif

Réaliser une application web permettant de gérer une petite bibliothèque personnelle de livres.
Les livres sont stockés **dans la session Express** (pas de base de données).

Vous utiliserez :

- **Node.js + Express** (+ `express-session`) pour construire l'API ;
- **React + Vite** pour l'interface utilisateur.

Le code fourni est incomplet : repérez les `TODO` et complétez-les.

## Description de l'application

### Côté serveur

Initialiser une session contenant un tableau `livres` (vide à la première visite), puis proposer **cinq routes** :

| Méthode | Route | Rôle | Succès | Erreurs |
|---|---|---|---|---|
| GET | `/api/livres` | Liste des livres. **N'envoyer que `id`, `titre`, `auteur`, `lu`.** Paramètre optionnel `?genre=` pour filtrer. | 200 | 400 si `genre` inconnu |
| GET | `/api/livres/:id` | Un livre complet | 200 | 404 si introuvable |
| POST | `/api/livres` | Ajoute un livre | 201 + livre créé | 400 + liste des erreurs |
| PATCH | `/api/livres/:id/lu` | Marque un livre comme lu / non lu. Corps : `{ "lu": true }` | 200 + livre modifié | 400 / 404 |
| DELETE | `/api/livres/:id` | Supprime un livre | 204 | 404 si introuvable |

Un livre contient :

| Champ | Type | Remarque |
|---|---|---|
| `id` | string | généré **par le serveur** |
| `titre` | string | obligatoire, 1 à 150 caractères (espaces de début/fin ignorés) |
| `auteur` | string | obligatoire, 1 à 100 caractères (espaces de début/fin ignorés) |
| `genre` | string | `"roman"`, `"science-fiction"`, `"policier"` ou `"essai"` |
| `annee` | number | entier, compris entre 1000 et l'année en cours |
| `lu` | boolean | vaut `false` à la création (non fourni par le client) |
| `dateAjout` | string | date d'enregistrement au format ISO, générée par le serveur |

Le serveur écoute sur le port **3000** (le client Vite redirige `/api` vers ce port).

### Côté client

- Un **formulaire** d'ajout (titre, auteur, genre, année). Les erreurs renvoyées par l'API sont affichées.
- Une **liste** des livres affichant le titre, l'auteur et l'état (lu / non lu) (**pas le genre, ni l'année**).
- Un **filtre** par genre (tous / roman / science-fiction / policier / essai) : il doit passer par le paramètre `?genre=` de l'API.
- Pour chaque livre : un bouton de **suppression** et un moyen de **marquer comme lu / non lu** (bouton ou case à cocher).
- Au clic sur un livre, un encadré apparaît **sous la liste** avec toutes ses informations (titre, auteur, genre, année, état de lecture, date d'ajout), récupérées via `GET /api/livres/:id`.
- Après chaque action (ajout, changement d'état de lecture, suppression), l'affichage se **recharge dynamiquement**, sans rafraîchir la page. Si le livre affiché dans l'encadré est supprimé, l'encadré disparaît.

## Structure du projet fourni

```
etudiant/
  README.md            → énoncé du TP
  server/
    package.json       → configuration serveur (complète)
    server.js          → serveur Express (incomplet)
  client/
    package.json       → configuration client (complète)
    index.html
    vite.config.js
    src/
      main.jsx         → point d'entrée React (complet)
      App.jsx          → interface React (incomplète)
```

## Lancer le projet

Serveur (terminal 1) :

```bash
cd server
npm install
node server.js
```

Client (terminal 2) :

```bash
cd client
npm install
npm run dev
```

Ouvrir http://localhost:5173

## Attendus

**Vous devez fournir un code commenté expliquant chacune des instructions.**
Un code non commenté sera pénalisé.

### Fonctionnel (16 points)

1. Configuration de la session Express (2 pts)
2. Routes `GET /api/livres` (avec filtre), `GET /api/livres/:id`, `POST`, `DELETE` fonctionnelles, avec les bons codes HTTP (4 pts)
3. Route `PATCH /api/livres/:id/lu` fonctionnelle (2 pts)
4. Formulaire fonctionnel permettant d'envoyer un livre (2 pts)
5. Client React affichant la liste et le filtre par genre (2 pts)
6. Consultation d'un livre (encadré de détail) (2 pts)
7. Suppression et changement d'état de lecture depuis la liste (2 pts)

### Qualité du code (4 points)

1. Validation des données reçues par l'API (2 pts)
2. Code clair, propre, indenté, structuré (2 pts)

## Rendu

Une archive (sans `node_modules`) contenant les dossiers `server` et `client`.
