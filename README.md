# ♻️ Sistema de Coleta Seletiva

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI-Swagger_UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

API REST em Spring Boot que conecta **moradores**, **pontos de coleta** e **operadores** para organizar o recolhimento de materiais recicláveis: cadastros, consulta de pontos e materiais aceitos, solicitação de recolhimento com vários itens, agendamento, execução, cancelamento, histórico e resumo gerencial.

🎓 Trabalho da disciplina **Programação Web I** (Sistemas de Informação, Instituto Federal de Sergipe).


## 👥 Autores

Projeto desenvolvido por alunos do *Instituto Federal de Sergipe (IFS)*:

* **Monique Andrade** - [@MoniqueKW](https://github.com/MoniqueKW)
* **Bruno Victor** - [@BrunoRVictor](https://github.com/BrunoRVictor)

## 🛠️ Tecnologias

- ☕ Java 21
- 🍃 Spring Boot 4.1.1
- 🐘 PostgreSQL
- 🪽 Flyway (migrações do banco)
- 📘 Swagger/OpenAPI
- 📦 Maven 

## 📋 Requisitos

- ☕ JDK 21
- 🐘 PostgreSQL
- 📦 Maven (o projeto inclui o Maven Wrapper, dispensando instalação prévia)

## 🚀 Como executar

Todos os comandos abaixo partem da pasta `backend/`, então é preciso estar nela para as coisas aqui abaixo rodarem sem erro.

### 1️⃣ 🗄️ Criar o banco de dados

O banco precisa existir; o **schema, as tabelas e os dados de demonstração são criados sozinhos pelo Flyway** na primeira execução. Então, primeiro:

```sql
CREATE DATABASE coleta_seletiva;
```

(Pelo terminal: `psql -U postgres -c "CREATE DATABASE coleta_seletiva;"`.)

### 2️⃣ 🔐 Configurar as variáveis de ambiente

As credenciais **não ficam no repositório**; são lidas do ambiente:

| Variável  | Obrigatória | Padrão     | Descrição                    |
|-----------|-------------|------------|------------------------------|
| `SENHA`   | sim         | —          | Senha do usuário do PostgreSQL |
| `USUARIO` | não         | `postgres` | Usuário do PostgreSQL        |

Desta forma, **dentro da pasta `backend/`**, execute:

🐧 Linux / macOS:
```bash
export SENHA=sua_senha
```

🪟 Windows (PowerShell):
```powershell
$env:SENHA="sua_senha"
```

Se for rodar pela IDE, defina as mesmas variáveis na configuração de execução da aplicação em `src/main/resources/application.properties` nos campos de usuário (que aceita padrão) e senha.


> O banco é acessado em `jdbc:postgresql://localhost:5432/coleta_seletiva`. Para usar outro endereço, altere `spring.datasource.url` em `src/main/resources/application.properties`.

### 3️⃣ ▶️ Subir a aplicação

Com o terminal **dentro da pasta `backend/`** (onde ficam o `pom.xml` e o `mvnw.cmd`), execute:

🐧 Linux / macOS:
```bash
./mvnw spring-boot:run
```

🪟 Windows:
```powershell
.\mvnw.cmd spring-boot:run
```
Ou, se preferir, apenas acione o run na IDE.

🌐 A API sobe em **http://localhost:8080**.

### 4️⃣ 📖 Acessar a documentação

| O quê                         | Endereço                                   |
|-------------------------------|--------------------------------------------|
| 🧭 Swagger UI (testar)     | http://localhost:8080/swagger-ui/index.html |
| 📄 Especificação OpenAPI (JSON)  | http://localhost:8080/v3/api-docs          |

Uma cópia da especificação fica versionada em [`docs/api/api-docsjson`]

---

## 🎲 Dados de demonstração

A migração `V14` carrega dados **fictícios** para a demonstração:

- 🧴 Materiais: Papel, Plástico, Vidro e Metal.
- 📍 Pontos de coleta: *Cooperativa Recicla Aracaju* (aceita Papel e Plástico, com recolhimento domiciliar) e *Ecoponto Bairro Industrial* (aceita Papel, Vidro e Metal, sem recolhimento domiciliar).
- 🏠 Moradores: Maria Silva e João Santos, cada um com um endereço.
- 🧑‍🔧 Operadores: Carlos Oliveira (Cooperativa) e Ana Souza (Ecoponto).
- 📝 Três solicitações em situações diferentes: `SC-DEMO0001` (SOLICITADA), `SC-DEMO0002` (AGENDADA) e `SC-DEMO0003` (CONCLUIDA, com quantidades efetivas e histórico completo).

🧪 Roteiro rápido pelo Swagger:

1. `GET /pontos` e `GET /pontos?idMaterial=<id do vidro>`: pontos ativos que aceitam o material.
2. `GET /solicitacoes/protocolo/SC-DEMO0003`: solicitação concluída, com itens e histórico.
3. `POST /solicitacoes`: criar uma solicitação com dois materiais aceitos pelo ponto. Tentar incluir um material que o ponto não aceita deve retornar `409` sem gravar nada.
4. Como operador, agendar, iniciar e concluir a solicitação criada; conferir o histórico em `GET /historicos`.
5. `GET /resumo/solicitacoes-por-situacao` e `GET /resumo/quantidades-recolhidas`.

---

## 🔌 Principais endpoints

| Recurso                 | Rota base                          |
|-------------------------|------------------------------------|
| 👤 Moradores               | `/moradores`                       |
| 🏠 Endereços               | `/enderecos`                       |
| 🧴 Materiais               | `/materiais`                       |
| ♻️ Pontos de coleta        | `/pontos`                          |
| 🔗 Materiais aceitos       | `/pontos/{idPonto}/materiais`      |
| 🧑‍🔧 Operadores              | `/pontos/{idPonto}/operadores`     |
| 📝 Solicitações            | `/solicitacoes`                    |
| 🕒 Histórico               | `/historicos`                      |
| 📊 Resumo gerencial        | `/resumo`                          |

Todos os detalhes (parâmetros, exemplos e respostas) estão no Swagger UI.

### 🚦 Padrão de respostas

| Situação                              | Código |
|---------------------------------------|--------|
| ✅ Criação                               | `201`  |
| ⚠️ Entrada inválida (validação)          | `400`  |
| 🔎 Registro não encontrado               | `404`  |
| ⛔ Conflito de regra de negócio ou de versão | `409` |

Os erros seguem um formato único, com `status`, `code`, `message` e `fieldErrors` (erros por campo), sem expor stack trace. As listagens são paginadas, com **no máximo 100 registros por página**.

---

## 📏 Regras de negócio implementadas

- 🧩 **Compatibilidade (RN01):** a solicitação exige ponto ativo, recolhimento domiciliar habilitado e materiais ativos e aceitos pelo ponto; respeita o limite por solicitação e não aceita o mesmo material duas vezes.
- ⚖️ **Quantidades e unidades (RN02):** a unidade pertence ao material; quantidade estimada positiva e efetiva não negativa.
- 🗃️ **Integridade e histórico (RN03):** não há exclusão de cadastros com solicitações ou histórico vinculados; cada mudança de situação grava situação anterior, nova, data, responsável e motivo.
- 📸 **Preservação dos dados (RN04):** a solicitação guarda cópia do endereço e da unidade dos itens; itens e endereço só podem ser corrigidos antes do agendamento, com nova validação.
- 🚧 **Alterações no ponto (RN05):** inativar um ponto ou remover um vínculo de material é bloqueado enquanto houver solicitações abertas afetadas.
- 🔄 **Atomicidade e concorrência (RN06):** criação com itens e mudanças de situação com histórico ocorrem em uma única transação; atualização com versão desatualizada é rejeitada com `409`.

🔀 Transições de situação permitidas: `SOLICITADA` → `AGENDADA`, `RECUSADA` ou `CANCELADA`; `AGENDADA` → `EM_RECOLHIMENTO` ou `CANCELADA` (reagendamento mantém `AGENDADA`); `EM_RECOLHIMENTO` → `CONCLUIDA` ou `AGENDADA` (tentativa sem sucesso). `CONCLUIDA`, `RECUSADA` e `CANCELADA` são finais.

---

## 🏗️ Estrutura do repositório

```
backend/                      Código da API (Spring Boot)
  src/main/java/.../          controller, service, repository, specification, model, dto, exception, config
  src/main/resources/
    application.properties
    db/migration/             Migrações Flyway (V1 a V16): estrutura, ajustes, dados de demonstração e índices
docs/
  api/                        Especificação OpenAPI (JSON)
  dbCarga/                    Scripts de carga de teste (ver abaixo)
  dbDadosDemonstrativos/      Script de dados demonstrativos (já incluso no versionamento do Flyway)
  dbSchema/                   Script SQL do banco
  diagramaER/                 Diagrama entidade-relacionamento (PNG e PDF)
  relatorio/                  Relatório atual do projeto (PDF)
```

## 🗃️ Banco de dados

O modelo está no diagrama em [`docs/diagramaDer/`](docs/diagramaDer/) e o script completo em [`docs/bdSchema/scriptColetaSeletiva.sql`](docs/bdSchema/scriptColetaSeletiva.sql). O Flyway aplica as migrações automaticamente, e o Hibernate apenas **valida** o schema (`ddl-auto=validate`). A migração `V15` cria os índices coerentes com os filtros da API (solicitações por morador, ponto, situação e período; materiais aceitos; histórico; pontos por município e bairro, entre outros), assim como requisitado no PDF do projeto.

## ⚡ Carga de teste (desempenho)

Em `docs/carga/` há dois scripts, **fora do Flyway**, para medir o desempenho com volume:

- 📥 `carga_1000_solicitacoes.sql`: cria 1.000 solicitações de teste (com itens e histórico).
- 🧹 `limpar_carga.sql`: remove somente o que a carga criou, preservando os dados de demonstração.

```bash
psql -U postgres -d coleta_seletiva -f docs/carga/carga_1000_solicitacoes.sql
psql -U postgres -d coleta_seletiva -f docs/carga/limpar_carga.sql
```

## 🔒 Segurança e credenciais

Nenhuma senha ou credencial é versionada: a conexão com o banco é configurada por variáveis de ambiente (`USUARIO` e `SENHA`). Todos os dados do repositório são fictícios. Esta versão não implementa autenticação; o operador é informado em cada requisição, como previsto para o protótipo básico do trabalho.

## 🚧 Futuras Atualizações

*Novas seções e instruções serão adicionadas a este README conforme o projeto evoluir nas próximas etapas da disciplina.*
