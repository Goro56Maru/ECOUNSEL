package ecc.ie3a.suitou.ecounsel.reservation

import android.R
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ecc.ie3a.suitou.ecounsel.databinding.FragmentReservationBinding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.DAY_OF_WEEK
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


class ReservationFragment : Fragment() {
    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!

    //firestore
    private val db = Firebase.firestore

    //表示するリストデータ
    private var ListView : MutableList<Counselor_Data> = ArrayList()
    //RecyclerView用のアダプター
    private val adapter = CounselorListAdapter(ListView)

    private var ScheduleView : MutableList<Schedule_Data> = ArrayList()
    private val adapter2 = ScheduleList_Adapter(ScheduleView)

    private var Select_Date = "2000/01/10"
    private var timeList = arrayListOf<String>("9:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00")

    private var selectCounselor = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentReservationBinding.inflate(inflater, container, false)
        val view = binding.root

        //カウンセラー表示の設定
        val linearLayoutManager = LinearLayoutManager(view.context,LinearLayoutManager.HORIZONTAL,false)
        binding.CounselorView.layoutManager = linearLayoutManager
        binding.CounselorView.adapter = adapter
        binding.CounselorView.setHasFixedSize(true)

        //時間表示の設定
        val linearLayoutManager2 = LinearLayoutManager(view.context,LinearLayoutManager.VERTICAL,false)
        binding.scheduleView.layoutManager = linearLayoutManager2
        binding.scheduleView.adapter = adapter2
        binding.scheduleView.setHasFixedSize(true)

        binding.CounselorView.visibility = android.widget.ListView.INVISIBLE
        ListView.clear()    //リスト表示前にデータを空にしておく

        //Firebaseに接続し、カウンセラーの情報を取得する
        db.collection("counselor").whereEqualTo("group","wyKJsGPFwUEjaVIuNfap").get().addOnSuccessListener {
            for (i in it){
                var setList = Counselor_Data(i.id, i.data["name"] as String,i.data["group"] as String,i.data["tastes"] as String,i.data["word"] as String,i.data["work"] as ArrayList<Map<String, *>>)
                ListView.add(setList)
                adapter.notifyDataSetChanged()
            }
            binding.CounselorView.visibility = android.widget.ListView.VISIBLE
        }.addOnFailureListener {
            Toast.makeText(activity,"データベース接続に失敗しました。", Toast.LENGTH_SHORT).show()
        }

        //カレンダーの表示設定
        var calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN)
        binding.calendarView.date = calendar.timeInMillis
        binding.calendarView.minDate = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 2)
        binding.calendarView.maxDate = calendar.timeInMillis

        var list_height = 0 //時間表示の高さを格納する
        val view2: LinearLayout = binding.scheduleList

        //時間表示の高さを取得する
        binding.scheduleList.post {
            list_height = binding.scheduleList.height // が返却される
        }

        //カレンダーの日付が選択された時
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val date = "$year/${month+1}/$dayOfMonth"
            Select_Date = date
            val calendar2: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN)
            calendar2.set(year, month, dayOfMonth, 12, 0, 50);
            val select_week = calendar2.get(DAY_OF_WEEK) - 1
            ScheduleView.clear()
            var bool = false

            //カウンセラーが選択されていなかった場合
            if (!selectCounselor.isNullOrEmpty()){
                for (i in ListView){
                    val map = i.work[select_week]
                    if(map["boolean"] as Boolean) {
                        bool = true
                    }
                }
            //カウンセラーが選択されていた場合
            }else{
                for (i in ListView){
                    if (i.CounselorID == selectCounselor){
                        val map = i.work[select_week]
                        if(map["boolean"] as Boolean) {
                            bool = true
                        }
                    }
                }
            }

            for (i in timeList){
                val setList = Schedule_Data(i, bool, selectCounselor)
                ScheduleView.add(setList)
            }
            adapter2.notifyDataSetChanged()
            ObjectAnimator.ofFloat(view2, "translationY", -list_height.toFloat()).apply {
                duration = 500
                start()
            }
            binding.calendarView.visibility = android.widget.CalendarView.INVISIBLE

        }

        //時間表示の閉じるボタンが押された時
        binding.closeButton.setOnClickListener {
            //時間表示を下げる
            ObjectAnimator.ofFloat(view2, "translationY", list_height.toFloat()).apply {
                duration = 500
                start()
            }
            //カレンダーを再表示
            binding.calendarView.visibility = android.widget.CalendarView.VISIBLE
            //時間表示をリセット
            ScheduleView.clear()
            adapter2.notifyDataSetChanged()
        }

        //カウンセラーが選択された時
        adapter.setOnItemClickListener(object :CounselorListAdapter.OnItemClickListener{
            override fun onItemClickListener(view: View, position: Int, clickedText: Counselor_Data) {
                if (selectCounselor == clickedText.CounselorID){
                    selectCounselor = ""
                }else{
                    selectCounselor = clickedText.CounselorID
                }

                //選択されたリストのID
                Toast.makeText(activity,"${clickedText.CounselorName}で絞り込みます", Toast.LENGTH_SHORT).show()
            }
        })

        //時間が選択された時
        adapter2.setOnItemClickListener(object :ScheduleList_Adapter.OnItemClickListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClickListener(view: View, position: Int, clickedText: Schedule_Data) {

                //選択されたリストのID
                Toast.makeText(activity,"${clickedText.Time}", Toast.LENGTH_SHORT).show()
            }
        })


        return view
    }

    companion object
}