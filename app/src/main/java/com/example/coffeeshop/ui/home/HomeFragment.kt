package com.example.coffeeshop.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.adapter.CategoryAdapter
import com.example.coffeeshop.adapter.OffersAdapter
import com.example.coffeeshop.adapter.PopularAdapter
import com.example.coffeeshop.databinding.FragmentHomeBinding
import com.example.coffeeshop.viewmodel.MainViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue


class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]



        setupRecyclerViews()
        observeData()


        // load data dari ViewModel
        viewModel.loadCategory()
        viewModel.loadPopular()
        viewModel.loadOffer()

        binding.recyclerViewCategory.visibility = View.GONE
        binding.editTextText.visibility = View.GONE
        binding.tvLihatSemua.visibility = View.GONE

    }

    private fun setupRecyclerViews() {
        binding.recyclerViewCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPopular.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewOffer.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun observeData() {
        binding.progressBarCategory.visibility = View.VISIBLE
        binding.progressBarPopular.visibility = View.VISIBLE
        binding.progressBarOffer.visibility = View.VISIBLE

        viewModel.category.observe(requireContext() as LifecycleOwner) {
            binding.recyclerViewCategory.adapter = CategoryAdapter(it)
            binding.progressBarCategory.visibility = View.GONE
        }
        viewModel.popular.observe(requireContext() as LifecycleOwner) {
            binding.recyclerViewPopular.adapter = PopularAdapter(it)
            binding.progressBarPopular.visibility = View.GONE
        }
        viewModel.offer.observe(requireContext() as LifecycleOwner) {
            binding.recyclerViewOffer.adapter = OffersAdapter(it)
            binding.progressBarOffer.visibility = View.GONE
        }
    }

    /** Tulis node test ke Realtime DB (aman kalau Firebase ready) */
    private fun writeDataToFirebase() {
        val db = FirebaseDatabase.getInstance(
            "https://coffego-890b3-default-rtdb.firebaseio.com"
        )
        db.getReference("dev_test").setValue(
            mapOf("status" to "ok", "time" to ServerValue.TIMESTAMP)
        ).addOnSuccessListener {
            Toast.makeText(requireContext(), "Write OK", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), "Write FAIL: ${e.message}", Toast.LENGTH_LONG).show()
        }

    }

}