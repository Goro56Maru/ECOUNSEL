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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ecc.ie3a.suitou.ecounsel.MainActivity
import ecc.ie3a.suitou.ecounsel.R
import ecc.ie3a.suitou.ecounsel.databinding.ActivitySignUpBinding


private var ruid = ""
private var remail = ""
private var rpass = ""
private var rpass2 = ""

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySignUpBinding

    //private val db = Firebase.firestore

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        binding.signupbutton.setOnClickListener {
            remail = binding.mailText.text.toString()
            rpass = binding.passText.text.toString()
            rpass2 = binding.againPassText.text.toString()

            //メールアドレスもしくはパスワードが入力されているときのみアカウント登録
            if (remail.isNullOrEmpty() || rpass.isNullOrEmpty() || rpass2.isNullOrEmpty()) {
                Toast.makeText(applicationContext, "メールアドレスとパスワードを入力してください", Toast.LENGTH_SHORT).show()
            } else {
                //入力された再確認パスワードが正しいか、入力されたパスワードが6文字以上かチェック
                if (rpass != rpass2) {
                    Toast.makeText(applicationContext, "入力されたパスワードが異なります。", Toast.LENGTH_SHORT).show()
                }else if(rpass.length < 6){
                    Toast.makeText(applicationContext, "パスワードは6文字以上です。", Toast.LENGTH_SHORT).show()
                }else{
                    createAccount(remail, rpass)
                }
            }

        }

    }

    //アカウント作成
    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    ruid = auth.currentUser?.uid.toString()


                    val userdata = hashMapOf(
                        "expenditure" to 0 ,
                        "income" to 0 ,
                        "users" to arrayListOf<String>()
                    )

//                    db.collection("user")
//                        .document(ruid).set(userdata)
//                        .addOnSuccessListener { documentReference ->
//                            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ")
//                        }

                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null){
            Toast.makeText(applicationContext, "ログイン成功！ UID = $ruid", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
//            startActivity(Intent(applicationContext,useradd::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }else{
            signOut()
            Toast.makeText(applicationContext,"ログイン失敗！", Toast.LENGTH_SHORT).show()
        }

    }

    private fun signOut() {
        auth.signOut()
    }

    private fun reload() {

    }

}