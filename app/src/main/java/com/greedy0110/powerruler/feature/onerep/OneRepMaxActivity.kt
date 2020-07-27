package com.greedy0110.powerruler.feature.onerep

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.greedy0110.powerruler.databinding.ActivityOneRepMaxBinding
import com.greedy0110.powerruler.databinding.ItemOnerepWorkoutBinding
import com.greedy0110.powerruler.feature.onerep.update.UpdateDialogFragment
import com.greedy0110.powerruler.feature.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OneRepMaxActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOneRepMaxBinding.inflate(layoutInflater) }
    private val viewModel: OneRepMaxViewModel by viewModels()
    private val adapter by lazy { Adapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.signal.observe(this) {
            when (it) {
                is OneRepMaxSignal.GoSettingSignal -> {
                    startActivityForResult(
                        Intent(this, SettingsActivity::class.java),
                        REQUEST_CODE
                    )
                }
            }
        }

        initList()
        initAd()
    }

    private fun initList() {
        binding.listWorkout.adapter = adapter
        binding.listWorkout.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        viewModel.items.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun initAd() {
        binding.adView.loadAd(AdRequest.Builder().build())
    }

    override fun onResume() {
        super.onResume()
        binding.adView.resume()
    }

    override fun onPause() {
        binding.adView.pause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.adView.destroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) viewModel.refresh()
            }
        }
    }

    //region RecyclerView.

    private inner class Adapter : ListAdapter<OneRepMaxViewModel.ItemHolder, ViewHolder>(
        object : DiffUtil.ItemCallback<OneRepMaxViewModel.ItemHolder>() {
            override fun areItemsTheSame(
                oldItem: OneRepMaxViewModel.ItemHolder,
                newItem: OneRepMaxViewModel.ItemHolder
            ): Boolean = oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: OneRepMaxViewModel.ItemHolder,
                newItem: OneRepMaxViewModel.ItemHolder
            ): Boolean = oldItem == newItem
        }
    ) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ItemOnerepWorkoutBinding.inflate(layoutInflater, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(getItem(position))
        }
    }

    private inner class ViewHolder(
        private val binding: ItemOnerepWorkoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: OneRepMaxViewModel.ItemHolder) {
            binding.itemHolder = item

            binding.root.setOnClickListener {
                UpdateDialogFragment.Builder(item.workout)
                    .setOnConfirmButton {
                        viewModel.refresh()
                        Timber.d("${item.workout} 클릭")
                    }
                    .show(supportFragmentManager)
            }
        }
    }

    //endregion

    companion object {
        private const val REQUEST_CODE = 1010
    }
}
