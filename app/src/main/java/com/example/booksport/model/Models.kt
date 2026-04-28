package com.example.booksport.model

import java.io.Serializable

data class Venue(
    val id: Int,
    val name: String,
    val type: String,
    val address: String,
    val pricePerHour: Int,
    val isAvailable: Boolean
) : Serializable

data class Booking(
    val bookingId: String,
    val customerName: String,
    val phoneNumber: String,
    val sportType: String,
    val venue: Venue,
    val date: String,
    val startTime: String,
    val endTime: String,
    val durationHours: Int,
    val totalPrice: Int,
    val notes: String = ""
) : Serializable