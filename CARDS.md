# 📋 Cards — Desafio 2: API para Agência de Viagem

> **Projeto:** API RESTful para Agência de Viagem  
> **Stack:** Java · Spring Boot · H2 · Flyway · Swagger  
> **Entrega:** Via repositório Git (AVA)

---

## 🗂️ Legenda de Status

| Ícone | Status |
|-------|--------|
| ✅ | Concluído |
| 🔲 | Pendente |
| ⚠️ | Incompleto / Precisa ajuste |

---

## 🏗️ Mapa Completo da Estrutura Esperada do Projeto

```
src/main/java/.../desafio2/
│
├── config/
│   ├── SwaggerConfig.java          ✅ criado
│   └── StartupLogger.java          ✅ criado
│
├── controller/
│   ├── DestinoController.java      ⚠️ faltam endpoints de busca e PATCH /avaliar
│   ├── AvaliacaoController.java    🔲 não existe
│   └── ReservaController.java      🔲 não existe
│
├── dto/                            🔲 pacote inteiro não existe
│   ├── request/
│   │   ├── DestinoRequest.java     🔲
│   │   ├── AvaliacaoRequest.java   🔲
│   │   └── ReservaRequest.java     🔲
│   └── response/
│       └── ErroResponse.java       🔲
│
├── entity/
│   ├── Destino.java                ⚠️ faltam campos nota e totalAvaliacoes
│   ├── Avaliacao.java              🔲 não existe
│   └── Reserva.java                🔲 não existe
│
├── enums/                          🔲 pacote inteiro não existe
│   └── StatusReserva.java          🔲
│
├── exception/                      🔲 pacote inteiro não existe
│   ├── DestinoNaoEncontradoException.java    🔲
│   ├── ReservaNaoEncontradaException.java    🔲
│   └── GlobalExceptionHandler.java          🔲
│
├── repository/
│   ├── DestinoRepository.java      ✅ criado
│   ├── AvaliacaoRepository.java    🔲 não existe
│   └── ReservaRepository.java      🔲 não existe
│
└── service/
    ├── DestinoService.java         ⚠️ faltam métodos buscarPorNome, buscarPorPais e avaliar
    ├── AvaliacaoService.java       🔲 não existe
    └── ReservaService.java         🔲 não existe

src/main/resources/db/migration/
├── V1__create_destino_table.sql    ✅ criado (com dados de exemplo)
├── V2__create_avaliacao_table.sql  🔲 não existe
└── V4__create_reserva_table.sql    🔲 não existe
```

---

## 🧭 Visão Geral dos Endpoints Exigidos

| Método | URL | Funcionalidade | Status |
|--------|-----|----------------|--------|
| `POST` | `/destinos` | Cadastrar destino | ✅ |
| `GET` | `/destinos` | Listar todos | ✅ |
| `GET` | `/destinos/buscar?nome=` | Pesquisar por nome | ⚠️ falta no controller e service |
| `GET` | `/destinos/buscar?pais=` | Pesquisar por localização | ⚠️ falta no controller e service |
| `GET` | `/destinos/{id}` | Visualizar detalhes | ✅ |
| `PUT` | `/destinos/{id}` | Atualizar destino | ✅ |
| `PATCH` | `/destinos/{id}/avaliar` | Avaliar destino (nota 1–10) | 🔲 |
| `DELETE` | `/destinos/{id}` | Excluir destino | ✅ |
| `POST` | `/reservas` | Reservar pacote de viagem | 🔲 |
| `GET` | `/reservas/destino/{id}` | Listar reservas de um destino | 🔲 |

---

## 📦 GRUPO 1 — Infraestrutura Transversal

---

### CARD 01 — Criar pacotes de organização do projeto

**Status:** 🔲 Pendente  
**Tipo:** Infraestrutura

**Descrição:**  
Criar a estrutura de pacotes Java que organiza o projeto corretamente antes de implementar as classes. Boa prática exigida no critério "Qualidade do Código".

**Tarefas:**
- [ ] Criar pacote `dto.request`
- [ ] Criar pacote `dto.response`
- [ ] Criar pacote `enums`
- [ ] Criar pacote `exception`

**Critério de aceite:**  
Todos os pacotes existem na estrutura do projeto e as futuras classes são criadas dentro deles.

---

### CARD 02 — DTOs de Request (Entrada de dados)

