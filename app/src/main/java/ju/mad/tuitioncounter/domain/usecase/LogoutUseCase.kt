package ju.mad.tuitioncounter.domain.usecase

import ju.mad.tuitioncounter.domain.model.AuthResult
import ju.mad.tuitioncounter.domain.repository.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): AuthResult {
        return authRepository.logout()
    }
}