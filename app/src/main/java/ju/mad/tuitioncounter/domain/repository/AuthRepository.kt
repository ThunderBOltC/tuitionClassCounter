// File: domain/repository/AuthRepository.kt
package ju.mad.tuitioncounter.domain.repository

import ju.mad.tuitioncounter.domain.model.AuthResult
import ju.mad.tuitioncounter.domain.model.UserData

interface AuthRepository {
    suspend fun signUp(email: String, password: String, displayName: String): AuthResult
    suspend fun login(email: String, password: String): AuthResult
    suspend fun logout(): AuthResult
    fun getCurrentUser(): UserData?
    suspend fun sendPasswordResetEmail(email: String): AuthResult
}