
# Todo List
Uma TodoList é, como o nome indica, uma lista de tarefas/atividades/notas que precisam ser gerenciadas e realizadas. Essas tarefas podem conter e ser organizadas em função de suas datas, títulos, descrição e/ou outros detalhes que sejam relevantes para o contexto.
A aplicação se trata de um sistema com arquitetura híbrida (com recursos distribuídos e centralizados) de gerenciamento de tarefas (Todo List). A aplicação permite que os usuários adicionem, editem e excluam tarefas em uma interface amigável. Os usuários podem manter as tarefas criadas em sua conta, compartilhá-las com outros usuários e visualizar tarefas que a ele foram compartilhadas.

## Para executar com o docker no windows:

 1. Instalar o docker em sua maquina

 2. Criar subnet do mysqlcluster
```docker network create --subnet=194.164.0.0/16 mysql-cluster```

3. Criar rede para conexão entre os containers
```docker network create spring-net```

4. Acessar pasta do projeto

5. Criar imagem do mysqlcluster
```docker build -t mysql-docker/mysql-cluster mysql-docker/mysql-cluster/8.0```

6. Criar container do nodo gerenciador, os nodos de dados e os nodos que executam o mysql
```
docker run -d --net=mysql-cluster --name=management1 --ip=194.164.0.2 mysql-docker/mysql-cluster ndb_mgmd
docker run -d --net=mysql-cluster --name=ndb1 --ip=194.164.0.3 mysql/mysql-cluster ndbd --ndb-connectstring=194.164.0.2
docker run -d --net=mysql-cluster --name=ndb2 --ip=194.164.0.4 mysql/mysql-cluster ndbd --ndb-connectstring=194.164.0.2
docker run -d --net=mysql-cluster --name=mysql1 --ip=194.164.0.10 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=todolist mysql/mysql-cluster --ndb-connectstring=194.164.0.2 
docker run -d --net=mysql-cluster --name=mysql2 --ip=194.164.0.9 -e MYSQL_ROOT_PASSWORD=root mysql/mysql-cluster --ndb-connectstring=194.164.0.2
```

7. Conecte o mysql na spring-net
```
docker network connect spring-net mysql1
docker network connect spring-net mysql2
```

8. Conceda acesso aos usuários remotos no mysql1 (usar senha "root"):
```
docker exec -it mysql1 mysql -uroot -p
CREATE USER 'root'@'todolistapi8081.spring-net' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'todolistapi8081.spring-net';
CREATE USER 'root'@'todolistapi8082.spring-net' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'todolistapi8082.spring-net';
CREATE USER 'root'@'todolistapi8083.spring-net' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'todolistapi8083.spring-net';
```

Quando finalizar, execute o comando "quit"

9. Conceda acesso aos usuários remotos no mysql2 (usar senha "root"):
```
docker exec -it mysql2 mysql -uroot -p

GRANT ALL PRIVILEGES ON *.* TO 'root'@'todolistapi8081.spring-net';

GRANT ALL PRIVILEGES ON *.* TO 'root'@'todolistapi8082.spring-net';

GRANT ALL PRIVILEGES ON *.* TO 'root'@'todolistapi8083.spring-net';
```

Quando finalizar, execute o comando "quit"

10. Executar a aplicação
```
mvn clean package
docker build -t todolistapi .
docker run -p 8081:8081 --name todolistapi8081  --net spring-net -e MYSQL_HOST=mysql1 -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_PORT=3306 -e SERVER_PORT=8081 -d --restart unless-stopped todolistapi
docker run -p 8082:8082 --name todolistapi8082  --net spring-net -e MYSQL_HOST=mysql2 -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_PORT=3306 -e SERVER_PORT=8082 -d --restart unless-stopped todolistapi
docker run -p 8083:8083 --name todolistapi8083  --net spring-net -e MYSQL_HOST=mysql1 -e MYSQL_USER=root -e MYSQL_PASSWORD=root -e MYSQL_PORT=3306 -e SERVER_PORT=8083 -d --restart unless-stopped todolistapi
```


11. Ainda na pasta do projeto, executar o nginx (caso esteja em um SO diferente de windows, baixar o [NGINX](https://nginx.org/en/download.html), configura-lo da mesma que no arquivo [\nginx\conf\nginx.conf](./nginx/conf/nginx.conf)):
```
cd .\nginx\
.\nginx.exe
```





