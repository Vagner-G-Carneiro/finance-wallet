package com.finance.wallet.v12.controller;

import com.finance.wallet.v12.domain.User;
import com.finance.wallet.v12.dto.request.LoginRequestDTO;
import com.finance.wallet.v12.dto.response.LoginResponseDTO;
import com.finance.wallet.v12.infra.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService){
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO)
    {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken
                (loginRequestDTO.email(), loginRequestDTO.password());
        Authentication authentication = this.authenticationManager.authenticate(usernamePassword);
        //Se não caiu em exception na linha de cima, o login passou
        String token = this.tokenService.generateToken((User)authentication.getPrincipal());

        return new ResponseEntity<>(new LoginResponseDTO(token), HttpStatus.OK);
    }

}
