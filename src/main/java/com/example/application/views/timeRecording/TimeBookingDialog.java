package com.example.application.views.timeRecording;

import com.example.application.data.Employees;
import com.example.application.data.Time_Account;
import com.example.application.data.Time_Recording;
import com.example.application.dialogs.Small_InfoDialog;
import com.example.application.service.CrmService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class represents a dialog for managing time recordings. It allows users to select and process time recording entries,
 * including checking for duplicate entries and updating time accounts.
 * <p>
 * The dialog includes a grid for displaying time recording entries, buttons for saving and closing the dialog, and methods
 * for processing the selected entries.
 *
 *  @author Daniel Altenburg
 *  @version 1.0
 *  @since 27.08.2023
 *
 */

@Route("dialog-TimeBooking")
public class TimeBookingDialog extends Dialog {
    Grid<Time_Recording> grid = new Grid<>(Time_Recording.class, false);
    private final CrmService service;
    private final Button save = new Button("übernehmen");
    private final Button close = new Button("abbrechen");
    private final List<Time_Recording> timeRecordingList = new ArrayList<>();
    private  boolean foundExistingAccount = false;
    private String currentDayValue;
    public TimeBookingDialog(CrmService service){
        VerticalLayout verticalLayout = new VerticalLayout();
        this.service = service;
        setWidth("30%");
        setHeight("80%");

        setDraggable(true);
        setCloseOnEsc(false);

        configureGrid();
        Label headline = new Label("Zeiten übernehmen");
        verticalLayout.add(headline,grid,createButtonsLayout());
        add (verticalLayout);
        updateList();

        headline.getStyle().set("font-size","16px").set("font-weight", "bold").set("margin-bottom","15px");

    }

    private void configureGrid() {

        grid.setMaxHeight("350px");
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(timeRecording -> timeRecording.getEmployees().getPersonalID()).setHeader("Fahrer_ID").setSortable(true);
        grid.addColumn(Time_Recording::getZeitbuchung).setHeader("Datum").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

    }

    private HorizontalLayout createButtonsLayout() {
        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        close.getStyle().set("border", "1px solid gray").set("color", "black").set("margin-top", "35px");
        save.getStyle().set("border", "1px solid gray").set("color", "black").set("margin-top", "35px").set("margin-left","25%");

        save.addClickListener(event -> getSelectedItems(grid));
        close.addClickListener(event -> close());

        return new HorizontalLayout(save, close);
    }

    //The method get the selected entries
    private void getSelectedItems(Grid<Time_Recording> grid){
        Set<Time_Recording> selectedItems = grid.getSelectedItems();

        // Check if something is selected
        if (selectedItems.isEmpty()) {
            new Small_InfoDialog("Es wurde noch keine Auswahl getroffen.").open();
        } else {
            for (Time_Recording timeRecording : selectedItems) {

                //In order to be able to work with the "zeitbuchung"" values, these must be split
                String tempEmployee = timeRecording.getEmployees().getPersonalID();
                String tempDateAndTime = timeRecording.getZeitbuchung();
                String[] tempSplitArray = tempDateAndTime.split(", ");

                // remember the current dataset
                if (tempSplitArray[1].equals("U") ||tempSplitArray[1].equals("U/S")
                        || tempSplitArray[1].equals("U/2") || tempSplitArray[1].equals("K")){
                    changeBookingStatus(timeRecording);
                    currentDayValue = tempSplitArray[1];
                    findTimeAccount(tempEmployee, tempSplitArray[0]);

                }else {
                    timeRecordingList.add(timeRecording);

                }
            }
        }
        processTimeRecordings();
        close();
    }

     // Process time recordings to check for duplicate entries and update time accounts.
    private void processTimeRecordings() {
        // Loop through each time recording entry in the list
        for (int i = 0; i < timeRecordingList.size(); i++) {
            // Get the employee ID and date-time of the current time recording entry
            String employeeID1 = timeRecordingList.get(i).getEmployees().getPersonalID();
            String tempDateTime1 = timeRecordingList.get(i).getZeitbuchung();
            String[] tempSplitArray1 = tempDateTime1.split(", ");

            // Compare the current time recording entry with subsequent entries
            for (int j = i + 1; j < timeRecordingList.size(); j++) {
                // Get the employee ID and date-time of the subsequent time recording entry
                String employeeID2 = timeRecordingList.get(j).getEmployees().getPersonalID();
                String tempDateTime2 = timeRecordingList.get(j).getZeitbuchung();
                String[] tempSplitArray2 = tempDateTime2.split(", ");

                // Check if the employee ID and date match in both time recording entries
                if (employeeID1.equals(employeeID2) && tempSplitArray1[0].equals(tempSplitArray2[0])) {

                    // Calculate the time difference between the subsequent entry and the current entry
                    currentDayValue = calculateTimeDifference(tempSplitArray2[1], tempSplitArray1[1]);

                    // Update the time account values for the employee and date
                    findTimeAccount(employeeID1, tempSplitArray1[0]);

                    // Change the booking status of both time recording entries
                    changeBookingStatus(timeRecordingList.get(i));
                    changeBookingStatus(timeRecordingList.get(j));
                }
            }
        }
    }

