import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.csslearn.models.QuestionModel
import com.example.csslearn.RetrofitClient

// View model para "juntar" las vistas con los datos de la API
class QuestionViewModel : ViewModel() {
    private val _questions = MutableStateFlow<List<QuestionModel>>(emptyList())
    val questions: StateFlow<List<QuestionModel>> get() = _questions.asStateFlow()

    fun loadQuestions() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getAllQuestions()
                _questions.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createQuestion(question: QuestionModel) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.createQuestion(question)
                _questions.value = _questions.value + response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteQuestion(questionId: String) {
        viewModelScope.launch {
            try {
                RetrofitClient.instance.deleteQuestion(questionId)
                _questions.value = _questions.value.filter { it.id != questionId }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateQuestion(updatedQuestion: QuestionModel) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.updateQuestion(
                    updatedQuestion.id!!,
                    updatedQuestion
                )
                _questions.value = _questions.value.map {
                    if (it.id == updatedQuestion.id) response else it
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}