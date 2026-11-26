# Amigo Fiel API

API REST da clínica veterinária **Amigo Fiel**. Cadastra tutores, veterinários e atendimentos e grava tudo no PostgreSQL. Data, valor e categoria ficam em colunas próprias, prontos para serem analisados em um painel.

## Tecnologias

- Java 21
- Spring Boot 3.5.7
- Spring Web, Spring Data JPA e Bean Validation
- PostgreSQL
- Maven (wrapper incluído, não precisa instalar)

## Pré-requisitos

- Java 21 instalado (`java -version` deve mostrar 21)
- PostgreSQL 15 ou superior instalado e rodando, na porta `5432`
- Opcional: pgAdmin 4 (vem junto com o instalador do PostgreSQL)

## Como rodar

### 1. Criar o banco

Pelo **pgAdmin**: botão direito em *Databases* → *Create* → *Database*, nome `amigofiel`.

Ou pelo terminal:

```bash
psql -U postgres -c "CREATE DATABASE amigofiel;"
```

### 2. Configurar usuário e senha

Edite `src/main/resources/application.properties` e informe o usuário e a senha do seu PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/amigofiel
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
```

Se preferir não alterar o arquivo, use variáveis de ambiente (o Spring as reconhece automaticamente):

```bash
# Linux / macOS
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=sua_senha

# Windows (PowerShell)
$env:SPRING_DATASOURCE_USERNAME="postgres"
$env:SPRING_DATASOURCE_PASSWORD="sua_senha"
```

### 3. Subir a aplicação

Pelo terminal, na raiz do projeto:

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Ou pelo IntelliJ: abra `AmigoFielApiApplication` e clique no botão verde de play.

Quando aparecer `Started AmigoFielApiApplication`, a API está em **http://localhost:8080**.

Na primeira execução, o Hibernate cria as três tabelas (`tutores`, `veterinarios`, `atendimentos`) automaticamente. Os dados ficam gravados no PostgreSQL e continuam lá depois de reiniciar a aplicação.

### 4. Carregar os dados de exemplo

O script `dados.sql` **não cria tabelas**: ele só insere dados. Por isso, suba a aplicação pelo menos uma vez (passo 3) para as tabelas existirem, e depois execute o script no banco vazio.

Pelo terminal:

```bash
psql -U postgres -d amigofiel -f src/main/resources/dados.sql
```

Ou pelo **pgAdmin**: botão direito no banco `amigofiel` → *Query Tool* → abra o arquivo `dados.sql` → execute (F5).

Carga de exemplo: 8 tutores em pelo menos 3 cidades, 6 veterinários (as 4 especialidades, com pelo menos 1 inativo) e 30 atendimentos espalhados por mais de 60 dias, com os 3 status e os 4 tipos. Todos os nomes são fictícios.

O `dados.sql` começa com um `TRUNCATE` e **apaga os dados existentes** antes de inserir, então ele pode ser executado de novo sempre que quiser voltar à carga original. Ele não roda sozinho ao subir a aplicação, para que os dados continuem lá depois de um reinício.

Para refazer a carga do zero, apague e recrie o banco (`DROP DATABASE amigofiel;` e `CREATE DATABASE amigofiel;`), suba a aplicação de novo e rode o `dados.sql`.

## Estrutura do projeto

```
com.amigofiel.api
 ├─ AmigoFielApiApplication
 ├─ config                 configurações (WebConfig)
 ├─ controller             endpoints REST
 ├─ dto
 │   ├─ request            corpos de entrada
 │   ├─ response           corpos de saída
 │   └─ filter             parâmetros de busca das listagens
 ├─ entity                 entidades JPA
 ├─ enums                  Especialidade, TipoAtendimento, StatusAtendimento
 ├─ exception              erros de negócio e tratador global
 ├─ repository             acesso ao banco (Spring Data JPA)
 └─ service                regras de negócio
