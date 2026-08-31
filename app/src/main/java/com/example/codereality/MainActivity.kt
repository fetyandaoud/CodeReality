package com.example.codereality

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.codereality.ui.theme.CodeRealityTheme

data class InlineImage(
    val afterText: String,
    val imageRes: Int
)

data class LearningPage(
    val title: String,
    val content: String,
    val imageResources: List<Int> = emptyList(),
    val imagePlacements: List<InlineImage> = emptyList()
)

data class Topic(
    val title: String,
    val pages: List<LearningPage>,
    val color: Color
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

private val beginnerQuickQuizzes = mapOf(
    "Variables" to listOf(
        QuizQuestion(
            question = "A variable can be compared to:",
            options = listOf(
                "A traffic light",
                "A box where information can be stored",
                "A road intersection",
                "A button"
            ),
            correctIndex = 1,
            explanation = "A variable can be seen as a box where information or a value can be stored."
        ),
        QuizQuestion(
            question = "If a box first contains the number 10 and is then replaced with the number 25, what value is in the box?",
            options = listOf(
                "10",
                "25",
                "10 and 25",
                "No value"
            ),
            correctIndex = 1,
            explanation = "When the value is replaced, the variable contains the new value, which is 25."
        )
    ),
    "Data Types" to listOf(
        QuizQuestion(
            question = "What does a data type describe?",
            options = listOf(
                "What kind of value a variable can store",
                "How many times a loop runs",
                "Which function is called first",
                "How a list is sorted"
            ),
            correctIndex = 0,
            explanation = "A data type describes what kind of value can be stored, such as an integer, decimal number, text, or true/false value."
        ),
        QuizQuestion(
            question = "Which data type is most suitable for storing a whole number such as 25?",
            options = listOf(
                "String",
                "boolean",
                "int",
                "double"
            ),
            correctIndex = 2,
            explanation = "The int data type is used for whole numbers such as 25."
        )
    ),
    "Operators and Expressions" to listOf(
        QuizQuestion(
            question = "What is an operator in programming?",
            options = listOf(
                "A symbol that performs an action such as calculation or comparison",
                "A container that stores several values",
                "A block of reusable instructions",
                "A condition that repeats code"
            ),
            correctIndex = 0,
            explanation = "An operator is a symbol such as +, -, *, /, ==, >, or < that performs an operation."
        ),
        QuizQuestion(
            question = "In the expression 5 + 3, what is the + symbol?",
            options = listOf(
                "A variable",
                "An operator",
                "A list",
                "A function"
            ),
            correctIndex = 1,
            explanation = "The + symbol is an operator because it tells the program to add the two values."
        )
    ),
    "If Statements" to listOf(
        QuizQuestion(
            question = "A condition works approximately like:",
            options = listOf(
                "A storage location",
                "A decision based on a rule",
                "A list",
                "A repetition"
            ),
            correctIndex = 1,
            explanation = "A condition is used to make a decision depending on whether a rule is true or false."
        ),
        QuizQuestion(
            question = "A person must be at least 18 years old to enter. The person is 20 years old. What happens?",
            options = listOf(
                "The rule is satisfied",
                "The rule is not satisfied",
                "It cannot be determined",
                "The rule is ignored"
            ),
            correctIndex = 0,
            explanation = "The person is 20 years old and therefore satisfies the condition of being at least 18."
        )
    ),
    "Loops" to listOf(
        QuizQuestion(
            question = "A loop is used when you want to:",
            options = listOf(
                "Store information",
                "Repeat the same activity several times",
                "Compare values",
                "Create a list"
            ),
            correctIndex = 1,
            explanation = "A loop is used to repeat instructions several times."
        ),
        QuizQuestion(
            question = "If an activity should be repeated five times, the most appropriate thing to use is:",
            options = listOf(
                "A condition",
                "A list",
                "A loop",
                "A variable"
            ),
            correctIndex = 2,
            explanation = "A loop is designed to repeat the same activity a fixed or condition-controlled number of times."
        )
    ),
    "Functions" to listOf(
        QuizQuestion(
            question = "Why are functions used?",
            options = listOf(
                "To group instructions that can be used several times",
                "To store several values",
                "To create loops",
                "To compare numbers"
            ),
            correctIndex = 0,
            explanation = "Functions group instructions into reusable blocks that can be called when needed."
        ),
        QuizQuestion(
            question = "If the same task needs to be performed many times, it is best to:",
            options = listOf(
                "Rewrite everything from the beginning each time",
                "Use a function",
                "Create more variables",
                "Create more lists"
            ),
            correctIndex = 1,
            explanation = "A function allows the same task to be reused without rewriting the same instructions."
        )
    ),
    "Lists" to listOf(
        QuizQuestion(
            question = "What is a list used for?",
            options = listOf(
                "To store several values together",
                "To make decisions",
                "To repeat code",
                "To create functions"
            ),
            correctIndex = 0,
            explanation = "A list is used to collect and store several values together."
        ),
        QuizQuestion(
            question = "Which example is most similar to a list?",
            options = listOf(
                "A single phone number",
                "A shopping list with several items",
                "A traffic light",
                "A button"
            ),
            correctIndex = 1,
            explanation = "A shopping list contains several items collected together and is therefore similar to a list in programming."
        )
    )
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CodeRealityTheme {
                AppScreen()
            }
        }
    }
}

@Composable
fun AppScreen() {
    var showIntro by remember { mutableStateOf(true) }
    var selectedTopic by remember { mutableStateOf<Topic?>(null) }
    var selectedPage by remember { mutableStateOf<LearningPage?>(null) }
    var showQuiz by remember { mutableStateOf(false) }

    fun opensDirectly(topic: Topic): Boolean {
        return topic.title == "Variables" ||
                topic.title == "Data Types" ||
                topic.title == "Operators and Expressions" ||
                topic.title == "Functions" ||
                topic.title == "Basic Problem Solving"
    }

    when {
        showIntro -> {
            IntroScreen(
                onContinue = {
                    showIntro = false
                }
            )
        }

        selectedTopic == null -> {
            TopicMenuScreen(
                topics = AppContent.topics,
                onTopicClick = { topic ->
                    selectedTopic = topic

                    selectedPage = if (opensDirectly(topic)) {
                        topic.pages.firstOrNull()
                    } else {
                        null
                    }
                }
            )
        }

        showQuiz -> {
            QuizScreen(
                topic = selectedTopic!!,
                onBackClick = {
                    showQuiz = false
                    if (opensDirectly(selectedTopic!!)) {
                        selectedTopic = null
                        selectedPage = null
                    }
                }
            )
        }

        selectedPage == null -> {
            PageMenuScreen(
                topic = selectedTopic!!,
                onPageClick = { page ->
                    selectedPage = page
                },
                onQuizClick = {
                    showQuiz = true
                },
                onBackClick = {
                    selectedTopic = null
                    showQuiz = false
                }
            )
        }

        else -> {
            ContentScreen(
                topic = selectedTopic!!,
                page = selectedPage!!,
                onBackClick = {
                    if (opensDirectly(selectedTopic!!)) {
                        selectedTopic = null
                        selectedPage = null
                    } else {
                        selectedPage = null
                    }
                }
            )
        }
    }
}

@Composable
fun IntroScreen(
    onContinue: () -> Unit
) {
    val background = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFEEE8FF),
            Color(0xFFDCCBFF)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = """Welcome to CodeReality

This app is designed to help beginners understand basic programming by connecting code to real-world situations.
Instead of only reading abstract code, you will see programming concepts explained through simple analogies, visual examples, explanations, and code examples.

In this app, you can explore topics such as:
Variables
Data types
Operators and expressions
If statements
Loops
Lists
Basic problem solving

Advanced topics such as sorting, data structures, trees, code/programs, and object-oriented programming are available in the Appendix.

Click below to start learning""",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF2F2347)
        )

        Spacer(modifier = Modifier.height(32.dp))

        MainButton(
            text = "Click here to continue",
            backgroundColor = Color(0xFF6E44C6),
            textColor = Color.White,
            onClick = onContinue
        )
    }
}

@Composable
fun TopicMenuScreen(
    topics: List<Topic>,
    onTopicClick: (Topic) -> Unit
) {
    val background = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFEEE8FF),
            Color(0xFFDCCBFF)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "CodeReality",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A2B8F)
        )

        Text(
            text = "Choose a topic",
            color = Color(0xFF5D4A85)
        )

        Spacer(modifier = Modifier.height(8.dp))

        topics.forEach { topic ->
            MainButton(
                text = topic.title,
                backgroundColor = topic.color,
                textColor = Color.White,
                onClick = {
                    onTopicClick(topic)
                }
            )
        }
    }
}

@Composable
fun PageMenuScreen(
    topic: Topic,
    onPageClick: (LearningPage) -> Unit,
    onQuizClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(topic.color)
            .padding(16.dp)
    ) {
        BackButton(onClick = onBackClick)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = topic.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (topic.title == "Loops") {
                Text(
                    text = """Loops

What are loops?

Programs often need to repeat the same task many times.

Instead of writing the same code again and again, programmers use loops.

A loop allows a program to repeat instructions automatically.

Examples:

Checking every student in a classroom
Printing multiple lines of text
Processing every item in a list
Counting from 1 to 100

Loops save time, reduce repeated code, and make programs more efficient.

Just as people repeat tasks in daily life, programs use loops to repeat instructions automatically.""",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            topic.pages.forEach { page ->
                MainButton(
                    text = page.title,
                    backgroundColor = Color.White,
                    textColor = topic.color,
                    onClick = {
                        onPageClick(page)
                    }
                )
            }

            if (beginnerQuickQuizzes.containsKey(topic.title)) {
                Spacer(modifier = Modifier.height(8.dp))
                MainButton(
                    text = "Quick Quiz",
                    backgroundColor = Color(0xFFFFD166),
                    textColor = Color(0xFF1E1E1E),
                    onClick = onQuizClick
                )
            }
        }
    }
}

@Composable
fun ContentScreen(
    topic: Topic,
    page: LearningPage,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(topic.color)
            .padding(16.dp)
    ) {
        BackButton(onClick = onBackClick)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = page.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = topic.color
                )

                Spacer(modifier = Modifier.height(16.dp))

                ContentWithInlineImages(page = page)

                val quizQuestions = beginnerQuickQuizzes[topic.title].orEmpty()
                val quizShownInsideContent =
                    topic.title == "Variables" ||
                            topic.title == "Data Types" ||
                            topic.title == "Operators and Expressions" ||
                            topic.title == "Functions"

                if (quizShownInsideContent && quizQuestions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(28.dp))
                    QuickQuiz(
                        topicTitle = topic.title,
                        questions = quizQuestions,
                        accentColor = topic.color
                    )
                }
            }
        }
    }
}

@Composable
fun QuizScreen(
    topic: Topic,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(topic.color)
            .padding(16.dp)
    ) {
        BackButton(onClick = onBackClick)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                QuickQuiz(
                    topicTitle = topic.title,
                    questions = beginnerQuickQuizzes[topic.title].orEmpty(),
                    accentColor = topic.color
                )
            }
        }
    }
}


