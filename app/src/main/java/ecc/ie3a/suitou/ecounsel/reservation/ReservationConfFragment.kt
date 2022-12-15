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
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReservationConfFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservationConfFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentReservationConfBinding? = null
    private val binding get() = _binding!!
    val db = Firebase.firestore
    //チェックボックスの内容を取得
    var checkbox_text = binding.friend
    //その他(備考欄)の内容を取得
    var comment = binding.otherComment

    val reservation_data = hashMapOf(
        "consaltation" to checkbox_text ,
        "remarks" to comment  ,
        "timestamp" to ServerTimestamp()
    )

    data class Reservation(
        val consaltation: String,
//        val counselor: String,
        val remarks: String,
//        val subscriber: String,
        val timestamp: ServerTimestamp
    )


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
        //ボタンが押されたとき
        binding.reservationButton.setOnClickListener() {
            //友人関係がチェックされたとき
            if (binding.friend.isChecked) {
                db.collection("reservation").add(reservation_data)
                    .addOnSuccessListener { documentReference ->
                        Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error writing document", e)
                    }



            }

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
         * @return A new instance of fragment ReservationConfFragment.
         */
        // TODO: Rename and change types and number of parameters
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

