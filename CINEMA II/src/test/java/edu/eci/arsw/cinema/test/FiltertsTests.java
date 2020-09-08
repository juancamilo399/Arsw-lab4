package edu.eci.arsw.cinema.test;

import edu.eci.arsw.cinema.filtros.impl.AvailabilityFilter;
import edu.eci.arsw.cinema.filtros.impl.GenreFilter;
import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.persistence.impl.InMemoryCinemaPersistence;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;

public class FiltertsTests {
    private InMemoryCinemaPersistence memoryTester;
    private AvailabilityFilter availabilityFilter;
    private GenreFilter genreFilter;

    @Before
    public void setUp(){
        memoryTester = new InMemoryCinemaPersistence();
        availabilityFilter = new AvailabilityFilter();
        genreFilter = new GenreFilter();
    }

    @Test
    public void filterByGenre() throws CinemaPersistenceException {
        InMemoryCinemaPersistence ipct=new InMemoryCinemaPersistence();

        String functionDate = "2018-12-18 15:30";
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction funct1 = new CinemaFunction(new Movie("SuperHeroes Movie 2","Action"),functionDate);
        CinemaFunction funct2 = new CinemaFunction(new Movie("The Night 2","Horror"),functionDate);
        functions.add(funct1);
        functions.add(funct2);
        Cinema c=new Cinema("Movies Bogotá", functions);
        ipct.saveCinema(c);
        List<CinemaFunction> selectedMovies= genreFilter.filter(functions,"Action");
        selectedMovies.stream().forEach(f -> assertTrue(f.getMovie().getGenre().equals("Action")));
    }

    @Test
    public void filterByAvailability() throws CinemaPersistenceException, CinemaException {
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction function = new CinemaFunction(new Movie("Hulk", "Sad"), "2015-11-15 13:12");
        functions.add(function);
        Cinema c = new Cinema("Cinema Tickets", functions);
        try {
            memoryTester.saveCinema(c);
            memoryTester.buyTicket(1, 2, "Cinema Tickets", "2015-11-15 13:12", "Hulk");
            memoryTester.buyTicket(2, 3, "Cinema Tickets", "2015-11-15 13:12", "Hulk");
            memoryTester.buyTicket(3, 3, "Cinema Tickets", "2015-11-15 13:12", "Hulk");
        } catch (CinemaPersistenceException exception){
            fail();
        }
        List<CinemaFunction> selectedMovies= availabilityFilter.filter(functions,"83");
        selectedMovies.stream().forEach(f -> assertTrue(f.getSeats().size()*f.getSeats().get(0).size()>83));
    }
}
