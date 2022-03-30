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
import org.springframework.web.bind.annotation.RequestParam;

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
			//model.addAttribute("equipo",UserRepository.findByName(principal.getName()).get().getEquipo());
		}else {
			model.addAttribute("logged",false);
		}
	}
	
	/* SIN USO POR AHORA
	//Consultar la información de todos los jugadores de un equipo
	@RequestMapping("/jugadores/{equipo}")
	public String jugadorList(Model model, @PathVariable String equipo) {
		Optional <Equipo> revisar = EquipoRepository.findByNombre(equipo);
		if(revisar.isPresent()) {
			model.addAttribute("equipo",revisar.get());
			return "jugadoresList_template";
		}else {
			return "home_template";
		}
	}
	*/
	
	//Consultar la información de un jugador
	@RequestMapping("/jugador/{jugador}")
	public String jugadorDetail( Model model, @PathVariable String jugador) {
		Optional <Jugador> revisar = JugadorRepository.findByNombre(jugador);
		if(revisar.isPresent()) {
			model.addAttribute("jugador",revisar.get());
			model.addAttribute("equipo",revisar.get().getEquipo());
			return "jugadorDetail_template";
		}else {
			return "equipoEdit_template";
		}
	}
	
	//Añadir un jugador
	@RequestMapping("/jugador/nuevo/{equipo}")
	public String jugadorNuevo(Model model, @PathVariable String equipo) {
		Optional <Equipo> revisar = EquipoRepository.findByNombre(equipo);
		if(revisar.isPresent()) {
			model.addAttribute("equipo",revisar.get());
		}
		return "jugadorNuevo_template";
	}
	
	@PostMapping("/jugador/nuevo")
	public String jugadorAdd( Model model, Jugador jugador, String nombredeequipo, String usuario) {
		Optional <Jugador> revisar = JugadorRepository.findByDni(jugador.getDni());
		User delecheck = UserRepository.findByName(usuario).get();
		User admincheck = UserRepository.findByName(usuario).get();
		
		if(admincheck.getRoles().contains("ADMIN")) {// Si el usuario que edita es admin, lo permite
			model.addAttribute("denegado",false);
			if (revisar.isPresent()) { // Si el DNI del nuevo jugador existe, no lo crea
				model.addAttribute("disponible",false);
			}else {
				jugador.setEquipo(EquipoRepository.findByNombre(nombredeequipo).get());
				JugadorRepository.save(jugador);
				model.addAttribute("jugador",jugador);
				model.addAttribute("equipo",jugador.getEquipo());
				model.addAttribute("disponible",true);
			}
		}else {
			if (delecheck.getEquipo().getNombre().equals(nombredeequipo)) {// Revisa que el usuario que crea el jugador sea el delegado del equipo para el que se va a crear
				model.addAttribute("denegado",false);
				if (revisar.isPresent()) { // Si el DNI del nuevo jugador existe, no lo crea
					model.addAttribute("disponible",false);
				}else {
					jugador.setEquipo(EquipoRepository.findByNombre(nombredeequipo).get());
					JugadorRepository.save(jugador);
					model.addAttribute("jugador",jugador);
					model.addAttribute("equipo",jugador.getEquipo());
					model.addAttribute("disponible",true);
				}
			}else {
				model.addAttribute("denegado",true);
			}
		}
		return "jugadorSave_template";
	}
	
	//Borrar un jugador
	@RequestMapping("/jugador/delete/{jugador}")
	public String jugadorDelete( Model model, @PathVariable long jugador) {
		Optional <Jugador> revisar = JugadorRepository.findById(jugador);
		if (revisar.isPresent()) {
			model.addAttribute("existe",true);
			model.addAttribute("jugador",revisar.get());
			model.addAttribute("equipo",revisar.get().getEquipo());
			model.addAttribute("borrado",true);
			JugadorRepository.delete(revisar.get());
		}else {
			model.addAttribute("existe",false);
			model.addAttribute("borrado",false);
		}
		return "jugadorDelete_template";
	}
	
	//Editar un jugador
	@RequestMapping("/jugador/editar/{id}")
	public String jugadorEdit(Model model, @PathVariable long id) {
		Optional <Jugador> revisar = JugadorRepository.findById(id);
		if(revisar.isPresent()) {
			model.addAttribute("jugador",revisar.get());
			model.addAttribute("nombrequipo",revisar.get().getEquipo().getNombre());
			model.addAttribute("existe",true);
		}else {
			model.addAttribute("existe",false);
		}
		return "jugadorEdit_template";
	}
	
	@PostMapping("/jugador/editar")
	public String jugadorEditado(Model model, Jugador jugador, String equipoactual, String usuario) {
		Optional <Jugador> revisar = JugadorRepository.findById(jugador.getId());
		User delecheck = UserRepository.findByName(usuario).get();
		User admincheck = UserRepository.findByName(usuario).get();
		
		if(revisar.isPresent()) {
			model.addAttribute("existe",true);
			Optional <Jugador> dnicheck = JugadorRepository.findByDni(jugador.getDni());
			jugador.setEquipo(EquipoRepository.findByNombre(equipoactual).get());
			model.addAttribute("equipo",jugador.getEquipo());
			if (admincheck.getRoles().contains("ADMIN")) {
				model.addAttribute("denegado",false);
				if (dnicheck.isPresent()) {
					model.addAttribute("disponible",false);
				}else {
					model.addAttribute("disponible",true);
					model.addAttribute("jugador",jugador);
					JugadorRepository.save(jugador);
				}
			}else {
				if (jugador.getEquipo().getNombre().equals(delecheck.getEquipo().getNombre())){
					model.addAttribute("denegado",false);	
					if (dnicheck.isPresent()) {
						model.addAttribute("disponible",false);
					}else {
						model.addAttribute("disponible",true);
						model.addAttribute("jugador",jugador);
						JugadorRepository.save(jugador);
					}
				}else {
					model.addAttribute("denegado",true);
				}
			}
		}else {
			model.addAttribute("existe",false);
		}
		return "jugadorEditado_template";
	}
}