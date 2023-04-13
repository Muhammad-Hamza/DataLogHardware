package com.example.myapplication.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.Listeners
import com.example.myapplication.network.ApiClient
import com.example.myapplication.network.ApiInterface
import com.example.myapplication.network.NoConnectivityException
import com.example.myapplication.network.remote.TokenResponse
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import retrofit2.Callback
class TokenViewModel(application: Application) : AndroidViewModel(application) {


//    val CLIENT_CRED_TE ="rZFse0cCPCuQkkNy1Lpm09Gr"
//    val CLIENT_ID_TE ="MzkX7fY5QjMLRFgo33XLkpPf"
//    val CLIENT_CRED_QA ="aa24dd48-bb7d-4fc9-b5a0-248b1bda0207"
//    val CLIENT_ID_QA ="bd902204-8a8f-4618-a6e9-2a995529f377"
// val CLIENT_CRED_QA ="0b4531d4-9e7b-458c-94c4-c7ddcf79d87c"
//    val CLIENT_ID_QA ="b803d3fc-4a86-4186-a43b-e82b6cfc9543"
    val CLIENT_SECRET_KEY_PROD = "f2ffa9dc-e798-4bce-9abd-9c24945ba453"
    val CLIENT_ID_PROD = "b82e8663-14a7-4b24-bf5d-aeaf2f2d0246"

    private lateinit var mErrorListener: Listeners.DialogInteractionListener

    fun attachErrorListener(mErrorListener: Listeners.DialogInteractionListener) {
        this.mErrorListener = mErrorListener
    }

    companion object {
        private val TAG = TokenViewModel::class.java.simpleName
    }

    fun getToken(
        context: Context,
        email: String,
        password: String,
        mListener: onTokenCompleteListener
    ) {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        Log.d(TAG, "===============LOGGING===============")
        var call = apiService.getToken("client_credentials",CLIENT_ID_PROD, CLIENT_SECRET_KEY_PROD)

        call?.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                Log.d(TAG, response.raw().toString())
                mErrorListener.dismissDialog()
                try {
                    val userResponse = response.body()
                    if (userResponse != null) mListener.onTokenFetched(userResponse)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                mErrorListener.dismissDialog()
                if (t is NoConnectivityException) {
                } else {
                    mErrorListener.addErrorDialog()
                }
            }
        })
    }
    interface onTokenCompleteListener
    {
        fun onTokenFetched(tokenResponse: TokenResponse)
    }
}


