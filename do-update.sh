#!/bin/bash

# Проверяем, передан ли параметр
if [ -z "$1" ]; then
  echo "Usage: $0 <database_type>"
  exit 1
fi

# Получаем значение параметра
database_type=$1

# Определяем команду в зависимости от типа базы данных
case $database_type in
  "postgres")
    command="mvn liquibase:update -Dliquibase.url=jdbc:postgresql://localhost:5432/postgres -Dliquibase.username=postgres -Dliquibase.password=postgres -Dliquibase.changeLogFile=db/changelog/master-changelog.xml"
    ;;
  "mariadb")
    command="mvn liquibase:update -Dliquibase.url=jdbc:mariadb://localhost:3306/maria -Dliquibase.username=root -Dliquibase.password=my-secret-pw -Dliquibase.changeLogFile=db/changelog/master-changelog.xml"
    ;;
  *)
    echo "Unsupported database type: $database_type"
    exit 1
    ;;
esac

# Выполняем команду
eval $command
