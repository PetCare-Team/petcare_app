package com.example.petcareavance.Fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.petcareavance.MenuActivity
import com.example.petcareavance.R
import com.example.petcareavance.api.RetrofitClient
import com.example.petcareavance.api.dataclasses.users.UserInfo
import com.example.petcareavance.api.dataclasses.users.UserResponse
import com.example.petcareavance.api.dataclasses.users.UserResponseForSingUp
import com.example.petcareavance.api.dataclasses.users.UserSignInInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sign_up_fragment, container, false)

        val etFirstName = view.findViewById<EditText>(R.id.firstnameSignUpInput)
        val etLastName = view.findViewById<EditText>(R.id.lastnameSignUpInput)
        val etEmail = view.findViewById<EditText>(R.id.mailSignUpInput)
        val etPassword = view.findViewById<EditText>(R.id.passwordSignUpInput)
        val etPhone = view.findViewById<EditText>(R.id.phoneSignUpInput)
        val etDni = view.findViewById<EditText>(R.id.dniSignUpInput)
        val etTypeUser = 1
        val btnLogin = view.findViewById<Button>(R.id.btnSignUp)


        val haveAnAccountTextView = view.findViewById<TextView>(R.id.haveAnAccountSignUpButton)

        haveAnAccountTextView.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, LogInFragment())
                ?.commit()
            activity?.findViewById<FrameLayout>(R.id.fragment_container)?.bringToFront()
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val phone = etPhone.text.toString().toInt()
            val dni = etDni.text.toString().toInt()
            val typeUserId = 1
            val btnLogin = view.findViewById<Button>(R.id.btnSignUp)

            if (email.isNotEmpty() && password.isNotEmpty()) {

         
                val call = RetrofitClient.instance.signUp(UserSignInInfo(email, password, firstName, lastName, phone,typeUserId,dni))
                call.enqueue(object : Callback<UserResponseForSingUp> {
                    override fun onResponse(call: Call<UserResponseForSingUp>, response: Response<UserResponseForSingUp>) {
                        if (response.isSuccessful) {
                            val id = response.body()?.message.toString()
                            val sharedPreferences = requireActivity().getSharedPreferences("UserID",
                                AppCompatActivity.MODE_PRIVATE
                            )
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.putString("ID", id)
                            editor.apply()
                            val intent = Intent(requireActivity(), MenuActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Error: ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UserResponseForSingUp>, t: Throwable) {
                        Toast.makeText(requireActivity(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(requireActivity(), "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
}