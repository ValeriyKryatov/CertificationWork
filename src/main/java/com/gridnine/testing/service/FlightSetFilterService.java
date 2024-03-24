package com.gridnine.testing.service;

import com.gridnine.testing.entity.Flight;

import java.util.List;

/**
 * Сервис для фильтрации тестового набора перелетов по правилам:
 * 1) Вылет до текущего момента времени,
 * 2) Сегменты с датой прилёта раньше даты вылета,
 * 3) Перелеты, где общее время, проведённое на земле, превышает два часа
 * (время на земле — это интервал между прилётом одного сегмента и вылетом следующего за ним)
 */
public interface FlightSetFilterService {

    /**
     * Исключение из списка перелетов вылета до текущего момента времени
     *
     * @param flightBuilder - список перелетов, полученный из заводского класса FlightBuilder
     * @return - отфильтрованный список
     */
    List<Flight> excludeFlightsWhereDepartureIsBeforeTheCurrentPointInTime(List<Flight> flightBuilder);


    /**
     * Исключение из списка перелетов сегментов с датой прилета раньше даты вылета
     *
     * @param flightBuilder - список перелетов, полученный из заводского класса FlightBuilder
     * @return - отфильтрованный список
     */
    List<Flight> excludeFlightsWhereArrivalDateEarlierThanDepartureDate(List<Flight> flightBuilder);

    /**
     * Исключение из списка перелетов, где общее время, проведённое на земле, превышает два часа
     * (время на земле — это интервал между прилётом одного сегмента и вылетом следующего за ним)
     *
     * @param flightBuilder - список перелетов, полученный из заводского класса FlightBuilder
     * @return - отфильтрованный список
     */
    List<Flight> excludeFlightsWhereTotalTimeSpentOnGroundExceedsTwoHours(List<Flight> flightBuilder);
}