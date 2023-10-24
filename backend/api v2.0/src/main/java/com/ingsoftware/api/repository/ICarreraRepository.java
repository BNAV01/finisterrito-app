package com.ingsoftware.api.repository;

import java.util.Optional;
import com.ingsoftware.api.models.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICarreraRepository extends JpaRepository<Carrera, Integer> {
    Optional<Carrera> findByNombreCarrera(String id);
}
