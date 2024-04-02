package com.example.zuizzsaeed_boggle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt
import kotlin.math.pow
import android.content.Context

class MainActivity : AppCompatActivity(), GameplayFragment.OnGameplayInteractionListener, ScoreFragment.OnNewGameListener {

    private lateinit var scoreFragment: ScoreFragment
    private lateinit var gameplayFragment: GameplayFragment
    private lateinit var sensorManager: SensorManager
    private var shakeDetector: ShakeDetector? = null
    private var accelerometer: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize sensor manager and accelerometer
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector = ShakeDetector { onNewGame() }
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Add GameplayFragment to the activity
        gameplayFragment = GameplayFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutGameplay, gameplayFragment)
            .commit()

        // Add ScoreFragment to the activity
        scoreFragment = ScoreFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutScore, scoreFragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        shakeDetector?.also { detector ->
            sensorManager.registerListener(detector, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        shakeDetector?.also { detector ->
            sensorManager.unregisterListener(detector)
        }
    }

    private class ShakeDetector(private val onShake: () -> Unit) : SensorEventListener {
        private var lastTime: Long = 0
        private var lastX: Float = 0.0f
        private var lastY: Float = 0.0f
        private var lastZ: Float = 0.0f
        private val shakeThreshold = 600

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            val currentTime = System.currentTimeMillis()
            if ((currentTime - lastTime) > 100) {
                val x = event?.values?.get(0) ?: 0f
                val y = event?.values?.get(1) ?: 0f
                val z = event?.values?.get(2) ?: 0f

                val speed = Math.sqrt(((x - lastX).toDouble().pow(2.0) + (y - lastY).toDouble().pow(2.0) + (z - lastZ).toDouble().pow(2.0))) / (currentTime - lastTime) * 10000
                if (speed > shakeThreshold) {
                    onShake()
                }

                lastTime = currentTime
                lastX = x
                lastY = y
                lastZ = z
            }
        }
    }


    override fun updateScore(score: Int) {
        // Update score in the ScoreFragment
        scoreFragment.updateScore(score)
    }

    override fun onNewGame() {
        scoreFragment.resetScore() // This method will be added in ScoreFragment to reset the score
        gameplayFragment.newGame() // This method will be added in GameplayFragment to regenerate the grid and reset the words
    }
}
