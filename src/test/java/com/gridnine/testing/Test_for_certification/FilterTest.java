package com.gridnine.testing.Test_for_certification;

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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FilterTest {
    @Mock
    private FiltrationDepartureBeforeCurrentTime filtrationDepartureBeforeCurrentTime;
    @Mock
    private FiltrationSegmentsWithArrivalDateEarlierDepartureDate segmentsWithArrivalDateEarlierDepartureDate;
    @Mock
    private FiltrationGroundTimeMoreThanTwoHours groundTimeMoreThanTwoHoursFilter;

    private Filter out;

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
    void flightsFilter_All_Test() {
        //test:
        Mockito.when(filtrationDepartureBeforeCurrentTime.filter(flights)).thenReturn(flights1); //Заглушка вместо реализатора-фильтра-1
        Mockito.when(segmentsWithArrivalDateEarlierDepartureDate.filter(flights)).thenReturn(flights2); //Заглушка вместо реализатора-фильтра-2
        Mockito.when(groundTimeMoreThanTwoHoursFilter.filter(any())).thenReturn(flights3); //Заглушка вместо реализатора-фильтра-3

        //То, что мы должны получить в результате (в этой ветке в классе main Лист листов решил не выводить,
        // т.к. вывожу профильтрованные перелёты динамически):
        List<List<Flight>> resultList = new ArrayList<>();
        resultList.add(flights1);  //Тестовый результат перелётов после фильтрации-1 вносим как элемент-лист в Лист 3-х фильтраций
        resultList.add(flights2);  //Тестовый результат перелётов после фильтрации-2 вносим как элемент-лист в Лист 3-х фильтраций
        resultList.add(flights3);  //Тестовый результат перелётов после фильтрации-3 вносим как элемент-лист в Лист 3-х фильтраций

        //check
        assertEquals(out.flightsFilter(flights, flightFilters), resultList);
        assertEquals(resultList.size(), 3);  //3 фильтрации
    }

    //Тестирование перегруженного метода flightsFilter (сделан для динамической логики)
    @Test
    void flightsFilter1_Test() {
        //test:
        Mockito.when(filtrationDepartureBeforeCurrentTime.filter(flights)).thenReturn(flights1); //Заглушка вместо реализатора-фильтра-1
//        Mockito.when(segmentsWithArrivalDateEarlierDepartureDate.filter(flights)).thenReturn(flights2); //Заглушка вместо реализатора-фильтра-2
//        Mockito.when(groundTimeMoreThanTwoHoursFilter.filter(any())).thenReturn(flights3); //Заглушка вместо реализатора-фильтра-3

        //То, что мы должны получить в результате (в этой ветке в классе main Лист листов решил не выводить,
        // т.к. вывожу профильтрованные перелёты динамически):
        List<Flight> resultList = flights1;

        //check
        assertEquals(out.flightsFilter(flights, filtrationDepartureBeforeCurrentTime), resultList);
        assertEquals(resultList.size(), 3);  //3 фильтрации
    }

    @Test
    void flightsFilter2_Test() {
        //test:
//        Mockito.when(filtrationDepartureBeforeCurrentTime.filter(flights)).thenReturn(flights1); //Заглушка вместо реализатора-фильтра-1
        Mockito.when(segmentsWithArrivalDateEarlierDepartureDate.filter(flights)).thenReturn(flights2); //Заглушка вместо реализатора-фильтра-2
//        Mockito.when(groundTimeMoreThanTwoHoursFilter.filter(any())).thenReturn(flights3); //Заглушка вместо реализатора-фильтра-3

        //То, что мы должны получить в результате (в этой ветке в классе main Лист листов решил не выводить,
        // т.к. вывожу профильтрованные перелёты динамически):
        List<Flight> resultList = flights2;

        //check
        assertEquals(out.flightsFilter(flights, segmentsWithArrivalDateEarlierDepartureDate), resultList);
        assertEquals(resultList.size(), 3);  //3 фильтрации
    }

    @Test
    void flightsFilter3_Test() {
        //test:
//        Mockito.when(filtrationDepartureBeforeCurrentTime.filter(flights)).thenReturn(flights1); //Заглушка вместо реализатора-фильтра-1
//        Mockito.when(segmentsWithArrivalDateEarlierDepartureDate.filter(flights)).thenReturn(flights2); //Заглушка вместо реализатора-фильтра-2
        Mockito.when(groundTimeMoreThanTwoHoursFilter.filter(any())).thenReturn(flights3); //Заглушка вместо реализатора-фильтра-3

        //То, что мы должны получить в результате (в этой ветке в классе main Лист листов решил не выводить,
        // т.к. вывожу профильтрованные перелёты динамически):
        List<Flight> resultList = flights3;

        //check
        assertEquals(out.flightsFilter(flights, groundTimeMoreThanTwoHoursFilter), resultList);
        assertEquals(resultList.size(), 3);  //3 фильтрации
    }
}

