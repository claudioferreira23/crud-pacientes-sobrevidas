# API de Gerenciamento de Pacientes

> Projeto desenvolvido em Novembro de 2025. Criado como um estudo aprofundado sobre a criação de APIs REST com o ecossistema Spring, focado em boas práticas de arquitetura, testes e documentação.

API REST completa para gerenciamento de pacientes (CRUD). O projeto implementa uma arquitetura em camadas robusta, separando responsabilidades com DTOs (Data Transfer Objects), Mappers (MapStruct), validação de entrada (`@Valid`) e um tratamento de exceções global (`@RestControllerAdvice`) para respostas de erro padronizadas.

---
## ✨ Features

- [x] **CRUD Completo:** Endpoints para `POST`, `GET`, `GET/{id}`, `PUT`, `PATCH` e `DELETE`.
- [x] **Importação Automática de CSV:** Na primeira inicialização, o banco de dados é populado automaticamente com os dados de `pacientes.csv` (via `CommandLineRunner`).
- [x] **Padrão DTO:** Separação clara entre dados de entrada (`PacienteRequestDTO`), saída (`PacienteResponseDTO`) e atualização parcial (`PacientePatchDTO`).
- [x] **Mapeamento Automatizado:** Uso do **MapStruct** para converter DTOs e Entidades de forma limpa.
- [x] **Validação Robusta:** Validação de dados de entrada em todos os DTOs (`@Valid`, `@NotBlank`, `@Pattern`, etc.).
- [x] **Tratamento de Exceções Centralizado:** Uso de `@RestControllerAdvice` para retornar respostas de erro padronizadas.
- [x] **Documentação Interativa:** API 100% documentada com **Swagger (SpringDoc)**, incluindo schemas de erro.
- [x] **Endpoints Protegidos:** Segurança em todos os endpoints com autenticação e autorização via Keycloak.
- [x] **Testes de Unidade e Integração:** Cobertura de testes para a camada de Serviço (`PacienteServiceTest`) e para a camada de API/Controller (`PacienteControllerTest`) usando JUnit 5 e Mockito.
- [x] **Ambiente Containerizado:** Banco de dados PostgreSQL 16 gerenciado via Docker Compose.

---
## 🚀 Como Executar o Projeto

Certifique-se de ter o **Java 21** (JDK), **Maven** e o **Docker** instalados e rodando na sua máquina.

```bash
# 1. Clone este repositório
git clone https://github.com/claudioferreira23/crud-pacientes-sobrevidas.git

# 2. Navegue até a pasta do projeto
cd crud-pacientes-sobrevidas

# 3. Inicie o contêiner do banco de dados (PostgreSQL)
docker-compose up -d

# 4. Execute a aplicação Spring Boot
# (Pode levar um momento na primeira vez para baixar as dependências)
./mvnw spring-boot:run

OBS: O arquivo 'pacientes.csv' deve estar dentro da pasta 'resources'.
```

- A API estará disponível em `http://localhost:8080`.
- A documentação do Swagger estará em `http://localhost:8080/swagger-ui.html`.
- O console de administração do Keycloak estará em `http://localhost:8081`.

---
## 🔌 Endpoints da API

Os endpoints são protegidos e exigem um token de acesso JWT válido obtido via Keycloak.

| Verbo HTTP | Endpoint | Descrição | Acesso |
| :--- | :--- | :--- |:----------|
| `GET` | `/pacientes` | Lista todos os pacientes cadastrados. | Protegido |
| `GET` | `/pacientes/{id}` | Busca um paciente pelo seu ID. | Protegido |
| `POST` | `/pacientes` | Cadastra um novo paciente. | Protegido |
| `PUT` | `/pacientes/{id}` | Atualiza um paciente (requer o objeto completo). | Protegido |
| `PATCH` | `/pacientes/{id}` | Atualiza parcialmente um paciente (apenas campos fornecidos). | Protegido |
| `DELETE` | `/pacientes/{id}` | Remove um paciente. | Protegido |

---
## 👨‍💻 Autor

**[Claudio Ferreira]**

- Github: [claudioferreira23] (https://github.com/claudioferreira23)
- LinkedIn: [claudio-eliziario-silva-ferreira] (www.linkedin.com/in/claudio-eliziario-silva-ferreira)

---
## 🛠️ Tecnologias Utilizadas

<div align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=spring" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Maven-red?style=for-the-badge&logo=apachemaven" alt="Maven">
  <img src="https://img.shields.io/badge/Docker-blue?style=for-the-badge&logo=docker" alt="Docker">
  <img src="https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql" alt="PostgreSQL 16">
  <img src="https://img.shields.io/badge/JUnit5-green?style=for-the-badge&logo=junit5" alt="JUnit 5">
  <img src="https://img.shields.io/badge/Keycloak-red?style=for-the-badge&logo=keycloak" alt="Keycloak">
  <img src="https://img.shields.io/badge/Swagger-blue?style=for-the-badge&logo=swagger" alt="Swagger">
</div>
