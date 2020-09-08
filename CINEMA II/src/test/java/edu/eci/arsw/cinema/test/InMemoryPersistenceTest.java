package edu.eci.arsw.cinema.test;

import edu.eci.arsw.cinema.filtros.impl.AvailabilityFilter;
import edu.eci.arsw.cinema.filtros.impl.GenreFilter;
import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.persistence.CinemaPersitence;
import edu.eci.arsw.cinema.persistence.impl.InMemoryCinemaPersistence;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cristian
 */
public class InMemoryPersistenceTest {

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
    public void shouldSaveCinema() throws CinemaPersistenceException {
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction function = new CinemaFunction(new Movie("Avengers ", "Drama"), "2015-11-15 13:12");
        functions.add(function);
        Cinema c = new Cinema ("Cinema Nuevo" , functions);
        memoryTester.saveCinema(c);
        assertEquals(c,memoryTester.getCinema("Cinema Nuevo"));
    }

    @Test
    public void shouldNotSaveExistingCinema() throws CinemaPersistenceException {
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction function = new CinemaFunction(new Movie("Spiderman", "Drama"), "2020-11-15 13:12");
        functions.add(function);
        Cinema c = new Cinema ("Cinema Viejo" , functions);
        memoryTester.saveCinema(c);
        try{
            memoryTester.saveCinema(c);
            fail();
        } catch(CinemaPersistenceException exception){
            assertEquals("The given cinema already exists: "+c.getName(),exception.getMessage());
        }
    }

    @Test
    public void shouldNotSaveANullCinema() throws CinemaPersistenceException {
        try {
            memoryTester.saveCinema(null);
        } catch(CinemaPersistenceException exception){
            assertEquals("Cannot save a null Cinema",exception.getMessage());
        }

    }
    @Test
    public void shouldNotGetANoExistCinema(){
        try{
            memoryTester.getCinema("Cinema F");
            fail();
        } catch (CinemaPersistenceException exception) {
            assertEquals("El cinema no existe",exception.getMessage());
        }
    }
    @Test
    public void shouldNotGetANullCinema(){
        try{
            memoryTester.getCinema(null);
            fail();
        } catch (CinemaPersistenceException exception) {
            assertEquals("El nombre del cinema no puede ser nulo",exception.getMessage());
        }
    }
    @Test
    public void shouldGetAllCinemas() throws CinemaPersistenceException {
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction function = new CinemaFunction(new Movie("Avengers ", "Drama"), "2015-11-15 13:12");
        functions.add(function);
        Cinema c = new Cinema ("Cinema Nuevo" , functions);
        Cinema c1 = new Cinema ("Cinema Caro" , functions);
        try {
            memoryTester.saveCinema(c);
            memoryTester.saveCinema(c1);
            //Los dos cinemas agregados y el creado por defecto en el constructor
            assertEquals(memoryTester.getCinemas().size(), 5);
        } catch (CinemaPersistenceException exception){
            fail();
        }
    }
    @Test
    public void shouldConsultFunctionsOfACinemaAndADate() throws CinemaPersistenceException {
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction function = new CinemaFunction(new Movie("Avengers ", "Drama"), "2015-11-15 13:12");
        CinemaFunction function1 = new CinemaFunction(new Movie("Spiderman ", "Action"), "2015-11-15 13:12");
        CinemaFunction function2 = new CinemaFunction(new Movie("Hulk", "Sad"), "2015-11-15 13:12");
        functions.add(function);
        functions.add(function1);
        functions.add(function2);
        try {
            Cinema c = new Cinema("Cinema action", functions);
            memoryTester.saveCinema(c);
            assertEquals(functions, memoryTester.getFunctionsbyCinemaAndDate("Cinema action", "2015-11-15 13:12"));
        } catch(CinemaPersistenceException exception){
            fail();
        }
    }

    @Test
    public void shouldNotConsultFunctionsWithANullParameter(){
        try {
            memoryTester.getFunctionsbyCinemaAndDate("cinema",null);
            fail();
        } catch (CinemaPersistenceException exception) {
            assertEquals("Alguno de los parametros es nulo",exception.getMessage());
        }
    }

    @Test
    public void shouldNotConsultFunctionsOfAInexistingCinema(){
        try{
            memoryTester.getFunctionsbyCinemaAndDate("cinemaF","2015-11-15 13:12");
            fail();
        } catch (CinemaPersistenceException exception) {
            assertEquals("El cinema no existe",exception.getMessage());
        }
    }

