package com.example.application.views.tripCollector;

import com.example.application.data.Cars;
import com.example.application.data.Customers;
import com.example.application.data.Employees;
import com.example.application.data.TripCollector;
import com.example.application.dialogs.Small_InfoDialog;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
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
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

/**
This class creates the dialog for editing the trip-collect dates
 *
 *  @author Daniel Altenburg
 *  @version 1.0
 *  @since 29.08.2023
 */

@Route("dialog-tripCollector")
public class TripDialog extends Dialog  {
    private final ZonedDateTime actDate = ZonedDateTime.now();
    private final DateTimeFormatter month = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    private TripCollector tripCollector;

    // Bind the binder to the class fields
    Binder<TripCollector> binderTripCollector = new BeanValidationBinder<>(TripCollector.class);

    private final TextField zeit = new TextField("Zeit");
    private final TextArea abholort = new TextArea("Abholort");
    private final TextArea zielort = new TextArea("Zielort");
    private final TextField anmerkung = new TextField("Telefon & Anmerkung");
    private final TextField personen = new TextField("Personen");
    private final ComboBox <Customers> customers = new ComboBox<>("Fahrgast");
    private final ComboBox <Cars> cars = new ComboBox<>("Auto");
    private final ComboBox <Employees> employees = new ComboBox<>("Fahrer_ID");
    private final ComboBox <String> bezahlung = new ComboBox<>("Bezahlung");
    private final Button save = new Button("speichern");
    private final Button delete = new Button("löschen");
    private final Button close = new Button("abbrechen");

    /**
     * Constructor for creating a TripDialog.
     *
     * @param customersList  A list of customer objects to populate the customer combo box.
     * @param carsList       A list of car objects to populate the car combo box.
     * @param employeesList  A list of employee objects to populate the employee combo box.
     */
    public TripDialog(List<Customers> customersList, List<Cars> carsList, List <Employees> employeesList) {

        // Initialization of the binder for the TripCollector class
        binderTripCollector.bindInstanceFields(this);

        // Create a headline
        Label headline = new Label("Fahrten anlegen / bearbeiten");
        headline.getStyle().set("font-size","16px").set("font-weight", "bold");

        // Set the entries in the Combo Boxes
        customers.setItems((customersList));
        customers.setItemLabelGenerator(Customers::getFahrgast);
        customers.addValueChangeListener(event ->setAutoValue());

        cars.setItems((carsList));
        cars.setItemLabelGenerator(Cars::getKennzeichen);

        employees.setItems((employeesList));
        employees.setItemLabelGenerator(Employees:: getPersonalID);

        bezahlung.setAllowCustomValue(true);
        bezahlung.setItems("bar", "privat RE", "Rechnung", "KTSs","KTSe", "BG","sonstiges");

        zeit.getStyle().set("margin-top","20px");

        // Enable drag-and-drop for the dialog
        setDraggable(true);
        add(headline,setDialogEntries(),createButtonsLayout());
    }


    //automatically places the phone and address entries in the fields when a customer is selected.
    private void setAutoValue() {
        // Get the currently selected customer from the 'customers' object.
        Customers selectedCustomer = customers.getValue();

        if (selectedCustomer != null) {
            // Get the name of the selected customer.
            String customerName = selectedCustomer.getFahrgast();

            // Check if the customer name is "Taxibetrieb".
            if ("Taxibetrieb".equals(customerName)) {
                // Set specific values for the fields when the customer is "Taxibetrieb".
                zielort.setValue("kein"); // Set 'zielort' to "kein".
                anmerkung.setValue("kein"); // Set 'anmerkung' to "kein".
                abholort.setValue("offen"); // Set 'abholort' to "offen".
                personen.setValue("offen"); // Set 'personen' to "offen".
                bezahlung.setValue("bar"); // Set 'bezahlung' to "bar".
            } else {
                // Set values from the selected customer for other fields.
                anmerkung.setValue(selectedCustomer.getTelefon());
                abholort.setValue(selectedCustomer.getAdresse());

                // Clear the values of fields that are not applicable.
                //zielort.clear();
                personen.clear();
                bezahlung.clear();
            }
        } else {
            // If no customer is selected, clear the 'anmerkung' and 'abholort' fields.
            anmerkung.clear();
            abholort.clear();
        }
    }



    // Create the form layout
    private FormLayout setDialogEntries(){
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

        save.addClickListener(event -> validateAndSaveTripCollector());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, tripCollector)));
        close.addClickListener(event -> close());


        binderTripCollector.addStatusChangeListener(e -> save.setEnabled(binderTripCollector.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    public void setTripCollectorEntries(TripCollector tripCollector) {
        String tempValue = actDate.format(month);

        this.tripCollector = tripCollector;
        binderTripCollector.readBean(tripCollector);

        if (zeit.getValue().equals(""))
            zeit.setValue(tempValue);
    }

    // Validates the input and saves the TripCollector entry when the save button is clicked.
    private void validateAndSaveTripCollector() {
        if (isValidDateFormat(zeit.getValue())) {
            try {
                binderTripCollector.writeBean(tripCollector);
                fireEvent(new SaveEvent(this, tripCollector));
            } catch (ValidationException e) {
                new Small_InfoDialog("Es ist ein Validierungsfehler aufgetreten: " + e.getMessage());
            }
        }else
            new Small_InfoDialog("Das Format entspricht nicht der erwarteten Schreibweise TT.MM.JJ, SS:MM");
    }

    /**
     * Checks if the provided time input has a valid date format (dd.MM.yy, hh:mm).
     *
     * @param timeInput The time input to validate.
     * @return True if the input has a valid date format, otherwise false.
     */
    private boolean isValidDateFormat(String timeInput) {
        // The expected date format
        String dateFormat = "dd.MM.yy, hh:mm";

        // Create a SimpleDateFormat object for the expected format.
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        //Versuche zu parsen. Wenn es klappt gebe true zurück, ansonsten false
        try {
            sdf.parse(timeInput);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Events TripCollector
    public static abstract class TripCollectorEvent extends ComponentEvent<TripDialog> {
        private final TripCollector tripCollector;

        protected TripCollectorEvent( TripDialog source, TripCollector tripCollector) {
            super(source, false);
            this.tripCollector = tripCollector;
        }

        public TripCollector getTripCollector() {
            return tripCollector;
        }
    }

    public static class SaveEvent extends TripDialog.TripCollectorEvent {
        SaveEvent(TripDialog source, TripCollector tripCollector) {
            super(source, tripCollector);
        }
    }


    public static class DeleteEvent extends TripDialog.TripCollectorEvent {

        DeleteEvent(TripDialog source, TripCollector tripCollector) {
            super(source, tripCollector);
        }

    }


    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
