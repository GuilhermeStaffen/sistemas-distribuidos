package com.todolist.springjwt.controllers;

import com.todolist.springjwt.models.User;
import com.todolist.springjwt.payload.request.CreateTodoRequest;
import com.todolist.springjwt.payload.request.ShareTodoRequest;
import com.todolist.springjwt.payload.response.MessageResponse;
import com.todolist.springjwt.models.Todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.todolist.springjwt.repository.TodoRepository;
import com.todolist.springjwt.repository.UserRepository;
import com.todolist.springjwt.security.services.UserDetailsServiceImpl;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoRepository todoRepository;

    private UserDetailsServiceImpl userDetailsService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createTodo(@Valid @RequestBody CreateTodoRequest todoRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Usuário não encontrado."));
        }
        Todo todo = new Todo();
        todo.setTitle(todoRequest.getTitle());
        todo.setDescription(todoRequest.getDescription());
        todo.setActive(true);
        todo.getUsersWithAccess().add(user);

        Todo savedTodo = todoRepository.save(todo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("Tarefa #" + savedTodo.getId() + " criada com sucesso!"));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> getActiveTodosWithAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

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
        response.put("todos", todosAsJson);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/share")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> shareTodoWithUser(@Valid @RequestBody ShareTodoRequest todoRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();

        User currentUser = userRepository.findByUsername(user);
        Todo todoToShare = todoRepository.findById(todoRequest.getId()).orElse(null);

        if (currentUser == null || todoToShare == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível compartilhar tarefa #" + todoToShare.getId() + " | Usuário ou Tarefa não encontrada!");
        }else if (!todoToShare.getUsersWithAccess().contains(currentUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível compartilhar tarefa #" + todoToShare.getId() + " | Sem permissão para compartilhar tarefa!");
        }

        User userToShareWith = userRepository.findByEmail(todoRequest.getEmail());
        if (userToShareWith == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível compartilhar tarefa #" + todoToShare.getId() + " | Não localizamos o usuário com quem deseja compartilhar");
        }
        todoToShare.getUsersWithAccess().add(userToShareWith);
        todoRepository.save(todoToShare);
        return ResponseEntity.ok(new MessageResponse("Tarefa #" + todoToShare.getId() + " compartilhada com sucesso!"));

    }
}