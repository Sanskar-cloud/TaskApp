package com.shaluambasta.phonenumlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shaluambasta.phonenumlogin.User

class MainActivity : AppCompatActivity() {

    private lateinit var editTextPersonName: EditText
    private lateinit var editTextTextPersonNameemail: EditText
    private lateinit var editTextTextPersonNameage: EditText
    private lateinit var editTextTextcollege: EditText
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        editTextPersonName = findViewById(R.id.editTextPersonName)
        editTextTextPersonNameemail = findViewById(R.id.editTextTextPersonNameemail)
        editTextTextPersonNameage = findViewById(R.id.editTextTextPersonNameage)
        editTextTextcollege = findViewById(R.id.editTextTextcollege)
    }

    fun saveData(view: View) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

        val name: String = editTextPersonName.text.toString()
        val email: String = editTextTextPersonNameemail.text.toString()
        val age: String = editTextTextPersonNameage.text.toString()
        val college: String = editTextTextcollege.text.toString()

        val user = User(name, email, age, college)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid



        // Push data to Firebase Realtime Database
        if (uid != null) {
            databaseReference.child(uid).setValue(user).addOnCompleteListener {

                if(it.isSuccessful){
                    Toast.makeText(
                        this@MainActivity,
                        "Invalid request", Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }

        // Optionally, clear the input fields
        editTextPersonName.text.clear()
        editTextTextPersonNameemail.text.clear()
        editTextTextPersonNameage.text.clear()
        editTextTextcollege.text.clear()

        val intent = Intent(this, EditProfilePage::class.java)
        startActivity(intent)


    }
}
