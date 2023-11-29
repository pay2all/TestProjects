package com.demo.apppay2all.FlightTicketBooking.FlightListDetail.MultiStopDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.apppay2all.FlightTicketBooking.FlightListDetail.Segment
import com.demo.apppay2all.R
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MultiStopAdapter constructor(mContext: Context,segment: List<Segment>): RecyclerView.Adapter<MultiStopAdapter.ViewHolder?>()
{

    var context: Context = mContext
     var segmentList: List<Segment> = segment

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = segmentList.get(position)

        var hours: Long? = null
        var minnutesRemaining: Long?=null

        if (item.groundTime?.toInt() !=0)
        {
//            val accu : Int = item.accumulatedDuration.toInt();
//
//            val h: Int = accu % (24 * 60) / 60
//            val m: Int = accu % (24 * 60) % 60

             hours = item.groundTime?.div(60)
             minnutesRemaining = item.groundTime?.rem(60)

            println("hour $hours, minutes $minnutesRemaining")

            holder.tv_layover_time.visibility=View.VISIBLE
            holder.tv_layover_time.setText(""+hours+"h "+minnutesRemaining+"m Layover")

        }

        else
        {
            holder.tv_layover_time.visibility=View.GONE
        }


        if (item.duration!=null) {
            val travel_time = item.duration.toInt();

            val h: Int = (travel_time / 60)
            val m: Int = travel_time % 60

            holder.tv_travel_time.text = h.toString() + "h " + m + "m"
        }

        holder.tv_airline_name.setText(item.airline.airlineName)
        holder.tv_flight_no.setText(item.airline.airlineCode+item.airline.flightNumber)
        holder.tv_from.setText(item.origin.airport.airportName)

        if (!item.origin.airport.terminal.equals(""))
        {
            holder.tv_terminal.setText("T "+item.origin.airport.terminal)
        }

        val dep_date: String = item.origin.depTime
        if (dep_date != "") {
            val formatedtime = dep_date.substring(dep_date.length - 8, dep_date.length - 2)
            try {
                val parsedDate: String = getparsedDate(dep_date)
                holder.tv_depart_time.text = "$formatedtime ($parsedDate)"
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
        else {
            holder.tv_depart_time.text = dep_date
        }

        holder.tv_to_city.setText(item.origin.airport.cityName)



//        for dest
        holder.tv_to.setText(item.destination.airport.airportName)

        if (item.destination.airport.terminal != "") {
            holder.tv_to_terminal.text = "T" + item.destination.airport.terminal
        }

        val arrival_date: String = item.destination.arrTime

        if (arrival_date != "") {
            val formatedtime =
                arrival_date.substring(arrival_date.length - 8, arrival_date.length - 2)
            try {
                val parsedDate = getparsedDate(arrival_date)
                holder.tv_arrival_time.text = "$formatedtime ($parsedDate)"
            } catch (e: java.lang.Exception) {
                throw java.lang.RuntimeException(e)
            }
        }
        else {
            holder.tv_arrival_time.text = arrival_date
        }



        if (segmentList.size>1)
        {
            if (position>0)
            {
                holder.ll_divider.visibility=View.VISIBLE
            }
            else
            {
                holder.ll_divider.visibility=View.GONE
            }
        }
        else
        {
            holder.ll_divider.visibility=View.GONE
        }

        holder.tv_to_city.setText(item.destination.airport.cityName)


    }

    override fun getItemCount(): Int {
        return if (segmentList==null) 0 else segmentList.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.flight_list_item_layout_multi_stop,parent,false)

        return ViewHolder(view)
    }


    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view)
    {
         var tv_airline_name : TextView
         var tv_flight_no : TextView
         var tv_from : TextView
         var tv_terminal : TextView
         var tv_travel_time : TextView
         var tv_to : TextView
         var tv_to_terminal : TextView
         var tv_depart_time : TextView
         var tv_arrival_time : TextView
         var tv_available_seat : TextView
         var tv_layover_time : TextView
         var tv_from_city : TextView
         var tv_to_city : TextView
         var ll_divider : View

        init {
            tv_airline_name=view.findViewById(R.id.tv_airline_name)
            tv_flight_no=view.findViewById(R.id.tv_flight_no)
            tv_from=view.findViewById(R.id.tv_from)
            tv_terminal=view.findViewById(R.id.tv_terminal)
            tv_travel_time=view.findViewById(R.id.tv_travel_time)
            tv_to=view.findViewById(R.id.tv_to)
            tv_to_terminal=view.findViewById(R.id.tv_to_terminal)
            tv_depart_time=view.findViewById(R.id.tv_depart_time)
            tv_arrival_time=view.findViewById(R.id.tv_arrival_time)
            tv_available_seat=view.findViewById(R.id.tv_available_seat)
            tv_layover_time=view.findViewById(R.id.tv_layover_time)
            ll_divider=view.findViewById(R.id.ll_divider)

            tv_from_city = view.findViewById<TextView>(R.id.tv_from_city)
            tv_to_city = view.findViewById<TextView>(R.id.tv_to_city)
        }
    }

    @Throws(java.lang.Exception::class)
    private fun getparsedDate(date: String): String {
        val sdf: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        var s2: String = ""
        val d: Date
        try {
            d = sdf.parse(date)
            s2 = SimpleDateFormat("dd MMM").format(d)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return s2
    }
}