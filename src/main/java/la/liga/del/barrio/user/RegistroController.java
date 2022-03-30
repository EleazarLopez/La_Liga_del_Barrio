package la.liga.del.barrio.user;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import la.liga.del.barrio.equipo.EquipoRepository;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistroController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EquipoRepository EquipoRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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
	
	//Crear nuevo usuario
	
	@RequestMapping("/registro")
	public String registroForm() {

		return "registro_template";
	}
	
	@PostMapping("/registro/save")
	public String registroSave(Model model, User user) {
		
		Optional <User> usuario = userRepository.findByName(user.getName());
		
		if (usuario.isPresent()){ // Si el nombre de usuario ya existe debe retornar que el nombre elegido no está disponible
			model.addAttribute("disponible",false);
			model.addAttribute("nombre",user.getName());
		}else { // Si el nombre está disponible, se crea el nuevo usuario con los roles seleccionados
			model.addAttribute("disponible",true);
			userRepository.save(new User(user.getName(),passwordEncoder.encode(user.getPassword()),user.getEmail()));
			model.addAttribute("nombre",user.getName());
		}
		//enviar correo
		return "registroSave_template";
	}
	
	//Editar usuario
	@RequestMapping("/cuenta/{nombre}")
	public String cuentaForm(Model model, @PathVariable String nombre) {
		Optional <User> user = userRepository.findByName(nombre);
		if(user.isPresent()) { // Falta comprobar si el usuario que intenta editar es el dueño de la cuenta
			model.addAttribute(user.get());
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
	public String cuentaSave(Model model, User user, String contraseñactual, String verificacontraseña, String nombreequipo) {
		
		Optional <User> usuario = userRepository.findByName(user.getName());
		
		if(usuario.isPresent()) {
			model.addAttribute("compruebauser",true);
			// Verificamos contraseña actual para aceptar el cambio de contraseña
			if (passwordEncoder.matches(contraseñactual,usuario.get().getPassword())) {
				model.addAttribute("contraseñaactual",true);
				if (user.getPassword().equals(verificacontraseña)){
					if(usuario.get().getRoles().contains("DELEGADO")) {
						user.setEquipo(EquipoRepository.findByNombre(nombreequipo).get());
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
	
	// Eliminar cuentas de usuario
	@RequestMapping("/cuentas")
	public String cuentasForm(Model model) {
		
		model.addAttribute("usuarios", userRepository.findAll());

		return "cuentas_template";
	}
	
	@RequestMapping("/cuentas/eliminar/{nombre}")
	public String cuentasDelete(Model model, @PathVariable String nombre) {
		
		Optional <User> usuario = userRepository.findByName(nombre);
	
		if (usuario.get().getRoles().contains("ADMIN")) {
			model.addAttribute("nombre",usuario.get().getName());
			return "cuentasAdmin_template";
		}else {
			userRepository.delete(usuario.get());
		}
		model.addAttribute("usuarios", userRepository.findAll());

		return "cuentas_template";
	}
	
	// Actualizar rol de cuenta básica
	
	@RequestMapping("/cuentas/rolup/{nombre}")
	public String cuentasRolup(Model model, @PathVariable String nombre) {
		
		Optional <User> usuario = userRepository.findByName(nombre);
	
		if (usuario.get().getRoles().contains("ADMIN")) {
			model.addAttribute("nombre",usuario.get().getName());
			return "cuentasAdmin_template";
		}else {
			usuario.get().getRoles().add("DELEGADO");
			userRepository.save(usuario.get());
		}
		model.addAttribute("usuarios", userRepository.findAll());

		return "cuentas_template";
	}

}