**Status:** 🔲 Pendente  
**Tipo:** DTO / Validação

**Descrição:**  
Criar as classes de transferência de dados (DTOs) para receber os dados do corpo das requisições (`@RequestBody`). Atualmente os controllers recebem diretamente as entidades JPA, o que é uma má prática — expõe campos internos (como `dataCriacao`, `id`) e mistura responsabilidades.

**Por que é necessário:**  
- Separar o contrato da API do modelo do banco de dados
- Permitir validações com Bean Validation (`@NotBlank`, `@Min`, `@Max`, etc.)
- Evitar que o cliente envie campos que não deveria (ex: `id`, `dataCriacao`)

**Tarefas:**

**`DestinoRequest.java`** — usado no `POST /destinos` e `PUT /destinos/{id}`
- [ ] Campo `nome` com `@NotBlank(message = "Nome é obrigatório")`
- [ ] Campo `descricao` com `@NotBlank(message = "Descrição é obrigatória")`
- [ ] Campo `pais` com `@NotBlank(message = "País é obrigatório")`

**`AvaliacaoRequest.java`** — usado no `PATCH /destinos/{id}/avaliar`
- [ ] Campo `valor` (Integer) com `@NotNull`, `@Min(1)` e `@Max(10)`

**`ReservaRequest.java`** — usado no `POST /reservas`
- [ ] Campo `nomeCliente` com `@NotBlank`
- [ ] Campo `email` com `@NotBlank` e `@Email`
- [ ] Campo `dataViagem` (LocalDate) com `@NotNull` e `@Future`
- [ ] Campo `numeroPessoas` (Integer) com `@NotNull` e `@Min(1)`
- [ ] Campo `destinoId` (Long) com `@NotNull`

**Critério de aceite:**  
Controllers recebem DTOs de request em vez de entidades. Enviar dados inválidos retorna `400 Bad Request` com mensagem descritiva.

---

### CARD 03 — Classe de resposta de erro padronizada

**Status:** 🔲 Pendente  
**Tipo:** DTO / Infraestrutura

**Descrição:**  
Criar uma classe `ErroResponse` para padronizar o formato das respostas de erro da API. Sem isso, erros retornam o stacktrace padrão do Spring, que é verboso e inadequado para uma API pública.

**`ErroResponse.java`** — corpo de todas as respostas de erro
- [ ] Campo `status` (int) — código HTTP (ex: 404, 400)
- [ ] Campo `mensagem` (String) — mensagem amigável do erro
- [ ] Campo `timestamp` (LocalDateTime) — momento do erro

**Exemplo de resposta esperada:**
```json
{
  "status": 404,
  "mensagem": "Destino não encontrado com id: 99",
  "timestamp": "2026-05-23T12:00:00"
}
```

**Critério de aceite:**  
Todos os erros da API retornam JSON nesse formato, sem stacktrace exposto.

---

### CARD 04 — Exceções customizadas

**Status:** 🔲 Pendente  
**Tipo:** Exception

**Descrição:**  
Criar exceções específicas para cada situação de erro do domínio. Atualmente o `DestinoService` lança `RuntimeException` genérica, o que dificulta o tratamento adequado e retorna erro `500` em vez de `404`.

**Tarefas:**

**`DestinoNaoEncontradoException.java`**
- [ ] Estender `RuntimeException`
- [ ] Construtor recebendo `Long id` e montando a mensagem: `"Destino não encontrado com id: " + id`

**`ReservaNaoEncontradaException.java`**
- [ ] Estender `RuntimeException`
- [ ] Construtor recebendo `Long id` e montando a mensagem: `"Reserva não encontrada com id: " + id`

**Critério de aceite:**  
Services lançam as exceções específicas do domínio em vez de `RuntimeException` genérica.

---

### CARD 05 — GlobalExceptionHandler (tratamento centralizado de erros)

**Status:** 🔲 Pendente  
**Tipo:** Exception / Infraestrutura

**Descrição:**  
Criar o handler global de exceções com `@RestControllerAdvice`. Sem ele, o Spring retorna HTML ou stacktrace nos erros, e os controllers precisam de try/catch individuais (como o atual `DestinoController.atualizar`). Este é o componente mais importante de qualidade de código da API.

