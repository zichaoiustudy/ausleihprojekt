package iustudy.webdev.ausleihproject.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {

        setAlignItems(Alignment.CENTER);

        // Create and add title text
        H2 title = new H2("Willkommen beim GeräteAusleihe System!");

        Button enter = new Button("Enter");
        //enter.addClickListener(clickEvent -> UI.getCurrent().navigate("/calendar"));

        add(title, enter);
    }

}
