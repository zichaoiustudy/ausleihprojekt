package iustudy.webdev.ausleihproject.views.admin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import iustudy.webdev.ausleihproject.data.Booking;
import iustudy.webdev.ausleihproject.service.MainService;
import iustudy.webdev.ausleihproject.views.AdminLayout;

@Route(value = "admin-booking", layout = AdminLayout.class)
@PageTitle("IU Webprogrammierung | Admin")
public class AdminBookingView extends VerticalLayout {
    Grid<Booking> grid = new Grid<>(Booking.class);
    TextField filterText = new TextField();
    AdminBookingForm form;
    MainService service;

    public AdminBookingView(MainService service) {
        this.service = service;
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new AdminBookingForm(service.findAllDevices());
        form.setWidth("25em");
        form.addSaveListener(this::saveBooking);
        form.addDeleteListener(this::deleteBooking);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveBooking(AdminBookingForm.SaveEvent event){
        service.saveBooking(event.getBooking());
        updateList();
        closeEditor();
    }

    private void deleteBooking(AdminBookingForm.DeleteEvent event){
        service.deleteBooking(event.getBooking());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("id","userName", "borrowDate", "returnDate");
        grid.addColumn(booking -> booking.getDevice().getModel()).setHeader("Device(Model)");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editBooking(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by device or user name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        filterText.setWidth("25em");

        Button addBookingButton = new Button("Add booking", click -> addBooking());

        return new HorizontalLayout(filterText, addBookingButton);
    }

    public void editBooking(Booking booking) {
        if (booking == null) {
            closeEditor();
        } else {
            form.setBooking(booking);
            form.setVisible(true);
        }
    }

    private void closeEditor() {
        form.setBooking(null);
        form.setVisible(false);
    }

    private void addBooking() {
        grid.asSingleSelect().clear();
        editBooking(new Booking());
    }

    private void updateList() {
        grid.setItems(service.searchBookings(filterText.getValue()));
    }

}
