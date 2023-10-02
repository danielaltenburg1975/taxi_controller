package com.example.application.views.timeRecording;

import com.example.application.data.Employees;
import com.example.application.data.Time_Recording;
import com.example.application.date_and_time.DateAndTimeHandler;
import com.example.application.dialogs.Small_InfoDialog;
import com.example.application.service.CrmService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



/**
This class creates the dialog for editing the time recording dates.
*  @author Daniel Altenburg
*  @version 1.0
*  @since 27.08.2023
*
*/

@Route("dialog-time_recording")
public class TimeRecordingDialog extends Dialog  {
    private final Binder<Time_Recording> binderTimeRecording= new BeanValidationBinder<>(Time_Recording.class);

    private LocalDate selectedStartDate;
    private LocalDate selectedEndDate;
    private final DateAndTimeHandler dateAndTimeHandler = new DateAndTimeHandler();
    private Time_Recording timeRecording;
    private CrmService service;
    private final VerticalLayout containerVerticalLayout = new VerticalLayout();
    private final ComboBox <Employees> employees = new ComboBox<>("Fahrer_ID");
    private final RadioButtonGroup<String> radioBookingType = new RadioButtonGroup<>();
    private final RadioButtonGroup<String> radioSpecialDays = new RadioButtonGroup<>();
    private final Button save = new Button("buchen");
    private final Button delete = new Button("löschen");
    private final Button close = new Button("abbrechen");
    private final DatePicker endDate = new DatePicker("Enddatum auswählen");
    private final DatePicker startDate = new DatePicker("Datum auswählen");
    private final TimePicker timePicker = new TimePicker("Zeit auswählen");
    private final Label headline = new Label();

    private final boolean isNewEntry;




    public TimeRecordingDialog(CrmService service,Time_Recording timeRecording,List <Employees> employeesList,boolean isNewEntry) {
        this.service = service;
        binderTimeRecording.bindInstanceFields(this); //set the entries in the field
        VerticalLayout mainVerticalLayout = new VerticalLayout();
        mainVerticalLayout.setMaxWidth("330px");

        //set the date format
        DatePicker.DatePickerI18n i18n = new DatePicker.DatePickerI18n();
        i18n.setDateFormat("dd.MM.yy");
        i18n.setReferenceDate(LocalDate.of(1980, 2, 2));

        startDate.setI18n(i18n);
        endDate.setI18n(i18n);

        // Filter the list. the entry "0000" must not be booked here,
        // because it is only for the trip transfer, if no driver has been defined yet.
        employeesList = employeesList.stream()
                .filter(employee -> !employee.getPersonalID().equals("0000"))
                .collect(Collectors.toList());

        employees.setItems((employeesList));
        employees.setItemLabelGenerator(Employees:: getPersonalID);
        employees.addValueChangeListener(e -> containerVerticalLayout.setEnabled(true));


        radioBookingType.setLabel("Buchungsart wählen:");
        radioBookingType.setItems("Zeit", "Urlaub", "Krankheit");
        radioSpecialDays.setLabel("Wählen sie die Urlaubsart in Tagen:");
        radioSpecialDays.setItems("ganzer", "halber", "sonder");

        this.service = service;
        this.isNewEntry = isNewEntry;
        this.timeRecording = timeRecording;

        setDraggable(true);
        setCloseOnEsc(false);

        setComponentVisibility();
        setVerticalContainer();
        setHeadline();

        headline.getStyle().set("font-size","16px").set("font-weight", "bold").set("margin-bottom","15px");

        timePicker.setStep(Duration.ofMinutes(15));

        mainVerticalLayout.add(headline,employees);
        containerVerticalLayout.add(setRadioBockingTypes(), setRadioSpecialDays(),startDate,
                                     endDate,timePicker);

        add(mainVerticalLayout, containerVerticalLayout,createButtonsLayout());
    }

    private void setVerticalContainer(){
       containerVerticalLayout.setEnabled(!isNewEntry);

    }

    private void setHeadline(){
        headline.setText(isNewEntry ? "Neue Buchung" : "Buchung bearbeiten");
    }

