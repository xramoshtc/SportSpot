package ioc.dammdev.SportSpotServer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entitat que representa una pista esportiva al sistema
 * @author Gess Montalbán
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Pistes")
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(nullable = false)
    private String type; // Pàdel, tenis, futbol..
    
    @Column(nullable = false)
    private double pricePerHour;
    
    @Column(nullable = false)
    private int capacity;
    
    @Column(nullable = false)
    private String location;
    
    public Court(String name, String type, double pricePerHour,int capacity, String location){
        this.name = name;
        this.type = type;
        this.pricePerHour = pricePerHour;
        this.location = location;
    }
    
}

