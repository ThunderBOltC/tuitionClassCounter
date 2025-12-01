package ju.mad.tuitioncounter.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ju.mad.tuitioncounter.domain.model.ClassLogModel
import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TuitionViewModel(
    private val getTuitionListUseCase: GetTuitionListUseCase,
    private val getTuitionDetailsUseCase: GetTuitionDetailsUseCase,
    private val getClassLogsUseCase: GetClassLogsUseCase,
    private val addTuitionUseCase: AddTuitionUseCase,
    private val updateTuitionUseCase: UpdateTuitionUseCase,
    private val deleteTuitionUseCase: DeleteTuitionUseCase,
    private val logClassUseCase: LogClassUseCase,
    private val deleteClassLogUseCase: DeleteClassLogUseCase,
    private val resetClassCountUseCase: ResetClassCountUseCase,
) : ViewModel() {

    // Tuition List State
    val tuitionList = mutableStateOf<List<TuitionModel>>(emptyList())

    // Tuition Details State
    private val _tuitionDetails = MutableStateFlow<TuitionModel?>(null)
    val tuitionDetails: StateFlow<TuitionModel?> = _tuitionDetails.asStateFlow()

    // Class Logs State
    private val _classLogs = MutableStateFlow<List<ClassLogModel>>(emptyList())
    val classLogs: StateFlow<List<ClassLogModel>> = _classLogs.asStateFlow()

    // Get all tuitions
    fun getAllTuitions() {
        viewModelScope.launch {
            getTuitionListUseCase.execute().collect { list ->
                tuitionList.value = list
            }
        }
    }

    // Get tuition details by ID
    fun getTuitionDetails(tuitionId: Long) {
        viewModelScope.launch {
            getTuitionDetailsUseCase.execute(tuitionId).collect { tuition ->
                _tuitionDetails.value = tuition
            }
        }
    }

    // Get class logs for a tuition
    fun getClassLogs(tuitionId: Long) {
        viewModelScope.launch {
            getClassLogsUseCase.execute(tuitionId).collect { logs ->
                _classLogs.value = logs
            }
        }
    }

    // Add a new tuition
    fun addTuition(tuition: TuitionModel) {
        viewModelScope.launch {
            addTuitionUseCase.execute(tuition)
            getAllTuitions()
        }
    }

    // Update existing tuition
    fun updateTuition(tuition: TuitionModel) {
        viewModelScope.launch {
            updateTuitionUseCase.execute(tuition)
            getAllTuitions()
            getTuitionDetails(tuition.id)
        }
    }

    // Delete tuition - FIXED: No longer sets _tuitionDetails to null
    // Navigation is controlled by the UI after this completes
    fun deleteTuition(tuition: TuitionModel) {
        viewModelScope.launch {
            deleteTuitionUseCase.execute(tuition)
            // Just refresh the list, UI handles navigation
            getAllTuitions()
        }
    }

    // Delete a single class log and refresh using correct tuitionId
    fun deleteClassLog(classLogId: Long, tuitionId: Long) {
        viewModelScope.launch {
            deleteClassLogUseCase.execute(classLogId)
            // Refresh class logs and tuition details after deletion
            getClassLogs(tuitionId)
            getTuitionDetails(tuitionId)
        }
    }

    // Reset class count
    fun resetClassCount(tuitionId: Long) {
        viewModelScope.launch {
            resetClassCountUseCase.execute(tuitionId)
            getTuitionDetails(tuitionId)
            getClassLogs(tuitionId)
        }
    }

    // Log a class with customized timestamp
    fun logClass(tuitionId: Long, timestamp: Long = System.currentTimeMillis()) {
        viewModelScope.launch {
            logClassUseCase.execute(tuitionId, timestamp)
            getTuitionDetails(tuitionId)
            getClassLogs(tuitionId)
        }
    }
}