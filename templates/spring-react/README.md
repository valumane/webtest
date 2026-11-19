# Spring Boot + React

Une liste de tâches avec Java 21+, Maven 3.9+, Node 20.19+ ou 22.12+, et npm.

## Démarrer en développement

Lance l’API depuis le dossier du modèle.

```bash
mvn spring-boot:run
```

Dans un second terminal, depuis le même dossier, lance le front.

```bash
cd frontend
npm ci
npm run dev
```

Ouvre http://localhost:5173. Vite relaie les requêtes `/api` à Spring sur le port 8080. H2 crée `data/todos.mv.db` au premier lancement.

## Construire et lancer ensemble

Depuis la racine du modèle, construis le front avant le JAR. Maven inclut le résultat dans les ressources statiques.

```bash
npm --prefix frontend ci
npm --prefix frontend run build
mvn package
java -jar target/template-spring-react-0.0.1-SNAPSHOT.jar
```

Ouvre http://127.0.0.1:8080.

## Où modifier le code

- `TodoController.java` gère les routes, la validation et les statuts.
- `Todo.java` définit le modèle JPA.
- `TodoRepository.java` accède à H2.
- `frontend/main.jsx` relie le formulaire et la liste à l’API.

Le contrat est le même que dans le modèle Express. `GET /api/v1/todos` liste les tâches, `POST` reçoit un JSON comme `{"title":"Réviser Spring","priority":"NORMALE"}` et `DELETE /api/v1/todos/:id` supprime la tâche.
