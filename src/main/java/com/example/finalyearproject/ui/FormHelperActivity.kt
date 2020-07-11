package com.example.finalyearproject.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.finalyearproject.R
import kotlinx.android.synthetic.main.activity_form_helper.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.round


@Suppress("DEPRECATION")
@SuppressLint("Registered")
class FormHelperActivity : AppCompatActivity(), SensorEventListener {
    lateinit var sensorManager: SensorManager
    private var position = arrayOf(200.2, 200.2, 200.2)
    private var startingPosition = arrayOf(200.2, 200.2, 200.2)
    private var endingPosition = arrayOf(200.2, 200.2, 200.2)

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_helper)
        //button and vibrator declaration
        val startBtn = findViewById<Button>(R.id.startBtn)
        val endBtn = findViewById<Button>(R.id.endBtn)
        val guideBtn = findViewById<Button>(R.id.guideBtn)

        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        //instance of sensor manager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //defining sensor

        //button click
        startBtn.setOnClickListener {
            sensorManager.registerListener(
                this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
            )
            Toast.makeText(this@FormHelperActivity, "Start exercise after vibration", Toast.LENGTH_LONG)
                .show()
            Timer("GetReady", false).schedule(5000) {
                /* if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator.vibrate(1000)
                }*/
                startingPosition[0] = position[0]
                startingPosition[1] = position[1]
                startingPosition[2] = position[2]
                this@FormHelperActivity.runOnUiThread {starting_rotation_data.text = "x = ${startingPosition[0].round(1)}\n" +
                        "y = ${startingPosition[1].round(0)}\n" +
                        "z = ${startingPosition[2].round(1)}\n"}

                Timer("ReturnToStartPosition", false).schedule(5000) {
                    this@FormHelperActivity.runOnUiThread {
                        Toast.makeText(
                            this@FormHelperActivity,
                            "Go back to starting position",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    /* if (Build.VERSION.SDK_INT >= 26) {
                         vibrator.vibrate(
                             VibrationEffect.createOneShot(
                                 1000,
                                 VibrationEffect.DEFAULT_AMPLITUDE
                             )
                         )
                     } else {
                         vibrator.vibrate(1000)
                     }*/
                    endingPosition[0] = position[0]
                    endingPosition[1] = position[1]
                    endingPosition[2] = position[2]
                    this@FormHelperActivity.runOnUiThread {ending_rotation_data.text = "x = ${endingPosition[0].round(1)}\n" +
                            "y = ${endingPosition[1].round(0)}\n" +
                            "z = ${endingPosition[2].round(1)}\n"}

                    this@FormHelperActivity.runOnUiThread {
                        Toast.makeText(
                            this@FormHelperActivity,
                            "Ending position" + endingPosition[0],
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

            }
        }
        endBtn.setOnClickListener{
            sensorManager.unregisterListener(this)
            starting_rotation_data.text = "Starting Position"
            ending_rotation_data.text = "Ending Position"
            rotation_data.text = "Rotation"
        }
        guideBtn.setOnClickListener {
            val intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        starting_rotation_data.text = getString(R.string.starting_position)
        ending_rotation_data.text = getString(R.string.ending_position)
        rotation_data.text = getString(R.string.rotation)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        this.position[0] = event!!.values[0].toDouble().round(1)
        this.position[1] = event.values[1].toDouble().round(0)
        this.position[2] = event.values[2].toDouble().round(1)

        rotation_data.text = "x = ${event.values[0].toDouble().round(1)}\n" +
                "y = ${event.values[1].toDouble().round(0)}\n" +
                "z = ${event.values[2].toDouble().round(1)}\n"
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (startingPosition[1].round(0) == event.values[1].toDouble().round(
                0
            )
        ) {
            Toast.makeText(this@FormHelperActivity, "This is the start position", Toast.LENGTH_LONG)
                .show()
            if (Build.VERSION.SDK_INT >= 26) {
                this.position[0] = event.values[0].toDouble().round(0)
                this.position[1] = event.values[1].toDouble().round(0)
                this.position[2] = event.values[2].toDouble().round(0)
                Timer("delay on vibrate", false).schedule(100) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            100,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )}
            } else {
                this.position[0] = event.values[0].toDouble().round(0)
                this.position[1] = event.values[1].toDouble().round(0)
                this.position[2] = event.values[2].toDouble().round(0)

                Timer("delay on vibrate", false).schedule(100) {
                    vibrator.vibrate(100)
                }
            }


        }
        if (endingPosition[1].round(0) == event.values[1].toDouble().round(
                0
            )
        ) {
            Toast.makeText(this@FormHelperActivity, "This is the end position", Toast.LENGTH_LONG)
                .show()
            if (Build.VERSION.SDK_INT >= 26) {
                this.position[0] = event.values[0].toDouble().round(0)
                this.position[1] = event.values[1].toDouble().round(0)
                this.position[2] = event.values[2].toDouble().round(0)
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        100,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )


            } else {
                this.position[0] = event.values[0].toDouble().round(0)
                this.position[1] = event.values[1].toDouble().round(0)
                this.position[2] = event.values[2].toDouble().round(0)
                vibrator.vibrate(100)

            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}