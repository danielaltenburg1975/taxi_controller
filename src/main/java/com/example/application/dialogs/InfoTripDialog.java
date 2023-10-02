package com.example.application.dialogs;

import com.example.application.data.TripCollector;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;

@Route("dialog-infoTrip")
public class InfoTripDialog extends Dialog {


    private Label headline = new Label("Vollständige Übersicht");
    private Label txtZeit = new Label("Zeit:");
    private Label zeit = new Label();
    private Label txtCustomer = new Label("Fahrgast:");
    private Label customers = new Label();
    private Label txtAbholort = new Label("Abholort:");
    private Label abholort = new Label();
    private Label txtZielort = new Label("Zielort:");
    private Label zielort = new Label();
    private Label txtAnmerkung  = new Label("Telefon / Anmerkung:");
    private Label anmerkung = new Label();
    private Label txtPersonen = new Label("Personen:");
    private Label personen = new Label();
    private Label txtCars = new Label("Auto:");
    private Label cars = new Label();
    private Label txtEmployees = new Label("Fahrer:");
    private Label employees = new Label();
    private Label txtBezahlung = new Label("Bezahlung:");
    private Label bezahlung = new Label();

    Button close = new Button("schließen");

    public InfoTripDialog(TripCollector tripCollector){


        headline.getStyle().set("font-size","16px").set("font-weight", "bold").set("margin-bottom","40px");
        txtZeit.getStyle().set("font-weight", "bold");
        txtCustomer.getStyle().set("font-weight", "bold");
        txtAbholort.getStyle().set("font-weight", "bold");
        txtZielort.getStyle().set("font-weight", "bold");
        txtAnmerkung.getStyle().set("font-weight", "bold");
        txtPersonen.getStyle().set("font-weight", "bold");
        txtCars.getStyle().set("font-weight", "bold");
        txtEmployees.getStyle().set("font-weight", "bold");
        txtBezahlung.getStyle().set("font-weight", "bold");

        zeit.setText(tripCollector.getZeit());
        customers.setText(tripCollector.getCustomers().getFahrgast());
        abholort.setText(tripCollector.getAbholort());
        zielort.setText(tripCollector.getZielort());
        anmerkung.setText(tripCollector.getAnmerkung());
        personen.setText(tripCollector.getPersonen());
        cars.setText(tripCollector.getCars().getKennzeichen());
        employees.setText(tripCollector.getEmployees().getPersonalID());
        bezahlung.setText(tripCollector.getBezahlung());

        close.addClickShortcut(Key.ESCAPE);
        close.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","40px");
        close.addClickListener(event -> closeDialog());

        add(setAllTripText());

    }
    private void closeDialog(){
        close();
    }
    private FormLayout setAllTripText(){
        FormLayout formLayout = new FormLayout();
        formLayout.setMaxWidth("400px");
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("0px", 2));
        formLayout.setColspan(headline, 2);


        formLayout.add(headline,txtZeit,zeit,txtCustomer,customers,txtAbholort,abholort,txtZielort,
                       zielort,txtAnmerkung,anmerkung,txtPersonen,personen,txtCars,cars,txtEmployees,
                       employees,txtBezahlung,bezahlung,new Label(""),close);

        return formLayout;

    }

}
