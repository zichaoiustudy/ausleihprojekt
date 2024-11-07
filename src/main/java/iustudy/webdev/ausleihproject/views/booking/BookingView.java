package iustudy.webdev.ausleihproject.views.booking;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.service.MainService;
import iustudy.webdev.ausleihproject.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.FullCalendar;

import java.util.Optional;

import static iustudy.webdev.ausleihproject.views.SearchResultView.getStatusColor;
import static iustudy.webdev.ausleihproject.views.SearchResultView.getStatusText;

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

    public void configureHeader() {
        header.removeAll();
        header.setWidthFull();
        header.setPadding(true);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H3 model = new H3("Gerätemodell: " + device.getModel());

        H3 maxDays = new H3("Maximale Ausleihdauer: " +
                (device.getMaxDays() == 0 ? "unbegrenzt" : device.getMaxDays() + " Tage"));

        String statusText = getStatusText(device.getStatus());
        String color = getStatusColor(device.getStatus());

        String statusHtml = "<span style='color:" + color + "'>" + statusText + "</span>";
        H3 status = new H3("Status: " + statusHtml);
        status.getElement().setProperty("innerHTML", "Status: " + statusHtml);

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