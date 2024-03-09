# 1. DataBase migration

Накатываем миграцию в зависимости от того какую БД используем (все команды запускать из корня проекта).

## PostgreSQL

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


## MariaDB
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