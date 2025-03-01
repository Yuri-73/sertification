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

        Scanner scanner = new Scanner(System.in);
        int number = -1;
        while (scanner.hasNextInt()) {
            scanFilter(flights, filters, number, scanner);
        }
    }

    private static void scanFilter(List<Flight> flights, List<FlightFilter> filters, Integer number, Scanner scanner) {
        int number1;
        int number2;
        int number3;
        List<Flight> filteredFlight = new ArrayList<>();

        if (scanner.hasNextInt()) {
            number = scanner.nextInt();
            if (number >= 1 && number <= 3) {
                filteredFlight = Filter.flightsFilter(flights, filters.get(number - 1));
                System.out.println("Введен фильтр № " + number);
                System.out.println("filteredFlight = " + filteredFlight);
            }
            if (number > 10 && number <= 32) {
                number1 = number / 10;
                filteredFlight = Filter.flightsFilter(flights, filters.get(number1 - 1));

                number2 = number % 10;
                filteredFlight = Filter.flightsFilter(filteredFlight, filters.get(number2 - 1));
                System.out.println("Введен фильтр № " + number);
                System.out.println("filteredFlight = " + filteredFlight);

            }
            if (number > 100 && number <= 321) {
                number1 = number / 100;
                filteredFlight = Filter.flightsFilter(flights, filters.get(number1 - 1));

                number2 = (number / 10) % 10;
                filteredFlight = Filter.flightsFilter(filteredFlight, filters.get(number2 - 1));

                number3 = number % 10;
                filteredFlight = Filter.flightsFilter(filteredFlight, filters.get(number3 - 1));
                System.out.println("Введен фильтр № " + number);
                System.out.println("filteredFlight = " + filteredFlight);
            }
        } else {
            System.out.println("Это не число или число, не входящее в диапазон [1, 2, 3]");
        }
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


