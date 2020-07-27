package com.mylektop.videomeeting.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mylektop.videomeeting.R
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        textSignUp.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }

//        val database = FirebaseFirestore.getInstance()
//        val user = HashMap<String, Any>()
//        user["first_name"] = "Iddan"
//        user["last_name"] = "Gunawan"
//        user["email"] = "iddandunk@gmail.com"
//        database.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Toast.makeText(
//                    this,
//                    "User inserted",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(
//                    this,
//                    "Error add user",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
    }
}