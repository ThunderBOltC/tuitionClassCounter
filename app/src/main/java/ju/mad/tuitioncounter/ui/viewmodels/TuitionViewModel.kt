package ju.mad.tuitioncounter.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.domain.usecase.AddTuitionUseCase
import ju.mad.tuitioncounter.domain.usecase.DeleteTuitionUseCase
import ju.mad.tuitioncounter.domain.usecase.GetTuitionListUseCase
import ju.mad.tuitioncounter.domain.usecase.ResetClassCountUseCase
import ju.mad.tuitioncounter.domain.usecase.UpdateTuitionUseCase
import kotlinx.coroutines.launch

class TuitionViewModel(
    private val getTuitionListUseCase: GetTuitionListUseCase,
    private val addTuitionUseCase: AddTuitionUseCase,
    private val updateTuitionUseCase: UpdateTuitionUseCase,
    private val deleteTuitionUseCase: DeleteTuitionUseCase,
    private val resetClassCountUseCase: ResetClassCountUseCase
) : ViewModel() {

    val tuitionList = mutableStateOf<List<TuitionModel>>(emptyList())

    fun getAllTuitions() {
        viewModelScope.launch {
            getTuitionListUseCase.execute().collect { list ->
                tuitionList.value = list
            }
        }
    }

    fun addTuition(tuition: TuitionModel) {
        viewModelScope.launch {
            addTuitionUseCase.execute(tuition)
            getAllTuitions() // Refresh the list
        }
    }

    fun updateTuition(tuition: TuitionModel) {
        viewModelScope.launch {
            updateTuitionUseCase.execute(tuition)
            getAllTuitions() // Refresh the list
        }
    }

    fun deleteTuition(tuition: TuitionModel) {
        viewModelScope.launch {
            deleteTuitionUseCase.execute(tuition)
            getAllTuitions() // Refresh the list
        }
    }

    fun resetClassCount(tuitionId: Long) {
        viewModelScope.launch {
            resetClassCountUseCase.invoke(tuitionId)
            getAllTuitions() // Refresh the list
        }
    }
}