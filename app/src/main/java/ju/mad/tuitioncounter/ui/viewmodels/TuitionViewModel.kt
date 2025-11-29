package ju.mad.tuitioncounter.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ju.mad.tuitioncounter.domain.model.TuitionModel
import ju.mad.tuitioncounter.domain.repository.TuitionRepository
import ju.mad.tuitioncounter.domain.usecase.GetTuitionListUseCase
import ju.mad.tuitioncounter.domain.usecase.AddTuitionUseCase
import kotlinx.coroutines.launch

class TuitionViewModel(
    private val getTuitionListUseCase: GetTuitionListUseCase,
    private val addTuitionUseCase: AddTuitionUseCase,
    private val tuitionRepository: TuitionRepository
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
        }
    }
    fun resetClassCount(tuitionId: Long) {
        viewModelScope.launch {
            tuitionRepository.resetClassCount(tuitionId) // Call the reset method in the repository
        }
    }

    fun deleteTuition(tuition: TuitionModel) {
        viewModelScope.launch {
            tuitionRepository.deleteTuition(tuition)
        }
    }
}
