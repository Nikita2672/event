### 2. Сборка и запуск проекта

Для сборки проекта необходимо предварительно [настроить базу данных](./databases.md).

Перед сборкой и запуском приложения вам необходимо настроить переменные окружения, чтобы можно было пользоваться 
[DADATA API](https://dadata.ru/api/). Делается это следующим образом на Linux/MacOs:
```bash
export DADATA_API_TOKEN=<your token for dadata>
export DADATA_API_SECRET=<your secret key for dadata>
```

Если вы на Windows то вам будет проще отредактировать [application.properties](../src/main/resources/application.properties)
и выставить свои значения

После чего выполните следующую команду в корне проекта:

```bash
mvn clean install
```

Для запуска приложения выполните следующую команду из корня проекта:
```bash
java -jar target/event-0.0.1-SNAPSHOT.jar
```