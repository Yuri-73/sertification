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
        List<List<Flight>> filteredFlights = Filter.flightsFilter(flights, filters); //В этой ветке не используется

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {
            scanFilter(flights, filters, scanner);
        }
    }

    private static void scanFilter(List<Flight> flights, List<FlightFilter> filters, Scanner scanner) {
        Integer number, number1, number2, number3;
        List<Flight> filteredFlight;

        if (scanner.hasNextInt()) {
            number = scanner.nextInt();
            if (number > 1000) {
                System.out.println("Такую комбинацию чисел задавать нельзя. Уменьшите до трёх разрядов.");
                return;
            }
            if (number.toString().contains("4") || number.toString().contains("5") || number.toString().contains("6")
                    || number.toString().contains("7") || number.toString().contains("8") || number.toString().contains("9") || number.toString().contains("00")
                    || number.toString().contains("01") || number.toString().contains("02") || number.toString().contains("03")
                    || number.toString().contains("10") || number.toString().contains("20") || number.toString().contains("30")) {
                System.out.println("Это число содержит цифры, отличные от [1, 2, 3] или имеются нули в середине числа (с краю)");
                return;
            }
            if (number == 0) {
                nameFilter(number);
                System.out.println("flights = " + flights);
            }
            if (number >= 1 && number <= 3) {
                filteredFlight = Filter.flightsFilter(flights, filters.get(number - 1));
                nameFilter(number);
                System.out.println("filteredFlight = " + filteredFlight);
                return;
            }
            if (number > 10 && number <= 32) {
                number1 = number / 10;
                filteredFlight = Filter.flightsFilter(flights, filters.get(number1 - 1));
                nameFilter(number1);

                number2 = number % 10;
                filteredFlight = Filter.flightsFilter(filteredFlight, filters.get(number2 - 1));
                nameFilter(number2);
                System.out.println("filteredFlight = " + filteredFlight);
                return;
            }
            if (number > 100 && number <= 321) {
                number1 = number / 100;
                filteredFlight = Filter.flightsFilter(flights, filters.get(number1 - 1));
                nameFilter(number1);

                number2 = (number / 10) % 10;
                filteredFlight = Filter.flightsFilter(filteredFlight, filters.get(number2 - 1));
                nameFilter(number2);

                number3 = number % 10;
                filteredFlight = Filter.flightsFilter(filteredFlight, filters.get(number3 - 1));
                nameFilter(number3);
                System.out.println("filteredFlight = " + filteredFlight);
            }
        } else {
            System.out.println("Это не число!");
        }
    }

    private static void nameFilter(Integer number) {
        if (number == 0) {
            System.out.println("Полный список перелётов до фильтрации: ");
        }
        if (number == 1) {
            System.out.println("Отфильтровка перелётов до текущего момента времени: ");
        }
        if (number == 2) {
            System.out.println("Отфильтровка перелётов с датой прилёта раньше даты вылета: ");
        }
        if (number == 3) {
            System.out.println("Отфильтровка перелетов, где общее время, проведённое на земле, превышает два часа: ");
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


