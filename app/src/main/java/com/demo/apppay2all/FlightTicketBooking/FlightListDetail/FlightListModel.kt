package com.demo.apppay2all.FlightTicketBooking.FlightListDetail

import com.google.gson.annotations.SerializedName

data class FlightListModel(
    val status: String="",
    val message: String="",
    @SerializedName("Response")
    val response: Response,
)

data class Response(
    @SerializedName("TraceId")
    val traceId: String="",
    @SerializedName("Origin")
    val origin: String="",
    @SerializedName("Destination")
    val destination: String="",
    @SerializedName("Results")
    val results: List<Result>,
)

data class Result(
    @SerializedName("IsHoldAllowedWithSSR")
    val isHoldAllowedWithSsr: Boolean,
    @SerializedName("ResultIndex")
    val resultIndex: String,
    @SerializedName("Source")
    val source: Long,
    @SerializedName("IsLCC")
    val isLcc: Boolean,
    @SerializedName("IsRefundable")
    val isRefundable: Boolean,
    @SerializedName("IsPanRequiredAtBook")
    val isPanRequiredAtBook: Boolean,
    @SerializedName("IsPanRequiredAtTicket")
    val isPanRequiredAtTicket: Boolean,
    @SerializedName("IsPassportRequiredAtBook")
    val isPassportRequiredAtBook: Boolean,
    @SerializedName("IsPassportRequiredAtTicket")
    val isPassportRequiredAtTicket: Boolean,
    @SerializedName("GSTAllowed")
    val gstallowed: Boolean,
    @SerializedName("IsCouponAppilcable")
    val isCouponAppilcable: Boolean,
    @SerializedName("IsGSTMandatory")
    val isGstmandatory: Boolean,
    @SerializedName("AirlineRemark")
    val airlineRemark: String="",
    @SerializedName("ResultFareType")
    val resultFareType: String="",
    @SerializedName("Fare")
    val fare: Fare,
    @SerializedName("FareBreakdown")
    val fareBreakdown: List<FareBreakdown>,
    @SerializedName("Segments")
    val segments: List<Segment>,
    @SerializedName("LastTicketDate")
    val lastTicketDate: String="",
    @SerializedName("TicketAdvisory")
    val ticketAdvisory: String="",
    @SerializedName("FareRules")
    val fareRules: List<FareRule>,
    @SerializedName("AirlineCode")
    val airlineCode: String="",
    @SerializedName("MiniFareRules")
    val miniFareRules: List<MiniFareRule>,
    @SerializedName("ValidatingAirline")
    val validatingAirline: String="",
    @SerializedName("FareClassification")
    val fareClassification: FareClassification2,
    @SerializedName("IsUpsellAllowed")
    val isUpsellAllowed: Boolean?,
    @SerializedName("PenaltyCharges")
    val penaltyCharges: PenaltyCharges?,
)

data class Fare(
    @SerializedName("Currency")
    val currency: String="",
    @SerializedName("BaseFare")
    val baseFare: Long,
    @SerializedName("Tax")
    val tax: Long,
    @SerializedName("TaxBreakup")
    val taxBreakup: List<TaxBreakUp>,
    @SerializedName("YQTax")
    val yqtax: Long,
    @SerializedName("AdditionalTxnFeeOfrd")
    val additionalTxnFeeOfrd: Long,
    @SerializedName("AdditionalTxnFeePub")
    val additionalTxnFeePub: Long,
    @SerializedName("PGCharge")
    val pgcharge: Long,
    @SerializedName("OtherCharges")
    val otherCharges: Double,
    @SerializedName("ChargeBU")
    val chargeBu: List<ChargeBu>,
    @SerializedName("Discount")
    val discount: Long,
    @SerializedName("PublishedFare")
    val publishedFare: Double,
    @SerializedName("CommissionEarned")
    val commissionEarned: Double,
    @SerializedName("PLBEarned")
    val plbearned: Double,
    @SerializedName("IncentiveEarned")
    val incentiveEarned: Double,
    @SerializedName("OfferedFare")
    val offeredFare: Double,
    @SerializedName("TdsOnCommission")
    val tdsOnCommission: Double,
    @SerializedName("TdsOnPLB")
    val tdsOnPlb: Double,
    @SerializedName("TdsOnIncentive")
    val tdsOnIncentive: Double,
    @SerializedName("ServiceFee")
    val serviceFee: Long,
    @SerializedName("TotalBaggageCharges")
    val totalBaggageCharges: Long,
    @SerializedName("TotalMealCharges")
    val totalMealCharges: Long,
    @SerializedName("TotalSeatCharges")
    val totalSeatCharges: Long,
    @SerializedName("TotalSpecialServiceCharges")
    val totalSpecialServiceCharges: Long,
)

//data class TaxBreakup(
//    val key: String="",
//    val value: Long,
//)

data class ChargeBu(
    val key: String="",
    val value: Double,
)

