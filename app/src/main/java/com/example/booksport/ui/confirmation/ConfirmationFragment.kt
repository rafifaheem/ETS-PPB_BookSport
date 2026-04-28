package com.example.booksport.ui.confirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.booksport.MainActivity
import com.example.booksport.databinding.FragmentConfirmationBinding
import com.example.booksport.databinding.ItemDetailRowBinding
import com.example.booksport.model.Booking
import com.example.booksport.ui.home.HomeFragment
import java.text.NumberFormat
import java.util.Locale

class ConfirmationFragment : Fragment() {

    private var _binding: FragmentConfirmationBinding? = null
    private val binding get() = _binding!!
    private lateinit var booking: Booking

    companion object {
        private const val ARG_BOOKING = "booking"
        fun newInstance(booking: Booking) = ConfirmationFragment().apply {
            arguments = Bundle().apply { putSerializable(ARG_BOOKING, booking as java.io.Serializable) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        booking = arguments?.getSerializable(ARG_BOOKING) as Booking

        displayBookingDetails()
    }

    private fun displayBookingDetails() {
        binding.tvBookingId.text = "ID: ${booking.bookingId}"
        binding.tvTotalPrice.text = "Rp ${formatPrice(booking.totalPrice)}"

        setDetailRow(binding.rowName, "Nama", booking.customerName)
        setDetailRow(binding.rowPhone, "Telepon", booking.phoneNumber)
        setDetailRow(binding.rowSport, "Olahraga", booking.sportType)
        setDetailRow(binding.rowVenue, "Tempat", booking.venue.name)
        setDetailRow(binding.rowDate, "Tanggal", booking.date)
        setDetailRow(binding.rowTime, "Waktu", "${booking.startTime} - ${booking.endTime}")
        setDetailRow(binding.rowDuration, "Durasi", "${booking.durationHours} jam")

        if (booking.notes.isNotEmpty()) {
            binding.tvNotesLabel.visibility = View.VISIBLE
            binding.tvNotes.visibility = View.VISIBLE
            binding.tvNotes.text = booking.notes
        }

        binding.btnBackHome.setOnClickListener {
            (activity as MainActivity).navigateTo(HomeFragment())
        }
    }

    private fun setDetailRow(rowBinding: ItemDetailRowBinding, label: String, value: String) {
        rowBinding.tvLabel.text = label
        rowBinding.tvValue.text = value
    }

    private fun formatPrice(price: Int): String {
        return NumberFormat.getNumberInstance(Locale("id", "ID")).format(price)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}