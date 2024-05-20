package com.example.flagquiz.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flagquiz.R
import com.example.flagquiz.databinding.FragmentResultBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class FragmentResult : Fragment() {

    lateinit var fragmentResultBinding: FragmentResultBinding
    private var correctNumber: Float = 0f
    private var emptyNumber: Float = 0f
    private var wrongNumber: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentResultBinding = FragmentResultBinding.inflate(inflater,container,false)

        correctNumber = arguments?.getInt("correct")?.toFloat() ?: 0f
        emptyNumber = arguments?.getInt("empty")?.toFloat() ?: 0f
        wrongNumber = arguments?.getInt("wrong")?.toFloat() ?: 0f

        val barEntriesArrayListCorrect = ArrayList<BarEntry>()
        val barEntriesArrayListEmpty = ArrayList<BarEntry>()
        val barEntriesArrayListWrong = ArrayList<BarEntry>()

        barEntriesArrayListCorrect.add(BarEntry(0F,correctNumber))
        barEntriesArrayListEmpty.add(BarEntry(1F,emptyNumber))
        barEntriesArrayListWrong.add(BarEntry(2F,wrongNumber))

        val barDataSetCorrect = BarDataSet(barEntriesArrayListCorrect,"Correct Number").apply {
            color = Color.GREEN
            valueTextSize = 24F
            setValueTextColors(arrayListOf(Color.BLACK))
        }
        val barDataSetEmpty = BarDataSet(barEntriesArrayListEmpty,"Empty Number").apply {
            color = Color.BLUE
            valueTextSize = 24F
            setValueTextColors(arrayListOf(Color.BLACK))
        }
        val barDataSetWrong = BarDataSet(barEntriesArrayListWrong,"Wrong Number").apply {
            color = Color.RED
            valueTextSize = 24F
            setValueTextColors(arrayListOf(Color.BLACK))
        }

        val barData = BarData(barDataSetCorrect,barDataSetEmpty,barDataSetWrong)
        fragmentResultBinding.resultChart.data = barData

        fragmentResultBinding.buttonNewQuiz.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frameLayout,FragmentHome()).commit()
        }
        fragmentResultBinding.buttonExit.setOnClickListener {
            requireActivity().finish()
        }
        return fragmentResultBinding.root
    }

}