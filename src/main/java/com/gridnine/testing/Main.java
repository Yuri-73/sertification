package com.gridnine.testing;

import com.gridnine.testing.filters.impl.FiltrationDepartureBeforeCurrentTime;
import com.gridnine.testing.filters.impl.FiltrationGroundTimeMoreThanTwoHours;
import com.gridnine.testing.filters.impl.FiltrationSegmentsWithArrivalDateEarlierDepartureDate;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.util.FlightBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.gridnine.testing.util.ScanFilterService.scanFilter;

public class Main {
    public static void main(String[] args) {
        List<FlightFilter> filters = getFilterList(); //Создание полиморфического Листа

        List<Flight> flights = FlightBuilder.createFlights();  //Получение общего списка перелётов через статический метод

        //в методе flightsFilter() происходит последовательное переопределение метода filter() интерфейса FlightFilter:
        List<List<Flight>> filteredFlights = Filter.flightsFilter(flights, filters); //В этой ветке не используется

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextInt()) {  //Создание бесконечного цикла, пока вводим числовые символы.
            // Если ввести нечисловые, то система закончит работу и надо будет её перезапускать.
            Integer number = scanner.nextInt();
            scanFilter(flights, filters, number); //Корневая логика последовательной фильтрации
        }
        System.out.println("Программа завершилась, т.к. введен нечисловой символ");
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


