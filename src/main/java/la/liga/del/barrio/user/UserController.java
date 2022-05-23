package la.liga.del.barrio.user;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import la.liga.del.barrio.partido.Partido;
import la.liga.del.barrio.partido.PartidoRepository;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private PartidoRepository partidoRepository;
	
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
	
	//Editar usuario
	
	@RequestMapping("/cuenta")
	public String cuentaForm(Model model, HttpServletRequest request) {
		
		Optional <User> user = userRepository.findByName(request.getUserPrincipal().getName());
		
		if(user.isPresent()) { // Comprueba si el id coincide con un usuario de la bd
			model.addAttribute(user.get()); // Añade el usuario al modelo, para su posterior edición
			if(user.get().getEquipo()!=null) {
				model.addAttribute("nombreequipo",user.get().getEquipo().getNombre());
				model.addAttribute("equipo",user.get().getEquipo());
				model.addAttribute("tiene",true);
			}else {
				model.addAttribute("tiene",false);
			}
			return "cuenta_template";
		}else {
			model.addAttribute("compruebauser",false);
			return "cuentaSave_template";
		}
	}
		
	@PostMapping("/cuenta/save")
	public String cuentaSave(Model model, User user, String contraseñactual, String verificacontraseña, HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		Optional<User> actual = userRepository.findByName(principal.getName());
		
		Optional <User> usuario = userRepository.findByName(user.getName());
			
		if(usuario.get().getID()==actual.get().getID()) { // Aseguramos que sólo el propietario de la cuenta puede editarla
			model.addAttribute("compruebauser",true);
			// Verificamos contraseña actual para aceptar el cambio de contraseña
			if (passwordEncoder.matches(contraseñactual,usuario.get().getPassword())) {
				model.addAttribute("contraseñaactual",true);
				if (user.getPassword().equals(verificacontraseña)){
					if(usuario.get().getRoles().contains("DELEGADO")) {
						user.setEquipo(usuario.get().getEquipo());
						model.addAttribute("contraseñanueva",true);
						user.setencodedPassword(passwordEncoder.encode(user.getPassword()));
						user.setRoles(usuario.get().getRoles());
						userRepository.save(user);
					}else {
						model.addAttribute("contraseñanueva",true);
						user.setencodedPassword(passwordEncoder.encode(user.getPassword()));
						user.setRoles(usuario.get().getRoles());
						userRepository.save(user);
					}
				}else {
					model.addAttribute("contraseñanueva",false); // Si no coincide, se declinan todos los cambios del form
				}
			}else {
				model.addAttribute("contraseñaactual",false); // Si no concide, se declinan todos los cambios del form
			}
		}else {
			model.addAttribute("compruebauser",false);
		}

		model.addAttribute("nombre",user.getName());

		return "cuentaSave_template";
	}
	
	// Consultar cuentas de usuario
	
	@RequestMapping("/cuentas")
	public String cuentasForm(Model model) {
		
		model.addAttribute("usuarios", userRepository.findAll());

		return "cuentas_template";
	}
	
	// Eliminar cuentas de usuario	
		
	@RequestMapping("/cuentas/eliminar/{id}")
	public String cuentasDelete(Model model, @PathVariable Long id) {
		
		Optional <User> usuario = userRepository.findById(id);
	
		if (usuario.get().getRoles().contains("ADMIN")) {
			model.addAttribute("nombre",usuario.get().getName());
			model.addAttribute("cuentaadmin",true);
			return "cuentasAdmin_template";
		}else {
			// Si tiene equipo, hay que eliminar los partidos en los que participa
			if (usuario.get().getEquipo()!=null) {
				if (partidoRepository.getPartidosDeEquipo(usuario.get().getEquipo())!=null) {
					List<Partido> partidos = partidoRepository.getPartidosDeEquipo(usuario.get().getEquipo());
					// Si tiene partidos, se borran
					for (Partido p : partidos) {
						partidoRepository.delete(p);
					}
				}
				// equipoRepository.delete(usuario.get().getEquipo()); no hace falta porque el orphan removal lo elimina
			}
			usuario.get().setRoles(null);
			userRepository.delete(usuario.get());
		}
		model.addAttribute("usuarios", userRepository.findAll());

		return "cuentas_template";
	}
		
	// Actualizar rol de cuenta
		
	@RequestMapping("/cuentas/rolup/{nombre}")
	public String cuentasRolup(Model model, @PathVariable String nombre) {
		
		Optional <User> usuario = userRepository.findByName(nombre);
	
		if (usuario.get().getRoles().contains("ADMIN")) {
			model.addAttribute("nombre",usuario.get().getName());
			model.addAttribute("cuentaadmin",true);
			return "cuentasAdmin_template";
		}else {
			model.addAttribute("cuentaadmin",false);
			//Si el usuario no es delegado, lo convertimos en delegado
			if (!usuario.get().getRoles().contains("DELEGADO")) {
				usuario.get().getRoles().add("DELEGADO");
				usuario.get().getRoles().remove("USUARIO");
				userRepository.save(usuario.get());
			}else{// Si ya es delegado, y no tiene equipo, lo convertimos en usuario normal
				if (usuario.get().getEquipo()==null) {
					usuario.get().getRoles().add("USUARIO");
					usuario.get().getRoles().remove("DELEGADO");
					userRepository.save(usuario.get());
					model.addAttribute("delegadoconequipo",false);
				}else {
					//Si tiene equipo, entonces informamos de que no se puede cambiar el rol del usuario mientras tenga un equipo asignado
					model.addAttribute("delegadoconequipo",true);
					return "cuentasAdmin_template";
				}
			}
		}
		model.addAttribute("usuarios", userRepository.findAll());
		return "cuentas_template";
	}
	
}