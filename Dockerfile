# Usa un'immagine Maven specifica
FROM maven:3.8.3-openjdk-8 AS builder

# Imposta la directory di lavoro all'interno del container
WORKDIR /app

# Copia il file POM e il resto del codice sorgente
COPY ./ /app

# Esegui il comando Maven per compilare l'app
RUN mvn clean package

# Usa un'immagine JRE (Java Runtime Environment) minimale
FROM openjdk:8-jre-alpine

# Copia il jar compilato dall'immagine Maven nell'immagine JRE
COPY --from=builder /app/target/DaoSimple-1.0-SNAPSHOT.jar /app/DaoSimple.jar

# Esponi la porta (se necessario)
EXPOSE 8080

# Comando per eseguire l'applicazione quando il container è avviato
CMD ["java", "-jar", "/app/DaoSimple.jar"]