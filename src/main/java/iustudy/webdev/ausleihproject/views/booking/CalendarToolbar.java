package iustudy.webdev.ausleihproject.views.booking;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Builder;
import org.vaadin.stefan.fullcalendar.FullCalendar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@CssImport("./styles/calendar.css")
public class CalendarToolbar extends HorizontalLayout {

    private final FullCalendar calendar;
    Button buttonDatePicker;
    Span pickDate;

    @Builder
    private CalendarToolbar(FullCalendar calendar) {
        this.calendar = calendar;
        if (calendar == null) {
            throw new IllegalArgumentException("Calendar instance is required");
        }

        initDateItems();
    }

    //Initialisierung der Steuerelemente und Buttons für die Toolbar
    private void initDateItems() {
        setWidthFull();

        // Erstellt Button für Navigierung zum vorherigen Monat
        Button prevButton = new Button(VaadinIcon.ANGLE_LEFT.create(), e -> calendar.previous());
        prevButton.setId("period-previous-button");

        // Initialisiert DatePicker, um ein Datum auszuwählen
        DatePicker gotoDate = new DatePicker();
        gotoDate.setLocale(Locale.GERMANY);
        gotoDate.addValueChangeListener(event1 -> calendar.gotoDate(event1.getValue()));
        gotoDate.getElement().getStyle().set("visibility", "hidden")
                .set("position", "fixed").set("width", "0px").set("height", "0px");
        gotoDate.setWeekNumbersVisible(true);

        buttonDatePicker = new Button(VaadinIcon.CALENDAR.create());
        buttonDatePicker.getElement().appendChild(gotoDate.getElement());
        buttonDatePicker.addClickListener(event -> gotoDate.open());
        buttonDatePicker.setId("period-picker-button");
        buttonDatePicker.setIconAfterText(true);
        buttonDatePicker.setWidth("240px");

        // Erstellt Button für Navigierung zum nächsten Monat
        Button nextButton = new Button(VaadinIcon.ANGLE_RIGHT.create(), e -> calendar.next());
        nextButton.setId("period-next-button");

        // Erstellt Button für Navigierung zum heutigen Tag
        Button todayButton = new Button("Heute", e -> calendar.today());
        todayButton.setId("today-button");

        // Zeigt ausgewähltes Datum an
        pickDate = new Span("Ausgewähltes Datum: ");
        pickDate.setId("selected-date-text");

        HorizontalLayout buttons = new HorizontalLayout(prevButton, buttonDatePicker, nextButton, todayButton);
        buttons.setJustifyContentMode(JustifyContentMode.CENTER);
        buttons.addClassName("calendar-toolbar");

        add(buttons, pickDate);
    }

    public void updateSelectedDateText(LocalDate date) {
        remove(pickDate);
        if (date != null) {
            String formattedDate = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            pickDate.setText("Ausgewähltes Datum: " + formattedDate);
        } else {
            pickDate.setText("Ausgewähltes Datum: ");
        }
        pickDate.setId("selected-date-text");
        add(pickDate);
    }

    public void updateInterval(LocalDate intervalStart) {
        if (buttonDatePicker != null) {
            updateIntervalLabel(buttonDatePicker, intervalStart);
        }
    }

    void updateIntervalLabel(HasText intervalLabel, LocalDate intervalStart) {
        String text;
        Locale locale = calendar.getLocale();

        String pattern = "MMMM yyyy";
        text = intervalStart.format(DateTimeFormatter.ofPattern(pattern).withLocale(locale));

        intervalLabel.setText(text);
    }

}
