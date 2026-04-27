# 🏢 Meeting Room API

API REST para gerenciamento de salas de reunião e reservas, desenvolvida com Spring Boot 4.

## 📋 Descrição do Projeto

Sistema completo para gerenciar salas de reunião e suas reservas, incluindo:
- Cadastro e gerenciamento de salas
- Sistema de reservas com validação de conflitos
- Autenticação JWT
- Cache de consultas
- Documentação interativa com Swagger

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 4.0.6**
  - Spring Web MVC
  - Spring Data JPA
  - Spring Security
  - Spring Cache
  - Spring Validation
- **H2 Database** (banco em memória)
- **JWT (JSON Web Token)** - Autenticação
- **Lombok** - Redução de boilerplate
- **SpringDoc OpenAPI 2.7.0** - Documentação Swagger
- **JUnit 5 + Mockito** - Testes unitários
- **Maven** - Gerenciamento de dependências

## 📦 Como Executar o Projeto

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+

### Passos para execução

1. **Clone o repositório**
```bash
git clone https://github.com/PeOliveira18/MeetingRoom.git
cd MeetingRoom
```

2. **Compile o projeto**
```bash
mvn clean install
```

3. **Execute a aplicação**
```bash
mvn spring-boot:run
```

4. **Acesse a aplicação**
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:meetingroom`
  - Username: `sa`
  - Password: *(deixe em branco)*

## 🔐 Autenticação

A API utiliza JWT (JSON Web Token) para autenticação. Para acessar os endpoints protegidos:

1. Faça login no endpoint `/api/auth/login`
2. Use o token retornado no header `Authorization: Bearer {token}`

**Credenciais padrão:**
- Username: `admin`
- Password: `admin123`

## 📚 Endpoints Disponíveis

### 🔑 Autenticação

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin"
}
```

---

### 🏢 Salas de Reunião

#### Criar Sala
```http
POST /api/salas
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Sala de Reunião A",
  "capacidade": 10,
  "localizacao": "1º Andar"
}
```

#### Listar Salas
```http
GET /api/salas
Authorization: Bearer {token}

# Com filtros opcionais:
GET /api/salas?localizacao=1º Andar
GET /api/salas?capacidadeMinima=10
```

#### Buscar Sala por ID
```http
GET /api/salas/{id}
Authorization: Bearer {token}
```

#### Atualizar Sala
```http
PUT /api/salas/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nome": "Sala de Reunião A - Atualizada",
  "capacidade": 15,
  "localizacao": "2º Andar"
}
```

#### Remover Sala
```http
DELETE /api/salas/{id}
Authorization: Bearer {token}
```

---

### 📅 Reservas

#### Criar Reserva
```http
POST /api/reservas
Authorization: Bearer {token}
Content-Type: application/json

{
  "salaId": 1,
  "dataHoraInicio": "2024-12-20T14:00:00",
  "dataHoraFim": "2024-12-20T16:00:00",
  "responsavel": "João Silva"
}
```

#### Listar Reservas (com paginação)
```http
GET /api/reservas?page=0&size=10
Authorization: Bearer {token}

# Com filtro por responsável:
GET /api/reservas?responsavel=João Silva
```

#### Cancelar Reserva
```http
DELETE /api/reservas/{id}
Authorization: Bearer {token}
```

---

## 🧪 Exemplos de Requisições

### Postman Collection

Importe a collection abaixo no Postman:

