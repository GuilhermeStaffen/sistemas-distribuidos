# Todo List
Uma TodoList é, como o nome indica, uma lista de tarefas/atividades/notas que precisam ser gerenciadas e realizadas. Essas tarefas podem conter e ser organizadas em função de suas datas, títulos, descrição e/ou outros detalhes que sejam relevantes para o contexto.
A aplicação se trata de um sistema com arquitetura híbrida (com recursos distribuídos e centralizados) de gerenciamento de tarefas (Todo List). A aplicação permite que os usuários adicionem, editem e excluam tarefas em uma interface amigável. Os usuários podem manter as tarefas criadas em sua conta, compartilhá-las com outros usuários e visualizar tarefas que a ele foram compartilhadas.

## Pré requisitos para executar o projeto (seja o front ou o back):
 1. Instalar o docker em sua maquina

 2. Criar subnet do mysqlcluster
```docker network create --subnet=194.164.0.0/16 mysql-cluster```

3. Criar rede para conexão entre os containers
```docker network create spring-net```

4. Acessar a pasta 'nginx', executar o nginx (caso esteja em um SO diferente de windows, baixar o [NGINX](https://nginx.org/en/download.html), configura-lo da mesma que no arquivo [\nginx\conf\nginx.conf](./nginx/conf/nginx.conf)):
```
cd .\nginx\
.\nginx.exe
```