/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  Data-Tranfer_object de petició de Logout (Json)
 * @author Gess Montalbán
 */
@Data
@AllArgsConstructor
public class LogoutRequest {
    private String token;
    
}
