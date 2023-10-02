package com.example.application.date_and_time;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HolidaysBW {

/**
Here the holidays for Baden Würtemberg are determined according to the Meeus/Jones/Butcher algorithm.
*
*  @author Daniel Altenburg
*
*  @since 27.08.2023
*
*/
    public List<GetHoliday> berechneFeiertage(int jahr) {
        List<GetHoliday> holidays = new ArrayList<>();

        // Neujahr
        LocalDate neujahr = LocalDate.of(jahr, 1, 1);
        holidays.add(new GetHoliday("Neujahr", neujahr));

        // Heilige Drei Könige
        LocalDate heiligeDreiKoenige = LocalDate.of(jahr, 1, 6);
        holidays.add(new GetHoliday("Heilige Drei Könige", heiligeDreiKoenige));

        // Karfreitag
        LocalDate karfreitag = getEasterSunnday(jahr).minusDays(2);
        holidays.add(new GetHoliday("Karfreitag", karfreitag));

        // Ostermontag
        LocalDate ostermontag = getEasterSunnday(jahr).plusDays(1);
        holidays.add(new GetHoliday("Ostermontag", ostermontag));

        // Tag der Arbeit
        LocalDate tagDerArbeit = LocalDate.of(jahr, 5, 1);
        holidays.add(new GetHoliday("Tag der Arbeit", tagDerArbeit));

        // Christi Himmelfahrt
        LocalDate christiHimmelfahrt = getEasterSunnday(jahr).plusDays(39);
        holidays.add(new GetHoliday("Christi Himmelfahrt", christiHimmelfahrt));

        // Pfingstmontag
        LocalDate pfingstmontag = getEasterSunnday(jahr).plusDays(50);
        holidays.add(new GetHoliday("Pfingstmontag", pfingstmontag));

        // Fronleichnam
        LocalDate fronleichnam = getEasterSunnday(jahr).plusDays(60);
        holidays.add(new GetHoliday("Fronleichnam", fronleichnam));

        // Tag der Deutschen Einheit
        LocalDate tagDerDeutschenEinheit = LocalDate.of(jahr, 10, 3);
        holidays.add(new GetHoliday("Tag der Deutschen Einheit", tagDerDeutschenEinheit));

        // Allerheiligen
        LocalDate allerheiligen = LocalDate.of(jahr, 11, 1);
        holidays.add(new GetHoliday("Allerheiligen", allerheiligen));

        // Erster Weihnachtstag
        LocalDate ersterWeihnachtstag = LocalDate.of(jahr, 12, 25);
        holidays.add(new GetHoliday("Erster Weihnachtstag", ersterWeihnachtstag));

        // Zweiter Weihnachtstag
        LocalDate zweiterWeihnachtstag = LocalDate.of(jahr, 12, 26);
        holidays.add(new GetHoliday("Zweiter Weihnachtstag", zweiterWeihnachtstag));

        return holidays;
    }

    private LocalDate getEasterSunnday(int year) {
        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int n = (h + l - 7 * m + 114) / 31;
        int p = (h + l - 7 * m + 114) % 31;

        return LocalDate.of(year, n, p + 1);
    }

    public static class GetHoliday {
        private final String name;
        private final LocalDate date;

        public GetHoliday (String name, LocalDate datum) {
            this.name = name;
            this.date = datum;
        }

        public String getName() {
            return name;
        }

        public LocalDate getDatum() {
            return date;
        }
    }
}
