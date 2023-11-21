package com.example.petcareavance.Fragments

import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.graphics.drawable.Icon
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.petcareavance.MenuActivity
import com.example.petcareavance.R
import com.example.petcareavance.api.RetrofitClient
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
        val btnPolitic = view.findViewById<TextView>(R.id.tvPolitic)
        val btnTermService = view.findViewById<TextView>(R.id.tvTermService)

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

        btnPolitic.setOnClickListener {

            showPrivacyPolitics(this.requireContext()!!)
        }

        btnTermService.setOnClickListener {

            showTermConditions(this.requireContext()!!)
        }

        return view
    }

    private fun showTermConditions(context: Context){

        val policyText = """
    
Al acceder y utilizar esta aplicación, usted acepta estar sujeto a los siguientes términos y condiciones. Lea detenidamente antes de continuar.

1. Uso Aceptable

Al utilizar esta aplicación, usted se compromete a no realizar ninguna actividad que sea ilegal o que viole los derechos de otras personas.

2. Contenido

El contenido proporcionado en esta aplicación es solo para fines informativos. No garantizamos la exactitud, integridad o actualidad de la información proporcionada.

3. Privacidad

Su privacidad es importante para nosotros. Consulte nuestra Política de Privacidad para obtener información sobre cómo manejamos sus datos personales.

4. Modificaciones

Nos reservamos el derecho de modificar o actualizar estos términos y condiciones en cualquier momento. Es su responsabilidad revisar periódicamente los cambios.

5. Derechos de Propiedad Intelectual

Todos los derechos de propiedad intelectual relacionados con esta aplicación son propiedad de [Nombre de la Empresa]. No se le otorga ningún derecho o licencia sobre estos derechos.

6. Renuncia de Responsabilidad

Esta aplicación se proporciona "tal cual", sin garantías de ningún tipo. No somos responsables de los daños que puedan surgir del uso de esta aplicación.

Al continuar utilizando esta aplicación, usted acepta estos términos y condiciones. Si no está de acuerdo, por favor, desinstale la aplicación y deje de usarla.

""".trimIndent()

        val spannableString = SpannableString(policyText)



        val builder = AlertDialog.Builder(context)
        builder.setTitle("Términos y Condiciones de Uso\n")
        builder.setMessage(spannableString)


        builder.setPositiveButton("Cerrar") { _, _ ->

        }


        val alert = builder.create()
        alert.show()

    }



    private fun showPrivacyPolitics(context: Context){

        val policyText = """
    Fecha de vigencia: 20/09/2023
    
    Esta Política de Privacidad describe cómo PetCare recopila, utiliza y comparte información personal cuando utilizas nuestra aplicación móvil.
    
    
    Información que Recopilamos:
    
    1. Podemos recopilar información que nos proporcionas directamente cuando utilizas la Aplicación. Esto puede incluir, entre otros, tu nombre, dirección de correo electrónico y otra información que eliges proporcionar.
    
    2. Al utilizar la Aplicación, podemos recopilar cierta información de forma automática, como tu dirección IP, tipo de dispositivo, sistema operativo, identificadores de dispositivo únicos y otros datos de diagnóstico.
    
   
    Cómo Utilizamos la Información:
   
    
    - Proporcionar y mantener la funcionalidad de la Aplicación.
    - Personalizar tu experiencia y entender cómo interactúas con la Aplicación.
    - Enviar notificaciones importantes relacionadas con la Aplicación.
    - Mejorar continuamente nuestra Aplicación y desarrollar nuevas características.
    
    
    Cómo Compartimos la Información:
    
    No compartimos información personal con terceros, excepto en circunstancias limitadas, como cuando sea necesario para cumplir con la ley o proteger nuestros derechos.
    
   
    Cambios en Esta Política de Privacidad:
    
    Podemos actualizar nuestra Política de Privacidad de vez en cuando. Te notificaremos de cualquier cambio publicando la nueva Política de Privacidad en esta página.
    
    
    Contacto:
    
    Si tienes preguntas sobre esta Política de Privacidad, contáctanos en petcare@support.com.
""".trimIndent()

        val spannableString = SpannableString(policyText)

        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, 19, 0)


        val builder = AlertDialog.Builder(context)
        builder.setTitle("Política de Privacidad para PetCare")
        builder.setMessage(spannableString)


        builder.setPositiveButton("Cerrar") { _, _ ->

        }


        val alert = builder.create()
        alert.show()

    }
}

