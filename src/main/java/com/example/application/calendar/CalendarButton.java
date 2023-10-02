package com.example.application.calendar;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
/**
 * A custom button component for use in a calendar application. This button represents a day on the calendar and provides the ability to mark it as a temporary day.
 * Clicking on the button opens a memory dialog associated with the clicked day.
 *
 * @author Daniel Altenburg
 * @version 1.0
 * @since 27.08.2023
 *
 */

public class CalendarButton extends Button {

    private final int currentDay;
    private final Calendar calendar;

    /**
     * Creates a new instance of a CalendarButton.
     *
     * @param currentDay The day to be displayed on the button.
     * @param tempDay    Indicates whether the day is temporary and should be visually marked.
     * @param calendar   The parent calendar to which this button belongs.
     */
    public CalendarButton(int currentDay, boolean tempDay, Calendar calendar) {
        this.currentDay = currentDay;
        this.calendar = calendar;

        // Set button style properties.
        getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("color", "black")
                .set("padding", "0")
                .set("border-radius", "50%");

        setText(Integer.toString(currentDay));
        addClickListener(e -> setDayForMemoryDialog());
        addThemeVariants(ButtonVariant.LUMO_ICON);
        setButtonDesign(tempDay);
    }

    //Sets the visual design of the button based on whether it's a temporary day.
    public void setButtonDesign(boolean tempDay) {
        if (tempDay) {
            getStyle().set("background-color", "#9ca6ae");

        } else {
            getStyle().set("background-color", "transparent");
        }
    }

    // Opens a memory dialog associated with the current day.
    private void setDayForMemoryDialog(){
        calendar.openMemoriesDialog(Integer.toString(currentDay));

    }

}
