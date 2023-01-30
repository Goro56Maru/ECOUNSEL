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

val TabArray = arrayOf("予約状況","予約","マイページ")
val imageArray = arrayOf(R.drawable.reservation_status,R.drawable.reservation,R.drawable.mypage)

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewPager = binding.viewPager2
        val tabLayout = binding.tabLayout

        val adapter = MainPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = TabArray[position]
            tab.setIcon(imageArray[position])
        }.attach()

        viewPager.setCurrentItem(fragmentselect, false)
        
        viewPager.isUserInputEnabled = false

    }

}

