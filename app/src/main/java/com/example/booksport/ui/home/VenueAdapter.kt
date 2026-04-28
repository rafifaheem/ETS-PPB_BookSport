package com.example.booksport.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booksport.R
import com.example.booksport.databinding.ItemVenueBinding
import com.example.booksport.model.Venue
import java.text.NumberFormat
import java.util.Locale

class VenueAdapter(
    private val onItemClick: (Venue) -> Unit
) : ListAdapter<Venue, VenueAdapter.VenueViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val binding = ItemVenueBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VenueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VenueViewHolder(private val binding: ItemVenueBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(venue: Venue) {
            binding.tvVenueName.text = venue.name
            binding.tvVenueType.text = venue.type
            binding.tvVenueAddress.text = venue.address
            binding.tvVenuePrice.text = "Rp ${formatPrice(venue.pricePerHour)}/jam"

            // Set emoji icon based on sport type
            binding.tvVenueIcon.text = when (venue.type) {
                "Futsal"    -> "⚽"
                "Badminton" -> "🏸"
                "Basket"    -> "🏀"
                "Tenis"     -> "🎾"
                else        -> "🏅"
            }

            if (venue.isAvailable) {
                binding.tvAvailability.text = "Tersedia"
                binding.tvAvailability.setBackgroundResource(R.drawable.bg_badge_green)
                binding.tvAvailability.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.green_dark)
                )
                binding.root.alpha = 1.0f
            } else {
                binding.tvAvailability.text = "Penuh"
                binding.tvAvailability.setBackgroundResource(R.drawable.bg_badge_amber)
                binding.tvAvailability.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.amber_dark)
                )
                binding.root.alpha = 0.7f
            }

            binding.root.setOnClickListener { onItemClick(venue) }
        }

        private fun formatPrice(price: Int): String {
            return NumberFormat.getNumberInstance(Locale("id", "ID")).format(price)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Venue>() {
        override fun areItemsTheSame(oldItem: Venue, newItem: Venue) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Venue, newItem: Venue) = oldItem == newItem
    }
}