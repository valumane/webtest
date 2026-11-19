# TP Tirage au sort — Spring Boot + H2 + Login

Projet Java **autonome** basé sur le précédent système de tirage `LDController` + `Content`.

## Prérequis et démarrage

- Java 21 et Maven 3.9+.
- Dans le dossier contenant `pom.xml` :

```bash
mvn spring-boot:run
```

- Page de connexion : <http://localhost:8080/>
- Tirage après connexion : <http://localhost:8080/tirage>
- Console H2 : <http://localhost:8080/h2-console>

### Comptes de démo

Identifiants de groupe de **groupe1** à **groupe12**. Mot de passe provisoire : **demo123** pour tous les comptes.

Ex. `groupe1` / `demo123`. Le numéro de groupe est enregistré dans `app_users` et est obtenu depuis la **session authentifiée**, jamais via un paramètre envoyé par le navigateur.

### Connexion à H2

Dans le formulaire de la console H2, saisir :

- JDBC URL : `jdbc:h2:file:./data/demo;DB_CLOSE_ON_EXIT=FALSE`
- Username : `sa`
- Password : `password`

Le chemin `./data/demo` est relatif au **répertoire de lancement**. Créez au besoin `data/` (le dossier existe dans cette archive).

Requêtes de vérification :

```sql
SELECT * FROM APP_USERS;
SELECT * FROM TIRAGES;
```

## Fonctionnement du tirage

- 12 groupes et 12 sujets numérotés de 1 à 12.
- Chaque groupe peut déclencher un tirage **une seule fois**.
- Un sujet déjà attribué n'est plus dans le choix possible.
- `Content` utilise `TirageRepository` (JPA/H2), et non une simple `Map`.
- Les colonnes `groupe` et `sujet` sont chacune `UNIQUE` en base.
- Sur **une instance** du serveur, les tirages concurrents sont sérialisés avec un verrou et une transaction englobant jusqu'au commit. Pour un déploiement multiserveur, prévoir un verrou côté BD ou une stratégie de retry.
- Après redémarrage, les résultats restent dans `data/demo.mv.db`.

### Routes

| Route | Accès | Fonction |
|---|---|---|
| `GET /` | public | connexion |
| `POST /login` | public | traitement de connexion Spring Security |
| `GET /tirage` | connecté | vue du tirage pour le groupe |
| `POST /tirage` | connecté | tirage (formulaire CSRF) |
| `POST /logout` | connecté | déconnexion |
| `GET /api/message` | connecté | route de l'ancien contrôleur |
| `GET /api/tirage` | connecté | JSON du groupe (`sujet` null si non tiré) |
| `POST /api/tirage` | connecté | déclenche le tirage ; nécessite CSRF |
| `GET /h2-console` | local | console H2 (dev uniquement) |

## Sécurité et déploiement

**Projet de TP / développement local uniquement**. Retirer les comptes de démonstration, remplacer les mots de passe, désactiver la console H2 et gérer les secrets hors des propriétés avant une vraie mise en production. La console H2 est configurée avec `web-allow-others=false` pour refuser les accès distants. Pour la console seulement, le filtre Spring Security désactive CSRF et autorise `X-Frame-Options: SAMEORIGIN`, conformément à la documentation Spring Boot.

Les formulaires HTML Thymeleaf incluent le jeton CSRF. Si vous appelez `POST /api/tirage` en JS ou `curl`, il faut fournir le jeton CSRF et la session valide.

## Architecture

- `modele/AppUser.java` et `modele/Tirage.java` : tables JPA.
- `repository/` : accès SQL automatique via Spring Data JPA.
- `modele/Content.java` : logique du tirage et transaction.
- `config/SecurityConfig.java` : authentification et console H2.
- `controller/PageController.java` : vues Thymeleaf et formulaire.
- `controller/LDController.java` : API JSON existante prolongée.

Si votre précédent TP Spring utilise un autre package Java, adapter le package de cette archive avant de copier les classes dans votre projet original.
