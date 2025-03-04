package com.gridnine.testing.util;

import com.gridnine.testing.Filter;
import com.gridnine.testing.FlightFilter;
import com.gridnine.testing.filters.impl.FiltrationDepartureBeforeCurrentTime;
import com.gridnine.testing.filters.impl.FiltrationGroundTimeMoreThanTwoHours;
import com.gridnine.testing.filters.impl.FiltrationSegmentsWithArrivalDateEarlierDepartureDate;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(MockitoExtension.class)
class ScanFilterServiceTest {
    @Spy
    private FiltrationDepartureBeforeCurrentTime filtrationDepartureBeforeCurrentTime;
    @Spy
    private FiltrationSegmentsWithArrivalDateEarlierDepartureDate segmentsWithArrivalDateEarlierDepartureDate;
    @Spy
    private FiltrationGroundTimeMoreThanTwoHours groundTimeMoreThanTwoHoursFilter;

    Flight flight1;
    Flight flight2;
    Flight flight3;

    List<Flight> flights = new ArrayList<>(); //Для списка всех рейсов

    List<Flight> flights1 = new ArrayList<>(); //Для тестового результата перелётов после фильтрации-1
    List<Flight> flights2 = new ArrayList<>(); //Для тестового результата перелётов после фильтрации-2
    List<Flight> flights3 = new ArrayList<>(); //Для тестового результата перелётов после фильтрации-3

    List<FlightFilter> flightFilters = new ArrayList<>();  //Лист полиморфизма

    @BeforeEach
    void init() {

        LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);
        Segment segment1 = new Segment(threeDaysFromNow, threeDaysFromNow.plusDays(2)); //сегмент для рейса, не требующего фильтрации
        Segment segment2 = new Segment(threeDaysFromNow.minusDays(6), threeDaysFromNow); //для фильтра-1:сегмент для рейса ранее текущей даты
        Segment segment3 = new Segment(threeDaysFromNow, threeDaysFromNow.minusHours(6)); //для фильтра-2: сегмент для рейса после его же прибытия
        //для фильтра-3: сегменты для рейса с наземным временем более двух часов:
        Segment segment4 = new Segment(threeDaysFromNow, threeDaysFromNow.plusHours(2));
        Segment segment5 = new Segment(threeDaysFromNow.plusHours(5), threeDaysFromNow.plusHours(6));

        List<Segment> segments1 = new ArrayList<>();
        segments1.add(segment1);
        flight1 = new Flight(segments1); //рейс нормальный

        List<Segment> segments2 = new ArrayList<>();
        segments2.add(segment2);
        flight2 = new Flight(segments2); //для фильтра-1: рейс ранее текущей даты

        List<Segment> segments3 = new ArrayList<>();
        segments3.add(segment3);
        flight3 = new Flight(segments3); //для фильтра-2: рейс после его же прибытия

        List<Segment> segments4 = new ArrayList<>();
        segments4.add(segment4);
        segments4.add(segment5);
        Flight flight4 = new Flight(segments4); //для фильтра-3: рейс с наземным временем более двух часов

        //Создание всего списка перелётов до фильтрации:
        flights.add(flight1);
        flights.add(flight2);
        flights.add(flight3);
        flights.add(flight4);

        //Тестовый результат перелётов после фильтрации-1:
        flights1.add(flight1);
        flights1.add(flight3);
        flights1.add(flight4);

        //Тестовый результат перелётов после фильтрации-2:
        flights2.add(flight1);
        flights2.add(flight2);
        flights2.add(flight4);

        //Тестовый результат перелётов после фильтрации-3:
        flights3.add(flight1);
        flights3.add(flight2);
        flights3.add(flight3);

        //Создание полиморфической коллекции с моками вместо реализаторов-фильтров:
        flightFilters.add(filtrationDepartureBeforeCurrentTime);
        flightFilters.add(segmentsWithArrivalDateEarlierDepartureDate);
        flightFilters.add(groundTimeMoreThanTwoHoursFilter);
    }
    @Test
    void scanFilter1_test() {
        //initial conditions:
        Integer number = 1;
        //test
        List<Flight> result = ScanFilterService.scanFilter(flights, flightFilters, number);
        //check:
        assertEquals(result, flights1);
        assertEquals(result.size(), 3);  //3 фильтрации
    }

    @Test
    void scanFilter2_test() {
        //initial conditions:
        Integer number = 2;
        //test
        List<Flight> result = ScanFilterService.scanFilter(flights, flightFilters, number);
        //check:
        assertEquals(result, flights2);
        assertEquals(result.size(), 3);
    }

    @Test
    void scanFilter3_test() {
        //initial conditions:
        Integer number = 3;
        //test
        List<Flight> result = ScanFilterService.scanFilter(flights, flightFilters, number);
        //check:
        assertEquals(result, flights3);
        assertEquals(result.size(), 3);
    }

    @Test
    void scanFilter0_test() {
        //initial conditions:
        Integer number = 0;
        //test
        List<Flight> result = ScanFilterService.scanFilter(flights, flightFilters, number);
        //check:
        assertEquals(result, flights);
        assertEquals(result.size(), 4);
    }
}

