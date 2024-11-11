package iustudy.webdev.ausleihproject.views.booking;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.data.DeviceStatus;
import iustudy.webdev.ausleihproject.service.MainService;
import iustudy.webdev.ausleihproject.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static iustudy.webdev.ausleihproject.views.search.SearchResultView.getStatusColor;
import static iustudy.webdev.ausleihproject.views.search.SearchResultView.getStatusText;

@Route(value = "booking/:deviceId", layout = MainLayout.class)
@PageTitle("IU Webprogrammierung | Gerätedetails")
public class BookingView extends VerticalLayout implements BeforeEnterObserver {
    MainService service;
    Device device;

    HorizontalLayout header;
    CalendarView calendar;
    BookingForm form;

    @Autowired
    public BookingView(MainService service) {
        this.service = service;
        setSizeFull();
        header = new HorizontalLayout();
        form = new BookingForm();
        calendar = new CalendarView(form);

        add(header, getContent());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Extract the deviceId parameter from the URL
        Optional<String> deviceId = event.getRouteParameters().get("deviceId");
        if (deviceId.isPresent()) {
            device = service.findDevice(Long.parseLong(deviceId.get()));
            form.setBookingForm(service, device, calendar);

            configureHeader();
            showNotification();
        } else {
            header.add(new H3("Device ID not found!"));
        }
    }

    private void configureHeader() {
        header.removeAll();
        header.setWidthFull();
        header.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-10pct)").setMargin("0");
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
        HorizontalLayout content = new HorizontalLayout(calendar, form);
        content.setFlexGrow(2, calendar);
        content.setFlexGrow(1, form);
        content.setSizeFull();

        form.setWidth("30em");
        return content;
    }

    private void showNotification() {
        Notification notification = new Notification();

        Div text = new Div();

        if (device.getStatus() == DeviceStatus.AVAILABLE) {
            text.add(new Text("Um das Gerät auszuleihen, wählen Sie bitte ein gültiges Datum im Kalender und geben Sie Ihren Namen ein."));
        } else {
            text.add(new Text("Um das Gerät zurückzugeben, wählen Sie bitte ein gültiges Datum im Kalender und klicken Sie auf „Zurückgeben“."));
        }

        Button closeButton = new Button("Bestätigen");
        closeButton.setAriaLabel("Close");
        closeButton.addClickListener(event -> notification.close());

        VerticalLayout layout = new VerticalLayout(text, closeButton);
        layout.setWidth("400px");
        layout.setAlignItems(Alignment.CENTER);

        notification.add(layout);
        notification.setPosition(Notification.Position.MIDDLE);
        notification.open();
    }

}