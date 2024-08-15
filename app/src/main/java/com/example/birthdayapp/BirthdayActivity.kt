package com.example.birthdayapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class BirthdayActivity : AppCompatActivity() {


    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthday)

        val username = intent.getStringExtra("USERNAME") ?: ""
        val birthDate = intent.getStringExtra("BIRTHDATE") ?: ""

        val daysUntilBirthday = calculateDaysUntilNextBirthday(birthDate)
        val notificationMessage = "There are still $daysUntilBirthday day(s) until your next birthday."
        val tvTitle: TextView = findViewById(R.id.textView)
        tvTitle.text = "Welcome，$username！"


        val birthdayMessage = "There are still $daysUntilBirthday day(s) until your next birthday."
        val birthdayFragment = BirthdayFragment.newInstance(birthdayMessage)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_fragment, birthdayFragment)
            commit()
        }

        showNotification(notificationMessage)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val birthDate = intent.getStringExtra("BIRTHDATE") ?: ""
        val daysUntilBirthday = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            calculateDaysUntilNextBirthday(birthDate)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val age = calculateAge(birthDate)

        when (item.itemId) {
            R.id.item1 -> {
                val birthdayMessage = "There are still $daysUntilBirthday day(s) until your next birthday."
                val birthdayFragment = BirthdayFragment.newInstance(birthdayMessage)
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.main_fragment, birthdayFragment)
                    commit()
                }

                showNotification(birthdayMessage)
            }
            R.id.item2 -> {
                val ageMessage = "You are already $age years old."
                val ageFragment = AgeFragment.newInstance(ageMessage)
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.main_fragment, ageFragment)
                    commit()
                }
                showNotification(ageMessage)
            }
        }
        return super.onOptionsItemSelected(item)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateAge(birthDateString: String): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val birthDate = LocalDate.parse(birthDateString, formatter)
        val currentDate = LocalDate.now()
        return ChronoUnit.YEARS.between(birthDate, currentDate).toInt()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDaysUntilNextBirthday(birthDateString: String): Long {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val birthDate = LocalDate.parse(birthDateString, formatter)

        val currentDate = LocalDate.now()
        var nextBirthday = birthDate.withYear(currentDate.year)

        if (nextBirthday.isBefore(currentDate) || nextBirthday.isEqual(currentDate)) {
            nextBirthday = nextBirthday.plusYears(1)
        }

        return ChronoUnit.DAYS.between(currentDate, nextBirthday)
    }

    private fun showNotification(message: String) {
        val notificationId = 101
        val channelId = "birthday_notification_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Birthday Notification",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = getColor(R.color.pink_500)
                enableVibration(true)
                description = "Notification for upcoming birthday"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_MUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_birthday_cake)
            .setContentTitle("Birthday notification")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        notificationManager.notify(notificationId, notification)
    }


}
