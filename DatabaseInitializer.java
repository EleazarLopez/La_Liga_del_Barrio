package la.liga.del.barrio;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import la.liga.del.barrio.equipo.Equipo;
import la.liga.del.barrio.jugador.Jugador;
import la.liga.del.barrio.user.User;
import la.liga.del.barrio.torneo.Torneo;

import la.liga.del.barrio.equipo.EquipoRepository;
import la.liga.del.barrio.jugador.JugadorRepository;
import la.liga.del.barrio.partido.Partido;
import la.liga.del.barrio.partido.PartidoRepository;
import la.liga.del.barrio.torneo.TorneoRepository;
import la.liga.del.barrio.user.UserRepository;

@Service
public class DatabaseInitializer {

	@Autowired
	private EquipoRepository equipoRepository;
	
	@Autowired
	private JugadorRepository jugadorRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TorneoRepository torneoRepository;
	
	@Autowired
	private PartidoRepository partidoRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void init() {

		// Al iniciar la aplicación, debe haber al menos un usuario admin creado
		if (!userRepository.findByName("admin").isPresent()) {
			// Si no está presente significa que es la primera vez que se arranca la aplicación en la bbdd y hay que crear todos los ejemplos de test
			
			// Usuarios de ejemplo
			List<String> Delegado = new ArrayList<String>();
			List<String> Usuario = new ArrayList<String>();
			List<String> Admin = new ArrayList<String>();
			Delegado.add("DELEGADO");
			Admin.add("ADMIN");
			Usuario.add("USUARIO");
			
			User test1 = new User("test1", passwordEncoder.encode("test1"), "email@propietarioEquipo1.com");
			User test2 = new User("test2", passwordEncoder.encode("test2"), "email@propietarioEquipo2.com");
			User test3 = new User("test3", passwordEncoder.encode("test3"), "email@propietarioEquipo3.com");
			User test4 = new User("test4", passwordEncoder.encode("test4"), "email@propietarioEquipo4.com");
			User test5 = new User("test5", passwordEncoder.encode("test5"), "email@propietarioEquipo5.com");
			User admin = new User("admin", passwordEncoder.encode("admin"),"email@admin.com");

			test1.setRoles(Delegado);
			test2.setRoles(Delegado);
			test3.setRoles(Delegado);
			test4.setRoles(Delegado);
			admin.setRoles(Admin);
			userRepository.save(test1);
			userRepository.save(test2);
			userRepository.save(test3);
			userRepository.save(test4);
			userRepository.save(test5);
			userRepository.save(admin);
			
					
			// Equipos de ejemplo
			
			Equipo equipo1 = new Equipo("Equipo1","CorreoEquipo1@urjc.es","TelefonoEquipo1",test1);
			Equipo equipo2 = new Equipo("Equipo2","CorreoEquipo2@urjc.es","TelefonoEquipo2",test2);
			Equipo equipo3 = new Equipo("Equipo3","CorreoEquipo3@urjc.es","TelefonoEquipo3",test3);
			Equipo equipo4 = new Equipo("Equipo4","CorreoEquipo4@urjc.es","TelefonoEquipo4",test4);

			equipoRepository.save(equipo1);
			equipoRepository.save(equipo2);
			equipoRepository.save(equipo3);
			equipoRepository.save(equipo4);
			
			
			// Jugadores de ejemplo
			
			Jugador jugador1 = new Jugador("Jugador1Equipo1","1","DNIJugador1Equipo1");
			Jugador jugador2 = new Jugador("Jugador2Equipo1","2","DNIJugador2Equipo1");
			Jugador jugador3 = new Jugador("Jugador3Equipo1","3","DNIJugador3Equipo1");
			Jugador jugador4 = new Jugador("Jugador4Equipo1","4","DNIJugador4Equipo1");
			Jugador jugador5 = new Jugador("Jugador1Equipo2","1","DNIJugador1Equipo2");
			Jugador jugador6 = new Jugador("Jugador2Equipo2","2","DNIJugador2Equipo2");
			Jugador jugador7 = new Jugador("Jugador3Equipo2","3","DNIJugador3Equipo2");
			Jugador jugador8 = new Jugador("Jugador4Equipo2","4","DNIJugador4Equipo2");
			Jugador jugador9 = new Jugador("Jugador1Equipo3","1","DNIJugador1Equipo3");
			Jugador jugador10 = new Jugador("Jugador2Equipo3","2","DNIJugador2Equipo3");
			Jugador jugador11 = new Jugador("Jugador3Equipo3","3","DNIJugador3Equipo3");
			Jugador jugador12 = new Jugador("Jugador4Equipo3","4","DNIJugador4Equipo3");
			Jugador jugador13 = new Jugador("Jugador1Equipo4","1","DNIJugador1Equipo4");
			Jugador jugador14 = new Jugador("Jugador2Equipo4","2","DNIJugador2Equipo4");
			Jugador jugador15 = new Jugador("Jugador3Equipo4","3","DNIJugador3Equipo4");
			Jugador jugador16 = new Jugador("Jugador4Equipo4","4","DNIJugador4Equipo4");

			
			jugador1.setEquipo(equipo1);
			jugador2.setEquipo(equipo1);
			jugador3.setEquipo(equipo1);
			jugador4.setEquipo(equipo1);
			jugador5.setEquipo(equipo2);
			jugador6.setEquipo(equipo2);
			jugador7.setEquipo(equipo2);
			jugador8.setEquipo(equipo2);
			jugador9.setEquipo(equipo3);
			jugador10.setEquipo(equipo3);
			jugador11.setEquipo(equipo3);
			jugador12.setEquipo(equipo3);
			jugador13.setEquipo(equipo4);
			jugador14.setEquipo(equipo4);
			jugador15.setEquipo(equipo4);
			jugador16.setEquipo(equipo4);
			
			
			jugadorRepository.save(jugador1);
			jugadorRepository.save(jugador2);
			jugadorRepository.save(jugador3);
			jugadorRepository.save(jugador4);
			jugadorRepository.save(jugador5);
			jugadorRepository.save(jugador6);
			jugadorRepository.save(jugador7);
			jugadorRepository.save(jugador8);
			jugadorRepository.save(jugador9);
			jugadorRepository.save(jugador10);
			jugadorRepository.save(jugador11);
			jugadorRepository.save(jugador12);
			jugadorRepository.save(jugador13);
			jugadorRepository.save(jugador14);
			jugadorRepository.save(jugador15);
			jugadorRepository.save(jugador16);
			
			// Torneos de ejemplo
			
			Torneo Liga = new Torneo("Liga");
			Torneo Copa = new Torneo ("Copa");
			Torneo Selecciones = new Torneo ("Selecciones");
			
			torneoRepository.save(Liga);
			torneoRepository.save(Copa);
			torneoRepository.save(Selecciones);
			
			// Partidos de ejemplo
			
			Partido partido1 = new Partido(Liga, equipo1,equipo2,"27/03/2022","20:00");
			Partido partido2 = new Partido(Liga, equipo1,equipo3,"29/03/2022","20:00");
			Partido partido3 = new Partido(Liga, equipo1,equipo4,"31/03/2022","20:00");
			Partido partido4 = new Partido(Liga, equipo2,equipo1,"02/04/2022","20:00");
			Partido partido5 = new Partido(Liga, equipo2,equipo3,"04/04/2022","20:00");
			Partido partido6 = new Partido(Liga, equipo2,equipo4,"06/04/2022","20:00");
			Partido partido7 = new Partido(Liga, equipo3,equipo1,"08/04/2022","20:00");
			Partido partido8 = new Partido(Liga, equipo3,equipo2,"10/04/2022","20:00");
			Partido partido9 = new Partido(Liga, equipo3,equipo4,"12/04/2022","20:00");
			Partido partido10 = new Partido(Liga, equipo4,equipo1,"14/04/2022","20:00");
			Partido partido11 = new Partido(Liga, equipo4,equipo2,"16/04/2022","20:00");
			Partido partido12 = new Partido(Liga, equipo4,equipo3,"18/04/2022","20:00");
			
			Partido partido13 = new Partido(Copa, equipo1,equipo2,"27/03/2022","20:00");
			Partido partido14 = new Partido(Copa, equipo1,equipo3,"29/03/2022","20:00");
			Partido partido15 = new Partido(Copa, equipo2,equipo1,"31/03/2022","20:00");
			Partido partido16 = new Partido(Copa, equipo2,equipo3,"02/04/2022","20:00");
			Partido partido17 = new Partido(Copa, equipo3,equipo1,"04/04/2022","20:00");
			Partido partido18 = new Partido(Copa, equipo3,equipo2,"06/04/2022","20:00");
			
			Partido partido19 = new Partido(Selecciones, equipo1,equipo3,"27/03/2022","20:00");
			Partido partido20 = new Partido(Selecciones, equipo1,equipo4,"29/03/2022","20:00");
			Partido partido21 = new Partido(Selecciones, equipo3,equipo1,"31/03/2022","20:00");
			Partido partido22 = new Partido(Selecciones, equipo3,equipo4,"02/04/2022","20:00");
			Partido partido23 = new Partido(Selecciones, equipo4,equipo1,"04/04/2022","20:00");
			Partido partido24 = new Partido(Selecciones, equipo4,equipo3,"06/04/2022","20:00");
			
			partidoRepository.save(partido1);
			partidoRepository.save(partido2);
			partidoRepository.save(partido3);
			partidoRepository.save(partido4);
			partidoRepository.save(partido5);
			partidoRepository.save(partido6);
			partidoRepository.save(partido7);
			partidoRepository.save(partido8);
			partidoRepository.save(partido9);
			partidoRepository.save(partido10);
			partidoRepository.save(partido11);
			partidoRepository.save(partido12);
			partidoRepository.save(partido13);
			partidoRepository.save(partido14);
			partidoRepository.save(partido15);
			partidoRepository.save(partido16);
			partidoRepository.save(partido17);
			partidoRepository.save(partido18);
			partidoRepository.save(partido19);
			partidoRepository.save(partido20);
			partidoRepository.save(partido21);
			partidoRepository.save(partido22);
			partidoRepository.save(partido23);
			partidoRepository.save(partido24);
		}
		
	}

}