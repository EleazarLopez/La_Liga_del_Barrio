package la.liga.del.barrio.equipo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import la.liga.del.barrio.torneo.Torneo;
import la.liga.del.barrio.torneo.TorneoRepository;
import la.liga.del.barrio.user.User;
import la.liga.del.barrio.user.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class EquipoController{
	@Autowired
	private EquipoRepository EquipoRepository;
	
	@Autowired
	private TorneoRepository TorneoRepository;
	
	@Autowired
	private UserRepository UserRepository;
	
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
		
		model.addAttribute("equipo", EquipoRepository.findByNombre(equipo).get());
		model.addAttribute("jugadores",EquipoRepository.findByNombre(equipo).get().getJugadores());
		
		return "equipoDetail_template";
	}
	
	//Añadir un equipo
	@RequestMapping("/equipo/nuevo/{delegado}")
	public String equipoNuevo(Model model, @PathVariable String delegado) {
		
		Optional <User> dele = UserRepository.findByName(delegado);
		
		if(dele.isPresent() && dele.get().getRoles().contains("DELEGADO")) {
			model.addAttribute("dele",true);
			if (dele.get().getEquipo() != null) {
				model.addAttribute("tiene",true);
				return "equipoSave_template";
			}else {
				model.addAttribute("existe",false);
				return "equipoNuevo_template";
			}
		}else {
			model.addAttribute("dele",false);
			return "equipoSave_template";
		}
	}
	
	@PostMapping("/equipo/nuevo")
	public String equipoCreado( Model model, Equipo nuevo, String nombredelegado) {
		
		Optional <Equipo> equiponuevo = EquipoRepository.findByNombre(nuevo.getNombre());
		
		if(equiponuevo.isPresent()) {
			model.addAttribute("disponible",false);
			model.addAttribute("nombre",nuevo.getNombre());
		}else {
			if (nuevo.getNombre().equals("nuevo")) {
				model.addAttribute("disponible",false);
				model.addAttribute("nombre",nuevo.getNombre());
			}else {
				model.addAttribute("disponible",true);
				model.addAttribute("nombre",nuevo.getNombre());
				model.addAttribute("dele",nombredelegado);
				nuevo.setDelegado(UserRepository.findByName(nombredelegado).get());
				EquipoRepository.save(nuevo);
			}
		}
		model.addAttribute("nombre",nuevo.getNombre());
		return "equipoNuevoSave_template";
	}
	
	/* Un equipo no puede existir sin delegado, por esto al eliminar un user, se elimina cualquier equipo asociado
	//Borrar un equipo
	@RequestMapping("/equipos/delete/{nombre}")
	public String equipoDelete( Model model, @PathVariable String nombre) {

		Optional <Equipo> equipo = EquipoRepository.findByNombre(nombre);

		if(equipo.isPresent()) {
			List <Torneo> torneos = TorneoRepository.findAll();
			model.addAttribute("juega",false);
			model.addAttribute("equipo",equipo.get().getNombre());
			for(Torneo t : torneos) {
				for (Equipo e : EquipoRepository.getEquipos(t)) {
					if (e==equipo.get()) {
						model.addAttribute("juega",true);
						return "equipoDelete_template";
					}
				}
			}
			EquipoRepository.delete(equipo.get());
			model.addAttribute("borrado",true);
		}else {
			model.addAttribute("borrado",false);
		}
		return "equipoDelete_template";
	}*/
	
	//Edita un equipo
	@RequestMapping("/equipos/editar/{nombre}")
	public String equipoEdit(Model model, @PathVariable String nombre) {
		
		Optional <Equipo> equipo = EquipoRepository.findByNombre(nombre);
		if(equipo.isPresent()) {
			model.addAttribute("equipo",EquipoRepository.findByNombre(nombre).get());
			model.addAttribute("jugadores", EquipoRepository.findByNombre(nombre).get().getJugadores());
			model.addAttribute("existe",true);
		}else {
			model.addAttribute("nombre",nombre);
			model.addAttribute("existe",false);
		}
		return "equipoEdit_template";
	}
	
	@PostMapping("/equipos/editar")
	public String equipoEditado(Model model, Equipo equipo, String nombreviejo, String usuario) {
		
		Optional <Equipo> revisar = EquipoRepository.findByNombre(equipo.getNombre());
		String dele = EquipoRepository.findById(equipo.getId()).get().getDelegado().getName();
		User admincheck = UserRepository.findByName(usuario).get();
		
		if (revisar.isPresent() || equipo.getNombre().equals("nuevo")) {
			model.addAttribute("disponible",false);
			model.addAttribute("nombreviejo",nombreviejo);
		}else {
			if (dele.equals(usuario) || (admincheck.getRoles().contains("ADMIN"))) {
				model.addAttribute("denegado",false);
				equipo.setJugadores(EquipoRepository.findById(equipo.getId()).get().getJugadores());
				equipo.setDelegado(EquipoRepository.findById(equipo.getId()).get().getDelegado());
				EquipoRepository.save(equipo);
				model.addAttribute("disponible",true);
			}else {
				model.addAttribute("denegado",true);
			}
		}
		model.addAttribute("nombre",equipo.getNombre());
		return "equipoEditado_template";
	}
}