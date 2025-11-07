package com.example.lab_week_09

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

// Object ini akan menjadi pusat konversi data JSON
object JsonConverter {
    // Inisialisasi Moshi dengan adapter Kotlin
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Mengubah List dari objek Student menjadi sebuah String JSON.
     */
    fun fromStudentList(students: List<Student>): String {
        val listType = Types.newParameterizedType(List::class.java, Student::class.java)
        val adapter = moshi.adapter<List<Student>>(listType)
        return adapter.toJson(students)
    }

    /**
     * Mengubah sebuah String JSON kembali menjadi List dari objek Student.
     * Mengembalikan list kosong jika JSON string tidak valid atau kosong.
     */
    fun toStudentList(json: String): List<Student> {
        if (json.isBlank()) return emptyList()
        return try {
            val listType = Types.newParameterizedType(List::class.java, Student::class.java)
            val adapter = moshi.adapter<List<Student>>(listType)
            adapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            // Jika terjadi error saat parsing, kembalikan list kosong
            emptyList()
        }
    }
}
