package iustudy.webdev.ausleihproject.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name = "Device")
public class Device extends AbstractEntity{

    @NotNull
    private String type;
    private String model;
    @NotNull
    @Enumerated
    @Column(columnDefinition = "smallint")
    private DeviceStatus status;

    public Device() {}

    public Device(String type, String model, DeviceStatus status) {
        this.type = type;
        this.model = model;
        this.status = status;
    }
}
