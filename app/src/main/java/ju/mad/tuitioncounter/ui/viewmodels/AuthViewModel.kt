// File: ui/viewmodels/AuthViewModel.kt
package ju.mad.tuitioncounter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ju.mad.tuitioncounter.domain.model.AuthResult
import ju.mad.tuitioncounter.domain.model.AuthState
import ju.mad.tuitioncounter.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val sendPasswordResetUseCase: SendPasswordResetUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _resetPasswordState = MutableStateFlow<AuthResult?>(null)
    val resetPasswordState: StateFlow<AuthResult?> = _resetPasswordState.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val user = getCurrentUserUseCase()
        _authState.update { it.copy(currentUser = user) }
    }

    fun signUp(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, registrationError = null) }

            val result = signUpUseCase(email, password, displayName)

            _authState.update {
                if (result.data != null) {
                    it.copy(
                        isLoading = false,
                        isRegistrationSuccess = true,
                        currentUser = result.data
                    )
                } else {
                    it.copy(
                        isLoading = false,
                        registrationError = result.errorMessage
                    )
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, loginError = null) }

            val result = loginUseCase(email, password)

            _authState.update {
                if (result.data != null) {
                    it.copy(
                        isLoading = false,
                        isLoginSuccess = true,
                        currentUser = result.data
                    )
                } else {
                    it.copy(
                        isLoading = false,
                        loginError = result.errorMessage
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val result = logoutUseCase()

            _authState.update {
                if (result.errorMessage == null) {
                    AuthState() // Reset to initial state
                } else {
                    it.copy(loginError = result.errorMessage)
                }
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            val result = sendPasswordResetUseCase(email)
            _resetPasswordState.value = result
        }
    }

    fun resetPasswordState() {
        _resetPasswordState.value = null
    }

    fun resetAuthState() {
        _authState.value = AuthState()
    }

    fun clearError() {
        _authState.update { it.copy(loginError = null, registrationError = null) }
    }
}