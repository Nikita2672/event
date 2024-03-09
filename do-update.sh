mvn liquibase:update -Dliquibase.url=jdbc:postgresql://localhost:5432/postgres -Dliquibase.username=postgres -Dliquibase.password=postgres -Dliquibase.changeLogFile=db/changelog/master-changelog.xml
