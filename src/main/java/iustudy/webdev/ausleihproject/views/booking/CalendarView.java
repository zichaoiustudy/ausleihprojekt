package iustudy.webdev.ausleihproject.views.booking;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import elemental.json.JsonObject;
import org.vaadin.stefan.fullcalendar.*;

import java.time.LocalDate;
import java.util.Locale;

@CssImport("./styles/calendar.css")
public class CalendarView extends AbstractCalendar{

    BookingForm bookingForm;

    public CalendarView(BookingForm bookingForm) {
        this.bookingForm = bookingForm;
    }

    @Override
    protected FullCalendar createCalendar(JsonObject defaultInitialOptions) {
        FullCalendar calendar = FullCalendarBuilder.create().build();
        calendar.setClassName("calendar");
        calendar.setLocale(Locale.GERMANY);

        calendar.setNowIndicatorShown(true);
        calendar.setNumberClickable(false);
        calendar.setTimeslotsSelectable(true);

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        return calendar;
    }

    @Override
    public void onTimeslotClicked(TimeslotClickedEvent event) {
        LocalDate date = event.getDate();
        if (date.isBefore(LocalDate.now())) {
            Notification notification = Notification.show("Es kann kein vergangenes Datum gewählt werden!", 2000, Notification.Position.BOTTOM_START);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            date = null;
        }
        bookingForm.setSelectedDate(date);
    }

    public void gotoDate(LocalDate date) {
        getCalendar().gotoDate(date);
    }

    public void removeAllEntries() {
        getCalendar().getEntryProvider().asInMemory().removeAllEntries();
    }

    public void refresh() {
        getCalendar().getEntryProvider().refreshAll();
    }

    public void createEntry(LocalDate startDate, LocalDate endDate, String color) {
        Entry entry = new Entry();
        entry.setColor(color);
        entry.setAllDay(true);
        entry.addClassNames("entry");

        entry.setEditable(false);
        entry.setDurationEditable(false);
        entry.setStartEditable(false);

        entry.setStart(startDate);
        entry.setEnd(endDate.plusDays(1));

        getCalendar().getEntryProvider().asInMemory().addEntries(entry);
    }

    public void createEntry(LocalDate startDate, LocalDate endDate, String color, boolean isBackground) {
        Entry entry = new Entry();
        entry.setColor(color);
        entry.setAllDay(true);
        if (isBackground) entry.setDisplayMode(DisplayMode.BACKGROUND);

        entry.setEditable(false);
        entry.setDurationEditable(false);
        entry.setStartEditable(false);

        entry.setStart(startDate);
        entry.setEnd(endDate);

        getCalendar().getEntryProvider().asInMemory().addEntries(entry);
    }

}
