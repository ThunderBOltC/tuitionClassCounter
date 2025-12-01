package ju.mad.tuitioncounter.domain.model


/**
 * Represents the result of a sign-in or sign-up attempt.
 */
data class AuthResult(
    val data: UserData? = null,
    val errorMessage: String? = null
)

/**
 * Simple data class for logged-in user information
 */
data class UserData(
    val userId: String,
    val username: String?,
    val email: String?
)

/**
 * Represents the entire state of the authentication UI
 */
data class AuthState(
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val loginError: String? = null,
    val isRegistrationSuccess: Boolean = false,
    val registrationError: String? = null,
    val currentUser: UserData? = null
)