```

## Modelo de dados

O atendimento é o centro: aponta para um tutor e para um veterinário. Tutor e veterinário não apontam um para o outro. Quem está com `ativo = false` continua no banco, e atendimentos antigos não são alterados por isso.

**tutores**

| Coluna | Tipo | Regra |
| --- | --- | --- |
| `id` | inteiro | chave primária, gerada pelo banco |
| `nome` | texto | obrigatório, 3 a 120 caracteres |
| `email` | texto | obrigatório, formato de e-mail, único |
| `telefone` | texto | obrigatório, só dígitos, 10 ou 11 caracteres |
| `cidade` | texto | obrigatório, 2 a 80 caracteres |
| `data_cadastro` | data e hora | preenchida pela API |
| `ativo` | booleano | nasce `true` |

**veterinarios**

| Coluna | Tipo | Regra |
| --- | --- | --- |
| `id` | inteiro | chave primária, gerada pelo banco |
| `nome` | texto | obrigatório, 3 a 120 caracteres |
| `crmv` | texto | obrigatório, 4 a 20 caracteres, único |
| `especialidade` | texto (enum) | `CLINICA_GERAL`, `CIRURGIA`, `DERMATOLOGIA` ou `EXOTICOS` |
| `ativo` | booleano | nasce `true` |

**atendimentos**

| Coluna | Tipo | Regra |
| --- | --- | --- |
| `id` | inteiro | chave primária, gerada pelo banco |
| `tutor_id` | inteiro | chave estrangeira para `tutores` |
| `veterinario_id` | inteiro | chave estrangeira para `veterinarios` |
| `data_hora` | data e hora | obrigatória |
| `tipo` | texto (enum) | `CONSULTA`, `VACINA`, `EXAME` ou `RETORNO` |
| `valor` | decimal (2 casas) | obrigatório, maior que zero |
| `observacao` | texto | opcional, até 500 caracteres |
| `status` | texto (enum) | `AGENDADO`, `CONCLUIDO` ou `CANCELADO`; nasce `AGENDADO` |

## Formatos

- Datas e horas em ISO-8601, **sem fuso**: `2026-03-10T14:30:00`
- Valores em reais com ponto decimal: `150.00`
- JSON em camelCase (`dataHora`, `tutorId`); colunas do banco em snake_case (`data_hora`, `tutor_id`)
- `Content-Type: application/json`

## Endpoints

### Tutores

| Método | Caminho | O que faz |
| --- | --- | --- |
| `POST` | `/tutores` | Cadastra um tutor |
| `GET` | `/tutores` | Lista. Filtros: `nome` (contém, sem diferenciar maiúsculas) e `cidade` (igual) |
| `GET` | `/tutores/{id}` | Busca um tutor |
| `PUT` | `/tutores/{id}` | Atualiza nome, e-mail, telefone, cidade e ativo |

### Veterinários

| Método | Caminho | O que faz |
| --- | --- | --- |
| `POST` | `/veterinarios` | Cadastra um veterinário |
| `GET` | `/veterinarios` | Lista. Filtros: `nome` (contém) e `especialidade` (igual) |
| `GET` | `/veterinarios/{id}` | Busca um veterinário |
| `PUT` | `/veterinarios/{id}` | Atualiza nome, crmv, especialidade e ativo |

### Atendimentos

| Método | Caminho | O que faz |
| --- | --- | --- |
| `POST` | `/atendimentos` | Agenda um atendimento |
| `GET` | `/atendimentos` | Lista. Filtros: `status`, `tipo`, `tutorId`, `veterinarioId`, `dataInicio`, `dataFim` |
| `GET` | `/atendimentos/{id}` | Busca um atendimento |
| `PATCH` | `/atendimentos/{id}/status` | Muda o status |

`dataInicio` e `dataFim` são data e hora, e os dois limites entram no intervalo. Pode-se informar só um deles.

## Paginação

As listagens aceitam `page` (padrão `0`) e `size` (padrão `20`, máximo `100`). A resposta usa o formato paginado do Spring:

```json
{
  "content": [],
  "number": 0,
  "size": 20,
  "totalElements": 0,
  "totalPages": 0
}
```

Exemplo: `GET /atendimentos?status=AGENDADO&tipo=VACINA&page=0&size=20`

## Códigos de resposta

| Situação | Código |
| --- | --- |
| Criou | `201` e o corpo do registro criado |
| Leu ou atualizou | `200` |
| Corpo inválido (campo faltando, formato, tamanho, valor ≤ 0) | `400` |
| Id que não existe | `404` |
| E-mail ou CRMV repetido, pessoa inativa, horário ocupado ou status inválido | `409` |

As respostas de erro trazem uma mensagem que indica qual regra falhou.

## Regras de negócio

1. Não pode haver dois tutores com o mesmo e-mail.
2. Não pode haver dois veterinários com o mesmo `crmv`.
3. Tutor ou veterinário com `ativo = false` não entra em atendimento novo (`409`).
4. O mesmo veterinário não pode ter dois atendimentos `AGENDADO` no mesmo `data_hora`. Atendimentos concluídos e cancelados não ocupam o horário (`409`).
5. `valor` precisa ser maior que zero (`400`).
6. O status só muda de `AGENDADO` para `CONCLUIDO` ou de `AGENDADO` para `CANCELADO`. `CONCLUIDO` e `CANCELADO` não mudam mais.
7. Qualquer outra troca de status responde `409`.
8. Se o tutor ou o veterinário informado não existir, responde `404`.

## Exemplos

### Cadastrar tutor

```bash
curl -X POST http://localhost:8080/tutores \
  -H "Content-Type: application/json" \
  -d '{"nome":"Ana Souza","email":"ana@email.com","telefone":"11987654321","cidade":"Campinas"}'
