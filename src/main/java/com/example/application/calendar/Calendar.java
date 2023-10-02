package com.example.application.calendar;

import com.example.application.dialogs.MemoryDialog;
import com.example.application.dialogs.Small_InfoDialog;
import com.example.application.service.CrmService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

/**
 * The `Calendar` class represents a separate calendar component.
 * It displays the current month and allows the user to navigate *between months.

 * The calendar supports highlighting the current day and allows the user to open a dialog
 * for managing reminders associated with a particular day.

 * @author Daniel Altenburg
 * @version 1.0
 * @since 27.08.2023
 */
public class Calendar extends VerticalLayout {

    private final String[] dayName = {"Mo","Di","Mi","Do","Fr","Sa", "So"};
    private final Button backButton = new Button("<<");
    private final Button nextButton = new Button(">>");
    private LocalDate date;
    private int currentMonth,currentYear;
    private int nextDay,backDay;
    private final Label showMonth = new Label();
    private boolean actDay = true;
    private CrmService service;




    public Calendar(CrmService service){
        this.service = service;
        date = LocalDate.now();
        nextButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        backButton.addThemeVariants(ButtonVariant.LUMO_ICON);

        nextButton.getStyle().set("color", "black").set("background","transparent").set("margin-bottom","20px");
        backButton.getStyle().set("color", "black").set("background","transparent");

        showMonth.getStyle().set("margin-bottom","20px");

        nextButton.addClickListener(click -> nextMonth());
        backButton.addClickListener(click -> backMonth());

        add(setCalendar());

    }
    public FormLayout setCalendar() {
        FormLayout formLayout = createFormLayout(); //Pass the form properties.

        addCalendarToolbar(formLayout);
        addDayLabels(formLayout);

        // Add placeholder
        addPlaceholders(formLayout);

        // Add the buttons for the days.
        addDayButtons(formLayout);

        // Update the label to show the month and year.
        setMonthLabel();

        return formLayout;
    }

