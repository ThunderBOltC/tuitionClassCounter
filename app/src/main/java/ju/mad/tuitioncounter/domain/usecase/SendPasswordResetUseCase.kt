package ju.mad.tuitioncounter.domain.usecase

import ju.mad.tuitioncounter.domain.model.AuthResult
import ju.mad.tuitioncounter.domain.repository.AuthRepository

class SendPasswordResetUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String): AuthResult {
        return authRepository.sendPasswordResetEmail(email)
    }
}