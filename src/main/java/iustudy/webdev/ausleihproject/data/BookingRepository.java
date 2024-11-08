package iustudy.webdev.ausleihproject.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.device.id = :deviceId")
    List<Booking> findByDevice(@Param("deviceId") long deviceId);

    @Query("select b from Booking b " +
            "where lower(b.device.model) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(b.userName) like lower(concat('%', :searchTerm, '%')) ")
    List<Booking> search(@Param("searchTerm") String searchTerm);

}
