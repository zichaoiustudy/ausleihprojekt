package iustudy.webdev.ausleihproject.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity(name = "Booking")
public class Booking extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "device_id")
    @NotNull
    private Device device;
    @NotNull
    private String userName;
    @NotNull
    private LocalDate borrowDate;
    @Nullable
    private LocalDate returnDate;
    private int maxDays;

    public Booking() {}

    public Booking(Device device, String userName, LocalDate borrowDate, @Nullable LocalDate returnDate, int maxDays) {
        this.device = device;
        this.userName = userName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.maxDays = maxDays;
    }
}
