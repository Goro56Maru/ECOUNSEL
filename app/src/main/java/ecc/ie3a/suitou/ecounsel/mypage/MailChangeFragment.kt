package ecc.ie3a.suitou.ecounsel.mypage

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ecc.ie3a.suitou.ecounsel.R
import ecc.ie3a.suitou.ecounsel.databinding.FragmentMailChangeBinding
import ecc.ie3a.suitou.ecounsel.databinding.FragmentNameChangeBinding
import ecc.ie3a.suitou.ecounsel.databinding.FragmentProfileBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MailChangeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */



class MailChangeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private val db = Firebase.firestore
    private var _binding: FragmentMailChangeBinding? = null
    private val binding get() = _binding!!
    private var mail = ""
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMailChangeBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = Firebase.auth
        val currentUser = auth.currentUser
        val userRef = db.collection("users").document("${currentUser?.uid}")

        //firestoreから名前とカナを取得してhintとして挿入する
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.mail.hint = document.data?.get("mail").toString()
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }


        binding.button2.setOnClickListener {
            mail = binding.mailEdit.text.toString()

            if (mail.isNullOrEmpty()) {
                Toast.makeText(context, "未入力項目があります", Toast.LENGTH_SHORT).show()
            } else {
                userRef.update(
                    "mail", mail,
                )
                Toast.makeText(context, "メールを変更しました", Toast.LENGTH_SHORT).show()
//                Update_name(name, name_kana)
            }
        }

        binding.mailBackbutton.setOnClickListener{
            findNavController().navigate(R.id.action_mailChangeFragment_to_profileFragment)
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
         * @return A new instance of fragment MailChangeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MailChangeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}