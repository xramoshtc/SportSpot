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