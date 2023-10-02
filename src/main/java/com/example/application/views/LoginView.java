package com.example.application.views;

import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Taxi / Controller")
@AnonymousAllowed
public class LoginView extends HorizontalLayout implements BeforeEnterObserver {

	private final LoginForm login = new LoginForm();

	public LoginView() {
		VerticalLayout verticalLayout = new VerticalLayout();


		verticalLayout.getStyle().set("background-color","white").set("box-shadow", "0px 0px 10px rgba(0, 0, 0, 0.5)");
		verticalLayout.setWidth("380px");
		verticalLayout.setHeight("100%");

		Image logoSmall = new Image("/images/taxilogo_small.png","Programmlogo");

		// Login information for the demo version
		H4 demoInfo = new H4("Demoversion!");
		Label demoLogIn = new Label("Benutzername: benutzer  /  Passwort: zufall1234");
		demoInfo.getStyle().set("margin-left", "105px");
		demoLogIn.getStyle().set("font-size", "14px").set("margin-left", "25px");


		addClassName("login-view");

		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		login.setAction("login");


		LoginI18n i18n = createGermanI18n();
		login.setI18n(i18n);

		// Add components to the vertical layout
		verticalLayout.add(demoInfo,logoSmall, demoLogIn, login);
		add(verticalLayout);

	}
	// Inform the user about an authentication error
	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		// inform the user about an authentication error
		if (beforeEnterEvent.getLocation()
				.getQueryParameters()
				.getParameters()
				.containsKey("error")) {
			login.setError(true);
		}
	}
	// Create and configure the internationalization properties for the login form
	private LoginI18n createGermanI18n() {
		LoginI18n i18n = LoginI18n.createDefault();
		i18n.setHeader(new LoginI18n.Header());
		i18n.getForm().setTitle("Anmelden");
		i18n.getForm().setUsername("Benutzername");
		i18n.getForm().setPassword("Passwort");
		i18n.getForm().setSubmit("Anmelden");
		i18n.getForm().setForgotPassword("Anmeldedaten vergessen?");
		i18n.getErrorMessage().setTitle("Falscher Benutzername oder falsches Passwort");
		i18n.getErrorMessage().setMessage("Überprüfen Sie, ob der Benutzername und das Passwort korrekt sind, und versuchen Sie es erneut.");

		return i18n;
	}
}
