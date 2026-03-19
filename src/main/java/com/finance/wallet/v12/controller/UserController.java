package com.finance.wallet.v12.controller;

import com.finance.wallet.v12.controller.api.UserApi;
import com.finance.wallet.v12.domain.User;
import com.finance.wallet.v12.dto.request.UserChangePasswordRequestDTO;
import com.finance.wallet.v12.dto.request.UserCreateDTO;
import com.finance.wallet.v12.dto.request.UserDeleteRequestDTO;
import com.finance.wallet.v12.dto.response.UserChangePasswordResponseDTO;
import com.finance.wallet.v12.dto.response.UserDeleteResponseDTO;
import com.finance.wallet.v12.dto.response.UserResponseDTO;
import com.finance.wallet.v12.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserCreateDTO data,
                                                  UriComponentsBuilder uriComponentsBuilder)
    {
        var user = userService.create(data);
        URI uri = uriComponentsBuilder
                .path("/user/{id}")
                .buildAndExpand(data.email())
                .toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @Override
    public ResponseEntity<UserChangePasswordResponseDTO> changePassword (@RequestBody @Valid UserChangePasswordRequestDTO request,
                                                                         @AuthenticationPrincipal User loggedUser)
    {
        var userResponse = this.userService.changePassword(request, loggedUser);
        return ResponseEntity.ok(userResponse);
    }

    @Override
    public ResponseEntity<UserDeleteResponseDTO> delete (@RequestBody @Valid UserDeleteRequestDTO userDeleteRequestDTO,
                                                         @AuthenticationPrincipal User loggedUser)
    {
        var response = this.userService.delete(userDeleteRequestDTO, loggedUser);
        return ResponseEntity.ok(response);
    }

    /*
     *  TODO Entendo que em uma api real, esse endpoint seria um problema grave de segurança, não e função
     *   da api retornar o usuario logado e sim apenas logar ou retornar que a sessao expirou, quem tem
     *   acesso aos dados do usuario e o front via o token JWT que vc gera nesse token vc pode colocar o nome do usuario
     *   e mais informações que achar melhor, ele ja fica protegido e nao exposto.
     */
    @Deprecated
    @Override
    public ResponseEntity<UserResponseDTO> returnsUser(@AuthenticationPrincipal User user, UriComponentsBuilder uriComponentsBuilder)
    {

        return ResponseEntity.ok(UserResponseDTO.fromEntity(user));
    }
}
