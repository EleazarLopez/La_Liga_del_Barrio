package la.liga.del.barrio.user;

import javax.persistence.Entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import la.liga.del.barrio.equipo.Equipo;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;

@Entity
public class User{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String encodedPassword;
	private String email;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles = new ArrayList<String>();
	@OneToOne(mappedBy="delegado", orphanRemoval=true)
	private Equipo equipo;
	
	//Constructores
	
	public User(){}
	
	public User(String name, String encodedPassword,String email) { 
		this.name = name;
		this.encodedPassword = encodedPassword;
		this.email = email;
		this.roles.add("USUARIO");
	}
	
	//Consultas
	
	public Long getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPassword() {
		return this.encodedPassword;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public List<String> getRoles() {
		return this.roles;
	}
	
	public Equipo getEquipo() {
		return this.equipo;
	}

	//Actualizaciones
	
	public void setId(Long Id) {
		this.id = Id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setencodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}
}