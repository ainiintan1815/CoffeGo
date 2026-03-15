package com.example.coffeeshop.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.coffeeshop.data.ViewModelFactory
import com.example.coffeeshop.databinding.FragmentProfileBinding
import com.example.coffeeshop.databinding.FragmentSettingBinding
import com.example.coffeeshop.ui.auth.LoginActivity
import com.example.coffeeshop.ui.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlin.getValue

class SettingFragment: Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var realtimeDb: DatabaseReference
    private var auth: FirebaseAuth = Firebase.auth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        realtimeDb = FirebaseDatabase.getInstance().getReference("User")


        logout()

    }

    private fun logout() {
        binding.cardLogout.setOnClickListener {
            showExitConfirmationDialog()
        }
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ ->

                viewModel.saveLoginState("isLoggedIn", false)

                auth.signOut()

                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}