package com.gea.app.user;

import com.gea.app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    <T> Optional<T> findById(UUID id);

    @Query("""
           select u
           from User u
           left join fetch u.unit un
           left join fetch un.unitType ut
           """)
    List<User> findAllWithUnit();

    @Query("""
           select u
           from User u
           left join fetch u.unit un
           left join fetch un.unitType ut
           where u.id = :id
           """)
    Optional<User> findByIdWithUnit(@Param("id") UUID id);
}