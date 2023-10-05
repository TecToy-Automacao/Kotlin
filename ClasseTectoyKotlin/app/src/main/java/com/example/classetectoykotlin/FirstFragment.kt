package com.example.classetectoykotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.classetectoykotlin.databinding.FragmentSecondBinding


class FirstFragment : Fragment(){

   lateinit var binding: FragmentSecondBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSecond.setOnClickListener(View.OnClickListener {
            NavHostFragment.findNavController(this@FirstFragment)
                .navigate(R.id.action_SecondFragment_to_FirstFragment)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding == null
    }
}