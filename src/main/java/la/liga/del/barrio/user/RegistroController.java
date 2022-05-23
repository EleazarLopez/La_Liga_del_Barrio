package la.liga.del.barrio.user;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistroController {
	
	@Autowired
	private UserRepository userRepository;
	
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
		return "registroSave_template";
	}
	

}