    // Calculates the time difference between two time strings in the format "HH:mm".
    public static String calculateTimeDifference(String timeString1, String timeString2) {
        // Define a formatter for parsing the time strings
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Parse the time strings into LocalTime objects
        LocalTime time1 = LocalTime.parse(timeString1, formatter);
        LocalTime time2 = LocalTime.parse(timeString2, formatter);

        if (!time1.equals(time2)) {
            // Calculate the duration between the two times
            Duration duration = time1.isBefore(time2) ? Duration.between(time1, time2) : Duration.between(time2, time1);

            // Calculate the total hours in the duration
            double totalHours = (double) duration.toMinutes() / 60.0;

            // Round the time difference to one decimal place
            totalHours = Math.round(totalHours * 10.0) / 10.0;

            return Double.toString(totalHours);
        } else {
            // Return "0.0" if the time strings are the same
            return Double.toString(0.0);
        }
    }

    // Changes the booking status of a given Time_Recording object to "JA" (yes) and saves the changes.
    private void changeBookingStatus(Time_Recording tempTimeRecording){
        tempTimeRecording.setGebucht("JA");
        try {
            service.saveTimeRecording(tempTimeRecording);
        } catch (Exception e) {
            new Small_InfoDialog("Ihre Eingabe scheint nicht zu stimmen! Es müssen alle Felder ausgefüllt werden.").open();
        }
    }

    // The method checks if there is already an entry for the month
    private void findTimeAccount(String employeeID, String date) {
        List<Time_Account> timeAccountList = service.findAllTime_Accounts(employeeID,""); // Search the entries for the employee
        String[] tempSplitArray = date.split("\\."); // Only the month and the year are needed
        int i = 0;
        // Search the list
        for (; i<timeAccountList.size(); i++){

                if (timeAccountList.get(i).getDatum().equals(tempSplitArray[1]+"."+tempSplitArray[2])){
                    foundExistingAccount = true;
                    setNewEntries(timeAccountList.get(i),tempSplitArray[0]);
                    break; // If an existing account was found, cancel the loop
                }
        }
        // No entry was found create one
        if (!foundExistingAccount) {
            createNewTimeAccount(employeeID,tempSplitArray[0], tempSplitArray[1], tempSplitArray[2]);
        }
    }

    private void createNewTimeAccount(String employeeID,String tempDay, String tempMonth, String tempYear) {
        List<Employees> employeesList = service.findAllEmployees(employeeID);
            Time_Account time_account = new Time_Account();

            time_account.setEmployees(employeesList.get(0));
            time_account.setDatum(tempMonth+"."+tempYear);
            time_account.setTagesZeit(setTempMonthEntryWithLength(tempMonth, tempYear));


        try {
            service.saveTime_Account(time_account);
        } catch (Exception e) {
            new Small_InfoDialog("Fehler beim Speichern des Time_Account-Objekts: " + e.getMessage()).open();
        }
        setNewEntries(time_account,tempDay);

    }


    // This method updates the daily entries of a given Time_Account object with a new value for a specific day.
    private void setNewEntries(Time_Account tempTimeAccount, String tempDay) {
        String tempDayValues = tempTimeAccount.getTagesZeit();
        String[] splitDayValues = tempDayValues.split(" ");
        int day = Integer.parseInt(tempDay);

        splitDayValues[day - 1] = currentDayValue; // Replace the entry for the specified day
        String result = String.join(" ", splitDayValues);
        tempTimeAccount.setTagesZeit(result);

        try {
            // Save the updated Time_Account object
            service.saveTime_Account(tempTimeAccount);
        } catch (Exception e) {
            // Display an error dialog if saving the Time_Account object fails
            new Small_InfoDialog("Error while saving the Time_Account object: " + e.getMessage()).open();
        }
    }


    // This method creates a string with a specified number of entries for a given month and year,
    // every entry is first created with 0.0.
    private String setTempMonthEntryWithLength(String tempMonth, String tempYear){
        StringBuilder setMonthEntry = new StringBuilder();
        int currentMonthLength = getMonthLength(tempMonth,tempYear);
        //generate the daily values according to the month length
        setMonthEntry.append("0.0 ".repeat(Math.max(0, currentMonthLength)));

        return setMonthEntry.toString();
    }

    // Calculates the length of a specific month in a given year.
    public int getMonthLength(String tempMonth, String tempYear) {
        // Convert two-digit year to four-digit year
        int year = Integer.parseInt("20" + tempYear);

        // Create a LocalDate object for the specified year
        LocalDate currentYear = LocalDate.of(year, 1, 1);

        // Check if the year is a leap year
        boolean leapYear = currentYear.isLeapYear();

        // Default month length
        int monthLength = 31;

        // Check the day and set the length
        if (tempMonth.equals("04") || tempMonth.equals("06") || tempMonth.equals("09") || tempMonth.equals("11")) {
            monthLength = 30;
        }

        if (tempMonth.equals("02")) {
            monthLength = 29;

            if (!leapYear)
                monthLength = 28;
        }

        return monthLength;
    }

    private void updateList() {
        grid.setItems(service.findAllRecordings("NEIN"));
    }

}
