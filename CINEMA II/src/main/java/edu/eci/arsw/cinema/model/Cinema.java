/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.model;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author cristian
 */
public class Cinema {
    private String name;
    private CopyOnWriteArrayList functions;
    
    
    public Cinema(){}
    
    public Cinema(String name,CopyOnWriteArrayList<CinemaFunction> functions){
        this.name=name;
        this.functions=functions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CopyOnWriteArrayList<CinemaFunction> getFunctions() {
        return this.functions;
    }

    public void setSchedule(CopyOnWriteArrayList<CinemaFunction> functions) {
        this.functions = functions;
    }

    public void addFunction(CinemaFunction function){
        this.functions.add(function);
    }

    @Override
    public String toString() {
        return "Cinema{" +
                "name='" + name + '\'' +
                ", functions=" + functions +
                '}';
    }

    public void replaceFunction(CinemaFunction function) {
        int index=0;
        Iterator<CinemaFunction> iterator = functions.iterator();
        while(iterator.hasNext()){
            CinemaFunction nextfunction = iterator.next();
            if(nextfunction.getMovie().getName().equals(function.getMovie().getName())){
                index=functions.indexOf(nextfunction);
            }
        }
        functions.remove(index);
        functions.add(function);
    }
}
