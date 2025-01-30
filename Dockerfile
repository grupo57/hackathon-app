# Etapa 1: Construção (Build Stage)
FROM maven:3.8-openjdk-17-slim AS build

# Diretório de trabalho no container
WORKDIR /app

# Copiar os arquivos do projeto (pom.xml, src)
# Copiar apenas o pom.xml para otimizar o cache
COPY pom.xml .
RUN mvn install -DskipTests
# Agora, copie o restante do código-fonte
COPY src ./src
# Rodar o Maven para compilar a aplicação e gerar o JAR
RUN mvn clean package -DskipTests

# Etapa 2: Execução (Runtime Stage)
FROM openjdk:17-jdk-slim

# Definir variáveis de ambiente no Dockerfile
ENV DATABASE_NAME=hackathon
ENV DATABASE_URL=jdbc:mysql://host.docker.internal:3306/hackathon?allowPublicKeyRetrieval=true
ENV DATABASE_USER=root
ENV DATABASE_PASSWORD=sobek
ENV DATABASE_DRIVER_CLASS_NAME=com.mysql.cj.jdbc.Driver
ENV BUCKET_UPLOAD_NAME=fiap-grupo57-hackathon
ENV BUCKET_DOWNLOAD_NAME=fiap-grupo57-hackathon-zip
ENV QUEUE_VIDEO_PROCESSADO_URL=hackathon-video-processado
ENV QUEUE_VIDEO_ERRO_URL=hackathon-video-erro

# Diretório de trabalho no container
WORKDIR /app

# Copiar o JAR gerado da etapa anterior
COPY --from=build /app/target/hackathon-video.jar /app/hackathon-video.jar

# Expor a porta que a aplicação Spring Boot estará escutando
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/hackathon-video.jar"]





