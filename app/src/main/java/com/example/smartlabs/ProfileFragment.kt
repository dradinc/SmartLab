package com.example.smartlabs

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.smartlabs.Common.OnBoardingStatus
import com.example.smartlabs.Common.SignInStatus
import com.example.smartlabs.Common.UserAppCode
import com.example.smartlabs.databinding.FragmentProfileBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var dataStoreSignInStatus: SignInStatus
    private lateinit var dataStoreOnBoarding: OnBoardingStatus
    private lateinit var dataStoreAppCode: UserAppCode
    private var signInStatus = false
    private lateinit var signInToken: String

    private var callbackWriteToken: (()->Unit)?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callbackWriteToken = {
            binding.simpleText.text = signInToken
        }

        dataStoreSignInStatus = SignInStatus(requireContext())
        dataStoreOnBoarding = OnBoardingStatus(requireContext())
        dataStoreAppCode = UserAppCode(requireContext())
        lifecycleScope.launch { userData() }

        binding.clearSignInStatus.setOnClickListener {
            lifecycleScope.launch {
                dataStoreSignInStatus.setSignInStatus(false, "")
            }
        }

        binding.clearOnBoardingStatus.setOnClickListener {
            lifecycleScope.launch {
                dataStoreOnBoarding.setOnBoardingStatus(false)
            }
        }

        binding.delAppCode.setOnClickListener {
            lifecycleScope.launch {
                dataStoreAppCode.delAppCode()
            }
        }
    }

    private suspend fun userData() {
        signInStatus = dataStoreSignInStatus.getSignInStatus().first()
        when(signInStatus){
            true -> {}
            false -> {}
        }
        signInToken = dataStoreSignInStatus.getSignInToken().first()
        Handler(Looper.getMainLooper()).post {
            callbackWriteToken!!.invoke()
        }
    }
}