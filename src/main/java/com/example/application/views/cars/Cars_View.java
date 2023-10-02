package com.example.application.views.cars;

import com.example.application.data.Cars;
import com.example.application.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * The Cars_View class represents the user interface for managing and displaying a list of cars.
 * It provides a grid for displaying car information, filtering functionality, and the ability
 * to add, edit, and delete car entries.
 *
 * @author Daniel Altenburg
 * @version 1.0
 * @since 14.09.2023
 */

@PermitAll
@PageTitle("Fuhrpark")
@Route(value = "cars", layout = MainLayout.class)
public class Cars_View extends VerticalLayout {


    Grid<Cars> grid = new Grid<>(Cars.class);
    private final TextField filterText = new TextField();
    private final CarsDialog carsDialog;

    private final CrmService service;
    private final Button addCarsButton = new Button("Auto hinzufügen");


    public Cars_View(CrmService service) {
        this.service = service;
        carsDialog = new CarsDialog(service);
        addCarsButton.getStyle().set("border","1px solid gray").set("color","black");
        setSizeFull();
        configureGrid();
        setCustomersDialogListener();

        add(getToolbar(), grid);
        updateList();

    }

    //This method configures the CarsDialog to listen for Save and Delete events and
    //specifies the methods to be called when these events occur.
    private void setCustomersDialogListener() {

      carsDialog.addListener(CarsDialog.SaveEvent.class, this::saveCars);
      carsDialog.addListener(CarsDialog.DeleteEvent.class, this::deleteCars);

    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("automarke", "kennzeichen", "reifengroessen", "sitzplatz","taxometer","hauptuntersuchung", "tankkarte");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        //set the action for the selected line
        grid.asSingleSelect().addValueChangeListener(event -> editCars(event.getValue()));

    }

    //Create a layout as toolbar.
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Anzeige Filtern");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        // Add a click listener to the "Add Cars" button to open the dialog for adding new cars.
        addCarsButton.addClickListener(click -> addCars());

        //Set the components in a horizontal layout and give this return.
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addCarsButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


     //Opens the CarsDialog for editing or adding a car entry.
    public void editCars(Cars cars) {
        // If 'cars' is null, close the CarsDialog to indicate adding a new car entry.
        if (cars == null) {
            carsDialog.close();

        // If 'cars' is not null, activate the delete button, set car entries for editing,
        // and open the CarsDialog for editing the selected car entry.
        } else {
            carsDialog.activateDeleteButton();
            carsDialog.setCarEntries(cars);
            carsDialog.open();
        }
    }

    //Opens the dialog with blank fields for a new entry.
    private void addCars() {
        grid.asSingleSelect().clear(); //Delete the selected line
        carsDialog.activateDeleteButton();
        carsDialog.setBlankEntries(new Cars());
        carsDialog.open();
    }


    private void updateList() {
        grid.setItems(service.findAllCars(filterText.getValue()));
    }

    //Saves a car entry after receiving a SaveEvent from the CarsDialog.
    private void saveCars(CarsDialog.SaveEvent event) {
        service.saveCars(event.getCars());
        updateList();
        carsDialog.close();
    }

    //Deletes a car entry after receiving a DeleteEvent from the CarsDialog.
    private void deleteCars(CarsDialog.DeleteEvent event) {
        service.deleteCars(event.getCars());
        updateList();
        carsDialog.close();
    }


}