    @Test
    public void shouldBuyTickets() throws CinemaPersistenceException, CinemaException {
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction function = new CinemaFunction(new Movie("Hulk", "Sad"), "2015-11-15 13:12");
        functions.add(function);
        Cinema c = new Cinema("Cinema Tickets", functions);
        try {
            memoryTester.saveCinema(c);
            memoryTester.buyTicket(1, 2, "Cinema Tickets", "2015-11-15 13:12", "Hulk");
            memoryTester.buyTicket(2, 3, "Cinema Tickets", "2015-11-15 13:12", "Hulk");
            memoryTester.buyTicket(3, 3, "Cinema Tickets", "2015-11-15 13:12", "Hulk");
            assertEquals(memoryTester.getFunctionsbyCinemaAndDate("Cinema Tickets", "2015-11-15 13:12").get(0).getSeats().get(1).get(2), false);
            assertEquals(memoryTester.getFunctionsbyCinemaAndDate("Cinema Tickets", "2015-11-15 13:12").get(0).getSeats().get(2).get(3), false);
            assertEquals(memoryTester.getFunctionsbyCinemaAndDate("Cinema Tickets", "2015-11-15 13:12").get(0).getSeats().get(3).get(3), false);
        } catch (CinemaPersistenceException exception){
            fail();
        }
    }
    @Test
    public void shouldNotBuyTicketsWithANullParameter() {
        try {
            memoryTester.buyTicket(3, 3, null, "2015-11-15 13:12", "Hulk");
            fail();
        } catch (CinemaPersistenceException | CinemaException exception) {
            assertEquals("Alguno de los parametros es nulo",exception.getMessage());
        }
        try {
            memoryTester.buyTicket(3, 3, "Cinema x", null, "Hulk");
            fail();
        } catch (CinemaPersistenceException | CinemaException exception) {
            assertEquals("Alguno de los parametros es nulo",exception.getMessage());
        }
        try {
            memoryTester.buyTicket(3, 3, "Cinema x", "2015-11-15 13:12", null);
            fail();
        } catch (CinemaPersistenceException | CinemaException exception) {
            assertEquals("Alguno de los parametros es nulo",exception.getMessage());
        }
    }
    @Test
    public void shouldNotBuyTicketsOfAnInexistingCinema() {
        try {
            memoryTester.buyTicket(3, 3, "Cinema F", "2015-11-15 13:12", "Hulk");
            fail();
        } catch (CinemaPersistenceException | CinemaException exception) {
            assertEquals("El cinema no existe",exception.getMessage());
        }
    }
    @Test
    public void shouldNotBuyTicketsInNonexistentSeats(){
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction function = new CinemaFunction(new Movie("Hulk", "Sad"), "2015-11-15 13:12");
        functions.add(function);
        Cinema c = new Cinema("Cinema Tickets", functions);
        try {
            memoryTester.saveCinema(c);
            memoryTester.buyTicket(100, 2, "Cinema Tickets", "2015-11-15 13:12", "Hulk");
            memoryTester.buyTicket(900, 3, "Cinema Tickets", "2015-11-15 13:12", "Hulk");
            memoryTester.buyTicket(300, 3, "Cinema Tickets", "2015-11-15 13:12", "Hulk");
            assertEquals(memoryTester.getFunctionsbyCinemaAndDate("Cinema Tickets", "2015-11-15 13:12").get(0).getSeats().get(1).get(2), false);
            assertEquals(memoryTester.getFunctionsbyCinemaAndDate("Cinema Tickets", "2015-11-15 13:12").get(0).getSeats().get(2).get(3), false);
            assertEquals(memoryTester.getFunctionsbyCinemaAndDate("Cinema Tickets", "2015-11-15 13:12").get(0).getSeats().get(3).get(3), false);
        } catch (CinemaPersistenceException | CinemaException exception){
            assertEquals("Sillas inexistentes",exception.getMessage());
        }
    }

    @Test
    public void saveNewAndLoadTest() throws CinemaPersistenceException{
        InMemoryCinemaPersistence ipct=new InMemoryCinemaPersistence();

        String functionDate = "2018-12-18 15:30";
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction funct1 = new CinemaFunction(new Movie("SuperHeroes Movie 2","Action"),functionDate);
        CinemaFunction funct2 = new CinemaFunction(new Movie("The Night 2","Horror"),functionDate);
        functions.add(funct1);
        functions.add(funct2);
        Cinema c=new Cinema("Movies Bogotá", functions);
        ipct.saveCinema(c);
        
        assertNotNull("Loading a previously stored cinema returned null.",ipct.getCinema(c.getName()));
        assertEquals("Loading a previously stored cinema returned a different cinema.",ipct.getCinema(c.getName()), c);
    }


