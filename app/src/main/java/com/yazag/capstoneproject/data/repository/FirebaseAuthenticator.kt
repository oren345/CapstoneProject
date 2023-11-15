package com.yazag.capstoneproject.data.repository


import com.google.firebase.auth.FirebaseAuth
import com.yazag.capstoneproject.common.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthenticator @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    fun getFirebaseUserUid(): String = firebaseAuth.currentUser?.uid.orEmpty()
    fun isCurrentUserExist() = firebaseAuth.currentUser != null//splash için kullancaz

    suspend fun signIn(email: String, password: String): Resource<Unit> {

        return try {

            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            if (result.user != null) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Bir hata oluştu")
            }
        } catch (e: Exception) {
            Resource.Error("Böyle bir kullanıcı yok")
        }
    }

    suspend fun signUp(email: String, password: String): Resource<Unit> {

        return try {

            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            if (result.user != null) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Bir hata oluştu")
            }
        } catch (e: Exception) {
            Resource.Error("Bir hata oluştu")
        }
    }

    fun signOut() = firebaseAuth.signOut()
}
