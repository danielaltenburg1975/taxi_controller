package com.example.application.views.employees;


import com.example.application.data.Employees;
import com.example.application.dialogs.Small_InfoDialog;
import com.example.application.service.CrmService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

/**
 * This class creates the dialog for editing the employees dates
 *
 *  @author Daniel Altenburg
 *  @version 1.0
 *  @since 15.08.2023
 */

public class EmployeesDialog extends Dialog {
    private final CrmService service;
    private Employees employees;
    private final Binder<Employees> binder = new BeanValidationBinder<>(Employees.class);
    private final TextField personalID = new TextField("Personal_ID");
    private final TextField name = new TextField("Name");
    private final TextArea adresse = new TextArea("Adresse");
    private final TextField telefon = new TextField("Telefon");
    private final EmailField email = new EmailField("Email");
    private final TextField geburtstag = new TextField("Geburtsdatum");
    private final TextField ein_austritt = new TextField("Ein- Austritt");
    private final TextField krankenversicherung = new TextField("Krankenversicherung");
    private final TextField sozialversicherung = new TextField("Sozialversicherung");

    private final ComboBox<String> pensum = new ComboBox<>("Arbeitspensum");
    private final ComboBox<String> urlaubsanspruch = new ComboBox<>("Urlaubsanspruch");

    private final Button save = new Button("speichern");
    private final Button delete = new Button("löschen");
    private final Button close = new Button("abbrechen");


    public EmployeesDialog(CrmService service) {
      this.service = service;

      setHeight("70%");
      setDraggable(true);

      urlaubsanspruch.setItems("kein","22 Tage", "25 Tage", "28 Tage", "30Tage");
      urlaubsanspruch.setPlaceholder("Urlaubsanspruch");
      pensum.setItems("450€", "40%", "50%", "60%","70%", "80%","100%");
      pensum.setPlaceholder("Pensum");

      setHeight("100%");

      // Create a headline
      Label headline = new Label("Fahrer anlegen / bearbeiten");
      headline.getStyle().set("font-size","16px").set("font-weight", "bold");

      binder.bindInstanceFields(this); //set the entries in the fields
      add(headline, setDialogEntries(),createButtonsLayout());
    }

    // Creates a form and sets the fields.
    private FormLayout setDialogEntries(){
      FormLayout formLayout = new FormLayout();
      formLayout.setMaxWidth("450px");
      formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
              new FormLayout.ResponsiveStep("0px", 2));

      formLayout.add(personalID, name, adresse, telefon, email, geburtstag, ein_austritt,urlaubsanspruch,
              pensum, krankenversicherung, sozialversicherung);

      formLayout.setColspan(adresse, 2);
      return formLayout;
    }

    // Creates a layout and sets the buttons.
    private HorizontalLayout createButtonsLayout() {

      close.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","30px");
      delete.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","30px");
      save.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","30px");

      save.addClickShortcut(Key.ENTER);
      close.addClickShortcut(Key.ESCAPE);

      //set the action listeners
      save.addClickListener(event -> validateAndSave());
      delete.addClickListener(event -> fireEvent(new DeleteEvent(this, employees)));
      close.addClickListener(event -> close());

      binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

      return new HorizontalLayout(save, delete, close);
    }

    //Set blank fields for a new entry. Parameter is here a new Employees()
    public void setBlankEntries(Employees employees) {
      delete.setVisible(false);//the button is not needed
      this.employees = employees;
      binder.readBean(employees);
    }

    // Set the Entries.
    public void setEmployeeEntries(Employees employees) {
      this.employees = employees;
      binder.readBean(employees);
      if (employees != null) {
        hasDependencies(employees);
      }
    }

    // Validate and Save the Entries.
    private void validateAndSave() {
      try {
        binder.writeBean(employees);
        fireEvent(new SaveEvent(this, employees));
      } catch (ValidationException e) {
          new Small_InfoDialog("Es ist ein Validierungsfehler aufgetreten: " + e.getMessage());
      }
    }

    /**
     * Checks if the given Employees object has dependencies in the service.
     * If dependencies exist, it disables the delete button and reduces its opacity to indicate it cannot be deleted.
     *
     * @param employees The Employees object to check for dependencies.
     */
    private void hasDependencies(Employees employees) {
      int countDependenciesForEmployees = service.countDependenciesForEmployees(employees);
      if( countDependenciesForEmployees > 0){
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
      public static abstract class EmployeesFormEvent extends ComponentEvent<EmployeesDialog> {
        private final Employees employees;

        protected EmployeesFormEvent(EmployeesDialog source, Employees employees) {
          super(source, false);
          this.employees = employees;
        }

        public Employees getEmployees() {
          return employees;
        }
      }

      public static class SaveEvent extends EmployeesFormEvent {
        SaveEvent(EmployeesDialog source, Employees employees) {
          super(source, employees);
        }
      }

      public static class DeleteEvent extends EmployeesFormEvent {
        DeleteEvent(EmployeesDialog source, Employees employees) {
          super(source, employees);
        }

      }

      public static class CloseEvent extends EmployeesFormEvent {
        CloseEvent(EmployeesDialog source) {
          super(source, null);
        }
      }

      public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
              ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
      }
}