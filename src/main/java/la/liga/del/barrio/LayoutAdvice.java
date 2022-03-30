package la.liga.del.barrio;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.samskivert.mustache.Mustache;

@ControllerAdvice
class LayoutAdvice {
	private final Mustache.Compiler compiler;
	
	@ModelAttribute
	public void addAttributes(Model model, HttpServletRequest request) {
		
		Principal principal = request.getUserPrincipal();
		
		if (principal != null) {
			model.addAttribute("logged",true);
			model.addAttribute("userName",principal.getName());
			model.addAttribute("admin", request.isUserInRole("ADMIN"));
		}else {
			model.addAttribute("logged",false);
		}
	}

	@Autowired
	public LayoutAdvice( Mustache.Compiler compiler) {
		this.compiler = compiler;
	}

	@ModelAttribute("layout")
	public Mustache.Lambda layout(Map<String, Object> model) {
		return new Layout(compiler);
	}
}