package com.example.login
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.login.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var v: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        v = ActivityMainBinding.inflate(layoutInflater)
        setContentView(v.root)

        userList = ArrayList()
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)

        initView()

        v.login.setOnClickListener {

            val account = v.accountId.text.toString()
            val password = v.password.text.toString()

            if(validateData(account, password)) {

                v.error.visibility = View.INVISIBLE
                getData()
                Log.d("userList", userList.toString())

                if(userList.contains(User(account, password))) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setTitle(R.string.Login)
                        .setPositiveButton("OK", null)
                        .show()
                } else {

                    v.error.apply{

                        visibility = View.VISIBLE
                        text = "Incorrect Account or Password"
                    }
                }

//                if (password == sharedPreferences.getString(account, "")) {
//
//                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//                    builder.setTitle(R.string.Login)
//                        .setPositiveButton("OK", null)
//                        .show()
//                } else {
//
//                    v.error.apply{
//
//                        visibility = View.VISIBLE
//                        text = "Incorrect Account or Password"
//                    }
//                }
            } else {
                Log.d("validateData error", "error")
            }
        }

        v.register.setOnClickListener {
            val account = v.accountId.text.toString()
            val password = v.password.text.toString()
            val intent = Intent(this, RegisterActivity::class.java)

            intent.putExtra("account", account)
            intent.putExtra("password", password)
            //startActivity(intent)
            resultLauncher.launch(intent)
        }
    }

    private fun initView() {

        v.accountId.setText(intent.getStringExtra("new_account"))
        v.password.setText(intent.getStringExtra("new_password"))
    }

    private fun validateData(account: String, password: String) : Boolean {

        var valid = true

        if (account.isEmpty()) {

            v.accountId.error = "Required"
            valid = false
        } else {

            v.accountId.error = null
        }

        if (password.isEmpty()) {

            v.password.error = "Required"
            valid = false
        } else {

            v.password.error = null
        }

        return valid
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val myData: Intent? = result.data
                if (myData != null) {
                    v.accountId.setText(myData.getStringExtra("new_account"))
                    v.password.setText(myData.getStringExtra("new_password"))
                }
                getData()
            }
        }

    private fun getData() {

        val sharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE)
        var i = 0

        while (!sharedPreferences.getString("account-${i}", "").isNullOrEmpty()){

            val user = User(sharedPreferences.getString("account-${i}", "")!!,
                sharedPreferences.getString("password-${i}", "")!!)
            userList.add(user)
            i++
        }

        Log.d("user", userList.toString())
    }
}