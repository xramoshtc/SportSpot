package ioc.dammdev.SportSpotServer.repository;

import ioc.dammdev.SportSpotServer.model.Booking;
import ioc.dammdev.SportSpotServer.model.Court;
import ioc.dammdev.SportSpotServer.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Proves de persistència per al repositori de reserves.
 * Adaptat per a la lògica de solapaments i camps persistits (endTime).
 * 
 * @author Gess Montalbán
 */
@DataJpaTest
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
        // Creem dades de prova. Nota: User i Court mantenen els seus constructors
        User user = new User(null, "TestUser", "pass", "test@test.com", "USER", true);
        savedUser = entityManager.persistFlushFind(user);

        Court court = new Court(null, "Pista Central", "Pàdel", 25.0, 12, "Valencia");
        savedCourt = entityManager.persistFlushFind(court);
    }

    @Test
    void whenFindByUser_thenReturnBookingList() {
        // GIVEN: El constructor ara usa dateTime i durationHours. El ID és null.
        // Utilitzem builder() de Lombok per no barallar-nos amb els constructors
        // a vegades no dispara els @PrePersist depenent de la configuració.
        Booking booking = Booking.builder()
                .dateTime(LocalDateTime.now().plusDays(1).withMinute(0).withSecond(0))
                .durationHours(1)
                .user(savedUser)
                .court(savedCourt)
                .build();
        booking.autoCalculateEndTime(); // Assegurem el càlcul per al test
        entityManager.persistAndFlush(booking);

        // WHEN
        List<Booking> found = bookingRepository.findByUser(savedUser);

        // THEN
        assertFalse(found.isEmpty());
        assertEquals(savedUser.getName(), found.get(0).getUser().getName());
    }

    /**
     * Test CRÍTIC: Verifica que la consulta detecta solapaments entre intervals.
     */
    @Test
    void whenOverlapping_thenReturnConflict() {
        // GIVEN: Una reserva de 10h a 12h (2 hores)
        LocalDateTime tenAM = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0).withSecond(0);
        Booking existing = Booking.builder()
                .dateTime(tenAM)
                .durationHours(2)
                .user(savedUser)
                .court(savedCourt)
                .build();
        existing.autoCalculateEndTime();
        entityManager.persistAndFlush(existing);

        // WHEN: Intentem buscar solapament amb una de 11h a 12h
        LocalDateTime elevenAM = tenAM.plusHours(1);
        LocalDateTime twelvePM = tenAM.plusHours(2);
        
        List<Booking> conflicts = bookingRepository.findOverlappingBookings(
                savedCourt, elevenAM, twelvePM);

        // THEN
        assertFalse(conflicts.isEmpty(), "Hauria de trobar la reserva existent com a conflicte");
        assertEquals(1, conflicts.size());
    }

    @Test
    void whenNoOverlap_thenReturnEmptyList() {
        // GIVEN: Reserva de 10h a 11h
        LocalDateTime tenAM = LocalDateTime.now().plusDays(3).withHour(10).withMinute(0).withSecond(0).withNano(0);
        Booking existing = Booking.builder()
                .dateTime(tenAM)
                .durationHours(1)
                .user(savedUser)
                .court(savedCourt)
                .build();
        existing.autoCalculateEndTime();
        entityManager.persistAndFlush(existing);

        // WHEN: Busquem de 11h a 12h (no solapa)
        List<Booking> conflicts = bookingRepository.findOverlappingBookings(
                savedCourt, tenAM.plusHours(1), tenAM.plusHours(2));

        // THEN
        assertTrue(conflicts.isEmpty(), "No hauria de trobar cap conflicte");
    }
}