
# Todo List
Uma TodoList é, como o nome indica, uma lista de tarefas/atividades/notas que precisam ser gerenciadas e realizadas. Essas tarefas podem conter e ser organizadas em função de suas datas, títulos, descrição e/ou outros detalhes que sejam relevantes para o contexto.
A aplicação se trata de um sistema com arquitetura híbrida (com recursos distribuídos e centralizados) de gerenciamento de tarefas (Todo List). A aplicação permite que os usuários adicionem, editem e excluam tarefas em uma interface amigável. Os usuários podem manter as tarefas criadas em sua conta, compartilhá-las com outros usuários e visualizar tarefas que a ele foram compartilhadas.

## Para executar com o docker no windows:

 1. Instalar o docker em sua maquina
 2. Baixar e iniciar a imagem do mysql
```
docker pull mysql
docker run -d -p 3307:3306 --name mysqldb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=todolist mysql
```

3. Criar rede para conexão entre os containers
```docker network create spring-net```

4. Conectar containers nesta rede criada, usando o comando
```docker network connect spring-net mysqldb```

5. Acessar pasta do projeto e rodar:
```
mvn clean package
docker build -t todolistapi .
docker run -p 8081:8081 --name todolistapi8081  --net spring-net -e MYSQL_HOST=mysqldb -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_PORT=3306 -e SERVER_PORT=8081 -d --restart unless-stopped todolistapi
docker run -p 8082:8082 --name todolistapi8082  --net spring-net -e MYSQL_HOST=mysqldb -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_PORT=3306 -e SERVER_PORT=8082 -d --restart unless-stopped todolistapi
docker run -p 8083:8083 --name todolistapi8083  --net spring-net -e MYSQL_HOST=mysqldb -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_PORT=3306 -e SERVER_PORT=8083 -d --restart unless-stopped todolistapi
```

6. Ainda na pasta do projeto, executar o nginx (caso esteja em um SO diferente de windows, baixar o [NGINX](https://nginx.org/en/download.html), configura-lo da mesma que no arquivo [\nginx\conf\nginx.conf](.\nginx\conf\nginx.conf)):
```
cd .\nginx\
.\nginx.exe
```


