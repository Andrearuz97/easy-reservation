package Capstone.easyreservation.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Capstone.easyreservation.entity.Utente;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, UUID> {

	Optional<Utente> findById(UUID idUser);
	Optional<Utente> findByEmail(String email);

}