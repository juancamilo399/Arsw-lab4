/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.services;

import edu.eci.arsw.cinema.filtros.CinemaFilter;
import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.persistence.CinemaPersitence;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author cristian
 */
@Service("cinemaService")
public class CinemaServices {

    @Autowired
    @Qualifier("inMemory")
    CinemaPersitence cps=null;

    @Autowired
    @Qualifier("Genre")
    CinemaFilter cf=null;

    public List<CinemaFunction> filterCinemas(String cinema , String date , String filtro) throws CinemaPersistenceException {
        List<CinemaFunction> functions = getFunctionsbyCinemaAndDate(cinema, date);
        return cf.filter(functions, filtro);
    }
    
    public Set<Cinema> getAllCinemas() throws CinemaPersistenceException {
        return cps.getCinemas();
    }
    
    /**
     * 
     * @param name cinema's name
     * @return the cinema of the given name created by the given author
     * @throws CinemaException
     */
    public Cinema getCinemaByName(String name) throws CinemaPersistenceException {
        return cps.getCinema(name);
    }
    
    
    public void buyTicket(int row, int col, String cinema, String date, String movieName) throws CinemaException, CinemaPersistenceException {
        cps.buyTicket(row, col, cinema, date, movieName);
    }
    
    public List<CinemaFunction> getFunctionsbyCinemaAndDate(String cinema, String date) throws CinemaPersistenceException {
        return cps.getFunctionsbyCinemaAndDate(cinema,date);
    }

    public void saveCinema(Cinema cinema) throws CinemaPersistenceException {
        cps.saveCinema(cinema);
    }

    public CinemaFunction getFunctionByName(List<CinemaFunction> functions , String name) throws CinemaPersistenceException {
        return cps.getFunctionByName(functions,name);
    }

    public void addFunctionToCinema(Cinema cinema , CinemaFunction function) throws CinemaPersistenceException {
        cps.addFunctionToCinema(cinema , function);
    }

    public void modifieFunctionOfACinema(Cinema cinema, CinemaFunction function) throws CinemaPersistenceException {
        cps.modifieFunctionOfACinema(cinema,function);
    }
}
