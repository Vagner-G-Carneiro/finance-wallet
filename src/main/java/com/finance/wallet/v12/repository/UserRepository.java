package com.finance.wallet.v12.repository;

import com.finance.wallet.v12.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByCpf(String cpf);
    Optional<User> findByEmail(String email);
}
