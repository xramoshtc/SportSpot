package ioc.dammdev.SportSpotServer.service;

import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.repository.CourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servei per gestionar la lògica de negoci de les pistes (Courts).
 * Inclou validació de permisos mitjançant el token de sessió.
 * @author Gess Montalbán
 */
@Service
public class CourtService {

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private UserService userService;

    /**
     * Retorna totes les pistes registrades.
     * @return Llista d'objectes {@link Court}.
     */
    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    /**
     * Crea una nova pista si l'usuari té permisos d'administrador.
     * @param court Dades de la pista a crear.
     * @param token Token de sessió per validar el rol d'ADMIN.
     * @return La pista creada o null si no té permisos o el nom està duplicat.
     */
    public Court createCourt(Court court, String token) {
        // 1. Validació de seguretat (Només ADMIN)
        if (!userService.isValidSession(token) || !userService.isAdmin(token)) {
            return null;
        }

        // 2. Validació de negoci: No permetem noms de pista duplicats
        Optional<Court> existing = courtRepository.findByName(court.getName());
        if (existing.isPresent()) {
            return null; 
        }

        // 3. Guardem la nova pista (incloent el camp location)
        return courtRepository.save(court);
    }

    /**
     * Busca una pista pel seu ID.
     * Utilitzat internament pel servei de reserves.
     * @param id ID de la pista.
     * @return Optional amb la pista.
     */
    public Optional<Court> getCourtById(Long id) {
        return courtRepository.findById(id);
    }
}