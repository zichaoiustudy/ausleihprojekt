package iustudy.webdev.ausleihproject.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.device.id = :deviceId")
    List<Booking> findByDevice(@Param("deviceId") long deviceId);

    @Query("SELECT b FROM Booking b WHERE b.userName = :userName")
    List<Booking> findByUser(@Param("userName") String name);

}
