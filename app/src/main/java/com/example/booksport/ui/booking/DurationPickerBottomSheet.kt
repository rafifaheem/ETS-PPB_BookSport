package com.example.booksport.ui.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.booksport.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.NumberFormat
import java.util.Locale

class DurationPickerBottomSheet(
    private val pricePerHour: Int,
    private val onDurationSelected: (Int) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_duration_picker, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fmt = NumberFormat.getNumberInstance(Locale("id", "ID"))

        // Set prices
        view.findViewById<TextView>(R.id.tv_price_1jam).text =
            "Rp ${fmt.format(pricePerHour * 1)}"
        view.findViewById<TextView>(R.id.tv_price_2jam).text =
            "Rp ${fmt.format(pricePerHour * 2)}"
        view.findViewById<TextView>(R.id.tv_price_3jam).text =
            "Rp ${fmt.format(pricePerHour * 3)}"

        // Click listeners with visual feedback
        setupOption(view, R.id.opt_1jam, 1)
        setupOption(view, R.id.opt_2jam, 2)
        setupOption(view, R.id.opt_3jam, 3)
    }

    private fun setupOption(view: View, optId: Int, duration: Int) {
        view.findViewById<LinearLayout>(optId).setOnClickListener {
            // Highlight selected
            listOf(R.id.opt_1jam, R.id.opt_2jam, R.id.opt_3jam).forEach { id ->
                view.findViewById<LinearLayout>(id)
                    .setBackgroundResource(R.drawable.bg_duration_option)
            }
            it.setBackgroundResource(R.drawable.bg_duration_option_selected)

            it.postDelayed({
                onDurationSelected(duration)
                dismiss()
            }, 200)
        }
    }

    override fun getTheme() = com.google.android.material.R.style.Theme_MaterialComponents_BottomSheetDialog
}
