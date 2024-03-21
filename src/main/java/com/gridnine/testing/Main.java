package com.gridnine.testing;

import java.util.List;

public class Main {

    private static final FlightSetFilterServiceImpl flightSetFilterService = new FlightSetFilterServiceImpl();
    private static final List<Flight> flightList = FlightBuilder.createFlights();

    public static void main(String[] args) {

        printAllFlights();
        excludeFlightsWhereDepartureIsBeforeTheCurrentPointInTime();
        excludeFlightsWhereArrivalDateEarlierThanDepartureDate();
        excludeFlightsWhereTotalTimeSpentOnGroundExceedsTwoHours();
    }

    public static void separator() {
        System.out.println("**********************************************************");

    }

    public static void printAllFlights() {
        separator();
        System.out.println("\n1. Cписок перелетов, полученный из заводского класса FlightBuilder:");
        System.out.println(flightList);
    }

    public static void excludeFlightsWhereDepartureIsBeforeTheCurrentPointInTime() {
        separator();
        System.out.println("\n2. Список перелетов, исключающий вылет до текущего момента времени:");
        System.out.println(flightSetFilterService.excludeFlightsWhereDepartureIsBeforeTheCurrentPointInTime(flightList));
    }

    public static void excludeFlightsWhereArrivalDateEarlierThanDepartureDate() {
        separator();
        System.out.println("\n3. Список перелетов, исключающий сегменты с датой прилета раньше даты вылета:");
        System.out.println(flightSetFilterService.excludeFlightsWhereArrivalDateEarlierThanDepartureDate(flightList));
    }

    public static void excludeFlightsWhereTotalTimeSpentOnGroundExceedsTwoHours() {
        separator();
        System.out.println("\n4. Список перелетов, исключающий перелеты, где общее время, проведенное на земле, превышает 2 часа:");
        System.out.println(flightSetFilterService.excludeFlightsWhereTotalTimeSpentOnGroundExceedsTwoHours(flightList));
    }
}