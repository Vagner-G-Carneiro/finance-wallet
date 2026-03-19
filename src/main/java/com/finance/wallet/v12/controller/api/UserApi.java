package com.finance.wallet.v12.controller.api;

import com.finance.wallet.v12.domain.User;
import com.finance.wallet.v12.dto.request.UserChangePasswordRequestDTO;
import com.finance.wallet.v12.dto.request.UserCreateDTO;
import com.finance.wallet.v12.dto.request.UserDeleteRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public interface UserApi {

    @PostMapping("/register")
    default ResponseEntity<?> create(@RequestBody @Valid UserCreateDTO data, UriComponentsBuilder uriComponentsBuilder){
        throw new UnsupportedOperationException("Endpoint not implemented yeld");
    };

    @PutMapping("/change_password")
    default ResponseEntity<?> changePassword (@RequestBody @Valid UserChangePasswordRequestDTO userRequest,
                                                                         @AuthenticationPrincipal User loggedUser) {
        throw new UnsupportedOperationException("Endpoint not implemented yeld");
    }

    @DeleteMapping("/delete")
    default ResponseEntity<?> delete (@RequestBody @Valid UserDeleteRequestDTO userDeleteRequestDTO,
                                                          @AuthenticationPrincipal User loggedUser) {
        throw new UnsupportedOperationException("Endpoint not implemented yeld");
    }

    @GetMapping("/me")
    default ResponseEntity<?> returnsUser(@AuthenticationPrincipal User user, UriComponentsBuilder uriComponentsBuilder) {
        throw new UnsupportedOperationException("Endpoint not implemented yeld");
    }
}
