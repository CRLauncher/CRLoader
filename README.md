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
java -javaagent:<path to your CRLoader.jar>=<Desired location of Cosmic Reach's files> -jar <Cosmic Reach jar>
```

Example:
```shell
java -javaagent:CRLoader-0.0.1.jar=C:\Users\User\CRLauncher\cosmic-reach -jar "Cosmic Reach-0.1.6.jar"
```