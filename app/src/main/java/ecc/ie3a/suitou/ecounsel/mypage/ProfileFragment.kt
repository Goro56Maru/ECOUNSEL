package ecc.ie3a.suitou.ecounsel.mypage

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ecc.ie3a.suitou.ecounsel.ProfileTabFragment
import ecc.ie3a.suitou.ecounsel.R
import ecc.ie3a.suitou.ecounsel.databinding.ActivityLoginBinding
import ecc.ie3a.suitou.ecounsel.databinding.FragmentProfileBinding
import ecc.ie3a.suitou.ecounsel.login.LoginActivity
import ecc.ie3a.suitou.ecounsel.reservation_status.ReservationStatusFragment


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: FragmentProfileBinding

    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val view = binding.root

        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
        }


        val userRef = db.collection("users").document("${currentUser?.uid}")

        //firestoreから名前とカナを取得してhintとして挿入する
        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    binding.namePro.text = document.data?.get("name").toString()
                    binding.emailPro.text= document.data?.get("mail").toString()
                } else {
                    Log.d(ContentValues.TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "get failed with ", exception)
            }



        binding.name.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_nameChangeFragment)
        }

        binding.id.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_mailChangeFragment)
        }

        binding.pass.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_passwordChangeFragment)
        }

        binding.loguout.setOnClickListener{
            SignOut()
        }

        binding.taikai.setOnClickListener{
            SignOut()
        }

        return view
    }

    private fun SignOut(){
        auth.signOut()
        Toast.makeText(context,"ログアウトしました。", Toast.LENGTH_SHORT).show()
        startActivity(Intent(context, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }
}