**Tarefas:**
- [ ] Criar classe `GlobalExceptionHandler` com `@RestControllerAdvice`
- [ ] Método `handleDestinoNaoEncontrado` anotado com `@ExceptionHandler(DestinoNaoEncontradoException.class)` → retorna `404 Not Found` + `ErroResponse`
- [ ] Método `handleReservaNaoEncontrada` anotado com `@ExceptionHandler(ReservaNaoEncontradaException.class)` → retorna `404 Not Found` + `ErroResponse`
- [ ] Método `handleValidationErrors` anotado com `@ExceptionHandler(MethodArgumentNotValidException.class)` → retorna `400 Bad Request` com lista de erros de validação
- [ ] Método `handleGenerico` anotado com `@ExceptionHandler(Exception.class)` → retorna `500 Internal Server Error`

**Impacto:**  
Após este card, remover todos os `try/catch` dos controllers e o `Optional.orElse(ResponseEntity.notFound().build())` — eles passam a ser desnecessários.

**Critério de aceite:**  
Qualquer erro retorna JSON padronizado com status correto. Nenhum controller precisa de bloco try/catch.

---

## 🗃️ GRUPO 2 — Entidades e Banco de Dados

---

### CARD 06 — Enum `StatusReserva`

**Status:** 🔲 Pendente  
**Tipo:** Enum

**Descrição:**  
Criar o enum que representa os possíveis estados de uma reserva de pacote de viagem.

**Tarefas:**
- [ ] Criar `StatusReserva.java` no pacote `enums`
- [ ] Valores: `CONFIRMADA`, `PENDENTE`, `CANCELADA`

**Critério de aceite:**  
Enum disponível para uso na entidade `Reserva`.

---

### CARD 07 — Atualizar entidade `Destino` (campos de avaliação)

**Status:** ⚠️ Incompleto  
**Tipo:** Entidade / Modelo

**Descrição:**  
Adicionar os campos necessários para armazenar a média calculada das notas e o total de avaliações recebidas. Esses campos são atualizados automaticamente pelo `NotaService` toda vez que uma nova `Nota` é registrada.

**Tarefas:**
- [ ] Adicionar campo `notaMedia` (Double) com `@Column(name = "nota_media")` — valor default `0.0`
- [ ] Adicionar campo `totalNotas` (Integer) com `@Column(name = "total_notas")` — valor default `0`

**Fluxo esperado:**
```
Cliente envia nota → NotaService salva registro em `nota`
                   → Recalcula média
                   → Atualiza `nota_media` e `total_notas` em `destino`
                   → Retorna destino com nova média
```

**Critério de aceite:**  
Campos presentes na entidade e na tabela do banco (via migration V2). Toda nova nota atualiza automaticamente esses campos.

---

### CARD 08 — Entidade `Nota`

**Status:** 🔲 Pendente  
**Tipo:** Entidade / Modelo

**Descrição:**  
Criar a entidade JPA que representa cada nota individual recebida para um destino. Cada chamada ao endpoint `PATCH /destinos/{id}/avaliar` gera **um registro novo** nesta tabela. A média (`notaMedia`) no `Destino` é então recalculada com base em todos os registros de `Nota` daquele destino.

**Por que ter uma tabela separada para `Nota`:**
- Preserva o **histórico completo** de avaliações
- Permite **recalcular a média** a partir dos dados reais a qualquer momento
- Possibilita futuras funcionalidades (ex: listar notas por destino, notas por período)
- Separa responsabilidade: `Nota` guarda o dado bruto, `Destino` guarda o dado calculado

**Campos:**
- `id` — Long, `@Id`, `@GeneratedValue`
- `valor` — Integer, `@Column(nullable = false)`, valor entre 1 e 10
- `dataAvaliacao` — LocalDateTime, `@Column(name = "data_avaliacao", nullable = false)`, preenchido automaticamente
- `destino` — relacionamento `@ManyToOne @JoinColumn(name = "destino_id")` com a entidade `Destino`

**Tarefas:**
- [ ] Criar `Nota.java` no pacote `entity`
- [ ] Anotações JPA: `@Entity`, `@Table(name = "nota")`
- [ ] Lombok: `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`
- [ ] Relacionamento `@ManyToOne` com `Destino`

**Critério de aceite:**  
Cada chamada ao endpoint de avaliação cria um registro novo na tabela `nota`. Relacionamento com `Destino` funcionando.

