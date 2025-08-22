package br.com.rb.api.interfaces.controller;

import br.com.rb.api.application.dto.auth.LoginDTO;
import br.com.rb.api.application.dto.auth.TokenDTO;
import br.com.rb.api.application.dto.user.CreateUserDTO;
import br.com.rb.api.application.dto.user.UserDetailsDTO;
import br.com.rb.api.domain.model.User;
import br.com.rb.api.application.service.UserService;
import br.com.rb.api.infrastructure.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class AuthenticationController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager manager, TokenService tokenService, UserService userService) {
        this.manager = manager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO dto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenDTO(tokenJWT));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDetailsDTO> registerStudent(@RequestBody @Valid CreateUserDTO dto, UriComponentsBuilder uriBuilder) {
        UserDetailsDTO newUserDto = userService.registerStudent(dto);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(newUserDto.id()).toUri();
        return ResponseEntity.created(uri).body(newUserDto);
    }
}