/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.dto;

/**
 *
 * @author Gess
 */
public class BookingResponse {
    private Long id;
    private String courtName;
    private String location;
    private String dateTime;
    private int duration;
    private String userName; // Només el nom, per seguretat
}