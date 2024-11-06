package iustudy.webdev.ausleihproject.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.service.BookingService;
import iustudy.webdev.ausleihproject.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.FullCalendar;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Route(value = "booking/:deviceId", layout = MainLayout.class)
@CssImport("./styles/styles.css")
public class BookingView extends VerticalLayout implements BeforeEnterObserver {
    private H2 label;
    DeviceService deviceService;
    BookingService bookingService;
    Device device;
    FullCalendar calendar;
    BookingForm form;

    @Autowired
    public BookingView(DeviceService deviceService, BookingService bookingService) {
        this.deviceService = deviceService;
        this.bookingService = bookingService;
        setSizeFull();
        label = new H2();
        form = new BookingForm();

        add(label, getContent());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Extract the deviceId parameter from the URL
        Optional<String> deviceId = event.getRouteParameters().get("deviceId");
        if (deviceId.isPresent()) {
            device = deviceService.findDevice(Long.parseLong(deviceId.get()));
            configureForm();
            label.setText("Device Model: " + device.getModel() + "\nBooking History: ");
            // You can add additional logic here to load data based on the deviceId
        } else {
            label.setText("Device ID not found!");
        }
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(form);
        //content.setFlexGrow(2, calendar);
        content.setFlexGrow(1, form);
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form.setBookingForm(bookingService, device);
    }
}