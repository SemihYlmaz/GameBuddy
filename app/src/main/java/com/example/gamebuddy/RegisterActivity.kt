package com.example.gamebuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@RegisterActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()



        register_btn.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
    val username: String = username_register.text.toString()
    val email: String = email_register.text.toString()
    val password: String = password_register.text.toString()

        if (username == ""){
            Toast.makeText(this@RegisterActivity, "Lütfen Kullanıcı Adı Giriniz.", Toast.LENGTH_LONG).show()
        }
        else if (email == ""){
            Toast.makeText(this@RegisterActivity, "Lütfen Email Giriniz.", Toast.LENGTH_LONG).show()
        }
        else if (password == ""){
            Toast.makeText(this@RegisterActivity, "Lütfen Şifre Giriniz.", Toast.LENGTH_LONG).show()
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    firebaseUserID = mAuth.currentUser!!.uid
                    refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseUserID
                    userHashMap["username"] = username
                    userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/gamebuddyson.appspot.com/o/profile.png?alt=media&token=b53b5a62-bcb4-4e15-8a32-2b000ee387f8"
                    userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/gamebuddyson.appspot.com/o/cover.jpg?alt=media&token=d168efb6-2b5c-431a-87f1-9e1e6d796209"
                    userHashMap["status"] = "offline"
                    userHashMap["search"] = username.toLowerCase()
                    userHashMap["steam"] = "https://www.steam.com"
                    userHashMap["reddit"] = "https://www.reddit.com"
                    userHashMap["twitch"] = "https://www.twitch.tv"

                    refUsers.updateChildren(userHashMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }
                        }
                }
                else {
                    Toast.makeText(this@RegisterActivity, "Hata: " + task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
