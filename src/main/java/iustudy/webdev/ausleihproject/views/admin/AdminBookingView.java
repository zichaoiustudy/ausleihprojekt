package iustudy.webdev.ausleihproject.views.admin;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import iustudy.webdev.ausleihproject.views.AdminLayout;

@Route(value = "admin-booking", layout = AdminLayout.class)
@PageTitle("IU Webprogrammierung | Admin")
public class AdminBookingView extends HorizontalLayout {
}
