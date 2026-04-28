package com.example.booksport.data

import com.example.booksport.model.Venue

object VenueRepository {

    fun getAllVenues(): List<Venue> = listOf(
        Venue(1, "GOR Sidoarjo Sport Center", "Futsal",
            "Jl. Pahlawan No.12, Sidoarjo", 80000, true),
        Venue(2, "Arena Badminton Maju Jaya", "Badminton",
            "Jl. Jenggolo No.45, Sidoarjo", 60000, true),
        Venue(3, "Delta Basket Court", "Basket",
            "Jl. Diponegoro No.7, Sidoarjo", 100000, false),
        Venue(4, "Lapangan Futsal Buana", "Futsal",
            "Jl. Raya Buduran No.3, Sidoarjo", 75000, true),
        Venue(5, "Club Tenis Sidoarjo", "Tenis",
            "Jl. Ahmad Yani No.99, Sidoarjo", 90000, true)
    )

    fun getVenuesByType(type: String): List<Venue> {
        if (type == "Semua") return getAllVenues()
        return getAllVenues().filter { it.type == type }
    }

    fun getVenueById(id: Int): Venue? = getAllVenues().find { it.id == id }
}