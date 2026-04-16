/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.dto;

import lombok.Data;

/**
 * DTO per enviar dades d'usuari de forma segura
 * @author Gess
 */
@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
}