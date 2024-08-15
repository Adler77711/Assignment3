package com.example.birthdayapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class BirthdayFragment : Fragment() {

    private var message: String? = null

    companion object {
        private const val ARG_MESSAGE = "message"

        fun newInstance(message: String): BirthdayFragment {
            val fragment = BirthdayFragment()
            val args = Bundle()
            args.putString(ARG_MESSAGE, message)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            message = it.getString(ARG_MESSAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_birthday, container, false)
        val textView: TextView = view.findViewById(R.id.messageTextView)
        textView.text = message
        return view
    }
}
