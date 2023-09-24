Para rodar em container:

Baixar e iniciar a imagem do mysql
-docker pull mysql
-docker run -d -p 3307:3306 --name mysqldb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=todolist mysql

Criar rede para conexão entre os containers
-docker network create spring-net


Conectar containers nesta rede criada, usando o comando
-docker network connect spring-net mysqldb

Acessar pasta do projeto e rodar
-mvn clean package
-docker build -t todolistapi .
-docker run -p 8081:8081 --name todolistapi8081  --net spring-net -e MYSQL_HOST=mysqldb -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_PORT=3306 -e SERVER_PORT=8081 -d --restart unless-stopped todolistapi
-docker run -p 8082:8082 --name todolistapi8082  --net spring-net -e MYSQL_HOST=mysqldb -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_PORT=3306 -e SERVER_PORT=8082 -d --restart unless-stopped todolistapi
-docker run -p 8083:8083 --name todolistapi8083  --net spring-net -e MYSQL_HOST=mysqldb -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_PORT=3306 -e SERVER_PORT=8083 -d --restart unless-stopped todolistapi
