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
import com.example.petcareavance.api.UserInfo
import com.example.petcareavance.api.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInFragment : Fragment(){



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.log_in_fragment, container, false)

        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)

        val haveAnAccountTextView = view.findViewById<TextView>(R.id.dontHaveAnAccountSignUpButton)

        haveAnAccountTextView.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, SignUpFragment())
                ?.commit()
            activity?.findViewById<FrameLayout>(R.id.fragment_container)?.bringToFront()
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()



            if (email.isNotEmpty() && password.isNotEmpty()) {
                val call = RetrofitClient.instance.signIn(UserInfo(email, password))
                call.enqueue(object : Callback<UserResponse> {
                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>
                    ) {
                        if (response.isSuccessful) {
                            val id = response.body()?.id.toString()
                            val sharedPreferences = requireActivity().getSharedPreferences(
                                "UserID",
                                AppCompatActivity.MODE_PRIVATE
                            )
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()
                            editor.putString("ID", id)
                            editor.apply()
                            val intent = Intent(requireActivity(), MenuActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Error: ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${t.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            } else {
                Toast.makeText(requireContext(), "Por favor, rellena todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }

        }

        return view;
    }
}