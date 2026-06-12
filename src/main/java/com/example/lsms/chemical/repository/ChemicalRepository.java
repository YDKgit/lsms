package com.example.lsms.chemical.repository;

import com.example.lsms.chemical.domain.Chemical;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChemicalRepository extends JpaRepository<Chemical, Long> {

    Optional<Chemical> findByCasNumber(String casNumber);

    Optional<Chemical> findByCatNumber(String catNumber);

    boolean existsByCasNumber(String casNumber);

    boolean existsByCatNumber(String catNumber);
}
