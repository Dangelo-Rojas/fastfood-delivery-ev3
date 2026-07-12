# FastFood Delivery — Microservicios

Sistema de delivery de comida rápida implementado con arquitectura de microservicios usando Spring Boot 4 y Spring Cloud.

---

## Autor

Dangelo Rojas

---

## Descripción

Proyecto académico (Duoc UC) que implementa un backend de delivery de comida rápida, dividido en microservicios independientes que se comunican via REST. Incluye gestión de usuarios, restaurantes, órdenes, pagos y delivery, con un API Gateway centralizado y service discovery via Eureka. El sistema completo está dockerizado con `docker-compose` para levantarlo con un solo comando.

---

## Arquitectura

```
                    +---------------------+
                    |   API Gateway       |
                    |   :8080             |
                    +----------+----------+
                               |
        +----------------------+----------------------+
        |                      |                      |
        v                      v                      v
+----------------+  +----------------+  +----------------+  +----------------+
|  ms-usuario    |  | ms-restaurante |  |   ms-orden     |  |  ms-delivery   |
|  :8081         |  | :8082          |  |   :8087        |  |  :8089         |
|  db_usuario    |  | db_restaurante |  |   db_orden     |  |  db_delivery   |
+--------+-------+  +--------+-------+  +--------+-------+  +--------+-------+
         |                   |                   |                   |
         |                   |   <--- WebClient -+                   |
         |                   |                   |                   |
         +-------------------+---------+---------+-------------------+
                                       |
                                +------v------+
                                |   Eureka    |
                                |   Server    |
                                |   :8761     |
                                +-------------+
```

### Patrón de comunicación

- **API Gateway** (puerto 8080): punto único de entrada, enruta peticiones a los microservicios.
- **Eureka Server** (puerto 8761): registro centralizado de servicios para discovery.
- **WebClient**: comunicación REST asíncrona entre `ms-orden` y `ms-restaurante` (validación de catálogo).
- Cada microservicio tiene su base de datos independiente (patrón Database per Service).

---

## Stack Tecnológico

| Categoría | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Microservicios | Spring Cloud 2025.1.1 |
| Persistencia | Spring Data JPA + Hibernate |
| Base de datos | MySQL 8 (Laragon en local, contenedor en Docker) |
| API Gateway | Spring Cloud Gateway |
| Service Discovery | Netflix Eureka |
| Comunicación REST | WebClient (Spring WebFlux) |
| Documentación API | OpenAPI / Swagger 3 (springdoc 2.8.8) |
| Testing | JUnit 5 + Mockito + AssertJ |
| Cobertura | JaCoCo 0.8.12 |
| Build | Maven 3 |
| Contenedores | Docker + Docker Compose |
| Boilerplate | Lombok |

---

## Estructura del Proyecto

```
fastfood-delivery-ev3/
├── docker-compose.yml     Orquestacion de todos los servicios
├── eureka-server/         Servidor de descubrimiento (:8761)
├── api-gateway/           Gateway con enrutamiento (:8080)
├── ms-usuario/            Usuarios, direcciones, regiones, comunas (:8081)
├── ms-restaurante/        Restaurantes, catálogo, promociones (:8082)
├── ms-orden/              Carrito, items, órdenes, pagos (:8087)
└── ms-delivery/           Conductores y delivery (:8089)
```

Cada microservicio incluye su propio `Dockerfile` y sigue la arquitectura por capas:

```
src/main/java/com/fastfood/ms_xxx/
├── controller/    REST endpoints
├── service/       Lógica de negocio
├── repository/    Acceso a datos (JPA)
├── model/         Entidades JPA
├── DTO/           Data Transfer Objects
├── client/        Clientes REST (solo en ms-orden)
└── config/        Beans y configuración
```

---

## Cómo Ejecutar con Docker (Recomendado)

### Prerequisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado
- En Windows: WSL 2 activado (Docker Desktop lo instala automáticamente)

### Comandos

Desde la raíz del proyecto:

```bash
# Construir las imagenes (primera vez, ~5-10 min)
docker compose build --no-cache

# Levantar todo el sistema
docker compose up

# Detener y limpiar
docker compose down
```

Esto levanta 7 contenedores en orden:

1. `mysql-db` (base de datos, imagen oficial de MySQL 8)
2. `eureka-server` (:8761) — cuando MySQL está healthy
3. `api-gateway` (:8080) — cuando Eureka está healthy
4. `ms-usuario`, `ms-restaurante`, `ms-orden`, `ms-delivery` — en paralelo

Tiempo total de arranque: ~90 segundos.

### Verificar que todo funciona

- Dashboard de Eureka: http://localhost:8761
- API Gateway: http://localhost:8080/api/v1/usuarios

En Eureka deben aparecer los 4 microservicios registrados como UP.

### Configuración con variables de entorno

Los `application.yml` y `application-dev.yml` usan variables de entorno con valores por defecto, lo que permite ejecutar el proyecto en dos entornos sin modificar código:

- **Con Docker**: las variables `DATABASE_HOST`, `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE`, `MS_XXX_URL` son inyectadas por `docker-compose.yml`.
- **En local (Laragon)**: al no existir las variables, se usan los valores por defecto (`localhost`).

Ejemplo:

```yaml
url: jdbc:mysql://${DATABASE_HOST:localhost}:3306/db_usuario
```

---

## Cómo Ejecutar en Local (sin Docker)

### Prerequisitos

