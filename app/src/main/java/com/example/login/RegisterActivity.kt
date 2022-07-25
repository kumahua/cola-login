package com.example.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.login.databinding.ActivityRegisterBinding

class RegisterActivity: AppCompatActivity() {

    private lateinit var v: ActivityRegisterBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        v = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(v.root)

        userList = ArrayList()
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)

        initSpinner()
        initView()
        dialogView()
    }

    private fun initView() {

        v.account.setText(intent.getStringExtra("account"))
        v.password.setText(intent.getStringExtra("password"))
    }

    private fun dialogView() {

        var gender = ""

        v.sexRadio.setOnCheckedChangeListener { radioGroup, i ->
            gender = if(R.id.male == i) "Male" else "Female"
        }

        v.registerBtn.setOnClickListener {

            val account = v.account.text.toString()
            val password = v.password.text.toString()
            val valid = validateData(account, password)
            val error = v.errorTextView
            val edu = v.eduSpinner.selectedItem.toString()

            if(valid && gender.isNotEmpty() && edu.isNotEmpty() && v.agree.isChecked) {

                error.visibility = View.INVISIBLE

                saveData(account, password, edu)

                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setMessage("Account: $account \n" +
                        "Password: $password\n" +
                        "Sex: $gender\n" +
                        "Education: $edu")
                    .setPositiveButton("OK") { _, _ ->
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("new_account", account)
                        intent.putExtra("new_password", password)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    .show()
            } else {

                error.visibility = View.VISIBLE
                error.text = "All field required"
            }
        }
    }

    private fun initSpinner() {

        val spinner = v.eduSpinner
        val educationArray = listOf("Doctor", "Master", "Bachelor")

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, educationArray)
    }

    private fun validateData(account: String, password: String): Boolean {

        var valid = true

        if (account.length < 8) {

            v.account.error = "At least 8 characters"
            valid = false
        } else {

            v.account.error = null
        }

        if (password.length < 8) {

            v.password.error = "At least 8 characters"
            valid = false
        } else {

            v.password.error = null
        }

        return valid
    }

    private fun saveData(acc: String, pas: String, edu: String) {

        val edit: SharedPreferences.Editor = sharedPreferences.edit()

        var i = 0
        while (!sharedPreferences.getString("account-${i}", "").isNullOrEmpty()){
            i++
        }

        edit.putString("account-${i}", acc)
        edit.putString("password-${i}", pas)
        edit.putString("education-${i}", edu)
        edit.apply()
    }
}