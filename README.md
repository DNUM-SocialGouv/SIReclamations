# API
## Setup
### Codestyle

Le projet suit le [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). Il
appliqué grâce à [google-java-format](https://github.com/google/google-java-format). Ce formateur
est appliqué sur les fichiers Java grâce au plugin
Maven [Spotless](https://github.com/diffplug/spotless/tree/main/plugin-maven).

Pour vérifier si le code respecte le formatage requis :

```
mvn spotless:check
```

Pour appliquer automatiquement les corrections de formatage :

```
mvn spotless:apply
```

### Configuration du codestyle dans intelliJ

Pour éviter d'exécuter les commandes Maven manuellement, IntelliJ peut être configuré pour appliquer
automatiquement le formatage correct.

1. Téléchargez le fichier XML de style Google pour IntelliJ depuis [ce dépôt](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml).
2. Dans IntelliJ, accédez à `IntelliJ IDEA > Settings > Editor > Code Style > Scheme > Import Scheme > IntelliJ IDEA Code Style XML.` 
3. Importez le fichier téléchargé.

### Mise en place d'un Git Hook
#### Fonctionnement
Pour automatiser l'application du codeStyle nous utilisons Lefthook.  
Lefthook est un outil permettant d'automatiser et gérer les hooks Git (scripts exécutés 
automatiquement à chaque étape clé, comme avant un commit ou un push).  
Son fichier de configuration `lefthook.yml` est présent à la racine du projet.

Lors de chaque commit, Lefthook exécutera `spotless:apply` pour vérifier et appliquer le bon
bon formatage du code. Si des corrections sont effectuées par Spotless, il sera nécessaire de
vérifier
les modifications, puis d'ajouter les fichiers concernés à la zone de staging (via `git add`) avant 
de pouvoir finaliser le commit.

#### Installation

1. Installer Lefthook sur son poste :

```
brew install lefthook
```

2. À la racine du projet, installer lefthook :

```
lefthook install
```
 
## Lancement de l'application

Pour lancer l'application en local, exécutez la commande suivante :
```
mvn spring-boot:run
```

### Remote debug
Il peut être nécessaire de déboguer l'API en local. Pour cela :

1. Créez une configuration de debug dans IntelliJ `Edit Configuration -> Remote JVM Debug` Laissez
   les paramètres par défaut.
2. Lancez l'application en mode debug :

```
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
```

3. Executez la configuration Remote JVM Debug créée à l'étape 1.

## Architecture projet

Le projet applique les concepts de [l'architecture hexagonale](https://blog.octo.com/architecture-hexagonale-trois-principes-et-un-exemple-dimplementation)


## Standards d'équipe
### Flow de développement

1. Organiser un "3 amigos" avec un PO pour clarifier le besoin du ticket.
2. Prendre le ticket de la colonne `à faire` et le déplacer vers `en cours`
3. Créer une branche de développement (voir rubrique [Git](#git))
4. Une fois les développements terminés, ouvrir une [Pull Request](#pull-request) et demander une
   revue de code via le canal d'équipe.  
   _(Si les développements ont été réalisés en pair/mob il est possible de ne pas demander de
   revue)._
5. Traiter les retours de revue dans un commit dédié.
6. Fusionner la pull request si elle est approuvée par au moins une personne.

### Git

Toute nouvelle branche de développement doit respecter les critères suivants :
1. Être créée à partir de la dernière version de develop.
2. Suivre la convention de nommage : `simal-N°ticket-nom-de-la-branche.`
   Le préfixe `simal-numeroTicket` est essentiel pour permettre la liaison entre les tickets
   Jira et les branches de développement.

### Pull Request

1. Le titre de la pull request doit respecter la convention suivante : `Simal-N° : pitch du ticket`
   exemple : `Simal-17 : Création de documentation via le fichier README`
   En respectant cette convention, le ticket JIRA sera directement lié a la pull request.
2. Dans la description de la pull request, ajoutez une courte explication ou insérez le lien
   vers le ticket Jira correspondant.