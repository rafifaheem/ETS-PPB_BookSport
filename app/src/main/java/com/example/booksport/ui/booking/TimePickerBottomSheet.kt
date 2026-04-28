package com.example.booksport.ui.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import com.example.booksport.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TimePickerBottomSheet(
    private val onTimeSelected: (String) -> Unit
) : BottomSheetDialogFragment() {

    private val timeSlots = listOf(
        "07:00", "08:00", "09:00", "10:00",
        "11:00", "13:00", "14:00", "15:00",
        "16:00", "19:00", "20:00", "21:00"
    )

    private var selectedTime = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_time_picker, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val grid = view.findViewById<GridLayout>(R.id.grid_times)
        grid.removeAllViews()

        timeSlots.forEach { time ->
            val chip = TextView(requireContext()).apply {
                text = time
                textSize = 14f
                gravity = android.view.Gravity.CENTER
                setTextColor(resources.getColor(R.color.text_primary, null))
                setBackgroundResource(R.drawable.bg_time_chip_normal)

                val params = GridLayout.LayoutParams().apply {
                    width = 0
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f)
                    setMargins(8, 8, 8, 8)
                }
                layoutParams = params
                setPadding(0, 20, 0, 20)

                setOnClickListener {
                    // Reset all chips
                    for (i in 0 until grid.childCount) {
                        val child = grid.getChildAt(i) as TextView
                        child.setBackgroundResource(R.drawable.bg_time_chip_normal)
                        child.setTextColor(resources.getColor(R.color.text_primary, null))
                    }
                    // Select this chip
                    setBackgroundResource(R.drawable.bg_time_chip_selected)
                    setTextColor(resources.getColor(android.R.color.white, null))
                    selectedTime = time

                    // Delay dismiss so user sees selection
                    postDelayed({
                        onTimeSelected(time)
                        dismiss()
                    }, 200)
                }
            }
            grid.addView(chip)
        }
    }

    override fun getTheme() = com.google.android.material.R.style.Theme_MaterialComponents_BottomSheetDialog
}