    private void setComponentVisibility(){
        startDate.setEnabled(false);
        endDate.setVisible(false);
        timePicker.setVisible(false);
        radioSpecialDays.setVisible(false);

    }

    private HorizontalLayout createButtonsLayout() {

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        close.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","35px");
        delete.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","35px");
        save.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","35px");

        save.addClickListener(event -> checkCustomerEntries());
        delete.addClickListener(event -> deleteTimeRecordingEntry());
        close.addClickListener(event -> close());

        return new HorizontalLayout(save, delete, close);
    }

    // Put the radio button for the Bocking types in a horizontal layout and wait for changes
    private HorizontalLayout setRadioBockingTypes(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        radioBookingType.addValueChangeListener(e ->handleRadioBookingAction(radioBookingType.getValue()));
        horizontalLayout.add(radioBookingType);
        return horizontalLayout;
    }

    // Put the radio button for the Special Days types in a horizontal layout and wait for changes
    private HorizontalLayout setRadioSpecialDays(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        radioSpecialDays.addValueChangeListener(e ->handleSingleAction(radioSpecialDays.getValue()));
        horizontalLayout.add(radioSpecialDays);
        return horizontalLayout;
    }

    // Method to handle radio button actions based on the provided radioValue
    private void handleRadioBookingAction(String radioValue) {
        if (isNewEntry)
            radioBookingType.setEnabled(false);

        if (radioValue.equals("Zeit")) {
            timePicker.setVisible(true);
            startDate.setLabel("Datum auswählen");
            startDate.setEnabled(true);

        } else if (radioValue.equals("Urlaub")) {
            timePicker.setVisible(false);
            startDate.setLabel("Startdatum auswählen");

        }else {
            timePicker.setVisible(false);
            setUpDateValueListeners();
            startDate.setEnabled(true);
        }

        radioSpecialDays.setVisible(radioValue.equals("Urlaub"));
    }

    // Method to handle single actions based on the provided specialValue
    private void handleSingleAction(String specialValue) {
        radioSpecialDays.setEnabled(false);
        startDate.setEnabled(true);

        if (specialValue.equals("halber") || specialValue.equals("sonder")) {
            endDate.setVisible(false);
            setStartDateListener();
        } else {
            setUpDateValueListeners();
        }
    }

    private void setStartDateListener(){
        startDate.addValueChangeListener(e -> selectedStartDate = startDate.getValue());
    }

    // Method to set up listeners for date value changes
    private void setUpDateValueListeners() {

        startDate.addValueChangeListener(e -> {
            // If it's a new entry, make the end date visible
            endDate.setVisible(isNewEntry);

            // Update the selected start date
            selectedStartDate = startDate.getValue();

            // Set the minimum date for the end date
            endDate.setMin(e.getValue());
        });

        endDate.addValueChangeListener(e -> {
            // Update the selected end date
            selectedEndDate = endDate.getValue();

            // Set the maximum start date for the end date
            startDate.setMax(e.getValue());
            dateAndTimeHandler.countHolidayAndWeekend(selectedStartDate, selectedEndDate);

        });
    }


    //if the dialog was opened by a single select, set the values in the fields and mark the radio button.
    // Method to set the time recording entries when it's not a new entry
    public void setTimeRecordingEntries(Time_Recording timeRecording, boolean isNewEntry) {
        this.timeRecording = timeRecording;

        // If it's not a new entry
        if (!isNewEntry) {
            // Retrieve the value of time booking from the Time_Recording object
            String tempValue = timeRecording.getZeitbuchung();

            // Split the value based on the separator ", " to extract date and booking type
            String[] split = tempValue.split(", ");

            // Format and set the date from the first part of the split string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
            LocalDate localDate = LocalDate.parse(split[0], formatter);
            startDate.setValue(localDate);

            // Disable employee selection as it's an existing entry
            employees.setEnabled(false);

            // Extract the booking type from the second part of the split string
            String bookingType = split[1];

            // Check the booking type and show/hide corresponding UI elements
            if (!Arrays.asList("U", "U/2", "U/S", "K").contains(bookingType)) {
                // If the booking type is "Zeit", set the radio element to "Zeit" and enable the time picker
                radioBookingType.setValue("Zeit");
                timePicker.setVisible(true);
                DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime time = LocalTime.parse(bookingType, formatterTime);
                timePicker.setValue(time);
            } else {
                // If the booking type is "K", "U", "U/2", or "U/S", set the radio element accordingly and show special day text
                radioBookingType.setValue(bookingType.equals("K") ? "Krankheit" : "Urlaub");
            }
        }
    }