@Composable
fun QuickQuiz(
    topicTitle: String,
    questions: List<QuizQuestion>,
    accentColor: Color
) {
    var selectedAnswers by remember(topicTitle) {
        mutableStateOf<Map<Int, Int>>(emptyMap())
    }

    HorizontalDivider(color = Color(0xFF555555))
    Spacer(modifier = Modifier.height(20.dp))

    Text(
        text = "Quick Quiz – ${quizDisplayName(topicTitle)}",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = accentColor
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "Check your understanding. You will receive immediate feedback after each answer.",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.White
    )

    Spacer(modifier = Modifier.height(16.dp))

    questions.forEachIndexed { questionIndex, quizQuestion ->
        Text(
            text = "Question ${questionIndex + 1}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = quizQuestion.question,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(10.dp))

        quizQuestion.options.forEachIndexed { optionIndex, option ->
            val isSelected = selectedAnswers[questionIndex] == optionIndex

            OutlinedButton(
                onClick = {
                    selectedAnswers = selectedAnswers + (questionIndex to optionIndex)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (isSelected) accentColor else Color.White
                )
            ) {
                Text(
                    text = "${('a'.code + optionIndex).toChar()}) $option"
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
        }

        val selectedIndex = selectedAnswers[questionIndex]
        if (selectedIndex != null) {
            val isCorrect = selectedIndex == quizQuestion.correctIndex

            Text(
                text = if (isCorrect) {
                    "Correct: ${quizQuestion.explanation}"
                } else {
                    "Not quite. The correct answer is ${('a'.code + quizQuestion.correctIndex).toChar()}) ${quizQuestion.options[quizQuestion.correctIndex]}. ${quizQuestion.explanation}"
                },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        if (questionIndex < questions.lastIndex) {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

fun quizDisplayName(topicTitle: String): String {
    return when (topicTitle) {
        "If Statements" -> "Conditions"
        "Variables" -> "Variables"
        "Data Types" -> "Data Types"
        "Operators and Expressions" -> "Operators and Expressions"
        "Loops" -> "Loops"
        "Functions" -> "Functions"
        "Lists" -> "Lists"
        else -> topicTitle
    }
}

@Composable
fun ContentWithInlineImages(
    page: LearningPage
) {
    if (page.imagePlacements.isEmpty()) {
        CodeStyledText(page.content)

        page.imageResources.forEach { imageRes ->
            LearningImage(imageRes = imageRes)
        }

        return
    }

    var currentIndex = 0

    page.imagePlacements.forEach { placement ->
        val foundIndex = page.content.indexOf(placement.afterText, startIndex = currentIndex)

        if (foundIndex >= 0) {
            val endIndex = foundIndex + placement.afterText.length
            val textPart = page.content.substring(currentIndex, endIndex)

            CodeStyledText(textPart)
            LearningImage(imageRes = placement.imageRes)

            currentIndex = endIndex
        }
    }

    if (currentIndex < page.content.length) {
        CodeStyledText(page.content.substring(currentIndex))
    }
}

@Composable
fun LearningImage(
    imageRes: Int
) {
    Spacer(modifier = Modifier.height(16.dp))

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun MainButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun BackButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF5A36A3),
            contentColor = Color.White
        )
    ) {
        Text("Back")
    }
}

@Composable
fun CodeStyledText(
    content: String
) {
    val keywords = listOf(
        "class",
        "public",
        "private",
        "protected",
        "void",
        "int",
        "double",
        "boolean",
        "char",
        "String",
        "return",
        "if",
        "else",
        "elif",
        "while",
        "break",
        "new",
        "import"
    )

    val formattedText = buildAnnotatedString {
        content.lines().forEach { line ->
            when {
                line.trim().startsWith("//") || line.trim().startsWith("#") -> {
                    withStyle(SpanStyle(color = Color(0xFF6A9955))) {
                        append(line)
                    }
                }

                line.contains("\"") || line.contains("'") -> {
                    withStyle(SpanStyle(color = Color(0xFFCE9178))) {
                        append(line)
                    }
                }

                keywords.any { keyword -> line.contains(keyword) } -> {
                    withStyle(SpanStyle(color = Color(0xFF569CD6))) {
                        append(line)
                    }
                }

                else -> {
                    append(line)
                }
            }

            append("\n")
        }
    }

    Text(
        text = formattedText,
        color = Color.White,
        fontFamily = FontFamily.Monospace
    )
}

object AppContent {
    private val sortingPages = listOf(
        LearningPage(
            title = "Bubble Sort",
            content = """
Sorting Algorithms
Sorting means putting values in order.

Example: 50, 10, 80, 30, 20

Sorted result: 10, 20, 30, 50, 80

Programs use sorting for:
prices
grades
names
rankings
search results

1. Bubble Sort
Airport luggage sorting by weight

Suitcases are placed on a conveyor belt in random order.

Each suitcase has a position on the belt (position 0, position 1, position 2...).

An airport worker walks through the entire belt multiple times.

During each round, the worker checks neighboring positions one by one.

If the suitcase at the left position is heavier than the suitcase at the right position, they switch places.

After each full round, the heaviest suitcase moves to the far right.

After multiple rounds, all suitcases become sorted from lightest to heaviest.

class BubbleSortExample {

    class BubbleSortExample {

    public static void bubbleSort(int[] arr) {
        // Suitcases are placed randomly on an airport conveyor belt

        for (int i = 0; i < arr.length - 1; i++) {
            // i = full rounds
            // The airport worker starts at position 0
            // and walks through the entire conveyor belt.
            // One full walk from start to end = one round.
            // Multiple rounds may be needed to fully sort the luggage.

            for (int j = 0; j < arr.length - i - 1; j++) {
                // j = current position being checked
                // The worker compares neighboring positions:
                // Position 0 vs Position 1
                // Then Position 1 vs Position 2
                // Then Position 2 vs Position 3
                // The worker keeps moving forward through positions.

                if (arr[j] > arr[j + 1]) {
                    // If the suitcase at the left position
                    // is heavier than the suitcase at the right position,
                    // they are in the wrong order and must switch places.

                    int temp = arr[j];
                    // The worker temporarily holds the left (heavier) suitcase

                    arr[j] = arr[j + 1];
                    // The lighter suitcase moves left
                    // The suitcase from the right position (j+1) moves into the left position (j)
                    
                    arr[j + 1] = temp;
                    // The heavier suitcase moves right
                }
            }

            // After one full round,
            // the heaviest remaining suitcase reaches the far right.
        }
    }

    public static void main(String[] args) {

        int[] bags = {50, 10, 80, 30, 20};
        // Random suitcase weights on the conveyor belt

        bubbleSort(bags);
        // The worker sorts the luggage from lightest to heaviest

        for (int bag : bags) {
            // Walk through the final sorted conveyor belt

            System.out.println(bag);
            // Show each suitcase weight
        }
    }
}

    public static void main(String[] args) {
    // The airport worker starts the luggage sorting process

    int[] bags = {50, 10, 80, 30, 20};
    // Suitcases are placed randomly on the conveyor belt by weight

    bubbleSort(bags);
    // The worker sorts the luggage from lightest to heaviest

    for (int bag : bags) {
        // Move through each final luggage position on the conveyor belt

        System.out.println(bag);
        // Display each suitcase weight after sorting
    }
}

Short Summary

Bubble Sort compares neighboring values and swaps them when they are in the wrong order. Repeating this process gradually moves the largest values to the end.
"""
        ),
        LearningPage(
            title = "Selection Sort",
            content = """
Insertion Sort
Organizing playing cards

You hold cards in your hand.

You take one new card at a time.

Then you move larger cards to the right until the new card fits in the correct position.

class InsertionSortExample {

    public static void insertionSort(int[] arr) {
        // Start organizing the hand of cards

        for (int i = 1; i < arr.length; i++) {
            // Pick one new card from the unsorted side

            int key = arr[i];
            // This is the card currently in your hand

            int j = i - 1;
            // Start comparing with the card immediately to the left

            while (j >= 0 && arr[j] > key) {
                // While the left card is bigger than the card in your hand

                arr[j + 1] = arr[j];
                // Move the bigger card one position to the right

                j--;
                // Continue checking the next card to the left
            }

            arr[j + 1] = key;
            // Place the card in your hand into the empty correct position
        }
    }

    public static void main(String[] args) {
        // Card sorting begins

        int[] cards = {5, 3, 8, 2};
        // Cards are currently messy in the hand

        insertionSort(cards);
        // Insert each card into its correct position

        for (int card : cards) {
            // Walk through the final card hand

            System.out.println(card);
            // Show each card after sorting
        }
    }
}

Short Summary

Selection Sort repeatedly finds the smallest remaining value and places it in the next correct position. It builds the sorted result one position at a time.
"""
        ),
        LearningPage(
            title = "Insertion Sort",
            content = """
Selection Sort
Finding the lightest box for each shelf position

Workers organize boxes by weight from lightest to heaviest.

The warehouse shelves have fixed positions:

position 0
position 1
position 2
position 3

The worker starts at position 0 and temporarily assumes that box is the lightest.

Then the worker checks all remaining positions:

compare position 0 with position 1
compare position 0 with position 2
compare position 0 with position 3

If a lighter box is found, the worker remembers that new position.

After checking all remaining boxes, the worker places the lightest box into position 0.

Then the worker moves to position 1.

Position 0 is already correct, so it is ignored.

Now the worker compares:

position 1 with position 2
position 1 with position 3
position 1 with position 4

Again, the lightest remaining box is placed into position 1.

This process repeats until every shelf position contains the correct box.

class SelectionSortExample {

    public static void selectionSort(int[] arr) {
        // The warehouse starts organizing boxes by weight

        for (int i = 0; i < arr.length - 1; i++) {
            // i = sorting rounds
            // Each round fills one shelf position
            // Round 1 → fill position 0
            // Round 2 → fill position 1
            // Round 3 → fill position 2

            int minIndex = i;
            // At the start of the round,
            // the worker begins with the current position
            // as the temporary lightest box position
            // Example:
            // Round 1 → start with position 0
            // Round 2 → start with position 1

            for (int j = i + 1; j < arr.length; j++) {
                // j checks all remaining positions after i
                // Example:
                // If round 1 (i = 0) → check positions 1,2,3...
                // If round 2 (i = 1) → check positions 2,3...
                // Already sorted positions are ignored

                if (arr[j] < arr[minIndex]) {
                     // Compare the current checked position (j)
                     // with the current lightest position (minIndex)
                     // If the box at position j is lighter
                     // it becomes the new lightest box found so far

                    minIndex = j;
                    // The new lighter position becomes
                    // the current lightest position
                    // j becomes the new lightest position found so far.
                    // Example
                    // [40, 30, 20, 10]
                    // Round 1: start → minIndex = 0 (40)
                    //j = 1 → finds 30 lighter than 40 → minIndex = 1
                    // j = 2 → finds 20 lighter than 30 → minIndex = 2
                    // j = 3 → finds 10 lighter than 20 → minIndex = 3
                  
                }
            }

            int temp = arr[i];
            // Temporarily hold the box
            // currently sitting in the shelf position

            arr[i] = arr[minIndex];
            // Move the final lightest box
            // into the correct shelf position

            arr[minIndex] = temp;
            // Move the old shelf box
            // to where the lightest box came from
        }
    }

    public static void main(String[] args) {
        // Warehouse sorting begins

        int[] boxes = {40, 10, 30, 20};
        // Boxes are randomly placed by weight

        selectionSort(boxes);
        // Workers sort boxes from lightest to heaviest

        for (int box : boxes) {
            // Walk through all final shelf positions

            System.out.println(box);
            // Display each final box weight
        }
    }
}

Short Summary

Insertion Sort takes one value at a time and inserts it into the correct position among the already sorted values. It works like organizing cards in your hand.
"""
        ),
        LearningPage(
            title = "Merge Sort",
            content = """
Merge Sort

Sorting exam papers into smaller piles

A teacher has one large pile of exam papers.

The pile is too big to sort directly.

So the teacher uses three main steps:

Split
The teacher splits the big pile into two smaller piles.
Repeat
Each smaller pile is split again until every pile has only one paper.

A pile with one paper is already sorted.

Merge
The teacher compares the first paper from the left pile and the first paper from the right pile.

The smaller paper is placed into the final pile first.

This continues until both piles are merged into one sorted pile.

Example:

Start:
[40, 10, 30, 20]

Split:
[40, 10]    [30, 20]

Split again:
[40] [10]   [30] [20]

Merge sorted:
[10, 40]    [20, 30]

Final merge:
[10, 20, 30, 40]
Full code with detailed comments
class MergeSortExample {

    public static void mergeSort(int[] arr) {
        // The teacher receives one pile of exam papers

        if (arr.length < 2) {
            // If the pile has 0 or 1 paper,
            // there is nothing to sort

            return;
            // A pile with one paper is already sorted
        }

        int middle = arr.length / 2;
        // Find the middle position of the pile
        // This tells the teacher where to split the pile

        int[] leftPile = new int[middle];
        // Create a new left pile
        // This pile will store the first half of the papers

        int[] rightPile = new int[arr.length - middle];
        // Create a new right pile
        // This pile will store the second half of the papers

        for (int i = 0; i < middle; i++) {
            // Move papers from the first half of the main pile
            // into the left pile
            // Example:
            // If main pile = [40, 10, 30, 20]
            // middle = 2
        
            // Position 0 → moves to left pile
            // Position 1 → moves to left pile

            leftPile[i] = arr[i];
            // Paper from main position i
            // moves into the same position in the left pile
        }

        for (int i = middle; i < arr.length; i++) {
            // Copy papers from the middle of the main pile
            // into the right pile

            rightPile[i - middle] = arr[i];
                // Move papers from the second half of the main pile
                // into the right pile
                
                // Example:
                // Main pile = [40, 10, 30, 20]
                // middle = 2
                
                // Main position 2 (30) → right position 0
                // Main position 3 (20) → right position 1
        }

        mergeSort(leftPile);
          // The teacher repeats the same splitting process
          // for the left pile until it becomes small and sorted

        mergeSort(rightPile);
          // The teacher repeats the same splitting process
          // for the right pile until it becomes small and sorted

        merge(arr, leftPile, rightPile);
          // Now both smaller piles are sorted
          // The teacher merges them back into the main pile
    }

    private static void merge(int[] arr, int[] leftPile, int[] rightPile) {
        // The teacher combines two already sorted piles
        // into one final sorted pile

        int i = 0;
        // i = current position in the left pile

        int j = 0;
        // j = current position in the right pile

        int k = 0;
        // k = current position in the final combined pile

        while (i < leftPile.length && j < rightPile.length) {
            // Continue while both piles still have papers left
            // The teacher compares one paper from the left pile
            // with one paper from the right pile

            if (leftPile[i] <= rightPile[j]) {
                // Compare the current left paper
                // with the current right paper
            
                // If the left paper is smaller,
                // it must go first because the final pile
                // is being built from smallest to largest

                arr[k] = leftPile[i];
                   // Place the smaller left paper
                   // into the current empty position in the final pile
                
                i++;
                  // Move to the next paper position
                  // in the left pile for the next comparison

            } else {
                // If the right paper is smaller,
                // it should be placed first in the final pile

                arr[k] = rightPile[j];
                   // Place the smaller right paper
                   // into the current empty position in the final pile
                
                j++;
                   // Move to the next paper position
                   // in the right pile for the next comparison
            }

            k++;
            // Move to the next empty position
            // in the final combined pile
        }

        while (i < leftPile.length) {
            // If the right pile is empty
            // but the left pile still has papers left

            arr[k] = leftPile[i];
                // The right pile is already empty
                // so no more comparisons can happen
                
                // The remaining left papers
                // must be moved directly into the final pile

            i++;
              // Move to the next remaining paper
              // in the left pile
            
            k++;
              // Move to the next empty position
              // in the final sorted pile
        }

        while (j < rightPile.length) {
            // If the left pile is empty,
            // but the right pile still has papers left
            
            arr[k] = rightPile[j];
               // No more comparisons can happen
               // so move the remaining right paper
               // directly into the final sorted pile
            
            j++;
              // Move to the next remaining paper
              // in the right pile
            
            k++;
              // Move to the next empty position
              // in the final sorted pile
        }
    }

    public static void main(String[] args) {
        // The teacher starts the paper sorting example

        int[] papers = {40, 10, 30, 20};
        // Exam papers are in random order

        mergeSort(papers);
        // Sort the papers from smallest to largest

        for (int paper : papers) {
            // Walk through the final sorted pile

            System.out.println(paper);
            // Show each paper value after sorting
        }
    }
}
Important note
mergeSort() = splits the pile again and again
merge() = puts two sorted piles back together in correct order

Result:

10
20
30
40
        }
    }

    public static void main(String[] args) {
        // Paper sorting begins

        int[] papers = {8, 3, 6, 2};
        // Exam papers are in messy order

        mergeSort(papers);
        // Split, sort, and merge the papers

        for (int paper : papers) {
            // Walk through the final paper pile

            System.out.println(paper);
            // Show each paper after sorting
        }
    }
}

Short Summary

Merge Sort splits data into smaller parts, sorts those parts, and merges them back together. It solves a large sorting problem by breaking it into smaller sorting problems.
"""
        ),
        LearningPage(
            title = "Quick Sort",
            content = """
Quick Sort
Choosing one leader

A teacher chooses one student as the leader.

That leader is called the pivot.

Students shorter than the leader go to the left.

Students taller than the leader go to the right.

Then the same process happens again on each side.

class QuickSortExample {

    public static void quickSort(int[] arr, int low, int high) {
        // Teacher sorts students between low and high positions

        if (low < high) {
            // If there is more than one student to sort

            int pivotIndex = partition(arr, low, high);
            // Choose a leader and place them in the correct position

            quickSort(arr, low, pivotIndex - 1);
            // Sort the shorter students on the left side

            quickSort(arr, pivotIndex + 1, high);
            // Sort the taller students on the right side
        }
    }

    private static int partition(int[] arr, int low, int high) {
        // This method organizes students around one leader

        int pivot = arr[high];
        // The last student is chosen as the leader/pivot

        int i = low - 1;
        // i marks the end of the shorter-student group

        for (int j = low; j < high; j++) {
            // j checks students one by one
            // from left to right before reaching the leader
        
            if (arr[j] < pivot) {
                // If the current checked student
                // is shorter than the leader,
                // they belong in the shorter group on the left side
        
                i++;
                // Move the boundary of the shorter group
                // one step to the right to create space
        
                int temp = arr[i];
                // Temporarily hold the student
                // currently standing at the shorter-group boundary
        
                arr[i] = arr[j];
                // Move the newly found shorter student
                // into the shorter group
        
                arr[j] = temp;
                // Move the previous boundary student
                // back to the current checked position
    }
}

        int temp = arr[i + 1];
        // Temporarily hold the student
        // standing at the first position after the shorter group
        
        arr[i + 1] = arr[high];
        // Move the leader into the position
        // directly after all shorter students
        
        arr[high] = temp;
        // Move the old student from that position
        // to the leader's old position
        
        return i + 1;
        // Return the leader's final sorted position
        // Now everything left of the leader is shorter
        // and everything right of the leader is taller

Short Summary

Quick Sort chooses a pivot value and places smaller values on one side and larger values on the other. The same process is repeated until the data is sorted.
"""
        )
    )

    private val dataStructurePages = listOf(
        LearningPage(
            title = "Queue with One Stack",
            content = """

QueueWithOneStack

A data structure is a way to organize and store data in memory so that a program can use the data efficiently. When programs store data, they often need to decide in what order the data should be removed later.

This is where data structures such as stack and queue are used.

What is a Stack?

A stack is like a stack of plates. If you place plates like this: Plate 1 then Plate 2 then Plate 3, then Plate 3 ends up at the top.

If you want to take a plate, you can only take the top plate first.

Last inserted → removed first.

This is called:
LIFO (Last In First Out)

Examples in programming:
Undo function in text editors
Browser history (back button)

Java methods for stack:
push() → add
pop() → remove the top element
peek() → look at the top element


What is a Queue?

A queue is like a normal line in a grocery store. People stand like this: 1 then 2 then 3.

Person 1 arrived first → leaves first.

This is called:
FIFO (First In First Out)

Examples in programming:
Printer queue
CPU process scheduling

Common queue methods:
enqueue() → add at the end
dequeue() → remove from the front


The problem in this task

The task says: "Build a queue using only one stack."

This is difficult because:
Stack wants to do Last In First Out
Queue wants to do First In First Out

They work in opposite ways.


How the code solves this

It cheats in a smart way.

It first removes: 4 then 3 then 2 then 1

When it reaches 1 it knows:
"This was the first element."

It returns 1.

Then it puts back:
2 then 3 then 4

So what remains is:
[2,3,4]

These repeated removals happen with recursion, which means the method calls itself again and again:

dequeue() -> dequeue() -> dequeue()

It is like removing plate after plate until you reach the bottom.


import java.util.Stack;

public class QueueWithOneStack {

    private Stack<Integer> stack = new Stack<>(); 
    // Think of a stack of plates:
    // This is the actual plate stack in the analogy

    // you can only place a new plate on the top
    // New values always go on top like new plates

    private int MAX_SIZE = 4; 
    // Maximum 4 plates can fit in the stack
    // The shelf can only hold 4 plates

    public void enqueue(int value) {

        if (stack.size() >= MAX_SIZE) { 
            // Checks if the stack is already full
            // Checks if the plate stack is already full

            System.out.println("Overflow: Queue is full."); 
            // Like saying: the shelf is full, no more plates can fit
            // No more plates can be added to the stack

            return; 
            // Stops here
            // Stops before adding a new plate
        }

        stack.push(value); 
        // Places a new plate on top of the stack
        // Adds the new value as the newest plate
    }

    public int dequeue() {

        if (stack.isEmpty()) { 
            // Checks if there are any plates at all
            // Checks if the plate stack is empty

            System.out.println("Underflow: Queue is empty."); 
            // Like trying to take a plate from an empty shelf
            // There is no plate/customer to remove

            return -1;
        }

        int top = stack.pop(); 
        // Removes the top plate first
        // Temporarily lifts away the newest plate

        // Example: if the stack is [1,2,3,4], 4 is removed first
        // This starts digging down toward the oldest plate

        if (stack.isEmpty()) { 
            // If the shelf becomes empty now, it means we reached the very first plate
            // The bottom plate has been reached

            return top; 
            // The first plate was at the bottom
            // This is the oldest element that should leave first

            // This is the customer who has waited the longest in the queue
            // This creates FIFO behavior
        }

        int dequeued = dequeue(); 
        // Goes deeper again:
        // Continues removing plates

        // removes the next plate
        // Moves one level closer to the bottom plate

        // like removing layer by layer until reaching the bottom
        // This is the recursion part of the analogy

        stack.push(top); 
        // After removing the correct plate from the bottom
        // Starts putting back the plates that were temporarily removed

        // all other plates are placed back in the same order
        // Restores the remaining stack

        return dequeued; 
        // Returns the person/plate that has waited the longest
        // Returns the oldest element from the queue
    }
}

Short Summary

A queue removes the oldest value first, while a stack removes the newest value first. This example uses recursion to make one stack behave like a queue.
"""
        ),
        LearningPage(
            title = "Queue with Two Stacks",
            content = """

QueueWithTwoStacks

A data structure is a way to organize and store data in memory so that a program can use the data efficiently. When programs store data, they often need to decide in what order the data should be removed later.

This is where data structures such as stack and queue are used.

What is a Stack? A stack is like a stack of plates.

If you place plates like this: Plate 1 then Plate 2 then Plate 3
Then Plate 3 ends up at the top.

If you want to take a plate, you can only take the top plate first. Last inserted → removed first. This is called:
LIFO (Last In First Out)

What is a Queue? A queue is like a normal line in a grocery store. People stand like this: 1 then 2 then 3.

Person 1 arrived first → leaves first. This is called:
FIFO (First In First Out)

The problem in this task. The task says: "Build a queue using two stacks."

This is difficult because a stack normally works with LIFO, while a queue works with FIFO.

How the code solves this. It uses two stacks.

stack1 is like the entrance table.
New plates are placed there.

stack2 is like the exit table.
When we want to remove something from the queue, the plates are moved from stack1 to stack2.

Example:
stack1 has: [1,2,3,4]

When moving them to stack2, the order becomes:
[4,3,2,1]

Now 1 is on top of stack2.
So when we pop from stack2, 1 comes out first.

This means the queue behavior becomes FIFO:
First In First Out.

import java.util.Stack;

public class QueueWithTwoStacks {

    private Stack<Integer> stack1 = new Stack<>();
    // stack1 is the entrance stack
    // This is the entrance table in the analogy

    // new values are placed here first
    // New plates are first placed on the entrance table

    private Stack<Integer> stack2 = new Stack<>();
    // stack2 is the exit stack
    // This is the exit table in the analogy

    // values are removed from here
    // Plates leave from the exit table

    public static final int MAX_SIZE = 10;
    // Maximum 10 values can exist in the queue
    // Both tables together can only hold 10 plates

    public void enqueue(int value) {

        if (stack1.size() + stack2.size() >= MAX_SIZE) {
            // Checks if both stacks together already contain 10 values
            // Checks if both tables together are full

            System.out.println("Overflow: Queue is full. Cannot add more elements.");
            // Like saying: the queue is full, no more people can stand in line
            // No more plates can be placed on the tables

            return;
            // Stops here
            // Stops before adding a new plate
        }

        stack1.push(value);
        // Adds the new value to stack1
        // Places the new plate on the entrance table

        // Like a new person joining the back of the queue
        // This new plate waits behind the older plates
    }

    public int dequeue() {

        return dequeue(false);
        // Calls the other dequeue method
        // Uses the normal exit rule

        // false means: show warning if the queue is empty
        // If there are no plates, show the warning
    }

    public int dequeue(boolean silent) {

        if (stack1.isEmpty() && stack2.isEmpty()) {
            // Checks if both stacks are empty
            // Checks if both entrance table and exit table are empty

            // This means the queue has no values
            // There are no plates/customers to remove

            if (!silent) {
                // If silent is false, show the warning
                // If we are not hiding messages, print the warning

                System.out.println("Underflow: Queue is empty.");
                // Like trying to serve a customer when nobody is in line
                // Like trying to take a plate when both tables are empty
            }

            return -1;
            // Returns -1 because there is nothing to remove
            // Nothing leaves because no plate exists
        }

        if (stack2.isEmpty()) {
            // If stack2 is empty, we must prepare it for removal
            // If the exit table is empty, move plates from the entrance table

            while (!stack1.isEmpty()) {
                // As long as stack1 still has values
                // As long as there are plates on the entrance table

                stack2.push(stack1.pop());
                // Move the top value from stack1 to stack2
                // Move one plate from the entrance table to the exit table

                // This reverses the order
                // The plate that was first in stack1 becomes reachable first in stack2

                // Example: stack1 [1,2,3,4] becomes stack2 [4,3,2,1]
                // Now plate 1 is on top of the exit table
            }
        }

        return stack2.pop();
        // Removes the top value from stack2
        // Removes the plate from the exit table

        // Because the order was reversed, the oldest value comes out first
        // This makes the queue behave as FIFO
    }
}

Short Summary

Two stacks can work together to create queue behavior. Moving values from one stack to another reverses the order so the oldest value can leave first.
"""
        ),
        LearningPage(
            title = "Stack with One Queue",
            content = """

Stack With One Queue

A data structure is a way to organize and store data in memory so that a program can use the data efficiently. When programs store data, they often need to decide in what order the data should be removed later.

This is where data structures such as stack and queue are used.

What is a Queue?

Think of a normal line in a grocery store.

People stand like this:

3 → 2 → 1

Person 1 arrived first → leaves first.

This is called:

FIFO (First In First Out)

Java queue methods:

add() → add a person to the line
remove() → remove the first person

What is a Stack?

Think of a stack of books.

Book 4
Book 3
Book 2
Book 1

Book 4 ends up on top.

If you want a book, you can only take the top one first.

Last inserted → removed first

This is called: LIFO (Last In First Out)

Examples in programming:
Undo function
Browser history
Function calls

The problem in this task.
The task says: "Build a stack using only one queue."

The problem is:
Queue wants to do → First In First Out
Stack wants to do → Last In First Out

They work in opposite ways.

How the code solves this.
Normal queue behavior: [1,2,3,4]

If we remove now: 1 would leave first

But a stack should remove: 4 first

So after adding a new value, the code rotates the queue.

How the code works step by step.

Imagine we own a waiting line outside a store.
Normally, a queue works like this:
Person 1 arrives
Person 2 arrives
Person 3 arrives
Person 4 arrives

Line:

4 → 3 → 2 → 1

The first person who arrived leaves first.

That is normal queue behavior: First In First Out

But our task is different.

We want the line to behave like a stack.

That means: The newest person must leave first. Last In First Out

That creates a problem: A normal line always lets the oldest person leave first.

So we create a new store rule:
Whenever a new person joins the line, everyone already standing there must move behind that new person.

Very strange rule, but it solves our problem.

Person 1 arrives first.

Line:

1

Nothing happens because he is alone.

He stays where he is.

Person 2 arrives.

Normal line would be:

2 → 1

But we want newest first.

So security tells person 1: "Move to the back."

Now the line becomes:

1 → 2

Now person 2 can leave first.

Perfect stack behavior.

Person 3 arrives.

Normal line would be:

3 → 1 → 2

Again, security steps in.

They tell older people to move behind person 3.

First person 2 moves back:

2 → 3 → 1

Then person 1 moves back:

1 → 2 → 3

Now person 3 is first.

Newest person is ready to leave first.

Person 4 arrives.

Normal line:

4 → 1 → 2 → 3

Again the workers rotate everyone.

Person 3 moves back:

3 → 4 → 1 → 2

Person 2 moves back:

2 → 3 → 4 → 1

Person 1 moves back:

1 → 2 → 3 → 4

Now the newest person stands first again.

This keeps happening every time someone new arrives.

Older people keep sacrificing their positions.

The newest person always gets priority.

Now someone wants to leave.

Who leaves?

The person at the front.

Right now:

1 → 2 → 3 → 4

Person 4 leaves first.

Then line becomes:

1 → 2 → 3

Next removal:

Person 3 leaves.

Then:

1 → 2

This continues exactly like a stack.

Newest leaves first.

What if the line is full?

The store only allows 10 people.

If an 11th person arrives:

The guard stops them and says: "Sorry, the line is full."

They cannot enter.

That is overflow.

What if nobody is in line?

And someone tries to leave?

There’s nobody there.

The guard says: "There is nobody in line."

That is underflow.

So the entire system is basically: A normal waiting line
combined with
a weird store rule where older people keep moving back whenever someone new arrives.

That trick forces a queue to behave like a stack.

import java.util.LinkedList;
import java.util.Queue;

public class StackWithOneQueue {

    private Queue<Integer> queue = new LinkedList<>();
    // One normal waiting line
    // This is the only line in the analogy

    public static final int MAX_SIZE = 10;
    // Maximum allowed values
    // The line has a maximum number of people (10)

    public void push(int value) {

        if (queue.size() >= MAX_SIZE) {
            // Checks if queue is full
            // Checks if the line is already full (overflow)

            System.out.println("Overflow: Stack is full. Cannot add more elements.");

            return;
            // Stops here
            // No new person/value can join the line
        }

        queue.add(value);
        // Add new value to the back of the queue
        // A new person joins at the back of the line

        int size = queue.size();
        // Store current size
        // Remember how many people are currently in the line

        while (size > 1) {
        // Continue rotating until the newest value reaches the front
             
            queue.add(queue.remove());
            // Remove front value and place it at the back
            // Reading the code from right to left: Take first person in line "queue.remove()", move to the back "queue.add(---)"
            
            size--;
        }
    }

    public int pop() {

        if (queue.isEmpty()) {
            // Checks if there are no people in the line

            System.out.println("Underflow: Stack is empty.");

            return -1;
        }

        return queue.remove();
        // Removes front value
        // The person at the front leaves

        // Because of rotation, newest value leaves first
        // Because we rotated the line, the newest person is now first
    }
}

Short Summary

A stack removes the newest value first. This example rotates one queue after each insertion so the newest value is always removed first.
"""
        ),
        LearningPage(
            title = "Stack with Two Queues",
            content = """

Stack With Two Queues

A data structure is a way to organize and store data in memory so that a program can use the data efficiently. When programs store data, they often need to decide in what order the data should be removed later.

This is where data structures such as stack and queue are used.

What is a Queue?

Think of a normal line in a grocery store.

People stand like this:

3 → 2 → 1

Person 1 arrived first → leaves first.

This is called:

FIFO (First In First Out)

Java queue methods:

add() → add a person to the line
remove() → remove the first person

What is a Stack?

Think of a stack of books.

Think of a stack of books.

Book 1
Book 2
Book 3

Book 3 ends up on top.

If you want a book, you can only take the top one first.

Last inserted → removed first

This is called:

LIFO (Last In First Out)

Examples in programming:

Undo function
Browser history

The problem in this task.

The task says: "Build a stack using two queues."

The problem is:
Queue wants to do → First In First Out
Stack wants to do → Last In First Out

They work in opposite ways.

How the code solves this.

Imagine we run a place with two waiting lines outside.

Line 1 = main line
This is where all new people normally arrive.

Line 2 = temporary line
This is only used when we need to reorganize people.

Normally people arrive like this:

Person 3 arrives
Person 2 arrives
Person 1 arrives

Main line:

3 → 2 → 1

This is normal queue behavior:

First person arrives first → leaves first. FIFO

But our task wants stack behavior.

That means: The newest person must leave first. LIFO

Problem:A normal line does the exact opposite.

So we use a second line to help us.

Person 1 arrives

They join main line.

Line 1:

1

Line 2:

empty

Person 2 arrives

They also join main line.

Line 1:

2 → 1

Line 2:

empty

Person 3 arrives

They join main line.

Line 1:

3 → 2 → 1

Line 2:

empty

Everything looks like a normal queue so far.

But now someone wants to leave.

A stack says:

Newest person must leave first.

That means person 3 must leave first.

But person 1 is blocking the front.

So workers begin moving people.

Person 1 is moved from line 1 → line 2

Line 1:

2 → 3

Line 2:

1

Person 2 is also moved from line 1 → line 2

Line 1:

3

Line 2:

2 → 1

Now only person 3 remains in line 1.

That means person 3 was the newest person.

They are allowed to leave first.

Exactly what a stack wants.

After person 3 leaves:

Line 1:

empty

Line 2:

1 → 2

Now the workers swap the signs on the lines.

They say:

“This temporary line is now the main line.”

So now:

Line 1:

2 → 1

Line 2:

empty

Everything resets.

Now person 4 arrives.

They join line 1.

Line 1:

4 → 2 → 1

Line 2:

empty

Someone wants to leave again.

Workers repeat the same process.

Move person 1 to line 2

Line 1:

4 → 2

Line 2:

1

Move person 2 to line 2

Line 1:

4

Line 2:

2 → 1

Now person 4 is alone.

They leave first.

Newest leaves first.

Stack behavior achieved again.

What happens if both lines are full?

The building only allows 10 total people.

If another person tries to enter:

Security says:

“Sorry, we're full.”

That is overflow.

What happens if both lines are empty?

And someone wants to leave?

There is nobody there.

Security says:

“No one is waiting.”

That is underflow.

So this entire system works like:

One normal line for arrivals

and

One temporary line used to move older people out of the way

so the newest person can always leave first.

That’s how two queues pretend to be a stack.

import java.util.LinkedList;
import java.util.Queue;

public class StackWithTwoQueues {

    private Queue<Integer> queue1 = new LinkedList<>();
    // Main line where people arrive

    private Queue<Integer> queue2 = new LinkedList<>();
    // Second line used to temporarily move people

    public static final int MAX_SIZE = 10;
    // Both lines together have a maximum capacity (10)

    public void push(int value) {

        if (queue1.size() + queue2.size() >= MAX_SIZE) {
            // Checks if both queues together are full

            System.out.println("Overflow: Stack is full. Cannot add more elements.");
            // Security tells the new person the place is full
            // No more people are allowed to join the lines
            
            return;
            // Stops here
            // The new person/value cannot join
        }

        queue1.add(value);
        // A new person joining  queue1
    }

    public int pop() {

        if (queue1.isEmpty() && queue2.isEmpty()) {
            // Checks if both queues are empty

            System.out.println("Underflow: Stack is empty.");
            // Nobody can leave because both lines are empty

            return -1;
        }

        while (queue1.size() > 1) {
            // We stop when only one person is left
            
            queue2.add(queue1.remove());
            // Move first person from queue1 to queue2
            // Person at the front moves to the second line
        }

        int poppedValue = queue1.remove();
        // Remove the last remaining value and save it in poppedValue
        // The last person who entered is now alone and leaves first

        Queue<Integer> temp = queue1;
        // Temporary holder
        // Temporary worker remembers the old main line
        // Like holding the sign of line 1 before switching the two lines

        queue1 = queue2;
        // queue2 becomes the new main queue
        // The second line becomes the main line again

        queue2 = temp;
        // old queue1 becomes empty temp queue
        // The old main line is now empty and ready to be reused

        return poppedValue;
        // Return the person who was allowed to leave first
        // This tells us who left the line-
        
    }
}

Short Summary

Two queues can be used to simulate stack behavior. Older values are moved aside so the newest value can be removed first.
"""
        )
    )

    private val ifAndLoopPages = listOf(

        LearningPage(
            title = "If / Else",
            content = """
What is an if statement?

An if statement allows a program to make decisions.

The program checks a condition.

If true → one action happens

If false → another action happens


Security guard analogy

Imagine a security guard at an event entrance.

The guard asks:

Do you have a ticket?

If yes → enter

If no → rejected


boolean hasTicket = true;
// Person arrives at entrance

if(hasTicket){
    // Guard checks ticket
    
    System.out.println("Enter event");
    // Person enters event
}
else{
    // No ticket found
    
    System.out.println("Access denied");
    // Person gets rejected
}

Short Summary

If statements allow a program to choose between actions. The program checks a condition and follows one path if it is true and another path if it is false.
""",      imagePlacements = listOf(
                    InlineImage(
                        afterText = "Security guard analogy",
                        imageRes = R.drawable.if_else_security_guard
                        )
                    )
               ),

        LearningPage(
            title = "Nested If",
            content = """
Nested if

A decision inside another decision.

Car speed robot analogy

The car first checks:

Are we speeding?

If yes:
Then it checks HOW MUCH over the speed limit.


int currentSpeed = 95;
int speedLimit = 80;
// Car reads current speed

if(currentSpeed > speedLimit){
    // First decision:
    // Are we speeding?

    int difference = currentSpeed - speedLimit;
    // Calculate how much over limit

    if(difference < 10){
        // Second decision:
        // Slight speeding

        System.out.println("Stop accelerating");
    }
    else{
        // Serious speeding
        
        System.out.println("Brake now");
    }

}
else{
    System.out.println("Speed is safe");
}

Short Summary

Nested if statements are decisions inside other decisions. They are useful when a program must check one condition first and then check another condition inside it.
""",     imagePlacements = listOf(
                InlineImage(
                    afterText = "Car speed robot analogy",
                    imageRes = R.drawable.nested_if_car
                )
            )
        ),

        LearningPage(
            title = "For Loop",
            content = """
What is a loop?

Sometimes programs repeat the same task multiple times.

A for loop is used when we know exactly how many repetitions are needed.

Imagine: Running on a track

A runner completes one lap at a time.

If the runner needs to complete 5 laps, the same action is repeated 5 times.

for(int lap = 1; lap <= 5; lap++){
    // Runner starts with lap 1

    System.out.println("Running lap " + lap);
    // Runner completes current lap
}

What happened?

lap = 1 → start at lap 1

lap <= 5 → stop after lap 5

lap++ → move to the next lap

Short Summary

A for loop repeats code a known number of times. It is useful when the program knows where to start, when to stop, and how to move to the next step.
""",
            imagePlacements = listOf(
                InlineImage(
                    afterText = "Sometimes programs repeat the same task multiple times.",
                    imageRes = R.drawable.loop_loparbana
                )
            )
        ),

        LearningPage(
            title = "While Loop",
            content = """
While loop

A while loop repeats as long as a condition remains true.

Imagine: Filling a glass with water

You keep pouring water while the glass is not full.

As long as there is space → continue pouring.

When the glass is full → stop.

int waterLevel = 0;

while(waterLevel < 5){
    System.out.println("Pouring water");

    waterLevel++;
}

What happened?

waterLevel < 5 → continue pouring

waterLevel = 5 → the glass is full, stop

Short Summary

A while loop repeats as long as a condition is true. It is useful when the program does not know exactly how many repetitions will be needed.
""",
            imagePlacements = listOf(
                InlineImage(
                    afterText = "A while loop repeats as long as a condition remains true.",
                    imageRes = R.drawable.while_loop_water_glass
                )
            )
        ),

        LearningPage(
            title = "Do While Loop",
            content = """
Do while loop

Runs at least once before checking condition.


Restaurant tasting.

A chef must taste food at least once
before deciding whether to continue.


int tries = 1;
// First food test

do{
    System.out.println("Taste food");
    // Chef tastes food

    tries++;
    // Another test completed

}
while(tries <= 3);
// Continue if more tests remain

Short Summary

A do while loop always runs at least once before checking the condition. It is useful when an action must happen before the program decides whether to continue.
""", imagePlacements = listOf(
                InlineImage(
                    afterText = "Restaurant tasting.",
                    imageRes = R.drawable.do_while_chef
                )
            )
        ),


        LearningPage(
            title = "Nested Loops",
            content = """
Nested loops

A loop inside another loop.


Classroom row.

A teacher checks students in rows.

3 rows

4 students per row

Teacher must check every student
inside every row.


for(int row = 1; row <= 3; row++){
    // Move through rows

    for(int student = 1; student <= 4; student++){
        // Move through students inside current row

        System.out.println(
            "Checking row " + row +
            " student " + student
        );
    }
}


What happened?

Outer loop = rows

Inner loop = students

Short Summary

Nested loops are loops inside other loops. They are useful when a program must repeat actions across rows, groups, or multiple levels.
""",        imagePlacements = listOf(
                InlineImage(
                    afterText = "A loop inside another loop.",
                    imageRes = R.drawable.nested_loops_classroom
                    )
            )
        )
    )

    private val treePages = listOf(
        LearningPage(
            title = "Binary Tree",
            content = """
Trees in Data Structures
Introduction

Sometimes programs need data to be stored in levels instead of straight lines.

A normal list looks like this:

1 → 2 → 3 → 4

A queue looks like people standing in line.

A stack looks like plates.

But some problems require branching paths.

Example:

File systems
Search engines
Game decision systems
Databases

This is where trees are used.

A tree stores data like a family hierarchy.

It starts from one top point and branches downward.
Binary Tree
Analogy: Family tree

A Binary Tree is like a family tree where each person can have at most two children:

        Parent
       /      \
 Left child  Right child

Important:

A Binary Tree only says:

Each node can have maximum two children.

It does not say smaller values must go left or bigger values must go right.

So this is allowed in a normal Binary Tree:

        50
       /  \
     90    10

 

Code
class Node {
    int value;

    Node left;
    // Left child in the family tree

    Node right;
    // Right child in the family tree

    Node(int value) {
        this.value = value;
        // This person/node receives a value
    }
}

Short Summary

A Binary Tree stores data in branches where each node can have at most two children. It is useful for representing hierarchical structures.
"""
        ),
        LearningPage(
            title = "Binary Search Tree",
            content = """
Trees in Data Structures
A tree is a data structure where data is stored in branches.

Instead of storing values in a straight line like:

10 -> 20 -> 30 -> 40

a tree stores values like levels:

        50
       /  \
     30    70

The first value becomes the root.

Values below it become children.


Binary Search Tree
Analogy: Smart family sorting by age

A Binary Search Tree is more organized than a normal Binary Tree.

It follows one strict rule:

Smaller values go left.
Bigger values go right.

Example:

        50
       /  \
     30    70

Why did 30 become the left child of 50?

Because:

30 < 50

So it goes left.

Why did 70 become the right child of 50?

Because:

70 > 50

So it goes right.

Full example

Values inserted:

50, 30, 70, 20, 40, 60, 80

Step by step:

50 enters first.

50

50 becomes the root because the tree is empty.

30 enters.

30 < 50

So 30 goes left.

        50
       /
     30

70 enters.

70 > 50

So 70 goes right.

        50
       /  \
     30    70

20 enters.

Compare with 50:

20 < 50

Go left to 30.

Compare with 30:

20 < 30

Go left again.

        50
       /  \
     30    70
    /
  20

40 enters.

Compare with 50:

40 < 50

Go left to 30.

Compare with 30:

40 > 30

Go right.

        50
       /  \
     30    70
    / \
  20  40

Final tree:

        50
       /  \
     30    70
    / \    / \
  20  40  60  80
Binary Search Tree Code
class Node {
    int value;
    Node left;
    Node right;

    Node(int value) {
        this.value = value;
        // A new family member/node gets a number
    }
}

class BinarySearchTree {

    Node root;

    public void insert(int value) {
        root = insertRecursive(root, value);
        // Start from the top/root of the family tree
    }

    private Node insertRecursive(Node current, int value) {

        if (current == null) {
            // Empty place found
            // The new family member sits here
            return new Node(value);
        }

        if (value < current.value) {
            // Smaller value goes to the left child
            current.left = insertRecursive(current.left, value);

        } else if (value > current.value) {
            // Bigger value goes to the right child
            current.right = insertRecursive(current.right, value);
        }

        return current;
        // Return the unchanged current node
    }

    public void printInOrder(Node node) {
        if (node != null) {
            printInOrder(node.left);
            // Visit smaller values first

            System.out.println(node.value);
            // Print current person/node

            printInOrder(node.right);
            // Visit bigger values after
        }
    }
}

Short Summary

A Binary Search Tree keeps smaller values on the left and larger values on the right. This organization makes searching and inserting values more structured.
"""
        ),
        LearningPage(
            title = "AVL Tree",
            content = """
AVL Tree
Balanced bookshelf

An AVL Tree is a Binary Search Tree, but stricter.

It still follows the BST rule:

Smaller values go left.
Bigger values go right.

But it also checks balance.

Imagine a bookshelf.

If too many books are placed on one side, the shelf tilts.

AVL fixes the shelf by rotating nodes.

AVL balance rule

Each node has a balance factor:

height(left side) - height(right side)

Allowed values:

-1, 0, 1

If the result becomes:

2

left side is too heavy.

If the result becomes:

-2

right side is too heavy.

Then AVL must rotate.

Example: Right-heavy tree

Insert:

10, 20, 30

Step 1:

10

Step 2:

20 is bigger than 10, so it goes right.

10
  \
  20

Step 3:

30 is bigger than 10, go right.
30 is bigger than 20, go right.

10
  \
  20
    \
    30

Now the shelf is leaning too much to the right.

AVL rotates left.

After rotation:

    20
   /  \
 10    30

20 becomes the new root because it is the middle value.

10 becomes the left child.

30 becomes the right child.

Balanced again.

AVL rotation types
Right Right case
10
  \
  20
    \
    30

Fix:

rotateLeft(10)
Left Left case
    30
   /
 20
 /
10

Fix:

rotateRight(30)
Left Right case
    30
   /
 10
   \
   20

Fix:

rotateLeft(10)
rotateRight(30)
Right Left case
10
  \
  30
 /
20

Fix:

rotateRight(30)
rotateLeft(10)
AVL Tree Code
class AVLNode {
    int value;
    int height;

    AVLNode left;
    AVLNode right;

    AVLNode(int value) {
        this.value = value;
        this.height = 1;
        // New book/node starts with height 1
    }
}

class AVLTree {

    AVLNode root;

    int height(AVLNode node) {
        if (node == null) {
            return 0;
            // Empty shelf side has height 0
        }

        return node.height;
    }

    int getBalance(AVLNode node) {
        if (node == null) {
            return 0;
        }

        return height(node.left) - height(node.right);
        // Positive means left side is heavier
        // Negative means right side is heavier
    }

    AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode temp = x.right;

        x.right = y;
        y.left = temp;

        // The left-heavy shelf is reorganized
        // x becomes the new top node

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode temp = y.left;

        y.left = x;
        x.right = temp;

        // The right-heavy shelf is reorganized
        // y becomes the new top node

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    AVLNode insert(AVLNode node, int value) {

        if (node == null) {
            return new AVLNode(value);
            // Empty shelf position found
        }

        if (value < node.value) {
            node.left = insert(node.left, value);
            // Smaller book/value goes to the left side

        } else if (value > node.value) {
            node.right = insert(node.right, value);
            // Bigger book/value goes to the right side

        } else {
            return node;
            // Duplicate values are ignored
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        // Update shelf height after inserting a new value

        int balance = getBalance(node);
        // Check if the shelf is leaning too much

        if (balance > 1 && value < node.left.value) {
            // Left Left case
            // Too heavy on left-left side
            return rotateRight(node);
        }

        if (balance < -1 && value > node.right.value) {
            // Right Right case
            // Too heavy on right-right side
            return rotateLeft(node);
        }

        if (balance > 1 && value > node.left.value) {
            // Left Right case
            // First fix left child, then fix current node
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && value < node.right.value) {
            // Right Left case
            // First fix right child, then fix current node
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
        // Node is already balanced
    }

    public void add(int value) {
        root = insert(root, value);
        // Insert value and keep the tree balanced
    }
}
AVL Rotation Types
Right Right case

Insert:

10, 20, 30

Step-by-step:

10
  \
  20
    \
    30

The bookshelf is too heavy on the right-right side.

Fix:

rotateLeft(10)

Result:

    20
   /  \
 10    30
Left Left case

Insert:

30, 20, 10

Step-by-step:

    30
   /
 20
 /
10

The bookshelf is too heavy on the left-left side.

Fix:

rotateRight(30)

Result:

    20
   /  \
 10    30
Left Right case

Insert:

30, 10, 20

Step-by-step:

    30
   /
 10
   \
   20

The bookshelf is heavy on the left side, but the new value is on the right side of the left child.

Fix:

rotateLeft(10)
rotateRight(30)

Result:

    20
   /  \
 10    30
Right Left case

Insert:

10, 30, 20

Step-by-step:

10
  \
  30
 /
20

The bookshelf is heavy on the right side, but the new value is on the left side of the right child.

Fix:

rotateRight(30)
rotateLeft(10)

Result:

    20
   /  \
 10    30
AVL Tree Code
class AVLNode {
    int value;
    // The number written on this book/node

    int height;
    // How tall this part of the bookshelf is

    AVLNode left;
    // The smaller books are placed on the left shelf

    AVLNode right;
    // The bigger books are placed on the right shelf

    AVLNode(int value) {
        this.value = value;
        // Put the number label on this new book/node

        this.height = 1;
        // A new single book starts as height 1
    }
}

class AVLTree {

    AVLNode root;
    // The top book/node of the whole balanced bookshelf

    int height(AVLNode node) {
        // This method checks how tall one side of the shelf is

        if (node == null) {
            // If there is no book on that side

            return 0;
            // Empty shelf side has height 0
        }

        return node.height;
        // Return the stored height of this shelf side
    }

    int getBalance(AVLNode node) {
        // This method checks if the shelf is leaning left or right

        if (node == null) {
            // If there is no shelf/node to check

            return 0;
            // No shelf means no leaning
        }

        return height(node.left) - height(node.right);
        // Left height minus right height
        // Positive result means left side is heavier
        // Negative result means right side is heavier
        // Allowed balance is -1, 0, or 1
    }

    AVLNode rotateRight(AVLNode y) {
        // Used when the shelf is too heavy on the left side

        AVLNode x = y.left;
        // x is the left child that will become the new top book

        AVLNode temp = x.right;
        // temp is the middle shelf part that must be moved safely

        x.right = y;
        // Put the old top book y to the right of x

        y.left = temp;
        // Attach the saved middle shelf part back to y's left side

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        // Recalculate height for the old top book after moving it down

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        // Recalculate height for the new top book

        return x;
        // Return x because x is now the new balanced top
    }

    AVLNode rotateLeft(AVLNode x) {
        // Used when the shelf is too heavy on the right side

        AVLNode y = x.right;
        // y is the right child that will become the new top book

        AVLNode temp = y.left;
        // temp is the middle shelf part that must be moved safely

        y.left = x;
        // Put the old top book x to the left of y

        x.right = temp;
        // Attach the saved middle shelf part back to x's right side

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        // Recalculate height for the old top book after moving it down

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        // Recalculate height for the new top book

        return y;
        // Return y because y is now the new balanced top
    }

    AVLNode insert(AVLNode node, int value) {
        // Insert a new book while keeping the shelf sorted and balanced

        if (node == null) {
            // If this shelf position is empty

            return new AVLNode(value);
            // Place the new book/node here
        }

        if (value < node.value) {
            // Smaller value belongs on the left shelf

            node.left = insert(node.left, value);
            // Walk left until an empty position is found

        } else if (value > node.value) {
            // Bigger value belongs on the right shelf

            node.right = insert(node.right, value);
            // Walk right until an empty position is found

        } else {
            // If the value already exists

            return node;
            // Do not add duplicate books/nodes
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        // After inserting, update how tall this shelf section has become

        int balance = getBalance(node);
        // Check if this shelf section is now leaning too much

        if (balance > 1 && value < node.left.value) {
            // Left Left case
            // The shelf is too heavy on the left-left side

            return rotateRight(node);
            // Rotate right to bring the middle book to the top
        }

        if (balance < -1 && value > node.right.value) {
            // Right Right case
            // The shelf is too heavy on the right-right side

            return rotateLeft(node);
            // Rotate left to bring the middle book to the top
        }

        if (balance > 1 && value > node.left.value) {
            // Left Right case
            // Heavy on the left, but the new book went to the right of the left child

            node.left = rotateLeft(node.left);
            // First straighten the left child shelf

            return rotateRight(node);
            // Then rotate the main shelf right
        }

        if (balance < -1 && value < node.right.value) {
            // Right Left case
            // Heavy on the right, but the new book went to the left of the right child

            node.right = rotateRight(node.right);
            // First straighten the right child shelf

            return rotateLeft(node);
            // Then rotate the main shelf left
        }

        return node;
        // If the shelf is already balanced, keep it as it is
    }

    public void add(int value) {
        // Public method used to add a new book/value

        root = insert(root, value);
        // Start from the top shelf/root and rebalance if needed
    }
}

Short Summary

An AVL Tree is a self-balancing Binary Search Tree. It keeps the tree balanced so searching, inserting, and deleting remain efficient.
"""
        ),
        LearningPage(
            title = "Heap",
            content = """
Heap
A heap is a tree-based data structure used when the most important value must be easy to access.

A heap does not care about full sorting like this:

10, 20, 30, 40, 50

Instead, it mainly cares about one rule:

The highest-priority value must stay at the top.

There are two common types:

Max Heap = biggest value at the top
Min Heap = smallest value at the top

Heap analogy: Hospital emergency room

Imagine a hospital emergency room.

Patients arrive with different emergency levels.

Higher number = more urgent case.

Example:

90 = heart attack
70 = broken leg
40 = fever
20 = small cut

The hospital does not need all patients perfectly sorted in one long line.

It only needs to make sure:

The most urgent patient is always treated first.

That is a Max Heap.

How values enter the heap

Insert:

90, 70, 40, 20
Step 1

90 arrives first.

The emergency room is empty, so 90 becomes the root.

90

90 is now the most urgent patient at the top.

Step 2

70 arrives.

It is placed in the next open position under 90.

        90
       /
     70

70 stays there because:

70 < 90

90 is still more urgent.

Step 3

40 arrives.

It is placed in the next open position.

        90
       /  \
     70    40

40 stays there because:

40 < 90

90 is still more urgent.

Step 4

20 arrives.

It is placed in the next open position under 70.

        90
       /  \
     70    40
    /
  20

20 stays there because:

20 < 70

The heap is valid.

Max Heap rule

In a Max Heap:

Parent must be bigger than its children.

Example:

        90
       /  \
     70    40
    /
  20

This is valid because:

90 > 70
90 > 40
70 > 20

The most urgent patient, 90, is at the top.

When a more urgent patient arrives

Insert:

50, 30, 80

Step 1:

50

50 arrives first, so it becomes the root.

Step 2:

  50
 /
30

30 is less urgent than 50, so it stays below.

Step 3:

80 enters at the next open position:

  50
 /  \
30   80

But 80 is more urgent than 50.

So 80 moves up.

Result:

  80
 /  \
30   50

This upward movement is called:

heapifyUp()
Remove example

When we remove from a Max Heap, we remove the top value.

That means:

The most urgent patient is treated first.

Example:

        90
       /  \
     70    40
    /
  20

Remove 90.

The hospital cannot leave the top empty.

So the last patient moves to the top temporarily:

        20
       /  \
     70    40

But 20 is not urgent enough to be above 70.

So it moves down.

Result:

        70
       /  \
     20    40

This downward movement is called:

heapifyDown()

Final idea:

heapifyUp() = urgent patient moves upward
heapifyDown() = less urgent patient moves downward

import java.util.ArrayList;
// The hospital uses a list to store patients in order

class MaxHeap {

    private ArrayList<Integer> heap = new ArrayList<>();
    // This is the hospital emergency waiting system
    // Each number represents a patient's urgency level

    public void insert(int value) {
        // A new patient arrives at the emergency room

        heap.add(value);
        // The patient first goes to the next open position
        // Example: 90 arrives first, so it becomes root
        // Example: 70 arrives next, so it goes under 90

        heapifyUp(heap.size() - 1);
        // After entering, the patient may move upward
        // If the new patient is more urgent than the parent, they swap places
    }

    private void heapifyUp(int index) {
        // This method moves a very urgent patient upward

        while (index > 0) {
            // Keep checking until the patient reaches the root
            // Or until the parent is more urgent

            int parentIndex = (index - 1) / 2;
            // Find the parent above this patient
            // In the hospital tree, this means: who is directly above me?

            if (heap.get(index) <= heap.get(parentIndex)) {
                // If the new patient is not more urgent than the parent

                break;
                // The patient stays where they are
            }

            swap(index, parentIndex);
            // The new patient is more urgent
            // So they move up and the parent moves down

            index = parentIndex;
            // Continue checking from the new higher position
        }
    }

    public int removeMax() {
        // The hospital treats the most urgent patient first

        if (heap.isEmpty()) {
            // If there are no patients waiting

            System.out.println("Heap is empty");
            // The hospital has nobody to treat

            return -1;
            // Nothing can be removed
        }

        int max = heap.get(0);
        // The root is always the most urgent patient

        int last = heap.remove(heap.size() - 1);
        // Take the last patient from the last open position

        if (!heap.isEmpty()) {
            // If there are still patients after removing the root

            heap.set(0, last);
            // Move the last patient to the root temporarily
            // The hospital cannot leave the top position empty

            heapifyDown(0);
            // Now move this patient down until priority order is fixed
        }

        return max;
        // Return the patient who was treated first
    }

    private void heapifyDown(int index) {
        // This method moves a less urgent patient downward

        int size = heap.size();
        // Count all patients currently waiting

        while (true) {
            // Keep checking until the patient is in the correct place

            int leftChild = 2 * index + 1;
            // Find the left child below this patient

            int rightChild = 2 * index + 2;
            // Find the right child below this patient

            int largest = index;
            // Assume the current patient is the most urgent for now

            if (leftChild < size && heap.get(leftChild) > heap.get(largest)) {
                // If the left child is more urgent

                largest = leftChild;
                // The left child should move up
            }

            if (rightChild < size && heap.get(rightChild) > heap.get(largest)) {
                // If the right child is more urgent than both

                largest = rightChild;
                // The right child should move up
            }

            if (largest == index) {
                // If the current patient is already more urgent than both children

                break;
                // The heap is fixed
            }

            swap(index, largest);
            // Swap the less urgent patient with the more urgent child

            index = largest;
            // Continue checking lower in the tree
        }
    }

    public int peek() {
        // Look at the most urgent patient without removing them

        if (heap.isEmpty()) {
            // If the emergency room is empty

            return -1;
            // Nobody is waiting
        }

        return heap.get(0);
        // Root is always the most urgent patient
    }

    private void swap(int first, int second) {
        // Two patients change places in the waiting system

        int temp = heap.get(first);
        // Temporarily hold the first patient

        heap.set(first, heap.get(second));
        // Move the second patient into the first position

        heap.set(second, temp);
        // Move the first patient into the second position
    }

    public void printHeap() {
        // Show the current emergency priority system

        System.out.println(heap);
        // Prints the heap as an array/list
    }

    public static void main(String[] args) {
        MaxHeap hospital = new MaxHeap();
        // Create the hospital emergency priority system

        hospital.insert(90);
        // 90 arrives first
        // Emergency room is empty, so 90 becomes root

        hospital.insert(70);
        // 70 arrives next
        // 70 is placed under 90 because 70 is less urgent than 90

        hospital.insert(40);
        // 40 arrives next
        // 40 is placed in the next open position under 90

        hospital.insert(20);
        // 20 arrives next
        // 20 is placed under 70 because it is less urgent

        hospital.printHeap();
        // Shows: [90, 70, 40, 20]

        hospital.insert(100);
        // 100 arrives
        // 100 is more urgent than 70, so it moves up
        // 100 is also more urgent than 90, so it becomes new root

        hospital.printHeap();
        // Shows the new heap where 100 is root

        int treatedPatient = hospital.removeMax();
        // The most urgent patient is treated first

        System.out.println("Treated patient urgency: " + treatedPatient);
        // Prints the removed root patient

        hospital.printHeap();
        // Shows the heap after heapifyDown fixes the order
    }
}

Short Summary

A heap keeps the highest-priority value at the top. It is useful when a program needs quick access to the most important value.
"""
        )
    )

    private val listPages = listOf(
        LearningPage(
            title = "List and Array",
            content = """
Programs often need to store multiple values, but not every 
collection works the same way. Sometimes data needs to be changeable, sometimes fixed, 
sometimes unique, or connected to labels. That’s why structures like lists, tuples, sets, 
dictionaries, and arrays exist.
             
Imagine you run different types of storage systems in real life
When programs store multiple pieces of data, they don’t always store them the same way.
Sometimes you need:
a flexible shopping list
a locked collection
labeled information
unique items only


That’s why programming has different data structures.

List → A shopping cart
A list is like a shopping cart in a grocery store.
You can keep adding items:
Milk
Bread
Eggs
Cart: [Milk, Bread, Eggs]
Why use it?
Because the cart is flexible: add new items, remove items, change items. keep order

Example: shopping = ["Milk", "Bread", "Eggs"]
Common actions:
shopping.append("Juice") # Add another item to the cart.
shopping.remove("Bread") # Remove an item from the cart.
shopping.pop() # Remove the last added item.
shopping[0] # Check the first item in the cart.
len(shopping) # Count how many items are in the cart

Used when:
Student grades
Game inventory
Tasks list




Array → Apartment building
An array is like apartments in a building.
Apartment 0 → Ahmed
Apartment 1 → Sara
Apartment 2 → John
Each apartment has a fixed position.
Example:
int[] numbers = {10,20,30};
Common actions:
numbers[0] # Check who lives in apartment 0
numbers.length # Count apartments
Arrays are fast and organized.
But usually fixed size.
You can’t easily expand the building.

Why so many different structures?
Imagine building everything with only shopping carts.
That would be inefficient.
Sometimes you need:
Flexible storage → List
Locked storage → Tuple
No duplicates → Set
Labeled storage → Dictionary
Fixed fast storage → Array
Different problems need different tools.

Short Summary

Lists and arrays store multiple values in order. Each item has a position, which makes it possible to access, update, and organize related values.
""",
            imagePlacements = listOf(
                InlineImage(
                    afterText = "List → A shopping cart",
                    imageRes = R.drawable.inkopslista
                ),
                InlineImage(
                    afterText = "Each apartment has a fixed position.",
                    imageRes = R.drawable.hylla_numrerade_lador
                )
            )
        ),
        LearningPage(
            title = "Tuple",
            content = """
Tuple → Sealed package
A tuple is like ordering a combo meal.
You receive:
Burger
Fries
Drink
Once sealed:
You cannot change it.
Example:
meal = ("Burger", "Fries", "Drink")
Common actions:
meal[0]# Check the first item in the package.
len(meal)# Count how many items exist.
meal.index("Fries")# Find where fries are located
You cannot do this:
meal[0] = "Pizza"# You cannot replace food in a sealed package
Used when:
Coordinates
RGB colors
Birth dates
...

Short Summary

A tuple groups values together in a fixed structure. It is useful when values belong together and should not be changed.
""",           imagePlacements = listOf(
                InlineImage(
                    afterText = "Tuple → Sealed package",
                    imageRes = R.drawable.tuple_sealed_package
                )
            )
        ),

        LearningPage(
            title = "Set",
            content = """
Set → Security club guest list
A set only allows unique people.
Guest list:
Ali
Sara
John
Ali tries entering twice.
Security says:
"Your name is already here."
Example:
guests = {"Ali", "Sara", "John"}
Common actions:
guests.add("Mike") # Add a new unique guest.
guests.remove("Ali") # Remove someone from the guest list.
len(guests) # Count unique guests
Used when:
Unique usernames
Unique product IDs
Unique visitors

Short Summary

A set stores unique values. It is useful when duplicates should be avoided and membership checking is important.
""",        imagePlacements = listOf(
                InlineImage(
                    afterText = "Set → Security club guest list",
                    imageRes = R.drawable.set_security_guest_list
                )
            )
        ),
        LearningPage(
            title = "Dictionary",
            content = """
Dictionary → Real-world contact book
A dictionary stores labels with values.
Think of phone contacts:
Ali → 070123456
Sara → 070999999
Example:
contacts = {    "Ali": "070123456",    "Sara": "070999999"}
Common actions:
contacts["Ali"] # Find Ali's phone number.
contacts["John"] = "070111111" # Add a new contact.
contacts.pop("Sara") # Remove a contact.
contacts.keys() # Show all contact names
Used when:
Student → grade
Country → capital
Product → price

Short Summary

A dictionary stores data as key-value pairs. It is useful when a program needs to find a value using a meaningful name or key.
""",        imagePlacements = listOf(
                InlineImage(
                    afterText = "Dictionary → Real-world contact book",
                    imageRes = R.drawable.dictionary_contact_book
                )
            )
        )

    )

    private val codeProgramPages = listOf(
        LearningPage(
            title = "Digit Sum Program",
            content = """
What is a program?
A program is a collection of instructions that tells a computer what to do.
Code is written in languages such as Java, Python, or JavaScript.

What are we building?
We are building a program that takes a three-digit number and calculates the sum of all its digits.
Example:

Input: 753

The program should calculate: 7 + 5 + 3 = 15

Final output: 15

A three-floor building
Think of a three-digit number as a building with three floors.
Example: 753
Top floor → 7
Middle floor → 5
Bottom floor → 3

Our job is to enter the building and collect the number from each floor.
After collecting all floor numbers, you add them together.

number = int(input('Input a three digit number: '))
# Someone gives you a building address. Example: building 753
# # You write that address on a note/box labeled "number" so you can use it later

last_digit = number % 10
# You first visit the bottom floor
# Modulus (%)  gives only the remainder after division. 753 ÷ 10 = 75 remainder 3
# You collect number 3
# # You store that value in a box labeled "Last_digit"

number = number // 10
# You divide the building address by 10 → 753 ÷ 10 = 75.3
# The // throws away everything after the decimal
# That removes the last floor (3), so the building becomes 75

middle_digit = number % 10
# The remainder checks the new building (75)
# 75 ÷ 10 = 7 remainder 5
# It grabs the new bottom floor number (5)
# You store that value in a box labeled "middle_digit"

number = number // 10
# The middle floor disappears too
# Building becomes 7

first_digit = number
# Only the top floor remains
# You collect 7

sum_of_digits = first_digit + middle_digit + last_digit
# You combine all collected floor numbers,  7 + 5 + 3

print('The sum of the three digit is ' + str(sum_of_digits))
# You announce the final result

What happened?
Building: 753
became: 75
then: 7

And you collected each floor number during the process.

That’s how the program finds the sum of all digits.

Short Summary

This program breaks a number into digits and adds them together. It shows how loops, arithmetic, and variables can solve a small problem step by step.
"""
        ),
        LearningPage(
            title = "ATM Program",
            content = """
ATM Program

A program does not always run once and stop.

Some programs continue running until the user decides to exit.

Examples:

ATM machines
Game menus
Restaurant ordering systems
Mobile apps

They keep waiting for user choices.

What are we building?

We are building an ATM program.

The ATM allows a user to:

Withdraw money
Deposit money
Check balance
Exit the machine

Example:

Starting balance: 1000

Withdraw: 200

New balance: 800

ATM machine analogy

Imagine standing in front of an ATM machine.

The ATM screen keeps showing options:

1 Withdraw
2 Deposit
3 Check balance
4 Exit

The machine stays active until you choose to leave.

balance = 1000
# Your bank account starts with 1000 dollars
# The ATM stores your money amount inside a box called balance

while True:
# The ATM machine stays turned on
# It keeps showing options again and again

    print("1. Withdraw")
    # ATM shows withdraw option

    print("2. Deposit")
    # ATM shows deposit option

    print("3. Check balance")
    # ATM shows balance option

    print("4. Exit")
    # ATM shows exit option

    choice = input("Choose option: ")
    # The ATM waits for the user to press a button
    # The chosen option gets stored inside choice

    if choice == "1":
    # User selected withdraw

        amount = int(input("Enter withdraw amount: "))
        # User types how much money they want

        balance = balance - amount
        # Money leaves your account
        # Your balance becomes smaller

        print("New balance:", balance)
        # ATM shows your updated balance

    elif choice == "2":
    # User selected deposit

        amount = int(input("Enter deposit amount: "))
        # User chooses how much money to add

        balance = balance + amount
        # Money gets added to your account

        print("New balance:", balance)
        # ATM shows updated balance

    elif choice == "3":
    # User wants to check current balance

        print("Current balance:", balance)
        # ATM simply displays remaining money

    elif choice == "4":
    # User wants to leave the ATM

        print("Goodbye")
        # ATM says goodbye

        break
        # Machine stops running

What happened?

The ATM kept repeating:

Show menu
Wait for input
Perform action
Show menu again

Until the user selected exit.

That is how many real programs work.

They continue running until the user decides to stop them.

Short Summary

The ATM program shows how conditions, variables, and user choices work together. It models a real-world process using programming logic.
"""
        )
    )

    private val oopPages = listOf(
        LearningPage(
            title = "Inheritance",
            content = """
Inheritance

Sometimes programmers create many objects that share similar features.

Imagine a company hiring different workers:

Manager
Developer
Designer

All workers have common things:

name
salary
work()

Instead of rewriting these same things in every class, Java allows one class to pass its common features to other classes.

This is called inheritance.

A child class inherits from a parent class.

Parent = general blueprint
Child = more specific version

Real-life analogy: Family restaurant recipe

Imagine a restaurant creates a basic burger recipe:

bread
meat
vegetables

This is the original recipe.

Then the restaurant creates new burger versions:

Cheese Burger
→ uses the original recipe + adds cheese

Spicy Burger
→ uses the original recipe + adds spicy sauce

Vegan Burger
→ uses the original recipe + replaces meat

The restaurant does not rebuild every burger from zero.

It reuses the original recipe.

That is inheritance.

Parent class
class Employee {
    String name;
    double salary;

    void work() {
        System.out.println("Employee is working");
    }
}

This is the parent class.

It contains common things all employees share.

Child class
class Developer extends Employee {

    void writeCode() {
        System.out.println("Developer writes code");
    }
}

extends Employee

means:

Developer inherits everything from Employee.

Developer automatically gets:

name
salary
work()

without rewriting them.

Another child class
class Manager extends Employee {

    void approveBudget() {
        System.out.println("Manager approved budget");
    }
}

Manager also inherits from Employee.

What inheritance saves

Without inheritance:

You would rewrite:

name
salary
work()

inside every class.

That creates repeated code.

Inheritance helps programmers:

reuse code
save time
keep programs organized
Beginner summary

Parent class = general blueprint

Child class = more specific version

Inheritance = child class reuses parent features instead of rebuilding everything from scratch.

Precis som en burgerkedja återanvänder ett grundrecept för att skapa flera nya burgare.

Short Summary

Inheritance lets one class reuse and extend another class. It helps organize shared behavior while allowing specialized versions.
"""
        ),
        LearningPage(
            title = "BlockingQueue",
            content = """
BlockingQueue

Sometimes multiple parts of a program need to share data safely.

Imagine a restaurant.

Waiters take customer orders.

Chefs cook the food.

The waiters and chefs need a safe way to pass orders to each other.

If waiters throw order papers directly at chefs:

orders may get lost
chefs may miss orders
too many orders may arrive at once

This creates chaos.

To solve this, the restaurant creates a special shelf between them.

This shelf represents:

BlockingQueue

Waiters place orders on the shelf.

Chefs take orders from the shelf.

The shelf keeps everything organized.

Producer

The waiter adds a new order to the shelf.

queue.put("Burger");

The waiter is called the:

Producer

Because it produces data for others.

Consumer

The chef removes an order from the shelf.

queue.take();

The chef is called the:

Consumer

Because it consumes data.

If the shelf is empty

A chef walks to the shelf.

There are no orders.

The chef cannot cook anything.

So the chef waits.

This matches:

Queue empty → consumer waits

If the shelf is full

The shelf only holds 5 orders.

The waiter keeps bringing new orders.

There is no more space.

The waiter must wait.

This matches:

Queue full → producer waits

Java example
BlockingQueue<String> orders =
new ArrayBlockingQueue<>(5);

This means:

The shelf can hold maximum 5 orders.

Why this helps

Waiters and chefs do not fight over orders.

The shelf handles everything safely.

Programs use BlockingQueue the same way:

Different threads can safely pass data without creating chaos.

Final summary

BlockingQueue = shared order shelf

Producer = waiter

Consumer = chef

put() = place order

take() = remove order

Empty queue → consumer waits

Full queue → producer waits

Safe communication between threads.

Composition vs Aggregation
Introduction

Short Summary

BlockingQueue manages data safely when multiple threads work together. It can make one thread wait until data is available or space is free.
"""
        ),
        LearningPage(
            title = "Packages and OOAD",
            content = """
Packages in Java

As programs grow, developers create many classes.
For example in an e-commerce app:
Payment classes
User classes
Product classes
Order classes
Delivery classes

If every class is thrown into one giant folder, the project becomes messy.

Developers won’t know:
Where payment logic is
Where user logic is
Where product logic is

This is why Java uses packages.
Packages help organize related classes and interfaces into structured groups.
They improve modularity and make large programs easier to manage.
First understand the problem

Imagine a huge shopping mall.
Inside the mall, everything is dumped into one giant room:
Shoes
Food
Phones
Furniture
Clothes

Customers would struggle to find anything.
Employees would waste time searching.
Large Java projects have the same problem without packages.

Package: Shopping mall departments
The mall decides to organize everything into departments.
Electronics department:
Phones
TVs
Computers

Clothing department:
Shirts
Shoes
Jackets

Food department:
Restaurants
Snacks
Drinks

Each department represents a package.
Classes inside packages
Inside electronics:
Phone.java
Laptop.java
TV.java
// Electronics products stay in electronics section

Inside clothing:
Shirt.java
Shoes.java
Jacket.java
// Clothing products stay in clothing section

This is exactly what packages do.
They group related classes together.
Java example

package electronics;
// This class belongs to electronics department
public class Phone {

}

Another package:

package clothing;
// This class belongs to clothing department
Hierarchical organization

Large malls may have sub-sections:

Electronics

→ Phones

→ Accessories

Java packages can also be hierarchical:

com.store.payment
// Mall → payment section → specific payment area
com.store.users
// Mall → user management section
Modularity
If something breaks in electronics:
The food department keeps working.

Packages help separate parts of the system.
This makes maintenance easier.
Importing from another package
If a customer leaves clothing and visits electronics:

That’s similar to importing classes.

import electronics.Phone;
// Employee goes to electronics department to get a phone

Final summary
Classes = products
Package = department
Hierarchical packages = mall sections/subsections
Imports = accessing another department
Packages organize large Java projects and keep code modular.



Object-Oriented Analysis and Design (OOAD)
Before developers start coding large systems, they usually don’t jump directly into writing classes.
Imagine building:
A banking system
A hospital system
A food delivery app
A social media platform

If developers immediately start coding without planning:
Classes may be badly structured
Features may conflict
Scaling becomes difficult
Maintenance becomes painful

This is why OOAD exists.

OOAD helps developers analyze real-world problems first, then design software using objects and classes in a structured way.
The goal is to create software that is easier to maintain and easier to scale.

Think : Building a new city

Imagine a government wants to build an entirely new city.

They need:
Houses
Roads
Hospitals
Schools
Electricity systems

If workers immediately begin construction without planning:
Someone may build roads through hospitals.

Schools may be placed in bad locations.
Infrastructure becomes chaotic.
That would be a disaster.

Object-Oriented Analysis = city planning phase

Before construction begins:
City planners study what the city needs.

They ask:
How many hospitals?
How many schools?
Where should roads go?
How many citizens will live there?

This is like analyzing software requirements.

In OOAD: Developers identify important objects.

For a food delivery app:
Customer
Restaurant
Order
Driver
Payment

These become potential classes.

// Planners identify what buildings are needed
Object-Oriented Design = blueprint phase
Now architects create detailed blueprints.

They decide:
Where roads connect
How buildings interact
Future expansion areas

This is like software design.

Developers decide:
Which class talks to another class
Which methods are needed
Which relationships exist

Example:
Customer
Order
Payment
Driver
   Architects design how city parts connect
Construction phase = coding
After proper planning:
Workers finally begin building.
This is actual programming.

   Developers now write the code
Why scalability matters
Imagine the city grows from:
10,000 people → 2 million people
A badly designed city collapses.
Bad software does the same.

OOAD helps prepare systems for future growth.
Why maintainability matters
Imagine water pipes break.
A well-designed city can fix one area without destroying everything else.
Well-designed software works the same way.
Developers can update one class without breaking the entire system.

Final summary
Object-oriented analysis = planning what the city needs
Object-oriented design = creating blueprints
Coding = actual construction
Goal:
Build software using objects/classes that is maintainable and scalable.

Short Summary

Packages organize code into groups, while OOAD helps design systems with objects and responsibilities. Together, they make larger programs easier to structure.
"""
        ),
        LearningPage(
            title = "Access and Methods",
            content = """
Access Modifiers in Java
When building software, not everyone should have access to everything.
Imagine a company building:

Employee records
Salary systems
Customer accounts
Internal tools

If every employee could access every room:

Interns could edit CEO salary
Customers could enter private offices
Sensitive information could be leaked
Software has the same problem.
That’s why Java uses access modifiers.
They control who can access fields and methods.

Think of them as security levels inside a company building.

Imagine a large company office.
It has different rooms with different access levels.
Each room represents fields or methods.
Different access modifiers decide who can enter.

Private = only your personal office

Imagine your private office.
Only you can enter.
No coworkers allowed.
This is: private

private double salary;
   Only the owner of this office can enter
Only the same class can access it.

Public = everyone can enter

Imagine the company reception desk.
Everyone can access it:
Employees
Visitors
Delivery workers

This is: public

public void login() {
}
// Open reception area

Accessible from anywhere.

Protected = family employees + department workers

Imagine a special room accessible to:

Your department employees
Family branches of the company

This is: protected

protected void companyPolicy() {
}
// Department workers + child branches allowed

In Java:
Same package
Subclasses
Default (no modifier) = same department only

This is the question’s correct answer.
Imagine a room with no badge label.
Only people working in the same department can enter.
People from other departments cannot.
This is Java’s default/package-private access.

String departmentReport;
// No modifier = same department only

No keyword is written.

Example
Finance department employees can access it.
HR department employees cannot.

That matches:
Same package → allowed
Different package → blocked

Why this matters
Without access modifiers:
Everyone could modify everything.
Large systems would become unsafe.

Final summary
Private → only same class
Public → everyone
Protected → same package + subclasses
Default (no modifier) → same package only
The correct answer focuses on default access.



Method Overloading vs Method Overriding
When learning OOP, beginners often see similar words and think they mean the same thing.
Overloading and Overriding sound nearly identical, but they happen in completely different situations.

Think of programming like managing real-world systems where people, machines, and services perform actions.

Sometimes the same action accepts different types of requests.
Sometimes a newer version completely replaces how an old action works.
That’s exactly what these two concepts explain.

Method Overloading
Restaurant orders with different request sizes

Imagine you work at a restaurant.
Customers can place orders in different ways:

Customer 1:
\"Burger\"

Customer 2:
\"Burger + fries\"

Customer 3:
\"Burger + fries + drink\"

The waiter still performs the same task: takeOrder

But depending on what the customer includes, the waiter handles it differently.
Same method name → different parameters.

Code:

class Restaurant {

    void takeOrder(String food) {
        // Customer gives only one item
        
        System.out.println("Order: " + food);
        // Waiter prepares only the main food item
    }

    void takeOrder(String food, String side) {
        // Customer gives two items:
        // main food + side dish
        
        System.out.println("Order: " + food + " with " + side);
        // Waiter handles a larger order
    }

    void takeOrder(String food, String side, String drink) {
        // Customer gives three items:
        // main food + side dish + drink
        
        System.out.println("Order: " + food + ", " + side + ", " + drink);
        // Waiter handles the biggest order variation
    }
}
Same method name (takeOrder) + different number/types of parameters = different versions of handling 
customer orders → this is method overloading.


Method Overriding
Different delivery vehicles replacing how delivery happens
A company has one general delivery rule:
"Deliver the package to the customer."
That is the parent class behavior.
But different vehicles deliver in different ways:

A truck uses roads
A boat uses water routes
A drone flies through the air

They all still perform the same task: deliverPackage()

But each vehicle replaces the original delivery method with its own version.
Example:
class DeliveryVehicle {
    void deliverPackage() {
        // Default delivery worker with a general delivery method
        
        System.out.println("Package delivered");
        // Basic delivery message
    }
}

class Truck extends DeliveryVehicle {
    void deliverPackage() {
        // Truck replaces the parent's delivery method
        // because trucks deliver differently
        
        System.out.println("Truck delivers using roads");
        // Truck-specific delivery behavior
    }
}

class Boat extends DeliveryVehicle {

    void deliverPackage() {
        // Boat also replaces the parent's delivery method
        // because boats use water routes
        
        System.out.println("Boat delivers using water routes");
        // Boat-specific delivery behavior
    }
}

class Drone extends DeliveryVehicle {
    void deliverPackage() {
        // Drone replaces the parent's delivery method
        // because drones deliver by air
        
        System.out.println("Drone delivers by flying");
        // Drone-specific delivery behavior
    }
}

Same method name (deliverPackage) + child classes replace the parent’s version with their own 
behavior → this is method overriding.
Summary
Overloading = same worker handling different request types.
Overriding = child replaces how inherited work is done.

Short Summary

Access modifiers control what parts of code can be reached from outside a class. Methods define actions that objects or classes can perform.
"""
        ),
        LearningPage(
            title = "Abstract Classes",
            content = """
 Abstract classes
Art school blueprint
Imagine an art school.
The school creates a general rule:
Every shape must be drawable.

But the school doesn’t know exactly how every shape should be drawn.

A circle draws differently from a square.

So the school creates a general parent blueprint:
Shape

Then child classes complete the missing details:
Circle
Square

Abstract parent class
abstract class Shape {
    // Abstract parent blueprint created by the art school
    // The school knows every shape must be drawable
    
    protected String color;
   // Every shape stores a color

    public Shape(String color) {
    // The parent creates a general drawing rule
    // but leaves the actual behavior unfinished
    // Child classes must build their own drawing behavior
    // The parent receives the color information
    
        this.color = color;
        // Store this shape's color
        
    }

    public abstract void draw();
    // Every child shape must explain how it draws itself
}

abstract means:
The parent creates general rules but leaves some work unfinished.


Circle child class
class Circle extends Shape {
// Circle is a child class
// It inherits from Shape and completes the missing drawing behavior

    public Circle(String color) {
    // Circle receives its own color
    
        super(color);
        // Send color to parent constructor
    }

    @Override
    public void draw() {
        // Circle replaces the unfinished parent draw method
        // and creates its own drawing behavior

        if ("red".equals(color)) {
        // Check if this circle is red
        
            System.out.println("Drawing a red circle");
            // Draw special red circle output
            
        } else {
          // If the circle is not red
          
            System.out.println("Drawing a circle");
            // Draw normal circle output
        }
    }
}

class Square extends Shape {
    // Square is another child class
    // It inherits from Shape and builds its own drawing behavior

    public Square(String color) {
        // Square receives color information

        super(color);
        // Send the color information to the parent
    }


    @Override
    public void draw() {
        // Square builds the missing behavior
        // that the parent left unfinished

        if ("blue".equals(color)) {
            // Check if this square is blue

            System.out.println("Drawing a blue square");
            // Draw special blue square output

        } else {
            // If the square is not blue

            System.out.println("Drawing a square");
            // Draw normal square output
        }
    }
}

Short Summary

Abstract classes provide a shared base that cannot be used directly as a complete object. They define common structure for subclasses.
"""
        ),
        LearningPage(
            title = "Polymorphism",
            content = """
Polymorphism
Introduction for a beginner

Before understanding polymorphism, a beginner first needs to understand what parent and child classes are.

Imagine a big electronics company.

The company notices that many products share common traits:

TVs turn on
Speakers turn on
Air conditioners turn on

Instead of writing separate rules from scratch for every product, the company creates one general blueprint called:

Device

This blueprint contains common things that all devices share.

This becomes the parent class.

Then the company creates more specific products based on that parent blueprint:

TV
Speaker
AirConditioner

These are called child classes because they inherit from the parent.

Think of it like:

Parent = general category
Child = more specific version of that category

Just like:

Parent → Vehicle
Children → Car, Boat, Plane

or

Parent → Animal
Children → Dog, Cat, Bird

Real-world analogy: Universal remote

Now imagine you have one universal remote.

The remote only knows:

"I control devices."

It doesn’t know whether it’s controlling:

A TV
A Speaker
An Air Conditioner

It just sends one command:

Power On

If the remote reaches a TV:

TV screen turns on

If it reaches an Air Conditioner:

AC starts cooling

If it reaches a Speaker:

Speaker starts playing music

Same command.

Different result.

How parent and child appear in code
class Device {
    void powerOn() {
        // Parent creates a general rule for all devices
        System.out.println("Device turns on");
    }
}

This is the parent.

It creates the general rule.

class TV extends Device {
    void powerOn() {
        // Child TV replaces the parent's behavior
        System.out.println("TV screen turns on");
    }
}

extends Device

This is where child appears.

It means:

TV is built from the parent Device blueprint.

class Speaker extends Device {
    void powerOn() {
        // Child speaker creates its own version
        System.out.println("Speaker starts music");
    }
}
Where polymorphism happens
Device myDevice = new TV();

This line means:

The remote thinks:

"I'm controlling a general Device"

But the real object is:

TV

Then:

myDevice.powerOn();

Java checks the real child object during runtime and runs that version.

Parent = general blueprint

Child = more specific version built from parent

Polymorphism = same command behaves differently depending on which child object is used.

Exactly like one remote controlling different devices differently..

Short Summary

Polymorphism allows the same method call to behave differently depending on the object. It makes code more flexible and reusable.
"""
        ),
        LearningPage(
            title = "Encapsulation",
            content = """
Encapsulation
Introduction

Imagine a hospital storing patient records.

Each patient has sensitive information:

Name
Age
Diagnosis
Medicine

If anyone could walk into the hospital database room and change records freely:

Wrong medicine could be assigned
Ages could be changed accidentally
Records could be corrupted

A system needs protection and controlled access.

That is encapsulation.

Encapsulation means:

Keep related data and methods together
Protect internal data using private
Use getters to safely read data
Use setters to safely update data
Hide internal implementation details
Real-world analogy: Hospital records room

Inside the hospital there is a locked records room.

This room stores patient data.

name
age
diagnosis
medicine

This is the class data/attributes.

Hospital staff members interact with these records:

Receptionist checks patient information
Doctor updates prescriptions
Nurse reads treatment plan

These are methods.

Step 1: Private = locked records room

Patients cannot walk directly into the records room.

private String diagnosis;
// Records room door is locked
// Patients cannot directly edit diagnosis

Without private:

patient.diagnosis = "Random disease";
// Random person edits hospital records
Step 2: How getters are actually used

Imagine a patient asks:

"What medicine am I currently taking?"

The patient cannot enter the records room.

They ask the receptionist.

Receptionist checks records and gives the information.

That receptionist is the getter.

public String getMedicine() {
    return medicine;
}
// Receptionist checks file
// Then tells patient the answer
Using the getter
Patient p = new Patient();

System.out.println(p.getMedicine());
// Patient asks receptionist for medicine info
Step 3: How setters are actually used

Now imagine a doctor wants to update medicine.

The doctor doesn’t enter the records room directly.

They tell the hospital staff to update the record properly.

That staff member is the setter.

public void setMedicine(String medicine) {
    this.medicine = medicine;
}
// Staff updates patient record safely
Using the setter
Patient p = new Patient();

p.setMedicine("Antibiotics");
// Doctor requests medicine update
Step 4: Validation inside setters

Hospital staff may reject dangerous updates.

public void setAge(int age) {
    if(age > 0){
        this.age = age;
    }
}
// Staff refuses impossible age values
Step 5: Internal implementation hiding

Hospital may switch from paper files to digital systems.

Patients still:

Ask for information
Update records through staff

They don’t need to know what changed internally.

Final summary

Encapsulation works like a hospital:

Private → locked records room
Getter → asks staff to read information
Setter → asks staff to update information
Validation → prevents bad updates
Hidden implementation → internal systems can change safely

That is how encapsulation works in practice.

Short Summary

Encapsulation protects data inside a class and controls access through methods. It helps make code safer and easier to maintain.
"""
        ),
        LearningPage(
            title = "Composition vs Aggregation",
            content = """
In OOP, objects often work together.

For example:

A car has an engine
A school has teachers
A phone has apps

But not all relationships between objects are equally strong.

Sometimes one object completely owns another object.

Sometimes objects are only loosely connected.

This is where composition and aggregation come in.

Both describe "has-a" relationships, but the strength of ownership is different.

First understand objects

Imagine a university.

The university contains many things:

Classrooms
Teachers
Students
Cafeteria

These are separate objects.

Now we need to ask:

What happens if the university disappears?

Do all those objects disappear too?

That’s where composition vs aggregation differs.

Composition = strong ownership
Analogy: Human body and heart

Imagine a human body.

The body contains a heart.

The heart is fully dependent on the body in this analogy.

If the body dies:

The heart no longer functions as an independent living object.

The heart’s lifecycle is strongly tied to the body.

This is composition.

Code
class Heart {

}

class Human {

    private Heart heart = new Heart();

}

Human creates its own heart.

The heart belongs strongly to the human.

If the Human object disappears:

The Heart disappears too.

Strong ownership.

Why?

The human directly creates the heart:

new Heart()

This usually signals composition.

Aggregation = weaker ownership
Analogy: University and teacher

A university has teachers.

But teachers can exist without that specific university.

A teacher can:

Quit
Move to another university
Teach elsewhere

If the university shuts down:

The teacher still exists.

That is aggregation.

Code
class Teacher {

}

class University {

    private Teacher teacher;

    public University(Teacher teacher) {
        this.teacher = teacher;
    }
}

The university receives an already existing teacher.

It does not create the teacher.

If the University disappears:

The Teacher can still exist.

Weak ownership.

Easy memory trick
Composition

Container creates object internally

new Heart()
Strong ownership
Same lifetime
Aggregation

Object is passed from outside

University(Teacher teacher)
Weak ownership
Independent lifetime
UML perspective

Composition:

Filled diamond

Strong ownership

Aggregation:

Empty diamond

Weak ownership

Final summary

Composition:

Body owns heart

Heart depends on body

Same lifetime

Aggregation:

University has teacher

Teacher can exist independently

Different lifetime

That is the key difference.

Short Summary

Composition and aggregation describe relationships between objects. Composition means strong ownership, while aggregation means a weaker connection.
"""
        )
    )
    private val variablePages = listOf(
        LearningPage(
            title = "Variables",
            content = """
What is a Variable?

A variable is a container used to store information in a program.

Programs often need to remember things such as:

A person's name
A person's age
A score in a game
The price of an item

Variables allow the program to store this information and use it later.

Why Do We Need Variables?

Imagine trying to remember every piece of information without writing anything down.

That would be difficult.

Variables give the program a place to store information so it can be accessed, changed, and reused whenever needed.

Example

int age = 25;

Here:

int is the data type
age is the variable name
25 is the value stored in the variable

The program now remembers that age is 25.

Analogy

Think of a labeled storage box.

The label on the box is the variable name.

The item inside the box is the value.

Example:

Box Label: Age
Contents: 25

The program can look at the label and find the stored information whenever it needs it.

Variables Can Change

Unlike a real label on a box, the contents of a variable can be replaced.

int age = 25;

age = 26;

The variable is still called age, but the value has changed from 25 to 26.

Everyday Example

Imagine a refrigerator with a note attached to it:

Milk = 2 liters

Later you buy more milk:

Milk = 4 liters

The label stays the same, but the amount changes.

Variables work in a similar way.

Another Analogy

Think of a parking space with a sign:

Parking Spot A

Today it contains a red car.

Tomorrow it contains a blue car.

The parking space remains the same, but what is stored there can change.

A variable works like that parking space.

Summary

A variable is:

A named storage location
Used to remember information
Able to store different types of data
Able to change its value during a program

Just as labeled boxes help people organize their belongings, variables help programs organize and store information.

Short Summary

A variable is a named storage location that helps a program remember information. Variables can store values, update those values, and make information available whenever the program needs it.
""",
            imagePlacements = listOf(
                InlineImage(
                    afterText = "A variable is a container used to store information in a program.",
                    imageRes = R.drawable.lada_med_innehall
                ),
                InlineImage(
                    afterText = "Think of a labeled storage box.",
                    imageRes = R.drawable.forvaringslada
                ),
                InlineImage(
                    afterText = "Think of a parking space with a sign:",
                    imageRes = R.drawable.parkeringsplats_bil
                )
            )
        )
    )

    private val dataTypePages = listOf(
        LearningPage(
            title = "Data Types",
            content = """
Data Types

What are data types?

Data types tell the computer what kind of information a variable can store.

Just like different containers are designed for different things, different data types are designed for different kinds of data.

1. int (Integer)

What is it?

Used to store whole numbers without decimals.

int apples = 5;

Analogy

Think of an egg carton where you count whole eggs.

1 egg
2 eggs
10 eggs

You cannot have 2.5 eggs in the carton.

2. double (Decimal Number)

What is it?

Used to store numbers with decimals.

double price = 19.99;

Analogy

Think of a gas pump.

You can fill:

1.5 liters
10.75 liters
23.99 liters

Not just whole numbers.

3. boolean (True/False)

What is it?

Can only store two values.

boolean isStudent = true;

or

boolean isStudent = false;

Analogy

Think of a light switch.

ON = true
OFF = false

There are only two possible states.

4. char (Character)

What is it?

Stores exactly one character.

char grade = 'A';

Analogy

Think of a mailbox that can hold only one letter.

Valid examples:

'A'
'B'
'7'
'!'

Not valid:

"Ali"

because it contains multiple characters.

5. String (Text)

What is it?

Stores text.

String name = "Ali";

Analogy

Think of a sheet of paper where you can write a word, a sentence, or an entire paragraph.

Examples:

"Ali"
"Hello"
"I enjoy programming"

A String can contain many characters.

Summary

Data Type    Stores                Analogy
int          Whole numbers         Egg carton
double       Decimal numbers       Gas pump
boolean      True/False values     Light switch
char         One character         Mailbox with one letter
String       Text                  Sheet of paper

Short Summary

Data types define what kind of information a variable can store. Different data types are used for numbers, text, characters, and true/false values.
""",
            imagePlacements = listOf(
                InlineImage(
                    afterText = "Think of an egg carton where you count whole eggs.",
                    imageRes = R.drawable.int_aggkartong
                ),
                InlineImage(
                    afterText = "Think of a gas pump.",
                    imageRes = R.drawable.double_bensinpump
                ),
                InlineImage(
                    afterText = "Think of a light switch.",
                    imageRes = R.drawable.boolean_lampknapp
                ),
                InlineImage(
                    afterText = "Think of a mailbox that can hold only one letter.",
                    imageRes = R.drawable.char_brevlada
                ),
                InlineImage(
                    afterText = "Think of a sheet of paper where you can write a word, a sentence, or an entire paragraph.",
                    imageRes = R.drawable.string_papper_text
                )
            )
        )
    )

    private val operatorPages = listOf(
        LearningPage(
            title = "Operators and Expressions",
            content = """
Operators and Expressions

What are Operators and Expressions?

Programs often need to perform calculations, compare values, or make decisions.

To do this, programmers use operators and expressions.

An operator is a symbol that performs an action.

An expression is a combination of values, variables, and operators that produces a result.

What is an Operator?

Operators are symbols that tell the computer what to do.

Examples:

+
-
*
/
==
>
<

Examples:

5 + 3

The + operator tells the computer to add the numbers.

Result:

8

What is an Expression?

An expression is a complete calculation or comparison.

Example:

5 + 3

This entire statement is an expression.

Another example:

age > 18

This is also an expression.

The result will be either:

true

or

false

Analogy

Think of a calculator.

The numbers are the values.

The buttons are the operators.

Example:

5 + 3

5 and 3 are values
+ is the operator
5 + 3 is the expression
8 is the result

Everyday Example

Imagine shopping.

You have:

3 apples
+
2 apples

The operator is:

+

The expression is:

3 + 2

Result:

5 apples

Comparison Operators

Operators can also compare values.

Example:

age > 18

Analogy:

Think of a security guard checking whether someone is old enough to enter a movie theater.

The guard asks:

Is age greater than 18?

Possible answers:

true
false

Common Operators

Addition

5 + 3

Result:

8

Subtraction

10 - 4

Result:

6

Multiplication

5 * 2

Result:

10

Division

20 / 4

Result:

5

Equal To

age == 18

Checks whether the values are equal.

Greater Than

age > 18

Checks whether one value is larger than another.

Less Than

age < 18

Checks whether one value is smaller than another.

Example Using Variables

int apples = 5;
int oranges = 3;

int total = apples + oranges;

Here:

apples and oranges are variables
+ is the operator
apples + oranges is the expression
total becomes 8

Another Analogy

Think of a recipe.

Ingredients:

2 eggs
+
1 egg

The operator combines them.

Expression:

2 + 1

Result:

3 eggs

Operators tell you what action to perform, while expressions represent the entire process.

Summary

An operator is:

A symbol that performs an action
Used for calculations and comparisons

An expression is:

A complete calculation or comparison
Made from values, variables, and operators
Produces a result

Just as calculator buttons help people perform calculations, operators help programs work with data and make decisions.

Short Summary

Operators perform actions such as calculations and comparisons. Expressions combine values, variables, and operators to produce a result.
"""
        )
    )

    private val ifStatementPages = ifAndLoopPages.take(2)

    private val loopsPages = ifAndLoopPages.drop(2)


    private val functionPages = listOf(
        LearningPage(
            title = "Functions",
            content = """
Functions

What is a function?

A function is a reusable block of code that performs a specific task.

Instead of writing the same instructions many times, we can place them inside a function and call it whenever we need it.

Real-world analogy

Think of a coffee machine.

You press a button.

The machine performs several steps internally:
Grinds coffee
Heats water
Brews coffee

You do not need to know every step each time.
You simply use the function.

Java Example

public static void makeCoffee() {
    System.out.println("Coffee is ready");
}

public static void main(String[] args) {

    makeCoffee();
    makeCoffee();

}

The function makeCoffee() can be called whenever coffee is needed.

Short Summary

Functions group instructions into reusable blocks. They help programmers avoid repetition and organize code into smaller tasks.
""",           imagePlacements = listOf(
                InlineImage(
                    afterText = "Think of a coffee machine.",
                    imageRes = R.drawable.function_coffee_machine
                )
            )
        )
    )

    private val basicProblemSolvingPages = listOf(
        LearningPage(
            title = "Basic Problem Solving",
            content = """
Basic Problem Solving

What is it?

Programming is not only about learning variables, loops, and functions. The main goal of programming is to solve problems.

Basic problem solving means learning how to:

Understand a problem
Break it into smaller steps
Create a solution
Use programming concepts to implement that solution

Programmers solve problems every day, from simple calculations to complex applications.

Example Problem

Imagine a teacher wants to calculate the average score of students.

Instead of doing it manually every time, a program can solve the problem.

Steps:

Get the scores
Add them together
Divide by the number of students
Display the average

Analogy

Think of following a recipe when cooking.

If you want to bake a cake, you do not throw everything into the bowl randomly.

You follow steps:

Gather ingredients
Mix them
Put the batter in a pan
Bake the cake

Programming works the same way.

A programmer takes a problem and creates a series of steps that lead to a solution.

Everyday Example

Problem:

You want to know whether you have enough money to buy a game that costs ${'$'}50.

Steps:

Check how much money you have.
Compare it with the price.
Decide whether you can buy it.

Program:

double money = 60;
double price = 50;

if (money >= price) {
    System.out.println("You can buy the game!");
}

The program solves the problem automatically.

Another Analogy

Think of a GPS.

You tell it where you want to go.

The GPS:

Analyzes the destination.
Calculates a route.
Gives step-by-step directions.

Problem solving in programming is similar.

You start with a goal and create a series of logical steps that lead to the solution.

Summary

Basic problem solving is the ability to:

Understand a problem
Break it into smaller steps
Create a plan
Use programming concepts to solve it

Just as a recipe helps a chef make a meal and a GPS helps a driver reach a destination, problem solving helps programmers find solutions to real-world challenges.

Short Summary

Problem solving is the process of understanding a problem, creating a plan, and implementing a solution. Programming concepts work together to solve real-world challenges.
""",            imagePlacements = listOf(
                InlineImage(
                    afterText = "Think of following a recipe when cooking.",
                    imageRes = R.drawable.basic_problem_solving_recipe
                )
            )
        )

    )

    private val appendixPages =
        sortingPages +
                dataStructurePages +
                treePages +
                codeProgramPages +
                oopPages

    val topics = listOf(
        Topic(title = "Variables", pages = variablePages, color = Color(0xFF6E44C6)),
        Topic(title = "Data Types", pages = dataTypePages, color = Color(0xFF7E57C2)),
        Topic(title = "Operators and Expressions", pages = operatorPages, color = Color(0xFF5E35B1)),
        Topic(title = "If Statements", pages = ifStatementPages, color = Color(0xFF512DA8)),
        Topic(title = "Loops", pages = loopsPages, color = Color(0xFF4EA8DE)),
        Topic(title = "Lists", pages = listPages, color = Color(0xFF90DBF4)),
        Topic(title = "Functions", pages = functionPages, color = Color(0xFF7986CB)),
        Topic(title = "Basic Problem Solving", pages = basicProblemSolvingPages, color = Color(0xFF303F9F)),
        Topic(title = "Appendix", pages = appendixPages, color = Color(0xFF555555))
    )
}
