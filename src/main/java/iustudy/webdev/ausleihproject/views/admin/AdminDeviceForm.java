package iustudy.webdev.ausleihproject.views.admin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import iustudy.webdev.ausleihproject.data.Device;
import lombok.Getter;

public class AdminDeviceForm extends FormLayout {

    TextField type = new TextField("Type");
    TextField model = new TextField("Model");
    TextField maxDays = new TextField("Max days");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    BeanValidationBinder<Device> binder = new BeanValidationBinder<>(Device.class);
    public void setDevice(Device device){
        binder.setBean(device);
    }
    public AdminDeviceForm() {
        binder.bindInstanceFields(this);

        add(type,
            model,
            maxDays,
            createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(click -> validateAndSave());
        delete.addClickListener(click -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(click -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    @Getter
    public static abstract class AdminDeviceFormEvent extends ComponentEvent<AdminDeviceForm> {
        private final Device device;

        protected AdminDeviceFormEvent(AdminDeviceForm source, Device device) {
            super(source, false);
            this.device = device;
        }

    }

    public static class SaveEvent extends AdminDeviceFormEvent {
        SaveEvent(AdminDeviceForm source, Device device) {
            super(source, device);
        }
    }

    public static class DeleteEvent extends AdminDeviceFormEvent {
        DeleteEvent(AdminDeviceForm source, Device device) {
            super(source, device);
        }
    }

    public static class CloseEvent extends AdminDeviceFormEvent {
        CloseEvent(AdminDeviceForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
