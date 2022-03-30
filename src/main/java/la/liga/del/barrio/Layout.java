package la.liga.del.barrio;

import java.io.IOException;
import java.io.Writer;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template.Fragment;

import la.liga.del.barrio.user.UserRepository;

class Layout implements Mustache.Lambda {
	@Autowired
	private UserRepository userRepository;
	
	  String body;
	  
	  @ModelAttribute
		public void addAttributes(Model model, HttpServletRequest request) {
			
			Principal principal = request.getUserPrincipal();
			
			if (principal != null) {
				model.addAttribute("logged",true);
				model.addAttribute("userName",principal.getName());
				model.addAttribute("admin", request.isUserInRole("ADMIN"));
				model.addAttribute("delegado", request.isUserInRole("DELEGADO"));
				model.addAttribute("equipo",userRepository.findByName(principal.getName()).get().getEquipo());
			}else {
				model.addAttribute("logged",false);
			}
		}

	  private  Mustache.Compiler compiler;

	  public Layout( Mustache.Compiler compiler) {
	    this.compiler = compiler;
	  }
	  @Override
	  public void execute(Fragment frag, Writer out) throws IOException {
	    body = frag.execute();
	    compiler.compile("{{>layout}}").execute(frag.context(), out);
	  }
	}