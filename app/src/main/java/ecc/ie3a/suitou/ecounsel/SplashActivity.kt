package ecc.ie3a.suitou.ecounsel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import ecc.ie3a.suitou.ecounsel.login.LoginActivity


class SplashActivity : AppCompatActivity() {
    private var SPLASH_TIME:Long=4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        },SPLASH_TIME)

    }
}