package la.liga.del.barrio.equipo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import la.liga.del.barrio.jugador.Jugador;
import la.liga.del.barrio.user.User;

@Entity
public class Equipo{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String nombre;
	private String correo;
	private String telefono;
	
	@OneToMany(mappedBy="equipo",cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Jugador> jugadores = new ArrayList<>();
	@OneToOne
	private User delegado;
	
	//Constructores
	
	public Equipo() {} //Constructor por defecto
	
	public Equipo(String nombre, String correo, String telefono, User delegado) { //Constructor para un nuevo equipo
		this.nombre = nombre;
		this.correo = correo;
		this.telefono = telefono;
		this.delegado = delegado;
	}
	
		// Consultas
		public Long getId() {
			return this.id;
		}
		
		public String getNombre() {
			return this.nombre;
		}
		
		public String getCorreo() {
			return this.correo;
		}
		
		public String getTelefono() {
			return this.telefono;
		}
		
		public List<Jugador> getJugadores() { //Devuelve la lista de jugadores pertenecientes a este equipo
			return this.jugadores;
		}
		
		public User getDelegado() {
			return this.delegado;
		}
		
		// Actualizaciones
		
		public void setId(Long id) {
			this.id = id;
		}
		
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		
		public void setCorreo(String correo) {
			this.correo = correo;
		}
		
		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}
		
		public void setJugadores(List<Jugador> jugadores) {
			this.jugadores = jugadores;
		}
		
		public void setDelegado(User delegado) {
			this.delegado = delegado;
		}
}