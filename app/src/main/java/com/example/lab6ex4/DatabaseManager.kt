package com.example.lab6ex4

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DatabaseManager {

    /**
     * Saves training data to Firestore under the current user's "trainings" collection.
     *
     * This method adds the provided training data to the Firestore database under the
     * "trainings" subcollection of the currently logged-in user. If the user is not logged in,
     * the operation fails with an appropriate error message.
     *
     * @param trainingData A map containing the training data to be saved. The keys represent
     *                     the field names, and the values represent the corresponding data.
     * @param callback A callback function that is invoked with the result of the operation.
     *                 The first parameter indicates success (`true`) or failure (`false`).
     *                 The second parameter provides an error message in case of failure.
     */
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



