package com.example.application.views.employees;

import com.example.application.data.Employees;
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
* This is the Employees View class that displays a list of employees.
* It extends VerticalLayout, a Vaadin component for arranging elements vertically.
*
* @author Daniel Altenburg
* @version 1.0
* @since 15.08.2023
*
*/
@PermitAll
@PageTitle("Fahrer")
@Route(value = "employees", layout = MainLayout.class)

public class Employees_View extends VerticalLayout {

    private final EmployeesDialog employeesDialog;

    private final Grid<Employees> grid = new Grid<>(Employees.class);
    private final TextField filterText = new TextField();
    private final CrmService service;

    /**
     * Constructor for Employees_View.
     * @param service The CRM service for managing employees' data.
     */
    public Employees_View(CrmService service) {
        employeesDialog = new EmployeesDialog(service);

        this.service = service;

        configureGrid();
        setSizeFull();
        setEmployeesDialogListener();
        add(getToolbar(), grid);
        updateList();
    }

    //Sets up listeners for the EmployeeDialog.
    private void setEmployeesDialogListener() {
        employeesDialog.addListener(EmployeesDialog.SaveEvent.class, this::saveEmployees);
        employeesDialog.addListener(EmployeesDialog.DeleteEvent.class, this::deleteEmployees);
    }

    private void configureGrid() {

        grid.setSizeFull();
        grid.setColumns("personalID", "name", "adresse", "telefon", "email", "geburtstag",
                        "ein_austritt","urlaubsanspruch", "pensum","krankenversicherung", "sozialversicherung");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editContact(event.getValue()));
    }

    //Creates and returns the toolbar with filter text field and "Add" button.
    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("suche Filtern");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Neuer Eintrag");
        addContactButton.getStyle().set("border","1px solid gray").set("color","black");
        addContactButton.addClickListener(click -> addEmployees());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    //Handles the save event from the EmployeesDialog.
    private void saveEmployees(EmployeesDialog.SaveEvent event) {
        service.saveEmployee(event.getEmployees());
        updateList();
        employeesDialog.close();
    }

    //Handles the delete event from the EmployeesDialog.
    private void deleteEmployees(EmployeesDialog.DeleteEvent event) {
        service.deleteEmployee(event.getEmployees());
        updateList();
        employeesDialog.close();
    }

    //Opens the EmployeesDialog to edit an employee's details.
    public void editContact(Employees employees) {
        if (employees == null) {
            employeesDialog.close();
        } else {
            employeesDialog.activateDeleteButton();
            employeesDialog.setEmployeeEntries(employees);
            employeesDialog.open();
        }
    }

    //Adds a new employee line by opening the EmployeesDialog.
    private void addEmployees() {
        grid.asSingleSelect().clear();
        employeesDialog.activateDeleteButton();
        employeesDialog.setBlankEntries(new Employees());
        employeesDialog.open();
    }

    //Updates the employees list based on the filter text.
    private void updateList() {
        grid.setItems(service.findAllEmployees(filterText.getValue()));
    }

}
