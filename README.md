# 🌳 CKOALA

Application Java de recherche dans des arbres de typologies à partir de caractéristiques observées, avec intégration continue, tests automatisés et rapport de couverture ().

## Auteur

**Amine Nakrou**

---

## Présentation du projet

Ce dépôt contient le cœur de l’application **CKOALA**, un outil qui permet de rechercher dans des arbres de typologies en fonction de caractéristiques définies dans un fichier XML.

Le principe du projet est simple :

- une typologie est décrite dans un fichier XML ;
- cette typologie est chargée dans l’application ;
- l’utilisateur fournit une observation ;
- l’application retourne les catégories compatibles avec cette observation.

Ce projet met en œuvre :
- la modélisation orientée objet en Java ;
- la manipulation de fichiers XML ;
- la classification d’observations selon des caractéristiques ;
- la validation du code via des tests et une chaîne CI/CD.

---

## Fonctionnement général

Une typologie est chargée depuis un fichier XML dans la classe `Typologie`.

Cette typologie est composée de plusieurs éléments :
- `Categorie` pour représenter les catégories de l’arbre ;
- `Caracteristique` pour représenter les attributs ;
- `Domaine` pour représenter les domaines de valeurs ;
- `Observation` pour représenter les données fournies par l’utilisateur.

L’application compare ensuite une observation avec les catégories disponibles afin de déterminer les catégories compatibles.

---

## Fichiers XML utilisables

Le projet peut utiliser les fichiers suivants :

- `arbres.xml`
- `nuages.xml`

Il est aussi possible d’utiliser un autre fichier XML personnel, à condition qu’il respecte la même structure que ceux attendus par l’application.

---

## Technologies utilisées

- **Java 17**
- **JUnit** pour les tests
- **JaCoCo** pour la couverture du code
- **Checkstyle** pour l’analyse statique du style
- **PMD CPD** pour la détection de duplications
- **GitLab CI/CD**
- **GitHub Actions**
- **GitHub Pages** pour publier les rapports

---

## Structure attendue du projet

```bash
.
├── .github/
│   └── workflows/
│       ├── ci.yml
│       └── pages.yml
├── .gitlab-ci.yml
├── src/
├── tests/
├── lib/
├── out/
├── report/
├── arbres.xml
├── nuages.xml
├── checkstyle.xml
├── README.md
└── CONTRIBUTING.md
