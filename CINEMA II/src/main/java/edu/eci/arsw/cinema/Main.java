package edu.eci.arsw.cinema;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.services.CinemaServices;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

import java.util.List;


public class Main {

    public static void main(String a[]) throws CinemaException, CinemaPersistenceException {
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        CinemaServices cs = ac.getBean(CinemaServices.class);
        System.out.println("Se registra el cinema nuevo : CinemaNuevo");
        cs.saveCinema(new Cinema("CinemaNuevo",cs.getFunctionsbyCinemaAndDate("cinemaX", "2018-12-18 15:30")));
        System.out.println("Consulta de todos los cinemas");
        for(Cinema c : cs.getAllCinemas()){
            System.out.println(c.toString());
        }
        System.out.println("---------------------------------------------------");
        System.out.println("Consulta de las funciones del cinemaX el dia : 2018-12-18 15:30");
        List<CinemaFunction> lista = cs.getFunctionsbyCinemaAndDate("cinemaX", "2018-12-18 15:30");
        for (CinemaFunction function : lista) {
            System.out.println(function.toString());
        }
        System.out.println("--------------------------------------------------");
        System.out.println("Compra de un ticket del cinemaX el dia 2018-12-18 15:30 para la pelicula SuperHeroes Movie");
        cs.buyTicket(1,2,"cinemaX","2018-12-18 15:30","SuperHeroes Movie");
        List<CinemaFunction> lista2 = cs.getFunctionsbyCinemaAndDate("cinemaX", "2018-12-18 15:30");
        for (CinemaFunction function : lista2) {
            System.out.println(function.toString());
        }


        List<CinemaFunction>selected_functions=cs.filterCinemas("cinemaX","2018-12-18 15:30","Horror");
        System.out.println("Peliculas de terror");
        selected_functions.stream().forEach(f-> System.out.println(f.getMovie().getName()));
    }
}
