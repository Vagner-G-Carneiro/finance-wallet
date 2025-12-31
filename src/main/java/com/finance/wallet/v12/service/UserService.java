package com.finance.wallet.v12.service;

import com.finance.wallet.v12.domain.Money;
import com.finance.wallet.v12.domain.User;
import com.finance.wallet.v12.domain.Wallet;
import com.finance.wallet.v12.domain.WalletStatus;
import com.finance.wallet.v12.dto.request.UserChangePasswordRequestDTO;
import com.finance.wallet.v12.dto.request.UserCreateDTO;
import com.finance.wallet.v12.dto.request.UserDeleteRequestDTO;
import com.finance.wallet.v12.dto.response.UserChangePasswordResponseDTO;
import com.finance.wallet.v12.dto.response.UserDeleteResponseDTO;
import com.finance.wallet.v12.dto.response.UserResponseDTO;
import com.finance.wallet.v12.infra.exceptions.V12SecurityException;
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

    public UserService(UserRepository userRepository, WalletRepository walletRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO create(UserCreateDTO userCreateDTO) {
        if (this.userRepository.findByCpf(userCreateDTO.cpf()).isPresent()) {
            throw V12UserException.businessRule("Este cpf já está cadastrado.");
        }
        if (this.userRepository.findByEmail(userCreateDTO.email()).isPresent()) {
            throw V12UserException.businessRule("Este email já está cadastrado.");
        }

        String hashPassword = passwordEncoder.encode(userCreateDTO.password());
        User newUser = User.createUser(userCreateDTO.name(), userCreateDTO.cpf(), userCreateDTO.email(), hashPassword);
        User savedUser = this.userRepository.save(newUser);

        Wallet newWallet = Wallet.createWallet(savedUser);
        this.walletRepository.save(newWallet);

        return UserResponseDTO.fromEntity(savedUser);
    }

    @Transactional
    public UserDeleteResponseDTO delete(UserDeleteRequestDTO userDTO, User loggedUser) {
        User user = this.userRepository.findByEmailAndActiveTrue(userDTO.email())
                .orElseThrow(() -> V12UserException.notFound("Erro ao encontrar usuário " + userDTO.email()));

        if (!user.getId().equals(loggedUser.getId())) {
            throw V12SecurityException.businessRule("Ação de deleção negada!");
        }

        Wallet wallet = this.walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> V12WalletException.notFound("Erro ao encontrar carteira de usuário."));

        if (wallet.getBalance().compareTo(Money.zero()) > 0) {
            throw V12WalletException.businessRule("Para excluir sua conta, primeiro precisa zerar ou tranferir o saldo em carteira.");
        }

        wallet.setWalletStatus(WalletStatus.CLOSED);
        user.setActive(false);
        this.walletRepository.save(wallet);
        this.userRepository.save(user);
        return new UserDeleteResponseDTO("Usuário " + user.getEmail() + " deletado com sucesso!", true);
    }

    @Transactional
    public UserChangePasswordResponseDTO changePassword(UserChangePasswordRequestDTO userRequest, User loggedUser) {
        if (!userRequest.password().equals(userRequest.confirmPassword())) {
            throw V12SecurityException.businessRule("'Senha' e 'Confirmação de senha' não são iguais, revise e tente novamente.");
        }

        User passwordUser = this.userRepository.findByEmailAndActiveTrue(userRequest.email()).
                orElseThrow(() -> V12UserException.notFound("Erro ao encontrar usuário"));

        if (!passwordUser.getId().equals(loggedUser.getId())) {
            throw V12SecurityException.businessRule("Troca de senha negada!");
        }

        if (!passwordEncoder.matches(userRequest.password(), passwordUser.getPassword())) {
            throw V12SecurityException.businessRule("Senha inválida! Tente novamente.");
        }

        passwordUser.setPassword(passwordEncoder.encode(userRequest.newPassword()));
        Instant now = Instant.now();
        passwordUser.setTokenValidSince(now);
        this.userRepository.save(passwordUser);
        return new UserChangePasswordResponseDTO("Troca de senha concluída com sucesso!", now);
    }
}
