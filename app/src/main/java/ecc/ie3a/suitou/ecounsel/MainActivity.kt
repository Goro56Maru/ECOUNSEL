package ecc.ie3a.suitou.ecounsel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ecc.ie3a.suitou.ecounsel.chat.ChatFragment
import ecc.ie3a.suitou.ecounsel.databinding.ActivityMainBinding
import ecc.ie3a.suitou.ecounsel.reservation_status.ReservationStatusFragment

val TabArray = arrayOf("予約","予約状況","マイページ","チャット")


class MainActivity : AppCompatActivity() {

    private var hairetu= arrayOf(ReservationTabFragment(),ReservationStatusFragment(),ProfileTabFragment(),ChatFragment())

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewPager = binding.viewPager2
        val tabLayout = binding.tabLayout

        val number = intent.getStringExtra("num")?.toInt()

        Toast.makeText(applicationContext, number.toString(), Toast.LENGTH_LONG).show()

        val adapter = MainPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = TabArray[position]
        }.attach()

        viewPager.setCurrentItem(fragmentselect, false)

    }

}

