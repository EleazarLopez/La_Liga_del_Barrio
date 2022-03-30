package la.liga.del.barrio.equipo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import la.liga.del.barrio.torneo.Torneo;
import la.liga.del.barrio.user.User;

public interface EquipoRepository extends JpaRepository<Equipo,Long>{
	Optional <Equipo> findByDelegado (User delegado);
	Optional <Equipo> findByNombre (String nombre);
	
	@Query("SELECT distinct equipo FROM Partido p, Equipo equipo " + "WHERE(p.equipo1 = equipo OR p.equipo2 = equipo) AND p.torneo = :t")
	public List<Equipo> getEquipos(@Param("t")Torneo torneo);
	
}