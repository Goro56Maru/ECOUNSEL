package ecc.ie3a.suitou.ecounsel.reservation

import android.R
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ecc.ie3a.suitou.ecounsel.databinding.FragmentReservationBinding
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
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

    private var ReservationData : MutableList<Reservation_Data> = ArrayList()

    private var Select_Date = "2000/01/10"
    private var Select_Date_Text = ""
    private var timeList = arrayListOf<String>("9:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00")
    private var weekList = arrayListOf<String>("(日)","(月)","(火)","(水)","(木)","(金)","(土)")

    private var counselor_hobby = ""
    private var counselor_word = ""
    private var selectCounselor = ""
    private var selectCounselor_name = ""
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

        var list_height = 0 //時間表示の高さを格納する
        val view2: LinearLayout = binding.scheduleList

        //時間表示の高さを取得する
        binding.scheduleList.post {
            list_height = binding.scheduleList.height // が返却される
        }

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

        //カレンダーの表示設定
        var calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN)
        binding.calendarView.date = calendar.timeInMillis

        var calendar_now = "${calendar.get(YEAR)}/${calendar.get(MONTH)+1}/${calendar.get(DATE)}/"
        binding.calendarView.minDate = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 2)
        binding.calendarView.maxDate = calendar.timeInMillis

        binding.calendarView.visibility = android.widget.CalendarView.INVISIBLE
        binding.linearLayoutIntroduction.visibility = LinearLayout.INVISIBLE
        binding.CounselorView.visibility = LinearLayout.INVISIBLE

        //Firebaseに接続し、カウンセラーの情報を取得する
        db.collection("counselor").whereEqualTo("group","wyKJsGPFwUEjaVIuNfap").get().addOnSuccessListener {
            for (i in it){
                var setList = Counselor_Data(i.id, i.data["name"] as String,i.data["group"] as String,i.data["tastes"] as String,i.data["word"] as String,i.data["work"] as ArrayList<Map<String, *>>)
                ListView.add(setList)
                adapter.notifyDataSetChanged()
            }
            ReservationData.clear()
            //Firebaseに接続し、予約表の情報を取得する
            db.collection("reservation").whereEqualTo("group","wyKJsGPFwUEjaVIuNfap").get().addOnSuccessListener {
                for (i in it){
                    var setList = Reservation_Data(i.id,i.data["counselor"]as String,i.data["timestamp"] as String)
                    ReservationData.add(setList)
                }

                binding.progressBar3.visibility = android.widget.ProgressBar.INVISIBLE
                binding.CounselorView.visibility = android.widget.ListView.VISIBLE
                binding.linearLayoutIntroduction.visibility = android.widget.LinearLayout.VISIBLE
                binding.calendarView.visibility = android.widget.CalendarView.VISIBLE
            }.addOnFailureListener {
                Toast.makeText(activity,"データベース接続に失敗しました。", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(activity,"データベース接続に失敗しました。", Toast.LENGTH_SHORT).show()
        }


        //カレンダーの日付が選択された時
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            val date = "$year/${month+1}/$dayOfMonth/"
            Select_Date = date
            val calendar2: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN)
            calendar2.set(year, month, dayOfMonth, 12, 0, 50);
            val select_week = calendar2.get(DAY_OF_WEEK) - 1
            val text_date = "${year}年 ${month+1}月 ${dayOfMonth}日 ${weekList[select_week]} "
            Select_Date_Text = text_date
            ScheduleView.clear()
            adapter2.notifyDataSetChanged()
            var bool = false

            //カウンセラーが選択されていなかった場合
            if (selectCounselor.isNullOrEmpty()){
                Toast.makeText(activity,"カウンセラーを選択してください" , Toast.LENGTH_SHORT).show()

//                for (i in ListView){
//                    val map = i.work[select_week]
//                    if(map["boolean"] as Boolean) {
//                        bool = true
//                    }
//                }
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

                //予約時間の表示
                for (i in timeList){
                    var str = date + i
                    var setList: Schedule_Data
                    Log.d("TAG", "$str")
                    Log.d("TAG", ReservationData.toString())


//                Toast.makeText(activity,"$str", Toast.LENGTH_SHORT).show()
                    if(ReservationData.any{ it.TimeStamp == str}){
                        Log.d("TAG", "とおってます")
                        setList = Schedule_Data(i, false, selectCounselor,i)
                    }
                    else{
                        setList = Schedule_Data(i, bool, selectCounselor,i)
                    }
                    //データを挿入
                    ScheduleView.add(setList)
                }
                adapter2.notifyDataSetChanged()

                binding.CounselorView.visibility = android.widget.ListView.INVISIBLE
                ObjectAnimator.ofFloat(view2, "translationY", -list_height.toFloat()).apply {
                    duration = 500
                    start()
                }
                binding.CounselorView.visibility = android.widget.ListView.INVISIBLE
                binding.calendarView.visibility = android.widget.CalendarView.INVISIBLE
                binding.linearLayoutIntroduction.visibility = LinearLayout.INVISIBLE
            }
        }

        //時間表示の閉じるボタンが押された時
        binding.closeButton.setOnClickListener {
            //時間表示を下げる
            ObjectAnimator.ofFloat(view2, "translationY", list_height.toFloat()).apply {
                duration = 500
                start()
            }
            //カレンダーを再表示
            binding.linearLayoutIntroduction.visibility = LinearLayout.VISIBLE
            binding.calendarView.visibility = android.widget.CalendarView.VISIBLE
            binding.CounselorView.visibility = android.widget.ListView.VISIBLE
            //時間表示をリセット
            ScheduleView.clear()
            adapter2.notifyDataSetChanged()
        }

        //カウンセラーが選択された時
        adapter.setOnItemClickListener(object :CounselorListAdapter.OnItemClickListener{
            override fun onItemClickListener(view: View, position: Int, clickedText: Counselor_Data) {
                if (selectCounselor == clickedText.CounselorID){
                    selectCounselor = ""
                    counselor_hobby = ""
                    counselor_word = ""
                    selectCounselor_name = ""
                    binding.counselorWeek.text = ""
                    binding.counselorName.text = ""
                    binding.counselorHobby.text = ""
                    binding.counselorOneWord.text = ""
                }else{
                    var work = arrayListOf<Boolean>(false,false,false,false,false,false,false)
                    var cnt = 0
                    var Text = ""
                    for (i in clickedText.work){
                        if(i["boolean"] as Boolean){
                            Text += weekList[cnt]
                        }
                        cnt +=1
                    }
                    selectCounselor = clickedText.CounselorID
                    counselor_hobby = clickedText.Testes
                    counselor_word = clickedText.word
                    selectCounselor_name = clickedText.CounselorName
                    binding.counselorWeek.text = Text
                    binding.counselorName.text = clickedText.CounselorName
                    binding.counselorHobby.text = clickedText.Testes
                    binding.counselorOneWord.text = clickedText.word
                }

            }
        })

        //時間が選択された時
        adapter2.setOnItemClickListener(object :ScheduleList_Adapter.OnItemClickListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClickListener(view: View, position: Int, clickedText: Schedule_Data) {
                SELECT_COUNSELOR = clickedText.Select_Counselor
                SELECT_COUNSELOR_NAME = selectCounselor_name
                SELECT_DATE = Select_Date + clickedText.Time
                SELECT_COUNSELOR_HOBBY = counselor_hobby
                SELECT_COUNSELOR_WORD = counselor_word
                Select_Date_Text += clickedText.Time_Only
                SELECT_DATE_TEXT = Select_Date_Text
                findNavController().navigate(ecc.ie3a.suitou.ecounsel.R.id.action_reservationFragment_to_reservationconfFragment)
            }
        })

        //更新ボタンが押された時
        binding.updateButton.setOnClickListener {

            binding.calendarView.visibility = android.widget.CalendarView.INVISIBLE
            binding.linearLayoutIntroduction.visibility = LinearLayout.INVISIBLE
            binding.CounselorView.visibility = LinearLayout.INVISIBLE
            binding.progressBar3.visibility = ProgressBar.VISIBLE
            ListView.clear()
            ReservationData.clear()
            //Firebaseに接続し、カウンセラーの情報を取得する
            db.collection("counselor").whereEqualTo("group","wyKJsGPFwUEjaVIuNfap").get().addOnSuccessListener {
                for (i in it){
                    var setList = Counselor_Data(i.id, i.data["name"] as String,i.data["group"] as String,i.data["tastes"] as String,i.data["word"] as String,i.data["work"] as ArrayList<Map<String, *>>)
                    ListView.add(setList)
                    adapter.notifyDataSetChanged()
                }
                //Firebaseに接続し、予約表の情報を取得する
                db.collection("reservation").whereEqualTo("group","wyKJsGPFwUEjaVIuNfap").get().addOnSuccessListener {
                    for (i in it){
                        var setList = Reservation_Data(i.id,i.data["counselor"]as String,i.data["timestamp"] as String)
                        ReservationData.add(setList)
                    }
                    binding.progressBar3.visibility = android.widget.ProgressBar.INVISIBLE
                    binding.CounselorView.visibility = android.widget.ListView.VISIBLE
                    binding.linearLayoutIntroduction.visibility = LinearLayout.VISIBLE
                    binding.calendarView.visibility = android.widget.CalendarView.VISIBLE
                }.addOnFailureListener {
                    Toast.makeText(activity,"データベース接続に失敗しました。", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(activity,"データベース接続に失敗しました。", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }

    companion object
}