package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для фильтрации тестового набора перелетов по правилам:
 * 1) Вылет до текущего момента времени,
 * 2) Сегменты с датой прилёта раньше даты вылета,
 * 3) Перелеты, где общее время, проведённое на земле, превышает два часа
 * (время на земле — это интервал между прилётом одного сегмента и вылетом следующего за ним)
 */
public class FlightSetFilterServiceImpl implements FlightSetFilterService {

    @Override
    public List<Flight> excludeFlightsWhereDepartureIsBeforeTheCurrentPointInTime(List<Flight> flightBuilder) {
        return flightBuilder.stream()
                .filter(flight -> flight
                        .getSegments()
                        .stream()
                        .anyMatch(segment -> segment.getDepartureDate().isAfter(LocalDateTime.now())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Flight> excludeFlightsWhereArrivalDateEarlierThanDepartureDate(List<Flight> flightBuilder) {
        return flightBuilder.stream()
                .filter(flight -> flight
                        .getSegments()
                        .stream()
                        .allMatch(segment -> segment.getArrivalDate().isAfter(segment.getDepartureDate())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Flight> excludeFlightsWhereTotalTimeSpentOnGroundExceedsTwoHours(List<Flight> flightBuilder) {
        return flightBuilder.stream()
                .filter(flight -> {
                    if (flight.getSegments().size() > 1) {
                        for (int i = 0; i < flight.getSegments().size() - 1; ) {
                            if (flight.getSegments().get(i + 1).getDepartureDate().getHour() - flight.getSegments().get(i).getArrivalDate().getHour() > 2) {
                                return false;
                            } else {
                                i++;
                            }
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}