- Java 21
- Maven 3.8+
- MySQL 8 (recomendado: [Laragon](https://laragon.org/))
- Crear las 4 bases de datos:

```sql
CREATE DATABASE db_usuario;
CREATE DATABASE db_restaurante;
CREATE DATABASE db_orden;
CREATE DATABASE db_delivery;
```

### Orden de arranque (6 terminales)

1. Eureka Server
```powershell
cd eureka-server/eureka-server
./mvnw spring-boot:run
```
Dashboard: http://localhost:8761

2. ms-usuario
```powershell
cd ms-usuario/ms-usuario
./mvnw spring-boot:run
```

3. ms-restaurante
```powershell
cd ms-restaurante/ms-restaurante
./mvnw spring-boot:run
```

4. ms-orden
```powershell
cd ms-orden/ms-orden
./mvnw spring-boot:run
```

5. ms-delivery
```powershell
cd ms-delivery/ms-delivery
./mvnw spring-boot:run
```

6. API Gateway
```powershell
cd api-gateway/api-gateway
./mvnw spring-boot:run
```

---

## Endpoints Principales

Todos accesibles via API Gateway en `http://localhost:8080`.

| Recurso | Método | Path | Descripción |
|---|---|---|---|
| Usuarios | GET | `/api/v1/usuarios` | Listar todos los usuarios |
| Usuarios | GET | `/api/v1/usuarios/{id}` | Buscar por ID |
| Usuarios | POST | `/api/v1/usuarios` | Crear usuario |
| Restaurantes | GET | `/api/v1/restaurantes` | Listar restaurantes |
| Catálogos | GET | `/api/v1/catalogos` | Listar productos |
| Catálogos | GET | `/api/v1/catalogos/{id}` | Buscar producto |
| Órdenes | GET | `/api/v1/ordenes` | Listar órdenes |
| Carrito | POST | `/api/v1/carrito-items` | Agregar item (valida catálogo en ms-restaurante) |
| Pagos | POST | `/api/v1/pagos` | Procesar pago |
| Delivery | GET | `/api/v1/deliveries` | Estado de delivery |

### Documentación Swagger (solo en modo local)

Cada microservicio expone su propio Swagger UI:

- ms-usuario: http://localhost:8081/swagger-ui.html
- ms-restaurante: http://localhost:8082/swagger-ui.html
- ms-orden: http://localhost:8087/swagger-ui.html
- ms-delivery: http://localhost:8089/swagger-ui.html

En modo Docker, los microservicios no exponen puertos directamente (solo Gateway y Eureka). Todo el tráfico pasa por el Gateway.

---

## Tests y Cobertura

Total: 144 tests unitarios distribuidos en los 4 microservicios.

| Microservicio | Tests | Cobertura paquete `service` |
|---|---|---|
| ms-usuario | 32 | 82% |
| ms-restaurante | 28 | 94% |
| ms-orden | 55 | 100% |
| ms-delivery | 29 | 100% |

### Ejecutar tests y generar reporte de cobertura

```powershell
cd ms-xxx/ms-xxx
./mvnw clean test
```

El reporte HTML se genera en `target/site/jacoco/index.html`.

### Convención de tests

- Patrón Given / When / Then con comentarios
- AssertJ para assertions fluidas
- Mockito para mocks de repositorios y clientes REST
- Anotación `@DisplayName` con descripción del caso

---

## Comunicación entre Microservicios

`ms-orden` consume `ms-restaurante` via WebClient para validar que un producto del catálogo exista antes de agregarlo al carrito:

```
POST /api/v1/carrito-items
    |
    v
CarritoItemService.guardar()
    |
    v
RestauranteClient.obtenerCatalogoPorId(idCatalogo)
    |
    v
GET http://ms-restaurante:8082/api/v1/catalogos/{id}   <-- via WebClient
    |
    v
Si existe: guarda el item con el precio del catálogo
Si no existe: lanza excepción "Catalogo con ID X no encontrado."
```

---

## Gestión del Proyecto

El avance del proyecto se gestiona en Trello:

Tablero público: https://trello.com/invite/b/6a406828f7a2d3cc405bbb30/ATTIe3a10a83a2fa11f5511075d2a7c34326186F1B50/ev3-fastfood-delivery-microservicios

Estructura del tablero:

- Backlog: tareas planificadas
- En Progreso: desarrollo activo
- Revisión / Tests: pendiente de validar
- Hecho: completado

---

## Estado actual

### Implementado

- [x] Arquitectura de 4 microservicios + Gateway + Eureka
- [x] Persistencia con MySQL y JPA
- [x] Validaciones con Bean Validation
- [x] API Gateway con 14 rutas configuradas
- [x] Documentación Swagger en los 4 microservicios
- [x] Configuración YAML con perfiles (dev, test, prod)
- [x] Tests unitarios con 80%+ cobertura en los 4 servicios
- [x] Service discovery con Eureka
- [x] Comunicación REST entre ms-orden y ms-restaurante (WebClient)
- [x] Reportes de cobertura con JaCoCo
- [x] Registro de los 4 microservicios en Eureka
- [x] Dockerización completa: 6 Dockerfiles + docker-compose.yml
- [x] Variables de entorno para portabilidad local/Docker

---

## Notas de Desarrollo

- La estructura tiene anidamiento doble (`ms-usuario/ms-usuario/`) debido a la inicialización del proyecto.
- El servicio `ms-orden` usa puerto 8087 (no 8083) por convención del proyecto.
- En desarrollo local se usa el perfil `dev` con MySQL de Laragon.
- En Docker se usa el mismo perfil `dev` pero la BD está en un contenedor.
- Los tests unitarios mockean el repository y clientes REST con Mockito.

---

## Licencia

Proyecto académico desarrollado para Duoc UC, asignatura DSY1103 — Microservicios.