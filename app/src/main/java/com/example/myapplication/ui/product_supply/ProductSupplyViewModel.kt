package com.scanapp.ui.product_supply

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.Listeners
import com.example.myapplication.network.ApiClient
import com.example.myapplication.network.ApiInterface
import com.example.myapplication.network.NoConnectivityException
import com.example.myapplication.network.remote.State
import com.example.myapplication.network.remote.SupplyModel
import com.example.myapplication.util.Utils
import com.example.myapplication.viewmodel.TokenViewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductSupplyViewModel(application: Application) : AndroidViewModel(application)
{

    private lateinit var mErrorListener: Listeners.DialogInteractionListener

    fun attachErrorListener(mErrorListener: Listeners.DialogInteractionListener)
    {
        this.mErrorListener = mErrorListener
    }

    companion object
    {
        private val TAG = TokenViewModel::class.java.simpleName
    }

    fun postSupplyInfo(context: Context, productCode: String, serialNumber: String, batch: String, expiry: String, state: String, mListener: onCompleteListener)
    {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context)
                .create(ApiInterface::class.java)
        val state = State().apply { _state = state }

        var call = apiService.getSupplyRequest(productCode, serialNumber, batch, expiry, Utils.getSupplyHeader(context), state)

        call?.enqueue(object : Callback<SupplyModel>
        {
            override fun onFailure(call: Call<SupplyModel>, t: Throwable)
            {
                mErrorListener.dismissDialog()
                if (t is NoConnectivityException)
                {
                }
                else
                {
                    mErrorListener.addErrorDialog()
                }
            }

            override fun onResponse(call: Call<SupplyModel>, response: Response<SupplyModel>)
            {
                Log.d(TAG, response.raw()
                        .toString())
                if (response?.body() != null)
                {
                    mListener.onDataFetch(response.body()!!,false)
                }
                else
                {
                    try {
                       val errorString  = response.errorBody()?.string()
                        if (response.errorBody() != null && response.code() != 400 && !errorString.isNullOrEmpty())
                            mListener.onDataFetch(Gson().fromJson<SupplyModel>(errorString, SupplyModel::class.java),true)
                        else
                            Toast.makeText(context,"Wrong State. Please try again with correct Data",Toast.LENGTH_SHORT).show()
                    } catch (ex:JsonSyntaxException){

                    }

                }
                mErrorListener.dismissDialog()
            }
        })
    }


    fun verifyPack(context: Context, productCode: String, serialNumber: String, batch: String,
        expiry: String, mListener: onCompleteListener)
    {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context)
                .create(ApiInterface::class.java)

        var call = apiService.getVerify(productCode, serialNumber, batch, expiry, Utils.getSupplyHeader(context))

        call?.enqueue(object : Callback<SupplyModel>
        {
            override fun onFailure(call: Call<SupplyModel>, t: Throwable)
            {
                mErrorListener.dismissDialog()
                if (t is NoConnectivityException)
                {
                }
                else
                {
                    mErrorListener.addErrorDialog()
                }
            }

            override fun onResponse(call: Call<SupplyModel>, response: Response<SupplyModel>)
            {
                Log.d(TAG, response.raw()
                        .toString())
                if (response.body() != null)
                {
                    mListener.onDataFetch(response.body()!!,false)
                }
                else
                {
                    val errorString  = response.errorBody()?.string()
                    if (response.errorBody() != null && response.code() != 400 && !errorString.isNullOrEmpty())
                        mListener.onDataFetch(Gson().fromJson<SupplyModel>(errorString, SupplyModel::class.java),true)
                }
                mErrorListener.dismissDialog()
            }
        })
    }

    interface onCompleteListener
    {
        fun onDataFetch(model: SupplyModel,isError:Boolean)
    }
}

