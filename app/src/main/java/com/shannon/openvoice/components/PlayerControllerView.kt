package com.shannon.openvoice.components

import android.content.ComponentName
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.shannon.android.lib.extended.singleClick
import com.shannon.android.lib.player.media.MediaController
import com.shannon.android.lib.player.media.MediaData
import com.shannon.android.lib.player.media.MusicService
import com.shannon.android.lib.player.media.MusicServiceConnection
import com.shannon.openvoice.R
import com.shannon.openvoice.databinding.LayoutPlayerControllerBinding
import com.shannon.openvoice.dialog.SpeedChooseDialog
import com.shannon.openvoice.util.PreferencesUtil
import timber.log.Timber
import java.util.*

/**
 *
 * @ProjectName:    OpenVoice
 * @Package:        com.shannon.openvoice.components
 * @ClassName:      PlayerControllerView
 * @Description:     播放器控制
 * @Author:         czhen
 */
class PlayerControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr), SeekBar.OnSeekBarChangeListener {
    private val formatBuilder: StringBuilder
    private val formatter: Formatter
    private val viewBinding: LayoutPlayerControllerBinding

    val serviceConnection: MusicServiceConnection

    var isConnected: MutableLiveData<Boolean>
        private set

    val scrollableLive = MutableLiveData<String>()

    private var currentSpeed = 1.0f
    private var currentPlayMode = MediaController.PLAY_MODE_ORDER

    init {
        viewBinding =
            LayoutPlayerControllerBinding.inflate(LayoutInflater.from(context), this, false)

        addView(viewBinding.root)

        formatBuilder = StringBuilder()
        formatter = Formatter(formatBuilder, Locale.getDefault())

        serviceConnection =
            MusicServiceConnection(context, ComponentName(context, MusicService::class.java))

        isConnected = serviceConnection.isConnected
        setPlaybackParams()
        viewBinding.apply {
            viewPlayerControllerPrevious.singleClick { serviceConnection.seekToPreviousItem() }
            viewPlayerControllerPlay.singleClick { serviceConnection.play() }
            viewPlayerControllerPause.singleClick { serviceConnection.pause() }
            viewPlayerControllerNext.singleClick { serviceConnection.seekToNextItem() }
            viewPlayerControllerMode.singleClick {
                if (currentPlayMode == MediaController.PLAY_MODE_ORDER) {
                    currentPlayMode = MediaController.PLAY_MODE_SHUFFLE
                    Toast.makeText(context, R.string.switched_shuffle, Toast.LENGTH_SHORT).show()
                    viewPlayerControllerMode.setImageResource(R.drawable.icon_play_mode_shuffle)
                } else {
                    currentPlayMode = MediaController.PLAY_MODE_ORDER
                    Toast.makeText(context, R.string.switched_sequential, Toast.LENGTH_SHORT).show()
                    viewPlayerControllerMode.setImageResource(R.drawable.icon_play_mode_order)
                }
                serviceConnection.switchPlayMode(currentPlayMode)
                PreferencesUtil.putInt(PreferencesUtil.Constant.KEY_PLAY_MODE, currentPlayMode)
            }
            viewPlayerSeekBar.setOnSeekBarChangeListener(this@PlayerControllerView)
            setSpeedText()
            viewPlayerControllerSpeed.singleClick {
                SpeedChooseDialog(context, currentSpeed) {
                    currentSpeed = it
                    serviceConnection.setPlaybackSpeed(currentSpeed)
                    PreferencesUtil.putFloat(PreferencesUtil.Constant.KEY_SPEED, currentSpeed)
                    setSpeedText()
                }.show()
            }
            viewPlayerControllerLocation.singleClick {
                val isPlaying = serviceConnection.isPlaying.value ?: false
                val mediaId = serviceConnection.nowPlaying.value ?: ""
                if (isPlaying && mediaId.isNotEmpty()) {
                    scrollableLive.postValue(mediaId)
                }
            }
        }
    }

    fun connectMusicService() {
        serviceConnection.connect()
    }

    fun recover(targetPosition: Int, positionMs: Long) {
        serviceConnection.recover(targetPosition, positionMs)
        onIsPlayingChanged(serviceConnection.isPlaying.value ?: false)
        setPlaybackParams()
    }

    private fun setPlaybackParams() {
        currentPlayMode = PreferencesUtil.getInt(
            PreferencesUtil.Constant.KEY_PLAY_MODE,
            MediaController.PLAY_MODE_ORDER
        )
        if (currentPlayMode == MediaController.PLAY_MODE_SHUFFLE) {
            viewBinding.viewPlayerControllerMode.setImageResource(R.drawable.icon_play_mode_shuffle)
        } else {
            viewBinding.viewPlayerControllerMode.setImageResource(R.drawable.icon_play_mode_order)
        }
        currentSpeed = PreferencesUtil.getFloat(PreferencesUtil.Constant.KEY_SPEED, 1.0f)
        setSpeedText()
    }

