# Tyba Backend

---

### Microservice Catalog

* [SwaggerUI (develop)](http://localhost:8080/api/tyba-backend/swagger-ui.html#/user-controller)

### Endpoints: 
Se construyeron los siguientes endpoints:

* Registro usuario: Registra usuario con contraseña encryptada

* Login Usuario: Valida que el usuario se encuentre registrado en BD, y genera token jwt de autenticación.

* Consulta de restaurantes por ciudad o coordenadas: Recibe el token de autenticación y valida que el mismo se encuentre valido, consulta api de lugares y registra las transacciones en la BD.

* Consulta de transacciones: Recibe el token lo valida, y consulta las transacciones de ese usuario en la BD.

* Logout: Recibe el token, lo valida, lo almacena en un redis de dados de baja, y valida en cada operación que el token no haya sido dado de baja en un login.


