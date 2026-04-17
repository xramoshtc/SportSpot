/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Entitat que representa un esdeveniment esportiu col·lectiu.
 * Permet que un usuari organitzi una activitat en una pista i que altres
 * usuaris s'hi puguin inscriure fins a completar la capacitat de la pista. *
 * @author Gess
 */
@Getter
@Setter
@Entity
@Table(name = "esdeveniments")
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private LocalDateTime dateTime;
    
    // L'Usuari que crea i gestiona l'esdeveniment
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;
    
    // La pista on tindrà lloc l'esdeveniment. 
    // la seva capacitat determinarà el límit de participants
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;
    
    
    // Llista d'inscrits a l'esdeveniment
    // IMplementa una telació ManyToMany amb una taula intermèdia
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "event_participants",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
        )
    private List<User> participants = new ArrayList<>();
    
    // Constructors 
    
    public Event() {
        
    }
    
    public Event (String title, LocalDateTime dateTime, User organizer, Court court) {
        this.title = title;
        this.dateTime = dateTime;
        this.organizer = organizer;
        this.court = court;
        // Per defecte, l'organitzador és el primer participant
        this.participants.add(organizer);
    }
    
    /**
     * Mètode d'ajuda per afegir un participant de forma segura.
     * @param user L'usuari que s'uneix.
     * @return true si s'ha pogut afegir, false si ja hi era o no hi ha lloc.
     */
    public boolean addParticipant(User user){
        if (this.participants.size() < this.court.getCapacity() && !this.participants.contains(user)) 
            return this.participants.add(user);
        return false;
        }
    
    
    
}
