package la.liga.del.barrio.partido;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import la.liga.del.barrio.equipo.EquipoRepository;
import la.liga.del.barrio.torneo.Torneo;
import la.liga.del.barrio.torneo.TorneoRepository;

@Controller
public class PartidoController {
	
	@Autowired
	private TorneoRepository TorneoRepository;
	
	@Autowired
	private PartidoRepository PartidoRepository;
	
	@Autowired
	private EquipoRepository EquipoRepository;
	
	/* SIN USO POR AHORA
	// Consultar todos los partidos
	@RequestMapping("/partidos")
	public String partidoList( Model model) {
		
		model.addAttribute("partidos",PartidoRepository.findAll());

		return "partidosList_template";
	}*/
	
	// Consultar un partido
	@RequestMapping("/partido/{id}")
	public String partidoDetail( Model model, @PathVariable long id) {
		
		Optional <Partido> partido = PartidoRepository.findById(id);
		
		if (partido.isPresent()) {
			model.addAttribute("partido", PartidoRepository.findById(id).get());
			model.addAttribute("equipo1", PartidoRepository.findById(id).get().getEquipo1());
			model.addAttribute("equipo2", PartidoRepository.findById(id).get().getEquipo2());
			model.addAttribute("torneo", PartidoRepository.findById(id).get().getTorneo());
			return "partidoDetail_template";
		}else {
			return "home_template";
		}	
	}
	
	// Crear un partido
	@RequestMapping("/partido/nuevo/{torneo}")
	public String partidoNuevo( Model model, @PathVariable String torneo) {
		Optional <Torneo> revisar = TorneoRepository.findByNombre(torneo);
		if(revisar.isPresent()) {
			model.addAttribute("existe",true);
			model.addAttribute("torneo", revisar.get());
			model.addAttribute("equipos", EquipoRepository.findAll());
		}else {
			model.addAttribute("existe",false);
		}
		
		return "partidoNuevo_template";
	}
	
	@PostMapping("/partido/nuevo")
	public String partidoAdd( Model model, Partido partido, String nombretorneo, String equipolocal, String equipovisitante) {
		
		partido.setTorneo(TorneoRepository.findByNombre(nombretorneo).get());
		model.addAttribute("torneo", partido.getTorneo().getNombre());
		if (equipolocal.equals(equipovisitante)){
			model.addAttribute("equiposok",false);
		}else {
			model.addAttribute("equiposok",true);
			partido.setEquipo1(EquipoRepository.findByNombre(equipolocal).get());
			partido.setEquipo2(EquipoRepository.findByNombre(equipovisitante).get());
			PartidoRepository.save(partido);
			model.addAttribute("partido", partido);
			model.addAttribute("equipo1", partido.getEquipo1().getNombre());
			model.addAttribute("equipo2", partido.getEquipo2().getNombre());
		}
		
		return "partidoSave_template";
	}
	
	// Editar un partido
	@RequestMapping("/partido/editar/{id}")
	public String partidoEditar( Model model, @PathVariable long id) {
		
		Optional <Partido> partido = PartidoRepository.findById(id);
		if(partido.isPresent()) {
			model.addAttribute("existe",true);
			model.addAttribute("partido",partido.get());
			model.addAttribute("torneo",partido.get().getTorneo().getNombre());
			model.addAttribute("equipo1",partido.get().getEquipo1());
			model.addAttribute("equipo2",partido.get().getEquipo2());
			model.addAttribute("equipos",EquipoRepository.getEquipos(partido.get().getTorneo()));
		}else {
			model.addAttribute("existe",false);
		}
		
		return "partidoEditar_template";
	}
	
	@PostMapping("/partido/editar")
	public String partidoEditado( Model model, Partido partido, String torneopartido, String equipolocal, String equipovisitante) {
		
		Optional <Partido> revisar = PartidoRepository.findById(partido.getId());
		if(revisar.isPresent()){
			model.addAttribute("partido",partido);
			model.addAttribute("torneo",revisar.get().getTorneo());
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
		
		Optional <Partido> partido = PartidoRepository.findById(id);
		if(partido.isPresent()) {
			PartidoRepository.delete(partido.get());
			model.addAttribute("borrado",true);
		}else {
			model.addAttribute("borrado",false);
		}
		
		return "partidoDelete_template";
	}
	
}