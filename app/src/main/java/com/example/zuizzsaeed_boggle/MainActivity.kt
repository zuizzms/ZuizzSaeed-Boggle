package com.example.zuizzsaeed_boggle
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GameplayFragment.OnGameplayInteractionListener {

    private lateinit var scoreFragment: ScoreFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add GameplayFragment to the activity
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutGameplay, GameplayFragment())
            .commit()

        // Add ScoreFragment to the activity
        scoreFragment = ScoreFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutScore, scoreFragment)
            .commit()
    }


    override fun updateScore(score: Int) {
        // Update score in the ScoreFragment
        scoreFragment.updateScore(score)
    }
}
