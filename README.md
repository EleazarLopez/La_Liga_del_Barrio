# La_Liga_del_Barrio

Web dedicada a la gestión y consulta de una liga de fútbol

## Instrucciones de despliegue en DOCKER
1 - En ubuntu, crear una carpeta. Por ejemplo, "mkdir LaLigaDelBarrio".

2 - Descargar este repositorio con "git clone https://github.com/EleazarLopez/La_Liga_del_Barrio"

3 - Crear la imagen de la aplicación: 

   > docker build -f laligadelbarrioapp.Dockerfile --no-cache -t eleazarls64/laligadelbarrioapp .


5 Crear y arrancar la infraestructura: 

   > docker compose up

La configuracio de haproxy lleva cookie de sesion con roundrobin (se ve el balanceo al cambiar de navegador).
La base de datos tiene persistencia de datos en host a traves de un volumen.

## Instrucciones de despliegue
Descargar los ficheros en zip, descomprimir e importar a Eclipse como proyecto. El archivo XML contiene todas las dependencias necesarias. Versión java 1.8.
El archivo application.properties está configurado con múltiples debuggers y con la base de datos "create-drop" para poder ejecutar el inicializador de base de datos que genera ejemplos y poder testear la aplicación. Además, contiene la configuración que debe tener la base de datos a la que se va a conectar (para pruebas).
spring.datasource.username=admin
spring.datasource.password=admin
spring.datasource.url=jdbc:mysql://localhost/laligadelbarrio
spring.jpa.hibernate.ddl-auto=create-drop
server.port=8443
server.ssl.key-store=classpath:keystore.jks
server.ssl.key-store-password=password
server.ssl.key-password=password

También se incluye el fichero keystore con el certificado, pero como no está firmado por una CA, la web aparecerá como no segura.

Una vez iniciada la aplicación, para usarla basta con poner en el navegador https://localhost:8443/. La seguridad está implementada con CSRF.

## Funcionalidades
* __Pública__: En la parte pública se podrá consultar la información de los torneos en curso, los partidos de los torneos, los equipos inscritos en los torneo y los jugadores inscritos en los equipos.

* __Privada__: En la parte privada se podrá crear un usuario, crear un torneo, crear un equipo, crear jugadores, crear un partido e introducir resultados de un partido.
 

## ENTIDADES
- Equipo: nombre, correo, teléfono, delegado y jugadores
- Jugador: nombre, dorsal, dni, equipo
- Torneo: nombre, ganador, equipos
- Partido: torneo, equipo casa, equipo visitante, goles casa, goles visitante, tarjetas, fecha y hora
- User: nombre, password, email, roles, (equipo)

## Integrantes del equipo de desarrollo:
 1. ELEAZAR FRANCISCO LOPEZ SOSA 

## Tablero Trello
https://trello.com/b/p4aGXsJA/laligadelbarrio

## Diagrama entidad relación: ![diagramaER](https://user-images.githubusercontent.com/27709224/160923628-785101aa-82d3-4c68-aedf-a84aa6d301ac.png)

