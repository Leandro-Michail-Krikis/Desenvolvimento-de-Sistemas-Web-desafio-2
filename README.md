# Desenvolvimento-de-Sistemas-Web-desafio-2

## Stack atual

- Spring Boot + Spring Data JPA
- PostgreSQL (via Docker Compose)
- Flyway para versionamento do schema
- Spring Security com autenticacao Basic Auth

## Subindo o banco com Docker Compose

O projeto usa `compose.yaml` na raiz com PostgreSQL na porta `5432`.

```bash
docker compose up -d
```

## Executando a aplicacao

```bash
./mvnw spring-boot:run
```

## Usuarios padrao (seed automatico)

No startup, os usuarios abaixo sao criados na tabela `usuario` (se nao existirem):

- `admin` / `admin123` (perfil `ADMIN`)
- `user` / `user123` (perfil `USER`)

## Regras de acesso

- `GET /destinos/**` e `GET /reservas/destino/**`: `USER` ou `ADMIN`
- `POST /reservas`: `USER` ou `ADMIN`
- `POST|PUT|PATCH|DELETE /destinos/**`: apenas `ADMIN`
- Swagger (`/swagger-ui/**` e `/v3/api-docs/**`): publico
