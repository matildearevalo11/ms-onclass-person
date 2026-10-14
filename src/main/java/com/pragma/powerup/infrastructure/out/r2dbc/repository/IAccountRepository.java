package com.pragma.powerup.infrastructure.out.r2dbc.repository;

import com.pragma.powerup.infrastructure.out.r2dbc.entity.AccountCredentialsEntity;
import com.pragma.powerup.infrastructure.out.r2dbc.entity.PersonSummaryEntity;
import java.util.List;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface IAccountRepository extends Repository<AccountCredentialsEntity, Long> {
    @Query("SELECT id, name, email FROM users WHERE id IN (:ids) ORDER BY id")
    Flux<PersonSummaryEntity> findProfilesByIds(List<Long> ids);
    @Query("""
            SELECT u.id, u.email, u.password_hash, r.name AS role
            FROM users u JOIN roles r ON r.id = u.role_id
            WHERE u.email = :email
            """)
    Mono<AccountCredentialsEntity> findByEmail(String email);
}
