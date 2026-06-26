# Lexora — Back-end

API REST du projet **Lexora**, une plateforme camerounaise de mise en relation entre **clients** et **avocats**, avec publication de cas juridiques et assistant juridique **IA**.

Ce dépôt contient uniquement le back-end. L'application mobile (React Native / Expo) se trouve dans un dépôt séparé.

## Stack technique

| Élément | Détail |
|---|---|
| Langage | Java 21 |
| Framework | Spring Boot |
| Persistance | Spring Data JPA / Hibernate |
| Base de données | PostgreSQL |
| Sécurité | Spring Security (BCrypt) |
| IA | API Mistral (`mistral-small`) |
| Build | Maven (wrapper `mvnw` fourni) |
| Déploiement | Docker (Render) |

## Architecture

Organisation **par fonctionnalité** (package-by-feature) :

```
com.example.lexora
├── LexoraApplication.java   # point d'entrée + activation de l'auditing JPA
├── config/                  # configuration Spring Security & CORS
├── user/                    # entité User (abstraite) + login
├── client/                  # entité Client (hérite de User)
├── avocat/                  # entité Avocat (hérite de User) + recherche/filtres
├── publication/             # publication de cas juridiques
└── ia/                      # assistant juridique (Mistral)
```

Les utilisateurs reposent sur un **héritage JPA en table unique** (`SINGLE_TABLE`) :
`User` (abstrait) → `Client` / `Avocat`, discriminés par la colonne `type_utilisateur`.

Les dates `createdAt` sont gérées automatiquement par l'**auditing JPA** (`@EnableJpaAuditing` + `@CreatedDate`).

## Prérequis

- JDK 21+
- Une base PostgreSQL accessible
- Une clé d'API Mistral

## Configuration (variables d'environnement)

Aucun secret n'est stocké dans le code. Définissez ces variables d'environnement avant de lancer l'application :

| Variable | Description |
|---|---|
| `DATABASE_URL` | URL JDBC PostgreSQL (ex. `jdbc:postgresql://localhost:5432/lexora`) |
| `DATABASE_USER` | Utilisateur de la base |
| `DATABASE_PASSWORD` | Mot de passe de la base |
| `MISTRAL_API_KEY` | Clé d'API Mistral pour l'assistant IA |

Exemple (Linux/macOS) :

```bash
export DATABASE_URL="jdbc:postgresql://localhost:5432/lexora"
export DATABASE_USER="postgres"
export DATABASE_PASSWORD="postgres"
export MISTRAL_API_KEY="votre_cle"
```

## Lancer en local

```bash
# build
./mvnw clean package

# démarrage
./mvnw spring-boot:run
```

L'API écoute par défaut sur le port `8080`, sous le préfixe `/lexora`.

## Lancer avec Docker

```bash
docker build -t lexora-backend .
docker run -p 8080:8080 \
  -e DATABASE_URL="..." -e DATABASE_USER="..." \
  -e DATABASE_PASSWORD="..." -e MISTRAL_API_KEY="..." \
  lexora-backend
```

## Aperçu des endpoints

Préfixe commun : `/lexora`

### Utilisateurs
| Méthode | Route | Description |
|---|---|---|
| `POST` | `/user/login` | Connexion (email + password) |
| `GET` | `/user/publication` | Publications d'un utilisateur (`id`, `email`) |

### Clients
| Méthode | Route | Description |
|---|---|---|
| `POST` | `/user/client/createClient` | Inscription d'un client |
| `PUT` | `/user/client/modifierClient` | Modification du profil |

### Avocats
| Méthode | Route | Description |
|---|---|---|
| `GET` | `/user/avocat` | Liste des avocats |
| `GET` | `/user/avocat/recherche?q=` | Recherche multi-champs |
| `GET` | `/user/avocat/filtreAvocat` | Filtres (région, spécialité, année) paginés |
| `POST` | `/user/avocat/createAvocat` | Création d'un avocat |
| `POST` | `/user/avocat/inscription` | Dépôt des documents de vérification |

### Publications
| Méthode | Route | Description |
|---|---|---|
| `POST` | `/publication/create` | Créer une publication |
| `GET` | `/publication/read` | Lister toutes les publications |
| `GET` | `/publication/readId?id=` | Publications d'un utilisateur |

### IA
| Méthode | Route | Description |
|---|---|---|
| `POST` | `/ia/question?question=` | Pose une question à l'assistant juridique |

## Sécurité — état actuel

- Mots de passe hashés avec **BCrypt** ; le hash n'est jamais renvoyé dans les réponses JSON.
- Secrets externalisés en variables d'environnement.
- CORS et liste blanche des routes configurés dans `config/ConfigSecurity`.
- ⚠️ **À venir** : authentification par jeton (JWT) pour protéger réellement les routes par utilisateur, et module **Cabinet + PostGIS** pour la géolocalisation des avocats.

## Tests

```bash
./mvnw test
```
