package ecc.ie3a.suitou.ecounsel.mypage

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ecc.ie3a.suitou.ecounsel.databinding.FragmentNameChangeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NameChangeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NameChangeFragment : Fragment() {

    private val db = Firebase.firestore
    private var _binding: FragmentNameChangeBinding? = null
    private val binding get() = _binding!!
    private var name = ""
    private var name_kana = ""

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        _binding = FragmentNameChangeBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = Firebase.auth
        val currentUser = auth.currentUser
        val userRef = db.collection("users").document("${currentUser?.uid}")

        //firestoreから名前とカナを取得してhintとして挿入する
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.usernameEdit.hint = document.data?.get("name").toString()
                    binding.userkana.hint = document.data?.get("name_k").toString()
                    Toast.makeText(context,"${document.data?.get("name")}", Toast.LENGTH_LONG).show()
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        if(currentUser != null){
            Toast.makeText(context,"${userRef}", Toast.LENGTH_LONG).show()
            Toast.makeText(context,"${currentUser.uid}", Toast.LENGTH_SHORT).show()
            Toast.makeText(context,"ログイン成功！", Toast.LENGTH_SHORT).show()
        }

        //変更ボタンが押されたとき
        binding.changeButton.setOnClickListener {
            name = binding.usernameEdit.text.toString()
            name_kana = binding.userkanaEdit.text.toString()

            if (name.isNullOrEmpty() || name_kana.isNullOrEmpty()) {
                Toast.makeText(context, "未入力項目があります", Toast.LENGTH_SHORT).show()
            } else {
                userRef.update(
                    "name", name,
                    "name_k", name_kana
                )
//                Update_name(name, name_kana)
            }
        }

        return view
    }

//    fun Update_name(name: String, name_kana: String) {
//
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NameChangeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NameChangeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}