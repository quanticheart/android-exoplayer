package com.quanticheart.exoplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.quanticheart.exoplayer.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       activity?. window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preparePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun preparePlayer() {
        exoPlayer = ExoPlayer.Builder(requireContext()).build()
        exoPlayer?.playWhenReady = true
        binding.playerView.player = exoPlayer
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaItem =
            MediaItem.fromUri(URL)
        val mediaSource = ProgressiveMediaSource.Factory(defaultHttpDataSourceFactory)
            .createMediaSource(mediaItem)
        exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.seekTo(playbackPosition)
        exoPlayer?.playWhenReady = playWhenReady
        exoPlayer?.prepare()
    }

    private fun releasePlayer() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            playWhenReady = player.playWhenReady
            player.release()
            exoPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    companion object {
        const val URL = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
    }
}