    fun observeEvents(viewLifecycleOwner: LifecycleOwner) {
        serviceConnection.isConnected.observe(viewLifecycleOwner, this::onConnected)
        serviceConnection.nowPlaying.observe(viewLifecycleOwner, this::nowPlaying)
        serviceConnection.isSkipToPreviousEnabled.observe(
            viewLifecycleOwner,
            this::isSkipToPreviousEnabled
        )
        serviceConnection.isSkipToNextEnabled.observe(viewLifecycleOwner, this::isSkipToNextEnabled)
        serviceConnection.isPlaying.observe(viewLifecycleOwner, this::onIsPlayingChanged)
        serviceConnection.onIsLoading.observe(viewLifecycleOwner, this::onIsLoadingChanged)
        serviceConnection.mediaDuration.observe(viewLifecycleOwner, this::onMediaDuration)
        serviceConnection.mediaProgress.observe(viewLifecycleOwner, this::onMediaProgress)
        serviceConnection.playlistUnselected.observe(viewLifecycleOwner) {
            if (it) {
                serviceConnection.isPlaying.postValue(false)
            }
        }
    }

    private fun onConnected(isConnected: Boolean) {
        if (isConnected) {
            Timber.e("onConnected: currentPlayMode = $currentPlayMode")
            serviceConnection.switchPlayMode(currentPlayMode)
            if (currentSpeed != 1.0f) {
                serviceConnection.setPlaybackSpeed(currentSpeed)
            }
        }
        serviceConnection.isConnected.removeObserver(this::onConnected)
    }

    private fun nowPlaying(mediaId: String) {
    }

    private fun isSkipToPreviousEnabled(hasPreviousItem: Boolean) {
//        viewBinding.viewPlayerControllerPrevious.visibility =
//            if (hasPreviousItem) View.VISIBLE else View.INVISIBLE
        viewBinding.viewPlayerControllerPrevious.isEnabled = hasPreviousItem
        viewBinding.viewPlayerControllerPrevious.alpha = if (hasPreviousItem) 1.0f else 0.5f
    }

    private fun isSkipToNextEnabled(hasNextItem: Boolean) {
//        viewBinding.viewPlayerControllerNext.visibility =
//            if (hasNextItem) View.VISIBLE else View.INVISIBLE
        viewBinding.viewPlayerControllerNext.isEnabled = hasNextItem
        viewBinding.viewPlayerControllerNext.alpha = if (hasNextItem) 1.0f else 0.5f
    }

    private fun onIsPlayingChanged(isPlaying: Boolean) {
        if (isPlaying) {
            viewBinding.viewPlayerControllerPause.visibility = View.VISIBLE
            viewBinding.viewPlayerControllerPlay.visibility = View.INVISIBLE
        } else {
            viewBinding.viewPlayerControllerPause.visibility = View.INVISIBLE
            viewBinding.viewPlayerControllerPlay.visibility = View.VISIBLE
        }
    }

    private fun onIsLoadingChanged(isLoading: Boolean) {
        viewBinding.viewPlayerControllerPlay.isEnabled = !isLoading
    }

    private fun onMediaDuration(duration: Int) {
        viewBinding.viewPlayerSeekBar.max = duration
        viewBinding.viewPlayerTotalTime.text = getStringForTime(duration.toLong())
    }

    private fun onMediaProgress(positionMs: Int) {
        viewBinding.viewPlayerCurrentTime.text = getStringForTime(positionMs.toLong())
        if (viewBinding.viewPlayerSeekBar.isDragging) return
        viewBinding.viewPlayerSeekBar.progress = positionMs
    }

    fun setMediaSource(sources: List<MediaData>) {
        serviceConnection.setMediaSource(sources)
    }

    fun addMediaSource(sources: List<MediaData>) {
        serviceConnection.addMediaSource(sources)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        serviceConnection.seekTo(seekBar.progress.toLong())
    }

    fun getCurrentProgress(): Int {
        return viewBinding.viewPlayerSeekBar.progress
    }

    private fun getStringForTime(timeMs: Long): String {
        var timeMsVar = timeMs
        val prefix = if (timeMsVar < 0) "-" else ""
        timeMsVar = Math.abs(timeMsVar)
        val totalSeconds = (timeMsVar + 500) / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        formatBuilder.setLength(0)
        return if (hours > 0) formatter.format("%s%d:%02d:%02d", prefix, hours, minutes, seconds)
            .toString() else formatter.format("%s%02d:%02d", prefix, minutes, seconds).toString()
    }

    private fun setSpeedText() {
        val text = when (currentSpeed) {
            0.5f -> "0.5x"
            0.75f -> "0.75x"
            1.0f -> context.getString(R.string.speed)
            1.25f -> "1.25x"
            1.5f -> "1.5x"
            2.0f -> "2.0x"
            else -> context.getString(R.string.speed)
        }

        viewBinding.viewPlayerControllerSpeed.text = text
    }

    companion object {
        private const val TAG = "PlayerControllerView"
    }
}