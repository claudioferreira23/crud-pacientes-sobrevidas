# Imagem de execução da API (usada pelo pipeline de CI/CD).
# O jar é gerado ANTES do build da imagem (./mvnw package), então esta imagem
# contém apenas o JRE + o jar, sem código-fonte nem ferramentas de build.
#
# Uso local:
#   ./mvnw -DskipTests package
#   docker build -t crud-pacientes .
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Executa como usuário sem privilégios de root
RUN addgroup -S app && adduser -S app -G app

COPY target/crud-pacientes-*.jar app.jar

USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
