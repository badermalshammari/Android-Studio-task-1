package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    val questions = listOf(
        "Android is an operating system" to true,
        "Kotlin is officially supported for Android development " to true,
        "Kotlin is a database" to false,
        "In Kotlin, `val` is used to declare mutable variables" to false,
        "Kotlin supports both object-oriented and functional programming" to true,
        "The entry point of a Kotlin application is the `main()` function" to true,
        "Kotlin code can run on the Java Virtual Machine (JVM)" to true,
        "`fun` is a keyword used to define functions in Kotlin" to true,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(modifier = Modifier.padding(innerPadding), questions = questions)
                }
            }
        }
    }
}

@Composable
fun GameScreen(modifier: Modifier = Modifier, questions: List<Pair<String, Boolean>>) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var answerResult by remember { mutableStateOf<Boolean?>(null) }
    var score by remember { mutableStateOf(0) }
    val currentQuestion = questions[currentQuestionIndex]
    val isLastQuestion = currentQuestionIndex == questions.lastIndex

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                if (isLastQuestion && answerResult != null) {
                    if (score < 4) Color(0xFFFFCDD2) else Color(0xFFC8E6C9)
                } else {
                    Color.White
                }
            )
            .padding(16.dp)
    ) {
        if (isLastQuestion && answerResult != null) {
            // ✅ Final Result Screen
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val message = if (score < 4) {
                    "Try again! You can do better 💪"
                } else {
                    "Great job! 🚀"
                }

                Text(
                    text = message,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "🎉 Your score: $score / ${questions.size}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    currentQuestionIndex = 0
                    answerResult = null
                    score = 0
                }) {
                    Text("Restart Game")
                }
            }
        } else {
            // ✅ Quiz Screen
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Question(name = currentQuestion.first)

                if (answerResult != null) {
                    val resultText = if (answerResult == true) "Correct Answer" else "Wrong Answer"
                    val resultColor = if (answerResult == true) Color.Green else Color.Red
                    CircleAnswer(resultText, resultColor)
                }

                if (answerResult == null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                val isCorrect = currentQuestion.second == true
                                answerResult = isCorrect
                                if (isCorrect) score++
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("True")
                        }

                        Button(
                            onClick = {
                                val isCorrect = currentQuestion.second == false
                                answerResult = isCorrect
                                if (isCorrect) score++
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("False")
                        }
                    }
                }

                if (answerResult != null && !isLastQuestion) {
                    Button(onClick = {
                        currentQuestionIndex++
                        answerResult = null
                    }) {
                        Text("Next Question")
                    }
                }
            }
        }
    }
}

@Composable
fun Question(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier,
        fontSize = 35.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 45.sp
    )
}

@Composable
fun CircleAnswer(text: String, color: Color) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.Black, fontSize = 20.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        GameScreen(questions = listOf())
    }
}