# Microservices Architecture with Spring Boot, Eureka, Kafka, Keycloak, and API Gateway

## Introduction

Ce projet est une démonstration d'une architecture microservices basée sur Spring Boot, qui comprend les services suivants :
- **Config Server** : Pour centraliser la configuration de tous les services microservices.
- **API Gateway** : Pour router les requêtes des clients vers les services appropriés (Produit, Utilisateur).
- **Eureka Server** : Pour la gestion de la découverte des services.
- **Service Produit** : Pour la gestion des produits.
- **Service Utilisateur** : Pour la gestion des utilisateurs.
- **Keycloak** : Pour l'authentification et l'autorisation centralisées.
- **Kafka** : Pour la communication asynchrone entre les services.

## Architecture

L'architecture du projet suit un modèle **microservices** avec les technologies suivantes :

- **Spring Boot** pour la création des services backend.
- **Spring Cloud Config Server** pour gérer la configuration des services de manière centralisée.
- **Spring Cloud Netflix Eureka** pour la découverte de services.
- **Spring Cloud Gateway** pour servir d'API Gateway centralisée.
- **Apache Kafka** pour gérer la communication asynchrone entre les microservices.
- **Keycloak** pour l'authentification et l'autorisation des utilisateurs.
- **MySQL** pour la gestion de la persistance des données.

[//]: # (![Architecture Diagram]&#40;path-to-architecture-diagram.png&#41;)

## Prérequis

Avant de commencer, assurez-vous que les éléments suivants sont installés sur votre machine :

- Java 17 ou version ultérieure
- Maven 
- Docker (facultatif, si vous voulez exécuter Keycloak ou Kafka en containers)
- Kafka en fonctionnement (ou utilisez Docker pour le démarrer)
- Keycloak (ou utilisez Docker pour démarrer un serveur Keycloak)

## Description des Services

### 1. **Config Server**
- Le **Config Server** centralise la configuration de tous les microservices. Il permet aux services de récupérer leurs configurations à partir d'un dépôt Git ou d'une base de données.

### 2. **Eureka Server**
- Le **Eureka Server** permet aux services de se découvrir et de communiquer entre eux. Il gère l'enregistrement et la récupération des services en utilisant un service de découverte.

### 3. **API Gateway**
- L'**API Gateway** sert de point d'entrée unique pour toutes les requêtes. Il orchestre les appels aux services Produit et Utilisateur. Il assure également la gestion des erreurs et peut intégrer des mécanismes de sécurité via des filtres.

### 4. **Service Utilisateur**
- Le **Service Utilisateur** gère les opérations liées aux utilisateurs, comme l'inscription, la mise à jour des profils, etc. Il utilise **Keycloak** pour la gestion de l'authentification et de l'autorisation.

### 5. **Service Produit**
- Le **Service Produit** gère les produits, avec des fonctionnalités telles que l'ajout de nouveaux produits, la mise à jour, et la suppression. Ce service consomme des messages via Kafka pour intégrer des mises à jour des produits en temps réel.

### 6. **Kafka**
- **Kafka** est utilisé pour assurer la communication asynchrone entre les services. Lorsqu'un produit est ajouté dans le service produit, un message est envoyé à un topic Kafka que les autres services peuvent consommer.

### 7. **Keycloak**
- **Keycloak** gère l'authentification et l'autorisation des utilisateurs. Il permet de sécuriser les endpoints des services et de fournir une gestion centralisée des rôles et des permissions.

## Fonctionnalités

- **Découverte de services** avec **Eureka**.
- **Routage des requêtes** via l'**API Gateway**.
- **Authentification et autorisation** des utilisateurs via **Keycloak**.
- **Communication asynchrone** entre les services via **Kafka**.
- **Gestion centralisée des configurations** avec **Config Server**.
- **Sécurisation des APIs** avec un mécanisme de sécurité intégré.

## Démarrage du Projet

### 1. **Démarrer les Services Kafka et Keycloak**

Si vous utilisez Docker, vous pouvez démarrer Kafka et Keycloak avec les commandes suivantes :

#### Kafka avec Docker

```bash
docker-compose -f docker/kafka-compose.yml up
