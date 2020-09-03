

package edu.eci.arsw.cinema.filtros;

import edu.eci.arsw.cinema.model.CinemaFunction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CinemaFilter {

    public List<CinemaFunction> filter (List<CinemaFunction> functions, String filtro);
}