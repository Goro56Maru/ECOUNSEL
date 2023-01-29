package ecc.ie3a.suitou.ecounsel

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ecc.ie3a.suitou.ecounsel.chat.ChatFragment
import ecc.ie3a.suitou.ecounsel.databinding.ActivityLoginBinding
import ecc.ie3a.suitou.ecounsel.databinding.ActivitySelectBinding
import ecc.ie3a.suitou.ecounsel.login.SignUpActivity
import ecc.ie3a.suitou.ecounsel.reservation_status.ReservationStatusFragment
import ecc.ie3a.suitou.ecounsel.reservation_status.Reservation_Status_Data
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class SelectActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectBinding

    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    private var ReservationData : MutableList<Reservation_Status_Data> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        //ログインユーザの情報の取得
        auth = Firebase.auth
        val currentUser = auth.currentUser
        var uid:String

        if (currentUser != null) uid = currentUser.uid
        else uid = ""


        binding.counselorText.visibility = TextView.INVISIBLE
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
                    val text_date = "${year}年 ${month+1}月 ${dayOfMonth}日 ${weekList[select_week]} "
                    binding.counselorText.text = text_date

                    break
                }
            }
            binding.counselorText.visibility = TextView.VISIBLE
        }


        binding.updateButton.setOnClickListener {
            binding.counselorText.visibility = TextView.INVISIBLE
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
                        val text_date = "${year}年 ${month+1}月 ${dayOfMonth}日 ${weekList[select_week]} "
                        binding.counselorText.text = text_date

                        break
                    }
                }
                binding.counselorText.visibility = TextView.VISIBLE
            }
        }


        binding.reservationButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            fragmentselect=1
            startActivity(intent)
        }

        binding.ReservationStatus.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            fragmentselect=0
            startActivity(intent)
        }

//        binding.reservationStatusButton.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            fragmentselect=1
//            startActivity(intent)
//        }

        binding.mypageButton.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            fragmentselect=2
            startActivity(intent)
        }

//        binding.chatButton.setOnClickListener {
//
//            val intent = Intent(this, MainActivity::class.java)
//            fragmentselect=3
//            startActivity(intent)
//        }

    }
}