package ecc.ie3a.suitou.ecounsel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ecc.ie3a.suitou.ecounsel.chat.ChatFragment
import ecc.ie3a.suitou.ecounsel.reservation_status.ReservationStatusFragment

var fragmentselect=0

class MainPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            //どのFragmentを表示するか
            0 -> ReservationStatusFragment()
            1 -> ReservationTabFragment()
            2 -> ProfileTabFragment()
//            3 -> ChatFragment()
            else -> ReservationStatusFragment()
        }
    }
}
