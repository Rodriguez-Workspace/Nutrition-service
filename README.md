# Nutrition Service - Azure Container Apps Deployment

Este proyecto es un microservicio Spring Boot que se despliega automáticamente en Azure Container Apps usando GitHub Actions.

## 🚀 Ejecución Local

### Prerrequisitos
- Java 21
- Maven 3.6+
- Docker (opcional, para pruebas de contenedor)

### Pasos para ejecutar localmente

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd Nutrition-service
   ```

2. **Configurar base de datos**
   - Asegúrate de tener MySQL ejecutándose
   - Actualiza `src/main/resources/application.properties` con tus credenciales

3. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```
   
   La aplicación estará disponible en: `http://localhost:8086`

4. **Documentación API**
   - Swagger UI: `http://localhost:8086/swagger-ui.html`
   - OpenAPI JSON: `http://localhost:8086/v3/api-docs`

## 📦 Generación del JAR

### Construir el JAR ejecutable

```bash
mvn clean package
```

El JAR se generará en: `target/nutrition-service-0.0.1-SNAPSHOT.jar`

### Ejecutar el JAR directamente

```bash
java -jar target/nutrition-service-0.0.1-SNAPSHOT.jar
```

## 🐳 Docker

### Construir imagen localmente

```bash
docker build -t nutrition-service:latest .
```

### Ejecutar contenedor

```bash
docker run -p 8086:8086 nutrition-service:latest
```

## ☁️ Despliegue Automático en Azure

### Configuración de Secretos en GitHub

Antes del primer despliegue, configura estos secretos en tu repositorio de GitHub:

1. Ve a **Settings** > **Secrets and variables** > **Actions**
2. Agrega los siguientes secrets:

| Secret | Descripción | Ejemplo |
|--------|-------------|---------|
| `AZURE_CREDENTIALS` | JSON del service principal con permisos Contributor | `{"clientId":"xxx","clientSecret":"xxx","subscriptionId":"xxx","tenantId":"xxx"}` |
| `ACR_LOGIN_SERVER` | URL del Azure Container Registry | `seniorhubacr.azurecr.io` |
| `ACR_USERNAME` | Usuario del ACR | `seniorhubacr` |
| `ACR_PASSWORD` | Contraseña del ACR | `xxxxxxxxxxxxxxxx` |

### Crear Service Principal para Azure

```bash
az ad sp create-for-rbac \
  --name "nutrition-service-deploy" \
  --role contributor \
  --scopes /subscriptions/{subscription-id}/resourceGroups/seniorhub-rg \
  --sdk-auth
```

Copia la salida JSON completa al secret `AZURE_CREDENTIALS`.

### Proceso de Despliegue

1. **Push a main branch**
   ```bash
   git add .
   git commit -m "Deploy nutrition service"
   git push origin main
   ```

2. **GitHub Actions automáticamente:**
   - ✅ Compila el proyecto con Maven
   - ✅ Construye la imagen Docker
   - ✅ Sube la imagen al Azure Container Registry
   - ✅ Despliega en Azure Container Apps

3. **Monitorear el despliegue**
   - Ve a la pestaña **Actions** en GitHub
   - Observa el progreso del workflow `Deploy to Azure Container Apps`

## 📊 Monitoreo en Azure

### Ver logs de la aplicación

```bash
# Logs del contenedor
az containerapp logs show \
  --name nutrition-service \
  --resource-group seniorhub-rg \
  --follow

# Logs de despliegue
az containerapp revision list \
  --name nutrition-service \
  --resource-group seniorhub-rg \
  --output table
```

### Acceder a la aplicación

Una vez desplegado, la aplicación estará disponible en:
```
https://nutrition-service.{random-id}.{region}.azurecontainerapps.io
```

Para obtener la URL exacta:
```bash
az containerapp show \
  --name nutrition-service \
  --resource-group seniorhub-rg \
  --query properties.configuration.ingress.fqdn \
  --output tsv
```

### Escalar la aplicación

```bash
# Escalar manualmente
az containerapp update \
  --name nutrition-service \
  --resource-group seniorhub-rg \
  --min-replicas 1 \
  --max-replicas 5
```

## 🔧 Configuración de Variables de Entorno

Las variables de entorno se pueden configurar en Azure Container Apps:

```bash
az containerapp update \
  --name nutrition-service \
  --resource-group seniorhub-rg \
  --set-env-vars "SPRING_PROFILES_ACTIVE=prod" "DATABASE_URL=jdbc:mysql://..."
```

## 🐛 Troubleshooting

### Problemas comunes

1. **Error en compilación Maven**
   ```bash
   mvn clean compile
   mvn dependency:resolve
   ```

2. **Imagen Docker no se construye**
   - Verifica que el JAR existe en `target/`
   - Revisa el Dockerfile

3. **Despliegue falla**
   - Verifica los secrets de GitHub
   - Revisa los logs del workflow
   - Confirma que el resource group existe

### Comandos útiles

```bash
# Ver estado del Container App
az containerapp show \
  --name nutrition-service \
  --resource-group seniorhub-rg

# Reiniciar la aplicación
az containerapp revision restart \
  --name nutrition-service \
  --resource-group seniorhub-rg \
  --revision {revision-name}

# Ver métricas
az monitor metrics list \
  --resource /subscriptions/{sub-id}/resourceGroups/seniorhub-rg/providers/Microsoft.App/containerApps/nutrition-service
```

## 📁 Estructura del Proyecto

```
├── .github/workflows/
│   └── deploy.yml              # GitHub Actions workflow
├── src/
│   ├── main/java/              # Código fuente Java
│   └── main/resources/         # Configuración y recursos
├── target/                     # JAR compilado (generado)
├── Dockerfile                  # Configuración de contenedor
├── pom.xml                     # Dependencias Maven
└── README.md                   # Esta documentación
```

## 🔄 Flujo de Desarrollo

1. Desarrolla localmente y prueba con `mvn spring-boot:run`
2. Commit y push a una rama feature
3. Crea Pull Request
4. Merge a `main` → despliegue automático
5. Monitorea logs en Azure

¡Listo! Tu aplicación se desplegará automáticamente cada vez que hagas push a main. 🎉