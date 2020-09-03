/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.persistence.impl;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.persistence.CinemaPersitence;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author cristian
 */

@Service("inMemory")
public class InMemoryCinemaPersistence implements CinemaPersitence{
    
    private final Map<String,Cinema> cinemas=new HashMap<>();

    public InMemoryCinemaPersistence() {
        //load stub data
        String functionDate = "2018-12-18 15:30";
        List<CinemaFunction> functions= new ArrayList<>();
        CinemaFunction funct1 = new CinemaFunction(new Movie("SuperHeroes Movie","Action"),functionDate);
        CinemaFunction funct2 = new CinemaFunction(new Movie("The Night","Horror"),functionDate);
        functions.add(funct1);
        functions.add(funct2);
        Cinema c=new Cinema("cinemaX",functions);
        cinemas.put("cinemaX", c);
    }    

    @Override
    public void buyTicket(int row, int col, String cinema, String date, String movieName) throws CinemaException, CinemaPersistenceException {
        if(cinema == null || date == null || movieName == null){
            throw new CinemaPersistenceException("Alguno de los parametros es nulo");
        }
        Cinema selectedCinema=getCinema(cinema);
        if(selectedCinema==null){
            throw new CinemaPersistenceException("El cinema no existe");
        }
        List<CinemaFunction> selected_functions=selectedCinema.getFunctions().stream()
                .filter(f -> f.getDate().equals(date) && f.getMovie().getName().equals(movieName))
                .collect(Collectors.toList());
        selected_functions.get(0).buyTicket(row,col);
    }

    @Override
    public List<CinemaFunction> getFunctionsbyCinemaAndDate(String cinema, String date) throws CinemaPersistenceException {
        if(cinema == null || date == null){
            throw new CinemaPersistenceException("Alguno de los parametros es nulo");
        }
        Cinema selectedCinema=getCinema(cinema);
        if(selectedCinema==null){
            throw new CinemaPersistenceException("El cinema no existe");
        }
        List<CinemaFunction> selected_functions=selectedCinema.getFunctions().stream()
                .filter(f -> f.getDate().equals(date))
                .collect(Collectors.toList());
        return selected_functions;
    }

    @Override
    public void saveCinema(Cinema c) throws CinemaPersistenceException {
        if(c == null){
            throw new CinemaPersistenceException("Cannot save a null Cinema");
        }
        if (cinemas.containsKey(c.getName())){
            throw new CinemaPersistenceException("The given cinema already exists: "+c.getName());
        }
        else{
            cinemas.put(c.getName(),c);
        }   
    }

    @Override
    public Cinema getCinema(String name) throws CinemaPersistenceException {
        if(name==null){
            throw new CinemaPersistenceException("El nombre del cinema no puede ser nulo");
        }
        if(!cinemas.containsKey(name)){
            throw new CinemaPersistenceException("El cinema no existe");
        }
        return cinemas.get(name);
    }

    @Override
    public Set<Cinema> getCinemas() {
        return new HashSet(cinemas.values());
    }

}
