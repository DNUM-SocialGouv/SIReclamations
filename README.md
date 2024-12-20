# API
## Setup
### Codestyle

Le projet suit le [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). Il
est forcé grâce au [google-java-format](https://github.com/google/google-java-format). Ce formateur
est appliqué sur les fichiers Java grâce au plugin
Maven [Spotless](https://github.com/diffplug/spotless/tree/main/plugin-maven).

Pour vérifier si le code est correctement formaté :

```
mvn spotless:check
```

Pour corriger automatiquement son code, il faut exécuter la commande suivante :

```
mvn spotless:apply
```
### Configurer le codestyle dans intelliJ
Afin d'éviter de devoir exécuter les commandes Maven à la main, on peut configurer IntelliJ pour qu'il mette en forme
le code de manière valide automatiquement.

1. Téléchargez le fichier XML de style Google pour IntelliJ depuis [ce dépôt](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml).
2. Dans IntelliJ, accédez à IntelliJ IDEA > Settings > Editor > Code Style > Scheme > Import Scheme > IntelliJ IDEA Code Style XML. 
3. Importez le fichier téléchargé.

### Mise en place d'un Git Hook
Pour automatiser l'application du codeStyle nous utilisons Lefthook.  
Lefthook est un outil permettant d'automatiser et gérer les hooks Git (scripts exécutés automatiquement à chaque étape clé, comme avant un commit ou un push).  

1. Installer Lefthook sur son poste : `brew install lefthook`
2. Aller à la racine du projet et installer lefthook : `lefthook install`
 
