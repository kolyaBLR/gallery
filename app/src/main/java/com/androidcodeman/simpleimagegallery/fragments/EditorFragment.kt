package com.androidcodeman.simpleimagegallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidcodeman.simpleimagegallery.R
import com.androidcodeman.simpleimagegallery.recycler.Data
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_image.*
import java.io.File

class EditorFragment : Fragment() {

    var data: Data? = null
        set(value) {
            field = value
            bindView()
        }

    var position = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)
        val size = activity?.window?.decorView?.width ?: 0
        view?.layoutParams = view?.layoutParams?.apply {
            width = size
            height = size
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveButton.setOnClickListener { (activity as? Listener)?.onSaveCLick() }
        reselectButton.setOnClickListener { (activity as? Listener)?.onReselectedCLick() }
        bindView()
    }

    private fun bindView() {
        val view = view ?: return
        val data = data ?: return

        if (data.imageUrl != null && data.imageUrl != "") {
            image.visibility = View.VISIBLE
            Picasso.get().load(File(data.imageUrl)).into(image)
        } else {
            image.visibility = View.INVISIBLE
        }
        if (data.isEditor) {
            saveButton.visibility = if (data.imageUrl != null) View.VISIBLE else View.GONE
            reselectButton.visibility = View.VISIBLE
            reselectButton.setText(if (data.imageUrl != null) R.string.reselect else R.string.select)
        } else {
            saveButton.visibility = View.INVISIBLE
            reselectButton.visibility = View.INVISIBLE
        }
    }

    interface Listener {
        fun onSaveCLick()
        fun onReselectedCLick()
    }
}
