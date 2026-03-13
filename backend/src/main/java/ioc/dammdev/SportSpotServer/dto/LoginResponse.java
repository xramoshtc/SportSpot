/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *Objecte de resposta que retorna el servidor després d'un intent de login.
 * @author Gess
 */

@Getter
@Setter
public class LoginResponse {
    
    /** Estat de la petició: true: success, false:failure     */
    private boolean success;
    /** Missatge descriptiu del resultat */
    private String message;
        /** Codi de resultat: 1 OK, 0 Pass incorrecte, -1 Altres */
    private int resultCode;
    /** Identificador de sessió generat (si el login és correcte) */
    private String sessionToken;
    /** Dades de l'usuari autenticat */
    private String role;
  //  private String permissions;

    public LoginResponse(boolean success, String message, int resultCode, String sessionToken, String role, String permissions) {
        this.success = success;
        this.message = message;
        this.resultCode = resultCode;
        this.sessionToken = sessionToken;
        this.role = role;
      //  this.permissions = permissions;
    }
}