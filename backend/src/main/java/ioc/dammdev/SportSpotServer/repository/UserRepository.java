/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ioc.dammdev.SportSpotServer.repository;

import ioc.dammdev.SportSpotServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 *  Interfície repositori per comunicar amb la BD
 * @author Gess Montalbán
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
    /**
 * Cerca un usuari per la seva clau de nom.
 * @param username El nom de l'usuari a buscar.
 * @return Un Optional que conté l'usuari si es troba.
 */
    Optional<User> findByName(String username);
    
}
