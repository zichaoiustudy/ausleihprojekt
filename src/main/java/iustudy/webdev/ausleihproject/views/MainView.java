package iustudy.webdev.ausleihproject.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.service.DeviceService;

@Route("")
@PageTitle("IU Webprogrammierung | GeräteAusleihe")
public class MainView extends VerticalLayout {
    Grid<Device> grid = new Grid<>(Device.class);
    TextField filterText = new TextField();
    DeviceService deviceService;

    public MainView(DeviceService deviceService) {
        this.deviceService = deviceService;
        setSizeFull();
        configureGrid();
        setAlignItems(Alignment.CENTER);

        H2 title = new H2("Willkommen beim GeräteAusleihe System!");

        //Button enter = new Button("Enter");
        //enter.addClickListener(clickEvent -> UI.getCurrent().navigate("/calendar"));

        add(title, createSearchBar(), getContent());
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.setFlexGrow(1, grid);
        content.addClassNames("content");
        content.setSizeFull();
        grid.setVisible(false);
        return content;
    }

    private void configureGrid() {
        grid.addClassNames("device-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(Device::getType).setHeader("Typ");
        grid.addColumn(Device::getModel).setHeader("Modell");

        grid.addColumn(new ComponentRenderer<>(device -> {
            Span statusSpan = new Span(device.getStatus().toString());
            String color = switch (device.getStatus()) {
                case RENTED -> "yellow";
                case MISSING -> "red";
                default -> "green";
            };
            statusSpan.getStyle().set("color", color);
            return statusSpan;
        })).setHeader("Status").setAutoWidth(true).setSortable(true).setComparator(Device::getStatus);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

    }

    private Component createSearchBar() {
        filterText.setPlaceholder("Welches Gerät suchen Sie gerade...");
        filterText.setClearButtonVisible(true);
        filterText.setWidth("50%");

        Button searchButton = new Button("Suchen");
        searchButton.addClickListener(clickEvent -> searchResult());

        var searchBarLayout = new HorizontalLayout(filterText, searchButton);
        searchBarLayout.addClassName("search-bar");
        searchBarLayout.setWidthFull();
        searchBarLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return searchBarLayout;
    }

    private void searchResult() {
        grid.setItems(deviceService.searchDevices(filterText.getValue()));
        grid.setVisible(true);
    }

}
