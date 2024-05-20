package com.example.flagquiz.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.flagquiz.R
import com.example.flagquiz.database.FlagsDao
import com.example.flagquiz.databinding.FragmentQuizBinding
import com.example.flagquiz.models.FlagsModel
import com.techmania.flagquizwithsqlitedemo.DatabaseCopyHelper

class FragmentQuiz : Fragment() {

    private lateinit var fragmentQuizBinding: FragmentQuizBinding
    private var flagList = ArrayList<FlagsModel>()
    private var wrongFlags = ArrayList<FlagsModel>()

    private lateinit var correctFlag: FlagsModel
    private var correctNumber = 0
    private var wrongNumber = 0
    private var emptyNumber = 0
    private var questionNumber = 0
    private val dao = FlagsDao()
    var optionControl = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentQuizBinding = FragmentQuizBinding.inflate(inflater, container, false)

        try {
            val dbHelper = DatabaseCopyHelper(requireActivity())
            dbHelper.createDataBase()
            flagList = dao.getRandomTenRecords(dbHelper)

            for (flag in flagList) {
                Log.d("flags", flag.id.toString())
                Log.d("flags", flag.countryName)
                Log.d("flags", flag.flagName)
                Log.d("flags", "****************")
            }

            showData()
        } catch (e: Exception) {
            Log.e("FragmentQuiz", "Error loading flags", e)
        }

        fragmentQuizBinding.buttonA.setOnClickListener {
            answerControl(fragmentQuizBinding.buttonA)
        }
        fragmentQuizBinding.buttonB.setOnClickListener {
            answerControl(fragmentQuizBinding.buttonB)
        }
        fragmentQuizBinding.buttonC.setOnClickListener {
            answerControl(fragmentQuizBinding.buttonC)
        }
        fragmentQuizBinding.buttonD.setOnClickListener {
            answerControl(fragmentQuizBinding.buttonD)
        }

        fragmentQuizBinding.buttonNext.setOnClickListener {
            questionNumber++

            if(questionNumber >8){
                if(!optionControl){
                    emptyNumber++
                }
                Toast.makeText(requireActivity(),"The quiz has finished",Toast.LENGTH_LONG).show()


                var bundle = Bundle()
                val fragmentResult = FragmentResult()
                bundle.putInt("correct",correctNumber)
                bundle.putInt("wrong",wrongNumber)
                bundle.putInt("empty",emptyNumber)

                fragmentResult.arguments = bundle
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frameLayout,fragmentResult).commit()

            }else{
                showData()
                if(!optionControl){
                    emptyNumber++
                    fragmentQuizBinding.textViewEmpty.text = emptyNumber.toString()
                }else{
                    setButtonToInitialProperties()
                }
            }
            optionControl = false
        }
        return fragmentQuizBinding.root
    }

    private fun showData() {
        if (questionNumber >= flagList.size) {
            // Handle end of quiz
            return
        }

        fragmentQuizBinding.textViewQuestion.text = "Question: ${questionNumber +1}"

        correctFlag = flagList[questionNumber]
        fragmentQuizBinding.imageViewFlag.setImageResource(
            resources.getIdentifier(correctFlag.flagName, "drawable", requireActivity().packageName)
        )
        wrongFlags = dao.getRandomThreeRecords(DatabaseCopyHelper(requireActivity()), correctFlag.id)

        val mixOptions = HashSet<FlagsModel>()
        mixOptions.add(correctFlag)
        mixOptions.addAll(wrongFlags)

        if (mixOptions.size < 4) {
            Log.e("FragmentQuiz", "Not enough options to show")
            return
        }

        val options = ArrayList<FlagsModel>()
        options.addAll(mixOptions)

        Log.d("FragmentQuiz", "Options: ${correctFlag.flagName}}")


        fragmentQuizBinding.buttonA.text = options[0].countryName
        fragmentQuizBinding.buttonB.text = options[1].countryName
        fragmentQuizBinding.buttonC.text = options[2].countryName
        fragmentQuizBinding.buttonD.text = options[3].countryName
    }

    private fun answerControl(button: Button){
        val clickedOption = button.text.toString()

        val correctAnswer =  correctFlag.countryName

        if(clickedOption == correctAnswer){
            correctNumber++
            fragmentQuizBinding.textViewCorrect.text = correctNumber.toString()
            button.setBackgroundColor(Color.GREEN)
        }else{
            wrongNumber++
            fragmentQuizBinding.textViewWrong.text = wrongNumber.toString()
            button.setBackgroundColor(Color.RED)
            button.setTextColor(Color.WHITE)

            when(correctAnswer){
                fragmentQuizBinding.buttonA.text -> fragmentQuizBinding.buttonA.setBackgroundColor(Color.GREEN)
                fragmentQuizBinding.buttonB.text -> fragmentQuizBinding.buttonB.setBackgroundColor(Color.GREEN)
                fragmentQuizBinding.buttonC.text -> fragmentQuizBinding.buttonC.setBackgroundColor(Color.GREEN)
                fragmentQuizBinding.buttonD.text -> fragmentQuizBinding.buttonD.setBackgroundColor(Color.GREEN)
            }
        }

        fragmentQuizBinding.buttonA.isClickable = false
        fragmentQuizBinding.buttonB.isClickable = false
        fragmentQuizBinding.buttonC.isClickable = false
        fragmentQuizBinding.buttonD.isClickable = false

        optionControl = true
    }

    private fun setButtonToInitialProperties(){
        fragmentQuizBinding.buttonA.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(resources.getColor(R.color.pink,requireActivity().theme))
            isClickable = true
        }
        fragmentQuizBinding.buttonB.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(resources.getColor(R.color.pink,requireActivity().theme))
            isClickable = true
        }
        fragmentQuizBinding.buttonC.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(resources.getColor(R.color.pink,requireActivity().theme))
            isClickable = true
        }
        fragmentQuizBinding.buttonD.apply {
            setBackgroundColor(Color.WHITE)
            setTextColor(resources.getColor(R.color.pink,requireActivity().theme))
            isClickable = true
        }
    }
}
