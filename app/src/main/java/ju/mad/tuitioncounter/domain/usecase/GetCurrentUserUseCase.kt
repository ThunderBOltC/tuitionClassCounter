package ju.mad.tuitioncounter.domain.usecase

import ju.mad.tuitioncounter.domain.model.UserData
import ju.mad.tuitioncounter.domain.repository.AuthRepository

class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(): UserData? {
        return authRepository.getCurrentUser()
    }
}