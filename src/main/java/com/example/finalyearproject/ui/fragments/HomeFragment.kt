package com.example.finalyearproject.ui.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalyearproject.R
import com.example.finalyearproject.ui.CalendarActivity
import com.example.finalyearproject.ui.FormHelperActivity
import com.example.finalyearproject.ui.TensorFlowActivity
import kotlinx.android.synthetic.main.fragment_home.*

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        formHelperButton.setOnClickListener {
            val intent = Intent(activity, FormHelperActivity::class.java)
            startActivity(intent)
        }
        predictCaloriesButton.setOnClickListener {
            val intent = Intent(activity, TensorFlowActivity::class.java)
            startActivity(intent)
        }
       calendar_fab.setOnClickListener {
           val intent = Intent(activity, CalendarActivity::class.java)
           startActivity(intent)
            }
        }


    }

