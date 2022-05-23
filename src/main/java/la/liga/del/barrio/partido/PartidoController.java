package la.liga.del.barrio.partido;

import java.security.Principal;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import la.liga.del.barrio.equipo.EquipoRepository;
import la.liga.del.barrio.torneo.TorneoRepository;
import la.liga.del.barrio.user.UserRepository;

@Controller
public class PartidoController {
	
	@Autowired
	private TorneoRepository TorneoRepository;
	
	@Autowired
	private PartidoRepository PartidoRepository;
	
	@Autowired
	private EquipoRepository EquipoRepository;
	
	@Autowired
	private UserRepository UserRepository;
	
	// Consultar un partido
	@RequestMapping("/partido/{id}")
	public String partidoDetail( Model model, @PathVariable long id) {
		
		Optional <Partido> partido = PartidoRepository.findById(id);
		
		if (partido.isPresent()) {
			model.addAttribute("partido", PartidoRepository.findById(id).get());
			model.addAttribute("equipo1", PartidoRepository.findById(id).get().getEquipo1());
			model.addAttribute("equipo2", PartidoRepository.findById(id).get().getEquipo2());
			model.addAttribute("torneo", PartidoRepository.findById(id).get().getTorneo());
			model.addAttribute("existe", true);
		}else {
			model.addAttribute("existe", false);
		}
		return "partidoDetail_template";
	}
	
	// Crear un partido
	@RequestMapping("/partido/nuevo/{torneo}")
	public String partidoNuevo( Model model, @PathVariable String torneo, HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		//Comprobamos si el usuario a crear el partido es un admin
		if (UserRepository.findByName(principal.getName()).isPresent() && UserRepository.findByName(principal.getName()).get().getRoles().contains("ADMIN")){
			model.addAttribute("administrador",true);
			//Se comprueba si el torneo para el que se quiere crear el partido existe
			if(TorneoRepository.findByNombre(torneo).isPresent()) {
				model.addAttribute("existe",true);
				model.addAttribute("torneo", TorneoRepository.findByNombre(torneo).get());
				model.addAttribute("equipos", EquipoRepository.findAll());
			}else {
				model.addAttribute("existe",false);
			}
		}else {
			model.addAttribute("administrador",false);
		}
		return "partidoNuevo_template";
	}
	
	@PostMapping("/partido/nuevo")
	public String partidoAdd( Model model, Partido partido, String nombretorneo, String equipolocal, String equipovisitante, HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		//Comprobamos si el usuario a crear el partido es un admin
		if (UserRepository.findByName(principal.getName()).isPresent() && UserRepository.findByName(principal.getName()).get().getRoles().contains("ADMIN")){
			model.addAttribute("administrador",true);
			//Se comprueba si el torneo para el que se quiere crear el partido existe
			if(TorneoRepository.findByNombre(nombretorneo).isPresent()) {
				model.addAttribute("existe", true);
				//Se comprueba que el equipo local y visitante no sean el mismo
				if (equipolocal.equals(equipovisitante)){
					model.addAttribute("equiposok",false);
				}else {
					model.addAttribute("equiposok",true);
					model.addAttribute("torneo", nombretorneo);
					partido.setTorneo(TorneoRepository.findByNombre(nombretorneo).get());
					partido.setEquipo1(EquipoRepository.findByNombre(equipolocal).get());
					partido.setEquipo2(EquipoRepository.findByNombre(equipovisitante).get());
					PartidoRepository.save(partido);
					model.addAttribute("partido", partido);
					model.addAttribute("equipo1", partido.getEquipo1().getNombre());
					model.addAttribute("equipo2", partido.getEquipo2().getNombre());
				}
			}else {
				model.addAttribute("existe", false);
			}
		}else {
			model.addAttribute("administrador",false);
		}
		return "partidoSave_template";
	}
	
	// Editar un partido
	@RequestMapping("/partido/editar/{id}")
	public String partidoEditar( Model model, @PathVariable long id) {
		
		//No se hacen mas comprobaciones de administrado porque se prohibe el acceso desde "SecurityConfiguration.java"
		
		//Comprobamos si el partido a editar existe
		if(PartidoRepository.findById(id).isPresent()) {
			model.addAttribute("existe",true);
			model.addAttribute("partido",PartidoRepository.findById(id).get());
			model.addAttribute("torneo",PartidoRepository.findById(id).get().getTorneo().getNombre());
			model.addAttribute("equipo1",PartidoRepository.findById(id).get().getEquipo1());
			model.addAttribute("equipo2",PartidoRepository.findById(id).get().getEquipo2());
			model.addAttribute("equipos",EquipoRepository.getEquipos(PartidoRepository.findById(id).get().getTorneo()));
		}else {
			model.addAttribute("existe",false);
		}
		
		return "partidoEditar_template";
	}
	
	@PostMapping("/partido/editar")
	public String partidoEditado( Model model, Partido partido, String torneopartido, String equipolocal, String equipovisitante) {
		
		//No se hacen mas comprobaciones de administrado porque se prohibe el acceso desde "SecurityConfiguration.java"
		
		//Comprobamos si el partido a editar existe
		if(PartidoRepository.findById(partido.getId()).isPresent()){
			model.addAttribute("partido",partido);
			model.addAttribute("torneo",PartidoRepository.findById(partido.getId()).get().getTorneo());
			//Se comprueba que el equipo local y visitante no sean el mismo
			if (equipolocal.equals(equipovisitante)){
				model.addAttribute("equiposok",false);
			}else {
				model.addAttribute("equiposok",true);
				partido.setEquipo1(EquipoRepository.findByNombre(equipolocal).get());
				partido.setEquipo2(EquipoRepository.findByNombre(equipovisitante).get());
				partido.setTorneo(TorneoRepository.findByNombre(torneopartido).get());
				PartidoRepository.save(partido);
			}
		}
		return "partidoEditado_template";
	}
	
	// Eliminar un partido
	@RequestMapping("/partido/delete/{id}")
	public String partidoDelete( Model model, @PathVariable long id) {
		
		//No se hacen mas comprobaciones de administrado porque se prohibe el acceso desde "SecurityConfiguration.java"
		model.addAttribute("torneo",PartidoRepository.findById(id).get().getTorneo());
		
		//Comprobamos si el partido a editar existe
		if(PartidoRepository.findById(id).isPresent()) {
			PartidoRepository.delete(PartidoRepository.findById(id).get());
			model.addAttribute("borrado",true);
		}else {
			model.addAttribute("borrado",false);
		}

		return "partidoDelete_template";
	}
	
}