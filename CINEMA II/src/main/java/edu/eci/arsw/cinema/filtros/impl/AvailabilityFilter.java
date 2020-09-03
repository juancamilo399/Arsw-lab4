
package edu.eci.arsw.cinema.filtros.impl;

import edu.eci.arsw.cinema.filtros.CinemaFilter;
import edu.eci.arsw.cinema.model.CinemaFunction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("Availability")

public class AvailabilityFilter implements CinemaFilter{

    @Override
    public List<CinemaFunction> filter(List<CinemaFunction> functions, String filtro) {
        List<CinemaFunction> newFunctions = new ArrayList<CinemaFunction>();
        int seats = Integer.parseInt(filtro);
        for(CinemaFunction c : functions){
            int empty = 84;
            for(List<Boolean> list : c.getSeats()){
                for(Boolean b : list){
                    if(b == false){
                        empty -=1;
                    }
                }
            }
            if(empty>=seats){
                newFunctions.add(c);
            }
        }
        return newFunctions;
    }
}