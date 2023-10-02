package com.example.application.views.customers;


import com.example.application.data.Customers;
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
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

/**
* This class creates the dialog for editing the customer dates
*
*  @author Daniel Altenburg
*  @version 1.0
*  @since 30.08.2023
*/

public class CustomersDialog extends Dialog {

  private Customers customers;
  private final CrmService service;
  private final Binder<Customers> binder = new BeanValidationBinder<>(Customers.class);
    private final TextField fahrgast = new TextField("Fahrgast");
    private final TextField adresse = new TextField("Adresse");
    private final TextField telefon = new TextField("Telefon");
    private final EmailField email = new EmailField("Email");
    private final TextField kostentraeger = new TextField("Kostenträger");
    private final Button save = new Button("speichern");
    private final Button delete = new Button("löschen");
    private final Button close = new Button("abbrechen");

    public CustomersDialog(CrmService service) {
      this.service = service;

      setHeight("80%");
      setDraggable(true);

      // Create a headline
      Label headline = new Label("Fahrgäste anlegen / bearbeiten");
      headline.getStyle().set("font-size","16px").set("font-weight", "bold");

      binder.bindInstanceFields(this); //set the entries in the fields
      add( headline,setDialogEntries(), createButtonsLayout());
    }

    // Creates a form and sets the fields.
    private FormLayout setDialogEntries(){
      FormLayout formLayout = new FormLayout();
      formLayout.setMaxWidth("350px");
      formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
              new FormLayout.ResponsiveStep("0px", 1));

      formLayout.add(fahrgast,adresse,telefon,email,kostentraeger);
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
      delete.addClickListener(event -> fireEvent(new DeleteEvent(this, customers)));
      close.addClickListener(event -> close());

      binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

      return new HorizontalLayout(save, delete, close);
    }

  //Set blank fields for a new entry. Parameter is here a new Customer()
  public void setBlankEntries(Customers customers) {
    delete.setVisible(false);//the button is not needed
    this.customers = customers;
    binder.readBean(customers);
  }

    // Set the Entries.
    public void setCustomersEntries(Customers customers) {
      this.customers = customers;
      binder.readBean(customers);
      if (customers != null) {
          hasDependencies(customers);
      }
    }

    // Validate and Save the Entries.
    private void validateAndSave() {
      try {
        binder.writeBean(customers);
        fireEvent(new SaveEvent(this, customers));
      } catch (ValidationException e) {
          new Small_InfoDialog("Es ist ein Validierungsfehler aufgetreten: " + e.getMessage());
        }
    }

  /**
   * Checks if the given Customer object has dependencies in the service.
   * If dependencies exist, it disables the delete button and reduces its opacity to indicate it cannot be deleted.
   *
   * @param customers The Customers object to check for dependencies.
   */
    private void hasDependencies(Customers customers) {
      int countDependenciesForCustomers = service.countDependenciesForCustomers(customers);
      if( countDependenciesForCustomers > 0){
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
      public static abstract class CustomersFormEvent extends ComponentEvent<CustomersDialog> {
        private final Customers customers;

        protected CustomersFormEvent(CustomersDialog source, Customers customers) {
          super(source, false);
          this.customers = customers;
        }

        public Customers getCustomers() {
          return customers;
        }
      }

      public static class SaveEvent extends CustomersFormEvent {
        SaveEvent(CustomersDialog source, Customers customers) {
          super(source, customers);
        }
      }

      public static class DeleteEvent extends CustomersFormEvent {
        DeleteEvent(CustomersDialog source, Customers customers) {
          super(source, customers);
        }

      }


      public static class CloseEvent extends CustomersFormEvent {
        CloseEvent(CustomersDialog source) {
          super(source, null);
        }
      }

      public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
              ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
      }
}