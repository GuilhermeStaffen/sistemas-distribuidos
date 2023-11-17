
# To Do List - Back
Se você já executou as pré-dependências descritas no "README.MD na pasta raiz, podemos iniciar o back-end.

## Para executar com o docker no windows:

1. Acessar pasta do projeto

2. Criar imagem do mysqlcluster
```docker build -t mysql-docker/mysql-cluster mysql-docker/mysql-cluster/8.0```

3. Criar container do nodo gerenciador, os nodos de dados e os nodos que executam o mysql
```
docker run -d --net=mysql-cluster --name=management1 --ip=194.164.0.2 mysql-docker/mysql-cluster ndb_mgmd
docker run -d --net=mysql-cluster --name=ndb1 --ip=194.164.0.3 mysql/mysql-cluster ndbd --ndb-connectstring=194.164.0.2
docker run -d --net=mysql-cluster --name=ndb2 --ip=194.164.0.4 mysql/mysql-cluster ndbd --ndb-connectstring=194.164.0.2
docker run -d --net=mysql-cluster --name=mysql1 --ip=194.164.0.10 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=todolist mysql/mysql-cluster --ndb-connectstring=194.164.0.2 
docker run -d --net=mysql-cluster --name=mysql2 --ip=194.164.0.9 -e MYSQL_ROOT_PASSWORD=root mysql/mysql-cluster --ndb-connectstring=194.164.0.2
```

4. Conecte o mysql na spring-net
```
docker network connect spring-net mysql1
docker network connect spring-net mysql2
```

5. Conceda acesso aos usuários remotos no mysql1 (usar senha "root"):
```
docker exec -it mysql1 mysql -uroot -p
CREATE USER 'root'@'todolistapi9091.spring-net' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'todolistapi9091.spring-net';
CREATE USER 'root'@'todolistapi9092.spring-net' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'todolistapi9092.spring-net';
CREATE USER 'root'@'todolistapi9093.spring-net' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'todolistapi9093.spring-net';
```

Quando finalizar, execute o comando "quit"

6. Conceda acesso aos usuários remotos no mysql2 (usar senha "root"):
```
docker exec -it mysql2 mysql -uroot -p

GRANT ALL PRIVILEGES ON *.* TO 'root'@'todolistapi9091.spring-net';

GRANT ALL PRIVILEGES ON *.* TO 'root'@'todolistapi9092.spring-net';

GRANT ALL PRIVILEGES ON *.* TO 'root'@'todolistapi9093.spring-net';
```

Quando finalizar, execute o comando "quit"

7. Executar a aplicação
```
mvn clean package
docker build -t todolistapi .
docker run -p 9091:9091 --name todolistapi9091  --net spring-net -e MYSQL_HOST=mysql1 -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_PORT=3306 -e SERVER_PORT=9091 -d --restart unless-stopped todolistapi
docker run -p 9092:9092 --name todolistapi9092  --net spring-net -e MYSQL_HOST=mysql2 -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_PORT=3306 -e SERVER_PORT=9092 -d --restart unless-stopped todolistapi
docker run -p 9093:9093 --name todolistapi9093  --net spring-net -e MYSQL_HOST=mysql1 -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_PORT=3306 -e SERVER_PORT=9093 -d --restart unless-stopped todolistapi
```