---

### CARD 09 — Entidade `Reserva`

**Status:** 🔲 Pendente  
**Tipo:** Entidade / Modelo

**Descrição:**  
Criar a entidade JPA que representa uma reserva de pacote de viagem para um destino. Necessária para o endpoint "Reservar pacotes de viagens para um destino".

**Campos:**
- `id` — Long, `@Id`, `@GeneratedValue`
- `nomeCliente` — String, `@Column(nullable = false)`
- `email` — String, `@Column(nullable = false)`
- `dataReserva` — LocalDateTime, `@Column(name = "data_reserva", nullable = false)` — preenchido automaticamente no service
- `dataViagem` — LocalDate, `@Column(name = "data_viagem", nullable = false)` — informada pelo cliente
- `numeroPessoas` — Integer, `@Column(name = "numero_pessoas", nullable = false)`
- `status` — `@Enumerated(EnumType.STRING)` usando `StatusReserva`
- `destino` — `@ManyToOne @JoinColumn(name = "destino_id")` com a entidade `Destino`

**Tarefas:**
- [ ] Criar `Reserva.java` no pacote `entity`
- [ ] Anotações JPA: `@Entity`, `@Table(name = "reserva")`
- [ ] Lombok: `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`
- [ ] Relacionamento `@ManyToOne` com `Destino`
- [ ] `@Enumerated(EnumType.STRING)` no campo `status`

**Critério de aceite:**  
Entidade mapeada no banco via Flyway. Reserva persiste vinculada ao Destino correto.

---

### CARD 10 — Migration `V3__create_nota_table.sql`

**Status:** 🔲 Pendente  
**Tipo:** Banco de dados / Flyway

**Descrição:**  
Criar o script Flyway que adiciona a tabela de notas individuais e os campos de média calculada no destino.

**Tarefas:**
- [ ] Criar arquivo `V3__create_nota_table.sql`
- [ ] `ALTER TABLE destino ADD COLUMN nota_media DOUBLE DEFAULT 0.0`
- [ ] `ALTER TABLE destino ADD COLUMN total_notas INTEGER DEFAULT 0`
- [ ] Criar `TABLE nota` com colunas:
  - `id` BIGINT AUTO_INCREMENT PRIMARY KEY
  - `valor` INTEGER NOT NULL
  - `data_avaliacao` TIMESTAMP NOT NULL
  - `destino_id` BIGINT NOT NULL, FOREIGN KEY → `destino(id)`

**Exemplo da tabela:**
```sql
CREATE TABLE nota (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    valor          INTEGER NOT NULL,
    data_avaliacao TIMESTAMP NOT NULL,
    destino_id     BIGINT NOT NULL,
    CONSTRAINT fk_nota_destino FOREIGN KEY (destino_id) REFERENCES destino(id)
);
```

**Critério de aceite:**  
Aplicação sobe com as novas colunas em `destino` e a tabela `nota` no banco. Visível no H2 Console.

---

### CARD 11 — Migration `V4__create_reserva_table.sql`

**Status:** 🔲 Pendente  
**Tipo:** Banco de dados / Flyway

**Descrição:**  
Criar o script Flyway que cria a tabela de reservas com chave estrangeira para a tabela de destinos.

**Tarefas:**
- [ ] Criar arquivo `V4__create_reserva_table.sql`
- [ ] `CREATE TABLE reserva` com colunas: `id`, `nome_cliente`, `email`, `data_reserva`, `data_viagem`, `numero_pessoas`, `status`, `destino_id` (FK → destino.id)

**Critério de aceite:**  
Tabela `reserva` criada no banco com o relacionamento correto com `destino`.

---

## 🗄️ GRUPO 3 — Repositórios (DAO)

---

### CARD 12 — `DestinoRepository` (já existe, verificar)

**Status:** ✅ Concluído

**Descrição:**  
Interface de repositório para a entidade `Destino`.

**Métodos existentes:**
- [x] `findByPaisIgnoreCase(String pais)`
- [x] `findByNomeContainingIgnoreCase(String nome)`

---

### CARD 13 — `NotaRepository`

**Status:** 🔲 Pendente  
**Tipo:** Repository / DAO

**Descrição:**  
Criar o repositório de acesso ao banco para a entidade `Nota`. O `NotaService` usa este repositório para persistir cada nota recebida e para buscar todas as notas de um destino e recalcular a média.

