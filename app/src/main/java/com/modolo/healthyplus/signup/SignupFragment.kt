package com.modolo.healthyplus.signup

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.modolo.healthyplus.main.MainFragment
import com.modolo.healthyplus.R
import com.modolo.healthyplus.login.LoginFragment
import java.util.*

class SignupFragment : Fragment() {

    lateinit var mAuth: FirebaseAuth
    private lateinit var emailText: TextInputEditText
    private lateinit var passwordText: TextInputEditText
    private lateinit var password2Text: TextInputEditText
    private lateinit var nameText: TextInputEditText
    private lateinit var surnameText: TextInputEditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        mAuth = Firebase.auth
        val back = view.findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            findNavController().navigateUp()
        }

        var email = "";
        var password = "";
        var name = "";
        var surname = "";
        var dateofbirth = "";

        val scroll = view.findViewById<ScrollView>(R.id.scrollData)
        val fields = scroll.findViewById<LinearLayout>(R.id.fieldsLayout)

        emailText = fields.findViewById(R.id.emailText)
        passwordText = fields.findViewById(R.id.passwordText)
        password2Text = fields.findViewById(R.id.password2Text)
        nameText = fields.findViewById(R.id.nameText)
        surnameText = fields.findViewById(R.id.surnameText)

        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val dateText = scroll.findViewById<TextView>(R.id.dateText)
        dateText.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val text = "$dayOfMonth-${month + 1}-$year"
                    dateofbirth = text
                    dateText.text = text
                },
                y,
                m - 1,
                d
            ).show()
        }

        val layoutSignup = view.findViewById<TextView>(R.id.layoutSignup)
        layoutSignup.setOnClickListener {
            var ready = true
            val emailtmp = emailText.text.toString()
            if (emailtmp != "" && emailtmp.isEmailValid()) email = emailtmp
            else ready = false

            val pswtmp = passwordText.text.toString()
            val psw2tmp = password2Text.text.toString()
            if (pswtmp != "" && pswtmp == psw2tmp) password = pswtmp
            else ready = false

            val nametmp = nameText.text.toString()
            if (nametmp != "") name = nametmp
            else ready = false

            val surnametmp = surnameText.text.toString()
            if (surnametmp != "") surname = surnametmp
            else ready = false

            if (dateofbirth == "") ready = false

            if (ready) {
                register(email, password)
            } else
                Toast.makeText(
                    requireContext(),
                    "Problema/i nei campi, ricontrolla",
                    Toast.LENGTH_SHORT
                ).show()
        }

        return view
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    private fun register(email: String, password: String): Boolean {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser

            }
        }

        return true
    }


}