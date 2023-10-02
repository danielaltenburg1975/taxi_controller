package com.example.application.views;


import com.example.application.components.appnav.AppNav;
import com.example.application.components.appnav.AppNavItem;
import com.example.application.security.SecurityService;
import com.example.application.service.CrmService;
import com.example.application.views.cars.Cars_View;
import com.example.application.views.customers.Customer_View;
import com.example.application.views.employees.Employees_View;
import com.example.application.views.index.Index_View;
import com.example.application.views.newTrip.NewTrip_View;
import com.example.application.views.timeRecording.TimeRecording_View;
import com.example.application.views.time_account.Time_Account_View;
import com.example.application.views.tripCollector.TripCollector_View;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


/**
 * MainLayout is the top-level layout for the application, serving as a
 * container for navigation, header, and footer components. It manages the
 * user interface, including a refresh button, date display, and navigation menu.
 *
 */
public class MainLayout extends AppLayout {
    // Services and components needed for the layout
    private final CrmService service;
    private final Label currentDate = new Label();
    private final SecurityService securityService;
    private final Timer timer;
    private final UI ui;
    private final Footer layout = new Footer();
    private final Span newItems = new Span();

    /**
     * Constructor for MainLayout.
     *
     * @param securityService The security service for authentication and authorization.
     * @param service         The CRM service for data manipulation.
     */
    public MainLayout(SecurityService securityService, CrmService service) {
        this.securityService = securityService;
        this.service = service;
        timer = new Timer();

        // Layout configuration
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();

        ui = UI.getCurrent();


        // Start checking for new entries periodically
        startNewEntryCheck();
    }

    /**
     * Adds header content to the layout, including the application logo, current date, and logout button.
     */
    private void addHeaderContent() {
        Image logoSmall = new Image("/images/taxilogo_small.png", "Programmlogo");
        logoSmall.getStyle().set("margin-right", "250px");

        setCurrentDate();
        currentDate.getStyle().set("font-size", "15px").set("margin-right", "20px").set("font-weight", "bold");
        H2 viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        Button logout = new Button("Log out", e -> stopTimer());
        logout.getStyle().set("border", "1px solid gray").set("color", "black");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), viewTitle, logoSmall, currentDate, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(viewTitle);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    /**
     * Sets the current date in a specific format and updates the currentDate label.
     */
    public void setCurrentDate() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.GERMAN);
        String formattedDate = date.format(formatter);
        currentDate.setText(formattedDate);
    }

    /**
     * Adds content to the drawer, including the application name and navigation items.
     */
    private void addDrawerContent() {
        H1 appName = new H1("Taxi Controller");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());
        addToDrawer(header, scroller, createFooter());
    }

    /**
     * Creates the navigation menu for the application.
     *
     * @return AppNav containing navigation items.
     */
    private AppNav createNavigation() {
        AppNav nav = new AppNav();
        nav.setCollapsible(true);

        // Create AppNavItem for the "Fahrtenkorb" view
        AppNavItem newTripNavItem = new AppNavItem("Fahrtenkorb", NewTrip_View.class);

        // Create a Span to display the count of new entries
        newItems.getElement().getStyle().set("font-size", "12px").set("margin-right", "20px");
        newItems.getElement().setAttribute("slot", "suffix");

        // Add the Span to the AppNavItem
        newTripNavItem.getElement().appendChild(newItems.getElement());

        // Add navigation items to the navigation menu
        nav.addItem(new AppNavItem("Startseite", Index_View.class));
        nav.addItem(newTripNavItem);
        nav.addItem(new AppNavItem("Fahrten", TripCollector_View.class));
        nav.addItem(new AppNavItem("Fahrgäste", Customer_View.class));
        nav.addItem(new AppNavItem("Fuhrpark", Cars_View.class));
        nav.addItem(new AppNavItem("Fahrer", Employees_View.class));
        nav.addItem(new AppNavItem("Zeitkorb", TimeRecording_View.class));
        nav.addItem(new AppNavItem("Zeitkonto", Time_Account_View.class));

        return nav;
    }

    /**
     * Creates the footer of the layout containing a refresh icon and a label for new entries.
     *
     * @return Footer component.
     */
    private Footer createFooter() {
        Button refreshButton = new Button();
        refreshButton.getStyle().set("border", "1px solid gray").set("color", "black");

        Icon refreshIcon = new Icon(VaadinIcon.FILE_REFRESH);
        refreshButton.setIcon(refreshIcon);

        refreshButton.setText("Seite aktualisieren");
        refreshButton.addClickListener(e -> checkForNewEntries());
        layout.add(refreshButton);
        return layout;
    }

    /**
     * Starts a scheduled task to check for new entries periodically.
     */
    private void startNewEntryCheck() {
           timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!Thread.currentThread().isInterrupted()) {
                    checkForNewEntries();
                }
            }
        }, 0, TimeUnit.MINUTES.toMillis(5));
    }


    /**
     * Stops the timer thread .
     */
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            securityService.logout();
        }
    }

    /**
     * Checks for new entries in the CRM service and updates the new entry count.
     */
    private void checkForNewEntries() {
        if (!Thread.currentThread().isInterrupted() && !ui.isClosing()) {
            long unbookedTripsNumber = service.countNewTripWithCondition("NEIN");
            ui.access(() -> setNewEntryValue(unbookedTripsNumber));
        }
    }


    /**
     * Sets the value of the new entry count and updates the UI.
     *
     * @param unbookedTripsNumber The number of new entries.
     */
    private void setNewEntryValue(long unbookedTripsNumber) {
        String tempValue = unbookedTripsNumber > 0 ? Long.toString(unbookedTripsNumber) : "0";

        ui.access(() -> {
            newItems.setText(tempValue); // Update the value of the Span element
        });
    }
}
