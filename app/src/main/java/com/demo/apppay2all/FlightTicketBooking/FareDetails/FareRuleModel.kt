package com.demo.apppay2all.FlightTicketBooking.FareDetails

import com.google.gson.annotations.SerializedName

data class FareRuleModel(

    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("FareRules")
    val fareRules: List<FareRule>,
)

data class FareRule(

    @SerializedName("Airline")
    val airline: String,

    @SerializedName("DepartureTime")
    val departureTime: String,

    @SerializedName("Destination")
    val destination: String,

    @SerializedName("FareBasisCode")
    val fareBasisCode: String,

    @SerializedName("FareInclusions")
    val fareInclusions: List<String>,

    @SerializedName("FareRestriction")
    val fareRestriction: Any?,

    @SerializedName("FareRuleDetail")
    val fareRuleDetail: String,

    @SerializedName("FlightId")
    val flightId: Long,

    @SerializedName("Origin")
    val origin: String,

    @SerializedName("ReturnDate")
    val returnDate: String,

)

