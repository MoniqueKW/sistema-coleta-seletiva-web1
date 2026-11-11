# Sistema de Coleta Seletiva (WEB1)

Este repositório contém o **Sistema de Coleta Seletiva**, desenvolvido como parte da disciplina de WEB1 no IFS. O projeto tem como objetivo gerenciar solicitações de coleta, pontos de descarte, moradores e operadores, fornecendo uma base sólida e escalável para aplicações de gestão de resíduos.

Atualmente, o repositório contempla a API (Backend), já estruturado para receber a interface de usuário (Frontend) nas próximas etapas.

## 📂 Estrutura do Projeto

Para facilitar a integração e o desenvolvimento, o repositório está sendo organizado na seguinte estrutura:

```text
/
├── backend/    # API desenvolvida em Spring Boot (Disponível)
├── frontend/   # (Futuro) Diretório onde o código da interface será anexado
└── docs/       # Documentações, Diagramas de Entidade-Relacionamento (DER), etc.
```

## ⚙️ Backend (API)

O backend foi construído sobre uma arquitetura em camadas bem definida (`Controller`, `Service`, `Repository`, `Entity`, `DTO`) garantindo separação de responsabilidades.

### Destaques Técnicos:

* **Padrão DTO:** Separação rigorosa entre entidades de banco de dados e dados expostos/recebidos via API.
* **Tratamento Global de Exceções:** Implementação de um `GlobalExceptionHandler` interceptando erros e retornando objetos padronizados.
* **Consultas Dinâmicas (JPA Specifications):** Uso de *Specifications* para filtragem avançada de dados na base.
* **Controle de Versão de Banco (Flyway):** O histórico do banco de dados PostgreSQL é versionado usando Flyway (da versão `V1` à `V15`).

### Tecnologias:

* **Java** e **Spring Boot**
* **PostgreSQL**
* **Flyway**
* **Swagger/OpenAPI** (Documentação)
* **Maven**

### Como Executar o Backend

1. Navegue até a pasta do backend: `cd backend`
2. Configure as variáveis de ambiente do banco de dados (URL, usuário e senha do PostgreSQL).
3. Execute o projeto usando o Maven: `./mvnw spring-boot:run`
4. Acesse o Swagger UI: `http://localhost:8080/swagger-ui.html`

## 💻 Frontend

*O código da interface web será adicionado futuramente no diretório `/frontend`.*

## 🚧 Futuras Atualizações

*Novas seções e instruções serão adicionadas a este README conforme o projeto evoluir nas próximas etapas da disciplina.*

## 👥 Autores

Projeto desenvolvido por alunos do *Instituto Federal de Sergipe (IFS)*:

* **Monique Andrade** - [@MoniqueKW](https://github.com/MoniqueKW)
* **Bruno Victor** - [@BrunoRVictor](https://github.com/BrunoRVictor)
