package com.example.booksport.ui.booking

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.booksport.MainActivity
import com.example.booksport.data.VenueRepository
import com.example.booksport.databinding.FragmentBookingFormBinding
import com.example.booksport.model.Booking
import com.example.booksport.model.Venue
import com.example.booksport.ui.confirmation.ConfirmationFragment
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class BookingFormFragment : Fragment() {

    private var _binding: FragmentBookingFormBinding? = null
    private val binding get() = _binding!!
    private lateinit var venue: Venue
    private var selectedDate = ""
    private var selectedTime = ""
    private var selectedDuration = 0

    companion object {
        private const val ARG_VENUE_ID = "venue_id"
        fun newInstance(venueId: Int) = BookingFormFragment().apply {
            arguments = Bundle().apply { putInt(ARG_VENUE_ID, venueId) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val venueId = arguments?.getInt(ARG_VENUE_ID) ?: return
        venue = VenueRepository.getVenueById(venueId) ?: return

        setupHeader()
        setupSportInfo()
        setupDatePicker()
        setupTimePicker()
        setupDurationPicker()
        setupBookButton()

        binding.btnBack.setOnClickListener { requireActivity().onBackPressed() }
        binding.btnCancelForm.setOnClickListener { requireActivity().onBackPressed() }
    }

    private fun setupHeader() {
        binding.tvVenueName.text = venue.name
        binding.tvVenueInfo.text = "${venue.type} · Rp ${formatPrice(venue.pricePerHour)}/jam"
    }

    private fun setupSportInfo() {
        val icon = when (venue.type) {
            "Futsal"    -> "⚽"
            "Badminton" -> "🏸"
            "Basket"    -> "🏀"
            "Tenis"     -> "🎾"
            else        -> "🏅"
        }
        binding.tvSportIcon.text = icon
        binding.tvSportName.text = venue.type
    }

    private fun setupDatePicker() {
        binding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val formatted = String.format("%02d/%02d/%04d", day, month + 1, year)
                    val isoFormat = String.format("%04d-%02d-%02d", year, month + 1, day)
                    binding.etDate.setText(formatted)
                    selectedDate = isoFormat
                    binding.tilDate.error = null
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.minDate = calendar.timeInMillis
            }.show()
        }
    }

    private fun setupTimePicker() {
        binding.btnPickTime.setOnClickListener {
            TimePickerBottomSheet { time ->
                selectedTime = time
                binding.tvSelectedTime.text = time
                binding.tvSelectedTime.setTextColor(
                    resources.getColor(com.example.booksport.R.color.text_primary, null)
                )
                binding.tvErrorTime.visibility = View.GONE
                updateTotalPrice()
            }.show(parentFragmentManager, "time_picker")
        }
    }

    private fun setupDurationPicker() {
        binding.btnPickDuration.setOnClickListener {
            DurationPickerBottomSheet(venue.pricePerHour) { duration ->
                selectedDuration = duration
                binding.tvSelectedDuration.text = "$duration jam"
                binding.tvSelectedDuration.setTextColor(
                    resources.getColor(com.example.booksport.R.color.text_primary, null)
                )
                binding.tvErrorDuration.visibility = View.GONE
                updateTotalPrice()
            }.show(parentFragmentManager, "duration_picker")
        }
    }

    private fun updateTotalPrice() {
        val total = venue.pricePerHour * selectedDuration
        binding.tvTotalPrice.text = "Rp ${formatPrice(total)}"
    }

    private fun setupBookButton() {
        binding.btnBook.setOnClickListener { validateAndSubmit() }
    }

    private fun validateAndSubmit() {
        var isValid = true

        val name = binding.etName.text.toString().trim()
        if (name.isEmpty()) {
            binding.tilName.error = "Nama tidak boleh kosong"
            isValid = false
        } else binding.tilName.error = null

        val phone = binding.etPhone.text.toString().trim().replace(Regex("[-\\s]"), "")
        if (phone.isEmpty() || !phone.matches(Regex("^0\\d{8,12}$"))) {
            binding.tilPhone.error = "Format nomor tidak valid (contoh: 08123456789)"
            isValid = false
        } else binding.tilPhone.error = null

        if (selectedDate.isEmpty()) {
            binding.tilDate.error = "Pilih tanggal pemesanan"
            isValid = false
        } else binding.tilDate.error = null

        if (selectedTime.isEmpty()) {
            binding.tvErrorTime.visibility = View.VISIBLE
            isValid = false
        } else binding.tvErrorTime.visibility = View.GONE

        if (selectedDuration == 0) {
            binding.tvErrorDuration.visibility = View.VISIBLE
            isValid = false
        } else binding.tvErrorDuration.visibility = View.GONE

        if (!isValid) return

        val startHour = selectedTime.split(":")[0].toInt()
        val endHour = startHour + selectedDuration
        val endTime = String.format("%02d:00", endHour)
        val totalPrice = venue.pricePerHour * selectedDuration
        val bookingId = "BSP-${System.currentTimeMillis().toString().takeLast(6)}"

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfDisplay = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id"))
        val dateDisplay = try {
            sdfDisplay.format(sdf.parse(selectedDate)!!)
        } catch (e: Exception) { selectedDate }

        val booking = Booking(
            bookingId = bookingId,
            customerName = name,
            phoneNumber = binding.etPhone.text.toString().trim(),
            sportType = venue.type,
            venue = venue,
            date = dateDisplay,
            startTime = selectedTime,
            endTime = endTime,
            durationHours = selectedDuration,
            totalPrice = totalPrice,
            notes = binding.etNotes.text.toString().trim()
        )

        val fragment = ConfirmationFragment.newInstance(booking)
        (activity as MainActivity).navigateTo(fragment, addToBackStack = false)
    }

    private fun formatPrice(price: Int): String {
        return NumberFormat.getNumberInstance(Locale("id", "ID")).format(price)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
