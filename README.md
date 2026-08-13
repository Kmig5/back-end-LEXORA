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
| Sécurité | Spring Security + BCrypt |
| IA | API Mistral (`mistral-small` / `mistral-large-latest`) |
| Build | Maven (`mvnw` fourni) |
| Déploiement | Docker |

## Architecture

Organisation par fonctionnalité :

```
com.example.lexora
├── LexoraApplication.java   # point d'entrée et activation de l'auditing JPA
├── config/                  # configuration Spring Security et CORS
├── user/                    # gestion des utilisateurs et des avocats
├── publication/             # publication de cas juridiques
├── rendezVous/              # gestion des rendez-vous
└── ia/                      # assistant juridique Mistral
```

L'entité `User` centralise les données utilisateur. Le champ `typeUtilisateur` permet de distinguer les clients et les avocats.

## Prérequis

- JDK 21+
- PostgreSQL accessible
- Clé d'API Mistral

## Configuration (variables d'environnement)

Aucun secret n'est stocké dans le code. Définissez ces variables d'environnement avant de lancer l'application :

| Variable | Description |
|---|---|
| `DATABASE_URL` | URL JDBC PostgreSQL, ex. `jdbc:postgresql://localhost:5432/lexora` |
| `DATABASE_USER` | Utilisateur de la base |
| `DATABASE_PASSWORD` | Mot de passe de la base |
| `MISTRAL_API_KEY` | Clé d'API Mistral |

Exemple (Linux/macOS) :

```bash
export DATABASE_URL="jdbc:postgresql://localhost:5432/lexora"
export DATABASE_USER="postgres"
export DATABASE_PASSWORD="postgres"
export MISTRAL_API_KEY="votre_cle"
```

## Lancer en local

```bash
./mvnw clean package
./mvnw spring-boot:run
```

L'API écoute par défaut sur le port `8080` et accepte les connexions sur `0.0.0.0`.

## Lancer avec Docker

```bash
docker build -t lexora-backend .
docker run -p 8080:8080 \
  -e DATABASE_URL="..." -e DATABASE_USER="..." \
  -e DATABASE_PASSWORD="..." -e MISTRAL_API_KEY="..." \
  lexora-backend
```

## Endpoints disponibles

### Utilisateurs
| Méthode | Route | Description |
|---|---|---|
| `POST` | `/lexora/user/login` | Connexion (email + password) |
| `POST` | `/lexora/user/createUser` | Inscription d'un client |
| `PUT` | `/lexora/user/modifierUser` | Modifier un profil utilisateur |
| `PUT` | `/lexora/user/changePassword` | Changer le mot de passe (`actualPassword`, `nouveauPassword`, `id`) |
| `GET` | `/lexora/user/users` | Liste de tous les utilisateurs (admin) |
| `GET` | `/lexora/user/KPIUser` | Statistiques utilisateurs |
| `PUT` | `/lexora/user/modifierType` | Passer un utilisateur en `AVOCAT` |
| `GET` | `/lexora/user/clientToAvocat` | Clients demandant une validation avocat |

### Avocats
| Méthode | Route | Description |
|---|---|---|
| `GET` | `/lexora/user/avocat` | Liste des avocats |
| `GET` | `/lexora/user/avocat/recherche?q=` | Recherche multi-critères sur les avocats |
| `POST` | `/lexora/user/avocat/inscriptionAvocat` | Demande de validation avocat (`doc1`, `doc2`, `doc3`, `description`, `specialite`, `idClient`) |

### Publications
| Méthode | Route | Description |
|---|---|---|
| `POST` | `/lexora/publication/create` | Créer une publication |
| `GET` | `/lexora/publication/read` | Lister toutes les publications |
| `GET` | `/lexora/publication/readId?id=` | Récupérer les publications d'un utilisateur |
| `PUT` | `/lexora/publication/modif` | Modifier une publication |

### Rendez-vous
| Méthode | Route | Description |
|---|---|---|
| `POST` | `/lexora/rendez-vous/create-RDV` | Créer un rendez-vous |
| `GET` | `/lexora/rendez-vous/ownerRdv` | RDV d'un avocat (`userId` en header) |
| `GET` | `/lexora/rendez-vous` | Lister tous les rendez-vous (admin) |
| `GET` | `/lexora/rendez-vous/KPIRdv` | Statistiques rendez-vous |

### IA
| Méthode | Route | Description |
|---|---|---|
| `POST` | `/lexora/ia/question?question=&contexte=` | Interroger l'assistant juridique IA |

## Sécurité

- Mots de passe hachés avec **BCrypt**.
- Secrets chargés depuis les variables d'environnement.
- Spring Security est configuré, mais toutes les routes `/lexora/**` sont actuellement autorisées.
- `formLogin`, `httpBasic` et `csrf` sont désactivés.
- CORS autorise `http://localhost:5173` et l'endpoint IA autorise toutes origines via `@CrossOrigin(origins = "*")`.

## Remarques

- La clé Mistral est chargée via `api.key=${MISTRAL_API_KEY}` dans `application.properties`.
- Spring JPA est configuré avec `spring.jpa.hibernate.ddl-auto=update`, ce qui met à jour le schéma automatiquement.
- Le modèle `User` contient à la fois les données client et avocat, avec un champ `typeUtilisateur` pour la distinction.

## Tests

```bash
./mvnw test
```
