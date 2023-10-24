package com.ingsoftware.api.repository;


import com.ingsoftware.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String username);
    Optional<User> findByIdAndIsActiveTrue(int id);
    Optional<List<User>> findAllByIdIn(List<Integer> ids);
}
