package com.example.application.service;

import com.example.application.data.*;
import com.example.application.dialogs.Small_InfoDialog;
import com.example.application.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* The CrmService class provides business logic and operations for managing CRM-related entities.
* It serves as a service layer that interacts with various repositories to perform CRUD operations
* and other data-related tasks for employees, trip collectors, trips, cars, customers, time recordings,
* and time accounts within the CRM application.
*
* @author Daniel Altenburg
* @version 1.0
* @since 15.09.2023
*/

@Service
public class CrmService {

    private final EmployeesRepository employeesRepository;
    private final TripCollectorRepository tripCollectorRepository;
    private final New_TripRepository newTripRepository;
    private final FindNewTripRepository findNewTripRepository;
    private final Time_RecordingRepository timeRecordingRepository;
    private final CarsRepository carsRepository;
    private final CustomersRepository customersRepository;
    private final Time_Account_Repository timeAccountRepository;
    private final MemoriesRepository memoriesRepository;


    //Constructor for the CrmService class.
    public CrmService(EmployeesRepository employeesRepository, TripCollectorRepository tripCollectorRepository,
                      Time_RecordingRepository timeRecordingRepository, CarsRepository carsRepository,
                      CustomersRepository customersRepository,New_TripRepository newTripRepository,
                      FindNewTripRepository findNewTripRepository,
                      Time_Account_Repository timeAccountRepository,
                      MemoriesRepository memoriesRepository){

        // Initialize class fields with the provided repository instances.
        this.employeesRepository = employeesRepository;
        this.tripCollectorRepository = tripCollectorRepository;
        this.timeRecordingRepository = timeRecordingRepository;
        this.carsRepository = carsRepository;
        this.customersRepository = customersRepository;
        this.newTripRepository = newTripRepository;
        this.findNewTripRepository = findNewTripRepository;
        this.timeAccountRepository = timeAccountRepository;
        this.memoriesRepository = memoriesRepository;
    }

    public List<Employees> findAllEmployees(String stringFilter) {

        if (stringFilter == null || stringFilter.isEmpty()) {
            return (List<Employees>) employeesRepository.findAll();
        } else {
            return employeesRepository.search(stringFilter);
        }
    }
    public List<TripCollector> findAllTripCollectors(String stringFilter) {

        if (stringFilter == null || stringFilter.isEmpty()) {
            return (List<TripCollector>) tripCollectorRepository.findAll();
        } else {
            return tripCollectorRepository.search(stringFilter);
        }
    }
    public List<New_Trip> findAllNewTrips(String stringFilter) {

        if (stringFilter == null || stringFilter.isEmpty()) {
            return (List<New_Trip>) newTripRepository.findAll();
        } else {
            return newTripRepository.search(stringFilter);
        }
    }
    public List<Cars> findAllCars(String stringFilter) {

        if (stringFilter == null || stringFilter.isEmpty()) {
            return (List<Cars>) carsRepository.findAll();
        } else {
            return carsRepository.search(stringFilter);
        }
    }
    public List<Customers> findAllCustomers(String stringFilter) {

        if (stringFilter == null || stringFilter.isEmpty()) {
            return (List<Customers>) customersRepository.findAll();
        } else {
            return customersRepository.search(stringFilter);
        }
    }
    public List<Time_Recording> findAllRecordings(String stringFilter) {

        if (stringFilter == null || stringFilter.isEmpty()) {
            return (List<Time_Recording>) timeRecordingRepository.findAll();
        } else {
            return timeRecordingRepository.search((stringFilter));

        }
    }

    public List<Time_Account> findAllTime_Accounts(String employeeID, String year) {
        List<Time_Account> timeAccounts = new ArrayList<>();

        if (employeeID != null && !employeeID.isEmpty()) {
            timeAccounts = timeAccountRepository.searchByPersonalIdAndDate(employeeID,year);
        }

        if (timeAccounts.isEmpty()) {
            Small_InfoDialog smallInfoDialog = new Small_InfoDialog("Es wurde kein Ergebnis gefunden.");
            smallInfoDialog.open();
        }

        return timeAccounts;
    }

