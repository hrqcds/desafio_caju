# Desafio CAJU

Neste desafio, decidi montar uma arquitetura de microserviços em Kotlin utilizando Micronaut como framework principal. Temos um gateway utilizando Node.js, 2 instâncias do banco Postgres (uma para contas e outra para relatórios), uma instância do Redis para cache de contas, uma instância do RabbitMQ para o serviço de mensageria de transações e todos os serviços irão rodar no Docker.

Foi imaginado 3 entidades principais, sendo elas:

- **Conta**: A conta que será responsável pela movimentação.
- **Transação**: O ato de efetuar uma compra.
- **Relatórios**: Onde são armazenadas todas as movimentações.

## Funcionalidades

### Conta

- **Adicionar conta**: Cria uma nova conta para usar no sistema.
- **Adicionar saldo**: Adiciona um saldo para cada respectivo tipo de saldo.
- **Atualizar saldo**: Atualiza o valor do saldo para o novo cálculo. Esta rota foi pensada visando a transação, onde a regra de negócio ficaria anexada lá e só receberia os valores para serem atualizados.
- **Lista de contas**: Lista todas as contas criadas no sistema.
- **Buscar dados da conta**: Visualiza os dados da conta.

### Transação

- **Executar transação**: Executa a transação e atualiza o saldo da conta através da comunicação de microserviço.

A transação era o ponto central do desafio, nele era necessário se comunicar com os outros 2 microserviços. Optei por fazer a comunicação entre microserviços via HTTP, além disso, outros serviços garantem a segurança ao redor da transação:
- Toda vez que acontece uma transação, ela é registrada em uma fila do RabbitMQ onde o microserviço de relatórios fica escutando para adicionar a transação independente do status ao banco de dados.
- Quando a transação não consegue se comunicar com o serviço de contas, é acionado um circuit breaker que direciona para um banco em cache que fornece os dados das últimas transações por cliente.
- Ao retornar a comunicação, é atualizado o valor do saldo conforme o uso dos dados em cache.

#### Como funciona a transação:
- Recebe um payload com a conta, o total, o código MCC e o mercador.
- Verifica se a conta existe no microserviço de contas.
- Se o total for maior que o saldo, já envia o código 51 de saldo insuficiente.
- Verifica o MCC: se atende um dos códigos 5411 e 5412 (FOOD), 5811 e 5812 (MEAL), qualquer outro fica como cash.
- Caso caia em cash, temos uma segunda verificação, onde se busca no microserviço de relatórios a última transação aprovada por aquele mercador e, se houver, usa o MCC da última compra e verifica novamente qual saldo vai ser usado.
- Definido o saldo, verifica-se se o valor atende ao total. Se não, verifica-se se o valor + cash atende ao total. Se atender, o valor da conta é atualizado, é registrado na fila do RabbitMQ a transação com o código 00 e é atualizado o valor na conta.

### Relatórios

- **Buscar última transação por mercador**: Esta rota visa validar o MCC em casos do mesmo mercador já ter feito uma compra aprovada anteriormente e enviar um MCC por algum motivo aleatório.
- **Listar todas as transações por período**: Lista todas as transações conforme o filtro de início e fim (formato da data: YYYY-MM-DD).
- **Listar todas as transações por conta**: Filtra pelo período e pelo número da conta.

## Rodando a Aplicação

Configurei um Docker Compose que vai usar os Dockerfiles de cada microserviço e iniciar os serviços. As portas já estão configuradas de forma interna visando não ocupar possíveis portas. Apenas a gateway, os bancos e os serviços de Redis e RabbitMQ estão acessíveis externamente.

Para iniciar, rode o comando:

Dependência: Docker e internet

```bash
docker-compose up --build -d

Ele vai subir as seguintes instâncias:

- Gateway
- Report Service
- Transaction Service
- Account Service
- DB_account
- DB_report
- Redis
- RabbitMQ

O gateway roda na porta 9000 e pode-se acessar o Swagger pelo IP:
- [http://127.0.0.1:9000/api-docs](http://127.0.0.1:9000/api-docs)
- [http://localhost:9000/api-docs](http://localhost:9000/api-docs)

Lembre-se de adicionar uma conta para usar os outros serviços.

## Maiores Desafios

- **Linguagem e framework**: Foi meu primeiro contato com Kotlin para backend. Eu conhecia um pouco de Java, mas achei uma linguagem interessante e bastante robusta. Junto com o Micronaut, foi difícil escolher o que usar, pois existem muitas opções e formas de fazer a mesma coisa.
- **Paradigmas**: Tentei aplicar o máximo de paradigma funcional que pude, mas senti uma certa dificuldade por conta do próprio Micronaut ser baseado em classes e no esquema de annotations.

Se precisar de mais alguma coisa, estou à disposição! 🚀

