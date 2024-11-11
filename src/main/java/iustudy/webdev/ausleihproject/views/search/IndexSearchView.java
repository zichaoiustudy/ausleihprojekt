package iustudy.webdev.ausleihproject.views.search;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import iustudy.webdev.ausleihproject.views.MainLayout;

@Route(value = "", layout = MainLayout.class)
@PageTitle("IU Webprogrammierung | GeräteAusleihe")
@CssImport("./styles/styles.css")
public class IndexSearchView extends VerticalLayout {

    public IndexSearchView() {
        addClassName("main-page");
        setSizeFull();
        setAlignItems(Alignment.CENTER);

        SearchBar searchBar = new SearchBar(query ->
                UI.getCurrent().navigate("search-results?query=" + query));
        H2 title = new H2("Willkommen beim Geräteausleihsystem!");

        VerticalLayout centeredLayout = new VerticalLayout(title, searchBar);
        centeredLayout.addClassName("centered-layout");
        centeredLayout.setSizeFull();

        add(centeredLayout);
    }
}