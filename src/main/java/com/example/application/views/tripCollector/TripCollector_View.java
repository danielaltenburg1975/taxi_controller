package com.example.application.views.tripCollector;

import com.example.application.data.TripCollector;
import com.example.application.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * This class represents the view for the trip summary.
 * It displays a grid table with the information about the collected trips.
 *
 * @author Daniel Altenburg
 * @version 1.0
 * @since 27.08.2023
 */

@PermitAll
@PageTitle("Fahrtenübersicht")
@Route(value = "trip_collector", layout = MainLayout.class)

public class TripCollector_View extends VerticalLayout {
    private final Grid<TripCollector> grid = new Grid<>(TripCollector.class,false);
    private final TextField filterText = new TextField();
    private final TripDialog tripDialog;
    private final CrmService service;

    public TripCollector_View(CrmService service) {
    this.service = service;
    addClassName("list-view");
    setSizeFull();
    configureGrid();

    // Create a TripDialog instance with data retrieved from service methods.
    tripDialog = new TripDialog(service.findAllCustomers(null), service.findAllCars(null),
                                service.findAllEmployees(null));

    // Add event listeners for save and delete actions triggered in the TripDialog.
    tripDialog.addListener(TripDialog.SaveEvent.class, this::saveTripCollector);
    tripDialog.addListener(TripDialog.DeleteEvent.class, this::deleteTripCollector);

    add(getToolbar(), grid);
    updateList();

    }

    // The method configures the grid table and sets the columns.
    private void configureGrid() {

        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.setSizeFull();
        grid.addColumn(TripCollector::getZeit).setHeader("Zeit").setSortable(true);
        grid.addColumn(tripCollector -> tripCollector.getCustomers().getFahrgast()).setHeader("Fahrgast").setWidth("100px").setSortable(true);
        grid.addColumn(TripCollector::getAbholort).setHeader("Abholort").setWidth("100px");
        grid.addColumn(TripCollector::getZielort).setHeader("Zielort");
        grid.addColumn(TripCollector::getAnmerkung).setHeader("Telefon / Anmerkung");
        grid.addColumn(TripCollector::getPersonen).setHeader("Personen");
        grid.addColumn(tripCollector -> tripCollector.getCars().getKennzeichen()).setHeader("Auto").setSortable(true);
        grid.addColumn(tripCollector -> tripCollector.getEmployees().getPersonalID()).setHeader("Fahrer_ID").setSortable(true);
        grid.addColumn(TripCollector::getBezahlung).setHeader("Bezahlung");

        grid.asSingleSelect().addValueChangeListener(event ->editTripCollector(event.getValue()));
    }


    // Creates a toolbar for the search field and a button for new entries.
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Suche filtern");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addTripCollectButton = new Button("Neuer Eintrag");
        addTripCollectButton.getStyle().set("border","1px solid gray").set("color","black");
        addTripCollectButton.addClickListener(click -> addTrip());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addTripCollectButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }
    private void saveTripCollector(TripDialog.SaveEvent event) {
        service.saveTripCollector(event.getTripCollector());
        updateList();
        tripDialog.close();
    }
    private void deleteTripCollector(TripDialog.DeleteEvent event) {
        service.deleteTripCollector(event.getTripCollector());
        updateList();
        tripDialog.close();
    }

    public void editTripCollector(TripCollector tripCollector) {
        if (tripCollector == null) {
            tripDialog.close();
        } else {
            tripDialog.setTripCollectorEntries(tripCollector);
            tripDialog.open();
        }
    }

    private void addTrip() {
        clearSingleSelect();
        editTripCollector(new TripCollector());
    }

    private void updateList() {
        grid.setItems(service.findAllTripCollectors(filterText.getValue()));
    }
    private void clearSingleSelect(){
        grid.asSingleSelect().clear();
    }

}
