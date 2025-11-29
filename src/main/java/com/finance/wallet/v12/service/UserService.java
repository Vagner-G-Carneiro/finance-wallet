package com.finance.wallet.v12.service;

import com.finance.wallet.v12.domain.User;
import com.finance.wallet.v12.domain.Wallet;
import com.finance.wallet.v12.dto.request.UserCreateDTO;
import com.finance.wallet.v12.dto.response.UserResponseDTO;
import com.finance.wallet.v12.infra.exceptions.V12UserException;
import com.finance.wallet.v12.repository.UserRepository;
import com.finance.wallet.v12.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, WalletRepository walletRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO create(UserCreateDTO userCreateDTO)
    {
        if(this.userRepository.findByCpf(userCreateDTO.cpf()).isPresent()) {
            throw V12UserException.businessRule("Este cpf já está cadastrado.");
        }
        if(this.userRepository.findByEmail(userCreateDTO.email()).isPresent()) {
            throw V12UserException.businessRule("Este email já está cadastrado.");
        }

        User newUser = new User();
        newUser.setCpf(userCreateDTO.cpf());
        newUser.setName(userCreateDTO.name());
        newUser.setEmail(userCreateDTO.email());
        newUser.setPassword(passwordEncoder.encode(userCreateDTO.password()));

        User savedUser = this.userRepository.save(newUser);
        Wallet newWallet = new Wallet();
        newWallet.setUser(savedUser);
        newWallet.setBalance(BigDecimal.ZERO);
        this.walletRepository.save(newWallet);

        return UserResponseDTO.fromEntity(savedUser);
    }

    public User findUser(UUID userId)
    {
        return this.userRepository.findById(userId).orElseThrow(() ->
                V12UserException.notFound("Erro ao encontrar usuário solicitado."));
    }

    /*@Transactional
    public */

}
