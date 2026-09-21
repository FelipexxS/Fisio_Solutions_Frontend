package br.com.fisiosolutions.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.fisiosolutions.R
import br.com.fisiosolutions.domain.model.Exercise

data class ErgonomicsTip(
    val id: String,
    val title: String,
    val imageResId: Int,
    val content: String
)

object AppState {
    var currentSelectedCategory by mutableStateOf("Coluna")

    val categories = listOf("Coluna", "Pescoço", "Braços", "Pernas")

    val allExercises = listOf(
        // Categoria: Coluna (5 exercícios reais)
        Exercise(
            id = "coluna_1",
            title = "Cat-Cow (Gato e Vaca)",
            category = "Coluna",
            imageResId = R.drawable.exercise_ilustration_1,
            isSaved = true,
            tutorialTitle = "MOBILIDADE E ALÍVIO ESPINHAL",
            instructions = "Apoie os joelhos e mãos no chão em quatro apoios. Ao inspirar, afunde a coluna suavemente em direção ao chão e eleve a cabeça (posição da vaca). Ao expirar, curve a coluna para cima em direção ao teto, recolhendo o pescoço (posição do gato). Repita suavemente de 10 a 12 vezes."
        ),
        Exercise(
            id = "coluna_2",
            title = "Ponte Pélvica (Glute Bridge)",
            category = "Coluna",
            imageResId = R.drawable.exercise_ilustration_2,
            isSaved = true,
            tutorialTitle = "FORTALECIMENTO LOMBAR E GLÚTEOS",
            instructions = "Deite-se de costas com os joelhos dobrados e pés firmes no chão. Contraia o abdômen e eleve o quadril até formar uma linha reta dos ombros aos joelhos. Mantenha a posição por 3 a 5 segundos e retorne devagar. Faça 3 séries de 10 repetições."
        ),
        Exercise(
            id = "coluna_3",
            title = "Abraço de Joelhos no Peito",
            category = "Coluna",
            imageResId = R.drawable.exercise_ilustration_3,
            isSaved = true,
            tutorialTitle = "DESCOMPRESSÃO E ALÍVIO LOMBAR",
            instructions = "Deite-se de costas em uma superfície confortável. Dobre os dois joelhos e traga-os em direção ao peito, abraçando as pernas com as mãos. Mantenha a lombar bem apoiada e faça respirações profundas por 30 segundos."
        ),
        Exercise(
            id = "coluna_4",
            title = "Exercício do Super-Homem",
            category = "Coluna",
            imageResId = R.drawable.exercise_ilustration_4,
            isSaved = false,
            tutorialTitle = "FORTALECIMENTO DA MUSCULATURA DORSAL",
            instructions = "Deite-se de barriga para baixo com os braços estendidos à frente. Eleve o peito, os braços e as pernas a poucos centímetros do chão, mantendo o pescoço neutro. Mantenha por 3 segundos e retorne. Faça 3 séries de 8 repetições."
        ),
        Exercise(
            id = "coluna_5",
            title = "Rotação de Tronco Deitado",
            category = "Coluna",
            imageResId = R.drawable.exercise_ilustration_5,
            isSaved = false,
            tutorialTitle = "MOBILIDADE TORÁCICA E LOMBAR",
            instructions = "Deite-se de costas com os braços abertos em T. Dobre os joelhos e deixe-os cair suavemente para o lado direito, enquanto olha para o lado esquerdo. Mantenha por 20 a 30 segundos e inverta o lado."
        ),

        // Categoria: Pescoço (4 exercícios reais)
        Exercise(
            id = "pescoco_1",
            title = "Inclinação Lateral do Pescoço",
            category = "Pescoço",
            imageResId = R.drawable.exercise_ilustration_1,
            isSaved = true,
            tutorialTitle = "ALONGAMENTO DO TRAPÉZIO SUPERIOR",
            instructions = "Sentado ereto, incline a cabeça trazendo a orelha direita em direção ao ombro direito. Use a mão direita para aplicar uma leve e suave pressão sobre a cabeça. Mantenha por 25 segundos e repita no lado esquerdo."
        ),
        Exercise(
            id = "pescoco_2",
            title = "Retração Cervical (Queixo Duplo)",
            category = "Pescoço",
            imageResId = R.drawable.exercise_ilustration_2,
            isSaved = true,
            tutorialTitle = "CORREÇÃO DE POSTURA CERVICAL",
            instructions = "Sentado com as costas retas, deslize a cabeça horizontalmente para trás (como se quisesse fazer um queixo duplo), mantendo o olhar para a frente. Segure por 3 segundos e relaxe. Realize 10 repetições."
        ),
        Exercise(
            id = "pescoco_3",
            title = "Rotação Cervical Suave",
            category = "Pescoço",
            imageResId = R.drawable.exercise_ilustration_3,
            isSaved = false,
            tutorialTitle = "MOBILIDADE DAS VERTEBRAS CERVICAIS",
            instructions = "Mantenha os ombros relaxados e gire a cabeça devagar olhando por cima do ombro direito até sentir um alongamento leve. Mantenha por 15 segundos e gire para o esquerdo. Repita 3 vezes de cada lado."
        ),
        Exercise(
            id = "pescoco_4",
            title = "Alongamento Flexor Anterior",
            category = "Pescoço",
            imageResId = R.drawable.exercise_ilustration_4,
            isSaved = false,
            tutorialTitle = "ALÍVIO DE TENSÃO ANTERIOR",
            instructions = "Sentado com a postura ereta, incline suavemente a cabeça para trás, elevando o queixo em direção ao teto. Abra e feche a boca devagar para intensificar o alongamento na parte frontal do pescoço. Mantenha por 20 segundos."
        ),

        // Categoria: Braços (4 exercícios reais)
        Exercise(
            id = "bracos_1",
            title = "Alongamento de Flexores do Punho",
            category = "Braços",
            imageResId = R.drawable.exercise_ilustration_4,
            isSaved = true,
            tutorialTitle = "PREVENÇÃO DE LER/DORT NO PUNHO",
            instructions = "Estenda o braço direito à frente com a palma voltada para cima. Com a mão esquerda, puxe os dedos para baixo e para trás em direção ao corpo, mantendo o cotovelo esticado. Mantenha por 25 segundos de cada lado."
        ),
        Exercise(
            id = "bracos_2",
            title = "Alongamento de Tríceps sobre a Cabeça",
            category = "Braços",
            imageResId = R.drawable.exercise_ilustration_3,
            isSaved = true,
            tutorialTitle = "ALONGAMENTO DE TRÍCEPS E OMBROS",
            instructions = "Eleve o braço direito, dobre o cotovelo e leve a mão atrás das costas. Com a mão esquerda, segure o cotovelo direito e puxe-o suavemente para trás. Mantenha a posição por 20 segundos em cada braço."
        ),
        Exercise(
            id = "bracos_3",
            title = "Alongamento Cruzado de Ombro",
            category = "Braços",
            imageResId = R.drawable.exercise_ilustration_1,
            isSaved = false,
            tutorialTitle = "ALÍVIO DE TENSÃO NOS OMBROS",
            instructions = "Traga o braço direito estendido horizontalmente sobre o peito. Com o braço esquerdo, pressione-o suavemente contra o corpo até sentir o alongamento no ombro e tríceps. Mantenha por 20 segundos de cada lado."
        ),
        Exercise(
            id = "bracos_4",
            title = "Rotação de Punhos e Antebraço",
            category = "Braços",
            imageResId = R.drawable.exercise_ilustration_2,
            isSaved = false,
            tutorialTitle = "MOBILIDADE ARTICULAR PARA DIGITADORES",
            instructions = "Feche as mãos levemente em punho e faça movimentos circulares lentos 10 vezes no sentido horário e 10 vezes no sentido anti-horário. Abra e feche a palma das mãos energicamente 15 vezes."
        ),

        // Categoria: Pernas (4 exercícios reais)
        Exercise(
            id = "pernas_1",
            title = "Alongamento de Isquiotibiais",
            category = "Pernas",
            imageResId = R.drawable.exercise_ilustration_5,
            isSaved = true,
            tutorialTitle = "ALONGAMENTO DA CADEIA POSTERIOR",
            instructions = "Sentado na ponta de uma cadeira, estenda uma perna à frente apoiando o calcanhar no chão. Mantenha a coluna ereta e incline o tronco suavemente para a frente a partir do quadril. Mantenha por 30 segundos em cada perna."
        ),
        Exercise(
            id = "pernas_2",
            title = "Alongamento de Panturrilha na Parede",
            category = "Pernas",
            imageResId = R.drawable.exercise_ilustration_1,
            isSaved = true,
            tutorialTitle = "ALÍVIO DE TENSÃO NAS PANTURRILHAS",
            instructions = "Apoie as mãos em uma parede e dê um passo para trás com a perna direita, mantendo o calcanhar no chão e o joelho esticado. Dobre o joelho esquerdo para a frente até sentir a panturrilha direita. Mantenha 30 segundos de cada lado."
        ),
        Exercise(
            id = "pernas_3",
            title = "Alongamento de Quadríceps em Pé",
            category = "Pernas",
            imageResId = R.drawable.exercise_ilustration_2,
            isSaved = false,
            tutorialTitle = "ALONGAMENTO DA PARTE ANTERIOR DA COXA",
            instructions = "Em pé, apoie uma mão na parede para equilíbrio. Dobre o joelho direito trazendo o calcanhar ao glúteo e segure o tornozelo com a mão direita. Mantenha os joelhos alinhados e o corpo ereto por 25 segundos. Inverta o lado."
        ),
        Exercise(
            id = "pernas_4",
            title = "Elevação de Calcanhares",
            category = "Pernas",
            imageResId = R.drawable.exercise_ilustration_3,
            isSaved = false,
            tutorialTitle = "FORTALECIMENTO E CIRCULAÇÃO DAS PERNAS",
            instructions = "Em pé com os pés afastados na largura do quadril, eleve-se sobre a ponta dos pés contraindo a panturrilha no topo. Desça suavemente até encostar o calcanhar no chão. Realize 3 séries de 12 repetições."
        )
    )

