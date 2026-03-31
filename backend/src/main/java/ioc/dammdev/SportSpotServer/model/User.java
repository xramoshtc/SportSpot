/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ioc.dammdev.SportSpotServer.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 *Model De dades per un usuari. 
 * Es fa servir mapatge objecte-relacional amb  hibernate.
 * @author Gess Montalbán
 * @version 1.0
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuaris")
public class User {
   @Id //Clau primària
   @GeneratedValue(strategy = GenerationType.IDENTITY) //Autoincrement
    private Long id;
   
   @Column(unique = true, nullable = false)
    private String name;
   
   @Column(nullable = false)
    private String password;
   
   @Column
    private String email;
   
   @Column
    private String role; 
   
   // Constructor per als clients
public User(String name, String password, String email, String role) {
    this.name = name;
    this.password = password;
    this.email = email;
    this.role = role;
    // L'id es queda a null per defecte fins que JPA el guardi
}
    
}
