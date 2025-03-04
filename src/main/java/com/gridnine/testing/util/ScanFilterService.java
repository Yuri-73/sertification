package com.gridnine.testing.util;

import com.gridnine.testing.Filter;
import com.gridnine.testing.FlightFilter;
import com.gridnine.testing.model.Flight;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScanFilterService {
    //Логика задания последовательности фильтров над одним общим списком перелётов. 0 - это общий список.
    // Его нельзя вставлять в середину и в конец, чтобы не нарушить логику (она завязана на сумме чисел и правильно работать не сможет.
    // В начало можно, но бесполезно, т.к., например, 02 будет работать, как 2. Поэтому применять 0 только отдельно.)
    public static List<Flight> scanFilter(List<Flight> flights, List<FlightFilter> filters, Integer number) {
        Integer number1, number2, number3;
        List<Flight> filteredFlight = new ArrayList<>();

        if (number > 1000) {
            System.out.println("Такую комбинацию чисел задавать нельзя. Уменьшите до трёх разрядов.");
            return filteredFlight;
        }
        if (number.toString().contains("4") || number.toString().contains("5") || number.toString().contains("6")
                || number.toString().contains("7") || number.toString().contains("8") || number.toString().contains("9") || number.toString().contains("00")
                || number.toString().contains("01") || number.toString().contains("02") || number.toString().contains("03")
                || number.toString().contains("10") || number.toString().contains("20") || number.toString().contains("30")) {
            System.out.println("Это число содержит цифры, отличные от [1, 2, 3] или имеются нули в середине числа (с краю)");
            return filteredFlight;
        }
        if (number == 0) {
            nameFilter(number);
            System.out.println("flights = " + flights);
            return flights;
        }
        if (number >= 1 && number <= 3) {
            System.out.println("number = " + number);
            filteredFlight = Filter.flightsFilter(flights, filters.get(number - 1));
            nameFilter(number);
            System.out.println("filteredFlight = " + filteredFlight);
            return filteredFlight;
        }
        if (number > 10 && number <= 32) {
            number1 = number / 10;
            filteredFlight = Filter.flightsFilter(flights, filters.get(number1 - 1));
            nameFilter(number1);

            number2 = number % 10;
            filteredFlight = Filter.flightsFilter(filteredFlight, filters.get(number2 - 1));
            nameFilter(number2);
            System.out.println("filteredFlight = " + filteredFlight);
            return filteredFlight;
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
        return filteredFlight;
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
}