    val allErgonomicsTips = listOf(
        ErgonomicsTip(
            id = "tip_1",
            title = "Altura Correta do Monitor e Distância dos Olhos",
            imageResId = R.drawable.ergonomics_banner,
            content = "Posicionar o monitor adequadamente previne dores no pescoço e fadiga visual durante longas jornadas de trabalho.\n\nPrincipais orientações:\n1. Mantenha o topo da tela na altura dos seus olhos ou ligeiramente abaixo.\n2. A distância entre a tela e seus olhos deve ser equivalente ao comprimento do seu braço (aproximadamente 50 a 70 cm).\n3. Incline levemente a tela para trás (10 a 20 graus) para evitar reflexos da iluminação ambiente."
        ),
        ErgonomicsTip(
            id = "tip_2",
            title = "Ajuste da Cadeira e Postura Sentada",
            imageResId = R.drawable.exercise_ilustration_1,
            content = "Uma cadeira bem regulada distribui o peso do corpo de forma uniforme e reduz a pressão sobre a coluna vertebral.\n\nComo ajustar sua cadeira:\n1. Ajuste a altura do assento para que os joelhos fiquem dobrados em um ângulo de 90 graus e ambos os pés fiquem totalmente apoiados no chão.\n2. Utilize o suporte lombar da cadeira para manter a curva natural da parte inferior das costas.\n3. Ajuste os apoios de braço para que os cotovelos fiquem em 90 graus e os ombros permanecam relaxados."
        ),
        ErgonomicsTip(
            id = "tip_3",
            title = "Posicionamento do Teclado e Mouse (Prevenção LER/DORT)",
            imageResId = R.drawable.exercise_ilustration_2,
            content = "LER (Lesão por Esforço Repetitivo) e DORT (Distúrbios Osteomusculares Relacionados ao Trabalho) são frequentes em quem digita por muitas horas.\n\nRecomendações fundamentais:\n1. Mantenha o teclado e o mouse no mesmo nível e próximos ao corpo.\n2. Evite dobrar os punhos para cima ou para os lados durante a digitação; os punhos devem permanecer neutros e retos.\n3. Utilize descansos de punho macios e mouses ergonômicos ou verticais se sentir desconforto frequente."
        ),
        ErgonomicsTip(
            id = "tip_4",
            title = "A Regra 20-20-20 para Alívio do Cansaço Visual",
            imageResId = R.drawable.exercise_ilustration_3,
            content = "Olhar para telas por longos períodos reduz a frequência de piscadas, gerando ressecamento nos olhos e dores de cabeça.\n\nComo praticar a regra 20-20-20:\n1. A cada 20 minutos de trabalho em frente à tela...\n2. Olhe para um objeto ou ponto situado a pelo menos 6 metros (20 pés) de distância.\n3. Mantenha o foco nesse ponto distante por 20 segundos.\nIsso permite que os músculos oculares relaxem completamente."
        ),
        ErgonomicsTip(
            id = "tip_5",
            title = "Pausas Ativas e Microintervalos Durante a Jornada",
            imageResId = R.drawable.exercise_ilustration_4,
            content = "Permanecer na mesma posição por mais de 1 hora consecutiva reduz a circulação sanguínea e aumenta a rigidez muscular.\n\nHábitos saudáveis diários:\n1. Faça micropausas de 2 a 3 minutos a cada hora para se levantar, caminhar um pouco e beber água.\n2. Realize alongamentos leves de pescoço, ombros e punhos durante as pausas.\n3. Alterne entre trabalhar sentado e em pé caso possua uma mesa com regulagem de altura."
        )
    )

    fun getExerciseById(id: String): Exercise? {
        return allExercises.find { it.id == id }
    }

    fun getErgonomicsTipById(id: String): ErgonomicsTip? {
        return allErgonomicsTips.find { it.id == id }
    }
}

