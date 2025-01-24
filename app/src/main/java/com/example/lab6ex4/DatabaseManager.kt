package com.example.lab6ex4

import Trainings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DatabaseManager {

    fun saveTraining(trainingData: Map<String, Any>, callback: (Boolean, String?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .collection("trainings")
                .add(trainingData)

                .addOnSuccessListener {
                    callback(true, null) // Success
                }
                .addOnFailureListener { e ->
                    callback(false, e.message) // Error
                }
        } else {
            callback(false, "User not logged in.")
        }
    }
}



