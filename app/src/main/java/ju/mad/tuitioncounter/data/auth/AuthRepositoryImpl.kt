// File: data/auth/AuthRepositoryImpl.kt
package ju.mad.tuitioncounter.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ju.mad.tuitioncounter.domain.model.AuthResult
import ju.mad.tuitioncounter.domain.model.UserData
import ju.mad.tuitioncounter.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth = Firebase.auth
) : AuthRepository {

    override suspend fun signUp(email: String, password: String, displayName: String): AuthResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return AuthResult(errorMessage = "User creation failed")

            // Update profile with display name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()
            user.updateProfile(profileUpdates).await()

            AuthResult(data = UserData(user.uid, user.displayName, user.email))
        } catch (e: Exception) {
            AuthResult(errorMessage = e.message ?: "Sign up failed")
        }
    }

    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: return AuthResult(errorMessage = "Login failed")

            AuthResult(data = UserData(user.uid, user.displayName, user.email))
        } catch (e: Exception) {
            AuthResult(errorMessage = e.message ?: "Login failed")
        }
    }

    override suspend fun logout(): AuthResult {
        return try {
            firebaseAuth.signOut()
            AuthResult(data = null)
        } catch (e: Exception) {
            AuthResult(errorMessage = e.message ?: "Logout failed")
        }
    }

    override fun getCurrentUser(): UserData? {
        val user = firebaseAuth.currentUser
        return user?.let {
            UserData(it.uid, it.displayName, it.email)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): AuthResult {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            AuthResult(data = UserData(userId = "", username = null, email = null))
        } catch (e: Exception) {
            AuthResult(errorMessage = e.message ?: "Failed to send reset email")
        }
    }
}