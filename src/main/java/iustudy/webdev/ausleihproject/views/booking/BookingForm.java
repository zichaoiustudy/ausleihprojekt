package iustudy.webdev.ausleihproject.views.booking;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import iustudy.webdev.ausleihproject.data.Booking;
import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.data.DeviceStatus;
import iustudy.webdev.ausleihproject.service.MainService;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static iustudy.webdev.ausleihproject.views.search.SearchBar.getQuery;

@CssImport("./styles/styles.css")
public class BookingForm extends VerticalLayout {
    Device device;
    MainService service;
    CalendarView calendar;

    Grid<Button> grid = new Grid<>(Button.class);
    TextField name = new TextField("Name");
    VerticalLayout buttons = new VerticalLayout();

    Button book = new Button("Buchen");
    Button returnBook = new Button("Zurückgeben");
    Button close = new Button("Zurück");

    @Setter
    LocalDate selectedDate;

    public BookingForm() {}

    public void setBookingForm(MainService service, Device device, CalendarView calendar) {
        this.service = service;
        this.device = device;
        this.calendar = calendar;
        configureGrid();
        configureButtonsLayout();

        add(grid, buttons);
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setClassName("booking-grid");
        grid.removeAllColumns();

        calendar.removeAllEntries();

        List<Booking> bookingList = service.findBookingsByDevice(device);
        List<Button> bookingListButtons = new ArrayList<>();
        for (Booking booking : bookingList) {
            Button button = new Button();
            button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
            button.addClassName("booking-history-button");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            String returnDate;
            if (booking.getReturnDate() == null) {
                String color = (device.getStatus() == DeviceStatus.MISSING) ? "red" : "orange";
                button.getStyle().set("color", color);
                returnDate = "";

                LocalDate startDate = booking.getBorrowDate().isAfter(LocalDate.now())
                        ? booking.getBorrowDate().plusDays(1)
                        : LocalDate.now().plusDays(1);
                calendar.createEntry(startDate, booking.getBorrowDate().plusDays(device.getMaxDays()), "#FFCCCB", true);
                calendar.createEntry(booking.getBorrowDate(), LocalDate.now(), color);
            } else {
                returnDate = booking.getReturnDate().format(formatter);
                calendar.createEntry(booking.getBorrowDate(), booking.getReturnDate(), "gray");
            }

            button.setText(booking.getBorrowDate().format(formatter) + "  ——  " + returnDate);

            button.addClickListener(e -> calendar.gotoDate(booking.getBorrowDate()));

            bookingListButtons.add(button);
        }

        grid.setColumns();
        grid.addColumn(new ComponentRenderer<>(button -> button)).setHeader("Buchungshistorie");
        grid.setItems(bookingListButtons);

        grid.getStyle().set("border", "none");
    }

    private void configureButtonsLayout() {
        buttons.removeAll();

        book.addClickShortcut(Key.ENTER);
        book.setClassName("booking-button");
        returnBook.addClickShortcut(Key.ENTER);
        returnBook.setClassName("booking-button");
        close.addClickShortcut(Key.ESCAPE);
        close.setClassName("booking-button");

        name.setWidthFull();
        name.setTooltipText("Bitte geben Sie den Namen des Mieters ein.");

        close.addClickListener(e -> UI.getCurrent().navigate("search-results?query=" + getQuery()));
        book.addClickListener(e -> newBooking());
        returnBook.addClickListener(e -> returnDevice());

        HorizontalLayout operations = new HorizontalLayout();
        operations.setSizeFull();

        if (device.getStatus().name().equals("AVAILABLE")) {
            operations.add(book, close);
            buttons.add(name,operations);
        } else {
            operations.add(returnBook, close);
            buttons.add(operations);
        }
    }

    public void newBooking() {
        Booking booking = new Booking();

        if (name.getValue().isEmpty() || name.getValue().isBlank()) {
            name.getStyle().set("--vaadin-input-field-helper-color", "red");
            name.setHelperText("Bitte Name eingeben!");
            return;
        }

        if (selectedDate == null) {
            Notification notification = Notification.show("Es wurde kein Datum ausgewählt!", 2000, Notification.Position.BOTTOM_START);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        booking.setUserName(name.getValue());
        booking.setDevice(device);
        booking.setBorrowDate(selectedDate);

        service.saveBooking(booking);
        refreshPage();
    }

    public void returnDevice() {
        Booking booking = service.findLastBooking(device);

        if (selectedDate == null) {
            Notification notification = Notification.show("Es wurde kein Datum ausgewählt!", 2000, Notification.Position.BOTTOM_START);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        if (!selectedDate.isAfter(booking.getBorrowDate()))
            selectedDate = booking.getBorrowDate();

        booking.setReturnDate(selectedDate);

        service.saveBooking(booking);
        refreshPage();
    }

    private void refreshPage() {
        name.clear();
        calendar.refresh();
        selectedDate = null;
        UI.getCurrent().navigate("/booking/" + device.getId());
    }

}
