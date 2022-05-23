package la.liga.del.barrio.partido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import la.liga.del.barrio.equipo.Equipo;
import la.liga.del.barrio.torneo.Torneo;

public interface PartidoRepository extends JpaRepository<Partido,Long>{
		
	@Query("Select p from Partido p Where p.torneo = :t")
	public List<Partido> getPartidos(@Param("t")Torneo torneo);
	
	@Query("Select p from Partido p Where (p.equipo1 = :equipo OR p.equipo2 = :equipo)")
	public List<Partido> getPartidosDeEquipo(@Param("equipo")Equipo equipo);
}