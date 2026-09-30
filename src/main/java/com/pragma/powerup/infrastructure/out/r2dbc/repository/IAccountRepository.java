package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.AccountCredentialsEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Mono;

public interface IAccountRepository extends Repository<AccountCredentialsEntity, Long> {
    @Query("""
            SELECT u.id, u.email, u.password_hash, r.name AS role
            FROM users u JOIN roles r ON r.id = u.role_id
            WHERE u.email = :email
            """)
    Mono<AccountCredentialsEntity> findByEmail(String email);
}
