package ju.mad.tuitioncounter.domain.usecase

import ju.mad.tuitioncounter.domain.model.AuthResult
import ju.mad.tuitioncounter.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): AuthResult {
        return authRepository.login(email, password)
    }
}