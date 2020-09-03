/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.persistence;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import java.util.List;
import java.util.Set;

/**
 *
 * @author cristian
 */
public interface CinemaPersitence {
    
    /**
     * 
     * @param row the row of the seat
     * @param col the column of the seat
     * @param cinema the cinema's name
     * @param date the date of the function
     * @param movieName the name of the movie
     * 
     * @throws CinemaException if the seat is occupied,
     *    or any other low-level persistence error occurs.
     */
    public void buyTicket(int row, int col, String cinema, String date, String movieName) throws CinemaException, CinemaPersistenceException;
    
    /**
     * 
     * @param cinema cinema's name
     * @param date date
     * @return the list of the functions of the cinema in the given date
     */
    public List<CinemaFunction> getFunctionsbyCinemaAndDate(String cinema, String date) throws CinemaPersistenceException;
    
    /**
     * 
     * @param cinema new cinema
     * @throws  CinemaPersistenceException n if a cinema with the same name already exists
     */
    public void saveCinema(Cinema cinema) throws CinemaPersistenceException;
    
    /**
     * 
     * @param name name of the cinema
     * @return Cinema of the given name
     * @throws  CinemaPersistenceException if there is no such cinema
     */
    public Cinema getCinema(String name) throws CinemaPersistenceException;

    /**
     * @return All the cinemas
     */

    public Set<Cinema> getCinemas() throws CinemaPersistenceException;

    /**
     *
     * @param functions
     * @param name
     * @return The function with that name
     */

    CinemaFunction getFunctionByName(List<CinemaFunction> functions, String name) throws CinemaPersistenceException;

    /**
     *
     * @param cinema the cinema on that the function is going to be added
     * @param function to be added
     */
    void addFunctionToCinema(Cinema cinema, CinemaFunction function) throws CinemaPersistenceException;

    /**
     *
     * @param cinema he cinema on that the function is going to be modified or added
     * @param function to be modified or added
     */
    void modifieFunctionOfACinema(Cinema cinema, CinemaFunction function) throws CinemaPersistenceException;
}
