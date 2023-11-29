package com.demo.apppay2all.FlightTicketBooking.FlightListDetail

import com.google.gson.annotations.SerializedName

class FlightListModelNew {
    inner class Airline {
        @SerializedName("AirlineCode")
        var airlineCode: String? = null

        @SerializedName("AirlineName")
        var airlineName: String? = null

        @SerializedName("FlightNumber")
        var flightNumber: String? = null

        @SerializedName("FareClass")
        var fareClass: String? = null

        @SerializedName("OperatingCarrier")
        var operatingCarrier: String? = null
    }

    inner class Airport {
        @SerializedName("AirportCode")
        var airportCode: String? = null

        @SerializedName("AirportName")
        var airportName: String? = null

        @SerializedName("Terminal")
        var terminal: String? = null

        @SerializedName("CityCode")
        var cityCode: String? = null

        @SerializedName("CityName")
        var cityName: String? = null

        @SerializedName("CountryCode")
        var countryCode: String? = null

        @SerializedName("CountryName")
        var countryName: String? = null
    }

    inner class ChargeBU {
        var key: String? = null
        var value = 0
    }

    inner class Destination {
        @SerializedName("Airport")
        var airport: Airport? = null

        @SerializedName("ArrTime")
        var arrTime: String? = null
    }

    inner class Fare {
        @SerializedName("Currency")
        var currency: String? = null

        @SerializedName("BaseFare")
        var baseFare = 0

        @SerializedName("Tax")
        var tax = 0

        @SerializedName("TaxBreakup")
        var taxBreakup: ArrayList<TaxBreakup>? = null

        @SerializedName("YQTax")
        var yQTax = 0

        @SerializedName("AdditionalTxnFeeOfrd")
        var additionalTxnFeeOfrd = 0

        @SerializedName("AdditionalTxnFeePub")
        var additionalTxnFeePub = 0

        @SerializedName("PGCharge")
        var pGCharge = 0

        @SerializedName("OtherCharges")
        var otherCharges = 0

        @SerializedName("ChargeBU")
        var chargeBU: ArrayList<ChargeBU>? = null

        @SerializedName("Discount")
        var discount = 0

        @SerializedName("PublishedFare")
        var publishedFare = 0

        @SerializedName("CommissionEarned")
        var commissionEarned = 0

        @SerializedName("PLBEarned")
        var pLBEarned = 0

        @SerializedName("IncentiveEarned")
        var incentiveEarned = 0

        @SerializedName("OfferedFare")
        var offeredFare = 0

        @SerializedName("TdsOnCommission")
        var tdsOnCommission = 0

        @SerializedName("TdsOnPLB")
        var tdsOnPLB = 0

        @SerializedName("TdsOnIncentive")
        var tdsOnIncentive = 0

        @SerializedName("ServiceFee")
        var serviceFee = 0

        @SerializedName("TotalBaggageCharges")
        var totalBaggageCharges = 0

        @SerializedName("TotalMealCharges")
        var totalMealCharges = 0

        @SerializedName("TotalSeatCharges")
        var totalSeatCharges = 0

        @SerializedName("TotalSpecialServiceCharges")
        var totalSpecialServiceCharges = 0
    }

    inner class FareBreakdown {
        @SerializedName("Currency")
        var currency: String? = null

        @SerializedName("PassengerType")
        var passengerType = 0

        @SerializedName("PassengerCount")
        var passengerCount = 0

        @SerializedName("BaseFare")
        var baseFare = 0

        @SerializedName("Tax")
        var tax = 0

        @SerializedName("TaxBreakUp")
        var taxBreakUp: ArrayList<TaxBreakup>? = null

        @SerializedName("YQTax")
        var yQTax = 0

        @SerializedName("AdditionalTxnFeeOfrd")
        var additionalTxnFeeOfrd = 0

        @SerializedName("AdditionalTxnFeePub")
        var additionalTxnFeePub = 0

        @SerializedName("PGCharge")
        var pGCharge = 0

        @SerializedName("SupplierReissueCharges")
        var supplierReissueCharges = 0
    }

    inner class FareClassification {
        @SerializedName("Type")
        var type: String? = null

        @SerializedName("Color")
        var color: String? = null
    }

    inner class FareRule {
        @SerializedName("Origin")
        var origin: String? = null

        @SerializedName("Destination")
        var destination: String? = null

        @SerializedName("Airline")
        var airline: String? = null

        @SerializedName("FareBasisCode")
        var fareBasisCode: String? = null

        @SerializedName("FareRuleDetail")
        var fareRuleDetail: String? = null

        @SerializedName("FareRestriction")
        var fareRestriction: String? = null

        @SerializedName("FareFamilyCode")
        var fareFamilyCode: String? = null

        @SerializedName("FareRuleIndex")
        var fareRuleIndex: String? = null
    }

