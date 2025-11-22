package com.finance.wallet.v12.service;

import com.finance.wallet.v12.domain.User;
import com.finance.wallet.v12.domain.Wallet;
import com.finance.wallet.v12.dto.UserCreateDTO;
import com.finance.wallet.v12.dto.UserResponseDTO;
import com.finance.wallet.v12.repository.UserRepository;
import com.finance.wallet.v12.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private WalletRepository walletRepository;
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO create(UserCreateDTO userCreateDTO)
    {
        if(this.userRepository.findByCpf(userCreateDTO.cpf()).isPresent()) {
            throw new RuntimeException("Este cpf já está cadastrado.");
        }
        if(this.userRepository.findByEmail(userCreateDTO.email()).isPresent()) {
            throw new RuntimeException("Este email já está cadastrado.");
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

}
