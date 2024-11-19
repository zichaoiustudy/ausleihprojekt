package iustudy.webdev.ausleihproject.views.search;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.service.MainService;
import iustudy.webdev.ausleihproject.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "search-results", layout = MainLayout.class)
@PageTitle("IU Webprogrammierung | GeräteAusleihe")
@CssImport("./styles/styles.css")
public class SearchResultView extends VerticalLayout implements BeforeEnterObserver {
    private final Grid<Device> grid = new Grid<>(Device.class);
    private final SearchBar searchBar;
    private final MainService service;

    @Autowired
    public SearchResultView(MainService service) {
        addClassName("main-page");
        this.service = service;
        setSizeFull();
        setAlignItems(Alignment.CENTER);

        searchBar = new SearchBar(query -> UI.getCurrent().navigate("search-results?query=" + query));

        VerticalLayout centeredLayout = new VerticalLayout(searchBar);
        centeredLayout.addClassName("top-aligned");
        centeredLayout.setSizeFull();

        configureGrid();
        add(centeredLayout, grid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String query = event.getLocation().getQueryParameters().getParameters().getOrDefault("query", List.of("")).get(0);
        searchBar.setQuery(query); // Set the initial query in the search bar
        grid.setItems(service.searchDevices(query)); // Fetch results based on query
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
            statusSpan.setText(device.getStatus().getGermanName());
            statusSpan.getStyle().set("color", device.getStatus().getColor());
            return statusSpan;

        })).setHeader("Status").setAutoWidth(true).setSortable(true).setComparator(Device::getStatus);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.addItemClickListener(event -> {
            Device clickedDevice = event.getItem();
            UI.getCurrent().navigate("booking/" + clickedDevice.getId());
        });
    }

}