data class FareBreakdown(
    @SerializedName("Currency")
    val currency: String,
    @SerializedName("PassengerType")
    val passengerType: Long,
    @SerializedName("PassengerCount")
    val passengerCount: Long,
    @SerializedName("BaseFare")
    val baseFare: Long,
    @SerializedName("Tax")
    val tax: Long,
    @SerializedName("TaxBreakUp")
    val taxBreakUp: List<TaxBreakUp>,
    @SerializedName("YQTax")
    val yqtax: Long,
    @SerializedName("AdditionalTxnFeeOfrd")
    val additionalTxnFeeOfrd: Long,
    @SerializedName("AdditionalTxnFeePub")
    val additionalTxnFeePub: Long,
    @SerializedName("PGCharge")
    val pgcharge: Long,
    @SerializedName("SupplierReissueCharges")
    val supplierReissueCharges: Long,
)

data class TaxBreakUp(

    @SerializedName("key")
    val key: String="",

    @SerializedName("value")
    val value: Long,
)

data class Segment(
    @SerializedName("Baggage")
    val baggage: String="",
    @SerializedName("CabinBaggage")
    val cabinBaggage: String="",
    @SerializedName("CabinClass")
    val cabinClass: Long,
    @SerializedName("SupplierFareClass")
    val supplierFareClass: Any?,
    @SerializedName("TripIndicator")
    val tripIndicator: Long,
    @SerializedName("SegmentIndicator")
    val segmentIndicator: Long,
    @SerializedName("Airline")
    val airline: Airline,
    @SerializedName("NoOfSeatAvailable")
    val noOfSeatAvailable: Long,
    @SerializedName("Origin")
    val origin: Origin,
    @SerializedName("Destination")
    val destination: Destination,
    @SerializedName("Duration")
    val duration: Long?=null,
    @SerializedName("GroundTime")
    val groundTime: Long?=0,
    @SerializedName("Mile")
    val mile: Long,
    @SerializedName("StopOver")
    val stopOver: Boolean,
    @SerializedName("FlightInfoIndex")
    val flightInfoIndex: String="",
    @SerializedName("StopPoint")
    val stopPoint: String="",
    @SerializedName("StopPointArrivalTime")
    val stopPointArrivalTime: String="",
    @SerializedName("StopPointDepartureTime")
    val stopPointDepartureTime: String="",
    @SerializedName("Craft")
    val craft: String="",
    @SerializedName("Remark")
    val remark: Any?,
    @SerializedName("IsETicketEligible")
    val isEticketEligible: Boolean,
    @SerializedName("FlightStatus")
    val flightStatus: String,
    @SerializedName("Status")
    val status: String="",
    @SerializedName("FareClassification")
    val fareClassification: FareClassification,
    @SerializedName("AccumulatedDuration")
    val accumulatedDuration: Long? = null,
)

data class Airline(
    @SerializedName("AirlineCode")
    val airlineCode: String="",
    @SerializedName("AirlineName")
    val airlineName: String="",
    @SerializedName("FlightNumber")
    val flightNumber: String="",
    @SerializedName("FareClass")
    val fareClass: String="",
    @SerializedName("OperatingCarrier")
    val operatingCarrier: String="",
)

data class Origin(
    @SerializedName("Airport")
    val airport: Airport,
    @SerializedName("DepTime")
    val depTime: String = "",
)

data class Airport(
    @SerializedName("AirportCode")
    val airportCode: String = "",
    @SerializedName("AirportName")
    val airportName: String = "",
    @SerializedName("Terminal")
    val terminal: String = "",
    @SerializedName("CityCode")
    val cityCode: String = "",
    @SerializedName("CityName")
    val cityName: String = "",
    @SerializedName("CountryCode")
    val countryCode: String = "",
    @SerializedName("CountryName")
    val countryName: String = "",
)

data class Destination(
    @SerializedName("Airport")
    val airport: Airport2,
    @SerializedName("ArrTime")
    val arrTime: String = "",
)

data class Airport2(
    @SerializedName("AirportCode")
    val airportCode: String = "",
    @SerializedName("AirportName")
    val airportName: String="",
    @SerializedName("Terminal")
    val terminal: String="",
    @SerializedName("CityCode")
    val cityCode: String="",
    @SerializedName("CityName")
    val cityName: String="",
    @SerializedName("CountryCode")
    val countryCode: String="",
    @SerializedName("CountryName")
    val countryName: String="",
)

data class FareClassification(
    @SerializedName("Type")
    val type: String="",
)

data class FareRule(
    @SerializedName("Origin")
    val origin: String="",
    @SerializedName("Destination")
    val destination: String="",
    @SerializedName("Airline")
    val airline: String="",
    @SerializedName("FareBasisCode")
    val fareBasisCode: String="",
    @SerializedName("FareRuleDetail")
    val fareRuleDetail: String="",
    @SerializedName("FareRestriction")
    val fareRestriction: String="",
    @SerializedName("FareFamilyCode")
    val fareFamilyCode: String="",
    @SerializedName("FareRuleIndex")
    val fareRuleIndex: String="",
)

data class MiniFareRule(
    @SerializedName("JourneyPoints")
    val journeyPoints: String="",
    @SerializedName("Type")
    val type: String="",
    @SerializedName("From")
    val from: String="",
    @SerializedName("To")
    val to: String="",
    @SerializedName("Unit")
    val unit: String="",
    @SerializedName("Details")
    val details: String="",
)

data class FareClassification2(
    @SerializedName("Color")
    val color: String="",
    @SerializedName("Type")
    val type: String="",
)

data class PenaltyCharges(
    @SerializedName("ReissueCharge")
    val reissueCharge: String="",
    @SerializedName("CancellationCharge")
    val cancellationCharge: String="",
)
