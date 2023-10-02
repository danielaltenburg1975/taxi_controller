package com.example.application.views.timeRecording;

import com.example.application.data.Time_Recording;
import com.example.application.dialogs.Small_InfoDialog;
import com.example.application.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PermitAll
@PageTitle("Zeitkorb")
@Route(value = "time_recording", layout = MainLayout.class)
public class TimeRecording_View extends VerticalLayout {


    Grid<Time_Recording> grid = new Grid<>(Time_Recording.class, false);
    private final TextField filterText = new TextField();
    private final CrmService service;
    private TimeRecordingDialog timeRecordingDialog;
    private TimeBookingDialog timeBookingDialog;
    private final Button newEntryButton = new Button("manuelle Buchung");
    private final Button bookingButton = new Button("Zeiten übernehmen");

    public TimeRecording_View(CrmService service) {
        this.service = service;
        newEntryButton.getStyle().set("border","1px solid gray").set("color","black");
        bookingButton.getStyle().set("border","1px solid gray").set("color","black");

        addClassName("list-view");
        setSizeFull();
        configureGrid();
        add(getToolbar(), getContent());
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);

        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(timeRecording -> timeRecording.getEmployees().getPersonalID()).setHeader("Fahrer_ID").setSortable(true);
        grid.addColumn(Time_Recording::getZeitbuchung).setHeader("Datum").setSortable(true);
        grid.addColumn(Time_Recording::getGebucht).setHeader("Gebucht").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

       updateList();


        grid.asSingleSelect().addValueChangeListener(event -> editTimeRecording(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Anzeige Filtern");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        newEntryButton.addClickListener(click ->newTimeRecordingDialog());
        bookingButton.addClickListener(click -> newTimeBookingDialog());


        HorizontalLayout toolbar = new HorizontalLayout(filterText, newEntryButton,bookingButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
    public void newTimeRecordingDialog(){
        timeRecordingDialog = new TimeRecordingDialog(service, new Time_Recording(),service.findAllEmployees(null),true);
        timerRecordingListener();
        timeRecordingDialog.open();
    }

    public void newTimeBookingDialog(){
        timeBookingDialog = new TimeBookingDialog(service);
        timeBookingListener();
        timeBookingDialog.open();
    }

    public void editTimeRecording(Time_Recording timeRecording) {
        if (timeRecording == null) {
            closeEditor();
        } else {
            //No booked entries can be edited
            if (grid.asSingleSelect().getValue().getGebucht().equals("NEIN")) {
                timeRecordingDialog = new TimeRecordingDialog(service,timeRecording,service.findAllEmployees(null),false);
                timeRecordingDialog.setBinderEntries(timeRecording);
                timeRecordingDialog.setTimeRecordingEntries(timeRecording,false);
                timerRecordingListener();
                timeRecordingDialog.open();
                addClassName("editing");
                grid.asSingleSelect().clear();
            }
            else {
                grid.asSingleSelect().clear();
                closeEditor();

                Small_InfoDialog noEditDialog = new Small_InfoDialog("Bereits gebuchte Einträge können nicht mehr bearbeitet werden!");
                noEditDialog.open();
            }
        }
    }
    private void timerRecordingListener(){
        timeRecordingDialog.addOpenedChangeListener(event -> {
            if (!event.isOpened()) {
                updateList(); }
        });
    }
    private void timeBookingListener(){
        timeBookingDialog.addOpenedChangeListener(event -> {
            if (!event.isOpened()) {
                updateList(); }
        });
    }

    private void updateList() {
        List<Time_Recording> recordings = service.findAllRecordings(filterText.getValue());

        // Sort the list by the "Booked" column so that "NO" comes first.
        recordings.sort((tr1, tr2) -> {
            if (tr1.getGebucht().equals("NEIN") && !tr2.getGebucht().equals("NEIN")) {
                return -1; // tr1 (NEIN) comes before tr2 (JA)
            } else if (!tr1.getGebucht().equals("NEIN") && tr2.getGebucht().equals("NEIN")) {
                return 1; // tr2 (NEIN) comes before (JA)
            } else {
                return 0; // tr1 und tr2 are same
            }
        });

        grid.setItems(recordings);
    }


    private void closeEditor() {
        removeClassName("editing");
    }

}
