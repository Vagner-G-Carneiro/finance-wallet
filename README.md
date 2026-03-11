# Finance Wallet
### Banking API – Java & Spring Boot
API bancária desenvolvida em Java 21 com foco em integridade de dados,
segurança e concorrência, simulando operações financeiras reais.

## Funcionalidades (MVP)

- Criação de usuário
- Criação automática de carteira
- Depósito
- Transferência entre carteiras
- Extrato bancário com paginação
- Self delete de usuário e carteira

## Arquitetura e Infraestrutura

- Java 21
- Spring Boot
- PostgreSQL
- Docker (Banco de dados em container)
- Flyway (versionamento de schema)
- JWT (autenticação stateless)

## Segurança

- Hash de senha com BCrypt
- Autenticação via JWT
- Filtros HTTP para validação de requisições
- Proteção contra IDOR (validação de posse da carteira)

## Integridade Financeira

- UUID como chave primária
- BigDecimal para operações monetárias
- Value Object para encapsular valores financeiros
- Transações atômicas (@Transactional)
- Pessimistic Locking para evitar race conditions
  (prevenção do bug de dinheiro infinito)

## Qualidade de Código

- DTO Pattern
- Records (imutabilidade)
- Tratamento de erros baseado na RFC 7807
- Paginação para consultas eficientes

## Pontos de Atenção

- Aplicação ainda não sobe completamente via Docker
- UUIDs gerados no banco (possível gargalo sob carga)
- Implementação de locking em revisão para evitar deadlocks

## Próximos Passos

- Refatoração dos pontos críticos
- Dockerização completa da aplicação
- Testes unitários e de integração (JUnit + Mockito)
- Usuário administrador
- Revisão de permissões e papéis

## 📌 Objetivo do Projeto

Projeto desenvolvido com foco em aprendizado prático de
back-end, arquitetura, segurança e concorrência em aplicações Java.
O foco pós desenvolvimento do mesmo seria criar o sistema de Deploy com CI/CD
Além de implementar proteções contra o OWASP 10 e subir para Nuvem a API Simples