**Tarefas:**
- [ ] Criar interface `NotaRepository` estendendo `JpaRepository<Nota, Long>`
- [ ] Método `findByDestinoId(Long destinoId)` — busca todas as notas de um destino (histórico completo)
- [ ] Método `countByDestinoId(Long destinoId)` — conta quantas notas um destino tem

**Critério de aceite:**  
Repositório injetado com sucesso no `NotaService`.

---

### CARD 14 — `ReservaRepository`

**Status:** 🔲 Pendente  
**Tipo:** Repository / DAO

**Descrição:**  
Criar o repositório de acesso ao banco para a entidade `Reserva`.

**Tarefas:**
- [ ] Criar interface `ReservaRepository` estendendo `JpaRepository<Reserva, Long>`
- [ ] Método `findByDestinoId(Long destinoId)` — listar reservas de um destino específico

**Critério de aceite:**  
Repositório injetado com sucesso no `ReservaService`.

---

## ⚙️ GRUPO 4 — Services (Lógica de Negócio)

---

### CARD 15 — Completar `DestinoService`

**Status:** ⚠️ Incompleto  
**Tipo:** Service

**Descrição:**  
O `DestinoService` atual tem apenas: `listarTodos`, `buscarPorId`, `salvar`, `atualizar`, `deletar`. Estão faltando os métodos de busca. Além disso, o método `atualizar` usa `RuntimeException` genérica e os métodos `salvar`/`atualizar` recebem entidade diretamente em vez de DTO.

**Tarefas:**
- [ ] Adicionar método `buscarPorNome(String nome)` — delega para `DestinoRepository.findByNomeContainingIgnoreCase`
- [ ] Adicionar método `buscarPorPais(String pais)` — delega para `DestinoRepository.findByPaisIgnoreCase`
- [ ] Substituir `RuntimeException` por `DestinoNaoEncontradoException` nos métodos `atualizar` e `buscarPorId`
- [ ] Substituir recebimento de `Destino` (entidade) por `DestinoRequest` (DTO) nos métodos `salvar` e `atualizar`

> ⚠️ A lógica de avaliação **não fica aqui** — fica em `NotaService` (CARD 16)

**Critério de aceite:**  
Service com métodos de busca implementados. Exceções corretas lançadas. DTO usado como entrada em `salvar` e `atualizar`.

---

### CARD 16 — Criar `NotaService`

**Status:** 🔲 Pendente  
**Tipo:** Service

**Descrição:**  
Criar a camada de serviço responsável pela lógica de avaliação de destinos. Toda vez que uma nova nota chega, o `NotaService` salva o registro na tabela `nota` e recalcula a média, atualizando os campos `notaMedia` e `totalNotas` na tabela `destino`.

**Fluxo completo dentro do método `avaliar`:**
```
1. Verificar se o destino existe → lança DestinoNaoEncontradoException se não
2. Criar e salvar um novo registro Nota { valor, dataAvaliacao, destino }
3. Calcular nova média:
      novoTotal    = destino.getTotalNotas() + 1
      novaMedia    = ((destino.getNotaMedia() * destino.getTotalNotas()) + valorNota) / novoTotal
4. Atualizar destino.setNotaMedia(novaMedia)
5. Atualizar destino.setTotalNotas(novoTotal)
6. Salvar destino atualizado
7. Retornar o destino com a nova média
```

**Tarefas:**
- [ ] Criar `NotaService` com `@Service` e `@RequiredArgsConstructor`
- [ ] Injetar `NotaRepository` e `DestinoRepository`
- [ ] Implementar método `avaliar(Long destinoId, Integer valorNota)` com o fluxo acima
- [ ] Usar `@Transactional` para garantir que salvamento da nota e atualização do destino sejam atômicos

**Critério de aceite:**  
Cada chamada cria 1 registro em `nota`. A `notaMedia` em `destino` é recalculada corretamente.  
Exemplo: destino com `notaMedia=6.0` e `totalNotas=2`, recebe nota `9` → nova média = `(6.0*2 + 9) / 3 = 7.0`.

---

### CARD 17 — Criar `ReservaService`

**Status:** 🔲 Pendente  
**Tipo:** Service