    @Test
    public void saveExistingCinemaTest() {
        InMemoryCinemaPersistence ipct=new InMemoryCinemaPersistence();
        
        String functionDate = "2018-12-18 15:30";
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction funct1 = new CinemaFunction(new Movie("SuperHeroes Movie 2","Action"),functionDate);
        CinemaFunction funct2 = new CinemaFunction(new Movie("The Night 2","Horror"),functionDate);
        functions.add(funct1);
        functions.add(funct2);
        Cinema c=new Cinema("Movies Bogotá", functions);
        
        try {
            ipct.saveCinema(c);
        } catch (CinemaPersistenceException ex) {
            fail("Cinema persistence failed inserting the first cinema.");
        }

        CopyOnWriteArrayList<CinemaFunction> functions2= new CopyOnWriteArrayList<>();
        CinemaFunction funct12 = new CinemaFunction(new Movie("SuperHeroes Movie 3","Action"),functionDate);
        CinemaFunction funct22 = new CinemaFunction(new Movie("The Night 3","Horror"),functionDate);
        functions.add(funct12);
        functions.add(funct22);
        Cinema c2=new Cinema("Movies Bogotá", (CopyOnWriteArrayList<CinemaFunction>) functions2);
        try{
            ipct.saveCinema(c2);
            fail("An exception was expected after saving a second cinema with the same name");
        }
        catch (CinemaPersistenceException ex){
            
        }
    }
    @Test
    public void shouldAddFunctionToACinema(){
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction function = new CinemaFunction(new Movie("Hulk", "Sad"), "2015-11-15 13:12");
        Cinema c = new Cinema("Cinema Tickets", functions);
        try {
            memoryTester.saveCinema(c);
            memoryTester.addFunctionToCinema(c,function);
            assertEquals(function.getMovie().getName(),c.getFunctions().get(0).getMovie().getName());
        } catch (CinemaPersistenceException exception){
            fail();
        }
    }
    @Test
    public void shouldNotAddAFunctionToANonExistingCinema(){
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction function = new CinemaFunction(new Movie("Hulk", "Sad"), "2015-11-15 13:12");
        Cinema c = new Cinema("Cinema Tickets", functions);
        try {
            memoryTester.addFunctionToCinema(c,function);
            fail();
        } catch (CinemaPersistenceException exception){
            assertEquals(exception.getMessage(),"El cinema no existe");
        }
    }
    @Test
    public void shouldNotAddAExistingFunctionToACinema(){
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction function = new CinemaFunction(new Movie("Hulk", "Sad"), "2015-11-15 13:12");
        Cinema c = new Cinema("Cinema Tickets", functions);
        try {
            memoryTester.saveCinema(c);
            memoryTester.addFunctionToCinema(c,function);
            memoryTester.addFunctionToCinema(c,function);
            fail();
        } catch (CinemaPersistenceException exception){
            assertEquals(exception.getMessage(),"La función ya existe en ese cinema");
        }
    }
    @Test
    public void shouldModifyTheFunctionOfACinema(){
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction function = new CinemaFunction(new Movie("Hulk", "Sad"), "2015-11-15 13:12");
        CinemaFunction function2 = new CinemaFunction(new Movie("Hulk", "Drama"), "2020-12-11 14:12");
        Cinema c = new Cinema("Cinema Tickets", functions);
        try {
            memoryTester.saveCinema(c);
            memoryTester.addFunctionToCinema(c,function);
            memoryTester.modifieFunctionOfACinema(c,function2);
            assertEquals("Drama",c.getFunctions().get(0).getMovie().getGenre());
        } catch (CinemaPersistenceException exception){
            fail();
        }
    }
    @Test
    public void shouldNotModifiedTheFunctionOfANoexistingCinema(){
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction function = new CinemaFunction(new Movie("Hulk", "Sad"), "2015-11-15 13:12");
        CinemaFunction function2 = new CinemaFunction(new Movie("Hulk", "Drama"), "2020-12-11 14:12");
        Cinema c = new Cinema("Cinema Tickets", functions);
        try {
            memoryTester.modifieFunctionOfACinema(c,function2);
            fail();
        } catch (CinemaPersistenceException exception){
            assertEquals(exception.getMessage(),"El cinema no existe");
        }
    }
    @Test
    public void shouldAddAFunctionToACinemaIfTheFunctionNoExists(){
        CopyOnWriteArrayList<CinemaFunction> functions= new CopyOnWriteArrayList<>();
        CinemaFunction function = new CinemaFunction(new Movie("Hulk", "Sad"), "2015-11-15 13:12");
        CinemaFunction function2 = new CinemaFunction(new Movie("Spiderman", "Drama"), "2020-12-11 14:12");
        Cinema c = new Cinema("Cinema Tickets", functions);
        try {
            memoryTester.saveCinema(c);
            memoryTester.addFunctionToCinema(c,function);
            memoryTester.modifieFunctionOfACinema(c,function2);
            assertEquals("Spiderman",c.getFunctions().get(1).getMovie().getName());
        } catch (CinemaPersistenceException exception){
            fail();
        }
    }
}
