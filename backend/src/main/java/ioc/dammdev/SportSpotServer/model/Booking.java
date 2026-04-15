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

    @Column(nullable = false)
    private int durationMinutes;

    // Relació moltes reserves a un usuari
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relació moltes reserves a una pista
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;
}