package com.example.zuizzsaeed_boggle
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GameplayFragment.OnGameplayInteractionListener, ScoreFragment.OnNewGameListener {

    private lateinit var scoreFragment: ScoreFragment
    private lateinit var gameplayFragment: GameplayFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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


    override fun updateScore(score: Int) {
        // Update score in the ScoreFragment
        scoreFragment.updateScore(score)
    }

    override fun onNewGame() {
        scoreFragment.resetScore() // This method will be added in ScoreFragment to reset the score
        gameplayFragment.newGame() // This method will be added in GameplayFragment to regenerate the grid and reset the words
    }
}