# Dealership API

API RESTful para gerenciamento de concessionária de veículos, desenvolvida com Spring Boot 4.0.0 e Java 21.

## Sobre o Projeto

O Dealership API é um sistema de gerenciamento para concessionárias que permite o controle de veículos e vendas. A aplicação fornece endpoints para criação, consulta e atualização de veículos, além de gerenciar todo o fluxo de vendas, desde a criação do pedido até a confirmação de pagamento.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 4.0.0**
  - Spring Data JPA
  - Spring Security (OAuth2 Resource Server)
  - Spring Validation
- **PostgreSQL** (Neon - produção)
- **H2** (testes)
- **AWS Cognito** (autenticação JWT)
- **SpringDoc OpenAPI 2.8.4** (documentação)
- **Docker** e **Docker Compose**
- **Kubernetes** (orquestração de containers)
- **Maven 3.9.6**
- **JUnit 5** e **JaCoCo** (cobertura de testes)

## Arquitetura

O projeto segue os princípios da **Arquitetura Hexagonal (Ports & Adapters)** com separação clara de responsabilidades em módulos independentes.

### Estrutura de Camadas

```
┌─────────────────────────────────────┐
│      Camada de Apresentação         │
│   (Controllers REST)                │
├─────────────────────────────────────┤
│      Camada de Aplicação            │
│   (Services e Use Cases)            │
├─────────────────────────────────────┤
│      Camada de Domínio              │
│   (Entidades, Regras de Negócio)    │
├─────────────────────────────────────┤
│      Portas (Interfaces)            │
│   (Contratos entre módulos)         │
├─────────────────────────────────────┤
│      Adaptadores                    │
│   (Repositories, DTOs, Mappers)     │
└─────────────────────────────────────┘
```

### Estrutura de Diretórios

```
src/main/java/br/com/dealership/
├── config/                    # Configurações globais (OpenAPI, Jackson)
├── exception/                 # Tratamento global de exceções
├── health/                    # Endpoint de health check
├── security/                  # Configuração OAuth2/JWT
└── modules/
    ├── vehicle/               # Módulo de Veículos
    │   ├── adapter/
    │   │   ├── database/      # Entidades JPA e Repositories
    │   │   └── http/          # Controllers e DTOs
    │   ├── application/
    │   │   ├── services/      # Orquestração de casos de uso
    │   │   └── useCases/      # Casos de uso individuais
    │   ├── domain/
    │   │   ├── entities/      # Entidades de domínio
    │   │   ├── exception/     # Exceções de domínio
    │   │   └── ports/         # Interfaces (in/out)
    │   └── mapper/            # Mapeamento entre camadas
    ├── sale/                  # Módulo de Vendas (mesma estrutura)
    └── shared/                # Portas compartilhadas entre módulos
```

### Módulos

#### 1. Vehicle Module
Responsável pelo gerenciamento de veículos.

**Entidade Principal:** `Vehicle`
- Atributos: id, make, model, year, vin, color, status (AVAILABLE/SOLD), price
- Validações de negócio integradas

**Principais Casos de Uso:**
- `CreateVehicleUseCase`: Criação de veículos (apenas Admin)
- `GetVehicleByVinUseCase`: Busca por VIN
- `GetVehicleByStatusUseCase`: Filtragem por status (ordenado por preço)
- `UpdateVehicleUseCase`: Atualização de dados (apenas Admin)
- `MarkVehicleAsSoldUseCase`: Marcação de veículo como vendido

**Endpoints:**
- `POST /api/v1/vehicles` - Criar veículo (Admin)
- `GET /api/v1/vehicles/{vin}` - Buscar por VIN
- `GET /api/v1/vehicles/available` - Listar veículos disponíveis (ordenado por preço)
- `GET /api/v1/vehicles/sold` - Listar veículos vendidos (ordenado por preço)
- `PUT /api/v1/vehicles/{id}` - Atualizar veículo (Admin)

