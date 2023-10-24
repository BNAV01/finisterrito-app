package com.ingsoftware.api.repository;


import com.ingsoftware.api.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRolRepository  extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByName(String name);

    Optional<List<Rol>> findByIdIn(List<Integer> ids);
}
