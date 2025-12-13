package com.finance.wallet.v12.controller;

import com.finance.wallet.v12.domain.User;
import com.finance.wallet.v12.dto.request.UserCreateDTO;
import com.finance.wallet.v12.dto.response.UserResponseDTO;
import com.finance.wallet.v12.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserCreateDTO data, UriComponentsBuilder uriComponentsBuilder)
    {
        UserResponseDTO newUser = this.userService.create(data);
        URI uri = uriComponentsBuilder
                .path("/user/{id}")
                .buildAndExpand(newUser.email())
                .toUri();
        return ResponseEntity.created(uri).body(newUser);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> returnsUser(@AuthenticationPrincipal User user, UriComponentsBuilder uriComponentsBuilder)
    {
        return new ResponseEntity<>(UserResponseDTO.fromEntity(user), HttpStatus.OK);
    }
}
