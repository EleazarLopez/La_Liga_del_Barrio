package la.liga.del.barrio.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	// Cifrado de contraseña con bcrypt
	
	@Autowired
	public RepositoryUserDetailsService userDetailService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10, new SecureRandom()); //Cifrador de passwords
	}
	
	// La autenticación se hace contra el hash, y no contra la contraseña en texto plano
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Login
		http.formLogin().loginPage("/login");
		http.formLogin().usernameParameter("nombre");
		http.formLogin().passwordParameter("contraseña");
		http.formLogin().defaultSuccessUrl("/");
		http.formLogin().failureUrl("/loginError");

        // Logout
		http.logout().logoutUrl("/logout");
		http.logout().logoutSuccessUrl("/");
		
		// Registro
		http.authorizeRequests().antMatchers("/registro").permitAll();
		http.authorizeRequests().antMatchers("/registro/save").permitAll();
		
		
		// Páginas públicas
		http.authorizeRequests().antMatchers("/").permitAll();
		http.authorizeRequests().antMatchers("/login").permitAll();
		http.authorizeRequests().antMatchers("/loginError").permitAll();
		http.authorizeRequests().antMatchers("/logout").permitAll();
		http.authorizeRequests().antMatchers("/torneos").permitAll();
		
		// Páginas privadas
		http.authorizeRequests().antMatchers("/torneos/nuevo").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/torneos/delete").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/torneos/delete/*").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/torneos/editar").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/torneos/editar/*").hasAnyRole("ADMIN");
		
		http.authorizeRequests().antMatchers("/equipo/nuevo").hasAnyRole("ADMIN","DELEGADO");
		http.authorizeRequests().antMatchers("/equipo/nuevo/*").hasAnyRole("ADMIN","DELEGADO");
		http.authorizeRequests().antMatchers("/equipos/editar/*").hasAnyRole("ADMIN","DELEGADO");
		http.authorizeRequests().antMatchers("/equipos/editar").hasAnyRole("ADMIN","DELEGADO");
		http.authorizeRequests().antMatchers("/equipos/delete").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/equipos/delete/*").hasAnyRole("ADMIN");
		
		http.authorizeRequests().antMatchers("/jugador/nuevo").hasAnyRole("ADMIN","DELEGADO");
		http.authorizeRequests().antMatchers("/jugador/nuevo/*").hasAnyRole("ADMIN","DELEGADO");
		http.authorizeRequests().antMatchers("/jugador/delete").hasAnyRole("ADMIN","DELEGADO");
		http.authorizeRequests().antMatchers("/jugador/delete/*").hasAnyRole("ADMIN","DELEGADO");
		http.authorizeRequests().antMatchers("/jugador/editar").hasAnyRole("ADMIN","DELEGADO");
		http.authorizeRequests().antMatchers("/jugador/editar/*").hasAnyRole("ADMIN","DELEGADO");
		
		http.authorizeRequests().antMatchers("/partido/nuevo").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/partido/nuevo/*").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/partido/editar").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/partido/editar/*").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/partidos/delete").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/partidos/delete/*").hasAnyRole("ADMIN");
		
		http.authorizeRequests().antMatchers("/cuentas").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/cuentas/eliminar/*").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/cuentas/rolup/*").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers("/cuenta/*").hasAnyRole("ADMIN","USUARIO","DELEGADO");
		http.authorizeRequests().antMatchers("/cuenta/save").hasAnyRole("ADMIN","USUARIO","DELEGADO");
		
	}
}