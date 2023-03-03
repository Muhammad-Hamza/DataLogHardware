package com.example.myapplication.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.myapplication.R
import com.example.myapplication.ui.FixActivity
import com.example.myapplication.ui.SupplyActivity
import com.example.myapplication.ui.VerifyActivity
import com.example.myapplication.util.launchActivity

class DashboardActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val llSupply = findViewById<CardView>(R.id.llSupply)
        val verify = findViewById<CardView>(R.id.verify)
        val fixStatus = findViewById<CardView>(R.id.fixStatus)

        llSupply.setOnClickListener{
            launchActivity<SupplyActivity> {  }
        }
        verify.setOnClickListener{

            launchActivity<VerifyActivity> {  }

        }
        fixStatus.setOnClickListener{
//
            launchActivity<FixActivity> {  }

        }

    }
}