    public List<Memories> findMemories(String stringFilter) {

        if (stringFilter == null || stringFilter.isEmpty()) {
            return (List<Memories>) memoriesRepository.findAll();
        } else {
            return memoriesRepository.search(stringFilter);
        }
    }

    public void deleteEmployee(Employees employees) {employeesRepository.delete(employees); }
    public void deleteTripCollector(TripCollector tripCollector) {tripCollectorRepository.delete(tripCollector); }
    public void deleteNewTrips(New_Trip newTrip) {newTripRepository.delete(newTrip); }
    public void deleteCars(Cars cars) {carsRepository.delete(cars); }
    public void deleteTimeRecording(Time_Recording timeRecording) {timeRecordingRepository.delete(timeRecording);}
    public void deleteCustomers(Customers customers) {customersRepository.delete(customers);}
    public void deleteMemories(Memories memories) {memoriesRepository.delete(memories);}

    public void saveEmployee(Employees employees) {
        if (employees == null) {
            System.err.println("Employee is null. Are you sure you have connected your form to the application?");
            return;
        }
        employeesRepository.save(employees);
    }

    public void saveTripCollector(TripCollector tripCollector) {
        if (tripCollector == null) {
            System.err.println("TripCollector is null. Are you sure you have connected your form to the application?");
            return;
        }
        tripCollectorRepository.save(tripCollector);
    }

    public void saveNew_Trip(New_Trip newTrip) {
        if (newTrip == null) {
            System.err.println("Time_Recording is null. Are you sure you have connected your form to the application?");
            return;
        }
        newTripRepository.save(newTrip);
    }

    public void saveTimeRecording(Time_Recording timeRecording) {
        if (timeRecording == null) {
            System.err.println("Time_Recording is null. Are you sure you have connected your form to the application?");
            return;
        }
        timeRecordingRepository.save(timeRecording);
    }
    public void saveCars(Cars cars) {
        if (cars == null) {
            System.err.println("Cars is null. Are you sure you have connected your form to the application?");
            return;
        }
        carsRepository.save(cars);
    }
    public void saveCustomers(Customers customers) {
        if (customers == null) {
            System.err.println("Customers is null. Are you sure you have connected your form to the application?");
            return;
        }
        customersRepository.save(customers);
    }
    public void saveTime_Account(Time_Account timeAccount) {
        if (timeAccount == null) {
            System.err.println("Employee is null. Are you sure you have connected your form to the application?");
            return;
        }
        timeAccountRepository.save(timeAccount);
    }

    public void saveMemories(Memories memories) {
        if (memories == null) {
            System.err.println("Employee is null. Are you sure you have connected your form to the application?");
            return;
        }
        memoriesRepository.save(memories);
    }

    //search new trips that are not booked
    public Long countNewTripWithCondition(String gebucht) {

        long entryCount;
        entryCount = findNewTripRepository.countByCondition(gebucht);

        return entryCount;
    }

    //Counts the number of dependencies associated with a specific car.
    public int countDependenciesForCar(Cars cars) {
        //Returns the count of dependencies for the specified car.
        return carsRepository.countCarDependencies(cars);
    }
    //Counts the number of dependencies associated with a specific Customers.
    public int countDependenciesForCustomers(Customers customers) {
        //Get the count of dependencies for the specified customers.
        int tempValue1 = customersRepository.countCustomersToTripCollectorDependencies(customers);
        int tempValue2 = customersRepository.countCustomersToNew_TripDependencies(customers);
        return tempValue1+tempValue2;
    }

    //Counts the number of dependencies associated with a specific Employees.
    public int countDependenciesForEmployees(Employees employees) {
        //Get the count of dependencies for the specified employees.
        int tempValue1 = employeesRepository.countEmployeesToTripCollectorDependencies(employees);
        int tempValue2 = employeesRepository.countEmployeesToNew_TripDependencies(employees);
        return tempValue1+tempValue2;
    }


}
