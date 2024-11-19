package iustudy.webdev.ausleihproject.data;

import lombok.Getter;

@Getter
public enum DeviceStatus {
    AVAILABLE("verfügbar", "green"),
    RENTED("vermietet", "orange"),
    MISSING("fehlend", "red");

    private final String color;
    private final String germanName;

    DeviceStatus(String germanName, String color) {
        this.germanName = germanName;
        this.color = color;
    }

}
