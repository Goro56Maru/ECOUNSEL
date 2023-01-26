package ecc.ie3a.suitou.ecounsel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ecc.ie3a.suitou.ecounsel.chat.ChatFragment
import ecc.ie3a.suitou.ecounsel.databinding.ActivityLoginBinding
import ecc.ie3a.suitou.ecounsel.databinding.ActivitySelectBinding
import ecc.ie3a.suitou.ecounsel.login.SignUpActivity
import ecc.ie3a.suitou.ecounsel.reservation_status.ReservationStatusFragment

class SelectActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.reservationButton.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            fragmentselect=0
            startActivity(intent)
        }

        binding.reservationStatusButton.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            fragmentselect=1
            startActivity(intent)
        }

        binding.mypageButton.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            fragmentselect=2
            startActivity(intent)
        }

        binding.chatButton.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            fragmentselect=3
            startActivity(intent)
        }

    }
}