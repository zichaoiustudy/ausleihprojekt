package iustudy.webdev.ausleihproject.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
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

public class BookingForm extends VerticalLayout {

    Device device;
    MainService service;

    Grid<Button> grid = new Grid<>(Button.class);
    TextField name = new TextField("Name");
    Span warningMessage = new Span();
    Button book = new Button("Buchen");
    Button returnBook = new Button("Zurückgeben");
    Button close = new Button("Zurück");
    VerticalLayout buttons = new VerticalLayout();

    public BookingForm() {}

    public void setBookingForm(MainService service, Device device) {
        this.service = service;
        this.device = device;

        renewForm();
    }

    private void renewForm() {
        removeAll();
        configureGrid();
        createButtonsLayout();
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

        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(new ComponentRenderer<>(button -> button)).setHeader("Buchungen");
        grid.setItems(bookingListButtons);

        grid.getStyle().set("border", "none");
    }

    private void createButtonsLayout() {
        buttons.removeAll();

        book.addClickShortcut(Key.ENTER);
        returnBook.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        close.addClickListener(e -> UI.getCurrent().navigate("/"));
        book.addClickListener(e -> newBooking());
        returnBook.addClickListener(e -> returnDevice());

        if (device.getStatus().name().equals("AVAILABLE")) {
            buttons.add(name, warningMessage, book, close);
        } else {
            buttons.add(returnBook, close);
        }
    }

    public void newBooking() {
        Booking booking = new Booking();
        warningMessage.setText("Bitte Name eingeben!");
        warningMessage.getStyle().set("color", "red");
        warningMessage.setVisible(false);

        if (name.getValue().isEmpty() || name.getValue().isBlank()) {
            warningMessage.setVisible(true);
            return;
        }

        warningMessage.setVisible(false);
        booking.setUserName(name.getValue());
        booking.setDevice(device);
        booking.setBorrowDate(LocalDate.now());

        service.saveBooking(booking);
        UI.getCurrent().navigate("/");
    }

    public void returnDevice() {
        Booking booking = service.findLastBooking(device);
        booking.setReturnDate(LocalDate.now());

        service.saveBooking(booking);
        UI.getCurrent().navigate("/");
    }

}
