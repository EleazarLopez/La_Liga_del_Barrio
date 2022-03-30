package la.liga.del.barrio.jugador;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorRepository extends JpaRepository<Jugador,Long>{

	Optional<Jugador> findByNombre(String jugador);
	Optional<Jugador> findByDni(String dni);
	
}