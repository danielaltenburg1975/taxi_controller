package com.example.application.views.customers;

import com.example.application.data.Customers;
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
 * This is the Customer View class that displays a list of customers.
 *  It extends VerticalLayout, a Vaadin component for arranging elements vertically.
 *
 *  @author Daniel Altenburg
 *  @version 1.0
 *  @since 27.08.2023
 *
 */

@PermitAll
@PageTitle("Fahrgäste")
@Route(value = "customers", layout = MainLayout.class)

public class Customer_View extends VerticalLayout {

    private final Grid<Customers> grid = new Grid<>(Customers.class);
    private final TextField filterText = new TextField();
    private final CustomersDialog customersDialog;

    CrmService service;

    /**
     * Constructor for Customer_View.
     * @param service The CRM service for managing customer data.
     */
    public Customer_View(CrmService service) {

        customersDialog = new CustomersDialog(service);

        this.service = service;
        setSizeFull();
        configureGrid();
        setCustomersDialogListener();
        add(getToolbar(), grid);
        updateList();

    }

    //Sets up listeners for the CustomersDialog.
    private void setCustomersDialogListener() {

        customersDialog.addListener(CustomersDialog.SaveEvent.class, this::saveCustomers);
        customersDialog.addListener(CustomersDialog.DeleteEvent.class, this::deleteCustomers);
    }

    private void configureGrid() {

        grid.setSizeFull();
        grid.setColumns("fahrgast", "adresse", "telefon", "email","kostentraeger");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editCustomer(event.getValue()));
    }

    //Creates and returns the toolbar with filter text field and "Add" button.
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Anzeige filtern");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Neuer Eintrag");
        addContactButton.getStyle().set("border","1px solid gray").set("color","black");
        addContactButton.addClickListener(click -> addCustomer());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    //Handles the save event from the CustomersDialog.
    private void saveCustomers(CustomersDialog.SaveEvent event) {
        service.saveCustomers(event.getCustomers());
        updateList();
        customersDialog.close();
    }

    //Handles the delete event from the CustomersDialog.
    private void deleteCustomers(CustomersDialog.DeleteEvent event) {
        service.deleteCustomers(event.getCustomers());
        updateList();
        customersDialog.close();
    }

    //Adds a new customer line by opening the CustomersDialog.
    private void addCustomer() {
        grid.asSingleSelect().clear();
        customersDialog.activateDeleteButton();
        customersDialog.setBlankEntries(new Customers());
        customersDialog.open();
    }

    //Opens the CustomersDialog to edit a customer's details.
    public void editCustomer(Customers customers) {
        if (customers == null) {
            customersDialog.close();
        } else {
            customersDialog.activateDeleteButton();
            customersDialog.setCustomersEntries(customers);
            customersDialog.open();
        }
    }


    //Updates the customer list based on the filter text.
    private void updateList() {
        grid.setItems(service.findAllCustomers(filterText.getValue()));
    }


}
