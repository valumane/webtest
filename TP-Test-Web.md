# TP test · React, Express et SQLite

**Durée** · 20 minutes. **Objectif** · vérifier que le formulaire React crée une tâche via Express, que SQLite la conserve et que la suppression fonctionne. À la fin, vérifie aussi la recherche dans le QCM.

## 1. Démarrer avec une base vide

Ouvre deux terminaux depuis `webtest/`. Dans le premier, démarre Express avec une base de test isolée. Tu ne modifies ainsi pas les tâches de ta base habituelle.

```bash
cd templates/express-sqlite
npm ci
export TP_TEST_DB="$(mktemp /tmp/tp-web-XXXXXX.db)"
DB_PATH="$TP_TEST_DB" npm start
```

Dans le deuxième terminal, lance React avec Vite.

```bash
cd templates/express-sqlite/frontend
npm ci
npm run dev
```

Ouvre <http://localhost:5173>. Si Vite indique un autre port, utilise l’URL affichée dans le terminal. La page doit indiquer qu’il n’y a aucune tâche.

## 2. Faire les essais dans le navigateur

Ouvre les outils de développement avec **F12**, puis l’onglet **Réseau**. Filtre sur `todos` pour repérer les appels à l’API.

| Action | Ce que tu dois observer |
| --- | --- |
| Recharge la page. | `GET /api/v1/todos` renvoie **200** et `[]`. |
| Ajoute « Tester la persistance » avec la priorité `NORMALE`. | Le `POST` renvoie **201**. Le JSON contient le titre, la priorité et un `id`. La tâche s’affiche. |
| Recharge la page. | Le `GET` renvoie la tâche et elle s’affiche de nouveau. |
| Arrête Express avec Ctrl+C dans le premier terminal, puis relance la commande ci-dessous dans ce **même terminal**. Recharge la page. | La tâche existe toujours. Express a rouvert le même fichier SQLite. |
| Clique sur « Supprimer ». | Le `DELETE` contient l’identifiant de la tâche et renvoie **204**, sans corps. La ligne disparaît. |
| Recharge la page une dernière fois. | La liste reste vide. |

```bash
DB_PATH="$TP_TEST_DB" npm start
```

## 3. Vérifier les refus côté serveur

Depuis un troisième terminal, envoie directement un titre composé d’espaces. Cette requête passe par Express sans passer par le formulaire React.

```bash
curl -i http://127.0.0.1:3000/api/v1/todos \
  -H 'Content-Type: application/json' \
  -d '{"title":"   ","priority":"NORMALE"}'
```

Attends **400 Bad Request** et un message JSON. Ensuite, demande la suppression d’un identifiant absent.

```bash
curl -i -X DELETE http://127.0.0.1:3000/api/v1/todos/tp-identifiant-absent
```

Attends **404 Not Found**. Si `curl` fonctionne et que la liste React échoue, vérifie l’URL dans Réseau et la configuration du proxy dans `frontend/vite.config.js`. Si le POST donne 400, inspecte le JSON envoyé. Si la tâche disparaît au redémarrage, vérifie que la variable `TP_TEST_DB` contient toujours le même chemin.

## 4. Vérifier le QCM et te tester

Depuis `webtest/`, cherche d’abord une question sans afficher le corrigé.

```bash
grep -iE -m1 -A3 '^Q .*SQLite' Questions-Reponses-Web.txt
```

Choisis A, B ou C, puis lance la même recherche avec `-A5` pour afficher aussi la bonne réponse et son explication. Cette commande doit compter **3 349 questions**.

```bash
grep -c '^Q \[' Questions-Reponses-Web.txt
```

## 5. Bilan à faire de mémoire

Explique dans quel fichier le navigateur gère le formulaire, où Express valide le titre et ce qui permet à la tâche de survivre au redémarrage. Tu dois pouvoir retrouver `frontend/main.jsx`, `server.js` et le fichier SQLite pointé par `DB_PATH`.

## Vérifications déjà réalisées

La compilation React réussit. Hors navigateur, Express sert la page compilée et les essais sur l’API avec SQLite couvrent la lecture, la création, les saisies invalides et la suppression. La persistance a été vérifiée en relisant le même fichier depuis un second processus Node. Le fichier QCM contient 3 349 blocs complets dont la réponse correspond à la lettre annoncée. Le test navigateur via Vite reste à faire localement, car l’environnement de vérification bloque l’ouverture de ports locaux.
