package Capstone.easyreservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import Capstone.easyreservation.entity.Utente;
import Capstone.easyreservation.enums.UserRole;
import Capstone.easyreservation.repository.UtenteRepository;

@Service
public class StartupRunner implements CommandLineRunner {

	@Autowired
	UtenteRepository ur;

	@Override
	public void run(String... args) {
		if (ur.findAll().isEmpty()) {
			Utente admin = Utente.builder().name("Admin").surname("Admin").email("admin@easyreservation.com")
					.password("1234").role(UserRole.ADMIN).build();
			ur.save(admin);
		}
	}
}

