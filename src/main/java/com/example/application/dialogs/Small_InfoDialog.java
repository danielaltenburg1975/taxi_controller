package com.example.application.dialogs;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route("dialog-NoEdit")
public class Small_InfoDialog extends Dialog  {
private String currentText;

    public Small_InfoDialog(String currentText){
        this.currentText = currentText;

        HorizontalLayout dialogLayout = createDialogLayout();
        add(dialogLayout);

    }


    private  HorizontalLayout createDialogLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();


        Label infoTextLabel = new Label();
        infoTextLabel.setText(currentText);

        Button closeButton = new Button(new Icon("lumo", "cross"), e -> close());
        closeButton.addClickShortcut(Key.ENTER);
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getStyle().set("border","1px solid gray").set("color","black");
        horizontalLayout.add(infoTextLabel,closeButton);
        return horizontalLayout;
    }



}
