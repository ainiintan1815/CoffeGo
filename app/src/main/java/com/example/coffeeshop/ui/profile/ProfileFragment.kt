package com.example.coffeeshop.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.coffeeshop.data.ViewModelFactory
import com.example.coffeeshop.databinding.FragmentHomeBinding
import com.example.coffeeshop.databinding.FragmentProfileBinding
import com.example.coffeeshop.helper.loadImageUrl
import com.example.coffeeshop.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlin.getValue


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
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
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        realtimeDb = FirebaseDatabase.getInstance().getReference("User")

        getProfile()
        logout()
    }

    private fun getProfile() {
        val userId = auth.currentUser!!.uid

        realtimeDb.child(userId).get()
            .addOnSuccessListener { snapshot ->

                val email = snapshot.child("email").value.toString()
                val fotoProfil = snapshot.child("fotoProfil").value.toString()
                val username = snapshot.child("username").value.toString()

                binding.apply {
                    imgProfile.loadImageUrl(fotoProfil, requireContext())
                    tvEmail.text = email
                    tvName.text = username
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal memuat profil", Toast.LENGTH_LONG).show()
            }
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
