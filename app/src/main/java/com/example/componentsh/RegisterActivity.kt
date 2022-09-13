package com.example.componentsh

import android.app.DatePickerDialog
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.componentsh.databinding.ActivityMainBinding
import com.google.gson.Gson

class RegisterActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var edtName: EditText
    private lateinit var edtPaterno: EditText
    private lateinit var edtMaterno: EditText
    private lateinit var edtBirth: EditText
    private lateinit var edtMail: EditText
    private lateinit var edtAdrres: EditText
    private lateinit var edtPhone: EditText
    private lateinit var mPrefs:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()
        getUserPrefs()
        setListeners()

    }

    private fun setListeners() {
        val onClickRegister = View.OnClickListener {
            val requiredFields= arrayOf<View>(edtBirth,edtMail,edtPhone)
            val toastMsgs = arrayOf("Fecha de Nacimiento Obligatoria",
                "Correo Obligatorio",
                "Telefono Obligatorio")
            if(validateRequiredFields(requiredFields,toastMsgs)){
                setUserPrefs()
                Toast.makeText(this@RegisterActivity, "Ta bien", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnRegister.setOnClickListener(onClickRegister)
        val birthPickListener=View.OnClickListener{
            showDatePickerDialog();
        }
        edtBirth.setOnClickListener(birthPickListener)
    }

    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val selectedDate = day.toString() + " / " + (month + 1) + " / " + year
            edtBirth.setText(selectedDate)
        })
        newFragment.show(supportFragmentManager, "datePicker")
    }

    fun validateRequiredFields(fields: Array<View>, toasts : Array<String>): Boolean{
        for (i in fields.indices){
            if(!emptyValidation(fields[i],toasts[i])){
                if(fields[i].id == edtBirth.id){
                    showDatePickerDialog()
                }
                return false
            }
        }
        return true
    }

    fun emptyValidation(view: View,msg: String): Boolean{
        if (view is EditText){
            if(view.text.toString().equals("")){
                Toast.makeText(this@RegisterActivity, msg, Toast.LENGTH_SHORT).show()
                view.requestFocus()
                return false
            }
            return true
        }
        return false
    }

    fun setUserPrefs(){
        val usrName = edtName.text.toString()
        val usrPaterno = edtPaterno.text.toString()
        val usrMaterno = edtMaterno.text.toString()
        val usrBirth = edtBirth.text.toString()
        val usrMail = edtMail.text.toString()
        val usrAdrres = edtAdrres.text.toString()
        val usrPhone = edtPhone.text.toString()

        val user = User(usrBirth, usrMail,usrPhone,usrName,usrPaterno,usrMaterno,usrAdrres)

        val gson= Gson()
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit();
        val json:String = gson.toJson(user)
        prefsEditor.putString("user", json)
        prefsEditor.apply()
    }

    fun getUserPrefs() {
        mPrefs = getPreferences(MODE_PRIVATE)
        val gson= Gson()
        val json: String? = mPrefs.getString("user","")
        val userPref = gson.fromJson(json, User::class.java)
        if(userPref != null) {
            edtName.setText(userPref.name)
            edtPaterno.setText(userPref.last_paterno)
            edtMaterno.setText(userPref.last_materno)
            edtBirth.setText(userPref.birth)
            edtMail.setText(userPref.mail)
            edtAdrres.setText(userPref.adres)
            edtPhone.setText(userPref.phone)
        }
    }

    fun initVars() {
        edtName=binding.edtName
        edtPaterno=binding.edtPaterno
        edtMaterno=binding.edtMaterno
        edtBirth=binding.edtBirth
        edtMail=binding.edtMail
        edtAdrres=binding.edtAdress
        edtPhone=binding.edtPhone
    }
}