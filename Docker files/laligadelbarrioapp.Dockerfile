#Imagen del compilador de maven
FROM maven as builder

#Definimos el directorio de trabajo con el que inicia el contenedor
WORKDIR /codigo

#Traemos los archivos desde github que serán guardadose en el directorio de trabajo
COPY ./La_Liga_del_Barrio /codigo/La_Liga_del_Barrio

#Compilamos la aplicacion evitando el testing para que no de error al generar el jar (ademas hay que  forzar situarse en la carpeta correcta)
RUN mvn -f "$PWD/La_Liga_del_Barrio" package "-Dmaven.test.skip=true"

#Imagen del jdk para la aplicación
FROM openjdk

#Definimos el directorio de trabajo con el que inicia el contenedor
WORKDIR /laligadelbarrio

#Copiamos el jar generado con el contenedor de maven
COPY --from=builder /codigo/La_Liga_del_Barrio/target/laligadelbarrio-0.0.1-SNAPSHOT.jar .

#Puertos
EXPOSE 8080

#Comando por defecto para iniciar la aplicación (ya estamos situados en el directorio donde se encuentra el jar)
ENTRYPOINT ["java","-jar","laligadelbarrio-0.0.1-SNAPSHOT.jar"]