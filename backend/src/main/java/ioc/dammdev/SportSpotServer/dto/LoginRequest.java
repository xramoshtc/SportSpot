/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Gess
 */
@Getter
@Setter
public class LoginRequest {
    
/**
 *Objecte per rebre les credencials de login des de l'aplicació client
 * @author Gess
 * @version 1.0
 */

    /** Nom d'usuari (Màxim 10 caràcters segons especificació) */
    private String user;
    /** Nom d'usuari (Màxim 20 caràcters segons especificació) */
    private String password;


    
}

    

