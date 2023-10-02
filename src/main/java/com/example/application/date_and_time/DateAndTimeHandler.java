package com.example.application.date_and_time;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
* The DateAndTimeHandler class provides methods for working with dates and times, including checking for holidays and weekends,
* counting the number of non-holiday and non-weekend days between two dates, and retrieving information about each day in a range.
*
* It uses the HolidaysBW class to calculate holidays in Baden-Württemberg, Germany.
*
*  @author Daniel Altenburg
*  @version 1.0
*  @since 20.09.2023
*
*/


public class DateAndTimeHandler {
    private final List<String> dayInformation= new ArrayList<>();

    //check the passed date if it is a holiday
    public boolean checkHolidays(LocalDate tempDate) {
        int tempYear = tempDate.getYear();
        HolidaysBW holidaysBW = new HolidaysBW();
        List<HolidaysBW.GetHoliday> holidays = holidaysBW.berechneFeiertage(tempYear);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");

        boolean isHoliday = false;
        for (HolidaysBW.GetHoliday holiday : holidays) {
            LocalDate holidayDate = holiday.getDatum();
            formatter.format(holidayDate);


            if (tempDate.isEqual(holidayDate)) {
                isHoliday = true;
                break;
            }else {
                isHoliday = false;

            }
        }
        return isHoliday;

    }

    //check the passed date if it is a weekend
    public boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public long countHolidayAndWeekend(LocalDate tempStartDate, LocalDate tempEndDate){
        long differenceInDays = 0;
        long holidaysOrWeekends = 0;
        LocalDate currentDate = tempStartDate;

        if (tempStartDate!=null && tempEndDate!=null){
            differenceInDays = ChronoUnit.DAYS.between(tempStartDate, tempEndDate);
        }

        while (currentDate != null && tempEndDate != null && !currentDate.isAfter(tempEndDate)) {
            if (checkHolidays(currentDate)) {
                dayInformation.add("H");
                holidaysOrWeekends++;
            }
            if (isWeekend(currentDate)) {
                dayInformation.add("W");
                holidaysOrWeekends++;
            } else {
                dayInformation.add("N");
            }
            currentDate = currentDate.plusDays(1);
        }
        return differenceInDays - holidaysOrWeekends;
    }
    public List<String> getDayInformation(){
        return dayInformation;
    }


}
