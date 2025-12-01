# Nutrition Service - Azure Deployment

Microservicio Spring Boot para gestión nutricional como parte de la plataforma SeniorHub, optimizado para despliegue en Azure Container Apps.

## 🏗️ Arquitectura

- **Framework**: Spring Boot 3.5.0
- **Java**: 21
- **Base de Datos**: Azure Database for MySQL Flexible Server
- **Contenedor**: Docker optimizado para producción
- **Orquestación**: Azure Container Apps
- **CI/CD**: GitHub Actions
- **Registry**: Azure Container Registry

## 📋 Requisitos Previos

### Desarrollo Local
- Java 21
- Maven 3.6+
- Docker
- MySQL 8.0+ (opcional para desarrollo local)

### Azure
- Suscripción de Azure activa
- Azure CLI instalado
- Permisos para crear recursos en Azure

## 🚀 Desarrollo Local

### 1. Clonar el Repositorio
```bash
git clone <repository-url>
cd Nutrition-service
```

### 2. Configuración Local
```bash
# Copiar variables de entorno (opcional)
cp .env.example .env
```

### 3. Ejecutar con Maven
```bash
# Instalar dependencias y ejecutar tests
mvn clean test

# Compilar y ejecutar
mvn clean package
mvn spring-boot:run
```

### 4. Ejecutar con Docker
```bash
# Construir imagen
docker build -t nutrition-service .

# Ejecutar contenedor
docker run -p 8086:8086 \
  -e DB_HOST=host.docker.internal \
  -e DB_NAME=nutrition_service \
  -e DB_USER=root \
  -e DB_PASSWORD=password \
  nutrition-service
```

## 🔧 Variables de Entorno

### Base de Datos
| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `DB_HOST` | Host del servidor MySQL | `localhost` |
| `DB_PORT` | Puerto de MySQL | `3306` |
| `DB_NAME` | Nombre de la base de datos | `nutrition_service` |
| `DB_USER` | Usuario de la base de datos | `root` |
| `DB_PASSWORD` | Contraseña de la base de datos | `12345678` |

### Microservicios Externos
| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `SERVICES_IAM_URL` | URL del servicio IAM | `http://localhost:8080` |
| `SERVICES_RESIDENTS_URL` | URL del servicio de Residentes | `http://localhost:8081` |
| `SERVICES_USERS_URL` | URL del servicio de Usuarios | `http://localhost:8083` |
| `SERVICES_APPOINTMENTS_URL` | URL del servicio de Citas | `http://localhost:8085` |
| `SERVICES_NOTIFICATIONS_URL` | URL del servicio de Notificaciones | `http://localhost:8084` |
| `SERVICES_PAYMENTS_URL` | URL del servicio de Pagos | `http://localhost:8082` |

### Configuración General
| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `SERVER_PORT` | Puerto del servidor | `8086` |
| `SWAGGER_ENABLED` | Habilitar Swagger UI | `true` |
| `LOG_LEVEL` | Nivel de logging | `INFO` |
| `SPRING_PROFILES_ACTIVE` | Perfil activo de Spring | `default` |

## 🚢 Despliegue en Azure

### 1. Configurar Secrets en GitHub

En tu repositorio de GitHub, ve a **Settings > Secrets and variables > Actions** y configura:

#### Secrets de Azure
```
AZURE_CREDENTIALS={
  "clientId": "your-client-id",
  "clientSecret": "your-client-secret",
  "subscriptionId": "your-subscription-id",
  "tenantId": "your-tenant-id"
}
AZURE_SUBSCRIPTION_ID=your-subscription-id
```

#### Secrets de Azure Container Registry
```
ACR_USERNAME=seniorhubacr
ACR_PASSWORD=your-acr-password
```

#### Secrets de Base de Datos
```
DB_HOST=seniorhub-mysql.mysql.database.azure.com
DB_NAME=nutritiondb
DB_USER=your-db-admin-user
DB_PASSWORD=your-secure-db-password
```

#### URLs de Microservicios (Azure Container Apps)
```
SERVICES_IAM_URL=https://iam-service.internal.your-env.azurecontainerapps.io
SERVICES_RESIDENTS_URL=https://residents-service.internal.your-env.azurecontainerapps.io
SERVICES_USERS_URL=https://users-service.internal.your-env.azurecontainerapps.io
SERVICES_APPOINTMENTS_URL=https://appointments-service.internal.your-env.azurecontainerapps.io
SERVICES_NOTIFICATIONS_URL=https://notifications-service.internal.your-env.azurecontainerapps.io
SERVICES_PAYMENTS_URL=https://payments-service.internal.your-env.azurecontainerapps.io
```

