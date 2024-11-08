package iustudy.webdev.ausleihproject.views.search;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.function.Consumer;

public class SearchBar extends HorizontalLayout {
    private final TextField filterText;
    private final Consumer<String> searchAction;

    public SearchBar(Consumer<String> searchAction) {
        this.searchAction = searchAction;
        filterText = new TextField();
        filterText.setPlaceholder("Welches Gerät suchen Sie gerade...");
        filterText.setClearButtonVisible(true);
        filterText.addClassName("search-field");

        Button searchButton = new Button("Suchen");
        searchButton.addClassName("search-button");
        searchButton.addClickListener(clickEvent -> search());
        searchButton.addClickShortcut(Key.ENTER);

        add(filterText, searchButton);
        addClassName("search-bar");
        setWidthFull();
    }

    private void search() {
        String query = filterText.getValue();
        UI.getCurrent().getSession().setAttribute("query", query);
        searchAction.accept(query);
    }

    public void setQuery(String query) {
        filterText.setValue(query);
    }

    public static String getQuery() {
        return (String) UI.getCurrent().getSession().getAttribute("query");
    }}