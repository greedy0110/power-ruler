package com.greedy0110.powerruler.feature.ad

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.ads.AdRequest
import com.greedy0110.powerruler.databinding.DialogAdBinding

class AdDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val binding = DialogAdBinding.inflate(layoutInflater, null, false)

            //TODO: 미리 로드하도록 구현해야 한다.
            binding.nativeAd.loadAd(AdRequest.Builder().build())

            return AlertDialog.Builder(it)
                .setView(binding.root)
                .create()
        } ?: throw IllegalStateException("${AdDialogFragment::class.simpleName} creation error")
    }

    class Builder {

        fun show(
            fm: FragmentManager,
            tag: String = AdDialogFragment::class.simpleName ?: "power"
        ) {
            val newFragment = AdDialogFragment()
            newFragment.show(fm, tag)
        }
    }
}