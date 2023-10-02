package com.example.application.views.time_account;


import com.example.application.data.Employees;
import com.example.application.data.Time_Account;
import com.example.application.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
/**

This class compiles the collected data of the employee.
Data from the database tables Employees and Time_Account are retrieved and made available.
The processing of the data is not provided here.
*
*  @author Daniel Altenburg
*  @version 1.0
*  @since 19.09.2023
*
*/

@PermitAll
@PageTitle("Zeitkonto")
@Route(value = "time_account", layout = MainLayout.class)
public class Time_Account_View extends VerticalLayout {

    Grid<Time_Account> grid = new Grid<>(Time_Account.class, false);
    private final ComboBox <Employees> employeesComboBox = new ComboBox<>("Fahrer_ID");
    private List<Employees> employeesList;
    private final Button backButton = new Button("<<");
    private final Button nextButton = new Button(">>");
    private final Button searchButton = new Button("suchen");
    private final Label searchedYear = new Label();
    private final CrmService service;
    private final Label name = new Label();
    private final Label address = new Label();
    private final Label birthday = new Label();
    private final Label entryExit = new Label();
    private final Label pensum = new Label();
    private final Label vacationEntitlement = new Label();
    private final Label remainingVacation = new Label();
    private final Label sickDays = new Label();
    private String total = "";
    private String valuePensum;
    private String txtVacationEntitlement;
    private double countSpecialDays=0;
    private double countVacation;
    private int countSickDays = 0;
    private int yearValue;


    public Time_Account_View(CrmService service) {
        this.service = service;

        yearValue = Calendar.getInstance().get(Calendar.YEAR) % 100;
        searchedYear.setText("20"+yearValue);
        searchedYear.getStyle().set("margin-top", "10px");

        nextButton.getStyle().set("color", "black").set("background","transparent");
        backButton.getStyle().set("color", "black").set("background","transparent");
        nextButton.addClickListener(click -> setYearValue("plus"));
        backButton.addClickListener(click -> setYearValue("minus"));

        searchButton.getStyle().set("border", "1px solid gray").set("color", "black").set("margin-left","100%");
        searchButton.addClickListener(e -> updateList());
        searchButton.addClickShortcut(Key.ENTER);



        setSizeFull();
        configureGrid();
        employeesList = service.findAllEmployees(null);

        employeesComboBox.setItems((employeesList));
        employeesComboBox.setItemLabelGenerator(Employees:: getPersonalID);



        name.getStyle().set("font-weight", "bold");
        address.getStyle().set("font-weight", "bold").set("margin-left","20px");
        birthday.getStyle().set("font-weight", "bold").set("margin-left","450px");
        entryExit.getStyle().set("font-size", "14px");
        pensum.getStyle().set("font-size", "14px").set("margin-left","50px");
        vacationEntitlement.getStyle().set("font-size", "14px").set("margin-left","50px");
        remainingVacation.getStyle().set("font-size", "14px").set("margin-left","50px");
        sickDays.getStyle().set("font-size", "14px").set("margin-left","50px");

        add(getToolbar(),getNameAndAddress(),getEmployeeData(), getContent());
    }

    //set the main heading
    private HorizontalLayout getNameAndAddress(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(name, address, birthday);
        horizontalLayout.getStyle().set("margin-top", "20px");
        return horizontalLayout;
    }

    //set the employee information
    private HorizontalLayout getEmployeeData(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.getStyle().set("margin-top", "10px");
        horizontalLayout.add(entryExit, pensum, vacationEntitlement,remainingVacation, sickDays);
        return horizontalLayout;
    }

    //set the grid
    private Component getContent() {

        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }
    private void configureGrid() {
        grid.getStyle().set("grid-template-rows", "80px");

        grid.addColumn(Time_Account::getDatum).setHeader("Monat");
        grid.addComponentColumn(item -> {
            //get the day entries
            return setDayValue(item.getTagesZeit()); }).setHeader(setHeaderForDayValues());

        grid.addColumn(item -> total).setHeader("Total");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

    }

    //create the column header with the numbers 1. to 31.
    private HorizontalLayout setHeaderForDayValues(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        for(int i = 0; i<31; i++){
            String tempValue = i + 1 +".";
            Label headerLabel= new Label(tempValue);

            //the distances must be adjusted
            if (i<9)
                headerLabel.getStyle().set("margin-left","5.2px");
            else if (i ==9)
                headerLabel.getStyle().set("margin-left","0px");
            else
                headerLabel.getStyle().set("margin-left","-2.4px");

            horizontalLayout.add(headerLabel);}
        return horizontalLayout;
    }