    inner class MiniFareRule {
        @SerializedName("JourneyPoints")
        var journeyPoints: String? = null

        @SerializedName("Type")
        var type: String? = null

        @SerializedName("From")
        var from: String? = null

        @SerializedName("To")
        var to: String? = null

        @SerializedName("Unit")
        var unit: String? = null

        @SerializedName("Details")
        var details: String? = null
    }

    inner class Origin {
        @SerializedName("Airport")
        var airport: Airport? = null

        @SerializedName("DepTime")
        var depTime: String? = null
    }

    inner class Root {
        @SerializedName("IsHoldAllowedWithSSR")
        var isHoldAllowedWithSSR = false

        @SerializedName("ResultIndex")
        var resultIndex: String? = null

        @SerializedName("Source")
        var source = 0

        @SerializedName("IsLCC")
        var isLCC = false

        @SerializedName("IsRefundable")
        var isRefundable = false

        @SerializedName("IsPanRequiredAtBook")
        var isPanRequiredAtBook = false

        @SerializedName("IsPanRequiredAtTicket")
        var isPanRequiredAtTicket = false

        @SerializedName("IsPassportRequiredAtBook")
        var isPassportRequiredAtBook = false

        @SerializedName("IsPassportRequiredAtTicket")
        var isPassportRequiredAtTicket = false

        @SerializedName("GSTAllowed")
        var gSTAllowed = false

        @SerializedName("IsCouponAppilcable")
        var isCouponAppilcable = false

        @SerializedName("IsGSTMandatory")
        var isGSTMandatory = false

        @SerializedName("AirlineRemark")
        var airlineRemark: String? = null

        @SerializedName("ResultFareType")
        var resultFareType: String? = null

        @SerializedName("Fare")
        var fare: Fare? = null

        @SerializedName("FareBreakdown")
        var fareBreakdown: ArrayList<FareBreakdown>? = null

        @SerializedName("Segments")
        var segments: ArrayList<Segment>? = null

        @SerializedName("LastTicketDate")
        var lastTicketDate: Any? = null

        @SerializedName("TicketAdvisory")
        var ticketAdvisory: Any? = null

        @SerializedName("FareRules")
        var fareRules: ArrayList<FareRule>? = null

        @SerializedName("AirlineCode")
        var airlineCode: String? = null

        @SerializedName("MiniFareRules")
        var miniFareRules: ArrayList<MiniFareRule>? = null

        @SerializedName("ValidatingAirline")
        var validatingAirline: String? = null

        @SerializedName("FareClassification")
        var fareClassification: FareClassification? = null
    }

    inner class Segment {
        @SerializedName("Baggage")
        var baggage: String? = null

        @SerializedName("CabinBaggage")
        var cabinBaggage: String? = null

        @SerializedName("CabinClass")
        var cabinClass = 0

        @SerializedName("SupplierFareClass")
        var supplierFareClass: Any? = null

        @SerializedName("TripIndicator")
        var tripIndicator = 0

        @SerializedName("SegmentIndicator")
        var segmentIndicator = 0

        @SerializedName("Airline")
        var airline: Airline? = null

        @SerializedName("NoOfSeatAvailable")
        var noOfSeatAvailable = 0

        @SerializedName("Origin")
        var origin: Origin? = null

        @SerializedName("Destination")
        var destination: Destination? = null

        @SerializedName("Duration")
        var duration = 0

        @SerializedName("GroundTime")
        var groundTime = 0

        @SerializedName("Mile")
        var mile = 0

        @SerializedName("StopOver")
        var stopOver = false

        @SerializedName("FlightInfoIndex")
        var flightInfoIndex: String? = null

        @SerializedName("StopPoint")
        var stopPoint: String? = null

        @SerializedName("StopPointArrivalTime")
        var stopPointArrivalTime: Any? = null

        @SerializedName("StopPointDepartureTime")
        var stopPointDepartureTime: Any? = null

        @SerializedName("Craft")
        var craft: String? = null

        @SerializedName("Remark")
        var remark: Any? = null

        @SerializedName("IsETicketEligible")
        var isETicketEligible = false

        @SerializedName("FlightStatus")
        var flightStatus: String? = null

        @SerializedName("Status")
        var status: String? = null

        @SerializedName("FareClassification")
        var fareClassification: FareClassification? = null
    }

    inner class TaxBreakup {
        var key: String? = null
        var value = 0
    }
}