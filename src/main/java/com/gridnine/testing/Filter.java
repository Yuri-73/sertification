package com.gridnine.testing;

import com.gridnine.testing.model.Flight;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс фильтрации перелётов и получения списков перелётов
 */
public class Filter {

    /**
     * Метод фильтрации по крём критериям
     * @param flightFilters Список из трёх фильтраций
     * @param flights Общий список перелётов
     * @return Возврат общего списка из трёх отдельных списков отфильтрованных перелётов
     */
    public static List<List<Flight>> flightsFilter(List<Flight> flights, List<FlightFilter> flightFilters) {
        List<List<Flight>> filteredFlights  = new ArrayList<>(); //Список трёх отфильтрованных списков для результата
        for (FlightFilter flightFilter : flightFilters) {
            filteredFlights.add(flightFilter.filter(flights)); //Полиморфизм 3-х фильтров в действии
        }
        return filteredFlights ;
    }
}
