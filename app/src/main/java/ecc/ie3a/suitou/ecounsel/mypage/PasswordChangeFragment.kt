package ecc.ie3a.suitou.ecounsel.mypage

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ecc.ie3a.suitou.ecounsel.R
import ecc.ie3a.suitou.ecounsel.databinding.FragmentMailChangeBinding
import ecc.ie3a.suitou.ecounsel.databinding.FragmentNameChangeBinding
import ecc.ie3a.suitou.ecounsel.databinding.FragmentPasswordChangeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PasswordChangeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
//追加　
private lateinit var binding: FragmentPasswordChangeBinding


class PasswordChangeFragment : Fragment() {

    private var _binding: FragmentPasswordChangeBinding? = null
    private val binding get() = _binding!!
    private var pass = ""

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

        //遷移追加
        _binding = FragmentPasswordChangeBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth
        val user = auth.currentUser

        binding.nbutton.setOnClickListener {
            pass = binding.userpassEdit.text.toString()
            user!!.updatePassword(pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User password updated.")
                    }
                }
        }





        binding.passBackbutton.setOnClickListener{
            findNavController().navigate(R.id.action_passwordChangeFragment_to_profileFragment)
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
         * @return A new instance of fragment PasswordChangeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PasswordChangeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}