### 2. Crear Service Principal para Azure

```bash
# Crear service principal
az ad sp create-for-rbac --name "nutrition-service-sp" \
  --role contributor \
  --scopes /subscriptions/{subscription-id}/resourceGroups/{resource-group} \
  --sdk-auth

# El output será el valor para AZURE_CREDENTIALS
```

### 3. Desplegar

El despliegue es automático:
- **Push a `main`**: Despliega automáticamente a Azure
- **Pull Request**: Solo ejecuta CI (tests y build)

### 4. Verificar Despliegue

```bash
# Ver logs del Container App
az containerapp logs show --name nutrition-service --resource-group rg-seniorhub

# Ver estado del Container App
az containerapp show --name nutrition-service --resource-group rg-seniorhub
```

## 📊 Endpoints

### Health Checks
- **Liveness**: `/actuator/health`
- **Readiness**: `/actuator/health/readiness`
- **Metrics**: `/actuator/metrics`

### API Documentation
- **Swagger UI**: `/swagger-ui.html`
- **OpenAPI Docs**: `/api-docs`

### Business Endpoints
- **Base URL**: `https://nutrition-service.{env}.azurecontainerapps.io`
- Ver documentación completa en Swagger UI

## 🔍 Monitoreo

### Logs
```bash
# Ver logs en tiempo real
az containerapp logs show --name nutrition-service \
  --resource-group rg-seniorhub --follow

# Ver logs de un periodo específico
az monitor activity-log list --resource-group rg-seniorhub \
  --start-time 2024-01-01T00:00:00Z
```

### Métricas
- Container Apps métricas disponibles en Azure Portal
- Application Insights (opcional)
- Log Analytics Workspace configurado automáticamente

## 🛠️ Solución de Problemas

### Problemas Comunes

#### 1. Error de Conexión a Base de Datos
```bash
# Verificar conectividad
az mysql flexible-server show --name seniorhub-mysql --resource-group rg-seniorhub

# Verificar firewall rules
az mysql flexible-server firewall-rule list --name seniorhub-mysql --resource-group rg-seniorhub
```

#### 2. Imagen no se puede descargar
```bash
# Verificar credenciales del ACR
az acr login --name seniorhubacr

# Verificar que la imagen existe
az acr repository show --name seniorhubacr --repository nutrition-service
```

#### 3. Container App no inicia
```bash
# Ver eventos del Container App
az containerapp revision list --name nutrition-service --resource-group rg-seniorhub

# Ver logs detallados
az containerapp logs show --name nutrition-service --resource-group rg-seniorhub --tail 100
```

### Debug Mode

Para habilitar logs debug:
```bash
# Actualizar variable de entorno
az containerapp update --name nutrition-service \
  --resource-group rg-seniorhub \
  --set-env-vars LOG_LEVEL=DEBUG
```

## 📝 Notas de Desarrollo

### Estructura del Proyecto
```
src/main/java/pe/edu/upc/center/agecare/
├── nutrition/
│   ├── application/         # Capa de aplicación
│   ├── domain/             # Lógica de negocio
│   ├── infrastructure/     # Persistencia e integraciones
│   └── interfaces/         # Controllers y DTOs
└── shared/                 # Código compartido
```

### Convenciones
- **Agregados**: Entidades principales del dominio
- **Commands**: Operaciones de escritura
- **Queries**: Operaciones de lectura
- **Value Objects**: Objetos inmutables
- **Resources**: DTOs para API REST

### Base de Datos
- **Flyway**: Migraciones automáticas
- **JPA/Hibernate**: ORM
- **MySQL 8.0**: Base de datos

## 🔐 Seguridad

- **No hardcodear credenciales** en el código
- **Usar Azure Key Vault** para secretos sensibles (opcional)
- **Conexiones SSL** habilitadas para MySQL
- **Container ejecuta como usuario no-root**
- **Health checks** configurados correctamente

## 📞 Soporte

Para problemas relacionados con:
- **Código**: Crear issue en el repositorio
- **Infraestructura**: Revisar logs de Azure
- **CI/CD**: Ver workflow en GitHub Actions