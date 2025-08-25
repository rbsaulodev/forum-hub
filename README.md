# FórumHub API

### 📖 Índice

- [🚀 Sobre o Projeto](#-sobre-o-projeto)
- [✨ Funcionalidades](#-funcionalidades)
- [🛠️ Tecnologias Utilizadas](#️-tecnologias-utilizadas)
- [📄 Documentação e Endpoints](#-documentação-e-endpoints)
- [🔧 Como Executar o Projeto](#-como-executar-o-projeto)
- [👨‍💻 Autor](#-autor)
- [📜 Licença](#-licença)

-----

## 🚀 Sobre o Projeto

Bem-vindo à API do **FórumHub**\!

Este projeto é o desafio final do programa **Oracle Next Education (ONE)** em parceria com a **Alura**, focado na trilha de especialização em **Spring Framework**. O objetivo foi construir uma API REST completa para uma plataforma de fórum, aplicando os principais conceitos do ecossistema Spring para criar uma aplicação robusta, segura e escalável.

A API permite o gerenciamento de cursos, tópicos, respostas, usuários e permissões, simulando um ambiente real de discussões e aprendizado.

-----

## ✨ Funcionalidades

  - **🔐 Segurança Robusta:** Sistema de autenticação e autorização stateless com **JWT (JSON Web Tokens)** e **Spring Security**.
  - **👤 Gerenciamento de Usuários Flexível:**
      - Separação clara de permissões com perfis (roles): `ADMIN`, `STUDENT`, `TEACHER`.
      - Administradores possuem controle total sobre os usuários.
      - Usuários comuns podem visualizar, atualizar e deletar seus próprios perfis, mas não o de outros.
  - **📚 Gerenciamento de Cursos:**
      - CRUD (Criar, Ler, Atualizar, Deletar) completo de cursos, com operações de escrita (`POST`, `PUT`, `DELETE`) restritas a administradores.
      - Endpoints para listar professores, alunos e tópicos de um curso específico.
  - **💬 Gerenciamento de Tópicos e Respostas:**
      - Usuários autenticados podem criar tópicos e respostas.
      - Autorização granular: apenas o **autor original** do conteúdo ou um **administrador** podem alterar ou deletar um tópico/resposta.
  - **🎓 Sistema de Matrícula:** Usuários podem se matricular (`enroll`) e cancelar a matrícula (`unenroll`) em cursos, com regras de negócio que impedem matrículas duplicadas ou de professores em seus próprios cursos.
  - **📄 Documentação Interativa:** API documentada com **Swagger (OpenAPI 3)** para facilitar os testes e a compreensão dos endpoints.

-----

## 🛠️ Tecnologias Utilizadas

  - **Java 17**
  - **Spring Boot 3**
  - **Spring Security** (Autenticação e Autorização com JWT)
  - **Spring Data JPA / Hibernate** (Persistência de dados)
  - **PostgreSQL** (Banco de dados relacional)
  - **Flyway** (Ferramenta de migração de banco de dados)
  - **Maven** (Gerenciamento de dependências)
  - **Lombok** (Redução de código boilerplate)
  - **SpringDoc (OpenAPI)** (Documentação da API)

-----

## 📄 Documentação e Endpoints

A documentação completa e interativa da API está disponível via Swagger. Após iniciar a aplicação, acesse:

👉 **[http://localhost:9090/swagger-ui.html](https://www.google.com/search?q=http://localhost:9090/swagger-ui.html)**

**Resumo dos Principais Recursos:**
| Recurso | Endpoints | Proteção Principal |
|---|---|---|
| **Autenticação** | `POST /login`, `POST /users/register` | Público |
| **Cursos (CRUD)** | `POST`, `PUT`, `DELETE` em `/courses/**` | `ADMIN` |
| **Cursos (Leitura)** | `GET /courses/**` | Autenticado |
| **Tópicos/Respostas** | `POST`, `PUT`, `DELETE` | Autenticado / Dono do Recurso |
| **Usuários (Perfil)** | `GET`, `PUT`, `DELETE` em `/users/{id}` | `ADMIN` ou Dono do Perfil |
| **Matrículas** | `POST /students/courses/{id}/enroll` | Autenticado |

-----

## 🔧 Como Executar o Projeto

Siga os passos abaixo para executar a API localmente.

**Pré-requisitos:**

  - Java 17 ou superior instalado
  - Maven 3.8 ou superior instalado
  - Git instalado
  - PostgreSQL instalado e em execução

**Passos:**

1.  **Crie o Banco de Dados:**

      - Abra um terminal `psql` ou sua ferramenta de SGBD preferida e crie o banco de dados:
        ```sql
        CREATE DATABASE forumhub_db;
        ```

2.  **Clone o repositório:**

    ```bash
    git clone https://github.com/seu-usuario/forum-hub.git
    cd forum-hub
    ```

3.  **Verifique a Configuração:**

      - O arquivo `src/main/resources/application.properties` já está pré-configurado para um ambiente de desenvolvimento local padrão com PostgreSQL.
      - **Usuário do banco:** `postgres`
      - **Senha do banco:** `1234`
      - **Porta da aplicação:** `9090`
      - *Se suas credenciais do PostgreSQL forem diferentes, ajuste-as neste arquivo.*

4.  **Execute a aplicação:**

      - Pelo terminal, na raiz do projeto, execute o comando Maven:
        ```bash
        mvn spring-boot:run
        ```
      - O Flyway irá criar as tabelas e as roles (`ROLE_ADMIN`, etc.) automaticamente na primeira execução.

5.  **Crie um Usuário Administrador (Opcional, mas recomendado):**

      - Para testar os endpoints protegidos, você precisa de um usuário com a role `ADMIN`. Após a aplicação rodar e criar as tabelas, execute o seguinte comando SQL no seu banco de dados `forumhub_db`:
        ```sql
        -- Inserir o usuário Admin. A senha é 'admin123'
        INSERT INTO users (name, email, password)
        VALUES ('Administrador', 'admin@email.com', '$2a$10$e.ExV8s.wN0g23hQc39vA.W24xSlA0/JjXTOs4StCEht5Ni4235jK');

        -- Associar o usuário Admin com a ROLE_ADMIN
        INSERT INTO user_roles (user_id, role_id)
        SELECT
            (SELECT id FROM users WHERE email = 'admin@email.com'),
            (SELECT id FROM roles WHERE name = 'ROLE_ADMIN');
        ```
      - Agora você pode se autenticar via `POST /login` com o email `admin@email.com` e a senha `admin123` para obter um token de administrador.

6.  **Acesse a API:**

      - A aplicação estará disponível em `http://localhost:9090`.
      - A documentação Swagger estará em `http://localhost:9090/swagger-ui.html`.

-----

## 👨‍💻 Autor

Desenvolvido por **Saulo Rodrigues Brilhante**.

- [LinkedIn](https://www.linkedin.com/in/saulo-brilhante/)

-----

## 📜 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.
