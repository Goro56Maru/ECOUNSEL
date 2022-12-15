package ecc.ie3a.suitou.ecounsel.login

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ecc.ie3a.suitou.ecounsel.MainActivity
import ecc.ie3a.suitou.ecounsel.R
import ecc.ie3a.suitou.ecounsel.databinding.ActivityLoginBinding
import ecc.ie3a.suitou.ecounsel.databinding.ActivitySignUpBinding

private var uid = ""
private var email1 = ""
private var pass1 = ""

// [START declare_auth]
private lateinit var auth: FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Firebase Auth
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(currentUser)
            reload()
        }

        //ログインボタンが押されたとき
        binding.loginbutton.setOnClickListener {
            email1 = binding.emailText.text.toString()
            pass1 = binding.passText.text.toString()
            if (email1.isNullOrEmpty() || pass1.isNullOrEmpty()){
                Toast.makeText(applicationContext,"メールアドレスとパスワードを入力してください", Toast.LENGTH_SHORT).show()
            }else{
                signIn(email1, pass1)
                reload()
            }
        }

        //アカウント作成ボタンが押されたとき
        binding.registerButton.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(ContentValues.TAG, "signInWithEmail:success")
                val user = auth.currentUser
                uid = auth.currentUser?.uid.toString()
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                updateUI(null)
            }
        }
        // [END sign_in_with_email]
    }

    //ログイン処理
    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            Toast.makeText(applicationContext,"ログイン成功！", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
//            startActivity(Intent(applicationContext,usercheck::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }else{
            signOut()
            Toast.makeText(applicationContext,"ログイン失敗！", Toast.LENGTH_SHORT).show()
        }

    }

    //ログアウト処理
    private fun signOut() {
        auth.signOut()
    }

    private fun reload() {

    }

}