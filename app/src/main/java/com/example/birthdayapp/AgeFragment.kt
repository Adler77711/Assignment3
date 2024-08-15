package com.example.birthdayapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class AgeFragment : Fragment() {

    private var ageMessage: String? = null

    companion object {
        private const val ARG_AGE_MESSAGE = "ageMessage"

        fun newInstance(ageMessage: String): AgeFragment {
            val fragment = AgeFragment()
            val args = Bundle()
            args.putString(ARG_AGE_MESSAGE, ageMessage)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ageMessage = it.getString(ARG_AGE_MESSAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_age, container, false)
        val textView: TextView = view.findViewById(R.id.messageTextView2)
        textView.text = ageMessage
        return view
    }
}