```json
{
  "info": {
    "name": "Meeting Room API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Auth",
      "item": [
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"admin\",\n  \"password\": \"admin123\"\n}"
            },
            "url": {
              "raw": "http://localhost:8080/api/auth/login",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "auth", "login"]
            }
          }
        }
      ]
    },
    {
      "name": "Salas",
      "item": [
        {
          "name": "Criar Sala",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              },
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"nome\": \"Sala de Reunião A\",\n  \"capacidade\": 10,\n  \"localizacao\": \"1º Andar\"\n}"
            },
            "url": {
              "raw": "http://localhost:8080/api/salas",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "salas"]
            }
          }
        },
        {
          "name": "Listar Salas",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/salas",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "salas"]
            }
          }
        }
      ]
    },
    {
      "name": "Reservas",
      "item": [
        {
          "name": "Criar Reserva",
          "request": {
            "method": "POST",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              },
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"salaId\": 1,\n  \"dataHoraInicio\": \"2024-12-20T14:00:00\",\n  \"dataHoraFim\": \"2024-12-20T16:00:00\",\n  \"responsavel\": \"João Silva\"\n}"
            },
            "url": {
              "raw": "http://localhost:8080/api/reservas",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "reservas"]
            }
          }
        },
        {
          "name": "Listar Reservas",
          "request": {
            "method": "GET",
            "header": [
              {
                "key": "Authorization",
                "value": "Bearer {{token}}"
              }
            ],
            "url": {
              "raw": "http://localhost:8080/api/reservas?page=0&size=10",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8080",
              "path": ["api", "reservas"],
              "query": [
                {
                  "key": "page",
                  "value": "0"
                },
                {
                  "key": "size",
                  "value": "10"
                }
              ]
            }
          }
        }
      ]
    }
  ]
}
```

### cURL Examples

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

**Criar Sala:**
```bash
curl -X POST http://localhost:8080/api/salas \
  -H "Authorization: Bearer {seu-token}" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Sala de Reunião A",
    "capacidade": 10,
    "localizacao": "1º Andar"
  }'
```

**Criar Reserva:**
```bash
curl -X POST http://localhost:8080/api/reservas \
  -H "Authorization: Bearer {seu-token}" \
  -H "Content-Type: application/json" \
  -d '{
    "salaId": 1,
    "dataHoraInicio": "2024-12-20T14:00:00",
    "dataHoraFim": "2024-12-20T16:00:00",
    "responsavel": "João Silva"
  }'
```

## 🧪 Testes

Execute os testes unitários:

```bash
mvn test
```

Execute testes específicos:
```bash
mvn test -Dtest=ReservaServiceTest
mvn test -Dtest=SalaServiceTest
```

**Cobertura de testes:**
- ✅ Testes de regras de negócio (conflito de reservas)
- ✅ Testes de validação
- ✅ Testes de CRUD
- ✅ Testes de filtros e buscas

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/example/cp2/
│   │   ├── config/          # Configurações (Security, Cache, Swagger)
│   │   ├── controller/      # Controllers REST
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entities/        # Entidades JPA
│   │   ├── exception/       # Tratamento de exceções
│   │   ├── repository/      # Repositórios JPA
│   │   ├── security/        # JWT Filter e Util
│   │   └── service/         # Lógica de negócio
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/cp2/
        └── service/         # Testes unitários
```

## 🔒 Regras de Negócio

1. **Reservas:**
   - Data/hora de fim deve ser posterior à data/hora de início
   - Não pode haver conflito de horários para a mesma sala
   - Apenas reservas ativas podem ser canceladas
   - Data/hora de início deve ser futura

2. **Salas:**
   - Capacidade mínima: 1 pessoa
   - Todos os campos são obrigatórios

3. **Autenticação:**
   - Token JWT válido por 1 hora
   - Todos os endpoints (exceto login) requerem autenticação

## 🐛 Tratamento de Erros

A API retorna respostas padronizadas para erros:

```json
{
  "timestamp": "2024-12-20T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Já existe uma reserva ativa para essa sala nesse horário",
  "path": "/api/reservas"
}
```

**Códigos de status:**
- `200` - Sucesso
- `201` - Criado com sucesso
- `204` - Sem conteúdo (deleção bem-sucedida)
- `400` - Requisição inválida
- `401` - Não autenticado
- `403` - Acesso negado
- `404` - Recurso não encontrado
- `500` - Erro interno do servidor

## 📝 Licença

Este projeto foi desenvolvido para fins educacionais.

## 👨‍💻 Autor

Pedro Oliveira - [GitHub](https://github.com/PeOliveira18)

---

⭐ Se este projeto foi útil, considere dar uma estrela no repositório!