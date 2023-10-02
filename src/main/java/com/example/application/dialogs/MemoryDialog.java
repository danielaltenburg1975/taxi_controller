package com.example.application.dialogs;


import com.example.application.data.Memories;
import com.example.application.service.CrmService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

import java.util.List;


/**
This class creates the dialog for editing the memory dates.
*  @author Daniel Altenburg
*  @version 1.0
*  @since 27.08.2023
*
*/

@Route("dialog-memory")
public class MemoryDialog extends Dialog  {
    private final TextArea textArea = new TextArea();
    private final Button save = new Button("speichern");
    private final Button delete = new Button("löschen");
    private final Button close = new Button("abbrechen");
    private final String currentDay;
    private final CrmService service;
    private List<Memories> memoryList;
    private Memories memories;


   public MemoryDialog(CrmService service,String currentDay){
       this.service = service;
       this.currentDay = currentDay;

       setDraggable(true);
       setCloseOnEsc(false);

       VerticalLayout verticalLayout = new VerticalLayout();

       textArea.setWidth("100%");
       Label headline = new Label("Erinnerungen hinzufügen / bearbeiten ");
       headline.getStyle().set("font-size","16px").set("font-weight", "bold").set("margin-bottom","20px");
       delete.setVisible(false);

       checkMemories();
       Label date = new Label();
       date.setText(currentDay);
       verticalLayout.add(headline, date, textArea);
       add(verticalLayout, createButtonsLayout());

   }

    // Creates the layout for buttons (Save, Delete, Close) in the dialog.
    private HorizontalLayout createButtonsLayout() {

        close.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","30px");
        delete.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","30px");
        save.getStyle().set("border","1px solid gray").set("color","black").set("margin-top","30px");

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        //set the action listeners
        save.addClickListener(event -> saveMemories());
        delete.addClickListener(event -> deleteMemories());
        close.addClickListener(event -> close());
        return new HorizontalLayout(save, delete, close);
    }

    // Checks if there are existing memories for the current day and updates the dialog accordingly.
    private void checkMemories(){

       memoryList = service.findMemories(currentDay);
        if (!memoryList.isEmpty()) {
            memories = memoryList.get(0);
            textArea.setValue(memories.getErinnerung());
            delete.setVisible(true);

        }else
            textArea.setPlaceholder("Es gibt für diesen Tag noch keinen Eintrag!");
    }

    // Saves or updates memories based on user input in the dialog.
    // If no text is entered, it displays an info dialog.
    private void saveMemories(){

        if (memoryList.isEmpty()) {
            Memories newMemories = new Memories();
            newMemories.setDatum(currentDay);
            newMemories.setErinnerung(textArea.getValue());

            if (!textArea.getValue().isEmpty()) {
                service.saveMemories(newMemories);
                close();
            }else {
                Small_InfoDialog smallInfoDialog = new Small_InfoDialog("Sie haben noch keine Notiz hinzugefügt!");
                smallInfoDialog.open();
            }
        }else {
            memories.setErinnerung(textArea.getValue());
             if (!textArea.getValue().isEmpty()) {
                service.saveMemories(memories);
                close();
            }else {
                Small_InfoDialog smallInfoDialog = new Small_InfoDialog("Es ist keine Notiz mehr vorhanden");
                smallInfoDialog.open();
            }
        }

    }

    // Deletes memories for the current day.
    private void deleteMemories(){
       service.deleteMemories(memories);
       close();
    }

}
