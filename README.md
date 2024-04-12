<p align="center">
  <a href="https://kotlinlang.org/"><img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-Latest%20Version-purple.svg"></a>
  <a href="https://developer.android.com/about/versions/6.0"><img alt="SDK 23" src="https://img.shields.io/badge/SDK-23-orange.svg"></a>
  <a href="https://github.com/robotemi/sdk"><img alt="robotemi version" src="https://img.shields.io/badge/robotemi-1.132.1-green.svg"></a>
	<a href="https://jitpack.io/#robotecsolutionsteam/caller"><img alt="jitpack" src="https://jitpack.io/v/robotecsolutionsteam/caller.svg"></a>

</p>

# **Temi Caller**

SDK Temi e funções genericas em Kotlin.

## **Requerimentos**

* [Android Studio](https://developer.android.com/studio?gclid=CjwKCAjwtuOlBhBREiwA7agf1q-5Y_UCpO0OgNYiiTbKC7T8WQ87M9ijPi1RKZNYm2wnHBbD4WiPTxoCHm8QAvD_BwE&gclsrc=aw.ds)
* [SDK Temi](https://github.com/robotemi/sdk)
* [ADB](https://developer.android.com/studio/command-line/adb?hl=pt-br)

## **Instalação e Configuração**

1. Clone o repositório do projeto:
```bash
git clone -b main https://github.com/robotecsolutionsteam/caller.git
```

2. Abra o projeto no Android Studio.
3. Conecte o robô ao computador.
```bash
curl -k "https://<ip>:5556/grantAuth?pwd=<senha>"
adb connect <ip>
```
4. Execute o projeto no Android Studio.

  > **Note** :
  > No canto superior direito da tela em verde, selecione o robô conectado..

5. Execute o seguinte comando para instalar as dependências da documentação e criar um `hook` para o `git`:
```bash
make install
```
Adicionar o maven no gradle.
```gradle
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' } // adicionar aqui !!
		}
	}
```

## **Baixe o repositório:**
Instalar o SDK:

```gradle
dependencies {
	...
	implementation 'com.github.robotecsolutionsteam:caller:latest'
}
```

## **Testes Automatizados**

Para executar os testes automatizados, basta executar o comando abaixo no terminal do Android Studio:
```bash
./gradlew connectedAndroidTest
```

## **Testes Automatizados no robô**

Para executar os testes automatizados no robô, basta executar o comando abaixo no terminal do Android Studio:
```bash
./gradlew connectedTemiDebugAndroidTest
```

