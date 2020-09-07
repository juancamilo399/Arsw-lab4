/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.model;

import edu.eci.arsw.cinema.persistence.CinemaException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author cristian
 */
public class CinemaFunction {
    
    private Movie movie;
    private CopyOnWriteArrayList<CopyOnWriteArrayList<Boolean>> seats=new CopyOnWriteArrayList<>();
    private String date;
    
    public CinemaFunction(){}
    
    public CinemaFunction(Movie movie, String date){
        this.movie=movie;
        this.date=date;
        for (int i=0;i<7;i++){
            CopyOnWriteArrayList<Boolean> row= new CopyOnWriteArrayList<>(Arrays.asList(new Boolean[12]));
            Collections.fill(row, Boolean.TRUE);
            this.seats.add(row);
        }
    }
    
    public void buyTicket(int row,int col) throws CinemaException{
        if(row < 0 || col <0 || row > 7 || col > 12){
            throw new CinemaException("Sillas inexistentes");
        }
        if (seats.get(row).get(col).equals(true)){
            seats.get(row).set(col,Boolean.FALSE);
        }
        else{
            throw new CinemaException("Seat booked");
        }
    }
    
    public CopyOnWriteArrayList<CopyOnWriteArrayList<Boolean>> getSeats() {
        return this.seats;
    }
    
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "CinemaFunction{" +
                "movie=" + movie.getName() +
                ", seats=" + seats +
                ", date='" + date + '\'' +
                '}';
    }
}
