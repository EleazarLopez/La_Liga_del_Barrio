package la.liga.del.barrio.jugador;

import java.security.Principal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import la.liga.del.barrio.equipo.Equipo;
import la.liga.del.barrio.equipo.EquipoRepository;
import la.liga.del.barrio.user.User;
import la.liga.del.barrio.user.UserRepository;

@Controller
public class JugadorController{
	
	@Autowired 
	private JugadorRepository JugadorRepository;
	
	@Autowired 
	private EquipoRepository EquipoRepository;
	
	@Autowired 
	private UserRepository UserRepository;
	
	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		if (principal != null) {
			model.addAttribute("logged",true);
			model.addAttribute("userName",principal.getName());
			model.addAttribute("delegado",request.isUserInRole("DELEGADO"));
			model.addAttribute("equipo",UserRepository.findByName(principal.getName()).get().getEquipo());
		}else {
			model.addAttribute("logged",false);
		}
	}
	
	//Consultar la información de un jugador
	@RequestMapping("/jugador/{jugador}")
	public String jugadorDetail( Model model, @PathVariable String jugador) {
		Optional <Jugador> revisar = JugadorRepository.findByNombre(jugador);
		if(revisar.isPresent()) {
			model.addAttribute("jugador",revisar.get());
			model.addAttribute("equipo",revisar.get().getEquipo());
			model.addAttribute("existe",true);
		}else {
			model.addAttribute("existe",false);
		}
		return "jugadorDetail_template";
	}
	
	//Añadir un jugador
	@RequestMapping("/jugador/nuevo")
	public String jugadorNuevo(Model model, HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		// Comprobamos que el usuario está logeado, sea delegado y tenga equipo
		if(UserRepository.findByName(principal.getName()).isPresent() && UserRepository.findByName(principal.getName()).get().getRoles().contains("DELEGADO")&& UserRepository.findByName(principal.getName()).get().getEquipo()!=null) {
			model.addAttribute("existe",true);
			model.addAttribute("equipo", UserRepository.findByName(principal.getName()).get().getEquipo());
		}else {
			model.addAttribute("existe",false);
		}
		return "jugadorNuevo_template";
	}
	
	@PostMapping("/jugador/nuevo")
	public String jugadorAdd( Model model, Jugador jugador, HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		// Comprobamos que el usuario está logeado, sea delegado y tenga equipo
		if(UserRepository.findByName(principal.getName()).isPresent() && UserRepository.findByName(principal.getName()).get().getRoles().contains("DELEGADO")&& UserRepository.findByName(principal.getName()).get().getEquipo()!=null) {
			model.addAttribute("denegado",false);
			// Se comprueba el DNI del jugador como clave primaria para no repetir jugadores
			if (JugadorRepository.findByDni(jugador.getDni()).isPresent()){
				model.addAttribute("disponible",false);
			}else {
				jugador.setEquipo(EquipoRepository.findByNombre(UserRepository.findByName(principal.getName()).get().getEquipo().getNombre()).get());
				JugadorRepository.save(jugador);
				model.addAttribute("jugador",jugador);
				model.addAttribute("equipo",jugador.getEquipo());
				model.addAttribute("disponible",true);
			}
		}else {
			model.addAttribute("denegado",true);
		}
		return "jugadorSave_template";
	}
	
	//Borrar un jugador
	@RequestMapping("/jugador/delete/{jugador}")
	public String jugadorDelete( Model model, @PathVariable long jugador, HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		// Comprobamos que el usuario está logeado, sea delegado y tenga equipo
		if(UserRepository.findByName(principal.getName()).isPresent() && UserRepository.findByName(principal.getName()).get().getRoles().contains("DELEGADO")&& UserRepository.findByName(principal.getName()).get().getEquipo()!=null) {
			model.addAttribute("propietario",true);
			// Se comprueba si el jugador a eliminar existe y si es del equipo del usuario que pretende eliminarlo
			if(JugadorRepository.findById(jugador).isPresent() && JugadorRepository.findById(jugador).get().getEquipo() == UserRepository.findByName(principal.getName()).get().getEquipo()) {
				model.addAttribute("jugador",JugadorRepository.findById(jugador).get());
				model.addAttribute("equipo",JugadorRepository.findById(jugador).get().getEquipo());
				model.addAttribute("borrado",true);
				JugadorRepository.delete(JugadorRepository.findById(jugador).get());
			}else {
				model.addAttribute("borrado",false);
			}
		}else {
			model.addAttribute("propietario",false);
		}
		return "jugadorDelete_template";
	}
	
	//Editar un jugador
	@RequestMapping("/jugador/editar/{id}")
	public String jugadorEdit(Model model, @PathVariable long id, HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		// Comprobamos que el usuario está logeado, sea delegado y tenga equipo
		if(UserRepository.findByName(principal.getName()).isPresent() && UserRepository.findByName(principal.getName()).get().getRoles().contains("DELEGADO")&& UserRepository.findByName(principal.getName()).get().getEquipo()!=null) {
			model.addAttribute("propietario",true);
			// Se comprueba si el jugador a editar existe y si es del equipo del usuario que pretende editarlo
			if(JugadorRepository.findById(id).isPresent() && JugadorRepository.findById(id).get().getEquipo() == UserRepository.findByName(principal.getName()).get().getEquipo()) {
				model.addAttribute("existe",true);
				model.addAttribute("jugador",JugadorRepository.findById(id).get());
				model.addAttribute("nombrequipo",JugadorRepository.findById(id).get().getEquipo().getNombre());
			}else {
				model.addAttribute("existe",false);
			}
		}else {
			model.addAttribute("propietario",false);
		}
		return "jugadorEdit_template";
	}
	
	@PostMapping("/jugador/editar")
	public String jugadorEditado(Model model, Jugador jugador, HttpServletRequest request, String dniactual) {
		
		Principal principal = request.getUserPrincipal();
		
		// Comprobamos que el usuario está logeado, sea delegado y tenga equipo
		if(UserRepository.findByName(principal.getName()).isPresent() && UserRepository.findByName(principal.getName()).get().getRoles().contains("DELEGADO")&& UserRepository.findByName(principal.getName()).get().getEquipo()!=null) {
			model.addAttribute("propietario",true);
			// Se comprueba si el jugador a editar existe y si es del equipo del usuario que pretende editarlo
			if(JugadorRepository.findById(jugador.getId()).isPresent() && JugadorRepository.findById(jugador.getId()).get().getEquipo() == UserRepository.findByName(principal.getName()).get().getEquipo()) {
				model.addAttribute("existe",true);
				//Se comprueba que el DNI es del jugador actual o está libre
				if (JugadorRepository.findById(jugador.getId()).get().getDni().equals(dniactual) || !JugadorRepository.findByDni(jugador.getDni()).isPresent()) {
					model.addAttribute("disponible",true);
					jugador.setEquipo(UserRepository.findByName(principal.getName()).get().getEquipo());
					model.addAttribute("equipo",jugador.getEquipo());
					model.addAttribute("jugador",jugador);
					JugadorRepository.save(jugador);
				}else {
					model.addAttribute("jugador",jugador);
					model.addAttribute("disponible",false);
				}
			}else {
				model.addAttribute("existe",false);
			}
		}else {
			model.addAttribute("propietario",false);
		}
		return "jugadorEditado_template";
	}
}