## Home
Página inicial de la aplicación. Todas las páginas llevan integrado un layout (header) que genera más opciones dependiendo del usuario.
![home](https://user-images.githubusercontent.com/27709224/160924034-f22589d2-4642-4a25-b58d-ab4623614bae.png)

## Login
Se ha implementado una clase usuario que permite registrar nuevos usuarios en la web. Existen tres roles de usuario: ADMIN (que solo hay uno y no se permite crear más), DELEGADO (que será quien podra crear y gestionar equipos y jugadores), USER (solo con con acceso a recursos públicos)
![login](https://user-images.githubusercontent.com/27709224/160924101-0f9d5b61-4c55-4ea5-9c56-8b61b93d359f.png)

## Registro
Por defecto, obtendrán el rol de USER, pero el admin puede cambiar su rol a DELEGADO.
No se permiten cuentas de usuario con el mismo nombre.
![registro](https://user-images.githubusercontent.com/27709224/160924515-ed4a291f-cb20-4800-aefc-1c32044d58e4.png)

## Edición usuario
Lleva integrado una comprobación de contraseña actual y verificación de nueva contraseña.
![user_edit](https://user-images.githubusercontent.com/27709224/160924600-9932078e-2513-4554-9020-d9d54ace53e7.png)

## Cuentas
Solo el admin tiene acceso a esta funcionalidad, que permite asignar el rol de Delegado a un usuario o bien borrar un usuario que no tenga equipo o un usuario que tenga un equipo que no participe actualmente en ningún torneo. Tampoco se puede eliminar al propio admin. La opción de borrado, borra también el equipo asociado al Delegado.
![cuentas](https://user-images.githubusercontent.com/27709224/160924803-b6e63e53-a2a1-4e74-9975-338e5db8b8ca.png)

## Torneos
Vista pública
![torneos](https://user-images.githubusercontent.com/27709224/160925143-8f104052-1fe4-45b7-816f-d9a65a4f7e8d.png)

Vista de admin (puede crear nuevos torneos)
![torneos_admin](https://user-images.githubusercontent.com/27709224/160925106-65fbcb49-ce63-4916-adb4-f0953de6c8ba.png)

## Torneos_detalles
Vista pública
![torneos_detalles](https://user-images.githubusercontent.com/27709224/160925259-9a2cb732-c1e5-4a4d-994b-2152efdecf85.png)

Vista de admin (puede editar/borrar el torneo y sus partidos)
Un torneo solo se puede eliminar si no tiene ningún partido
![torneos_admin](https://user-images.githubusercontent.com/27709224/160925285-128b797f-ea08-417e-bc81-6617ba2c3dd2.png)

## Torneo_nuevo
No se permiten torneos con el mismo nombre.
![torneo_nuevo](https://user-images.githubusercontent.com/27709224/160925423-ff1009df-8ca0-42c2-a8f0-aaf771735e6d.png)

## Torneo_edit
![torneo_edit](https://user-images.githubusercontent.com/27709224/160925479-4251e003-2f2b-49f0-ac69-c20a3ea9d241.png)

## Equipo_detail
![equipo_detail](https://user-images.githubusercontent.com/27709224/160925722-9b370fd4-dc8e-42b6-9fa2-73f42c714253.png)

## Equipo_nuevo
Solo un admin o un usuario con rol DELEGADO puede crear un equipo. Los equipos están limitados a uno por DELEGADO. La opción de creación se muestra en la cabecera de la web cuando un usuario con rol DELEGADO y sin equipo accede a la web con usuario y contraseña.
No se permite que un DELEGADO gestione otros equipos que no sea el suyo.
![equipo_nuevo](https://user-images.githubusercontent.com/27709224/160925763-9de4c630-ef74-42d8-b32d-510691557fd5.png)

## Equipo_edit
Solo un admin o un usuario con rol DELEGADO puede editar un equipo. Un admin puede editar cualquier equipo pero un DELEGADO solo puede editar su equipo.
La opción de edición se activa para el DELEGADO cuando accede a la web con usuario y contraseña y siempre que tenga equipo.
Aquí se puede editar la información del equipo, ver la lista de jugadores, editarlos o crear nuevos.
![equipo_edit](https://user-images.githubusercontent.com/27709224/160925981-5b96eda8-390e-402b-8b0d-15219e8bb546.png)

## Equipo_delete
Esta opción solo está disponible en la zona privada CUENTAS a la que solo tiene acceso el admin y ha sido explicada anteriormente.

## Jugador_detail
Vista pública
![jugador_detail](https://user-images.githubusercontent.com/27709224/160926596-54d30a4e-60ec-414d-a690-35528761c618.png)

## Jugador_edit
Solo un admin o el DELEGADO del equipo al que pertenece el jugador pueden editar su información. No se permiten jugadores con idéntico DNI.
![jugador_edit](https://user-images.githubusercontent.com/27709224/160926742-c2b6ea73-391d-4c8b-a9bd-fd113c1daa5f.png)

## Jugador_nuevo
Solo un admin o un DELEGADO puede crear un nuevo jugador. No se permiten jugadores con idéntico DNI. El jugador creado será asignado al equipo para el que se va a crear. No se permite que un DELEGADO gestione jugadores de otro equipo que no sea el suyo.
![Jugador_nuevo](https://user-images.githubusercontent.com/27709224/160932575-58762dcc-1eb0-47a2-8963-80becd4e0e90.png)

## Jugador_delete
Implícita en la vista de edición de equipo.

## Partido_detail
Vista pública.
![Partido_detail](https://user-images.githubusercontent.com/27709224/160927498-2b3b508a-37ac-4e51-ad7f-d897610abe70.png)

## Partido_nuevo
Vista de admin. Solo un admin puede crear partidos.
![Partido_nuevo](https://user-images.githubusercontent.com/27709224/160927568-38a21606-f538-4166-93a2-fd9dda4fc0a8.png)

## Partido_edit
Vista de admin. Solo un admin puede editar partidos.
![Partido_edit](https://user-images.githubusercontent.com/27709224/160927623-4e535bf2-b91c-473b-b763-9dce85d49194.png)

