package la.liga.del.barrio.torneo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import la.liga.del.barrio.equipo.Equipo;

public interface TorneoRepository extends JpaRepository<Torneo,Long>{
	Optional<Torneo> findByNombre(String nombre);
	
	@Query("SELECT distinct t FROM Partido p JOIN p.torneo t "+"WHERE p.equipo1 = :equipo OR p.equipo2 = :team")
	public List<Torneo> getTorneos(@Param("team")Equipo equipo);

}
