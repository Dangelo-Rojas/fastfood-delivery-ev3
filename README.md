# FastFood-Delivery — Arquitectura de Microservicios (EV3)

Conversión de la aplicación monolítica **FastFood-Delivery** a una arquitectura de microservicios con Spring Boot.

## Microservicios

| Módulo | Puerto | Responsabilidad |
|---|---|---|
| `api-gateway` | 8080 | Punto de entrada único, enrutamiento hacia los microservicios |
| `ms-usuario` | 8081 | Usuarios, regiones, comunas y direcciones |
| `ms-restaurante` | 8082 | Restaurantes, catálogo y promociones |
| `ms-orden` | 8087 | Órdenes, carritos, pagos y métodos de pago |
| `ms-delivery` | 8089 | Entregas y conductores |

## Tecnologías

- Java 21
- Spring Boot 4.0.6 (Web MVC, Data JPA, Validation)
- Spring Cloud Gateway (MVC)
- MySQL (Laragon)
- Maven
- Swagger / OpenAPI (springdoc) — *en integración*
- Postman para pruebas

## Cómo ejecutar

1. Levantar MySQL (Laragon) y verificar las bases de datos de cada microservicio.
2. Iniciar cada microservicio desde su carpeta:
   ```bash
   ./mvnw spring-boot:run
   ```
   (en Windows PowerShell: `.\mvnw spring-boot:run`)
3. Iniciar el `api-gateway` al final.
4. Probar los endpoints a través del gateway: `http://localhost:8080/...`

## Documentación de APIs (Swagger)

Una vez integrado springdoc, la documentación de cada microservicio queda disponible en:

- ms-usuario → http://localhost:8081/swagger-ui.html
- ms-restaurante → http://localhost:8082/swagger-ui.html
- ms-orden → http://localhost:8087/swagger-ui.html
- ms-delivery → http://localhost:8089/swagger-ui.html

## Integrantes

- Dangelo
- (agregar integrante 2)
- (agregar integrante 3)
