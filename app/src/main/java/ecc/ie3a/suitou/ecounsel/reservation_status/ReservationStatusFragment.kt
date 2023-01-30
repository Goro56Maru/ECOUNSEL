package ecc.ie3a.suitou.ecounsel.reservation_status

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ecc.ie3a.suitou.ecounsel.databinding.FragmentReservationStatusBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReservationStatusFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservationStatusFragment : Fragment() {

    private var _binding: FragmentReservationStatusBinding? = null
    private val binding get() = _binding!!

    //firestore
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var ReservationData : MutableList<Reservation_Status_Data> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReservationStatusBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.infoText.visibility = LinearLayout.INVISIBLE
        binding.day.visibility = android.widget.TextView.INVISIBLE
        binding.consaltation.visibility = android.widget.TextView.INVISIBLE
        binding.otherComment.visibility = android.widget.TextView.INVISIBLE
        binding.dayText.visibility = android.widget.TextView.INVISIBLE
        binding.counselText2.visibility = android.widget.TextView.INVISIBLE
        binding.deleteButton.visibility = android.widget.Button.INVISIBLE

        //ログインユーザの情報の取得
        auth = Firebase.auth
        val currentUser = auth.currentUser
        var uid:String

        if (currentUser != null) uid = currentUser.uid
        else uid = ""

        var reserv_id = ""

        binding.counselorText.text = "現在の予約はありません"
        ReservationData.clear()
        db.collection("reservation").whereEqualTo("subscriber",uid).get().addOnSuccessListener {
            for (i in it){
                val df = SimpleDateFormat("yyyy/MM/dd/HH:mm")
                val date_str = i.data["timestamp"]as String
                val dt = df.parse(date_str)
                var setList = Reservation_Status_Data(i.id,i.data["counselor"]as String,dt,i.data["consaltation"]as String,i.data["remarks"]as String)
                ReservationData.add(setList)
            }
            ReservationData.sortBy { it.TimeStamp }
            Log.d("TAG", "結果")
            Log.d("TAG", "$ReservationData")
            val localDateTime = LocalDateTime.now()
            val date = Date()
            for (i in ReservationData){
                Log.d("TAG", "とおてるよ")
                Log.d("TAG", "${i.TimeStamp}")
                Log.d("TAG", "$date")
                if (date.before(i.TimeStamp) ){
                    var weekList = arrayListOf<String>("(日)","(月)","(火)","(水)","(木)","(金)","(土)")
                    Log.d("TAG", "とおてるよ")
                    val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN)
                    calendar.time = i.TimeStamp
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val dayOfMonth = calendar.get(Calendar.DATE)
                    val select_week = calendar.get(Calendar.DAY_OF_WEEK) - 1
                    val df = SimpleDateFormat("HH:mm")
                    val time = df.format(i.TimeStamp)
                    Log.d("TAG", "${calendar}")
                    val text_date = "${year}年 ${month+1}月 ${dayOfMonth}日 ${weekList[select_week]} $time"
                    db.collection("counselor").document(i.CounselorID).get().addOnSuccessListener { it ->
                        val df = SimpleDateFormat("yyyy年 MM月 dd日 HH:mm")
                        binding.counselorText.text = "担当者：" + it["name"] as String
                        binding.day.text = text_date
                        binding.consaltation.text = i.consaltation
                        binding.otherComment.text = i.remarks
                        reserv_id = i.ID

                        binding.infoText.visibility = LinearLayout.VISIBLE
                        binding.day.visibility = android.widget.TextView.VISIBLE
                        binding.consaltation.visibility = android.widget.TextView.VISIBLE
                        binding.otherComment.visibility = android.widget.TextView.VISIBLE
                        binding.dayText.visibility = android.widget.TextView.VISIBLE
                        binding.counselText2.visibility = android.widget.TextView.VISIBLE
                        binding.deleteButton.visibility = android.widget.Button.VISIBLE
                    }.addOnFailureListener {

                    }
                    break
                }
            }
            binding.infoText.visibility = LinearLayout.VISIBLE
        }

        binding.updateButton.setOnClickListener {
            binding.infoText.visibility = LinearLayout.INVISIBLE
            binding.day.visibility = android.widget.TextView.INVISIBLE
            binding.consaltation.visibility = android.widget.TextView.INVISIBLE
            binding.otherComment.visibility = android.widget.TextView.INVISIBLE
            binding.dayText.visibility = android.widget.TextView.INVISIBLE
            binding.counselText2.visibility = android.widget.TextView.INVISIBLE
            binding.deleteButton.visibility = android.widget.Button.INVISIBLE


            binding.counselorText.text = "現在の予約はありません"
            ReservationData.clear()
            db.collection("reservation").whereEqualTo("subscriber",uid).get().addOnSuccessListener {
                for (i in it){
                    val df = SimpleDateFormat("yyyy/MM/dd/HH:mm")
                    val date_str = i.data["timestamp"]as String
                    val dt = df.parse(date_str)
                    var setList = Reservation_Status_Data(i.id,i.data["counselor"]as String,dt,i.data["consaltation"]as String,i.data["remarks"]as String)
                    ReservationData.add(setList)
                }
                ReservationData.sortBy { it.TimeStamp }
                Log.d("TAG", "結果")
                Log.d("TAG", "$ReservationData")
                val localDateTime = LocalDateTime.now()
                val date = Date()
                for (i in ReservationData){
                    Log.d("TAG", "とおてるよ")
                    Log.d("TAG", "${i.TimeStamp}")
                    Log.d("TAG", "$date")
                    if (date.before(i.TimeStamp) ){
                        var weekList = arrayListOf<String>("(日)","(月)","(火)","(水)","(木)","(金)","(土)")
                        Log.d("TAG", "とおてるよ")
                        val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN)
                        calendar.time = i.TimeStamp
                        val year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH)
                        val dayOfMonth = calendar.get(Calendar.DATE)
                        val select_week = calendar.get(Calendar.DAY_OF_WEEK) - 1
                        val df = SimpleDateFormat("HH:mm")
                        val time = df.format(i.TimeStamp)
                        Log.d("TAG", "${calendar}")
                        val text_date = "${year}年 ${month+1}月 ${dayOfMonth}日 ${weekList[select_week]} $time"
                        db.collection("counselor").document(i.CounselorID).get().addOnSuccessListener { it ->
                            val df = SimpleDateFormat("yyyy年 MM月 dd日 HH:mm")
                            binding.counselorText.text = "担当者：" + it["name"] as String
                            binding.day.text = text_date
                            binding.consaltation.text = i.consaltation
                            binding.otherComment.text = i.remarks
                            reserv_id = i.ID

                            binding.day.visibility = android.widget.TextView.VISIBLE
                            binding.infoText.visibility = LinearLayout.VISIBLE
                            binding.consaltation.visibility = android.widget.TextView.VISIBLE
                            binding.otherComment.visibility = android.widget.TextView.VISIBLE
                            binding.dayText.visibility = android.widget.TextView.VISIBLE
                            binding.counselText2.visibility = android.widget.TextView.VISIBLE
                            binding.deleteButton.visibility = android.widget.Button.VISIBLE
                        }.addOnFailureListener {

                        }
                        break
                    }
                }
                binding.infoText.visibility = LinearLayout.VISIBLE
            }
        }

        binding.deleteButton.setOnClickListener {
            AlertDialog.Builder(context) // FragmentではActivityを取得して生成
                .setTitle("予約取り消し")
                .setMessage("予約を取り消します\n本当によろしいですか？")
                .setPositiveButton("Ok") { dialog, which ->
                    db.collection("reservation").document(reserv_id).delete()
                        .addOnSuccessListener {
                            Toast.makeText(activity, "予約を取り消しました。", Toast.LENGTH_SHORT).show()
                            binding.infoText.visibility = LinearLayout.INVISIBLE
                            binding.day.visibility = android.widget.TextView.INVISIBLE
                            binding.consaltation.visibility = android.widget.TextView.INVISIBLE
                            binding.otherComment.visibility = android.widget.TextView.INVISIBLE
                            binding.dayText.visibility = android.widget.TextView.INVISIBLE
                            binding.counselText2.visibility = android.widget.TextView.INVISIBLE
                            binding.deleteButton.visibility = android.widget.Button.INVISIBLE


                            binding.counselorText.text = "現在の予約はありません"
                            ReservationData.clear()
                            db.collection("reservation").whereEqualTo("subscriber",uid).get().addOnSuccessListener {
                                for (i in it){
                                    val df = SimpleDateFormat("yyyy/MM/dd/HH:mm")
                                    val date_str = i.data["timestamp"]as String
                                    val dt = df.parse(date_str)
                                    var setList = Reservation_Status_Data(i.id,i.data["counselor"]as String,dt,i.data["consaltation"]as String,i.data["remarks"]as String)
                                    ReservationData.add(setList)
                                }
                                ReservationData.sortBy { it.TimeStamp }
                                Log.d("TAG", "結果")
                                Log.d("TAG", "$ReservationData")
                                val localDateTime = LocalDateTime.now()
                                val date = Date()
                                for (i in ReservationData){
                                    Log.d("TAG", "とおてるよ")
                                    Log.d("TAG", "${i.TimeStamp}")
                                    Log.d("TAG", "$date")
                                    if (date.before(i.TimeStamp) ){
                                        var weekList = arrayListOf<String>("(日)","(月)","(火)","(水)","(木)","(金)","(土)")
                                        Log.d("TAG", "とおてるよ")
                                        val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN)
                                        calendar.time = i.TimeStamp
                                        val year = calendar.get(Calendar.YEAR)
                                        val month = calendar.get(Calendar.MONTH)
                                        val dayOfMonth = calendar.get(Calendar.DATE)
                                        val select_week = calendar.get(Calendar.DAY_OF_WEEK) - 1
                                        val df = SimpleDateFormat("HH:mm")
                                        val time = df.format(i.TimeStamp)
                                        Log.d("TAG", "${calendar}")
                                        val text_date = "${year}年 ${month+1}月 ${dayOfMonth}日 ${weekList[select_week]} $time"
                                        db.collection("counselor").document(i.CounselorID).get().addOnSuccessListener { it ->
                                            val df = SimpleDateFormat("yyyy年 MM月 dd日 HH:mm")
                                            binding.counselorText.text = "担当者：" + it["name"] as String
                                            binding.day.text = text_date
                                            binding.consaltation.text = i.consaltation
                                            binding.otherComment.text = i.remarks
                                            reserv_id = i.ID

                                            binding.day.visibility = android.widget.TextView.VISIBLE
                                            binding.infoText.visibility = LinearLayout.VISIBLE
                                            binding.consaltation.visibility = android.widget.TextView.VISIBLE
                                            binding.otherComment.visibility = android.widget.TextView.VISIBLE
                                            binding.dayText.visibility = android.widget.TextView.VISIBLE
                                            binding.counselText2.visibility = android.widget.TextView.VISIBLE
                                            binding.deleteButton.visibility = android.widget.Button.VISIBLE
                                        }.addOnFailureListener {

                                        }
                                        break
                                    }
                                }
                                binding.infoText.visibility = LinearLayout.VISIBLE
                            }
                        }
                }
                .setNegativeButton("No") { dialog, which ->
                }
                .show()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReservationStatusFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReservationStatusFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}