package com.gridnine.testing;

import com.gridnine.testing.filters.impl.FiltrationDepartureBeforeCurrentTime;
import com.gridnine.testing.filters.impl.FiltrationGroundTimeMoreThanTwoHours;
import com.gridnine.testing.filters.impl.FiltrationSegmentsWithArrivalDateEarlierDepartureDate;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.util.FlightBuilder;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<FlightFilter> filters = getFilterList();

        List<Flight> flights = FlightBuilder.createFlights();  //Получение общего списка перелётов через статический метод

        //в методе flightsFilter() происходит последовательное переопределение метода filter() интерфейса FlightFilter:
        List<List<Flight>> filteredFlights = Filter.flightsFilter(flights, filters);

        int count = 0;
        for (List<Flight> f:filteredFlights) {

            count++;
            if (count == 1) {
                System.out.println("Отфильтровка перелётов до текущего момента времени: ");
            }
            if (count == 2) {
                System.out.println("Отфильтровка перелётов с датой прилёта раньше даты вылета: ");
            }
            if (count == 3) {
                System.out.println("Отфильтровка перелетов, где общее время, проведённое на земле, превышает два часа: ");
            }
            System.out.println(f.toString());
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



