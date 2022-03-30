package la.liga.del.barrio.jugador;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import la.liga.del.barrio.equipo.Equipo;

@Entity
public class Jugador{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String nombre;
	private String dorsal;
	private String dni;
	@ManyToOne
	private Equipo equipo;
	
	//Constructores
	public Jugador() {} //Constructor por defecto
	
	public Jugador(String nombre,String dorsal,String dni) { //Constructor para un nuevo jugador
		this.nombre = nombre;
		this.dorsal = dorsal;
		this.dni = dni;
	}
	
	// Consultas
	public Long getId() {
		return this.id;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getDorsal() {
		return this.dorsal;
	}
	
	public String getDni() {
		return this.dni;
	}
	
	public Equipo getEquipo() {
		return this.equipo;
	}
	
	// Actualizaciones
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setDorsal(String dorsal) {
		this.dorsal = dorsal;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public void setEquipo (Equipo equipo) {
		this.equipo = equipo;
	}
}