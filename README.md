# Esteira de Propostas

Sistema web para gerenciamento e tramitação de propostas comerciais, desenvolvido em Java com JSP e Servlet, seguindo os padrões de projeto estudados na disciplina de Padrões de Projeto.

---

## Sumário

- [Visão Geral](#visão-geral)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Padrões de Projeto](#padrões-de-projeto)
- [Arquitetura](#arquitetura)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Modelo de Dados](#modelo-de-dados)
- [Funcionalidades](#funcionalidades)
- [Endpoints](#endpoints)
- [Configuração e Execução](#configuração-e-execução)
- [Critérios de Avaliação](#critérios-de-avaliação)

---

## Visão Geral

O sistema Esteira de Propostas permite o cadastro e a tramitação de propostas comerciais por meio de um fluxo controlado de etapas. Cada proposta percorre os seguintes estados:

```
RASCUNHO --> ANALISE --> APROVADA
                  \
                   --> REPROVADA --> RASCUNHO
```

O controle de transições entre estados é realizado de forma automática e segura, garantindo que apenas as ações permitidas sejam executadas em cada etapa do fluxo.

---

## Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 21 | Linguagem principal |
| JSP | 3.0 | Camada de visualização |
| Servlet | 3.1 | Camada de controle |
| Apache Tomcat | 8.5 | Servidor de aplicação |
| MySQL | 9.x | Banco de dados relacional |
| MySQL Connector/J | 9.6 | Driver JDBC |
| Bootstrap | 5.3 | Estilização da interface |
| Apache Ant | - | Build do projeto |
| NetBeans IDE | - | Ambiente de desenvolvimento |

---

## Padrões de Projeto

O sistema foi desenvolvido aplicando os seguintes Design Patterns:

### MVC + DAO

A aplicação segue a arquitetura Model-View-Controller combinada com o padrão Data Access Object. Os Servlets atuam como Controllers, os arquivos JSP como Views e as classes de modelo representam os dados do domínio. Os DAOs encapsulam toda a lógica de acesso ao banco de dados.

### Builder

A construção de objetos complexos como `Proposta`, `Cliente` e `Documento` é realizada exclusivamente por meio de Builders internos. Esse padrão garante que objetos sejam criados em um estado válido e consistente, impedindo instâncias incompletas.

```java
Proposta proposta = new Proposta.Builder()
    .titulo("Proposta de Desenvolvimento")
    .valor(25000.00)
    .cliente(cliente)
    .build();
```

### Command + Factory Method

Cada ação da esteira é encapsulada em um objeto Command. A `FabricaComandoEsteira` decide qual Command instanciar com base na ação solicitada, desacoplando o Controller da lógica de negócio.

```java
ComandoEsteira comando = FabricaComandoEsteira.criar("APROVAR");
comando.executar(proposta);
```

### State Pattern

O comportamento da `Proposta` varia de acordo com seu estado atual. Cada estado concreto implementa a interface `EstadoProposta` e define quais transições são permitidas, eliminando condicionais distribuídas pelo código.

```java
proposta.avancar();   // RASCUNHO -> ANALISE
proposta.avancar();   // ANALISE  -> APROVADA
proposta.reprovar();  // ANALISE  -> REPROVADA
proposta.reabrir();   // REPROVADA -> RASCUNHO
```

### Polimorfismo com Classe Abstrata

A classe `AbstractDAO<T>` define o contrato CRUD que todos os DAOs concretos devem implementar, promovendo reuso de código e padronização da camada de persistência.

### Polimorfismo com Interface

A interface `EstadoProposta` e a interface `ComandoEsteira` permitem que múltiplas implementações sejam tratadas de forma uniforme, aplicando polimorfismo nas camadas de negócio e esteira.

---

## Arquitetura

```
Controller (Servlet)
      |
      v
Factory Method
      |
      v
Command  <-->  State Pattern
      |
      v
     DAO
      |
      v
   MySQL
```

O fluxo de uma requisição segue o seguinte caminho:

1. O usuário aciona uma ação na tela JSP
2. O Servlet recebe a requisição e delega para a `FabricaComandoEsteira`
3. A fábrica instancia o Command correto
4. O Command chama o método correspondente na `Proposta`
5. O State Pattern valida a transição e atualiza o estado
6. O DAO persiste as alterações no banco de dados
7. O Servlet redireciona para a view atualizada

---

## Estrutura do Projeto

```
PROJECT_UMC/
├── src/
│   └── java/
│       ├── command/
│       │   ├── ComandoEsteira.java
│       │   ├── ComandoEnviarParaAnalise.java
│       │   ├── ComandoAprovarProposta.java
│       │   ├── ComandoReprovarProposta.java
│       │   └── ComandoReabrirProposta.java
│       ├── controller/
│       │   ├── PropostaServlet.java
│       │   ├── ClienteServlet.java
│       │   ├── DocumentoServlet.java
│       │   └── EsteiraServlet.java
│       ├── dao/
│       │   ├── AbstractDAO.java
│       │   ├── ClienteDAO.java
│       │   ├── DocumentoDAO.java
│       │   └── PropostaDAO.java
│       ├── factory/
│       │   └── FabricaComandoEsteira.java
│       ├── model/
│       │   ├── Proposta.java
│       │   ├── Cliente.java
│       │   └── Documento.java
│       ├── state/
│       │   ├── EstadoProposta.java
│       │   ├── EstadoRascunho.java
│       │   ├── EstadoAnalise.java
│       │   ├── EstadoAprovada.java
│       │   ├── EstadoReprovada.java
│       │   └── EstadoFactory.java
│       └── util/
│           └── ConexaoDB.java
│
└── web/
    ├── index.jsp
    └── WEB-INF/
        ├── web.xml
        ├── context.xml
        ├── lib/
        │   └── mysql-connector-j-9.6.0.jar
        └── views/
            ├── fragments/
            │   ├── header.jsp
            │   └── footer.jsp
            ├── erro.jsp
            ├── proposta/
            │   ├── lista.jsp
            │   ├── form.jsp
            │   └── detalhe.jsp
            └── cliente/
                ├── lista.jsp
                └── form.jsp
```

---

## Modelo de Dados

### Relacionamentos

- `Proposta` possui relacionamento **1:1** com `Cliente`
- `Proposta` possui relacionamento **1:N** com `Documento`

### Diagrama de Tabelas

```
cliente
-------
id          INT PK AUTO_INCREMENT
nome        VARCHAR(150) NOT NULL
cpf_cnpj    VARCHAR(20)  NOT NULL UNIQUE
email       VARCHAR(100)
telefone    VARCHAR(20)
endereco    VARCHAR(200)
cidade      VARCHAR(100)
estado      CHAR(2)
cep         VARCHAR(10)
tipo        VARCHAR(20)
ativo       TINYINT(1)

proposta
--------
id                INT PK AUTO_INCREMENT
codigo            VARCHAR(20) NOT NULL UNIQUE
titulo            VARCHAR(200) NOT NULL
descricao         TEXT
valor             DECIMAL(15,2) NOT NULL
status            VARCHAR(20) NOT NULL
etapa_atual       VARCHAR(50) NOT NULL
data_criacao      DATETIME NOT NULL
data_atualizacao  DATETIME NOT NULL
observacoes       TEXT
cliente_id        INT FK -> cliente(id)

documento
---------
id           INT PK AUTO_INCREMENT
nome         VARCHAR(200) NOT NULL
tipo         VARCHAR(50)
caminho      VARCHAR(300)
descricao    TEXT
data_upload  DATETIME NOT NULL
proposta_id  INT FK -> proposta(id)
```

### Script de criação do banco

```sql
CREATE DATABASE esteira_proposta;
USE esteira_proposta;

CREATE TABLE cliente (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(150) NOT NULL,
    cpf_cnpj   VARCHAR(20)  NOT NULL UNIQUE,
    email      VARCHAR(100),
    telefone   VARCHAR(20),
    endereco   VARCHAR(200),
    cidade     VARCHAR(100),
    estado     CHAR(2),
    cep        VARCHAR(10),
    tipo       VARCHAR(20)  NOT NULL DEFAULT 'PESSOA_FISICA',
    ativo      TINYINT(1)   NOT NULL DEFAULT 1
);

CREATE TABLE proposta (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    codigo            VARCHAR(20)   NOT NULL UNIQUE,
    titulo            VARCHAR(200)  NOT NULL,
    descricao         TEXT,
    valor             DECIMAL(15,2) NOT NULL,
    status            VARCHAR(20)   NOT NULL DEFAULT 'RASCUNHO',
    etapa_atual       VARCHAR(50)   NOT NULL DEFAULT 'CADASTRO',
    data_criacao      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    observacoes       TEXT,
    cliente_id        INT           NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE documento (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    nome         VARCHAR(200) NOT NULL,
    tipo         VARCHAR(50),
    caminho      VARCHAR(300),
    descricao    TEXT,
    data_upload  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    proposta_id  INT          NOT NULL,
    FOREIGN KEY (proposta_id) REFERENCES proposta(id)
);
```

---

## Funcionalidades

### Propostas

- Cadastrar nova proposta vinculada a um cliente
- Editar proposta (permitido apenas no estado RASCUNHO)
- Consultar proposta por ID com detalhes completos
- Listar todas as propostas
- Excluir proposta (remove documentos vinculados automaticamente)
- Tramitar proposta pela esteira de aprovacao

### Clientes

- Cadastrar novo cliente (Pessoa Fisica ou Juridica)
- Editar dados do cliente
- Listar todos os clientes
- Excluir cliente

### Documentos

- Vincular documento a uma proposta (relacionamento 1:N)
- Listar documentos de uma proposta
- Remover documento vinculado

### Esteira de Aprovacao

| Estado atual | Acao disponivel | Proximo estado |
|---|---|---|
| RASCUNHO | Enviar para analise | ANALISE |
| ANALISE | Aprovar | APROVADA |
| ANALISE | Reprovar | REPROVADA |
| REPROVADA | Reabrir | RASCUNHO |
| APROVADA | Nenhuma | Estado final |

---

## Endpoints

### Propostas

| Metodo | URL | Descricao |
|---|---|---|
| GET | `/proposta?acao=listar` | Lista todas as propostas |
| GET | `/proposta?acao=novo` | Exibe formulario de cadastro |
| GET | `/proposta?acao=editar&id={id}` | Exibe formulario de edicao |
| GET | `/proposta?acao=detalhe&id={id}` | Exibe detalhes e esteira |
| GET | `/proposta?acao=deletar&id={id}` | Remove a proposta |
| POST | `/proposta` (acao=inserir) | Cadastra nova proposta |
| POST | `/proposta` (acao=atualizar) | Atualiza proposta existente |

### Clientes

| Metodo | URL | Descricao |
|---|---|---|
| GET | `/cliente?acao=listar` | Lista todos os clientes |
| GET | `/cliente?acao=novo` | Exibe formulario de cadastro |
| GET | `/cliente?acao=editar&id={id}` | Exibe formulario de edicao |
| GET | `/cliente?acao=deletar&id={id}` | Remove o cliente |
| POST | `/cliente` (acao=inserir) | Cadastra novo cliente |
| POST | `/cliente` (acao=atualizar) | Atualiza cliente existente |

### Esteira

| Metodo | URL | Descricao |
|---|---|---|
| POST | `/esteira` | Executa acao na esteira |

Parametros do POST `/esteira`:

| Parametro | Tipo | Obrigatorio | Descricao |
|---|---|---|---|
| propostaId | int | Sim | ID da proposta |
| acao | String | Sim | ENVIAR_ANALISE, APROVAR, REPROVAR ou REABRIR |
| observacao | String | Nao | Comentario sobre a acao |

### Documentos

| Metodo | URL | Descricao |
|---|---|---|
| POST | `/documento` (acao=inserir) | Vincula documento a uma proposta |
| GET | `/documento?acao=deletar&id={id}&propostaId={id}` | Remove documento |

---

## Configuracao e Execucao

### Pre-requisitos

- Java JDK 21
- Apache Tomcat 8.5
- MySQL Server 9.x
- NetBeans IDE
- MySQL Connector/J 9.6

### Configuracao do banco de dados

Edite o arquivo `src/java/util/ConexaoDB.java` com as credenciais do seu ambiente:

```java
private static final String URL     = "jdbc:mysql://127.0.0.1:3306/esteira_proposta"
                                    + "?useSSL=false"
                                    + "&serverTimezone=America/Sao_Paulo"
                                    + "&allowPublicKeyRetrieval=true";
private static final String USUARIO = "root";
private static final String SENHA   = "sua_senha";
```

### Passos para execucao

1. Clone ou importe o projeto no NetBeans
2. Adicione o arquivo `mysql-connector-j-9.6.0.jar` em `WEB-INF/lib/`
3. Execute o script SQL de criacao do banco no MySQL Workbench
4. Configure o Tomcat 8.5 no NetBeans em Tools > Servers
5. Execute o projeto com `F6` ou Run > Run Project
6. Acesse pelo navegador em `http://localhost:8080/PROJECT_UMC`

---

## Criterios de Avaliacao

| Criterio | Implementacao |
|---|---|
| Utilizacao correta dos Design Patterns | MVC+DAO, Builder, Command, Factory Method, State, Polimorfismo |
| Implementacao das funcionalidades e requisitos | CRUD completo para Proposta, Cliente e Documento |
| Implementacao dos relacionamentos | 1:1 entre Proposta e Cliente / 1:N entre Proposta e Documento |
| Diagrama de Classes e de Sequencia | Disponivel na documentacao do projeto |
| Especificacao de Endpoints | Documentado na secao Endpoints deste arquivo |
| Boa usabilidade | Interface responsiva com Bootstrap 5 |
| Conformidade com SOLID e Coesao/Acoplamento | Cada classe possui responsabilidade unica e baixo acoplamento |
| Bonus — Toque pessoal | State Pattern aplicado como camada adicional de controle de fluxo |

---

## Autores

Projeto desenvolvido para a disciplina **Padroes de Projeto** — Turma 5B Engenharia de Software.