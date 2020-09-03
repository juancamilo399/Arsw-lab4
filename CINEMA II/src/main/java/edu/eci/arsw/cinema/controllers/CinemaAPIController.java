/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.controllers;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.persistence.CinemaPersitence;
import edu.eci.arsw.cinema.services.CinemaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.http.HTTPException;

/**
 *
 * @author cristian
 */

@RestController
@RequestMapping(value = "/cinemas")
public class CinemaAPIController {

    @Autowired
    @Qualifier("cinemaService")
    CinemaServices cs = null;


    @GetMapping("/{name}/{date}/{movieName}")
    public ResponseEntity<?> GetFunctions(@PathVariable String name, @PathVariable String date, @PathVariable String movieName) {
        try {
            return new ResponseEntity<>(cs.getFunctionByName(cs.getFunctionsbyCinemaAndDate(name, date), movieName), HttpStatus.ACCEPTED);
        } catch (CinemaPersistenceException ex) {
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            ResponseEntity response = null;
            if (ex.getMessage().equals("No existe una función de esa pelicula")) {
                response = new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
            }
            if (ex.getMessage().equals("Alguno de los parametros es nulo")) {
                response = new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return response;
        }
        // ...
    }

    @GetMapping("/{name}/{date}")
    public ResponseEntity<?> GetFunctions(@PathVariable String name, @PathVariable String date) {
        try {
            return new ResponseEntity<>(cs.getFunctionsbyCinemaAndDate(name, date), HttpStatus.ACCEPTED);
        } catch (CinemaPersistenceException ex) {
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            ResponseEntity response = null;
            if (ex.getMessage().equals(ex.getMessage().equals("Funciones no encontradas para ese cinema y esa fecha"))) {
                response = new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
            }
            if (ex.getMessage().equals("Alguno de los parametros es nulo")) {
                response = new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return response;
        }
        // ...
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> GetFunctions(@PathVariable String name) {
        try {
            return new ResponseEntity<>(cs.getCinemaByName(name).getFunctions(), HttpStatus.ACCEPTED);
        } catch (CinemaPersistenceException ex) {
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            ResponseEntity response = null;
            if (ex.getMessage().equals("El cinema no existe")) {
                response = new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
            }
            if (ex.getMessage().equals("El nombre del cinema no puede ser nulo")) {
                response = new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return response;
        }
        // ...
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> GetCinemas() {
        try {
            return new ResponseEntity<>(cs.getAllCinemas(), HttpStatus.ACCEPTED);
        } catch (CinemaPersistenceException ex) {
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/{name}")
    public ResponseEntity<?> PostFunction(@PathVariable String name, @RequestBody CinemaFunction function) {
        try {
            cs.addFunctionToCinema(cs.getCinemaByName(name), function);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CinemaPersistenceException ex) {
            ResponseEntity response = null;
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().equals("El cinema no existe")) {
                response = new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
            }
            if (ex.getMessage().equals("La función ya existe en ese cinema")) {
                response = new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
            }
            return response;
        }
    }

    @PutMapping("/{name}")
    public ResponseEntity<?> PutFunction(@PathVariable String name, @RequestBody CinemaFunction function) {
        try {
            cs.modifieFunctionOfACinema(cs.getCinemaByName(name),function);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (CinemaPersistenceException ex) {
            ResponseEntity response = null;
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().equals("El cinema no existe")) {
                response = new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
            }
            return response;
        } catch (org.springframework.http.converter.HttpMessageNotReadableException hx){
            Logger.getLogger(CinemaAPIController.class.getName()).log(Level.SEVERE, null, hx);
            return new ResponseEntity<>("Existe un error en el formato del JSON de la funcion", HttpStatus.BAD_REQUEST);
        }
    }
}
