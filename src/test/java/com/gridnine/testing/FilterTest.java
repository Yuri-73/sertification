package com.gridnine.testing;

import com.gridnine.testing.filters.impl.FiltrationDepartureBeforeCurrentTime;
import com.gridnine.testing.filters.impl.FiltrationGroundTimeMoreThanTwoHours;
import com.gridnine.testing.filters.impl.FiltrationSegmentsWithArrivalDateEarlierDepartureDate;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FilterTest {
    @Mock
    private FlightFilter flightFilter;

    private Filter out;

    @Test
    void flightsFilter() {
        //test:
        LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);

        Segment segment1 = new Segment(threeDaysFromNow, threeDaysFromNow.plusDays(2)); //сегмент для рейса, не требующего фильтрации

        Segment segment2 = new Segment(threeDaysFromNow.minusDays(6), threeDaysFromNow); //для фильтра-1:сегмент для рейса ранее текущей даты

        Segment segment3 = new Segment(threeDaysFromNow, threeDaysFromNow.minusHours(6)); //для фильтра-2: сегмент для рейса после его же прибытия

        //для фильтра-3: сегменты для рейса с наземным временем более двух часов:
        Segment segment4 = new Segment(threeDaysFromNow, threeDaysFromNow.plusHours(2));
        Segment segment5 = new Segment(threeDaysFromNow.plusHours(5), threeDaysFromNow.plusHours(6));

        List<Segment> segments1 = new ArrayList<>();
        segments1.add(segment1);
        Flight flight1 = new Flight(segments1); //рейс нормальный

        List<Segment> segments2 = new ArrayList<>();
        segments2.add(segment2);
        Flight flight2 = new Flight(segments2); //для фильтра-1: рейс ранее текущей даты

        List<Segment> segments3 = new ArrayList<>();
        segments3.add(segment3);
        Flight flight3 = new Flight(segments3); //для фильтра-2: рейс после его же прибытия

        List<Segment> segments4 = new ArrayList<>();
        segments4.add(segment4);
        segments4.add(segment5);
        Flight flight4 = new Flight(segments4); //для фильтра-3: рейс с наземным временем более двух часов

        List<Flight> flights = new ArrayList<>(); //Все рейсы
        flights.add(flight1);
        flights.add(flight2);
        flights.add(flight3);
        flights.add(flight4);

        List<Flight> flights1 = new ArrayList<>();
        flights1.add(flight1);
        flights1.add(flight3);
        flights1.add(flight4);

        List<Flight> flights2 = new ArrayList<>();
        flights2.add(flight1);
        flights2.add(flight2);
        flights2.add(flight4);

        List<Flight> flights3 = new ArrayList<>();
        flights3.add(flight1);
        flights3.add(flight2);
        flights3.add(flight3);

        List<FlightFilter> flightFilters = new ArrayList<>();  //Лист полиморфизма

        FiltrationDepartureBeforeCurrentTime filtrationDepartureBeforeCurrentTime = new FiltrationDepartureBeforeCurrentTime();
        FiltrationSegmentsWithArrivalDateEarlierDepartureDate segmentsWithArrivalDateEarlierDepartureDate = new FiltrationSegmentsWithArrivalDateEarlierDepartureDate();
        FiltrationGroundTimeMoreThanTwoHours groundTimeMoreThanTwoHoursFilter = new FiltrationGroundTimeMoreThanTwoHours();
        flightFilters.add(filtrationDepartureBeforeCurrentTime);
        flightFilters.add(segmentsWithArrivalDateEarlierDepartureDate);
        flightFilters.add(groundTimeMoreThanTwoHoursFilter);

        List<List<Flight>> flightsFilter = new ArrayList<>();
        flightsFilter.add(flights1);
        flightsFilter.add(flights2);
        flightsFilter.add(flights3);

        //check
        assertEquals(Filter.flightsFilter(flights, flightFilters), flightsFilter);
        assertEquals(flightsFilter.size(), 3);  //3 фильтрации
    }
}
