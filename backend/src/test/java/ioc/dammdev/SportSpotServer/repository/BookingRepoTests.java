package ioc.dammdev.SportSpotServer.repository;

import ioc.dammdev.SportSpotServer.model.Booking;
import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.ActiveProfiles;

/**
 * Proves de persistència per al repositori de reserves.
 * Verifica les relacions ManyToOne amb User i Court.
 * @author Gess Montalbán
 */
@DataJpaTest // Configura H2 en memòria, Hibernate i Spring Data JPA
@ActiveProfiles("test")   
class BookingRepoTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    private User savedUser;
    private Court savedCourt;

    @BeforeEach
    void setUp() {
        // Preparem dades de prova persistides
        User user = new User(null, "TestUser", "pass", "test@test.com", "USER", true);
        savedUser = entityManager.persistFlushFind(user);

        Court court = new Court(null,"Pista Central", "Pàdel", 25.0, 12,"Valencia");
        savedCourt = entityManager.persistFlushFind(court);
    }

    /**
     * Verifica que es pot guardar una reserva i recuperar-la per l'usuari.
     */
    @Test
    void whenFindByUser_thenReturnBookingList() {
        // GIVEN
        Booking booking = new Booking(null, LocalDateTime.now().plusDays(1), 60, savedUser, savedCourt);
        entityManager.persistAndFlush(booking);

        // WHEN
        List<Booking> found = bookingRepository.findByUser(savedUser);

        // THEN
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(savedUser.getName(), found.get(0).getUser().getName());
        assertEquals(savedCourt.getName(), found.get(0).getCourt().getName());
    }

    /**
     * Verifica que la cerca per ID de pista funciona correctament (Llistes).
     */
    @Test
    void whenFindByCourtId_thenReturnCorrectBookings() {
        // GIVEN
        Booking b1 = new Booking(null, LocalDateTime.now().plusDays(1), 60, savedUser, savedCourt);
        entityManager.persistAndFlush(b1);

        // WHEN
        List<Booking> found = bookingRepository.findByCourtId(savedCourt.getId());

        // THEN
        assertEquals(1, found.size());
        assertEquals("Pista Central", found.get(0).getCourt().getName());
    }
}