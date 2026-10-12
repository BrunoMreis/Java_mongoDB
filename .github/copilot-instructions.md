# Contexto do Projeto

Sistema desenvolvido com:

- Java 25
- Spring Boot
- MongoDB
- Thymeleaf
- Docker

# Regras

- Utilizar Java 25.
- Utilizar Spring Boot seguindo arquitetura Controller > Service > Repository.
- Persistência com Spring Data MongoDB.
- Views utilizando Thymeleaf.
- Priorizar reutilização de código existente.
- Não criar novas bibliotecas sem necessidade.
- Seguir padrão de nomenclatura já existente no projeto.
- Gerar apenas os arquivos solicitados.
- Evitar respostas longas.
- Não criar testes automaticamente.
- Não criar documentação automaticamente.

# Testes

- Priorizar cobertura de Services.
- Utilizar JUnit 5.
- Utilizar Mockito para mocks.
- Utilizar AssertJ quando disponível.
- Criar testes legíveis seguindo Given/When/Then.
- Não mockar objetos simples (DTOs, Entities).
- Cobrir fluxos de sucesso e erro.
- Evitar testes frágeis.
- Reutilizar builders e fixtures existentes.
- Gerar testes com nomes descritivos.