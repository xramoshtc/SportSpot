/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO per rebre les peticions de creació i modificació d'esdeveniments.
 * * @param title Títol de l'esdeveniment.
 * @param courtId ID de la pista on es realitza.
 * @param dateTime Data i hora en format ISO-8601.
 * @author Gess Montalbán
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest{
    String title;
    Long courtId;
    String dateTime;
}