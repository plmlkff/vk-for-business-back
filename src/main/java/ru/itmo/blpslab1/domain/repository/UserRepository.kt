package ru.itmo.blpslab1.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itmo.blpslab1.domain.entity.User;
import ru.itmo.blpslab1.domain.repository.specification.UserSpecification

import java.util.Optional;
import java.util.UUID;

@Repository
interface UserRepository : JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    @Query(
        """
            SELECT u FROM User u
            WHERE u.login = :login
        """
    )
    fun findUserByLogin(@Param("login") login: String): User?

    fun existsUserByLogin(login: String): Boolean
}
