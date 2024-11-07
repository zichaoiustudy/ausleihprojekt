package iustudy.webdev.ausleihproject.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import iustudy.webdev.ausleihproject.data.Booking;
import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.service.MainService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static iustudy.webdev.ausleihproject.views.SearchView.searchResult;

public class BookingForm extends VerticalLayout {

    Device device;
    MainService service;

    Grid<Button> grid = new Grid<>(Button.class);
    TextField name = new TextField("Name");
    Button book = new Button("Buchen");
    Button returnBook = new Button("Zurückgeben");
    Button close = new Button("Zurück");
    VerticalLayout buttons = new VerticalLayout();

    public BookingForm() {}

    public void setBookingForm(MainService service, Device device) {
        this.service = service;
        this.device = device;

        configureGrid();
        configureButtonsLayout();
        add(grid, buttons);
    }

    private void configureGrid() {
        List<Booking> bookingList = service.findBookingsByDevice(device);
        List<Button> bookingListButtons = new ArrayList<>();
        for (Booking booking : bookingList) {
            Button button = new Button();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            String returnDate = Objects.toString(booking.getReturnDate() != null ? booking.getReturnDate().format(formatter) : null, "");
            button.setText(booking.getBorrowDate().format(formatter) + " - " + returnDate);
            bookingListButtons.add(button);
        }
        grid.removeAllColumns();

        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(new ComponentRenderer<>(button -> button)).setHeader("Buchungen");
        grid.setItems(bookingListButtons);

        grid.getStyle().set("border", "none");
    }

    private void configureButtonsLayout() {
        buttons.removeAll();

        book.addClickShortcut(Key.ENTER);
        returnBook.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        close.addClickListener(e -> searchResult());
        book.addClickListener(e -> newBooking());
        returnBook.addClickListener(e -> returnDevice());

        if (device.getStatus().name().equals("AVAILABLE")) {
            buttons.add(name, book, close);
        } else {
            buttons.add(returnBook, close);
        }
    }

    public void newBooking() {
        Booking booking = new Booking();

        if (name.getValue().isEmpty() || name.getValue().isBlank()) {
            name.getStyle().set("--vaadin-input-field-helper-color", "red");
            name.setHelperText("Bitte Name eingeben!");
            return;
        }

        booking.setUserName(name.getValue());
        booking.setDevice(device);
        booking.setBorrowDate(LocalDate.now());

        service.saveBooking(booking);
        UI.getCurrent().navigate("/booking/" + device.getId());
    }

    public void returnDevice() {
        Booking booking = service.findLastBooking(device);
        booking.setReturnDate(LocalDate.now());

        service.saveBooking(booking);
        UI.getCurrent().navigate("/booking/" + device.getId());
    }

}
