package iustudy.webdev.ausleihproject.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
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
import iustudy.webdev.ausleihproject.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainLayout.class)
@PageTitle("IU Webprogrammierung | GeräteAusleihe")
@CssImport("./styles/styles.css")
@org.springframework.stereotype.Component
public class SearchView extends VerticalLayout {
    Grid<Device> grid = new Grid<>(Device.class);
    H2 title = new H2();
    TextField filterText = new TextField();
    VerticalLayout centeredLayout = new VerticalLayout();
    VerticalLayout content = new VerticalLayout();
    MainService service;

    @Autowired
    public SearchView(MainService service) {
        addClassName("main-page");
        this.service = service;
        setSizeFull();
        configureGrid();
        setAlignItems(Alignment.CENTER);

        title = new H2("Willkommen beim Geräteausleihsystem!");

        centeredLayout = new VerticalLayout(title, createSearchBar());
        centeredLayout.addClassName("centered-layout");
        centeredLayout.setSizeFull();

        add(centeredLayout, getContent());
    }

    private Component getContent() {
        grid.setVisible(false);
        content = new VerticalLayout(grid);
        return content;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(Device::getType).setHeader("Typ").setSortable(true);
        grid.addColumn(Device::getModel).setHeader("Modell").setSortable(true);
        grid.addColumn(device -> device.getMaxDays() == 0 ? "unbegrenzt" : String.valueOf(device.getMaxDays()))
                .setHeader("Maximale Ausleihtage")
                .setSortable(true);

        grid.addColumn(new ComponentRenderer<>(device -> {
            Span statusSpan = new Span(device.getStatus().toString());
            String color = switch (device.getStatus()) {
                case RENTED -> "orange";
                case MISSING -> "red";
                default -> "green";
            };
            statusSpan.getStyle().set("color", color);
            return statusSpan;
        })).setHeader("Status").setAutoWidth(true).setSortable(true).setComparator(Device::getStatus);

        // Add a new column with clickable buttons
//        grid.addColumn(new ComponentRenderer<>(device -> {
//            Button actionButton = new Button("Click Me");
//            actionButton.addClickListener(event -> {
//                System.out.println("Button clicked for device: " + device.getId());
//            });
//            return actionButton;
//        })).setHeader("Action");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addItemClickListener(event -> {
            Device clickedDevice = event.getItem();
            UI.getCurrent().navigate("booking/" + clickedDevice.getId());
        });
    }

    private Component createSearchBar() {
        filterText.setPlaceholder("Welches Gerät suchen Sie gerade...");
        filterText.setClearButtonVisible(true);
        filterText.addClassName("search-field");

        Button searchButton = new Button("Suchen");
        searchButton.addClassName("search-button");
        searchButton.addClickListener(clickEvent -> searchResult());
        searchButton.addClickShortcut(Key.ENTER);

        var searchBarLayout = new HorizontalLayout(filterText, searchButton);
        searchBarLayout.addClassName("search-bar");
        searchBarLayout.setWidthFull();
        return searchBarLayout;
    }

    private void searchResult() {
        content.setSizeFull();
        grid.setItems(service.searchDevices(filterText.getValue()));
        grid.addClassName("popup");
        grid.setVisible(true);
        centeredLayout.remove(title);
        centeredLayout.addClassName("top-aligned");
    }

    public void refreshSearchResult() {
        filterText.clear();
        grid.setVisible(false);
        centeredLayout.removeAll();
        centeredLayout.add(title, createSearchBar());
        centeredLayout.removeClassName("top-aligned");
        content.setSizeUndefined();
    }

}