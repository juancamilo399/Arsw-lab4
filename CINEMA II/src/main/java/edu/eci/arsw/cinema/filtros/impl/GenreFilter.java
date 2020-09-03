package edu.eci.arsw.cinema.filtros.impl;

import edu.eci.arsw.cinema.filtros.CinemaFilter;
import edu.eci.arsw.cinema.model.CinemaFunction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("Genre")

public class GenreFilter implements CinemaFilter {


    @Override
    public List<CinemaFunction> filter(List<CinemaFunction> functions, String filtro) {
        List<CinemaFunction> newFunctions = new ArrayList<CinemaFunction>();
        for(CinemaFunction c : functions){
            if(filtro.equals(c.getMovie().getGenre())){
                newFunctions.add(c);
            }
        }
        return newFunctions;
    }
}