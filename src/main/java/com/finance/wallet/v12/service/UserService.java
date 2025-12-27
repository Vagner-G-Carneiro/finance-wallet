package com.finance.wallet.v12.service;

import com.finance.wallet.v12.domain.User;
import com.finance.wallet.v12.domain.Wallet;
import com.finance.wallet.v12.domain.WalletStatus;
import com.finance.wallet.v12.dto.request.UserCreateDTO;
import com.finance.wallet.v12.dto.request.UserDeleteRequestDTO;
import com.finance.wallet.v12.dto.response.UserDeleteResponseDTO;
import com.finance.wallet.v12.dto.response.UserResponseDTO;
import com.finance.wallet.v12.infra.exceptions.V12UserException;
import com.finance.wallet.v12.infra.exceptions.V12WalletException;
import com.finance.wallet.v12.repository.UserRepository;
import com.finance.wallet.v12.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

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
        newUser.setActive(true);
        newUser.setTokenValidSince(Instant.now());
        User savedUser = this.userRepository.save(newUser);
        Wallet newWallet = new Wallet();
        newWallet.setUser(savedUser);
        newWallet.setBalance(BigDecimal.ZERO);
        newWallet.setWalletStatus(WalletStatus.ACTIVE);
        this.walletRepository.save(newWallet);

        return UserResponseDTO.fromEntity(savedUser);
    }

    @Transactional
    public UserDeleteResponseDTO delete(UserDeleteRequestDTO userDTO)
    {
        User user = this.userRepository.findByEmail(userDTO.email())
                .orElseThrow(() -> V12UserException.notFound("Erro ao encontrar usuário " + userDTO.email()));
        if(user.isActive())
        {
            Wallet wallet = this.walletRepository.findByUserId(user.getId())
                    .orElseThrow(() -> V12WalletException.notFound("Erro ao encontrar carteira de usuário."));

            if(wallet.getBalance().compareTo(BigDecimal.ZERO) > 0)
            {
                throw V12WalletException.businessRule("Para excluir sua conta, primeiro precisa zerar ou tranferir o saldo em carteira.");
            }

            wallet.setWalletStatus(WalletStatus.CLOSED);
            user.setActive(false);
            this.walletRepository.save(wallet);
            this.userRepository.save(user);
            return new UserDeleteResponseDTO("Usuário " + user.getEmail() + " deletado com sucesso!", true);
        }
        throw V12UserException.businessRule("Usuário não encontrado.");
    }
}
