package com.example.application.views.newTrip;

import com.example.application.data.New_Trip;
import com.example.application.data.TripCollector;
import com.example.application.dialogs.Small_InfoDialog;
import com.example.application.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.util.List;

/**
 * This class represents a view for managing and displaying trip entries in a web application.
 * It includes a grid for displaying trip data, filtering options, and buttons for editing and deleting trips.
 * The class integrates with a service for data operations and leverages Vaadin components for the user interface.
 *
 * @author Daniel Altenburg
 * @version 1.0
 * @since 27.08.2023
 */

@PermitAll
@PageTitle("Fahrtenkorb")
@Route(value = "newTrip", layout = MainLayout.class)
public class NewTrip_View extends VerticalLayout {
    Grid<New_Trip> grid = new Grid<>(New_Trip.class, false);
    private final TextField filterText = new TextField();
    private final CrmService service;
    private final New_TripDialog newTripDialog;
    private final Button deleteButton = new Button("Einträge löschen");


    public NewTrip_View(CrmService service) {
        this.service = service;

        deleteButton.getStyle().set("border","1px solid gray").set("color","black");

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        // Initialize the New_TripDialog
        newTripDialog = new New_TripDialog(service,service.findAllCustomers(null), service.findAllCars(null),
                service.findAllEmployees(null));

        add(getToolbar(), getContent());
        updateList();
    }

    // Method for building the main content layout
    private Component getContent() {

        HorizontalLayout content = new HorizontalLayout(grid);

        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    // Method for configuring the grid for displaying trip data
    private void configureGrid() {

        grid.addClassNames("contact-grid");
        grid.setSizeFull();

        // Define columns for the grid
        grid.addColumn(New_Trip::getZeit).setHeader("Zeit").setSortable(true);
        grid.addColumn(newTrip -> newTrip.getCustomers().getFahrgast()).setHeader("Fahrgast").setWidth("100px").setSortable(true);
        grid.addColumn(New_Trip::getAbholort).setHeader("Abholort");
        grid.addColumn(New_Trip::getZielort).setHeader("Zielort");
        grid.addColumn(New_Trip::getAnmerkung).setHeader("Telefon / Anmerkung");
        grid.addColumn(newTrip -> newTrip.getEmployees().getPersonalID()).setHeader("Fahrer_ID").setSortable(true);
        grid.addColumn(New_Trip::getGebucht).setHeader("gebucht");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editNewTrip(event.getValue()));
    }

    // Method for building the toolbar with filtering and delete functionality
    private HorizontalLayout getToolbar() {

        filterText.setPlaceholder("Anzeige Filtern");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        deleteButton.addClickListener(click -> deleteNewTrips());

        HorizontalLayout toolbar = new HorizontalLayout(filterText,deleteButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    // Method for editing a selected trip entry
    public void editNewTrip(New_Trip newTrip) {
        if (newTrip == null) {
            newTripDialog.close();
        } else {
            if (newTrip.getGebucht().equals("NEIN")) {
                // Set the data for the New_TripDialog
                newTripDialog.setNew_TripEntries(newTrip, new TripCollector());
                // Update the grid when the dialog is closed
                newTripDialog.addOpenedChangeListener(event -> {
                    if (!event.isOpened()) {
                        updateList();
                    }
                });
                newTripDialog.open();
            } else {
                // Show a message when trying to edit a booked entry
                new Small_InfoDialog("Booked entries cannot be edited").open();
            }
        }
    }
    // Method for deleting selected trip entries
    public void deleteNewTrips() {
        // Initialize and open the delete dialog
        TripDeleteDialog tripDeleteDialog = new TripDeleteDialog(service);
        tripDeleteDialog.addOpenedChangeListener(event -> {
            if (!event.isOpened()) {
                updateList();
            }
        });
        tripDeleteDialog.open();
    }

    //
    public void updateList() {

        List<New_Trip> newTrips = service.findAllNewTrips(filterText.getValue());

        // Sort the list by the "Booked" column so that "NO" comes first.
        newTrips.sort((tr1, tr2) -> {
            if (tr1.getGebucht().equals("NEIN") && !tr2.getGebucht().equals("NEIN")) {
                return -1; // tr1 (NEIN) comes before tr2 (JA)
            } else if (!tr1.getGebucht().equals("NEIN") && tr2.getGebucht().equals("NEIN")) {
                return 1; // tr2 (NEIN) comes before (JA)
            } else {
                return 0; // tr1 und tr2 are same
            }
        });

        grid.setItems(newTrips);
    }

}
