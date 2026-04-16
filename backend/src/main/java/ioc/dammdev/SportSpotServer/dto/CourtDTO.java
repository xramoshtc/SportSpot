package ioc.dammdev.SportSpotServer.dto;

import lombok.Data;

@Data
public class CourtDTO {
    private Long id;
    private String name;
    private String type;
    private String location;
    private double pricePerHour;
    private int capacity;
}