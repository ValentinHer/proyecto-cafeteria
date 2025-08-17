# 📌 Proyecto Cafetería

Proyecto que presenta una solución integral para los servicios que ofrece una cafetería, con el propósito de optimizar las operaciones diarias, mejorar la experiencia del usuario y facilitar la administración del negocio.

## 🚀 Características

- ✅ Registro y Login con Email / Password.
- ✅ Registro y Login con Google.
- ✅ Gestión de categorías para los productos.
- ✅ Gestión de productos.
- ✅ Creación de reservaciones tanto para usuarios registrados como para invitados.
- ✅ Envio de emails personalizados.
- ✅ Gestión de carritos de compra.
- ✅ Registro de pagos con PayPal.
- ✅ Registro de ordenes del usuario.

## 🛠️ Tecnologías utilizadas

Este proyecto fue desarrollado con: 

- Java con Spring Boot.
- IntelliJ IDEA, Git, Docker, PostgreSQL.
- OAuth2.0, JWT, Swagger, AWS S3, Webhook de Calendly, API y Webhook de PayPal.

## 📦 Instalación y Configuración

1. **Clona o descarga el archivo `.zip` de este repositorio**.

1. **Crear un bucket en AWS para realizar la carga de imágenes**.

    - Dentro de AWS, en la sección S3 crea un bucket con las configuraciones por defecto (solo añade el nombre que tendrá el bucket), una vez creado copia la región donde se encuentre el bucket junto con el nombre del bucket y añadeselo al archivo `application.properties`, ejemplo: **aws.region=us-east-1**.

    - En la sección IAM crea una nueva política.
        - Añade las siguientes acciones para el servicio S3 (al crear la política, esta sección para asignar permisos se muestra como **Acciones permitido**):
            * GetObject
            * DeleteObject
            * PutObject
            * ListBucket

        - Al momento de crear la política, en la sección **Recursos**, especifica los recursos para los que las acciones anteriores se permitiran.
            - En bucket, seleciona **Agregar ARN** y agrega el nombre del bucket creado en S3.
            - En object, selecciona **Agregar ARN**, agrega el nombre del bucket creado en S3, pero en este caso para el campo **Resource object name** selecciona la casilla **Cualquier object name** (esto con la finalidad de acceder a cualquier archivo almacenado dentro del bucket).

        - Como paso final añade un nombre a la política y creala.

    - Siguiendo en la sección IAM crea un nuevo usuario (en la sección **Personas**) y añadele la política antes creada.
        - Añade un nombre al usuario.
        - En la sección **Establecer permisos**, escoge la opción **Adjuntar políticas directamente** y añade la política antes creada.
        - Crea el usuario.

    - Crear claves de acceso para el usuario.
        - Una vez completado el paso anterior accedemos al usuario creado.
        - En la sección **Credenciales de seguridad** buscamos **Claves de acceso**, una vez ubicado crea una clave de acceso.
            - En el Paso 1, en Caso de uso selecciona: **Código local**.
            - En el Paso 2, agrega una descripcion para la clave de acceso.
            - Copia la **Clave de acceso** y la **Clave de acceso secreta** y añadeselo al archivo `application.properties`.

1. **Crear cuenta de Calendly**.
    - Crea una cuenta de Calendly y accede al apartado de integraciones, ve a Api y Webhooks y crea un token de acceso, ese token copialo al archivo `application.properties`.
     
1. **Crear cuenta de PayPal**.
   - Crea una cuenta en PayPal y accede en modo Developer para que puedas acceder a las credenciales de autenticación, una vez ya tienes las credenciales copialos al archivo `application.properties`.
   - Para obtener un token de acceso para poder usarlos en el Webhook solamente necesitas de las credenciales antes obtenidas.

1. **Crear base de datos PostgreSQL**.
    - Si cuentas con Docker, puedes utilizar el archivo `docker-compose.yml` que se encuentra en el proyecto y dejar las URLs de las bases de datos en el archivo `application.properties` tal como estan, de otro modo dirígete al siguiente punto.
        - Desde la terminal dirigete a la ruta donde se encuentre el archivo **docker-compose**, y ejecuta los siguientes comandos:

          Iniciar Servicios
            ```
            docker compose up -d
            ``` 

          Finalizar Servicios
            ```
            docker compose down
            ``` 
    - Crea una base de datos PostgreSQL local, y añade las credenciales de acceso al archivo `application.properties`.

1. **Probar Webhooks**
    - Para probar los webhooks es necesario una URL pública, puedes utilizar un herramienta que rediriga las peticiones (como ngrok) de una URL pública a tu local (Esto solo en desarrollo), si lo tienes en producción solo cambia la URL del webhook en `application.properties` por el que tienes en producción.

## Inicialización

- **Inicializa el proyecto**.
    - Puedes entrar al archivo principal del proyecto y ejecutar el método main del proyecto.

    - O ejecutar el siguiente comando desde la terminal:

        ```
        mvn spring-boot:run
        ```

## Documentación de API

- Entra al siguiente link una vez inicializado el proyecto para visualizar todas las operaciones disponibles en la API:
  `http://localhost:8080/api/swagger-ui/index.html`
