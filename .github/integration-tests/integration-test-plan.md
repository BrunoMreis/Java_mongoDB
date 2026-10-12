# Plano de testes de integração

## Escopo e lacunas

O projeto usa Spring Boot com persistência MongoDB por meio de `AlunoRepository` e do `AlunoCodec` customizado. Os testes unitários existentes verificam as operações com mocks, mas não validam serialização BSON, gravação, leitura e consultas com um MongoDB real.

## Estratégia

- Camada: Layer 1 (integração local).
- Infraestrutura: MongoDB 7.0.14 via Testcontainers; Docker é necessário.
- Configuração: `spring.data.mongodb.uri` recebe a URI dinâmica do container; `MongoConfiguration` cria o cliente compartilhado e registra o codec.
- Isolamento: limpar a coleção `teste.alunos` antes de cada teste.
- Código testado: operações públicas de `AlunoRepository`, com `AlunoCodec` utilizado pelo driver.

## Cenários e critérios

1. Salvar aluno sem ID e recuperar pelo ID gerado; verificar campos principais e notas.
2. Salvar alunos diferentes e pesquisar pelo nome; somente correspondências devem ser retornadas.
3. Pesquisar aprovados e reprovados pelo limite, verificando as notas armazenadas.

Cada cenário deve passar com o MongoDB real. `mvn verify` deve descobrir todos os testes; `mvn verify -Dgroups=Layer1` executa somente esta camada.
