package la.liga.del.barrio.torneo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;

@Entity
public class Torneo{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String nombre;
	private String ganador;
	
	// Constructores
	public Torneo(){}
	
	public Torneo(String nombre) {
		this.nombre = nombre;
	}
	
	//Consultas
	public Long getId() {
		return this.id;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getGanador() {
		return this.ganador;
	}
	
	// Actualizaciones
	
	public void setId (Long id) {
		this.id = id;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setGanador(String ganador) {
		this.ganador = ganador;
	}
	
}