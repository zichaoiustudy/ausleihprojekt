package iustudy.webdev.ausleihproject.views.admin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.service.MainService;
import iustudy.webdev.ausleihproject.views.AdminLayout;

import java.util.Collections;

@Route(value = "admin-device", layout = AdminLayout.class)
@PageTitle("IU Webprogrammierung | Admin")
public class AdminDeviceView extends VerticalLayout {
    Grid<Device> grid = new Grid<>(Device.class);
    TextField filterText = new TextField();
    AdminDeviceForm form;
    MainService service;

    public AdminDeviceView(MainService service) {
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
        form = new AdminDeviceForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveDevice);
        form.addDeleteListener(this::deleteDevice);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveDevice(AdminDeviceForm.SaveEvent event){
        service.saveDevice(event.getDevice());
        service.updateDevice(event.getDevice());
        updateList();
        closeEditor();
    }

    private void deleteDevice(AdminDeviceForm.DeleteEvent event){
        service.deleteDevice(event.getDevice());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("id", "type", "model", "maxDays", "status");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editDevice(event.getValue()));
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by type or model...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        filterText.setWidth("25em");

        Button addDeviceButton = new Button("Add device", click -> addDevice());

        return new HorizontalLayout(filterText, addDeviceButton);
    }

    public void editDevice(Device device) {
        if (device == null) {
            closeEditor();
        } else {
            form.setDevice(device);
            form.setVisible(true);
        }
    }

    private void closeEditor() {
        form.setDevice(null);
        form.setVisible(false);
    }

    private void addDevice() {
        grid.asSingleSelect().clear();
        editDevice(new Device());
    }

    private void updateList() {
        grid.setItems(service.searchDevices(filterText.getValue()));
    }
}
