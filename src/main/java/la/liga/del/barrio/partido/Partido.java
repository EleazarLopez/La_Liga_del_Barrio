package la.liga.del.barrio.partido;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import la.liga.del.barrio.equipo.Equipo;
import la.liga.del.barrio.torneo.Torneo;

@Entity
public class Partido{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String golesE1;
	private String golesE2;
	private String fecha;
	private String hora;
	
	@ManyToOne
	private Equipo equipo1;
	
	@ManyToOne
	private Equipo equipo2;
	
	@ManyToOne
	private Torneo torneo;
	
	//Constructores
	public Partido() {} //Constructor por defecto para cuando los datos vienen de una bd existente
	
	public Partido(Torneo torneo, Equipo equipo1, Equipo equipo2, String fecha, String hora) { //Constructor para un nuevo partido
		this.torneo = torneo;
		this.equipo1 = equipo1;
		this.equipo2 = equipo2;
		this.fecha = fecha;
		this.hora = hora;
	}
	
	// Consultas
	public Long getId() {
		return this.id;
	}
	
	public Equipo getEquipo1() {
		return this.equipo1;
	}
	
	public Equipo getEquipo2() {
		return this.equipo2;
	}
	
	public String getgolesE1() {
		return this.golesE1;
	}
	
	public String getgolesE2() {
		return this.golesE2;
	}
	
	public String getFecha() {
		return this.fecha;
	}
	
	public String getHora() {
		return this.hora;
	}
	
	public Torneo getTorneo() {
		return this.torneo;
	}
	
	// Actualizaciones
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setEquipo1 (Equipo equipo1) {
		this.equipo1 = equipo1;
	}
	
	public void setEquipo2 (Equipo equipo2) {
		this.equipo2 = equipo2;
	}
	
	public void setgolesE1(String golesE1) {
		this.golesE1 = golesE1;
	}
	
	public void setgolesE2(String golesE2) {
		this.golesE2 = golesE2;
	}
	
	public void setFecha (String fecha) {
		this.fecha = fecha;
	}
	
	public void setHora (String hora) {
		this.hora = hora;
	}
	
	public void setTorneo (Torneo torneo) {
		this.torneo = torneo;
	}
}