package com.example.booksport.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.booksport.MainActivity
import com.example.booksport.R
import com.example.booksport.data.VenueRepository
import com.example.booksport.databinding.FragmentHomeBinding
import com.example.booksport.model.Venue
import com.example.booksport.ui.booking.BookingFormFragment
import com.google.android.material.chip.Chip

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var venueAdapter: VenueAdapter
    private var selectedCategory = "Semua"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupCategoryChips()
        loadVenues("Semua")
    }

    private fun setupRecyclerView() {
        venueAdapter = VenueAdapter { venue ->
            if (!venue.isAvailable) {
                showUnavailableMessage()
                return@VenueAdapter
            }
            val fragment = BookingFormFragment.newInstance(venue.id)
            (activity as MainActivity).navigateTo(fragment, addToBackStack = true)
        }
        binding.recyclerVenues.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        binding.recyclerVenues.adapter = venueAdapter
    }

    private fun setupCategoryChips() {
        val categories = listOf("Semua", "Futsal", "Badminton", "Basket", "Tenis")
        categories.forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category
                isCheckable = true
                isChecked = category == "Semua"
                setChipBackgroundColorResource(
                    if (category == "Semua") R.color.green_primary else R.color.chip_default
                )
                setTextColor(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        if (category == "Semua") R.color.white else R.color.chip_text
                    )
                )
                setOnClickListener {
                    updateChipStates(this, categories)
                    selectedCategory = category
                    loadVenues(category)
                }
            }
            binding.chipGroupCategory.addView(chip)
        }
    }

    private fun updateChipStates(selectedChip: Chip, categories: List<String>) {
        for (i in 0 until binding.chipGroupCategory.childCount) {
            val chip = binding.chipGroupCategory.getChildAt(i) as Chip
            val isSelected = chip == selectedChip
            chip.isChecked = isSelected
            chip.setChipBackgroundColorResource(
                if (isSelected) R.color.green_primary else R.color.chip_default
            )
            chip.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    if (isSelected) R.color.white else R.color.chip_text
                )
            )
        }
    }

    private fun loadVenues(category: String) {
        val venues = VenueRepository.getVenuesByType(category)
        venueAdapter.submitList(venues)
        binding.tvEmptyState.visibility = if (venues.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun showUnavailableMessage() {
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Lapangan Tidak Tersedia")
            .setMessage("Maaf, lapangan ini sedang penuh. Silakan pilih lapangan lain.")
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}