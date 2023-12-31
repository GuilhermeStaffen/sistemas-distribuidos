package com.todolist.springjwt.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.todolist.springjwt.models.User;
import com.todolist.springjwt.payload.request.CreateTodoRequest;
import com.todolist.springjwt.payload.request.ShareTodoRequest;
import com.todolist.springjwt.payload.request.UpdateTodoRequest;
import com.todolist.springjwt.payload.response.MessageResponse;
import com.todolist.springjwt.payload.response.TodoResponse;
import com.todolist.springjwt.models.Todo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.todolist.springjwt.repository.TodoRepository;
import com.todolist.springjwt.repository.UserRepository;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoRepository todoRepository;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createTodo(@Valid @RequestBody CreateTodoRequest todoRequest) {
        System.out.println("");
        logger.info("Iniciando processo de criacao de tarefa: " + todoRequest);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            logger.error("Usuario nao encontrado");
            logger.info("Finalizando processo de criacao de tarefa");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Usuário não encontrado."));
        }

        logger.info("Criando tarefa");
        Todo todo = new Todo();
        todo.setTitle(todoRequest.getTitle());
        todo.setDescription(todoRequest.getDescription());
        todo.setActive(true);
        todo.setIdUserLastChange(user.getId());
        todo.setDateLastChange(getTime());
        todo.getUsersWithAccess().add(user);

        Todo savedTodo = todoRepository.save(todo);

        logger.info("Finalizando processo de criacao de tarefa");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("Tarefa #" + savedTodo.getId() + " criada com sucesso!"));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getActiveTodosWithAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("");
        logger.info("Iniciando processo de listagem de tarefas de " + authentication.getName());
        List<Object[]> todoResults = todoRepository.findActiveTodosWithAccess(username);

        List<Map<String, Object>> todosAsJson = new ArrayList<>();
        for (Object[] result : todoResults) {
            Map<String, Object> todoMap = new HashMap<>();
            todoMap.put("id", result[0]);
            todoMap.put("title", result[1]);
            todoMap.put("description", result[2]);
            todoMap.put("active", result[3]);
            todosAsJson.add(todoMap);
        }

        Map<String, List<Map<String, Object>>> response = new HashMap<>();
        response.put("activedTodos", todosAsJson);

        logger.info("Finalizando processo de listagem de tarefas");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/done")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getInactiveTodosWithAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("");
        logger.info("Iniciando processo de listagem de tarefas inativas/concluidas de " + authentication.getName());
        List<Object[]> todoResults = todoRepository.findInactiveTodosWithAccess(username);

        List<Map<String, Object>> todosAsJson = new ArrayList<>();
        for (Object[] result : todoResults) {
            Map<String, Object> todoMap = new HashMap<>();
            todoMap.put("id", result[0]);
            todoMap.put("title", result[1]);
            todoMap.put("description", result[2]);
            todoMap.put("active", result[3]);
            todosAsJson.add(todoMap);
        }

        Map<String, List<Map<String, Object>>> response = new HashMap<>();
        response.put("inactivedTodos", todosAsJson);

        logger.info("Finalizando processo de listagem de tarefas inativas/canceladas");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTodo(@PathVariable Long id) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        User currentUser = userRepository.findByUsername(user);
        System.out.println("");
        logger.info("Iniciando processo de buscar tarefa " + id);
        Todo todoResult = todoRepository.findById(id).orElse(null);
        if (todoResult == null) {
            logger.error("Tarefa nao existe");
            logger.info("Finalizando processo de buscar tarefa");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Não foi possível buscar tarefa #" + id
                            + " | Tarefa não encontrada!"));
        }
        if (!todoResult.getUsersWithAccess().contains(currentUser)) {
            logger.error("Usuario nao tem acesso a tarefa");
            logger.info("Finalizando processo de buscar tarefa");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Não foi possível buscar tarefa #" + id
                            + " | Sem permissão para buscar tarefa!"));
        }

        Date openDate = getTime();

        logger.info("Finalizando processo de busca da tarefaa");
        return ResponseEntity.ok(new TodoResponse(
                todoResult.getId(),
                todoResult.getTitle(),
                todoResult.getDescription(),
                openDate));
    }

    @PostMapping("/share")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> shareTodoWithUser(@Valid @RequestBody ShareTodoRequest todoRequest) {
        System.out.println("");
        logger.info("Iniciando processo de compartilhamento de tarefa: " + todoRequest);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();

        User currentUser = userRepository.findByUsername(user);
        Todo todoToShare = todoRepository.findById(todoRequest.getId()).orElse(null);
        if (currentUser == null || todoToShare == null) {
            logger.error("Usuario ou tarefa nao existe");
            logger.info("Finalizando processo de compartilhamento de tarefa");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Não foi possível compartilhar tarefa #" + todoRequest.getId()
                            + " | Usuário ou Tarefa não encontrada!"));
        }
        if (!todoToShare.getUsersWithAccess().contains(currentUser)) {
            logger.error("Usuario nao tem acesso a tarefa");
            logger.info("Finalizando processo de compartilhamento de tarefa");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Não foi possível compartilhar tarefa #" + todoRequest.getId()
                            + " | Sem permissão para compartilhar tarefa!"));
        }

        User userToShareWith = userRepository.findByEmail(todoRequest.getEmail());
        if (userToShareWith == null) {
            logger.error("Usuario de destino nao existe");
            logger.info("Finalizando processo de compartilhamento de tarefa");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Não foi possível compartilhar tarefa #" + todoToShare.getId()
                            + " | Não localizamos o usuário com quem deseja compartilhar"));
        }

        todoToShare.getUsersWithAccess().add(userToShareWith);
        todoRepository.save(todoToShare);
        logger.info("Finalizando processo de compartilhamento de tarefa");
        return ResponseEntity.ok(new MessageResponse("Tarefa #" + todoToShare.getId() + " compartilhada com sucesso!"));

    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateTodo(@PathVariable Long id, @RequestBody UpdateTodoRequest updateRequest) {
        System.out.println("");
        logger.info("Iniciando processo de edicao de tarefa: " + updateRequest);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();

        User currentUser = userRepository.findByUsername(user);
        Todo existingTodo = todoRepository.findById(id).orElse(null);

        if (currentUser == null || existingTodo == null || !existingTodo.getUsersWithAccess().contains(currentUser)) {
            logger.error("Tarefa|Usuario nao encontrada ou usuario sem permissao");
            logger.info("Finalizando processo de edicao de tarefa");
            return ResponseEntity.notFound().build();
        }

        if (updateRequest.getChangeDate().compareTo(existingTodo.getDateLastChange()) < 0
                && currentUser.getId() != existingTodo.getIdUserLastChange()) {
            logger.error("Tarefa foi editada por outro usuário");
            logger.info("Finalizando processo de edicao de tarefa");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Tarefa foi editada por outro usuário, por favor atualize a página"));
        }

        existingTodo.setTitle(updateRequest.getTitle());
        existingTodo.setDescription(updateRequest.getDescription());
        existingTodo.setDateLastChange(getTime());
        existingTodo.setIdUserLastChange(currentUser.getId());

        todoRepository.save(existingTodo);
        logger.info("Finalizando processo de edicao de tarefa");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("Tarefa #" + existingTodo.getId() + " editada com sucesso!"));
    }

    @PostMapping("/{id}/done")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> markTodoAsDone(@PathVariable Long id) {
        System.out.println("");
        logger.info("Iniciando processo de conclusao de tarefa: " + id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();

        User currentUser = userRepository.findByUsername(user);
        Todo existingTodo = todoRepository.findById(id).orElse(null);

        if (currentUser == null || existingTodo == null || !existingTodo.getUsersWithAccess().contains(currentUser)) {
            logger.error("Tarefa|Usuario nao encontrada ou usuario sem permissao");
            logger.info("Finalizando processo de conclusao de tarefa");
            return ResponseEntity.notFound().build();
        }

        existingTodo.setActive(false);

        todoRepository.save(existingTodo);
        logger.info("Finalizando processo de conclusao de tarefa");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("Tarefa #" + existingTodo.getId() + " marcada como concluída!"));
    }

    @PostMapping("/{id}/open")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> markTodoAsOpen(@PathVariable Long id) {
        System.out.println("");
        logger.info("Iniciando processo de reabertura de tarefa: " + id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();

        User currentUser = userRepository.findByUsername(user);
        Todo existingTodo = todoRepository.findById(id).orElse(null);

        if (currentUser == null || existingTodo == null || !existingTodo.getUsersWithAccess().contains(currentUser)) {
            logger.error("Tarefa|Usuario nao encontrada ou usuario sem permissao");
            logger.info("Finalizando processo de reabertura de tarefa");
            return ResponseEntity.notFound().build();
        }

        existingTodo.setActive(true);

        todoRepository.save(existingTodo);
        logger.info("Finalizando processo de reabertura de tarefa");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("Tarefa #" + existingTodo.getId() + " reaberta!"));
    }

    public static Date getTime() {
        try {
            URL obj = new URL("https://relogio.ntp.br/horacerta/s.php?zone=-3");
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                String[] parts = response.toString().split("#");
                long timestamp = Long.parseLong(parts[1]);
                Instant instant = Instant.ofEpochMilli(timestamp);
                return Date.from(instant);
            } else {
                logger.error("Erro ao buscar data/hora");
                logger.info("Utilizando data/hora local");
                return new Date();
            }
        } catch (

        Exception e) {
            e.printStackTrace();
            logger.error("Erro ao buscar data/hora");
            logger.info("Utilizando data/hora local");
            return new Date();
        }
    }

}