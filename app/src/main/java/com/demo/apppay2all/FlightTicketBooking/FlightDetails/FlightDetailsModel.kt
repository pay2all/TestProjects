package com.demo.apppay2all.FlightTicketBooking.FlightDetails

import com.google.gson.annotations.SerializedName

class FlightDetailsModel {

    var status: String? = null
    var message: String? = null

    @SerializedName("FareRules")
    var fareRules: ArrayList<FareRule>? = null
    var flightDetails: FlightDetails? = null

    class Airline {
        @SerializedName("AirlineCode")
        var airlineCode: String? = null

        @SerializedName("AirlineName")
        var airlineName: String = ""

        @SerializedName("FlightNumber")
        var flightNumber: String? = null
    }

    class ArrDateTime {
        var date: String? = null
        var time: String? = null
    }

    class Arrival {
        @SerializedName("AirportCode")
        var airportCode: String? = null

        @SerializedName("AirportName")
        var airportName: String? = null

        @SerializedName("Terminal")
        var terminal: String = ""

        @SerializedName("CityCode")
        var cityCode: String? = null

        @SerializedName("CityName")
        var cityName: String? = null

        @SerializedName("CountryCode")
        var countryCode: String? = null

        @SerializedName("CountryName")
        var countryName: String? = null

        @SerializedName("ArrDateTime")
        var arrDateTime: ArrDateTime? = null
    }

    class Departure {
        @SerializedName("AirportCode")
        var airportCode: String? = null

        @SerializedName("AirportName")
        var airportName: String? = null

        @SerializedName("Terminal")
        var terminal: String = ""

        @SerializedName("CityCode")
        var cityCode: String? = null

        @SerializedName("CityName")
        var cityName: String? = null

        @SerializedName("CountryCode")
        var countryCode: String? = null

        @SerializedName("CountryName")
        var countryName: String? = null

        @SerializedName("DepDateTime")
        var depDateTime: DepDateTime? = null
    }

    class DepDateTime {
        var date: String? = null
        var time: String? = null
    }

    class FareRule {
        @SerializedName("Airline")
        var airline: String? = null

        @SerializedName("DepartureTime")
        var departureTime: String? = null

        @SerializedName("Destination")
        var destination: String? = null

        @SerializedName("FareBasisCode")
        var fareBasisCode: String? = null

        @SerializedName("FareInclusions")
        var fareInclusions: Any? = null

        @SerializedName("FareRestriction")
        var fareRestriction: Any? = null

        @SerializedName("FareRuleDetail")
        var fareRuleDetail: String? = null

        @SerializedName("FlightId")
        var flightId = 0

        @SerializedName("Origin")
        var origin: String? = null

        @SerializedName("ReturnDate")
        var returnDate: String? = null
    }

    class FlightDetails {
        @SerializedName("TraceId")
        var traceId: String? = null

        @SerializedName("Adult")
        var adult: String? = null

        @SerializedName("Child")
        var child: String? = null

        @SerializedName("Infant")
        var infant: String? = null

        @SerializedName("ResultIndex")
        var resultIndex: String? = null

        @SerializedName("IsLCC")
        var isLCC = false

        @SerializedName("IsRefundable")
        var isRefundable = false

        @SerializedName("Baggage")
        var baggage: String? = null

        @SerializedName("CabinBaggage")
        var cabinBaggage: String? = null

        @SerializedName("Airline")
        var airline: Airline? = null

        @SerializedName("Departure")
        var departure: Departure? = null

        @SerializedName("Arrival")
        var arrival: Arrival? = null

        @SerializedName("Duration")
        var duration: String? = null

        @SerializedName("Price")
        var price = 0
    }
}