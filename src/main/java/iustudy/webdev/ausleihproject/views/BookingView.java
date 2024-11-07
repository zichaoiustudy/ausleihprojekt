package iustudy.webdev.ausleihproject.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.data.DeviceStatus;
import iustudy.webdev.ausleihproject.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.FullCalendar;

import java.util.Optional;

@Route(value = "booking/:deviceId", layout = MainLayout.class)
@PageTitle("IU Webprogrammierung | Gerätedetails")
@CssImport("./styles/styles.css")
public class BookingView extends VerticalLayout implements BeforeEnterObserver {
    MainService service;
    Device device;

    HorizontalLayout header;
    FullCalendar calendar;
    BookingForm form;

    @Autowired
    public BookingView(MainService service) {
        this.service = service;
        setSizeFull();
        header = new HorizontalLayout();
        form = new BookingForm();

        add(header, getContent());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Extract the deviceId parameter from the URL
        Optional<String> deviceId = event.getRouteParameters().get("deviceId");
        if (deviceId.isPresent()) {
            device = service.findDevice(Long.parseLong(deviceId.get()));
            configureForm();
            configureHeader();
        } else {
            header.add(new H3("Device ID not found!"));
        }
    }

    private String getStatusColor(DeviceStatus status) {
        return switch (status) {
            case RENTED -> "orange";
            case MISSING -> "red";
            default -> "green";
        };
    }

    public void configureHeader() {
        header.setWidthFull();
        header.setPadding(true);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H3 model = new H3("Gerätemodell: " + device.getModel());
        H3 maxDays = new H3("Maximale Ausleihdauer: " + device.getMaxDays());

        String statusText = "<span style='color:" + getStatusColor(device.getStatus()) + "'>" + device.getStatus().name().toLowerCase() + "</span>";
        H3 status = new H3("Status: " + statusText);
        status.getElement().setProperty("innerHTML", "Status: " + statusText);

        header.add(model, maxDays, status);
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(form);
        //content.setFlexGrow(2, calendar);
        content.setFlexGrow(1, form);
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form.setBookingForm(service, device);
    }
}