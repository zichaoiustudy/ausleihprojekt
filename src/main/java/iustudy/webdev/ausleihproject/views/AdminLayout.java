package iustudy.webdev.ausleihproject.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import iustudy.webdev.ausleihproject.views.admin.AdminBookingView;
import iustudy.webdev.ausleihproject.views.admin.AdminDeviceView;

import static iustudy.webdev.ausleihproject.views.MainLayout.headerTitle;
import static iustudy.webdev.ausleihproject.views.MainLayout.switchToUser;

@CssImport("./styles/styles.css")
public class AdminLayout extends AppLayout {


    public AdminLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H3 logo = new H3(headerTitle);
        Button userSwitch = new Button("Benutzer", e -> switchToUser());
        userSwitch.addClassName("switch-button");

        var header = new HorizontalLayout(new DrawerToggle(), logo, userSwitch);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        addToNavbar(header);
    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Geräte", AdminDeviceView.class),
                new RouterLink("Buchungen", AdminBookingView.class)
        ));
    }

}