#### 2. Sale Module
Responsável pelo gerenciamento de vendas.

**Entidade Principal:** `SaleOrder`
- Atributos: id, customerName, customerCpf, vehicleVin, salePrice, vehicleId, status, saleDate
- Status: PENDING, COMPLETED, CANCELED

**Value Object:** `CPF`
- Implementa validação de CPF brasileiro
- Formatação automática (XXX.XXX.XXX-XX)
- Imutável com equals/hashCode

**Principais Casos de Uso:**
- `CreateSaleUseCase`: Criação de vendas
- `FindSaleByIdUseCase`: Busca por ID
- `FindAllSalesUseCase`: Listagem de vendas
- `FindAllSaleByCustomerCPFUseCase`: Busca por CPF do cliente
- `CompleteSaleUseCase`: Finalização da venda (preenche saleDate)

**Endpoints:**
- `POST /api/v1/sales` - Criar ordem de venda
- `GET /api/v1/sales/{id}` - Buscar venda por ID
- `GET /api/v1/sales?cpf={cpf}` - Listar vendas (filtro opcional por CPF)
- `POST /api/v1/sales/payment-webhook/{id}` - Webhook de pagamento (sem autenticação)

#### 3. Shared Module

O módulo **shared** implementa o conceito de **Portas (Ports)** da Arquitetura Hexagonal, servindo como camada de integração entre módulos.

**Propósito:**
- **Desacoplamento:** Módulos se comunicam através de interfaces, não de implementações concretas
- **Inversão de Dependências:** O módulo Sale depende de portas definidas no Shared, implementadas pelo módulo Vehicle
- **Contratos Bem Definidos:** DTOs e interfaces compartilhadas garantem contratos claros entre módulos

**Fluxo de Dependências:**
```
Sale Module (Domínio) → Shared (Portas) ← Vehicle Module (Adaptador)
```

## Segurança

A aplicação utiliza **OAuth2 Resource Server** com tokens JWT fornecidos pelo **AWS Cognito**.

**Autenticação:**
- Tokens JWT validados contra o issuer do AWS Cognito
- Claims extraídas: `name`, `custom:cpf`, `cognito:groups`

**Autorização:**
- Controle de acesso baseado em roles (RBAC)
- Role `Admin` necessária para criação e atualização de veículos
- Segurança em nível de método via `@PreAuthorize`

**Endpoints Públicos:**
- `/health` - Health check
- `/api-docs/**` - Documentação OpenAPI
- `/swagger-ui/**` - Interface Swagger
- `/api/v1/sales/payment-webhook/**` - Webhook de pagamento

## Como Usar Localmente

### Pré-requisitos

- Java 21 JDK
- Maven 3.9.6+
- Docker e Docker Compose
- kubectl (para Kubernetes)

### Opção 1: Usando Docker Compose

1. Clone o repositório:
```bash
git clone https://github.com/Norrels/tech-challenge-soat-3
cd Dealership-api
```

2. Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:
```env
POSTGRES_DB=dealership_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/dealership_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
AWS_COGNITO_ISSUER_URI=https://cognito-idp.<region>.amazonaws.com/<user-pool-id>
```

3. Execute o Docker Compose:
```bash
docker-compose up -d
```

A aplicação estará disponível em `http://localhost:8080`.

### Opção 2: Usando Kubernetes

1. Construa e publique a imagem Docker:
```bash
docker build -t seuusuario/dealership-api:v1.0.0 .
docker push seuusuario/dealership-api:v1.0.0
```

2. Configure os secrets (copie o template e edite):
```bash
cp k8s/secret.yaml.example k8s/secret.yaml
# Edite k8s/secret.yaml com suas credenciais
```

3. Atualize a imagem no deployment:
```bash
# Edite k8s/deployment.yaml e altere a linha:
# image: seuusuario/dealership-api:v1.0.0
```

