package com.example.application.views.index;

import com.example.application.calendar.Calendar;
import com.example.application.data.Memories;
import com.example.application.data.TripCollector;
import com.example.application.dialogs.InfoTripDialog;
import com.example.application.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The {@code Index_View} class represents the home page of the application.
 * This view displays a list of trips in a grid, allows searching for reminders,
 * and includes a calendar component for displaying appointments.
 *
 *  @author Daniel Altenburg
 *  @version 1.0
 *  @since 27.08.2023
 */

@PermitAll
@PageTitle("Homepage")
@Route(value = "index", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class Index_View extends VerticalLayout {

    private final Grid<TripCollector> grid = new Grid<>(TripCollector.class, false);
    private final TextField filterText = new TextField();
    private final LocalDate date = LocalDate.now();
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yy");

    private final CrmService service;

    public Index_View(CrmService service) {
        this.service = service;

        // Configuring the grid to display trip data
        configureGrid();

        // Setting up a calendar component
        setCalendar();

        // Creating a reminder search box
        setRememberBox();

        // Setting the size of the main layout
        setSizeFull();

        // Updating the grid with data
        updateList();

        // Creating a layout for messages
        VerticalLayout messageLayout = new VerticalLayout();
        messageLayout.setVisible(true);
    }

    // Method to set up the calendar component
    private void setCalendar() {
        Calendar calendar = new Calendar(service);
        calendar.setMinWidth("450px");

        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("0px", 2));
        formLayout.add(calendar, setMessageBox());
        add(formLayout);
    }

    // Method to create and display messages
    private VerticalLayout setMessageBox() {
        VerticalLayout messageLayout = new VerticalLayout();

        Label messageHeadline = new Label("Bitte beachten:");
        messageHeadline.getStyle().set("color", "black").set("font-size", "16px").set("font-weight", "bold");

        messageLayout.add(messageHeadline, createUnbookedTripMessage(), createCurrentMemoryMessage());

        return messageLayout;
    }

    // The method checks if there are unbooked trips and creates a link to the class if the search is successful.
    private Component createUnbookedTripMessage() {
        Div divLayout = new Div();

        // Construct the message as an Anchor (Link)
        String messageText;
        long ungebuchteFahrtenAnzahl = service.countNewTripWithCondition("NEIN");

        if (ungebuchteFahrtenAnzahl > 0) {
            messageText = "Es gibt " + ungebuchteFahrtenAnzahl + " ungebuchte Fahrten.";
            Anchor messageLink = new Anchor("newTrip", messageText);
            messageLink.getStyle().set("text-decoration", "underline").set("color", "black");

            divLayout.add(messageLink);
            // Return the link container
        } else {
            String emptyMessage = "Es gibt keine ungebuchten Fahrten";
            Text emptyMessageText = new Text(emptyMessage);

           divLayout.add(emptyMessageText);
        }
        return divLayout;
    }

    // The method checks if there is a memory for the current day.
    private Component createCurrentMemoryMessage(){

        Div divLayout = new Div();
        divLayout.getStyle().set("margin-top", "30px");
        String currentDate = date.format(format);

        List<Memories> memoryList = service.findMemories(currentDate);

        if (!memoryList.isEmpty()) {
            Memories memories = memoryList.get(0);
            Text currentMemory = new Text(memories.getErinnerung());
            divLayout.add(currentMemory);
        }
        return divLayout;
    }

    // Method to set up the reminder search box
    private void setRememberBox() {


        VerticalLayout rememberBox = new VerticalLayout();

        filterText.setValue(date.format(format));
        filterText.setVisible(true);
        filterText.setPlaceholder("Search for reminders");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        rememberBox.add(filterText, grid);
        add(rememberBox);
    }

    // Method to configure the grid's columns
    private void configureGrid() {
        grid.setWidth("100%");

        // Adding columns to the grid
        grid.addColumn(TripCollector::getZeit).setHeader("Zeit").setSortable(true);
        grid.addColumn(tripCollector -> tripCollector.getCustomers().getFahrgast()).setHeader("Fahrgast").setWidth("100px").setSortable(true);
        grid.addColumn(TripCollector::getAbholort).setHeader("Abholort").setWidth("100px");
        grid.addColumn(TripCollector::getZielort).setHeader("Zielort");

        grid.addColumn(tripCollector -> tripCollector.getCars().getKennzeichen()).setHeader("Auto").setSortable(true);
        grid.addColumn(tripCollector -> tripCollector.getEmployees().getPersonalID()).setHeader("Fahrer").setSortable(true);

        // Clear any previous selection when selecting a new item
        grid.asSingleSelect().addValueChangeListener(event -> editTripCollector(event.getValue()));
        grid.asSingleSelect().clear();
    }

    // Method to update the grid with filtered data
    public void updateList() {
        grid.setItems(service.findAllTripCollectors(filterText.getValue()));
    }

    // Method to open a dialog to edit trip details
    public void editTripCollector(TripCollector tripCollector) {
        if (tripCollector != null) {
            InfoTripDialog infoTripDialog = new InfoTripDialog(tripCollector);
            infoTripDialog.open();
        }
    }
}
