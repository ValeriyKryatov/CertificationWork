import com.gridnine.testing.Flight;
import com.gridnine.testing.FlightSetFilterService;
import com.gridnine.testing.FlightSetFilterServiceImpl;
import com.gridnine.testing.Segment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Тестируем класс реализации сервиса для фильтрации тестового набора перелетов по правилам:
 * 1) Вылет до текущего момента времени,
 * 2) Сегменты с датой прилёта раньше даты вылета,
 * 3) Перелеты, где общее время, проведённое на земле, превышает два часа
 * (время на земле — это интервал между прилётом одного сегмента и вылетом следующего за ним)
 */
class FlightSetFilterServiceImplTest {

    private static final LocalDateTime DEPARTURE_DATE_TRUE = LocalDateTime.now().plusDays(2);
    private static final LocalDateTime DEPARTURE_DATE_FALSE = LocalDateTime.now().minusDays(3);
    private static final LocalDateTime ARRIVAL_DATE_TRUE = DEPARTURE_DATE_TRUE.plusHours(6);
    private static final LocalDateTime ARRIVAL_DATE_FALSE = LocalDateTime.now().minusHours(8);
    private Segment segmentTrue;
    private Segment segmentFalse1;
    private Segment segmentFalse2;
    private final List<Flight> flightList = new ArrayList<>();
    private final List<Segment> segmentList1 = new ArrayList<>();
    private final List<Segment> segmentList2 = new ArrayList<>();
    private FlightSetFilterService flightSetFilterService;

    @BeforeEach
    public void setup() {

        flightSetFilterService = new FlightSetFilterServiceImpl();

        segmentTrue = new Segment(DEPARTURE_DATE_TRUE, ARRIVAL_DATE_TRUE);
        segmentFalse1 = new Segment(DEPARTURE_DATE_FALSE, ARRIVAL_DATE_TRUE);
        segmentFalse2 = new Segment(DEPARTURE_DATE_TRUE, ARRIVAL_DATE_FALSE);
    }

    @Test
    public void testExcludeFlightsWhereDepartureIsBeforeTheCurrentPointInTime() {

        segmentList1.add(segmentTrue);
        segmentList2.add(segmentFalse1);

        flightList.add(new Flight(segmentList1));
        flightList.add(new Flight(segmentList2));
        flightList.add(new Flight(segmentList1));

        Assertions.assertEquals(3, flightList.size());

        List<Flight> result = flightSetFilterService.excludeFlightsWhereDepartureIsBeforeTheCurrentPointInTime(flightList);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(DEPARTURE_DATE_TRUE, result.get(0).getSegments().get(0).getDepartureDate());
    }

    @Test
    public void testExcludeFlightsWhereArrivalDateEarlierThanDepartureDate() {

        segmentList1.add(segmentTrue);
        segmentList2.add(segmentFalse2);

        flightList.add(new Flight(segmentList1));
        flightList.add(new Flight(segmentList2));

        Assertions.assertEquals(2, flightList.size());

        List<Flight> result = flightSetFilterService.excludeFlightsWhereArrivalDateEarlierThanDepartureDate(flightList);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(ARRIVAL_DATE_TRUE, result.get(0).getSegments().get(0).getArrivalDate());
    }

    @Test
    public void testExcludeFlightsWhereTotalTimeSpentOnGroundExceedsTwoHours() {

        segmentList1.add(segmentTrue);
        segmentList2.add(segmentFalse2);
        segmentList2.add(segmentFalse1);

        flightList.add(new Flight(segmentList1));
        flightList.add(new Flight(segmentList2));

        Assertions.assertEquals(2, flightList.size());

        List<Flight> result = flightSetFilterService.excludeFlightsWhereTotalTimeSpentOnGroundExceedsTwoHours(flightList);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.get(0).getSegments().size());
        Assertions.assertEquals(ARRIVAL_DATE_TRUE, result.get(0).getSegments().get(0).getArrivalDate());
    }
}