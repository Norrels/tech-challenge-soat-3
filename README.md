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
- **PostgreSQL** (produção)
- **H2** (testes)
- **AWS Cognito** (autenticação JWT)
- **SpringDoc OpenAPI 2.8.4** (documentação)
- **Docker** e **Docker Compose**
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

### Módulos

#### 1. Vehicle Module
Responsável pelo gerenciamento de veículos.

**Entidade Principal:** `Vehicle`
- Atributos: id, make, model, year, vin, color, status (AVAILABLE/SOLD), price
- Validações de negócio integradas

**Principais Casos de Uso:**
- `CreateVehicleUseCase`: Criação de veículos (apenas Admin)
- `GetVehicleByVinUseCase`: Busca por VIN
- `GetVehicleByStatusUseCase`: Filtragem por status
- `UpdateVehicleUseCase`: Atualização de dados (apenas Admin)
- `MarkVehicleAsSoldUseCase`: Marcação de veículo como vendido

**Endpoints:**
- `POST /api/v1/vehicles` - Criar veículo (Admin)
- `GET /api/v1/vehicles/{vin}` - Buscar por VIN
- `GET /api/v1/vehicles/available` - Listar veículos disponíveis
- `GET /api/v1/vehicles/sold` - Listar veículos vendidos
- `PUT /api/v1/vehicles/{id}` - Atualizar veículo (Admin)

#### 2. Sale Module
Responsável pelo gerenciamento de vendas.

**Entidade Principal:** `SaleOrder`
- Atributos: id, customerName, customerCpf, vehicleVin, salePrice, vehicleId, status
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
- `CompleteSaleUseCase`: Finalização da venda

**Endpoints:**
- `POST /sales` - Criar ordem de venda
- `GET /sales/{id}` - Buscar venda por ID
- `GET /sales?cpf={cpf}` - Listar vendas (filtro opcional por CPF)
- `POST /sales/payment-webhook/{id}` - Webhook de pagamento (sem autenticação)

#### 3. Shared Module

O módulo **shared** é fundamental para a arquitetura do sistema. Ele implementa o conceito de **Portas (Ports)** da Arquitetura Hexagonal, servindo como camada de integração entre módulos.

**Propósito:**
- **Desacoplamento:** Módulos se comunicam através de interfaces, não de implementações concretas
- **Inversão de Dependências:** O módulo Sale depende de portas definidas no Shared, implementadas pelo módulo Vehicle
- **Contratos Bem Definidos:** DTOs e interfaces compartilhadas garantem contratos claros entre módulos

**Componentes Principais:**
- `FindAvailableVehicleByIdUseCasePort`: Interface para buscar veículos disponíveis
- `MarkVehicleAsSoldUseCasePort`: Interface para marcar veículo como vendido
- `FindVehicleDTO`: DTO para transferência de dados de veículos entre módulos

**Fluxo de Dependências:**
```
Sale Module (Domínio) → Shared (Portas) ← Vehicle Module (Adaptador)
```

Essa abordagem permite que o módulo de vendas realize operações sobre veículos sem conhecer detalhes de implementação, facilitando testes, manutenção e evolução independente dos módulos.

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
- `/sales/payment-webhook/**` - Webhook de pagamento

## Como Usar Localmente

### Pré-requisitos

- Java 21 JDK
- Maven 3.9.6+
- Docker e Docker Compose (opcional, mas recomendado)
- PostgreSQL 15+ (se não usar Docker)

### Opção 1: Usando Docker Compose (Recomendado)

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

### Opção 2: Execução Manual

1. Configure o PostgreSQL e crie o banco de dados:
```sql
CREATE DATABASE dealership_db;
```

2. Configure as variáveis de ambiente ou edite `src/main/resources/application.yaml`:
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/dealership_db
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
export AWS_COGNITO_ISSUER_URI=https://cognito-idp.<region>.amazonaws.com/<user-pool-id>
```

3. Execute a aplicação:
```bash
./mvnw clean install
./mvnw spring-boot:run
```

### Acessando a Documentação

Após iniciar a aplicação, acesse:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/api-docs

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

### Estrutura de Testes

**Testes Unitários:**
- Testes de Use Cases: `src/test/java/br/com/dealership/modules/{module}/application/useCases/`
- Testes de Entidades: `src/test/java/br/com/dealership/modules/{module}/domain/entities/`

**Testes de Integração:**
- Controllers: `src/test/java/br/com/dealership/integration/`
- Fluxo completo: `src/test/java/br/com/dealership/integration/e2e/`

**Utilitários de Teste:**
- `JwtTestHelper`: Geração de tokens JWT para testes
  - `createAdminJwt()`: Token com role Admin
  - `createRegularUserJwt()`: Token de usuário comum

### Requisitos de Cobertura

O projeto possui as seguintes metas de cobertura (verificadas pelo JaCoCo):
- Cobertura de instruções: mínimo 80%
- Cobertura de linhas: mínimo 80%
- Cobertura de branches: mínimo 70%

### Executar Testes de um Módulo Específico

```bash
# Testes do módulo Vehicle
./mvnw test -Dtest="br.com.dealership.modules.vehicle.**"

# Testes do módulo Sale
./mvnw test -Dtest="br.com.dealership.modules.sale.**"

# Testes de integração
./mvnw test -Dtest="br.com.dealership.integration.**"
```

## Build e Deploy

### Gerar Build Local

```bash
./mvnw clean package
java -jar target/dealership-api-0.0.1-SNAPSHOT.jar
```

### Build Docker

```bash
docker build -t dealership-api:latest .
docker run -p 8080:8080 --env-file .env dealership-api:latest
```

### Deploy Automático

O projeto possui pipeline de CI/CD configurado no GitHub Actions que realiza:

1. **PR Checks:** Execução de testes e verificação de cobertura em cada Pull Request
2. **Deploy:** Build da imagem Docker e push para AWS ECR ao realizar merge na branch master

Mais detalhes podem ser encontrados em [adr/deploy.md](adr/deploy.md).

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

