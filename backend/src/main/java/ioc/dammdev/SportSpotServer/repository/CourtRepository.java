package ioc.dammdev.SportSpotServer.repository;

import ioc.dammdev.SportSpotServer.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repositori per a l'entitat Court.
 * Gestiona l'accés a dades de les pistes esportives.
 * @author Gess Montalbán
 */
@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
    
    /**
     * Busca una pista pel seu nom exacte.
     * Útil per a validacions abans de crear una pista nova.
     * @param name Nom de la pista.
     * @return Un Optional amb la pista si existeix.
     */
    Optional<Court> findByName(String name);
}