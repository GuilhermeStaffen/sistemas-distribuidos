# Todo List

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).

npm run build
docker build -t todolist-front .
docker run -it -p 8081:80 --name todolist-front-1 -d --restart unless-stopped --net spring-net todolist-front
docker run -it -p 8082:80 --name todolist-front-2 -d --restart unless-stopped --net spring-net todolist-front
docker run -it -p 8083:80 --name todolist-front-3 -d --restart unless-stopped --net spring-net todolist-front