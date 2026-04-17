/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO per enviar la informació detallada d'un esdeveniment al client.
 * Inclou dades calculades de capacitat per facilitar la feina al Frontend.
 * @author Gess Montalbán
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder            //Ens permet generar l'objecte d'una manera més flexible
public class EventResponse {
    private Long id;
    private String title;
    private String courtName;
    private String organizerName;
    private String dateTime;
    private int currentParticipants;
    private int maxCapacity;
    private List<String> participantNames;
}