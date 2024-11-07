package iustudy.webdev.ausleihproject.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    @Query("select d from Device d " +
            "where lower(d.type) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(d.model) like lower(concat('%', :searchTerm, '%')) ")
    List<Device> search(@Param("searchTerm") String searchTerm);
}