```

Resposta `201`:

```json
{
  "id": 1,
  "nome": "Ana Souza",
  "email": "ana@email.com",
  "telefone": "11987654321",
  "cidade": "Campinas",
  "dataCadastro": "2026-03-01T10:15:00",
  "ativo": true
}
```

### Cadastrar veterinário

```bash
curl -X POST http://localhost:8080/veterinarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"Carlos Lima","crmv":"SP-12345","especialidade":"CLINICA_GERAL"}'
```

Resposta `201`:

```json
{
  "id": 1,
  "nome": "Carlos Lima",
  "crmv": "SP-12345",
  "especialidade": "CLINICA_GERAL",
  "ativo": true
}
```

### Agendar atendimento

```bash
curl -X POST http://localhost:8080/atendimentos \
  -H "Content-Type: application/json" \
  -d '{"tutorId":1,"veterinarioId":1,"dataHora":"2026-03-10T14:30:00","tipo":"CONSULTA","valor":150.00,"observacao":"Animal apático desde ontem"}'
```

Resposta `201`:

```json
{
  "id": 1,
  "tutorId": 1,
  "tutorNome": "Ana Souza",
  "veterinarioId": 1,
  "veterinarioNome": "Carlos Lima",
  "dataHora": "2026-03-10T14:30:00",
  "tipo": "CONSULTA",
  "valor": 150.00,
  "observacao": "Animal apático desde ontem",
  "status": "AGENDADO"
}
```

### Concluir atendimento

```bash
curl -X PATCH http://localhost:8080/atendimentos/1/status \
  -H "Content-Type: application/json" \
  -d '{"status":"CONCLUIDO"}'
```

### Listar com filtros

```bash
curl "http://localhost:8080/atendimentos?status=AGENDADO&tipo=VACINA&dataInicio=2026-03-01T00:00:00&dataFim=2026-03-31T23:59:59&page=0&size=20"
curl "http://localhost:8080/tutores?nome=ana&cidade=Campinas"
curl "http://localhost:8080/veterinarios?especialidade=CIRURGIA"
```

## Solução de problemas

- **`Connection refused` ou erro de conexão ao subir:** confirme que o serviço do PostgreSQL está ativo (`services.msc` no Windows) e que a porta é `5432`.
- **`password authentication failed`:** usuário ou senha do `application.properties` não conferem com os do PostgreSQL.
- **`database "amigofiel" does not exist`:** crie o banco (passo 1).
- **`relation "tutores" does not exist` ao rodar o `dados.sql`:** suba a aplicação uma vez antes, para o Hibernate criar as tabelas.
- **Porta 8080 em uso:** feche o programa que a usa ou suba com `--server.port=8081`.
-