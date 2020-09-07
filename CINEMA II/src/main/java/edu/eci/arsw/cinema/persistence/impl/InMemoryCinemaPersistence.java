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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author cristian
 */

@Service("inMemory")
public class InMemoryCinemaPersistence implements CinemaPersitence{
    
    private final ConcurrentHashMap<String,Cinema> cinemas=new ConcurrentHashMap<>();

    public InMemoryCinemaPersistence() {
        //load stub data
        String functionDate = "2018-12-18 15:30";
        String functionDate2 = "2013-12-18 15:30";
        CopyOnWriteArrayList<CinemaFunction> functions = new CopyOnWriteArrayList<>();
        CinemaFunction funct1 = new CinemaFunction(new Movie("SuperHeroes Movie","Action"),functionDate);
        CinemaFunction funct2 = new CinemaFunction(new Movie("The Night","Horror"),functionDate2);
        functions.add(funct1);
        functions.add(funct2);
        Cinema c=new Cinema("cinemaX",functions);

        functionDate = "2020-12-18 14:30";
        functions = new CopyOnWriteArrayList<>();
        funct1 = new CinemaFunction(new Movie("Spiderman","Drama"),functionDate);
        funct2 = new CinemaFunction(new Movie("IT","Adventure"),functionDate);
        functions.add(funct1);
        functions.add(funct2);
        Cinema c2=new Cinema("cinemaY",functions);
        cinemas.put("cinemaY",c2);

        functionDate = "2015-11-13 15:30";
        functions = new CopyOnWriteArrayList<>();
        funct1 = new CinemaFunction(new Movie("Deadpool","Funny"),functionDate);
        funct2 = new CinemaFunction(new Movie("The Avengers","Action"),functionDate);
        functions.add(funct1);
        functions.add(funct2);
        Cinema c3=new Cinema("cinemaZ",functions);
        cinemas.put("cinemaZ",c3);
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
        if(selected_functions.size()==0){
            throw new CinemaPersistenceException("Funciones no encontradas para ese cinema y esa fecha");
        }
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
    public Set<Cinema> getCinemas() throws CinemaPersistenceException {
        if(cinemas.size()==0){
            throw new CinemaPersistenceException("No existen cinemas");
        }
        return new HashSet(cinemas.values());
    }

    @Override
    public CinemaFunction getFunctionByName(List<CinemaFunction> functions, String name) throws CinemaPersistenceException {
        CinemaFunction cinemaFunction = null;
        for(CinemaFunction function : functions){
            if(function.getMovie().getName().equals(name)){
                cinemaFunction=function;
            }
        }
        if(cinemaFunction==null){
            throw new CinemaPersistenceException("No existe una función de esa pelicula");
        }
        return cinemaFunction;
    }

    @Override
    public void addFunctionToCinema(Cinema cinema, CinemaFunction function) throws CinemaPersistenceException {
        if(!cinemas.containsKey(cinema.getName())){
            throw new CinemaPersistenceException("El cinema no existe");
        }
        for(CinemaFunction cFunction : cinema.getFunctions()){
            if(cFunction.equals(function)){
                throw new CinemaPersistenceException("La función ya existe en ese cinema");
            }
        }
        cinema.addFunction(function);
    }

    @Override
    public void modifieFunctionOfACinema(Cinema cinema, CinemaFunction function) throws CinemaPersistenceException {
        if(!cinemas.containsKey(cinema.getName())){
            throw new CinemaPersistenceException("El cinema no existe");
        }
        boolean replaced = false;
        for(CinemaFunction cFunction : cinema.getFunctions()){
            if(cFunction.getMovie().getName().equals(function.getMovie().getName())){
                cinema.replaceFunction(function);
                replaced = true;
            }
        }
        if(!replaced){
            addFunctionToCinema(cinema,function);
        }
    }
}
