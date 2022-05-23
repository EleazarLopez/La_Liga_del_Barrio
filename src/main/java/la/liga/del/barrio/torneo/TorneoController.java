package la.liga.del.barrio.torneo;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import la.liga.del.barrio.equipo.EquipoRepository;
import la.liga.del.barrio.user.UserRepository;
import la.liga.del.barrio.partido.Partido;
import la.liga.del.barrio.partido.PartidoRepository;

@Controller
public class TorneoController{
	@Autowired
	private TorneoRepository TorneoRepository;
	
	@Autowired
	private EquipoRepository EquipoRepository;
	
	@Autowired
	private PartidoRepository PartidoRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		if (principal != null) {
			model.addAttribute("logged",true);
			model.addAttribute("userName",principal.getName());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
			model.addAttribute("delegado",request.isUserInRole("DELEGADO"));
			model.addAttribute("equipo",userRepository.findByName(principal.getName()).get().getEquipo());
		}else {
			model.addAttribute("logged",false);
		}
	}
	
	//Consulta los torneos en curso
	@RequestMapping("/torneos")
	public String torneoList(Model model) {
		
		model.addAttribute("torneos", TorneoRepository.findAll());
		
		return "torneos_template";
	}
	
	//Consultar la información de un torneo
	@RequestMapping("/torneos/{nombre}")
	public String torneoDetail( Model model, @PathVariable String nombre) {
		
		Optional <Torneo> torneo = TorneoRepository.findByNombre(nombre);
		
		if (torneo.isPresent()) { // Si el torneo existe, se añade el torneo al modelo y sus equipos
			model.addAttribute("torneo", torneo.get());
			model.addAttribute("equipos",EquipoRepository.getEquipos(torneo.get()));
			model.addAttribute("partidos",PartidoRepository.getPartidos(torneo.get()));
			model.addAttribute("existe",true);
		}else{ // si no, vuelve a torneos
			model.addAttribute("existe",false);
		}	
		return "torneoDetail_template";
	}
	
	//Añadir un torneo
	
	@RequestMapping("/torneos/nuevo")
	public String torneoNuevo(Model model) {
    	
		return "torneoNuevo_template";
	}
	
	@PostMapping("/torneos/nuevo")
	public String torneoSave(Model model, Torneo torneo) {
    	
		Optional <Torneo> torneonuevo = TorneoRepository.findByNombre(torneo.getNombre());
		
		if (torneonuevo.isPresent() || torneo.getNombre().equals("nuevo")){ // Si el nombre de torneo ya existe o se llama igual que el método debe retornar que el nombre elegido no está disponible
			model.addAttribute("disponible",false);
		}else { // Si el nombre está disponible, se crea el nuevo torneo
			model.addAttribute("disponible",true);
			torneo.setGanador(null);
			TorneoRepository.save(torneo);
		}
		model.addAttribute("nombre",torneo.getNombre());
		return "torneoSave_template";
	}
	
	//Borrar un torneo
	@RequestMapping("/torneos/delete/{nombre}")
	public String torneoDelete( Model model, @PathVariable String nombre) {
		
		Optional <Torneo> torneo = TorneoRepository.findByNombre(nombre);
		model.addAttribute(torneo.get().getNombre());
		
		if(torneo.isPresent()) { //Comprobamos si el torneo existe
			if (PartidoRepository.getPartidos(torneo.get())!=null) { //Si el torneo tiene partidos, se borran los partidos
				List<Partido> partidos = PartidoRepository.getPartidos(torneo.get());
				for (Partido p : partidos) {
					PartidoRepository.delete(p);
				}
			}
			TorneoRepository.delete(torneo.get());
			model.addAttribute("borrado",true);
		}else {
			model.addAttribute("borrado",false);
		}
		
		return "torneoDelete_template";
	}
	
	//Edita un torneo
	@RequestMapping("/torneos/editar/{nombre}")
	public String torneoEditar(Model model, @PathVariable String nombre) {
    	Optional <Torneo> torneo = TorneoRepository.findByNombre(nombre);
    	
    	if (torneo.isPresent()) {
    		model.addAttribute("torneo",torneo.get());
    		model.addAttribute("equipos",EquipoRepository.getEquipos(torneo.get()));
    		return "torneoEditar_template";
    	}else {
    		return "torneos_template";
    	}
	}
	
	@PostMapping("/torneos/editar")
	public String torneoEditado(Model model, Torneo torneo, String nombreactual){
		Optional <Torneo> revisar = TorneoRepository.findByNombre(torneo.getNombre());
		
		if (revisar.isPresent() && !(torneo.getNombre().equals(nombreactual))){
			model.addAttribute("disponible",false);
		}else {
			if(torneo.getGanador().equals(""))
			{
				torneo.setGanador(null);
			}
			TorneoRepository.save(torneo);
			model.addAttribute("disponible",true);
		}
		model.addAttribute("nombre",torneo.getNombre());
		return "torneoEditado_template";
	}
}
