package com.example.application.views.cars;

import com.example.application.data.Cars;
import com.example.application.dialogs.Small_InfoDialog;
import com.example.application.service.CrmService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

/**
 * The CarsDialog class represents a dialog window for creating and editing vehicle entries.
 * It uses Vaadin UI components for entering vehicle information and provides buttons for saving,
 * deleting, and canceling actions.
 *
 *  @author [Daniel Altenburg]
 *  @version 1.0
 *  @since [14.09.2023]
 *
 */

public class CarsDialog extends Dialog {

    private Cars cars;
    private final Binder<Cars> binder = new BeanValidationBinder<>(Cars.class);
    private final CrmService service;

    private final TextField automarke = new TextField("Automarke");
    private final TextField reifengroessen = new TextField("Reifengrößen");
    private final TextField kennzeichen = new TextField("Kennzeichen");
    private final TextField sitzplatz = new TextField("Sitzplatz");
    private final TextField taxometer = new TextField("Taxometer / Eichdatum");
    private final TextField hauptuntersuchung = new TextField("Hauptuntersuchung");
    private final TextField tankkarte = new TextField("Tankkarte / PIN");

    private final Button save = new Button("speichern");
    private final Button delete = new Button("löschen");
    private final Button close = new Button("abbrechen");

    /**
     * Constructor for the {@code CarsDialog} class.
     *
     * @param service The service for managing vehicle data.
     */
    public CarsDialog(CrmService service) {
        this.service = service;
        setHeight("80%");
        setDraggable(true);

        // Create a headline
        Label headline = new Label("Fahrzeug anlegen / bearbeiten");
        headline.getStyle().set("font-size","16px").set("font-weight", "bold");


        binder.bindInstanceFields(this); //set the entries in the fields
        add(headline, setDialogEntries(), createButtonsLayout());


    }
    // Creates a form and sets the fields.
    private FormLayout setDialogEntries(){
        FormLayout formLayout = new FormLayout();
        formLayout.setMaxWidth("350px");
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("0px", 2));

        formLayout.add(automarke, kennzeichen,reifengroessen,sitzplatz, taxometer, hauptuntersuchung, tankkarte);
        formLayout.setColspan(reifengroessen, 2);
        return formLayout;
    }

    //Creates a horizontal layout containing buttons for saving, deleting, and closing the dialog.
    private HorizontalLayout createButtonsLayout() {

        close.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","30px");
        delete.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","30px");
        save.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","30px");

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        // Add click listeners to buttons
        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, cars)));
        close.addClickListener(event -> close());

        // Enable the Save button based on the binder's validity
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    //Set blank fields for a new entry. Parameter is here a new Cars()
    public void setBlankEntries(Cars cars) {
        delete.setVisible(false);//the button is not needed
        this.cars = cars;
        binder.readBean(cars);
    }

    //Set the entries in the fields. Parameter is here the selected entry.
    public void setCarEntries(Cars cars) {
        this.cars = cars;
        binder.readBean(cars);

        if (cars != null) {
            hasDependencies(cars);
        }
    }

    // Validate and Save the Entries
    private void validateAndSave() {
        try {
            binder.writeBean(cars);
            fireEvent(new CarsDialog.SaveEvent(this, cars));
        } catch (ValidationException e) {
            new Small_InfoDialog("Es ist ein Validierungsfehler aufgetreten: " + e.getMessage());
        }
    }

    /**
     * Checks if the given Cars object has dependencies in the service.
     * If dependencies exist, it disables the delete button and reduces its opacity to indicate it cannot be deleted.
     *
     * @param cars The Cars object to check for dependencies.
     */
    private void hasDependencies(Cars cars) {
        int countDependenciesForCar = service.countDependenciesForCar(cars);
        if( countDependenciesForCar > 0){
            delete.setEnabled(false);
            delete.getStyle().set("opacity", "0.5");
        }
    }

    //activates the button and makes it visible
    public void activateDeleteButton(){
        delete.setVisible(true);
        delete.setEnabled(true);
        delete.getStyle().set("opacity", "1");
    }

    // Events
    public static abstract class CarsFormEvent extends ComponentEvent<CarsDialog> {
        private final Cars cars;

        protected CarsFormEvent(CarsDialog source, Cars cars) {
            super(source, false);
            this.cars = cars;
        }

        public Cars getCars() {
            return cars;
        }
    }

    public static class SaveEvent extends CarsDialog.CarsFormEvent {
        SaveEvent(CarsDialog source, Cars cars) {
            super(source, cars);
        }
    }

    public static class DeleteEvent extends CarsDialog.CarsFormEvent {
        DeleteEvent(CarsDialog source, Cars cars) {
            super(source, cars);
        }

    }


    public static class CloseEvent extends CarsDialog.CarsFormEvent {
        CloseEvent(CarsDialog source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
