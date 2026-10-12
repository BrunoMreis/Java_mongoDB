# Resumo dos testes de integração

## Testes adicionados

- `src/test/java/br/com/curso/alura/cursoalura/repository/AlunoRepositoryL1Test.java`
  - Persistência/leitura pelo ID, consulta por nome e classificação por nota usando MongoDB real em Testcontainers.
- `src/test/java/br/com/curso/alura/cursoalura/repository/AlunoRepositoryTest.java`
  - Testes unitários ajustados para injetar `MongoClient` mockado, sem interceptar a criação estática do cliente.

## Alterações de produção

- `MongoConfiguration` cria um `MongoClient` singleton com URI configurável por `spring.data.mongodb.uri` e registra o `AlunoCodec`.
- `AlunoRepository` recebe o cliente por construtor, mantém seu ciclo de vida sob gestão do Spring e fecha os cursores após o uso.
- As pesquisas de classificação consultam o campo `notas`, conforme o documento persistido pelo codec; classificações desconhecidas retornam lista vazia.

## Execução

- `mvn -Dgroups=Layer1 verify`: passou, 3 testes Layer 1, 0 falhas.
- `mvn verify`: passou, 42 testes no total, 0 falhas, 0 erros, 0 ignorados.

## Comandos/launchers

- Windows: `powershell -ExecutionPolicy Bypass -File .github/integration-tests/run-layer1-tests.ps1`
- Unix: `bash .github/integration-tests/run-layer1-tests.sh`
