package ioc.dammdev.SportSpotServer.repository;

import ioc.dammdev.SportSpotServer.model.Court;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Proves de persistència per al repositori de pistes.
 * @author Gess Montalbán
 */
@DataJpaTest
class CourtRepoTests {

    @Autowired
    private CourtRepository courtRepository;

    @Test
    void whenFindByName_thenReturnCourt() {
        // GIVEN
        Court court = new Court(null, "Pista 5", "Tennis", 15.0, "Valencia");
        courtRepository.save(court);

        // WHEN
        Optional<Court> found = courtRepository.findByName("Pista 5");

        // THEN
        assertTrue(found.isPresent());
        assertEquals("Tennis", found.get().getType());
    }
    @Test
    void whenSaveCourtWithLocation_thenReturnCorrectLocation() {
        // GIVEN
        Court court = new Court(null, "Pista 7", "Basket", 12.0, "Pavelló Oest");
        courtRepository.save(court);

        // WHEN
        Optional<Court> found = courtRepository.findByName("Pista 7");

        // THEN
        assertTrue(found.isPresent());
        assertEquals("Pavelló Oest", found.get().getLocation());
    }
}