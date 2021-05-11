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
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.format.DateFormat
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.letspaanidriver.activity.Home
import com.example.letspaanidriver.activity.LogIn
import com.google.android.libraries.places.internal.of
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.bank_detail.*
import kotlinx.android.synthetic.main.fragment_ratings.*
import kotlinx.android.synthetic.main.payouts.barchart
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Calendar.*


class Ratings : Fragment() {

    val labels = ArrayList<String>()
    val NoOfEmp = ArrayList<BarEntry>()

    var ratingList: List<AllRating> = ArrayList<AllRating>()
    var salary=""

    val profileResponse = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(
            com.example.letspaanidriver.R.layout.fragment_ratings,
            container,
            false
        )

        if(Home.langval==1)
        {
            salary="रेटिंग"
        }
        else{
            salary="Ratings"
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

        getDriverRatingData()

        txtDailly.setOnClickListener {
            SetGraphDaily(ratingList)
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

            Log.i("da", "totaldayofmonth=======" + datedays)

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

            SetGraphWeekly(ratingList, strtrat, endrat)
        }
        txtMonthly.setOnClickListener {

            txtMonthly.setBackgroundColor(Color.parseColor("#74B8D6"))
            txtDailly.setBackgroundColor(Color.parseColor("#ffffff"))
            txtweekly.setBackgroundColor(Color.parseColor("#ffffff"))

            layWeek.visibility = View.GONE
            SetGraphMonthly(ratingList)
        }



        week1.setOnClickListener {
            SetGraphWeekly(ratingList, 1, 7)
            week1.setBackgroundColor(Color.parseColor("#76D4FC"))
            week5.setBackgroundColor(Color.parseColor("#ffffff"))
            week2.setBackgroundColor(Color.parseColor("#ffffff"))
            week3.setBackgroundColor(Color.parseColor("#ffffff"))
            week4.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        week2.setOnClickListener {
            SetGraphWeekly(ratingList, 8, 14)
            week2.setBackgroundColor(Color.parseColor("#76D4FC"))
            week5.setBackgroundColor(Color.parseColor("#ffffff"))
            week1.setBackgroundColor(Color.parseColor("#ffffff"))
            week3.setBackgroundColor(Color.parseColor("#ffffff"))
            week4.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        week3.setOnClickListener {
            SetGraphWeekly(ratingList, 15, 21)
            week3.setBackgroundColor(Color.parseColor("#76D4FC"))
            week5.setBackgroundColor(Color.parseColor("#ffffff"))
            week2.setBackgroundColor(Color.parseColor("#ffffff"))
            week1.setBackgroundColor(Color.parseColor("#ffffff"))
            week4.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        week4.setOnClickListener {
            SetGraphWeekly(ratingList, 22, 28)
            week4.setBackgroundColor(Color.parseColor("#76D4FC"))
            week5.setBackgroundColor(Color.parseColor("#ffffff"))
            week2.setBackgroundColor(Color.parseColor("#ffffff"))
            week3.setBackgroundColor(Color.parseColor("#ffffff"))
            week1.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        week5.setOnClickListener {
            SetGraphWeekly(ratingList, 29, 31)
            week5.setBackgroundColor(Color.parseColor("#76D4FC"))
            week1.setBackgroundColor(Color.parseColor("#ffffff"))
            week2.setBackgroundColor(Color.parseColor("#ffffff"))
            week3.setBackgroundColor(Color.parseColor("#ffffff"))
            week4.setBackgroundColor(Color.parseColor("#ffffff"))
        }


        ongetAllView(view)
        //SetDateOurDate(ratingList)


        return view
    }

    fun ongetAllView(view: View) {
        view.findViewById<BarChart>(com.example.letspaanidriver.R.id.barchart) as BarChart

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Ratings_Back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    fun getDriverRatingData() {
        val userid = UserShared.getUserID(context?.applicationContext!!)!!
        val myApplicationInterface =
            AppController.instance?.requestQueue?.create(UserService::class.java)
        val call = myApplicationInterface?.getUserRating(
            "application/json",
            DriverRatingRequest(userid)
        )
        call?.enqueue(object : Callback<DriverRatingResponse> {
            override fun onFailure(call: Call<DriverRatingResponse>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<DriverRatingResponse>,
                response: Response<DriverRatingResponse>
            ) {
                val driverResponse = response.body()

                Log.i("asd", "response======" + response.body())
                if (driverResponse?.allRatings != null) {
                    ratingList = driverResponse!!.allRatings
                    Log.i("asd", "responsearrsize======" + ratingList.size)

                    SetGraphDaily(ratingList)// daily

                }
            }

        })
    }

    fun SetGraphDaily(ratingList: List<AllRating>?) {

        NoOfEmp.clear()
        labels.clear()
        var totaldayearn = 0

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


        var dlyevg1 = 0
        var dlyevg2 = 0
        var dlyevg3 = 0
        var dlyevg4 = 0
        var dlyevg5 = 0
        var dlyevg6 = 0
        var dlyevg7 = 0


        var totalratingDailly = 0


        for (j in 0 until ratingList!!.size) {

            val dat = ratingList.get(j).created_at.split(" ")


            totaldayearn += ratingList.get(j).rating.toFloat().toInt()

            Log.i("asdf", "datemetchingis==================" + dat + "============" + dateConvert0)

            when (dat[0]) {
                dateConvert0 -> {
                    toterning1 = (toterning1 + ratingList.get(j).rating.toFloat()).toInt()
                    dlyevg1 = dlyevg1 + 1

                    totalratingDailly = totalratingDailly + 1
                }
                dateConvert1 -> {
                    toterning2 = (toterning2 + ratingList.get(j).rating.toFloat()).toInt()
                    dlyevg2 = dlyevg2 + 1
                    totalratingDailly = totalratingDailly + 1
                }
                dateConvert2 -> {
                    toterning3 = (toterning3 + ratingList.get(j).rating.toFloat()).toInt()
                    dlyevg3 = dlyevg3 + 1
                    totalratingDailly = totalratingDailly + 1
                }
                dateConvert3 -> {
                    toterning4 = (toterning4 + ratingList.get(j).rating.toFloat()).toInt()
                    dlyevg4 = dlyevg4 + 1
                    totalratingDailly = totalratingDailly + 1
                }
                dateConvert4 -> {
                    toterning5 = (toterning5 + ratingList.get(j).rating.toFloat()).toInt()
                    dlyevg5 = dlyevg5 + 1
                    totalratingDailly = totalratingDailly + 1
                }
                dateConvert5 -> {
                    toterning6 = (toterning6 + ratingList.get(j).rating.toFloat()).toInt()
                    dlyevg6 = dlyevg6 + 1
                    totalratingDailly = totalratingDailly + 1
                }
                dateConvert6 -> {
                    toterning7 = (toterning7 + ratingList.get(j).rating.toFloat()).toInt()
                    dlyevg7 = dlyevg7 + 1
                    totalratingDailly = totalratingDailly + 1
                }
                else -> println("Number too high")
            }

        }

        for (k in 0..labels.size - 1) {
            when (k) {
                0 -> {

                    if (dlyevg1 != 0) {
                        NoOfEmp.add(BarEntry(toterning1.toFloat() / dlyevg1, k))
                    } else {
                        NoOfEmp.add(BarEntry(dlyevg1.toFloat(), k))
                    }


                }
                1 -> {
                    if (dlyevg2 != 0) {
                        NoOfEmp.add(BarEntry(toterning2.toFloat() / dlyevg2, k))
                    } else {
                        NoOfEmp.add(BarEntry(dlyevg2.toFloat(), k))
                    }
                }
                2 -> {

                    if (dlyevg3 != 0) {
                        NoOfEmp.add(BarEntry(toterning3.toFloat() / dlyevg3, k))
                    } else {
                        NoOfEmp.add(BarEntry(dlyevg3.toFloat(), k))
                    }


                }
                3 -> {

                    if (dlyevg4 != 0) {
                        NoOfEmp.add(BarEntry(toterning4.toFloat() / dlyevg4, k))
                    } else {
                        NoOfEmp.add(BarEntry(dlyevg4.toFloat(), k))
                    }

                }
                4 -> {

                    if (dlyevg5 != 0) {
                        NoOfEmp.add(BarEntry(toterning4.toFloat() / dlyevg5, k))
                    } else {
                        NoOfEmp.add(BarEntry(dlyevg5.toFloat(), k))
                    }

                }
                5 -> {

                    if (dlyevg6 != 0) {
                        NoOfEmp.add(BarEntry(toterning6.toFloat() / dlyevg6, k))
                    } else {
                        NoOfEmp.add(BarEntry(dlyevg6.toFloat(), k))
                    }

                }
                6 -> {
                    if (dlyevg7 != 0) {
                        NoOfEmp.add(BarEntry(toterning7.toFloat() / dlyevg7, k))
                    } else {
                        NoOfEmp.add(BarEntry(dlyevg7.toFloat(), k))
                    }
                }
                else -> println("Number too high")
            }
        }
           // var salary= "salary"

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


        ratingAvg.rating = (toterning1 + toterning2 + toterning3 + toterning4 + toterning5 + toterning6 + toterning7).toFloat() / totalratingDailly

        // totDelivery.text = ratingList.size.toString()
        //  totearning.text = totaldayearn.toString()
        /*  if (incentive == 0){
              totEarningsum.text =  "₹ " + totaldayearn.toString()
          }else{
              totEarningsum.text =  "₹ " + (incentive + totaldayearn).toString()
          }*/


    }


    /*  fun SetGraphDaily(ratingList: List<AllRating>?) {

          NoOfEmp.clear()
          labels.clear()


          for (i in 7 - 1 downTo 0) {

              labels.add(getCalculatedDate("d MMM", -i))
              val dateConvert = getCalculatedDate("yyyy-MM-dd", -i)

              var sal = 0
              var totratcount = 0

              for (j in 0 until ratingList?.size as Int) {

                  val dat = ratingList.get(j).created_at.split(" ")


                  Log.i("tag", "bothdate----==========---" + dateConvert + "====" + dat[0])

                  if (dateConvert == dat[0]) {
                      sal = (sal + ratingList.get(j).rating.toFloat()).toInt()
                      totratcount = totratcount + 1
                  }

              }

              Log.i("rrrr", "position==========---====" + i + "sale==" + sal)


              if (totratcount != 0) {
                  NoOfEmp.add(BarEntry(sal.toFloat() / totratcount.toFloat(), 6 - i))
              } else {
                  NoOfEmp.add(BarEntry(sal.toFloat(), 6 - i))
              }


              // NoOfEmp.add(BarEntry(1000f, 0))

          }


          val barDataSet = BarDataSet(NoOfEmp, "Rating")

          val barData = BarData(labels, barDataSet)

          barchart.setDrawBarShadow(false)
          barchart.setDrawValueAboveBar(true)

          // scaling can now only be done on x- and y-axis separately
          barchart.setPinchZoom(false)

          barchart.setDrawGridBackground(false)

          barchart.animateY(2000)

          barchart.setDescription("")

          barchart.data = barData

      }*/

    fun SetGraphMonthly(ratingList: List<AllRating>?) {
        NoOfEmp.clear()
        labels.clear()
        val localCalendar = Calendar.getInstance(TimeZone.getDefault())
        val currentMonth = localCalendar.get(Calendar.MONTH) + 1

        val currentyear = localCalendar.get(Calendar.YEAR)

        Log.i("asdf", "currentMonth======" + currentyear)

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


        var totratcount1 = 0
        var totratcount2 = 0
        var totratcount3 = 0
        var totratcount4 = 0
        var totratcount5 = 0
        var totratcount6 = 0
        var totratcount7 = 0
        var totratcount8 = 0
        var totratcount9 = 0
        var totratcount10 = 0
        var totratcount11 = 0
        var totratcount12 = 0


        var totcurrentmonthrating = 0
        var totcountcurrentrating = 0

        for (j in 0 until ratingList!!.size) {

            val dat = ratingList[j].created_at

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val parse = sdf.parse(dat)
            val calendar = Calendar.getInstance()
            calendar.setTime(parse)
            val date = calendar.get(Calendar.DATE)
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)

            if (currentyear == currentyear) {

                Log.i("asdf", "currentmonth==================" + currentMonth + "====" + month)
                if (currentMonth == month) {
                    totcurrentmonthrating =
                        (totcurrentmonthrating + ratingList.get(j).rating.toFloat()).toInt()
                    totcountcurrentrating++


                }


                when (month) {


                    1 -> {

                        toterning1 = (toterning1 + ratingList.get(j).rating.toFloat()).toInt()
                        totratcount1++


                    }


                    2 -> {
                        toterning2 = (toterning2 + ratingList.get(j).rating.toFloat()).toInt()
                        totratcount2++


                    }


                    3 -> {
                        toterning3 = (toterning3 + ratingList.get(j).rating.toFloat()).toInt()
                        totratcount3++


                    }

                    4 -> {
                        toterning4 = (toterning4 + ratingList.get(j).rating.toFloat()).toInt()
                        totratcount4++


                    }

                    5 -> {
                        toterning5 = (toterning5 + ratingList.get(j).rating.toFloat()).toInt()
                        totratcount5++


                    }
                    6 -> {
                        toterning6 = (toterning6 + ratingList.get(j).rating.toFloat()).toInt()
                        totratcount6++


                    }
                    7 -> {
                        toterning7 = (toterning7 + ratingList.get(j).rating.toFloat()).toInt()
                        totratcount7++


                    }
                    8 -> {
                        toterning8 = (toterning8 + ratingList.get(j).rating.toFloat()).toInt()
                        totratcount8++


                    }
                    9 -> {
                        toterning9 = (toterning9 + ratingList.get(j).rating.toFloat()).toInt()
                        totratcount9++


                    }
                    10 -> {
                        toterning10 = (toterning10 + ratingList.get(j).rating.toFloat()).toInt()
                        totratcount10++

                    }
                    11 -> {
                        toterning11 = (toterning11 + ratingList.get(j).rating.toFloat()).toInt()
                        totratcount11++

                    }
                    12 -> {
                        toterning12 = (toterning12 + ratingList.get(j).rating.toFloat()).toInt()
                        totratcount12++


                    }

                    else -> println("Number too high")
                }

            }


        }

        for (k in 0..labels.size - 1) {
            when (k) {
                /* 0 -> NoOfEmp.add(BarEntry(toterning1.toFloat(), k))
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
                 else -> println("Number too high")*/

                0 -> {
                    if (totratcount1 != 0) {
                        NoOfEmp.add(BarEntry(toterning1.toFloat() / totratcount1, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterning1.toFloat(), k))
                    }

                }


                1 -> {
                    if (totratcount2 != 0) {
                        NoOfEmp.add(BarEntry(toterning2.toFloat() / totratcount2, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterning2.toFloat(), k))
                    }

                }


                2 -> {

                    if (totratcount3 != 0) {
                        NoOfEmp.add(BarEntry(toterning3.toFloat() / totratcount3, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterning3.toFloat(), k))
                    }

                }


                3 -> {
                    if (totratcount4 != 0) {
                        NoOfEmp.add(BarEntry(toterning4.toFloat() / totratcount4, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterning4.toFloat(), k))
                    }

                }

                4 -> {
                    if (totratcount5 != 0) {
                        NoOfEmp.add(BarEntry(toterning5.toFloat() / totratcount5, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterning5.toFloat(), k))
                    }

                }

                5 -> {
                    if (totratcount6 != 0) {
                        NoOfEmp.add(BarEntry(toterning6.toFloat() / totratcount6, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterning6.toFloat(), k))
                    }

                }
                6 -> {
                    if (totratcount7 != 0) {
                        NoOfEmp.add(BarEntry(toterning7.toFloat() / totratcount7, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterning7.toFloat(), k))
                    }


                }
                7 -> {
                    if (totratcount8 != 0) {
                        NoOfEmp.add(BarEntry(toterning8.toFloat() / totratcount8, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterning8.toFloat(), k))
                    }

                }
                8 -> {
                    if (totratcount9 != 0) {
                        NoOfEmp.add(BarEntry(toterning9.toFloat() / totratcount9, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterning9.toFloat(), k))
                    }

                }
                9 -> {
                    if (totratcount10 != 0) {
                        NoOfEmp.add(BarEntry(toterning10.toFloat() / totratcount10, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterning10.toFloat(), k))
                    }

                }
                10 -> {
                    if (totratcount11 != 0) {
                        NoOfEmp.add(BarEntry(toterning11.toFloat() / totratcount11, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterning11.toFloat(), k))
                    }

                }
                11 -> {
                    if (totratcount12 != 0) {
                        NoOfEmp.add(BarEntry(toterning12.toFloat() / totratcount12, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterning12.toFloat(), k))
                    }

                }


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

        ratingAvg.rating = (totcurrentmonthrating).toFloat() / totcountcurrentrating


    }

    fun SetGraphWeekly(ratingList: List<AllRating>?, start: Int, end: Int) {
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

        var totratcount1 = 0
        var totratcount2 = 0
        var totratcount3 = 0
        var totratcount4 = 0
        var totratcount5 = 0
        var totratcount6 = 0
        var totratcount7 = 0


        var totcount = 0

        for (j in 0..ratingList!!.size - 1) {

            val dat = ratingList.get(j).created_at

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val parse = sdf.parse(dat)

            val calendar = Calendar.getInstance()
            calendar.setTime(parse)

            val date = calendar.get(Calendar.DATE)

            Log.i("asdf", "datedatedate======" + date)
            val month = calendar.get(Calendar.MONTH) + 1

            val simpleDateformat = SimpleDateFormat("E")
            val day = simpleDateformat.format(parse)


            if (currentMonth == month) {

                if (date >= start && date <= end) {
                    when (day) {
                        "Mon" -> {
                            toterningday1 =
                                (toterningday1 + ratingList.get(j).rating.toFloat()).toInt()
                            totratcount1++
                            totcount++
                        }
                        "Tue" -> {
                            toterningday2 =
                                (toterningday2 + ratingList.get(j).rating.toFloat()).toInt()
                            totratcount2++
                            totcount++
                        }
                        "Wed" -> {
                            toterningday3 =
                                (toterningday3 + ratingList.get(j).rating.toFloat()).toInt()
                            totratcount3++
                            totcount++
                        }
                        "Thu" -> {
                            toterningday4 =
                                (toterningday4 + ratingList.get(j).rating.toFloat()).toInt()
                            totratcount4++
                            totcount++
                        }
                        "Fri" -> {
                            toterningday5 =
                                (toterningday5 + ratingList.get(j).rating.toFloat()).toInt()
                            totratcount5++
                            totcount++
                        }
                        "Sat" -> {
                            toterningday6 =
                                (toterningday6 + ratingList.get(j).rating.toFloat()).toInt()
                            totratcount6++
                            totcount++
                        }
                        "Sun" -> {
                            toterningday7 =
                                (toterningday7 + ratingList.get(j).rating.toFloat()).toInt()
                            totratcount7++
                            totcount++
                        }
                        else -> println("Number too high")
                    }
                }
            }
        }

        for (k in 0..labels.size - 1) {
            when (k) {
                0 -> {
                    if (totratcount1 != 0) {
                        NoOfEmp.add(BarEntry(toterningday1.toFloat() / totratcount1, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterningday1.toFloat(), k))
                    }
                }
                1 -> {
                    if (totratcount2 != 0) {
                        NoOfEmp.add(BarEntry(toterningday2.toFloat() / totratcount2, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterningday2.toFloat(), k))
                    }
                }
                2 -> {
                    if (totratcount3 != 0) {
                        NoOfEmp.add(BarEntry(toterningday3.toFloat() / totratcount3, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterningday3.toFloat(), k))
                    }
                }
                3 -> {
                    if (totratcount4 != 0) {
                        NoOfEmp.add(BarEntry(toterningday4.toFloat() / totratcount4, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterningday4.toFloat(), k))
                    }
                }
                4 -> {
                    if (totratcount5 != 0) {
                        NoOfEmp.add(BarEntry(toterningday5.toFloat() / totratcount5, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterningday5.toFloat(), k))
                    }
                }
                5 -> {
                    if (totratcount6 != 0) {
                        NoOfEmp.add(BarEntry(toterningday6.toFloat() / totratcount6, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterningday6.toFloat(), k))
                    }
                }
                6 -> {
                    if (totratcount7 != 0) {
                        NoOfEmp.add(BarEntry(toterningday7.toFloat() / totratcount7, k))
                    } else {
                        NoOfEmp.add(BarEntry(toterningday7.toFloat(), k))
                    }
                }
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

        ratingAvg.rating =
            (toterningday1 + toterningday2 + toterningday3 + toterningday4 + toterningday5 + toterningday6 + toterningday7).toFloat() / totcount


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