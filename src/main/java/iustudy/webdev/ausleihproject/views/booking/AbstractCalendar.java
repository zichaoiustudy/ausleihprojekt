package iustudy.webdev.ausleihproject.views.booking;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import iustudy.webdev.ausleihproject.views.booking.CalendarToolbar.CalendarToolbarBuilder;
import elemental.json.Json;
import elemental.json.JsonObject;
import lombok.AccessLevel;
import lombok.Getter;
import org.vaadin.stefan.fullcalendar.*;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractCalendar extends VerticalLayout {
    private final CalendarToolbar toolbar;
    protected final FullCalendar calendar;

    public AbstractCalendar() {
        calendar = createCalendar(createDefaultInitialOptions());

        calendar.addThemeVariants(FullCalendarVariant.LUMO);

        calendar.addEntryClickedListener(this::onEntryClick);
        calendar.addEntryDroppedListener(this::onEntryDropped);
        calendar.addEntryResizedListener(this::onEntryResized);
        calendar.addDayNumberClickedListener(this::onDayNumberClicked);
        calendar.addBrowserTimezoneObtainedListener(this::onBrowserTimezoneObtained);
        calendar.addMoreLinkClickedListener(this::onMoreLinkClicked);
        calendar.addTimeslotClickedListener(this::onTimeslotClicked);
        calendar.addTimeslotsSelectedListener(this::onTimeslotsSelected);
        calendar.addViewSkeletonRenderedListener(this::onViewSkeletonRendered);
        calendar.addDatesRenderedListener(this::onDatesRendered);
        calendar.addWeekNumberClickedListener(this::onWeekNumberClicked);

        toolbar = createToolbar(CalendarToolbar.builder()
                .calendar(calendar));

        if (toolbar != null) {
            calendar.addDatesRenderedListener(e -> toolbar.updateInterval(e.getIntervalStart()));
        }

        VerticalLayout titleAndDescription = new VerticalLayout();
        titleAndDescription.setSpacing(false);
        titleAndDescription.setPadding(false);

        Component descriptionElement = createDescriptionElement();
        if (descriptionElement != null) {
            titleAndDescription.add(descriptionElement);
            titleAndDescription.setHorizontalComponentAlignment(Alignment.STRETCH, descriptionElement);
        }

        if (toolbar != null) {
            add(toolbar);
            setHorizontalComponentAlignment(Alignment.CENTER, toolbar);
        }

        add(calendar);

        setFlexGrow(1, calendar);
        setHorizontalComponentAlignment(Alignment.STRETCH, calendar);

        setSizeFull();

        postConstruct(calendar);
    }

    protected CalendarToolbar getToolbar() {
        return toolbar;
    }

    protected void postConstruct(FullCalendar calendar) {
        // NOOP
    }

    /**
     * Creates the plain full calendar instance with all initial options. The given default initial options are created by
     * {@link #createDefaultInitialOptions()} beforehand.
     * <p></p>
     * The calendar is automatically embedded afterwords and connected with the toolbar (if one is created, which
     * is the default). Also, all event listeners will be initialized with a default callback method.
     *
     * @param defaultInitialOptions default initial options
     * @return calendar instance
     */
    protected abstract FullCalendar createCalendar(JsonObject defaultInitialOptions);

    /**
     * Creates a default set of initial options.
     *
     * @return initial options
     */
    protected JsonObject createDefaultInitialOptions() {
        JsonObject initialOptions = Json.createObject();
        JsonObject eventTimeFormat = Json.createObject();
        eventTimeFormat.put("hour", "2-digit");
        eventTimeFormat.put("minute", "2-digit");
        eventTimeFormat.put("meridiem", false);
        eventTimeFormat.put("hour12", false);
        initialOptions.put("eventTimeFormat", eventTimeFormat);
        return initialOptions;
    }

    /**
     * Called by the calendar's entry click listener. Noop by default.
     * @see FullCalendar#addEntryClickedListener(ComponentEventListener)
     * @param event event
     */
    protected void onEntryClick(EntryClickedEvent event) {
    }

    /**
     * Called by the calendar's entry drop listener (i.e. an entry has been dragged around / moved by the user).
     * Applies the changes to the entry and calls {@link #onEntryChanged(Entry)} by default.
     * @see FullCalendar#addEntryDroppedListener(ComponentEventListener)
     * @param event event
     */
    protected void onEntryDropped(EntryDroppedEvent event) {
        event.applyChangesOnEntry();
        onEntryChanged(event.getEntry());
    }

    /**
     * Called by the calendar's entry resize listener.
     * Applies the changes to the entry and calls {@link #onEntryChanged(Entry)} by default.
     * @see FullCalendar#addEntryResizedListener(ComponentEventListener)
     * @param event event
     */
    protected void onEntryResized(EntryResizedEvent event) {
        event.applyChangesOnEntry();
        onEntryChanged(event.getEntry());
    }

    /**
     * Called by the calendar's week number click listener. Noop by default.
     * @see FullCalendar#addWeekNumberClickedListener(ComponentEventListener)
     * @param event event
     */
    protected void onWeekNumberClicked(WeekNumberClickedEvent event) {
    }

    /**
     * Called by the calendar's dates rendered listener. Noop by default.
     * Please note, that there is a separate dates rendered listener taking
     * care of updating the toolbar.
     * @see FullCalendar#addDatesRenderedListener(ComponentEventListener)
     * @param event event
     */
    protected void onDatesRendered(DatesRenderedEvent event) {

    }

    /**
     * Called by the calendar's view skeleton rendered listener. Noop by default.
     * @see FullCalendar#addViewSkeletonRenderedListener(ComponentEventListener)
     * @param event event
     */
    protected void onViewSkeletonRendered(ViewSkeletonRenderedEvent event) {

    }
    /**
     * Called by the calendar's timeslot selected listener. Noop by default.
     *
     * @param event event
     * @see FullCalendar#addTimeslotsSelectedListener(ComponentEventListener)
     */
    protected void onTimeslotsSelected(TimeslotsSelectedEvent event) {
    }

    /**
     * Called by the calendar's timeslot clicked listener. Noop by default.
     * @see FullCalendar#addTimeslotClickedListener(ComponentEventListener)
     * @param event event
     */
    protected void onTimeslotClicked(TimeslotClickedEvent event) {

    }

    /**
     * Called by the calendar's "more" link clicked listener. Noop by default.
     * @see FullCalendar#addMoreLinkClickedListener(ComponentEventListener)
     * @param event event
     */
    protected void onMoreLinkClicked(MoreLinkClickedEvent event) {
    }

    /**
     * Called by the calendar's browser timezone obtained listener. Noop by default.
     * Please note, that the full calendar builder registers also a listener, when the
     * {@link FullCalendarBuilder#withAutoBrowserTimezone()} option is used.
     * @see FullCalendar#addBrowserTimezoneObtainedListener(ComponentEventListener)
     * @param event event
     */
    protected void onBrowserTimezoneObtained(BrowserTimezoneObtainedEvent event) {
    }

    /**
     * Called by the calendar's day number click listener. Noop by default.
     * @see FullCalendar#addDayNumberClickedListener(ComponentEventListener)
     * @param event event
     */
    protected void onDayNumberClicked(DayNumberClickedEvent event) {
    }

    protected Component createDescriptionElement() {
        String description = createDescription();
        if (description == null) {
            return null;
        }
        Span descriptionElement = new Span(description);
        descriptionElement.getStyle() // TODO move to css at some point
                .set("font-size", "0.8rem")
                .set("color", "#666");

        return descriptionElement;
    }

    protected String createDescription() {
        return null;
    }

    /**
     * Inits the toolbar. Calendar and the "onSample" callbacks are already set. Change view and date
     * parameters are also enabled by default. Either update the given variable or create a new one, if
     * necessary. Return null for no toolbar at all.
     *
     * @param toolbarBuilder toolbar builder
     * @return modified or new instance
     */
    protected CalendarToolbarBuilder initToolbarBuilder(CalendarToolbarBuilder toolbarBuilder) {
        return toolbarBuilder;
    }

    /**
     * Creates the toolbar. The parameter might be null depending on a custom implementation of
     * {@link #initToolbarBuilder(CalendarToolbarBuilder)}. Return null if no toolbar shall
     * be available.
     *
     * @param toolbarBuilder builder or null
     * @return toolbar or null
     */
    protected CalendarToolbar createToolbar(CalendarToolbarBuilder toolbarBuilder) {
        return toolbarBuilder != null ? toolbarBuilder.build() : null;
    }

    /**
     * Called, when one of the sample entries have been modified, e.g. by an event.
     * Might be called by any other source, too.
     * <p></p>
     * Intended to update the used backend. By default, it will check, if the used entry provider is eager in memory
     * and in that case automatically update the entry provider (to prevent unnecessary code duplication when
     * the default entry provider is used).
     *
     * @param entry entry that has changed
     */
    protected void onEntryChanged(Entry entry) {
        // The eager in memory provider provides API to modify its internal cache and takes care of pushing
        // the data to the client - no refresh call is needed (or even recommended here)
        if (getCalendar().isInMemoryEntryProvider()) {
            // TODO was update before, refreshItem correct here?
            getCalendar().getEntryProvider().refreshItem(entry);
        }
    }

}