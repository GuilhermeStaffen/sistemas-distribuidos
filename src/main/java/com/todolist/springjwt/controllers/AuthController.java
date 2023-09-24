package com.todolist.springjwt.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.springjwt.models.ERole;
import com.todolist.springjwt.models.Role;
import com.todolist.springjwt.models.User;
import com.todolist.springjwt.payload.request.LoginRequest;
import com.todolist.springjwt.payload.request.SignupRequest;
import com.todolist.springjwt.payload.response.JwtResponse;
import com.todolist.springjwt.payload.response.MessageResponse;
import com.todolist.springjwt.repository.RoleRepository;
import com.todolist.springjwt.repository.UserRepository;
import com.todolist.springjwt.security.jwt.JwtUtils;
import com.todolist.springjwt.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    System.out.println("");
    logger.info("Iniciando processo de login: "+loginRequest);

    User user = userRepository.findByEmail(loginRequest.getEmail());
    
    if (user == null) {
      logger.error("Usuário nao encontrado");
      logger.info("Finalizando processo de login");
      return ResponseEntity.badRequest().body(new MessageResponse("Usuário ou senha inválidos"));
    }

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    logger.info("Finalizando processo de login");

    return ResponseEntity.ok(new JwtResponse(jwt,
        userDetails.getId(),
        userDetails.getUsername(),
        userDetails.getEmail(),
        roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    System.out.println("");
    logger.info("Iniciando processo de criação de conta: "+signUpRequest);

    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      logger.error("Nome de usuário "+signUpRequest.getUsername()+" ja esta em uso!");
      logger.info("Finalizando processo de criacao de conta");
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Nome de usuário já esta em uso!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      logger.error("Email "+signUpRequest.getEmail()+" ja esta em uso!");
      logger.info("Finalizando processo de criacao de conta");
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Email já esta em uso!"));
    }

    User user = new User(signUpRequest.getUsername(),
        signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()));

    Set<Role> roles = new HashSet<>();
    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
        .orElseThrow(() -> new RuntimeException("Permissão não encontrada."));
    roles.add(userRole);

    user.setRoles(roles);
    userRepository.save(user);
    logger.info("Finalizando processo de criacao de conta");
    return ResponseEntity.ok(new MessageResponse("Usuário criado com sucesso!"));
  }
}
