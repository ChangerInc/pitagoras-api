package changer.pitagoras.repository;

import changer.pitagoras.model.Circulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CirculoRepository extends JpaRepository<Circulo, UUID> {}