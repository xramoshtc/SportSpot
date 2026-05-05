/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Gess
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private Long courtId;
    private String dateTime; // "2026-04-15T10:00"
    private int durationHours;
    // Getters i Setters
}