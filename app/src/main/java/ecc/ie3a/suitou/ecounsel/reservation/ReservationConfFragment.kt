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

class ReservationConfFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentReservationConfBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    val comment = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentReservationConfBinding.inflate(inflater, container, false)
        val checkbox = arrayOf(
            binding.friend, binding.school, binding.homeEnviroment,
            binding.mentalHealth, binding.others,
        )
        val checkbox_text = arrayOf("友人関係","学業進路","家庭環境","心身・健康","その他")
        val otherComment = binding.otherComment
        var chosen_text: String? = ""
        var comment: String? = ""
        val now = Date()
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val timestamp = sdf.format(now)
        val view = binding.root

        //予約ボタンが押されたとき
        binding.reservationButton.setOnClickListener() {
            //チェックされたテキストの内容を配列に追加
            for (i in checkbox.indices) {
                if (checkbox[i].isChecked) {
                    chosen_text = chosen_text + "," + checkbox_text[i]
                    if (checkbox[4].isChecked && otherComment.text != null) {
                        comment = otherComment.text.toString()
                    }
                }
            }

                var reservation_data = hashMapOf(
                    "consaltation" to chosen_text,
                    "counselor" to "test",
                    "remarks" to comment,
                    "subscriber" to "test",
                    "timestamp" to timestamp
                )

                db.collection("reservation").add(reservation_data)
                    .addOnSuccessListener { documentReference ->
                        Log.d(ContentValues.TAG, "予約データを追加しました")
                    }
                    .addOnFailureListener { e ->
                        Log.d(ContentValues.TAG, "予約データの追加に失敗しました", e)
                    }
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