    //sets the database entries for the times in the field. The data must be split first
    private HorizontalLayout setDayValue( String tempValue ){

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        String[] split = tempValue.split(" ");
        calculateTotalTime(split);

        for(int i = 0; i<split.length; i++){
            Label containerText = new Label(split[i]);
            Div container = new Div();
            //for a better overview every second value should have a different background
            if (i % 2 == 0)
                container.getStyle().set("display", "flex").set("justify-content", "center").set("align-items", "center")
                                .set("background-color", "#c1c6ce").set("margin-left","-4px").set("margin-right","-4px");
            else
                container.getStyle().set("display", "flex").set("justify-content", "center").set("align-items", "center")
                        .set("background-color", "#dadbdd").set("margin-left","-4px").set("margin-right","-4px");

            container.setMinHeight("25px");
            container.setMinWidth("25px");

            containerText.getStyle().set("font-size","12px");
            container.add(containerText);
            horizontalLayout.add(container);

        }
        return horizontalLayout;
    }
    //for the search of employees id
    private HorizontalLayout getToolbar() {
        HorizontalLayout yearHandler = new HorizontalLayout();
        yearHandler.getStyle().set("margin-top","35px").set("margin-left","45%");
        yearHandler.add( backButton, searchedYear,nextButton, searchButton);


        HorizontalLayout toolbar = new HorizontalLayout(employeesComboBox,yearHandler);

        return toolbar;
    }

    //assigns the employee information to the label
    private void setHeaderData(String employeesID){
        List<Employees> employees = service.findAllEmployees(employeesID);
        if (!employees.isEmpty()) {
            Employees foundEmployee = employees.get(0);
            String tempName = foundEmployee.getName();
            String tempAddress = foundEmployee.getAdresse();
            String tempBirthday = foundEmployee.getGeburtstag();
            String tempEntryExit = foundEmployee.getEin_austritt();
            valuePensum = foundEmployee.getPensum();
            txtVacationEntitlement = foundEmployee.getUrlaubsanspruch();

            name.setText(tempName);
            address.setText(tempAddress);
            birthday.setText("Geburtsdatum: "+tempBirthday);
            entryExit.setText("Ein-Austritt: "+tempEntryExit);
            pensum.setText("Arbeitspensum: "+valuePensum);
            vacationEntitlement.setText("Urlaubsanspruch: "+txtVacationEntitlement +" Tage");
        }
    }

    //determines the total times from the times of day
    private void calculateTotalTime(String[]tempValue){
        double countTime = 0;

        for (String s : tempValue) {
            if (s.equals("K") || s.equals("U") || s.equals("U/2")|| s.equals("U/S")) {
                checkSpecialDays(s);
                countTime = countTime + countSpecialDays;
            } else {
                double tempTime = Double.parseDouble(s);
                countTime = tempTime + countTime;
            }
        }
        double roundCountTime = Math.round(countTime* 100.0) / 100.0;
        total = Double.toString(roundCountTime);
    }

    //determines the values from the special days
    private void checkSpecialDays(String tempValue){
        if (!valuePensum.equals("450€")) {//first the workload must be determined
            String zahlString = valuePensum.replaceAll("%", "");
            double tempPensum = Double.parseDouble(zahlString) / 100;


            if (tempValue.equals("U") || tempValue.equals("U/S")) {
                countSpecialDays = 8 * tempPensum;
                if (tempValue.equals("U")) {
                    countVacation++;
                    setRemainingVacation();
                }
            }
            if (tempValue.equals("U/2")) {
                countSpecialDays = (8 * tempPensum) / 2;
                countVacation = countVacation + 0.5;
                setRemainingVacation();
            }
            if (tempValue.equals("K")) {
                countSpecialDays = 8 * tempPensum;
                countSickDays++;
                setSickDays();
            }
        }
    }

    //determines the remaining vacation days
    private void setRemainingVacation(){
        double tempVacationValue = Double.parseDouble(txtVacationEntitlement);
        String tempValue = Double.toString(tempVacationValue-countVacation);
        remainingVacation.setText("Resturlaub: "+tempValue+" Tage");

    }

    // count the sick days
    private void setSickDays(){
        sickDays.setText("Krankheitstage: "+ countSickDays +" Tage");

    }
    private void setYearValue(String backOrNext){

        if (backOrNext.equals("minus") && yearValue>10 )
            yearValue--;

        if (backOrNext.equals("plus") && yearValue<98 )
            yearValue++;

            searchedYear.setText("20"+yearValue);
    }

    private void updateList() {

        Employees selectedEmployee = employeesComboBox.getValue();
        String selectedEmployeeId = (selectedEmployee != null) ? selectedEmployee.getPersonalID() : "";

        // Get data from the service.
        List<Time_Account> data = service.findAllTime_Accounts(selectedEmployeeId, Integer.toString(yearValue));

        // Sort the list by the date column.
        Collections.sort(data, (item1, item2) -> {
            String dateStr1 = item1.getDatum(); // MM.YY format
            String dateStr2 = item2.getDatum(); // MM.YY format

            // Extracts the month part from the date strings.
            int month1 = Integer.parseInt(dateStr1.split("\\.")[0]);
            int month2 = Integer.parseInt(dateStr2.split("\\.")[0]);

            // Vergleichen Sie die Monate
            return Integer.compare(month1, month2);
        });

        // Add data to the table.
        grid.setItems(data);

        if (!grid.getColumns().isEmpty())
            setHeaderData(selectedEmployeeId);

    }
}
