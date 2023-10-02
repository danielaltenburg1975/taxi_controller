package com.example.application.views.newTrip;

import com.example.application.data.*;
import com.example.application.dialogs.Small_InfoDialog;
import com.example.application.service.CrmService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

import java.util.List;

/**
 * This class represents a dialog for creating and editing trip entries in a web application.
 * It provides a user-friendly interface for inputting and managing trip-related information,
 * including date, customer, location, and payment. The class leverages Vaadin components and
 * follows good coding practices to enhance code readability and maintainability.
 *
 * @author Daniel Altenburg
 * @version 1.0
 * @since 27.08.2023
 */

@Route("dialog-new_trip")
public class New_TripDialog extends Dialog  {

    private TripCollector tripCollector;
    private New_Trip newTrip;
    private final CrmService service;
    private final Binder<New_Trip> binderNew_Trip= new BeanValidationBinder<>(New_Trip.class);
    private final TextField zeit = new TextField("Zeit");
    private final TextArea abholort = new TextArea("Abholort");
    private final TextArea zielort = new TextArea("Zielort");
    private final TextField anmerkung = new TextField("Telefon & Anmerkung");
    private final TextField personen = new TextField("Personen");
    private final ComboBox <Customers> customers = new ComboBox<>("Fahrgast");
    private final ComboBox <Cars> cars = new ComboBox<>("Auto");
    private final ComboBox <Employees> employees = new ComboBox<>("Fahrer_ID");
    private final ComboBox <String> bezahlung = new ComboBox<>("Bezahlung");
    private final Button save = new Button("buchen");
    private final Button delete = new Button("löschen");
    private final Button close = new Button("abbrechen");

    /**
     * Constructs a new instance of New_TripDialog.
     *
     * @param service        The service used for data operations.
     * @param customersList  A list of customer objects.
     * @param carsList       A list of car objects.
     * @param employeesList  A list of employee objects.
     */
    public New_TripDialog(CrmService service, List<Customers> customersList, List<Cars> carsList, List <Employees> employeesList) {
        this.service = service;
        binderNew_Trip.bindInstanceFields(this); //set the entries in the field

        // Create a headline for the dialog
        Label headline = new Label("Fahrten anlegen / bearbeiten");
        headline.getStyle().set("font-size","16px").set("font-weight", "bold");

        setDraggable(true);
        customers.setItems((customersList));
        customers.setItemLabelGenerator(Customers::getFahrgast);
        customers.addValueChangeListener(event ->setAutoValue());


        cars.setItems((carsList));
        cars.setItemLabelGenerator(Cars::getKennzeichen);

        employees.setItems((employeesList));
        employees.setItemLabelGenerator(Employees:: getPersonalID);

        // Settings for the "payment" ComboBox
        bezahlung.setAllowCustomValue(true);
        bezahlung.setItems("bar", "privat RE", "Rechnung", "KTSs","KTSe", "BG","sonstiges");

        zeit.getStyle().set("margin-top","20px");
        add(headline,setDialog(),createButtonsLayout());

        addClassName("contact-form");

    }


    //automatically places the phone and address entries in the fields when a customer is selected.
    private void setAutoValue(){

        anmerkung.setValue(customers.getValue().getTelefon());
        abholort.setValue((customers.getValue().getAdresse()));

    }

    // Settings for the form layout
    private FormLayout setDialog(){
        FormLayout formLayout = new FormLayout();
        formLayout.setMaxWidth("400px");
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("0px", 2));
        formLayout.setColspan(anmerkung, 2);

        formLayout.add(zeit,customers,abholort,zielort,anmerkung,personen,cars,employees,bezahlung);
        return formLayout;
    }

    //the design for the toolbar
    private HorizontalLayout createButtonsLayout() {

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        close.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","35px");
        delete.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","35px");
        save.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","35px").set("margin-left","50px");

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> deleteNewTripEntry());
        close.addClickListener(event -> close());

        binderNew_Trip.addStatusChangeListener(e -> save.setEnabled(binderNew_Trip.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    // Set new trip entries
    public void setNew_TripEntries(New_Trip newTrip,TripCollector tripCollector) {

        this.tripCollector = tripCollector;
        this.newTrip = newTrip;
        binderNew_Trip.readBean(newTrip);

    }

    // Validate and save data
    private void validateAndSave() {
        if (newTrip.getGebucht().equals("NEIN")) {
            tripCollector.setZeit(zeit.getValue());
            tripCollector.setCustomers(customers.getValue());
            tripCollector.setAbholort(abholort.getValue());
            tripCollector.setZielort(zielort.getValue());
            tripCollector.setAnmerkung(anmerkung.getValue());
            tripCollector.setPersonen(personen.getValue());
            tripCollector.setCars(cars.getValue());
            tripCollector.setEmployees(employees.getValue());
            tripCollector.setBezahlung(bezahlung.getValue());

            try {
                service.saveTripCollector(tripCollector);
                newTrip.setGebucht("JA");
                service.saveNew_Trip(newTrip);
                close();

            } catch (Exception e) {
                new Small_InfoDialog("Ihre Eingabe scheinen nicht zu stimmen! Es müssen alle Felder ausgefüllt werden.").open();
            }
        }else {
            new Small_InfoDialog("Bereits gebuchte Einträge können nicht mehr bearbeitet werden!").open();
            close();

        }
    }

    // Delete a new trip entry
    private void deleteNewTripEntry(){
        service.deleteNewTrips(newTrip);
        close();

    }


}
