package com.greedy0110.powerruler.feature.onerep

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.greedy0110.powerruler.R
import com.greedy0110.powerruler.databinding.ActivityOneRepMaxBinding
import com.greedy0110.powerruler.databinding.ItemOnerepWorkoutBinding
import com.greedy0110.powerruler.databinding.NativeAdTemplateBinding
import com.greedy0110.powerruler.feature.onerep.update.UpdateDialogFragment
import com.greedy0110.powerruler.feature.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint

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

    private var currentNativeAd: UnifiedNativeAd? = null

    /**
     * Populates a [UnifiedNativeAdView] object with data from a given
     * [UnifiedNativeAd].
     *
     * @param nativeAd the object containing the native_ad_template's assets
     * @param adView the view to be populated
     */
    private fun populateUnifiedNativeAdView(
        nativeAd: UnifiedNativeAd,
        adViewBinding: NativeAdTemplateBinding
    ) {
        // You must call destroy on old ads when you are done with them,
        // otherwise you will have a memory leak.
        currentNativeAd?.destroy()
        currentNativeAd = nativeAd

        val adView = adViewBinding.adView
        // Set the media view.
        adView.mediaView = adViewBinding.adMedia

        // Set other native_ad_template assets.
        adView.headlineView = adViewBinding.adHeadline
        adView.bodyView = adViewBinding.adBody
        adView.callToActionView = adViewBinding.adCallToAction
        adView.iconView = adViewBinding.adAppIcon
        adView.priceView = adViewBinding.adPrice
        adView.starRatingView = adViewBinding.adStars
        adView.storeView = adViewBinding.adStore
        adView.advertiserView = adViewBinding.adAdvertiser

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        adViewBinding.adHeadline.text = nativeAd.headline
        adViewBinding.adMedia.setMediaContent(nativeAd.mediaContent)

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable
            )
            adView.iconView.visibility = View.VISIBLE
        }

        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native native_ad_template view with this native native_ad_template.
        adView.setNativeAd(nativeAd)
    }

    /**
     * Creates a request for a new native native_ad_template based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private fun refreshAd() {
        val builder = AdLoader.Builder(this, getString(R.string.admob_main_native_id))

        builder.forUnifiedNativeAd { unifiedNativeAd ->
            val adBinding = NativeAdTemplateBinding.inflate(layoutInflater)

            populateUnifiedNativeAdView(unifiedNativeAd, adBinding)
            binding.containerNativeAd.removeAllViews()
            binding.containerNativeAd.addView(adBinding.adView)

            adBinding.backgroundAd.setOnClickListener {
                currentNativeAd?.destroy()
                binding.containerNativeAd.removeAllViews()
            }
        }

        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()

        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()

        builder.withNativeAdOptions(adOptions)

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                Toast.makeText(
                    this@OneRepMaxActivity,
                    "Failed to load native native_ad_template: " + errorCode,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())
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

                        if (viewModel.needShowAd()) {
                            refreshAd()
                            viewModel.showAd()
                        }
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
