package com.example.numbersgameapp


import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var mainActivityLayout: ConstraintLayout
    lateinit var guessButton: Button
    lateinit var playAgainButton: Button
    lateinit var guessEditText: EditText
    lateinit var userGuessTextView: TextView
    lateinit var userAttemptsTextView: TextView
    lateinit var messageArrayList: ArrayList<String>

    var randomNumber: Int = 0
    var userRemainingAttempts = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityLayout = findViewById(R.id.mainActivityLayout_cl)
        guessButton = findViewById(R.id.guess_button)
        playAgainButton = findViewById(R.id.playAgain_button)
        guessEditText = findViewById(R.id.guess_editText)
        userGuessTextView = findViewById(R.id.userGuess_textview)

        randomNumber = Random.nextInt(11)
        messageArrayList = ArrayList()


        playAgainButton.isVisible = false


        messages_recyclerView.adapter = MessageAdapter(messageArrayList)
        messages_recyclerView.layoutManager = LinearLayoutManager(this)

        guessButton.setOnClickListener {
            val userGuess = guessEditText.text.toString()
            if(validateInput(userGuess)){
                guessEditText.text.clear()
                userGuessTextView.text=userGuess
                if(checkUserGuess(userGuess)){
                    messageArrayList.add("Correct!")
                    messages_recyclerView.adapter?.notifyItemInserted(messageArrayList.size)
                    showDialog("You Won!", "your guess was correct")
                    guessButton.isVisible = false
                    guessEditText.isVisible = false
                    playAgainButton.isVisible = true
                }else{
                    userRemainingAttempts--
                    messageArrayList.add("your guess was $userGuess")
                    messages_recyclerView.adapter?.notifyItemInserted(messageArrayList.size)
                    messageArrayList.add("$userRemainingAttempts guesses left")
                    messages_recyclerView.adapter?.notifyItemInserted(messageArrayList.size)
                }
                if(userRemainingAttempts==0){
                    messageArrayList.add("you lost :(")
                    messages_recyclerView.adapter?.notifyItemInserted(messageArrayList.size)
                    messageArrayList.add("the correct answer is $randomNumber")
                    messages_recyclerView.adapter?.notifyItemInserted(messageArrayList.size)
                    showDialog("Game Over", "You lost :( the correct answer was $randomNumber")
                    guessButton.isVisible = false
                    guessEditText.isVisible = false
                    playAgainButton.isVisible = true
                }
            }else{
                Snackbar.make(mainActivityLayout, "only numbers are allowed", Snackbar.LENGTH_SHORT).show()
            }
        }

        playAgainButton.setOnClickListener {
            this.recreate()
        }


    }



    private fun checkUserGuess(number: String): Boolean {
        return number.toInt() == 5
    }


    private fun validateInput(number: String): Boolean {
        return try {
            var integer = number.toInt()
            true
        } catch (e: Exception) {
            false
        }

    }

    private fun showDialog(title: String,message: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("$message\nDo you want to play again?")
            .setCancelable(false)
            .setPositiveButton("Yes", DialogInterface.OnClickListener {
                    dialog, id -> this.recreate()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }
}


