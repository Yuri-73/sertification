package com.gridnine.testing.filters.impl;

import com.gridnine.testing.FlightFilter;
import com.gridnine.testing.model.Flight;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс отфильтровывания тех полётов, у которых сегмент имеет вылет до текущего момента времени
 */
public class FiltrationDepartureBeforeCurrentTime implements FlightFilter {

    /**
     * Метод получения отфильтрованного списка
     * @param flights Общий список всех перелётов
     * @return Список, отфильтрованный по определённой логике: используется метод сравнения поля getDepartureDate и текущего времени - isAfter()
     */
    @Override
    public List<Flight> filter(List<Flight> flights) {
        LocalDateTime currentTime = LocalDateTime.now();
        return flights.stream()
                .filter(flight -> flight.getSegments()
                        .stream()
                        .anyMatch(segment -> segment.getDepartureDate().isAfter(currentTime)))
                .collect(Collectors.toList());
    }
}
