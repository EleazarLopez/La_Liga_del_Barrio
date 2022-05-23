package la.liga.del.barrio.equipo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import la.liga.del.barrio.user.UserRepository;
import la.liga.del.barrio.partido.Partido;
import la.liga.del.barrio.partido.PartidoRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class EquipoController{
	@Autowired
	private EquipoRepository EquipoRepository;
	
	@Autowired
	private UserRepository UserRepository;
	
	@Autowired
	private PartidoRepository PartidoRepository;
	
	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		if (principal != null) {
			model.addAttribute("logged",true);
			model.addAttribute("userName",principal.getName());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
			model.addAttribute("delegado",request.isUserInRole("DELEGADO"));
			//model.addAttribute("equipo",UserRepository.findByName(principal.getName()).get().getEquipo());
		}else {
			model.addAttribute("logged",false);
		}
	}
	
	//Consultar la información de un equipo
	@RequestMapping("/equipos/{equipo}")
	public String equipoDetail( Model model, @PathVariable String equipo) {
		
		if (EquipoRepository.findByNombre(equipo).isPresent()) {
			model.addAttribute("equipo", EquipoRepository.findByNombre(equipo).get());
			model.addAttribute("jugadores",EquipoRepository.findByNombre(equipo).get().getJugadores());
			model.addAttribute("existe",true);
		}else {
			model.addAttribute("existe",false);
		}
		
		return "equipoDetail_template";
	}
	
	//Añadir un equipo
	@RequestMapping("/equipo/nuevo")
	public String equipoNuevo(Model model, HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		// Comprobamos que el usuario está registrado y es delegado
		if(UserRepository.findByName(principal.getName()).isPresent() && UserRepository.findByName(principal.getName()).get().getRoles().contains("DELEGADO")) {
			model.addAttribute("dele",true);
			//Comprobamos si el usuario tiene equipo
			if (UserRepository.findByName(principal.getName()).get().getEquipo() != null) {
				//El usuario ya tiene equipo y se niega el acceso al formulario de creación
				model.addAttribute("tiene",true);
				return "equipoSave_template";
			}else {
				//El usuario es delegado y no tiene equipo y se habilita el acceso al formulario de creación
				model.addAttribute("existe",false);
				return "equipoNuevo_template";
			}
		}else {
			//El usuario no existe en la base de datos y se niega el acceso al formulario de creación
			model.addAttribute("dele",false);
			return "equipoSave_template";
		}
	}
	
	@PostMapping("/equipo/nuevo")
	public String equipoCreado( Model model, Equipo nuevo, HttpServletRequest request ) {
		
		Principal principal = request.getUserPrincipal();
		
		// Comprobamos que el usuario está registrado y es delegado
		if(UserRepository.findByName(principal.getName()).isPresent() && UserRepository.findByName(principal.getName()).get().getRoles().contains("DELEGADO")) {
			//Comprobamos que el nombre de equipo no esté ya registrado o el nombre elegido sea "nuevo"
			if (EquipoRepository.findByNombre(nuevo.getNombre()).isPresent() || nuevo.getNombre().equals("nuevo")) {
				model.addAttribute("disponible",false);
				model.addAttribute("nombre",nuevo.getNombre());
			}else {
				//Si el nombre está disponible y no es "nuevo", se crea
				model.addAttribute("disponible",true);
				model.addAttribute("nombre",nuevo.getNombre());
				model.addAttribute("dele",principal.getName());
				nuevo.setDelegado(UserRepository.findByName(principal.getName()).get());
				EquipoRepository.save(nuevo);
			}
		}
		model.addAttribute("nombre",nuevo.getNombre());
		return "equipoNuevoSave_template";
	}
	
	//Borrar un equipo
	@RequestMapping("/equipos/delete")
	public String equipoDelete( Model model,  HttpServletRequest request) {

		Principal principal = request.getUserPrincipal();
		
		// Comprobamos que el usuario está logeado, sea delegado, tenga equipo
		if(UserRepository.findByName(principal.getName()).isPresent() && UserRepository.findByName(principal.getName()).get().getRoles().contains("DELEGADO")&& UserRepository.findByName(principal.getName()).get().getEquipo()!=null) {
			//Si el equipo participa en algún partido, se eliminan esos partidos
			if(PartidoRepository.getPartidosDeEquipo(UserRepository.findByName(principal.getName()).get().getEquipo())!=null){
				List<Partido> partidos = PartidoRepository.getPartidosDeEquipo(UserRepository.findByName(principal.getName()).get().getEquipo());
				for (Partido p : partidos) {
					PartidoRepository.delete(p);
				}
			}
			model.addAttribute("nombre",UserRepository.findByName(principal.getName()).get().getEquipo().getNombre());
			EquipoRepository.findByNombre(UserRepository.findByName(principal.getName()).get().getEquipo().getNombre()).get().setDelegado(null);
			EquipoRepository.delete(UserRepository.findByName(principal.getName()).get().getEquipo());
			model.addAttribute("borrado",true);
		}else {
			model.addAttribute("borrado",false);
		}
		return "equipoDelete_template";
	}
	
	//Edita un equipo
	@RequestMapping("/equipos/editar")
	public String equipoEdit(Model model,  HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		// Comprobamos que el usuario está registrado, sea delegado y tenga equipo
		if(UserRepository.findByName(principal.getName()).isPresent() && UserRepository.findByName(principal.getName()).get().getRoles().contains("DELEGADO") && UserRepository.findByName(principal.getName()).get().getEquipo()!=null) {
			model.addAttribute("equipo",EquipoRepository.findByNombre(UserRepository.findByName(principal.getName()).get().getEquipo().getNombre()).get());
			model.addAttribute("jugadores", EquipoRepository.findByNombre(UserRepository.findByName(principal.getName()).get().getEquipo().getNombre()).get().getJugadores());
			model.addAttribute("existe",true);
		}else {
			model.addAttribute("existe",false);
		}
		return "equipoEdit_template";
	}
	
	@PostMapping("/equipos/editar")
	public String equipoEditado(Model model, Equipo equipo, String nombreviejo, HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		// Comprobamos que el usuario está registrado, sea delegado y tenga equipo
		if(UserRepository.findByName(principal.getName()).isPresent() && UserRepository.findByName(principal.getName()).get().getRoles().contains("DELEGADO") && UserRepository.findByName(principal.getName()).get().getEquipo()!=null) {
			model.addAttribute("propietario",true);
			//Si el equipo cambia de nombre y el nuevo nombre está ya registrado, o el nuevo nombre es "nuevo", se declinan los cambios
			if ((!equipo.getNombre().equals(nombreviejo) && EquipoRepository.findByNombre(equipo.getNombre()).isPresent()) || equipo.getNombre().equals("nuevo")) {
				model.addAttribute("disponible",false);
				model.addAttribute("nombreviejo",nombreviejo);
			}else {
				equipo.setJugadores(EquipoRepository.findById(equipo.getId()).get().getJugadores());
				equipo.setDelegado(EquipoRepository.findById(equipo.getId()).get().getDelegado());
				EquipoRepository.save(equipo);
				model.addAttribute("disponible",true);
			}
		}else {
			model.addAttribute("propietario",false);
		}
		model.addAttribute("nombre",equipo.getNombre());
		return "equipoEditado_template";
	}
}