**Descrição:**  
Criar a camada de serviço para a lógica de negócio das reservas de pacotes de viagem.

**Tarefas:**
- [ ] Criar `ReservaService` com `@Service` e `@RequiredArgsConstructor`
- [ ] Injetar `ReservaRepository` e `DestinoRepository`
- [ ] Implementar método `reservar(ReservaRequest request)`:
  - Verificar se o destino informado existe (`DestinoNaoEncontradoException` se não)
  - Criar entidade `Reserva` com os dados do request
  - Definir `dataReserva` como `LocalDateTime.now()`
  - Definir `status` inicial como `StatusReserva.CONFIRMADA`
  - Persistir e retornar a reserva criada
- [ ] Implementar método `listarPorDestino(Long destinoId)`:
  - Verificar se o destino existe
  - Retornar `reservaRepository.findByDestinoId(destinoId)`

**Critério de aceite:**  
Reserva criada com status `CONFIRMADA`, `dataReserva` preenchida automaticamente e vinculada ao destino correto.

---

## 🌐 GRUPO 5 — Controllers (Endpoints HTTP)

---

### CARD 18 — Completar `DestinoController`

**Status:** ⚠️ Incompleto  
**Tipo:** Controller

**Descrição:**  
O `DestinoController` atual tem: `GET /destinos`, `GET /destinos/{id}`, `POST /destinos`, `PUT /destinos/{id}`, `DELETE /destinos/{id}`. Estão **faltando** os endpoints de busca e o de avaliação. Além disso, os métodos precisam ser atualizados para usar DTOs e o `GlobalExceptionHandler`.

**Tarefas:**
- [ ] Adicionar `GET /destinos/buscar?nome=` → chama `destinoService.buscarPorNome(nome)`
- [ ] Adicionar `GET /destinos/buscar?pais=` (via `@RequestParam` opcional) → chama `destinoService.buscarPorPais(pais)` — ou usar dois parâmetros opcionais no mesmo endpoint
- [ ] Adicionar `PATCH /destinos/{id}/avaliar` → recebe `@Valid @RequestBody AvaliacaoRequest` e chama `notaService.avaliar(id, request.getValor())`
- [ ] Atualizar `POST /destinos` para receber `@Valid @RequestBody DestinoRequest` em vez de `Destino`
- [ ] Atualizar `PUT /destinos/{id}` para receber `@Valid @RequestBody DestinoRequest` em vez de `Destino`
- [ ] Remover todos os blocos `try/catch` (tratados pelo `GlobalExceptionHandler`)
- [ ] Remover `Optional.orElse(ResponseEntity.notFound().build())` — exceção customizada cuida disso

**Critério de aceite:**  
Controller limpo, sem try/catch. DTOs usados nas entradas. Todos os 7 endpoints do controller funcionando corretamente.

---

### CARD 19 — Criar `ReservaController`

**Status:** 🔲 Pendente  
**Tipo:** Controller

**Descrição:**  
Criar o controller REST para o fluxo de reservas de pacotes de viagem.

**Tarefas:**
- [ ] Criar `ReservaController` com `@RestController`, `@RequestMapping("/reservas")`, `@RequiredArgsConstructor`
- [ ] Injetar `ReservaService`
- [ ] Endpoint `POST /reservas` → recebe `@Valid @RequestBody ReservaRequest`, retorna `201 Created` com a reserva
- [ ] Endpoint `GET /reservas/destino/{destinoId}` → retorna `200 OK` com lista de reservas do destino

**Critério de aceite:**  
`POST /reservas` com `destinoId` válido retorna `201 Created`. `destinoId` inválido retorna `404 Not Found` via `GlobalExceptionHandler`.

---

## 🔧 GRUPO 6 — Configuração e Entrega

---

### CARD 20 — Documentação Swagger

**Status:** ✅ Concluído

**Descrição:**  
Swagger UI integrado ao projeto para documentação interativa.

**Tarefas:**
- [x] Dependência `springdoc-openapi-starter-webmvc-ui`
- [x] `SwaggerConfig.java` com metadados da API
- [x] `StartupLogger.java` que imprime URLs no console

**URLs:**
```
📋 Swagger UI → http://localhost:8080/swagger-ui/index.html
📄 API Docs   → http://localhost:8080/v3/api-docs
🗄️  H2 Console → http://localhost:8080/h2-console
```

---

