package iustudy.webdev.ausleihproject.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.data.DeviceStatus;
import iustudy.webdev.ausleihproject.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static iustudy.webdev.ausleihproject.views.IndexSearchView.createSearchBar;

@Route(value = "search-results", layout = MainLayout.class)
@PageTitle("IU Webprogrammierung | GeräteAusleihe")
@CssImport("./styles/styles.css")
public class SearchResultView extends VerticalLayout implements BeforeEnterObserver {
    Grid<Device> grid = new Grid<>(Device.class);
    TextField filterText = new TextField();
    MainService service;

    @Autowired
    public SearchResultView(MainService service) {
        addClassName("main-page");
        this.service = service;
        setSizeFull();
        setAlignItems(Alignment.CENTER);

        VerticalLayout centeredLayout = new VerticalLayout(createSearchBar());
        centeredLayout.addClassName("top-aligned");
        centeredLayout.setSizeFull();

        configureGrid();
        add(centeredLayout, grid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String query = event.getLocation().getQueryParameters().getParameters().getOrDefault("query", List.of("")).get(0);
        filterText.setValue(query);
        grid.setItems(service.searchDevices(query));
    }

    private void configureGrid() {
        grid.addClassNames("popup", "round-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(Device::getType).setHeader("Typ").setSortable(true);
        grid.addColumn(Device::getModel).setHeader("Modell").setSortable(true);
        grid.addColumn(device -> device.getMaxDays() == 0 ? "unbegrenzt" : String.valueOf(device.getMaxDays()))
                .setHeader("Maximale Ausleihtage")
                .setSortable(true);

        grid.addColumn(new ComponentRenderer<>(device -> {
            Span statusSpan = new Span();
            String statusText = getStatusText(device.getStatus());
            String color = getStatusColor(device.getStatus());

            statusSpan.setText(statusText);
            statusSpan.getStyle().set("color", color);
            return statusSpan;
        })).setHeader("Status").setAutoWidth(true).setSortable(true).setComparator(Device::getStatus);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addItemClickListener(event -> {
            Device clickedDevice = event.getItem();
            UI.getCurrent().navigate("booking/" + clickedDevice.getId());
        });
    }

    public static String getStatusText(DeviceStatus status) {
        return switch (status) {
            case RENTED -> "vermietet";
            case MISSING -> "fehlend";
            default -> "verfügbar";
        };
    }

    public static String getStatusColor(DeviceStatus status) {
        return switch (status) {
            case RENTED -> "orange";
            case MISSING -> "red";
            default -> "green";
        };
    }
}