package iustudy.webdev.ausleihproject.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import iustudy.webdev.ausleihproject.data.Booking;
import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.service.BookingService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookingForm extends VerticalLayout {

    Device device;
    BookingService bookingService;
    Grid<Button> grid = new Grid<>(Button.class);
    Button book = new Button("Buchen");
    Button returnBook = new Button("Zurückgeben");
    Button close = new Button("Homepage");

    public BookingForm() {}

    public void setBookingForm(BookingService bookingService, Device device) {
        this.bookingService = bookingService;
        this.device = device;

        configureGrid();
        add(grid, createButtonsLayout());
    }

    private void configureGrid() {
        List<Booking> bookingList = bookingService.findBookingsByDevice(device);
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

    private Component createButtonsLayout() {

        close.addClickShortcut(Key.ESCAPE);
        if (device.getStatus().name().equals("AVAILABLE")) {
            book.addClickShortcut(Key.ENTER);
            return new HorizontalLayout(book, close);
        } else {
            returnBook.addClickShortcut(Key.ENTER);
            return new HorizontalLayout(returnBook, close);
        }

    }

}
