# Wiki du Système de Gestion des Employés

## Acceuil
Bienvenue sur le wiki du projet Employee Management System ! Ce référentiel contient toute la documentation relative à notre application de gestion des employés basée sur Spring Boot.

## Table of Contents
- [[Présentation du projet]]
- [[Spécifications techniques]]
- [[Exigences fonctionnelles]]
- [[Exigences non fonctionnelles]]
- [[Acteurs et rôles]()]
- [[Pipeline CI/CD]]
- [[Stratégie de tests]()]
- [[Planification du projet]()]
- [[Guide de démarrage]]
- [[Contribution & bonnes pratiques Git]]

## Présentation du projet

### Introduction
Le Système de Gestion des Employés est une application web destinée à gérer les données des employés via des opérations CRUD complètes. Elle repose sur le framework Spring Boot et suit une architecture en trois couches.

### Objectifs
- Développer un système fiable et sécurisé pour la gestion des employés
- Implémenter des fonctionnalités complètes de création, lecture, mise à jour et suppression (CRUD)
- Offrir une interface utilisateur intuitive grâce à Bootstrap
- Mettre en place un pipeline CI/CD robuste avec Jenkins, Nexus, et GitHub
- Intégrer l'authentification et l'autorisation avec Spring Security

## Spécifications techniques

### Architecture
Architecture en trois couches :
- **Couche présentation**: Contrôleurs + Thymeleaf + Bootstrap
- **Couche métier**: Composants de service
- **Couche persistance**: Repositories JPA

### Technologies utilisées
- **Backend Framework**: Spring Boot 3.1+
- **Sécurité**: Spring Security 6.1+
- **Accès aux données**: Spring Data JPA
- **Base de données**: MySQL 8.0+
- **Frontend**: Thymeleaf + Bootstrap 5.3+
- **Build Tool**: Maven 3.8+
- **Versionnement**: GitHub
- **CI/CD**: Jenkins
- **Gestion des artefacts**: Nexus Repository Manager 3.x

### Environnement de développement
- JDK 17+
- IDE compatible avec Spring Boot
- Client Git
- Maven installé localement

## Exigences fonctionnelles

### Authentification & Autorisation
- Contrôle d’accès basé sur les rôles:
  - Admin: Accès total à toutes les fonctionnalités
  - Manager: Lecture et modification (pas de suppression)
  - User: Accès en lecture seule

### Gestion des employés
- **Create**: Ajouter un nouvel employé
- **Read**: Afficher tous les employés ou un seul
- **Update**: Modifier les informations d’un employé
- **Delete**: Supprimer un enregistrement

### Structure des données employés
Champs requis :
- ID Employé (généré automatiquement)
- Prénom
- Nom
- Email (unique)
- Téléphone
- Date d'embauche
- Poste
- Département
- Salaire

### Pages et UI
- **/login**: Page de connexion
- **/employees**: Liste des employés (avec recherche/filtrage)
- **/employees/add**: Formulaire d’ajout
- **/employees/edit/{id}**: Formulaire d’édition
- **/employees/{id}**: Détails de l’employé

## Exigences non fonctionnelles

### Performance
- Chargement des pages en moins de 2 secondes
- Support d'au moins 50 utilisateurs simultanés
- Requêtes SQL optimisées

### Sécurité
- Chiffrement HTTPS
- Hachage des mots de passe
- Protection contre les injections SQL, XSS, CSRF
- Validation des entrées utilisateur
- Gestion des sessions avec expiration

### Fiabilité
- Procédures de sauvegarde de base de données
- Journalisation des erreurs et surveillance
- Gérer les problèmes avec des messages d’erreur conviviaux

### Scalabilité
- Prise en charge d’un nombre croissant d’employés
- Pagination des résultats pour les gros volumes

## Acteurs et rôles
  - Admin: Gestion complète des employés
  - Manager: Consultation et modification des données
  - User: Consultation uniquement des informations

## Pipeline CI/CD

### Gestion de version
- GitHub pour le versionnement
- Branches:
  - main: code prêt pour production
  - develop: branche d’intégration
  - feature: branches pour le  development

### Intégration continue (Jenkins)
- Jenkins pipeline configuré pour:
  - Pull depuis GitHub
  - Build via Maven
  - Lancement des tests
  - Génération de rapports qualité
  - Packaging

### Gestions des artéfacts
- Nexus Repository Manager pour stocker:
  - Dépendances Maven
  - Fichiers .jar compilés
  - Images Docker (si containerisé)

### Déploiement
- Jenkins pipeline déploiera:
  - Environnement de développement : après chaque build
  - QA/Test : après validation
  - Production : déclenchement manuel

## Stratégie de tests

### Types de tests
- Tests unitaires (services, repositories)
- Tests d’intégration (contrôleurs)
- Tests UI (parcours critiques)
- Tests de sécurité

### Couverture minimale
- 80 % pour la logique métier
- Tous les endpoints REST couverts

## Planification du projet

### Phase 1 – Fondations (2 semaines)
- Setup Spring Boot
- Schéma BDD
- Configuration CI/CD
- Authentification

### Phase 2 – Fonctionnalité (3 semaines)
- Implémentation des entités/services
- CRUD employés
- Développement UI

### Phase 3 – Tests et optimisation (2 semaines)
- Améliorations UI
- Validation
- Tests complets
- Optimisation performance

### Phase 4 – Déploiement (1 semaine)
- Tests finaux
- Mise en production
- Rédaction de documentation

## Guide de démarrage

### Prérequis
- JDK 17+
- Maven 3.8+
- MySQL 8.0+

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Mouadbouanani/Employee-Management-System.git
   ```

2. Configure MySQL database:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/employee_db
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Utilisateurs par défaut
- Admin: admin@company.com / admin123
- Manager: manager@company.com / manager123
- User: user@company.com / user123

## Contribution & Bonnes pratiques Git

### Stratégie Git
- Créer d'une branche feature depuis develop
- Name: `feature/feature-name` ou `bugfix/issue-description`
- Faire une Pull Request vers develop

### Style de code
- Suivre le Google Java Style Guide
- Noms explicites pour les variables/méthodes
- Commenter le code complexe

### Commits
- Utiliser des messages de 'commit' utile
- Liens vers issues si possible
- Format : [TYPE] Description courte (#numéro_issue)

### Pull Requests
1. S'assurer que le code se compile avec succès et que tous les tests passent
2. Mettre à jour la documentation si nécessaire
3. Demander un avis à au moins un membre de l'équipe
4. usionner uniquement après approbation

### Liste de vérification pour la relecture de code
- Le code respecte les conventions de style du projet
- La gestion des erreurs est correctement implémentée
- Les aspects de sécurité sont pris en compte
- Les test sont inclues et réussit
- La documentation est à jour
