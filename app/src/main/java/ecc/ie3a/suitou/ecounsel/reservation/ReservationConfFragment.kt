package ecc.ie3a.suitou.ecounsel.reservation

import android.app.Activity
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ecc.ie3a.suitou.ecounsel.R
import ecc.ie3a.suitou.ecounsel.databinding.FragmentReservationBinding
import ecc.ie3a.suitou.ecounsel.databinding.FragmentReservationConfBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var SELECT_COUNSELOR = ""
var SELECT_DATE = ""
var SELECT_DATE_TEXT = ""

class ReservationConfFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentReservationConfBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    val comment = ""

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentReservationConfBinding.inflate(inflater, container, false)
        val view = binding.root

        //ログインユーザの情報の取得
        auth = Firebase.auth
        val currentUser = auth.currentUser
        var uid:String

        if (currentUser != null) uid = currentUser.uid
        else uid = ""

        var select_counselor = ""
        //前画面でカウンセラーが選択されていた場合反映する
        if (!SELECT_COUNSELOR.isNullOrEmpty()){
            select_counselor = SELECT_COUNSELOR
        }
        var select_date = SELECT_DATE

        val checkbox = arrayOf(
            binding.friend, binding.school, binding.homeEnviroment,
            binding.mentalHealth, binding.others,
        )
        val checkbox_text = arrayOf("友人関係","学業進路","家庭環境","心身・健康","その他")
        val otherComment = binding.otherComment
        var chosen_text: String? = ""
        var comment: String? = ""
//        val now = Date()
//        val sdf = SimpleDateFormat("yyyy/MM/dd")
//        val timestamp = sdf.format(now)
        var day_text = SELECT_DATE_TEXT

        binding.day.text = day_text

        //予約ボタンが押されたとき
        binding.reservationButton.setOnClickListener{
            if (select_counselor.isNullOrEmpty()){
                Toast.makeText(activity,"カウンセラーを選択してください", Toast.LENGTH_SHORT).show()
            } else{
                //チェックされたテキストの内容を配列に追加
                for (i in checkbox.indices) {
                    if (checkbox[i].isChecked) {
                        if (chosen_text == "" || checkbox[4].isChecked){
                            chosen_text += checkbox_text[i]
                        }else{
                            chosen_text = chosen_text + "," + checkbox_text[i]
                        }
//                        if (checkbox[4].isChecked && otherComment.text != null) {
//                            comment = otherComment.text.toString()
//                        }
                    }
                }
                comment = otherComment.text.toString()

                var reservation_data = hashMapOf(
                    "consaltation" to chosen_text,
                    "counselor" to select_counselor,
                    "remarks" to comment,
                    "subscriber" to uid,
                    "timestamp" to select_date
                )

                db.collection("reservation").add(reservation_data)
                    .addOnSuccessListener { documentReference ->
                        Log.d(ContentValues.TAG, "予約データを追加しました")
                        //選択されたリストのID
                        Toast.makeText(activity,"予約しました\n担当者：$select_counselor \n日程：$select_date", Toast.LENGTH_SHORT).show()
                        SELECT_DATE = ""
                        SELECT_DATE_TEXT = ""
                        SELECT_COUNSELOR = ""
                        findNavController().navigate(ecc.ie3a.suitou.ecounsel.R.id.action_reservationconfFragment_to_reservationFragment)
                    }
                    .addOnFailureListener { e ->
                        Log.d(ContentValues.TAG, "予約データの追加に失敗しました", e)
                    }
            }
        }

        binding.backButton.setOnClickListener {
            SELECT_COUNSELOR = ""
            SELECT_DATE = ""
            SELECT_DATE_TEXT = ""
            findNavController().navigate(ecc.ie3a.suitou.ecounsel.R.id.action_reservationconfFragment_to_reservationFragment)
        }


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReservationConfFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

