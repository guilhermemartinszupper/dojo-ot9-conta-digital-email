# dojo-ot9-conta-digital-email

O serviço de email é, basicamente, um serviço de notificação. A sua função é ficar escutando um tópico no Kafka para saber quais as transações que aconteceram e enviar um email para as pessoas que são donas das contas.

Reunião 09/12 
Ficou acordado que temos que implementar o listener de emails, e salvar no banco de dados, e, se possível, enviar de fato um email. 
Além disso precisamos de testes de integração para o listener.

Reunião 10/12
Percebemos que existe uma dificuldade na hora de desenhar testes para os producers/listeners, tanto de boleto como de email, por causa do acomplamento com o kafka. Hoje precisamos verificar como fazer para estrever um teste de alto padrão para estas classes.
Também precisamos nos reunir com a squad do orquestrador, a fir de garantir que os endpoints estarão de acordo com o que é esperado pela regra de negócio
