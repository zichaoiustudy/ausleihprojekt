package iustudy.webdev.ausleihproject.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("IU Webprogrammierung | GeräteAusleihe")
@CssImport("./styles/styles.css")
public class IndexSearchView extends VerticalLayout {
    H2 title = new H2("Willkommen beim Geräteausleihsystem!");
    static TextField filterText = new TextField();
    VerticalLayout centeredLayout;

    public IndexSearchView() {
        addClassName("main-page");
        setSizeFull();
        setAlignItems(Alignment.CENTER);

        centeredLayout = new VerticalLayout(title, createSearchBar());
        centeredLayout.addClassName("centered-layout");
        centeredLayout.setSizeFull();

        removeAll();
        add(centeredLayout);
    }

    public static Component createSearchBar() {
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

    public static void searchResult() {
        String query = filterText.getValue();
        UI.getCurrent().navigate("search-results?query=" + query);
    }

}