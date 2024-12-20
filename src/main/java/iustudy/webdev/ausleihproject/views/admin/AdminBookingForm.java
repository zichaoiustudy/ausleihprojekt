package iustudy.webdev.ausleihproject.views.admin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import iustudy.webdev.ausleihproject.data.Booking;
import iustudy.webdev.ausleihproject.data.Device;
import lombok.Getter;

import java.util.List;

public class AdminBookingForm extends FormLayout {

    ComboBox<String> deviceType = new ComboBox<>("Device Type");
    ComboBox<Device> device = new ComboBox<>("Device");
    TextField userName = new TextField("User Name");
    DatePicker borrowDate = new DatePicker("Borrow Date");
    DatePicker returnDate = new DatePicker("Return Date");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    BeanValidationBinder<Booking> binder = new BeanValidationBinder<>(Booking.class);

    public void setBooking(Booking booking){
        binder.setBean(booking);
    }

    public AdminBookingForm(List<Device> devices) {

        List<String> deviceTypes = devices.stream().map(Device::getType).distinct().toList();
        deviceType.setItems(deviceTypes);

        binder.forField(deviceType)
                .withConverter(
                        type -> {
                            // Convert from device type String to Device (for saving to Booking)
                            return devices.stream()
                                    .filter(d -> d.getType().equals(type))
                                    .findFirst()
                                    .orElse(null);
                        },
                        deviceObj -> {
                            // Convert from Device to device type String (for displaying in deviceType ComboBox)
                            return deviceObj != null ? deviceObj.getType() : "";
                        }
                ).bind(Booking::getDevice, Booking::setDevice);
        binder.forField(device).bind(Booking::getDevice, Booking::setDevice);
        binder.forField(userName).bind(Booking::getUserName, Booking::setUserName);
        binder.forField(borrowDate).bind(Booking::getBorrowDate, Booking::setBorrowDate);
        binder.forField(returnDate).bind(Booking::getReturnDate, Booking::setReturnDate);

        deviceType.addValueChangeListener(e -> {
            String selectedType = e.getValue();
            List<Device> filteredDevices = devices.stream().filter(d -> d.getType().equals(selectedType)).toList();
            device.setItems(filteredDevices);
            device.clear();
        });

        device.setItemLabelGenerator(Device::getModel);

        add(deviceType,
                device,
                userName,
                borrowDate,
                returnDate,
                createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new AdminBookingForm.DeleteEvent(this, binder.getBean())));
        close.addClickListener(click -> fireEvent(new AdminBookingForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new AdminBookingForm.SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    @Getter
    public static abstract class AdminBookingFormEvent extends ComponentEvent<AdminBookingForm> {
        private final Booking booking;

        protected AdminBookingFormEvent(AdminBookingForm source, Booking booking) {
            super(source, false);
            this.booking = booking;
        }
    }

    public static class SaveEvent extends AdminBookingForm.AdminBookingFormEvent {
        SaveEvent(AdminBookingForm source, Booking booking) {
            super(source, booking);
        }
    }

    public static class DeleteEvent extends AdminBookingForm.AdminBookingFormEvent {
        DeleteEvent(AdminBookingForm source, Booking booking) {
            super(source, booking);
        }
    }

    public static class CloseEvent extends AdminBookingForm.AdminBookingFormEvent {
        CloseEvent(AdminBookingForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<AdminBookingForm.DeleteEvent> listener) {
        return addListener(AdminBookingForm.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<AdminBookingForm.SaveEvent> listener) {
        return addListener(AdminBookingForm.SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<AdminBookingForm.CloseEvent> listener) {
        return addListener(AdminBookingForm.CloseEvent.class, listener);
    }

}
