package com.example.application.views.newTrip;

import com.example.application.data.New_Trip;
import com.example.application.service.CrmService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
/**
 * The dialog allows a multi selection to delete several entries. Only booked entries should be deleted here.
 *
 *  @author Daniel Altenburg
 *  @version 1.0
 *  @since 27.08.2023
 */

@Route("dialog-DeleteTrips")
public class TripDeleteDialog extends Dialog {
    Grid<New_Trip> grid = new Grid<>(New_Trip.class, false);
    private final CrmService service;
    private final Button delete = new Button("löschen");
    private final Button close = new Button("abbrechen");


    public TripDeleteDialog(CrmService service){
        VerticalLayout verticalLayout = new VerticalLayout();
        this.service = service;
        setWidth("600px");
        setHeight("80%");

        Label infoTxt = new Label("Hinweis: Es können hier nur gebuchte Einträge gelöscht werden!");
        infoTxt.getStyle().set("font-size","12px");

        setDraggable(true);
        setCloseOnEsc(false);

        configureGrid();
        Label headline = new Label("Zeiten übernehmen");
        verticalLayout.add(headline,infoTxt,grid,createButtonsLayout());
        add (verticalLayout);
        updateList();

        headline.getStyle().set("font-size","16px").set("font-weight", "bold").set("margin-bottom","15px");

    }

    private void configureGrid() {
        grid.setMaxHeight("300px");
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(New_Trip::getZeit).setHeader("Zeit").setSortable(true);
        grid.addColumn(newTrip -> newTrip.getCustomers().getFahrgast()).setHeader("Fahrgast").setWidth("100px").setSortable(true);

        grid.addColumn(New_Trip::getGebucht).setHeader("gebucht");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

    }
    // Create a layout for buttons
    private HorizontalLayout createButtonsLayout() {
        delete.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);
        delete.getStyle().set("border", "1px solid gray").set("color", "black").set("margin-top", "35px");
        close.getStyle().set("border", "1px solid gray").set("color", "black").set("margin-top", "35px");


        delete.addClickListener(event -> deleteSelectedEntries());
        close.addClickListener(event -> close());

        return new HorizontalLayout(delete, close);
    }

    // Delete selected entries
    public void deleteSelectedEntries() {
        List<New_Trip> selectedItems = new ArrayList<>(grid.getSelectedItems());

        // Check if anything is selected
        if (!selectedItems.isEmpty()) {
            // Delete the selected items here
            for (New_Trip item : selectedItems) {
                service.deleteNewTrips(item);
            }
            // Clear selected items after deletion
            selectedItems.clear();

            close();
        }
        updateList();
    }

    private void updateList() {
        grid.setItems(service.findAllNewTrips("JA"));
    }



}
