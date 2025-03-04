package com.gridnine.testing.filters.impl;

import com.gridnine.testing.FlightFilter;
import com.gridnine.testing.model.Flight;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс отфильтровывания тех полётов, у которых сегменты с датой прилёта раньше даты вылета.
 */
public class FiltrationSegmentsWithArrivalDateEarlierDepartureDate implements FlightFilter {

    /**
     * Метод получения отфильтрованного списка
     * @param flights Общий список всех перелётов
     * @return Список, отфильтрованный по определённой логике: используется метод сравнения полей getArrivalDate и getDepartureDate каждого сегмента - isAfter()
     */
    @Override
    public List<Flight> filter(List<Flight> flights) {
        return flights.stream()
                .filter(flight -> flight.getSegments()
                        .stream()
                        .anyMatch(segment -> segment.getArrivalDate().isAfter(segment.getDepartureDate())))
                .collect(Collectors.toList());
    }
}
