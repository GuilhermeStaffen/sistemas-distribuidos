
# To Do List - Front
Para iniciar, se você já executou as pré-dependências descritas no "README.MD na pasta raiz, precisamos acessar a pasta do front e instalar as dependências do projeto:

```npm install```

Após isso, basta buildar a aplicação:

```npm run build```


Em seguida, devemos construir a imagem do docker

```docker build -t todolist-front .```

  

Por fim, basta iniciarmos as três instâncias do frontend (nas portas 8081, 8082 e 8083):
```
docker run -it -p 8081:80 --name todolist-front-1 -d --restart unless-stopped --net spring-net todolist-front
docker run -it -p 8082:80 --name todolist-front-2 -d --restart unless-stopped --net spring-net todolist-front
docker run -it -p 8083:80 --name todolist-front-3 -d --restart unless-stopped --net spring-net todolist-front
```