4. Aplique os manifestos:
```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

5. Verifique o status:
```bash
kubectl get pods -n dealership
```

6. Acesse a aplicação via port-forward:
```bash
kubectl port-forward -n dealership svc/dealership-api-service 8080:80
```

### Acessando a Documentação

Após iniciar a aplicação, acesse:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api-docs

## Kubernetes

### Arquitetura do Cluster

```
┌──────────────────────────────────────────────────────────────┐
│                    Namespace: dealership                     │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌─────────────┐     ┌─────────────────────────────────┐     │
│  │   Service   │───> │            Deployment           │     │
│  │  ClusterIP  │     │  (3 réplicas com RollingUpdate) │     │
│  │   :80       │     │                                 │     │
│  └─────────────┘     │    ┌─────┐  ┌─────┐  ┌─────┐    │     │
│                      │    │ Pod │  │ Pod │  │ Pod │    │     │
│                      │    └─────┘  └─────┘  └─────┘    │     │
│                      └─────────────────────────────────┘     │
│                                   │                          │
│                      ┌────────────┴────────────┐             │
│                      ▼                         ▼             │
│              ┌─────────────┐          ┌─────────────┐        │
│              │  ConfigMap  │          │    Secret   │        │
│              │  (configs)  │          │(credentials)│        │
│              └─────────────┘          └─────────────┘        │
│                                                              │
└──────────────────────────────────────────────────────────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │  Neon (PostgreSQL) │
                    │   (Banco Externo)  │
                    └────────────────────┘
```

### Manifestos Kubernetes

| Arquivo | Descrição |
|---------|-----------|
| `namespace.yaml` | Namespace dedicado `dealership` |
| `configmap.yaml` | Configurações não sensíveis (Spring profiles, JPA settings) |
| `secret.yaml.example` | Template para credenciais (DB, Cognito) |
| `deployment.yaml` | Deployment com 3 réplicas, health probes, resources |
| `service.yaml` | ClusterIP service para expor a aplicação |

### Recursos do Deployment

- **Réplicas:** 3
- **Strategy:** RollingUpdate (maxSurge: 1, maxUnavailable: 0)
- **Resources:**
  - Requests: 250m CPU, 512Mi RAM
  - Limits: 500m CPU, 1Gi RAM
- **Health Probes:**
  - Liveness: `/health` (initialDelay: 90s)
  - Readiness: `/health` (initialDelay: 75s)
- **Security:**
  - runAsNonRoot: true
  - allowPrivilegeEscalation: false

## Como Testar

### Executar Todos os Testes

```bash
./mvnw test
```

### Executar Testes com Relatório de Cobertura

```bash
./mvnw verify
```

O relatório de cobertura será gerado em `target/site/jacoco/index.html`.

### Requisitos de Cobertura

O projeto possui as seguintes metas de cobertura (verificadas pelo JaCoCo):
- Cobertura de instruções: mínimo 80%
- Cobertura de linhas: mínimo 80%
- Cobertura de branches: mínimo 70%

## Build e Deploy

### Build Docker

```bash
docker build -t dealership-api:v1.0.0 .
docker run -p 8080:8080 --env-file .env dealership-api:v1.0.0
```

### Deploy no Kubernetes

```bash
# Build e push
docker build -t seuusuario/dealership-api:v1.0.0 .
docker push seuusuario/dealership-api:v1.0.0

# Deploy
kubectl apply -f k8s/
```

### CI/CD

O projeto possui pipeline de CI/CD configurado no GitHub Actions:

1. **PR Checks:** Execução de testes e verificação de cobertura em cada Pull Request
2. **Deploy:** Build da imagem Docker e push para AWS ECR ao realizar merge na branch master

## Health Check

A aplicação expõe um endpoint de health check em:

```
GET /health
```

Resposta:
```json
{
  "status": "ok"
}
```

## Licença

Este projeto foi desenvolvido como parte do Tech Challenge SOAT - Pós-Tech FIAP.
