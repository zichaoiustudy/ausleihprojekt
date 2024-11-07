package iustudy.webdev.ausleihproject.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import static iustudy.webdev.ausleihproject.views.IndexSearchView.filterText;

@CssImport("./styles/styles.css")
public class MainLayout extends AppLayout {
    static public H3 logo = new H3("IU Webprogrammierung | GeräteAusleihe");

    public MainLayout() {
        createHeader();
    }

    private void createHeader() {
        logo.addClassName("logo");
        logo.addClickListener(clickEvent -> switchToUser());

        Button adminSwitch = new Button("Admin", e -> switchToAdmin());
        adminSwitch.addClassName("switch-button");

        var header = new HorizontalLayout(logo, adminSwitch);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        addToNavbar(header);
    }

    private void switchToAdmin() {
        UI.getCurrent().navigate("/admin-device");
    }

    public static void switchToUser() {
        filterText.clear();
        UI.getCurrent().navigate("/");
    }

}
