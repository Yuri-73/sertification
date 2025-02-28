package com.gridnine.testing;

import com.gridnine.testing.filters.impl.FiltrationDepartureBeforeCurrentTime;
import com.gridnine.testing.filters.impl.FiltrationGroundTimeMoreThanTwoHours;
import com.gridnine.testing.filters.impl.FiltrationSegmentsWithArrivalDateEarlierDepartureDate;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.util.FlightBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<FlightFilter> filters = getFilterList();

        List<Flight> flights = FlightBuilder.createFlights();  //Получение общего списка перелётов через статический метод

        //в методе flightsFilter() происходит последовательное переопределение метода filter() интерфейса FlightFilter:
        List<List<Flight>> filteredFlights = Filter.flightsFilter(flights, filters);

        int count = 0;
        for (List<Flight> filter : filteredFlights) {

            count++;
            if (count == 1) {
                System.out.println();
                System.out.println("Отфильтровка перелётов до текущего момента времени: ");
            }
            if (count == 2) {
                System.out.println();
                System.out.println("Отфильтровка перелётов с датой прилёта раньше даты вылета: ");
            }
            if (count == 3) {
                System.out.println();
                System.out.println("Отфильтровка перелетов, где общее время, проведённое на земле, превышает два часа: ");
            }
            System.out.println(filter.toString());
        }

        List<Flight> flightAnyList1 = scanFilter(flights, filters);
        List<Flight> flightAnyList2 = scanFilter2(flightAnyList1, filters);
        List<Flight> flightAnyList3 = scanFilter2(flightAnyList2, filters);

    }

    private static List<Flight> scanFilter(List<Flight> flights, List<FlightFilter> filters) {
        Scanner scanner = new Scanner(System.in);
        int number = -1;
        if (scanner.hasNextInt()) {
            number = scanner.nextInt();
            System.out.println("Введен фильтр № " + number);
        } else {
            System.out.println("Это не число!");
        }

        List<Flight> filteredFlight = new ArrayList<>();
        if (number >= 0 && number <= 2) {
            filteredFlight = Filter.flightsFilter(flights, filters.get(number));
        } else {
            System.out.println("Значения 0 - 3 не введены");
        }
        System.out.println("filteredFlight = " + filteredFlight);
        return filteredFlight;
    }

    private static List<Flight> scanFilter2(List<Flight> flights, List<FlightFilter> filters) {
        List<Flight> filteredFlight = new ArrayList<>();
        Scanner scanner2 = new Scanner(System.in);
        String str;
        if (scanner2.hasNext()) {
            str = scanner2.next();
            if (str.equals("+")) {
                System.out.println("Теперь введите номер фильтра");
                filteredFlight = scanFilter(flights, filters);
            } else {
                System.out.println("Это не знак [+]! Ввести правильно!");
            }
        } else {
            System.out.println("Это не строка!");
        }
        return filteredFlight;
    }

    private static List<FlightFilter> getFilterList() {
        FiltrationDepartureBeforeCurrentTime departureBeforeCurrentTimeFilter =
                new FiltrationDepartureBeforeCurrentTime();
        FiltrationSegmentsWithArrivalDateEarlierDepartureDate segmentsWithArrivalDateEarlierDepartureDate =
                new FiltrationSegmentsWithArrivalDateEarlierDepartureDate();
        FiltrationGroundTimeMoreThanTwoHours groundTimeMoreThanTwoHoursFilter =
                new FiltrationGroundTimeMoreThanTwoHours();

        List<FlightFilter> filters = new ArrayList<>();
        //Подготовка метода filter() интерфейса FlightFilter к переопределению через три реализации:
        filters.add(departureBeforeCurrentTimeFilter);
        filters.add(segmentsWithArrivalDateEarlierDepartureDate);
        filters.add(groundTimeMoreThanTwoHoursFilter);
        return filters;
    }
}


