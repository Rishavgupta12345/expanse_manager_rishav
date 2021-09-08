package com.example.expansemanager

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.expanse.Login.FirebaseLoginFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_firebase_login.*


class FirebaseLoginFragment : Fragment() {


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if (this.auth.currentUser != null) {
            findNavController().navigate(FirebaseLoginFragmentDirections.actionFirebaseLoginFragmentToLoginFragment())
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        UpdateUI(currentUser)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_firebase_login, container, false)
    }

    private fun UpdateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            findNavController().navigate(FirebaseLoginFragmentDirections.actionFirebaseLoginFragmentToLoginFragment())
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notRegistered_buttonid.setOnClickListener {
            findNavController().navigate(FirebaseLoginFragmentDirections.actionFirebaseLoginFragmentToFirebaseRegisterFragment4())
        }

        Guest_buttonid.setOnClickListener{
            findNavController().navigate(FirebaseLoginFragmentDirections.actionFirebaseLoginFragmentToLoginFragment())
        }



        login_buttonid.setOnClickListener {
            firebaseEmailLoginid.error = null
            firebasePasswordLoginid.error = null

            val email = firebaseEmailLoginid.editText?.text.toString()
            val pass = firebasePasswordLoginid.editText?.text.toString()

            if (validateInput(email, pass)) {
                progressBarid1_0.visibility = View.VISIBLE

                // authenticating user
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    progressBarid1_0.visibility = View.INVISIBLE

                    if (task.isSuccessful) {
                        findNavController().navigate(FirebaseLoginFragmentDirections.actionFirebaseLoginFragmentToLoginFragment())
                    } else {
                        val toast = Toast.makeText(
                            requireActivity(),
                            "Authentication failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        )
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                        toast.show()
                    }
                }

            }
        }

    }

    private fun validateInput(email: String, pass: String): Boolean {
        var valid = true
        if (email.isBlank()) {
            firebaseEmailLoginid.error = "Please enter an email address"
            valid = false
        } else if (pass.length < 8) {
            firebasePasswordLoginid.error = "Password show 8 character or more"
            valid = false
        }

        return valid
    }


}