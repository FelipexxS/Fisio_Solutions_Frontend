package br.com.fisiosolutions.domain.model

data class Exercise(
    val id: String,
    val numericId: Int = 0,
    val title: String,
    val category: String,
    val imageResId: Int,
    val isSaved: Boolean = false,
    val isCompletedToday: Boolean = false,
    val tutorialTitle: String = "ALONGAMENTO DE QUADRIL E LOMBAR",
    val instructions: String = "Para alongar, deitar de barriga para cima e dobrar os joelhos mantendo os pés no chão, ou deixando uma perna esticada. Com a ajuda das mãos, trazer um joelho em direção ao peito, mantendo essa posição por cerca de 15 segundos. Fazer o mesmo com a outra perna, repetindo o movimento por 2 vezes em cada perna.\n\nLembre-se de fazer os movimentos de forma controlada, respeitando seus limites e evitando dor intensa, e se a dor persistir, consulte um médico.",
    val objetivo: String = "alongamento",
    val repeticaoSugerida: String = "10 a 12 repetições",
    val duracaoSegundos: Int = 30
)

