package com.example.letspaanidriver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.letspaanidriver.apis.classes.*
import com.example.letspani.apis.AppController
import com.example.letspani.apis.UserService
import com.example.letspani.utils.UserShared
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.payouts.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.R
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.letspaanidriver.activity.Home
import com.example.letspaanidriver.activity.LogIn
import com.google.android.libraries.places.internal.of
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar.*


//desktop new payouts

class Payouts : Fragment() {

    val labels = ArrayList<String>()
    val NoOfEmp = ArrayList<BarEntry>()
    var incentive: Int = 0
    var salary = ""
    var orderearningList: List<orders> = ArrayList<orders>()


    val profileResponse = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(com.example.letspaanidriver.R.layout.payouts, container, false)
        if (Home.langval == 1) {
            salary = "वेतन"
        } else {
            salary = "salary"
        }
        val txtDailly = view.findViewById<TextView>(com.example.letspaanidriver.R.id.txtDailly)
        val txtweekly = view.findViewById<TextView>(com.example.letspaanidriver.R.id.txtweekly)
        val txtMonthly = view.findViewById<TextView>(com.example.letspaanidriver.R.id.txtMonthly)
        val week1 = view.findViewById<TextView>(com.example.letspaanidriver.R.id.week1)
        val week2 = view.findViewById<TextView>(com.example.letspaanidriver.R.id.week2)
        val week3 = view.findViewById<TextView>(com.example.letspaanidriver.R.id.week3)
        val week4 = view.findViewById<TextView>(com.example.letspaanidriver.R.id.week4)
        val week5 = view.findViewById<TextView>(com.example.letspaanidriver.R.id.week5)
        val layWeek = view.findViewById<LinearLayout>(com.example.letspaanidriver.R.id.layWeek)
        txtDailly.setBackgroundColor(Color.parseColor("#74B8D6"))
        txtDailly.setOnClickListener {
            SetGraphDaily(orderearningList)
            layWeek.visibility = View.GONE
            txtDailly.setBackgroundColor(Color.parseColor("#74B8D6"))
            txtweekly.setBackgroundColor(Color.parseColor("#ffffff"))
            txtMonthly.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        txtweekly.setOnClickListener {

            txtweekly.setBackgroundColor(Color.parseColor("#74B8D6"))
            txtDailly.setBackgroundColor(Color.parseColor("#ffffff"))
            txtMonthly.setBackgroundColor(Color.parseColor("#ffffff"))

            layWeek.visibility = View.VISIBLE
            val localCalendar = Calendar.getInstance(TimeZone.getDefault())
            val currentMonth = localCalendar.get(Calendar.MONTH) + 1
            val currentMonthdays = localCalendar.getActualMaximum(DAY_OF_MONTH)
            val datedays = localCalendar.get(DATE)

            var mon = getCalculatedMonth("MMM", currentMonth)

            Log.i("da", "totaldayofmonth====gfhnfgghb===" + mon)

            var strtrat: Int = 0
            var endrat: Int = 0


            if (datedays >= 1 && datedays <= 7) {
                week1.setBackgroundColor(Color.parseColor("#76D4FC"))
                week2.setBackgroundColor(Color.parseColor("#ffffff"))
                week3.setBackgroundColor(Color.parseColor("#ffffff"))
                week4.setBackgroundColor(Color.parseColor("#ffffff"))
                week5.setBackgroundColor(Color.parseColor("#ffffff"))

                strtrat = 1
                endrat = 7


            } else if (datedays >= 8 && datedays <= 14) {
                week2.setBackgroundColor(Color.parseColor("#76D4FC"))
                week1.setBackgroundColor(Color.parseColor("#ffffff"))
                week3.setBackgroundColor(Color.parseColor("#ffffff"))
                week4.setBackgroundColor(Color.parseColor("#ffffff"))
                week5.setBackgroundColor(Color.parseColor("#ffffff"))

                strtrat = 8
                endrat = 14

            } else if (datedays >= 15 && datedays <= 21) {
                week3.setBackgroundColor(Color.parseColor("#76D4FC"))
                week1.setBackgroundColor(Color.parseColor("#ffffff"))
                week2.setBackgroundColor(Color.parseColor("#ffffff"))
                week4.setBackgroundColor(Color.parseColor("#ffffff"))
                week5.setBackgroundColor(Color.parseColor("#ffffff"))

                strtrat = 15
                endrat = 21

            } else if (datedays >= 22 && datedays <= 28) {
                week4.setBackgroundColor(Color.parseColor("#76D4FC"))
                week1.setBackgroundColor(Color.parseColor("#ffffff"))
                week2.setBackgroundColor(Color.parseColor("#ffffff"))
                week3.setBackgroundColor(Color.parseColor("#ffffff"))
                week5.setBackgroundColor(Color.parseColor("#ffffff"))

                strtrat = 22
                endrat = 28

            } else if (datedays >= 29 && datedays <= 31) {
                week5.setBackgroundColor(Color.parseColor("#76D4FC"))
                week1.setBackgroundColor(Color.parseColor("#ffffff"))
                week2.setBackgroundColor(Color.parseColor("#ffffff"))
                week3.setBackgroundColor(Color.parseColor("#ffffff"))
                week4.setBackgroundColor(Color.parseColor("#ffffff"))

                strtrat = 29
                endrat = 31

            }

            week1.setText("1-7" + mon)
            week2.setText("8-14" + mon)
            week3.setText("15-21" + mon)
            week4.setText("22-28" + mon)

            if (currentMonthdays > 28) {
                week5.setText("29-" + currentMonthdays + mon)
            } else {
                week5.visibility = View.GONE
            }
            SetGraphWeekly(orderearningList, strtrat, endrat)
        }

        txtMonthly.setOnClickListener {

            txtMonthly.setBackgroundColor(Color.parseColor("#74B8D6"))
            txtDailly.setBackgroundColor(Color.parseColor("#ffffff"))
            txtweekly.setBackgroundColor(Color.parseColor("#ffffff"))

            layWeek.visibility = View.GONE
            SetGraphMonthly(orderearningList)
        }

        week1.setOnClickListener {
            SetGraphWeekly(orderearningList, 1, 7)
            week1.setBackgroundColor(Color.parseColor("#76D4FC"))
            week5.setBackgroundColor(Color.parseColor("#ffffff"))
            week2.setBackgroundColor(Color.parseColor("#ffffff"))
            week3.setBackgroundColor(Color.parseColor("#ffffff"))
            week4.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        week2.setOnClickListener {
            SetGraphWeekly(orderearningList, 8, 14)
            week2.setBackgroundColor(Color.parseColor("#76D4FC"))
            week5.setBackgroundColor(Color.parseColor("#ffffff"))
            week1.setBackgroundColor(Color.parseColor("#ffffff"))
            week3.setBackgroundColor(Color.parseColor("#ffffff"))
            week4.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        week3.setOnClickListener {
            SetGraphWeekly(orderearningList, 15, 21)
            week3.setBackgroundColor(Color.parseColor("#76D4FC"))
            week5.setBackgroundColor(Color.parseColor("#ffffff"))
            week2.setBackgroundColor(Color.parseColor("#ffffff"))
            week1.setBackgroundColor(Color.parseColor("#ffffff"))
            week4.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        week4.setOnClickListener {
            SetGraphWeekly(orderearningList, 22, 28)
            week4.setBackgroundColor(Color.parseColor("#76D4FC"))
            week5.setBackgroundColor(Color.parseColor("#ffffff"))
            week2.setBackgroundColor(Color.parseColor("#ffffff"))
            week3.setBackgroundColor(Color.parseColor("#ffffff"))
            week1.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        week5.setOnClickListener {
            SetGraphWeekly(orderearningList, 29, 31)
            week5.setBackgroundColor(Color.parseColor("#76D4FC"))
            week1.setBackgroundColor(Color.parseColor("#ffffff"))
            week2.setBackgroundColor(Color.parseColor("#ffffff"))
            week3.setBackgroundColor(Color.parseColor("#ffffff"))
            week4.setBackgroundColor(Color.parseColor("#ffffff"))
        }


        ongetAllView(view)
        getDriverEarningData()
        //SetDateOurDate(orderearningList)


        return view
    }

    fun ongetAllView(view: View) {
        view.findViewById<BarChart>(com.example.letspaanidriver.R.id.barchart) as BarChart

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Payouts_Back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    fun getDriverEarningData() {
        val userid = UserShared.getUserID(context?.applicationContext!!)
        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getDriverAllEarning(
            "application/json",
            DriverEarningRequest(userid.toString())
        )
        call?.enqueue(object : Callback<DriverEarningResponse> {
            override fun onFailure(call: Call<DriverEarningResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<DriverEarningResponse>,
                response: Response<DriverEarningResponse>
            ) {
                val drivererrresponse = response.body()

                if (drivererrresponse?.zeroval != null) {

                    orderearningList = drivererrresponse.zeroval.order

                    if (drivererrresponse.zeroval.ratingEarning != 0) {
                        incentive = drivererrresponse.zeroval.ratingEarning
                        totincentive.text =
                            "₹ " + drivererrresponse.zeroval.ratingEarning.toString()
                        //totEarningsum.text =  "₹ " + (incentive + drivererrresponse.zeroval.earning).toString()
                    } else {
                        totincentive.text = "₹ 0"
                        //totEarningsum.text =  "₹ " + drivererrresponse.zeroval.earning.toString()
                    }

                    SetGraphDaily(orderearningList)// daily
                }

            }


        })
    }

    fun SetGraphDaily(orderearningList: List<orders>) {

        Log.i("dfef", "dsfwethrefjke====" + orderearningList)

        NoOfEmp.clear()
        labels.clear()
        var totaldayearn = 0
        Home.langval == 0

        labels.add(getCalculatedDate("d MMM", -6))
        labels.add(getCalculatedDate("d MMM", -5))
        labels.add(getCalculatedDate("d MMM", -4))
        labels.add(getCalculatedDate("d MMM", -3))
        labels.add(getCalculatedDate("d MMM", -2))
        labels.add(getCalculatedDate("d MMM", -1))
        labels.add(getCalculatedDate("d MMM", 0))

        val dateConvert0 = getCalculatedDate("yyyy-MM-dd", -6)
        val dateConvert1 = getCalculatedDate("yyyy-MM-dd", -5)
        val dateConvert2 = getCalculatedDate("yyyy-MM-dd", -4)
        val dateConvert3 = getCalculatedDate("yyyy-MM-dd", -3)
        val dateConvert4 = getCalculatedDate("yyyy-MM-dd", -2)
        val dateConvert5 = getCalculatedDate("yyyy-MM-dd", -1)
        val dateConvert6 = getCalculatedDate("yyyy-MM-dd", 0)


        var toterning1 = 0
        var toterning2 = 0
        var toterning3 = 0
        var toterning4 = 0
        var toterning5 = 0
        var toterning6 = 0
        var toterning7 = 0

        var sal = 0
        for (j in 0 until orderearningList.size) {

            val dat = orderearningList.get(j).created_at.split(" ")

            totaldayearn += orderearningList.get(j).amount.toFloat().toInt()

            Log.i("asdf", "datemetchingis==================" + dat + "============" + dateConvert0)

            when (dat[0]) {
                dateConvert0 -> toterning1 =
                    (toterning1 + orderearningList.get(j).amount.toFloat()).toInt()
                dateConvert1 -> toterning2 =
                    (toterning2 + orderearningList.get(j).amount.toFloat()).toInt()
                dateConvert2 -> toterning3 =
                    (toterning3 + orderearningList.get(j).amount.toFloat()).toInt()
                dateConvert3 -> toterning4 =
                    (toterning4 + orderearningList.get(j).amount.toFloat()).toInt()
                dateConvert4 -> toterning5 =
                    (toterning5 + orderearningList.get(j).amount.toFloat()).toInt()
                dateConvert5 -> toterning6 =
                    (toterning6 + orderearningList.get(j).amount.toFloat()).toInt()
                dateConvert6 -> toterning7 =
                    (toterning7 + orderearningList.get(j).amount.toFloat()).toInt()
                else -> println("Number too high")
            }
        }

        for (k in 0..labels.size - 1) {
            when (k) {
                0 -> NoOfEmp.add(BarEntry(toterning1.toFloat(), k))
                1 -> NoOfEmp.add(BarEntry(toterning2.toFloat(), k))
                2 -> NoOfEmp.add(BarEntry(toterning3.toFloat(), k))
                3 -> NoOfEmp.add(BarEntry(toterning4.toFloat(), k))
                4 -> NoOfEmp.add(BarEntry(toterning5.toFloat(), k))
                5 -> NoOfEmp.add(BarEntry(toterning6.toFloat(), k))
                6 -> NoOfEmp.add(BarEntry(toterning7.toFloat(), k))
                else -> println("Number too high")
            }
        }


        val barDataSet = BarDataSet(NoOfEmp, salary);

        val barData = BarData(labels, barDataSet)
        barchart.setPinchZoom(false)
        barchart.setDrawGridBackground(false)
        barchart.animateY(2000)
        barchart.setDescription("")
        barchart.data = barData
        barchart.setDrawBarShadow(false)
        barchart.setDrawValueAboveBar(true)


        totDelivery.text = orderearningList.size.toString()
        totearning.text = totaldayearn.toString()
        if (incentive == 0) {
            totEarningsum.text = "₹ " + totaldayearn.toString()
        } else {
            totEarningsum.text = "₹ " + (incentive + totaldayearn).toString()
        }
    }
    /*  fun SetGraphDaily(orderearningList: List<orders>) {

          NoOfEmp.clear()
          labels.clear()
          var totaldayearn = 0
          for (i in 7 - 1 downTo 0) {

              labels.add(getCalculatedDate("d MMM", -i))
              val dateConvert = getCalculatedDate("yyyy-MM-dd", -i)
              var sal = 0

              for (j in 0 until orderearningList.size) {

                  val dat = orderearningList.get(j).created_at.split(" ")

                  Log.i("tag", "bothdate----==========---" + dateConvert + "====" + dat[0])

                  if (dateConvert == dat[0]) {
                      sal = (sal + orderearningList.get(j).amount.toFloat()).toInt()
                  }
              }

              Log.i("rrrr", "position==========---====" + i + "sale==" + sal)

              NoOfEmp.add(BarEntry(sal.toFloat(), 6 - i))

              //NoOfEmp.add(BarEntry(1000f, 0))
          }

          val barDataSet = BarDataSet(NoOfEmp, "Salary")
          val barData = BarData(labels, barDataSet)

          barchart.setDrawBarShadow(false)
          barchart.setDrawValueAboveBar(true)
          // scaling can now only be done on x- and y-axis separately
          barchart.setPinchZoom(false)
          barchart.setDrawGridBackground(false)
          barchart.animateY(2000)
          barchart.setDescription("")
          barchart.data = barData


          for (i in 7 - 1 downTo 0) {

              val dateConvert = getCalculatedDate("yyyy-MM-dd", -i)

              for (j in 0 until orderearningList.size) {

                  val dat = orderearningList.get(j).created_at.split(" ")

                  if (dateConvert == dat[0]) {
                      totaldayearn += orderearningList.get(j).amount.toFloat().toInt()
                  }
              }
          }

          totDelivery.text = orderearningList.size.toString()
          totearning.text = totaldayearn.toString()
          if (incentive == 0){
              totEarningsum.text =  "₹ " + totaldayearn.toString()
          }else{
              totEarningsum.text =  "₹ " + (incentive + totaldayearn).toString()
          }


      }*/

    fun SetGraphMonthly(orderearningList: List<orders>) {
        NoOfEmp.clear()
        labels.clear()
        val localCalendar = Calendar.getInstance(TimeZone.getDefault())
        val currentMonth = localCalendar.get(Calendar.MONTH) + 1

        val currentyear = localCalendar.get(Calendar.YEAR)

        Log.i("asdf", "currentMonth======" + currentMonth)

        for (i in 1..currentMonth) {

            labels.add(getCalculatedMonth("MMM", i))

        }

        var toterning1 = 0
        var toterning2 = 0
        var toterning3 = 0
        var toterning4 = 0
        var toterning5 = 0
        var toterning6 = 0
        var toterning7 = 0
        var toterning8 = 0
        var toterning9 = 0
        var toterning10 = 0
        var toterning11 = 0
        var toterning12 = 0

        for (j in 0..orderearningList.size - 1) {

            val dat = orderearningList.get(j).created_at

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val parse = sdf.parse(dat)
            val calendar = Calendar.getInstance()
            calendar.setTime(parse)
            val date = calendar.get(Calendar.DATE)
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)

            if (currentyear == currentyear) {

                when (month) {
                    1 -> toterning1 =
                        (toterning1 + orderearningList.get(j).amount.toFloat()).toInt()
                    2 -> toterning2 =
                        (toterning2 + orderearningList.get(j).amount.toFloat()).toInt()
                    3 -> toterning3 =
                        (toterning3 + orderearningList.get(j).amount.toFloat()).toInt()
                    4 -> toterning4 =
                        (toterning4 + orderearningList.get(j).amount.toFloat()).toInt()
                    5 -> toterning5 =
                        (toterning5 + orderearningList.get(j).amount.toFloat()).toInt()
                    6 -> toterning6 =
                        (toterning6 + orderearningList.get(j).amount.toFloat()).toInt()
                    7 -> toterning7 =
                        (toterning7 + orderearningList.get(j).amount.toFloat()).toInt()
                    8 -> toterning8 =
                        (toterning8 + orderearningList.get(j).amount.toFloat()).toInt()
                    9 -> toterning9 =
                        (toterning9 + orderearningList.get(j).amount.toFloat()).toInt()
                    10 -> toterning10 =
                        (toterning10 + orderearningList.get(j).amount.toFloat()).toInt()
                    11 -> toterning11 =
                        (toterning11 + orderearningList.get(j).amount.toFloat()).toInt()
                    12 -> toterning12 =
                        (toterning12 + orderearningList.get(j).amount.toFloat()).toInt()
                    else -> println("Number too high")
                }

            }


        }

        for (k in 0..labels.size - 1) {
            when (k) {
                0 -> NoOfEmp.add(BarEntry(toterning1.toFloat(), k))
                1 -> NoOfEmp.add(BarEntry(toterning2.toFloat(), k))
                2 -> NoOfEmp.add(BarEntry(toterning3.toFloat(), k))
                3 -> NoOfEmp.add(BarEntry(toterning4.toFloat(), k))
                4 -> NoOfEmp.add(BarEntry(toterning5.toFloat(), k))
                5 -> NoOfEmp.add(BarEntry(toterning6.toFloat(), k))
                6 -> NoOfEmp.add(BarEntry(toterning7.toFloat(), k))
                7 -> NoOfEmp.add(BarEntry(toterning8.toFloat(), k))
                8 -> NoOfEmp.add(BarEntry(toterning9.toFloat(), k))
                9 -> NoOfEmp.add(BarEntry(toterning10.toFloat(), k))
                10 -> NoOfEmp.add(BarEntry(toterning11.toFloat(), k))
                11 -> NoOfEmp.add(BarEntry(toterning12.toFloat(), k))
                else -> println("Number too high")
            }
        }


        when (currentMonth - 1) {
            0 -> totearning.text = (toterning1).toString()
            1 -> totearning.text = (toterning2).toString()
            2 -> totearning.text = (toterning3).toString()
            3 -> totearning.text = (toterning4).toString()
            4 -> totearning.text = (toterning5).toString()
            5 -> totearning.text = (toterning6).toString()
            6 -> totearning.text = (toterning7).toString()
            7 -> totearning.text = (toterning8).toString()
            8 -> totearning.text = (toterning9).toString()
            9 -> totearning.text = (toterning10).toString()
            10 -> totearning.text = (toterning11).toString()
            11 -> totearning.text = (toterning12).toString()
            else -> println("Number too high")
        }


        val barDataSet = BarDataSet(NoOfEmp, salary)

        val barData = BarData(labels, barDataSet)

        barchart.setDrawBarShadow(false)
        barchart.setDrawValueAboveBar(true)

        // scaling can now only be done on x- and y-axis separately
        barchart.setPinchZoom(false)
        barchart.setDrawGridBackground(false)
        barchart.animateY(2000)
        barchart.setDescription("")
        barchart.data = barData
        totDelivery.text = orderearningList.size.toString()
    }

    fun SetGraphWeekly(orderearningList: List<orders>, start: Int, end: Int) {
        NoOfEmp.clear()
        labels.clear()

        val localCalendar = Calendar.getInstance(TimeZone.getDefault())
        val currentMonth = localCalendar.get(Calendar.MONTH) + 1

        if (Home.langval == 1) {
            labels.add("सोम.")
            labels.add("मंग.")
            labels.add("बुध")
            labels.add("गुरु.")
            labels.add("शुक्र.")
            labels.add("शनि.")
            labels.add("रवि.")
        } else {
            labels.add("Mon")
            labels.add("Tue")
            labels.add("Wed")
            labels.add("Thu")
            labels.add("Fri")
            labels.add("Sat")
            labels.add("Sun")
        }


        var toterningday1 = 0
        var toterningday2 = 0
        var toterningday3 = 0
        var toterningday4 = 0
        var toterningday5 = 0
        var toterningday6 = 0
        var toterningday7 = 0



        for (j in 0..orderearningList.size - 1) {

            val dat = orderearningList.get(j).created_at

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val parse = sdf.parse(dat)

            val calendar = getInstance()
            calendar.setTime(parse)

            val date = calendar.get(DATE)

            Log.i("asdf", "datedatedate======" + date)
            val month = calendar.get(MONTH) + 1

            val simpleDateformat = SimpleDateFormat("E")
            val day = simpleDateformat.format(parse)

            if (currentMonth == month) {

                if (date >= start && date <= end) {
                    when (day) {
                        "Mon" -> toterningday1 =
                            (toterningday1 + orderearningList.get(j).amount.toFloat()).toInt()
                        "Tue" -> toterningday2 =
                            (toterningday2 + orderearningList.get(j).amount.toFloat()).toInt()
                        "Wed" -> toterningday3 =
                            (toterningday3 + orderearningList.get(j).amount.toFloat()).toInt()
                        "Thu" -> toterningday4 =
                            (toterningday4 + orderearningList.get(j).amount.toFloat()).toInt()
                        "Fri" -> toterningday5 =
                            (toterningday5 + orderearningList.get(j).amount.toFloat()).toInt()
                        "Sat" -> toterningday6 =
                            (toterningday6 + orderearningList.get(j).amount.toFloat()).toInt()
                        "Sun" -> toterningday7 =
                            (toterningday7 + orderearningList.get(j).amount.toFloat()).toInt()
                        else -> println("Number too high")
                    }
                }
            }
        }

        for (k in 0..labels.size - 1) {
            when (k) {
                0 -> NoOfEmp.add(BarEntry(toterningday1.toFloat(), k))
                1 -> NoOfEmp.add(BarEntry(toterningday2.toFloat(), k))
                2 -> NoOfEmp.add(BarEntry(toterningday3.toFloat(), k))
                3 -> NoOfEmp.add(BarEntry(toterningday4.toFloat(), k))
                4 -> NoOfEmp.add(BarEntry(toterningday5.toFloat(), k))
                5 -> NoOfEmp.add(BarEntry(toterningday6.toFloat(), k))
                6 -> NoOfEmp.add(BarEntry(toterningday7.toFloat(), k))
                else -> println("Number too high")
            }
        }


        val barDataSet = BarDataSet(NoOfEmp, salary)

        val barData = BarData(labels, barDataSet)

        barchart.setDrawBarShadow(false)
        barchart.setDrawValueAboveBar(true)

        // scaling can now only be done on x- and y-axis separately
        barchart.setPinchZoom(false)

        barchart.setDrawGridBackground(false)

        barchart.animateY(2000)

        barchart.setDescription("")

        barchart.data = barData

        totDelivery.text = orderearningList.size.toString()
        totearning.text = (
                toterningday1 +
                        toterningday2 +
                        toterningday3 +
                        toterningday4 +
                        toterningday5 +
                        toterningday6 +
                        toterningday7
                ).toString()


    }


    fun countWeekendDays(year: Int, month: Int): Int {

        val now = Date()

        val simpleDateformat = SimpleDateFormat("E")
        // the day of the week abbreviated
        println(simpleDateformat.format(now))

        val sdf = SimpleDateFormat("EEEE")
        val d = Date()
        val dayOfTheWeek = sdf.format(d)


        val calendar = Calendar.getInstance()
        // Note that month is 0-based in calendar, bizarrely.
        calendar.set(year, month - 1, 1)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        var count = 0
        for (day in 1..daysInMonth) {
            calendar.set(year, month - 1, day)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek == Calendar.SUNDAY) {
                count++
                // Or do whatever you need to with the result.
            }
        }

        Log.i("start", "cal======================" + count)

        return count
    }

    private val df = SimpleDateFormat("yyyy/MM/dd")
    private fun printCalendar(c: Calendar): String {
        return df.format(c.getTime())
    }

    fun getCalculatedDate(dateFormat: String, days: Int): String {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.getTimeInMillis()))
    }

    fun getCalculatedMonth(dateFormat: String, days: Int): String {
        var mnt = ""
        if (Home.langval == 1) {
            if (days == 1) {
                mnt = "जन."
            } else if (days == 2) {
                mnt = "फ़र."
            } else if (days == 3) {
                mnt = "मार्च."
            } else if (days == 4) {
                mnt = "अप्रै."
            } else if (days == 5) {
                mnt = "मई."
            } else if (days == 6) {
                mnt = "जून."
            } else if (days == 7) {
                mnt = "जुला."
            } else if (days == 8) {
                mnt = "अग."
            } else if (days == 9) {
                mnt = "सित."
            } else if (days == 10) {
                mnt = "अक्टू."
            } else if (days == 11) {
                mnt = "नव."
            } else if (days == 12) {
                mnt = "दिस."
            }
        } else {
            if (days == 1) {
                mnt = "Jan"
            } else if (days == 2) {
                mnt = "Feb"
            } else if (days == 3) {
                mnt = "Mar"
            } else if (days == 4) {
                mnt = "Apr"
            } else if (days == 5) {
                mnt = "May"
            } else if (days == 6) {
                mnt = "Jun"
            } else if (days == 7) {
                mnt = "Jul"
            } else if (days == 8) {
                mnt = "Aug"
            } else if (days == 9) {
                mnt = "Sep"
            } else if (days == 10) {
                mnt = "Oct"
            } else if (days == 11) {
                mnt = "Nov"
            } else if (days == 12) {
                mnt = "Dec"
            }
        }


        return mnt
    }
}