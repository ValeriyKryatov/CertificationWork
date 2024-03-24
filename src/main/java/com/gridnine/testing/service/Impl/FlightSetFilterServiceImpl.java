package com.gridnine.testing.service.Impl;

import com.gridnine.testing.entity.Flight;
import com.gridnine.testing.entity.Segment;
import com.gridnine.testing.service.FlightSetFilterService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
                        .anyMatch(segment -> segment.getDepartureDate().isAfter(LocalDateTime.now())
                                && segment.getArrivalDate().isAfter(LocalDateTime.now())))
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
                .filter(flight -> checkingTheListOfSegments(flight.getSegments()))
                .collect(Collectors.toList());
    }

    private static boolean checkingTheListOfSegments(List<Segment> segments) {
        final long SECONDS_IN_HOUR = 7200;
        if (segments.size() > 1) {
            long sumHours = 0;
            for (int i = segments.size() - 1; i > 0; i--) {
                LocalDateTime dateTimeMax = segments.get(i).getDepartureDate();
                Long endSeconds = dateTimeMax.toEpochSecond(ZoneOffset.UTC);
                LocalDateTime dateTimeMin = segments.get(i - 1).getArrivalDate();
                Long startSeconds = dateTimeMin.toEpochSecond(ZoneOffset.UTC);
                sumHours = sumHours + (endSeconds - startSeconds);
            }
            return SECONDS_IN_HOUR > sumHours;
        }
        return true;
    }
}