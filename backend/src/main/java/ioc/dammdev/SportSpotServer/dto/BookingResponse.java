/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author Gess
 */

@Data
public class BookingResponse {
    private Long id;
    private LocalDateTime dateTime;
    private int durationHours;
    private LocalDateTime endTime;
    private String userName; // Només el nom, per seguretat
    private String courtName; //Nom de la pista
    private String location;  //On és la pista
}