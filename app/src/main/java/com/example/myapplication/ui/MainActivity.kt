package com.example.myapplication.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Listeners
import com.example.myapplication.R
import com.example.myapplication.network.remote.TokenResponse
import com.example.myapplication.ui.dashboard.DashboardActivity
import com.example.myapplication.util.launchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mViewModel: TokenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btnSubmit)

        mViewModel = ViewModelProvider(this).get(TokenViewModel::class.java)
        mViewModel.attachErrorListener(object : Listeners.DialogInteractionListener {
            override fun dismissDialog() {

            }

            override fun addDialog() {
            }

            override fun addErrorDialog() {
            }

            override fun addErrorDialog(msg: String?) {
            }

        })
        btn.setOnClickListener {
            mViewModel.getToken(
                this,
                "WHUSER01",
                "WHUSER01",
                object : TokenViewModel.onTokenCompleteListener {
                    override fun onTokenFetched(tokenResponse: TokenResponse) {
                        getSharedPreferences("TOKEN", Context.MODE_PRIVATE).edit()
                            .putString("accessToken", tokenResponse.accessToken).apply()
                        launchActivity<DashboardActivity> { }
                    }
                })
        }
    }
}