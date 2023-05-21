package com.shannon.android.lib.player.media

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.MutableLiveData

/**
 *
 * @ClassName:      MusicServiceConnection
 * @Description:     java类作用描述
 * @Author:         czhen
 */
class MusicServiceConnection(
    private val context: Context,
    private val componentName: ComponentName
) : MediaController, MediaPlayerListener {

    val isConnected = MutableLiveData<Boolean>().apply { postValue(false) }

    //正在播放，mediaId
    val nowPlaying = MutableLiveData<String>()

    //是否有上一首
    val isSkipToPreviousEnabled = MutableLiveData<Boolean>().apply { postValue(false) }

    //是否有下一首
    val isSkipToNextEnabled = MutableLiveData<Boolean>().apply { postValue(false) }

    // 播放状态，true:正在播放；false:暂停|停止
    val isPlaying = MutableLiveData<Boolean>().apply { postValue(false) }

    //是否在缓冲，只在未开始播放前通知一次
    val onIsLoading = MutableLiveData<Boolean>().apply { postValue(false) }

    //媒体时长
    val mediaDuration = MutableLiveData<Int>()

    //当前播放进度
    val mediaProgress = MutableLiveData<Int>()

    val playlistUnselected = MutableLiveData<Boolean>()

    @Volatile
    private var mState = CONNECT_STATE_DISCONNECTED

    private lateinit var mediaController: MediaController

    private var mServiceConnection: InnerServiceConnection? = null

    fun connect() {
        if (mState != CONNECT_STATE_DISCONNECTING && mState != CONNECT_STATE_DISCONNECTED) {
            Log.e(TAG, "connect() called while neither disconnecting nor disconnected ")
            return
        }
        setState(CONNECT_STATE_CONNECTING)

        mServiceConnection = InnerServiceConnection()
        val intent = Intent().apply { component = componentName }

        var bound = false
        try {
            bound = context.bindService(intent, mServiceConnection!!, Context.BIND_AUTO_CREATE)
        } catch (ex: Exception) {
            Log.e(TAG, "Failed binding to service $mServiceConnection")
        }
        if (!bound) {
            forceCloseConnection()
        }
    }

    fun disconnect() {
        setState(CONNECT_STATE_DISCONNECTING)
        forceCloseConnection()
    }

    private fun setState(state: Int) {
        mState = state
    }

    private inner class InnerServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val serviceBinder = binder as MusicService.ServiceBinder
            mediaController = serviceBinder.getMediaController()
            mediaController.setMediaPlayerListener(this@MusicServiceConnection)
            setState(CONNECT_STATE_CONNECTED)
            isConnected.postValue(true)
            Log.d(TAG, "onServiceConnected: CONNECT_STATE_CONNECTED")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            setState(CONNECT_STATE_DISCONNECTED)
            isConnected.postValue(false)
        }
    }

    private fun forceCloseConnection() {
        if (mServiceConnection != null) {
            try {
                context.unbindService(mServiceConnection!!)
            } catch (e: IllegalArgumentException) {
                Log.d(TAG, "unbindService failed", e)
            }
        }
        setState(CONNECT_STATE_DISCONNECTED)
        mServiceConnection = null
    }

    private fun isConnected() = mState == CONNECT_STATE_CONNECTED

    fun recover(targetPosition: Int, positionMs: Long) {
        if (isConnected()) {
            mediaController.setMediaPlayerListener(this@MusicServiceConnection)
            if (targetPosition == -1) return
            mediaController.seekTo(targetPosition, positionMs)
        }
    }

    override fun play() {
        if (isConnected()) mediaController.play()
    }

    override fun play(targetPosition: Int) {
        if (isConnected()) mediaController.play(targetPosition)
    }

    override fun play(targetPosition: Int, positionMs: Long) {
        if (isConnected()) mediaController.play(targetPosition, positionMs)
    }

    override fun seekTo(positionMs: Long) {
        if (isConnected()) mediaController.seekTo(positionMs)
    }

    override fun seekTo(targetPosition: Int, positionMs: Long) {
        if (isConnected()) mediaController.seekTo(targetPosition, positionMs)
    }

    override fun pause() {
        if (isConnected()) mediaController.pause()
    }

    override fun stop() {
        if (isConnected()) mediaController.stop()
    }

    override fun release() {
        if (isConnected()) mediaController.release()
    }

    override fun seekToPreviousItem() {
        if (isConnected()) mediaController.seekToPreviousItem()
    }

    override fun seekToNextItem() {
        if (isConnected()) mediaController.seekToNextItem()
    }

    override fun setMediaSource(sources: List<MediaData>) {
        if (isConnected()) mediaController.setMediaSource(sources)
    }

    override fun addMediaSource(sources: List<MediaData>) {
        if (isConnected()) mediaController.addMediaSource(sources)
    }

    override fun setMediaPlayerListener(mediaPlayerListener: MediaPlayerListener) {
    }

    override fun getCurrentMediaItem(): String {
        return if (isConnected()) mediaController.getCurrentMediaItem() else ""
    }

    override fun switchPlayMode(@MediaController.PlayMode model: Int): Int {
        return if (isConnected()) mediaController.switchPlayMode(model) else MediaController.PLAY_MODE_ORDER
    }

    override fun setPlaybackSpeed(speed: Float) {
        if (isConnected()) mediaController.setPlaybackSpeed(speed)
    }

    override fun getTargetPosition(mediaId: String): Int {
        return if (isConnected()) mediaController.getTargetPosition(mediaId) else -1
    }

    override fun isMediaPlaying(): Boolean {
        return if (isConnected()) mediaController.isMediaPlaying() else false
    }

    companion object {
        private const val TAG = "MusicServiceConnection"
        const val CONNECT_STATE_DISCONNECTING = 0
        const val CONNECT_STATE_DISCONNECTED = 1
        const val CONNECT_STATE_CONNECTING = 2
        const val CONNECT_STATE_CONNECTED = 3
    }

    override fun onMediaItemTransition(mediaId: String) {
        nowPlaying.postValue(mediaId)
    }

    override fun onMediaItemTransition(hasPreviousItem: Boolean, hasNextItem: Boolean) {
        val isSkipToPreviousEnabledValue = isSkipToPreviousEnabled.value ?: false
        if (isSkipToPreviousEnabledValue != hasPreviousItem) {
            isSkipToPreviousEnabled.postValue(hasPreviousItem)
        }
        val isSkipToNextEnabledValue = isSkipToNextEnabled.value ?: false
        if (isSkipToNextEnabledValue != hasNextItem) {
            isSkipToNextEnabled.postValue(hasNextItem)
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        this.isPlaying.postValue(isPlaying)
    }

    override fun onIsLoadingChanged(isLoading: Boolean) {
        val onIsLoadingValue = onIsLoading.value ?: false
        if (onIsLoadingValue != isLoading) {
            onIsLoading.postValue(isLoading)
        }
    }

    override fun updateProgress(positionMs: Long, duration: Long) {
        val mediaDurationValue = mediaDuration.value ?: 0
        if (mediaDurationValue != duration.toInt()) {
            mediaDuration.postValue(duration.toInt())
        }
        mediaProgress.postValue(positionMs.toInt())
    }

    override fun onPlaylistUnselected() {
        playlistUnselected.postValue(true)
    }

}