### CARD 21 — Entrega via Repositório Git

**Status:** 🔲 Pendente  
**Tipo:** Entrega

**Descrição:**  
Realizar a entrega final do projeto via repositório Git.

**Tarefas:**
- [ ] Verificar que todos os endpoints respondem corretamente via Swagger
- [ ] Executar `./mvnw clean package` e confirmar que compila sem erros
- [ ] Fazer commit de todos os arquivos
- [ ] Fazer push para repositório remoto (GitHub / GitLab)
- [ ] Compartilhar link do repositório no AVA dentro do prazo

---

## 📊 Resumo Completo do Progresso

### Por Camada:

| Camada | Total | ✅ Feito | ⚠️ Incompleto | 🔲 Pendente |
|--------|-------|----------|---------------|-------------|
| Infraestrutura (pacotes, DTOs, exceptions) | 5 cards | 0 | 0 | 5 |
| Entidades / Banco | 6 cards | 1 (V1) | 1 (Destino) | 4 |
| Repositories | 3 cards | 1 | 0 | 2 |
| Services | 3 cards | 0 | 1 (Destino) | 2 |
| Controllers | 2 cards | 0 | 1 (Destino) | 1 |
| Config / Entrega | 2 cards | 1 (Swagger) | 0 | 1 |
| **Total** | **21 cards** | **3** | **3** | **15** |

---

### Por Card:

| Card | Grupo | Descrição | Status |
|------|-------|-----------|--------|
| 01 | Infra | Criar pacotes dto, enums, exception | 🔲 |
| 02 | Infra | DTOs de request (Destino, Avaliacao, Reserva) | 🔲 |
| 03 | Infra | `ErroResponse` (resposta padronizada de erros) | 🔲 |
| 04 | Infra | Exceções customizadas do domínio | 🔲 |
| 05 | Infra | `GlobalExceptionHandler` (tratamento centralizado) | 🔲 |
| 06 | Modelo | Enum `StatusReserva` | 🔲 |
| 07 | Modelo | Atualizar `Destino` (campos nota + totalAvaliacoes) | ⚠️ |
| 08 | Modelo | Entidade `Nota` (histórico de avaliações) | 🔲 |
| 09 | Modelo | Entidade `Reserva` | 🔲 |
| 10 | Banco | Migration V2 — tabela `nota` + colunas `notaMedia`/`totalNotas` em destino | 🔲 |
| 11 | Banco | Migration V3 — tabela reserva | 🔲 |
| 12 | DAO | `DestinoRepository` | ✅ |
| 13 | DAO | `NotaRepository` | 🔲 |
| 14 | DAO | `ReservaRepository` | 🔲 |
| 15 | Service | Completar `DestinoService` (busca + avaliar + DTOs) | ⚠️ |
| 16 | Service | `NotaService` (avaliar + recalcular média + atualizar Destino) | 🔲 |
| 17 | Service | `ReservaService` | 🔲 |
| 18 | Controller | Completar `DestinoController` (busca + PATCH + DTOs) | ⚠️ |
| 19 | Controller | `ReservaController` | 🔲 |
| 20 | Config | Swagger / Documentação | ✅ |
| 21 | Entrega | Entrega via Git | 🔲 |

---

## 🚨 Ordem de Implementação Recomendada (dependências entre cards)

```
CARD 01 (pacotes)
    ↓
CARD 06 (enum StatusReserva)
CARD 03 (ErroResponse)
CARD 04 (exceções customizadas)
    ↓
CARD 02 (DTOs de request)
CARD 05 (GlobalExceptionHandler)
CARD 07 (atualizar Destino → campos notaMedia e totalNotas)
CARD 08 (entidade Nota)
CARD 09 (entidade Reserva)
    ↓
CARD 10 (migration V2 → tabela nota + colunas em destino)
CARD 11 (migration V3 → tabela reserva)
    ↓
CARD 13 (NotaRepository)
CARD 14 (ReservaRepository)
    ↓
CARD 15 (completar DestinoService → busca por nome/pais)
CARD 16 (NotaService → avaliar + recalcular notaMedia + salvar Nota)
CARD 17 (ReservaService)
    ↓
CARD 18 (completar DestinoController → busca + PATCH /avaliar)
CARD 19 (ReservaController)
    ↓
CARD 21 (Entrega Git)
```
