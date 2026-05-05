package ioc.dammdev.SportSpotServer.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entitat que gestiona les reserves de les pistes per part dels usuaris.
 * @author Gess Montalbán
 */
@Getter 
@Setter 
@Builder
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "reserves")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "duration_hours", nullable = false)
    private Integer durationHours;
    
    //Camp persistit per simplificar les consultes de solapament.
    @Column(nullable = false)
    private LocalDateTime endTime;

    // Relació moltes reserves a un usuari
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relació moltes reserves a una pista
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;
    
    /**
     * Camp per al control de concurrència optimista (JPA).
     * S'incrementa automàticament en cada actualització (PUT).
     */
    @Version
    private Long version;
    
    /**
     * Mètode automàtic de JPA que s'executa abans de guardar a la BD.
     * Garanteix que endTime = startTime + durationHours.
     */
    @PrePersist
    @PreUpdate
    public void autoCalculateEndTime() {
        if (this.dateTime != null && this.durationHours != null) {
            this.endTime = this.dateTime.plusHours(this.durationHours);
        }
    }

  
}