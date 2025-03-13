# CRLoader

CRLoader is a Java agent that replaces some code at runtime enabling the user to change location of
[Cosmic Reach](https://finalforeach.itch.io/cosmic-reach)'s files

### Quick start

You can either download a prebuilt jar from [Releases](https://github.com/CRLauncher/CRLoader/releases) page, or, build it yourself. To build CRLoader you need at least JDK 17 and
[Apache Maven](https://maven.apache.org/):
```shell
git clone https://github.com/CRLauncher/CRLoader
cd CRLauncher
mvn clean package
```

### Usage

You use CRLoader like the following:
```shell
java -Dcrloader.saveDirPath=<Desired location of Cosmic Reach files> -javaagent:<path to your CRLoader.jar> -jar <Cosmic Reach jar>
```

Example:
```shell
java -Dcrloader.saveDirPath=C:\Users\User\Documents\CosmicReach -javaagent:CRLoader-0.1.6.jar "Cosmic-Reach-0.4.1.jar"
```

You can also set initial window title with `crloader.windowTitle` system property.