    public void setBinderEntries(Time_Recording timeRecording) {
        this.timeRecording = timeRecording;
        binderTimeRecording.readBean(timeRecording);

    }
    //checks if all fields have been filled in
    private void checkCustomerEntries() {
        //all visible fields must have values
        if (employees.getValue() == null || radioBookingType.getValue() == null || startDate.getValue() == null
                || (radioSpecialDays.isVisible() && radioSpecialDays.isEmpty())
                || (endDate.isVisible() && endDate.isEmpty())
                || (timePicker.isVisible() && timePicker.isEmpty())) {

            new Small_InfoDialog("Es müssen alle Felder ausgefüllt werden").open();
        } else {
            saveBookingEntryAndClose();
        }
    }

    // Validates and saves the booking information based on the selected entries.
    // Closes the booking window afterward.
    private void saveBookingEntryAndClose() {
        String inputValue = startDate.getValue().toString();
        LocalDate localDate = LocalDate.parse(inputValue);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        String formattedDate = localDate.format(formatter);

        // If it is a full day of vacation or a sick day, we need to save for each day.
        if ((radioBookingType.getValue().equals("Urlaub") && radioSpecialDays.getValue().equals("ganzer")) ||
                radioBookingType.getValue().equals("Krankheit")) {

            // Calculate the number of loop iterations using Math.max() to ensure a minimum of 1 iteration
            // this is required if the start date is the same as the end date
            int iterations = Math.max(1, dateAndTimeHandler.getDayInformation().size());

            // Prepare the booking information for the initial day
            String tempBooking = formattedDate + ", " + getDayValueForBookingType();
            saveEntries(employees.getValue(), tempBooking);

            for (int i = 1; i < iterations; i++) { // Start from i = 1 to skip the initial entry
                // Retrieve the temporary day attribute for the current loop pass or use "N".
                String tempDayValue = (i < dateAndTimeHandler.getDayInformation().size()) ? dateAndTimeHandler.getDayInformation().get(i) : "N";

                // Check if the current day value is "N" before saving the entry
                if (tempDayValue.equals("N")) {
                    // Prepare the booking information for the current day, depending on the availability of the day's value.
                    tempBooking = localDate.plusDays(i).format(formatter) + ", " + getDayValueForBookingType();
                    saveEntries(employees.getValue(), tempBooking);
                }
            }

        } else {
            String tempBooking = formattedDate + ", " + getDayValueForBookingType();
            saveEntries(employees.getValue(), tempBooking);
        }

        close();
    }



    //Saves the booking entries based on the passed parameters.
    private void saveEntries(Employees employees, String zeitbuchung) {
            //Is it a new entry? If yes create a new instance
            if (isNewEntry) {
                this.timeRecording = new Time_Recording();
            }
                //set the entries
                timeRecording.setGebucht("NEIN");
                timeRecording.setEmployees(employees);
                timeRecording.setZeitbuchung(zeitbuchung);
            //And try to save
            try {
                service.saveTimeRecording(timeRecording);
            } catch (Exception e) {
                new Small_InfoDialog("Ihre Eingabe scheint nicht zu stimmen! Es müssen alle Felder ausgefüllt werden.").open();
            }
    }

    //Returns the corresponding booking type for the selected day.
    private String getDayValueForBookingType() {
        if (radioBookingType.getValue().equals("Urlaub")) {
            if (radioSpecialDays.getValue().equals("halber")) {
                return "U/2";
            } else if (radioSpecialDays.getValue().equals("ganzer")) {
                return "U";
            } else {
                return "U/S";
            }
        } else if (radioBookingType.getValue().equals("Krankheit")) {
            return "K";
        } else {
            return timePicker.getValue().toString();
        }
    }

    private void deleteTimeRecordingEntry(){
        service.deleteTimeRecording(timeRecording);
        close();

    }


}
