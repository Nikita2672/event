# 1. Настраиваем базу данных

Накатываем миграцию в зависимости от того какую БД используем (все команды запускать из корня проекта).

## Развертывание базы данных в Docker
Если у вас не установлена база данных, то при наличии docker-а это сделать проще простого:

### PostgreSQL

```bash
docker pull postgres
docker run --name postgres -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 postgres
```

### MariaDB

```bash
docker pull mariadb
docker run --name my-mariadb-container -e MYSQL_ROOT_PASSWORD=my-secret-pw -d -p 3306:3306 mariadb
```

## Накатываем миграцию
### PostgreSQL

Параметры в скрипте по умолчанию для PostgreSQL (при желании можно заменить
в [do-update.sh](./../do-update.sh) и в [do-clear-checksums.sh](./../do-clear-checksums.sh)):
```
-Dliquibase.url=jdbc:postgresql://localhost:5432/postgres

-Dliquibase.username=postgres 

-Dliquibase.password=postgres
```
Команда для обновления PostgreSQL:
```bash
./do-update.sh postgres
```

Команда для отчистки контрольных сумм в PostgreSQL:
```bash
./do-clear-checksums.sh postgres
```


### MariaDB
Параметры в скрипте по умолчанию для MariaDB (при желании можно заменить
в [do-update.sh](./../do-update.sh) и в [do-clear-checksums.sh](./../do-clear-checksums.sh)):
```
-Dliquibase.url==jdbc:mariadb://localhost:3306/maria

-Dliquibase.username=root 

-Dliquibase.password=my-secret-pw
```

Команда для обновления MariaDB:
```bash
./do-update.sh mariadb
```

Команда для отчистки контрольных сумм в MariaDB:
```bash
./do-clear-checksums.sh mariadb
```

Далее в зависимости от используемой базы данных вам необходимо раскомментировать те или иные строки в файле 
[application.properties](../src/main/resources/application.properties), возможно поменять на свои значения