    // Set the properties for the form.
    private FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.setMaxWidth("400px");
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("0px", 7));

        return formLayout;
    }

    // This method adds empty labels for layout control, sets column spans for empty labels,
    // styles the showMonth label for center alignment, and adds the showMonth label
    // and navigation buttons to the FormLayout.
    private void addCalendarToolbar(FormLayout formLayout) {
        // Add empty labels for layout control
        Label emptyLabel1 = new Label();
        Label emptyLabel2 = new Label();

        // Style the showMonth label for center alignment
        showMonth.getElement().getStyle().set("margin", "auto");
        showMonth.getElement().getStyle().set("text-align", "center");

        // Add the showMonth label and navigation buttons to the FormLayout
        formLayout.add(emptyLabel1, backButton, showMonth, nextButton, emptyLabel2);

        // Set column span for showMonth label to 3 to center it
        formLayout.setColspan(showMonth, 3);
    }


    // This method creates the labels with the week names Mo to Su.
    private void addDayLabels(FormLayout formLayout) {
        for (String s : dayName) {
            Label tempLabel = new Label(s);
            tempLabel.addClassNames("calendar");
            formLayout.add(tempLabel);
        }
    }

    //Sets empty fields if the 1st of a month does not correspond to the first day of the week.
    private void addPlaceholders(FormLayout formLayout) {
        int tempPlaceholderIndex = getIndexPlaceHolder();
        if (tempPlaceholderIndex > 0) {
            for (int p = 0; p <= tempPlaceholderIndex - 1; p++) {
                formLayout.add(new Label());
            }
        }
    }

    // Sets the day-buttons in the form
    private void addDayButtons(FormLayout formLayout) {
        int tempMonthLength = getMonthLength();
        for (int i = 0; i < tempMonthLength; i++) {
            if (i + 1 == date.getDayOfMonth() && actDay) {
                formLayout.add(new CalendarButton(i + 1, true, this));
            } else {
                formLayout.add(new CalendarButton(i + 1, false, this));
            }
        }
    }

    // Puts the month name and the year in the label.
    private void setMonthLabel(){
        String[] tempMonth= {"Januar", "Februar", "März", "April", "Mai", "Juni",
                    "Juli", "August", "September", "Oktober", "November", "Dezember"};

        currentMonth = date.getMonthValue(); //set the current month
        showMonth.setText(tempMonth[currentMonth-1]+" "+currentYear);

    }


    // Gets and returns a LocalDate representing the first day of the current month.

    // This method calculates the LocalDate for the first day of the current month based on
    // the current month and year values. It ensures that the month part is formatted with a leading zero.
    // is formatted with a leading zero if it is less than 10.
    private LocalDate getSearchDate(){
        String tempMonth ;
        currentMonth = date.getMonthValue(); //set the current month
        currentYear = date.getYear(); //set the current year

        //If the daily value is less than 10, a zero must be written.
        if (date.getMonthValue()<10)
            tempMonth = "0" + currentMonth;
        else
            tempMonth = Integer.toString(currentMonth);

        return LocalDate.parse(currentYear+"-"+tempMonth+"-"+"01");
    }

    // Calculates and returns the index of the placeholder (empty labels) for the calendar of the current month.
    // This method determines the day index of the first day of the month for the placement of
    // the placeholders in the FormLayout before the actual buttons.
    private int getIndexPlaceHolder(){
        int placeHolderIndex ;
        int dayIndex = getSearchDate().getDayOfWeek().getValue();
        placeHolderIndex = dayIndex-1;

        return placeHolderIndex;
    }

    // Determine the corresponding month length.
    public int getMonthLength() {
        boolean leapYear = getSearchDate().isLeapYear();//it is a leap year
        int monthLength = 31;

        if (currentMonth == 4||currentMonth == 6 || currentMonth == 9 || currentMonth == 11){
            monthLength = 30;}

        if(currentMonth == 2) {
            monthLength = 29;

            if (!leapYear)
                monthLength = 28;
        }

        return monthLength;
    }

    /**
     * Displays the calendar for the next month and updates the current date accordingly.

     * This method clears the current calendar layout, increments the `nextDay` counter,
     * decrements the `backDay` counter, checks if it's the current day, updates the `date`
     * to the next month, and then adds and displays the updated calendar.
     */
    private void nextMonth(){
        removeAll();
        nextDay++;
        backDay--;
        checkCurrentDay();
        date = LocalDate.now().plusMonths(nextDay);
        add(setCalendar());

    }

    // Has the same function as the nextMonth() method. Displays the calendar
    // for the previous month and updates the current date accordingly.
    private void backMonth(){
        removeAll();
        backDay++;
        nextDay--;
        checkCurrentDay();
        date = LocalDate.now().minusMonths(backDay);
        add(setCalendar());
    }

    private void checkCurrentDay(){
        actDay = nextDay == 0;
    }

    //Opens the dialog for creating and editing the memories.
    public void openMemoriesDialog(String tempDay){
        String inputDateString = tempDay+ ". " + showMonth.getText();

        SimpleDateFormat inputFormat = new SimpleDateFormat("d. MMMM yyyy", Locale.GERMAN);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yy");

        try {
            // Parsen Sie den Eingabestring in ein Date-Objekt
            Date date = inputFormat.parse(inputDateString);

            // Formatieren Sie das Date-Objekt im gewünschten Ausgabeformat
            String outputDateString = outputFormat.format(date);

            // Create a new Dialog
            MemoryDialog test = new MemoryDialog(service, outputDateString);
            test.open();

        } catch (ParseException e) {
            Small_InfoDialog smallInfoDialog = new Small_InfoDialog("Es ist ein Fehler bei der Datumsübergabe (Parsen) aufgetreten");
            smallInfoDialog.open();
        }
    }





}
