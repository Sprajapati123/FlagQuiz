package com.example.flagquiz.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.flagquiz.R
import com.example.flagquiz.databinding.FragmentHomeBinding
import com.techmania.flagquizwithsqlitedemo.DatabaseCopyHelper


class FragmentHome : Fragment() {

    lateinit var fragmentHomeBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater,container,false)



        createAndOpenaDatabase()


        fragmentHomeBinding.btnStart.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frameLayout,FragmentQuiz()).commit()
//            findNavController().navigate(R.id.action_fragmentHome_to_fragmentQuiz2)
        }
        return fragmentHomeBinding.root
    }

    private fun createAndOpenaDatabase(){
        try {
            val helper = DatabaseCopyHelper(requireActivity())
            helper.createDataBase()
            helper.openDataBase()
            Log.d("message","Database created")
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}