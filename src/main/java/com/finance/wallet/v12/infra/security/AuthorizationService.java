package com.finance.wallet.v12.infra.security;

import com.finance.wallet.v12.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService{

    private UserRepository userRepository;
    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmailAndActiveTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }
}
