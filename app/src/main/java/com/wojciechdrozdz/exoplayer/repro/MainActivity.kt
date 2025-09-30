package com.wojciechdrozdz.exoplayer.repro

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.wojciechdrozdz.exoplayer.repro.databinding.ActivityMainBinding
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.ui.PlayerView

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private val uri = "android.resource://com.wojciechdrozdz.exoplayer.repro/${R.raw.video}".toUri()
  private var player: ExoPlayer? = null
  private var player2: ExoPlayer? = null
  private var player3: ExoPlayer? = null
  private var player4: ExoPlayer? = null
  private var player5: ExoPlayer? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }

  @OptIn(UnstableApi::class)
  private fun initializePlayer() {
    player = ExoPlayer.Builder(this).buildExample(this, binding.playerView, uri)
    player2 = ExoPlayer.Builder(this).buildExample(this, binding.playerView2, uri)
    player3 = ExoPlayer.Builder(this).buildExample(this, binding.playerView3, uri)
    player4 = ExoPlayer.Builder(this).buildExample(this, binding.playerView4, uri)
    player5 = ExoPlayer.Builder(this).buildExample(this, binding.playerView5, uri)
  }

  private fun releasePlayer() {
    player?.release()
    player2?.release()
    player3?.release()
    player4?.release()
    player5?.release()
  }

  override fun onStart() {
    super.onStart()
    initializePlayer()
  }

  override fun onStop() {
    super.onStop()
    releasePlayer()
  }

  fun play(view: View) {
    player?.play()
    player2?.play()
    player3?.play()
    player4?.play()
    player5?.play()
  }
}

@OptIn(UnstableApi::class)
internal fun ExoPlayer.Builder.buildExample(context: Context, playerView: PlayerView, uri:Uri): ExoPlayer {
  return this
    .setRenderersFactory(
      DefaultRenderersFactory(context).setEnableDecoderFallback(true)
    )
    .build()
    .also { exoPlayer ->
      playerView.player = exoPlayer
      val mediaItem = MediaItem.fromUri(uri)

      exoPlayer.setMediaItem(mediaItem)
      exoPlayer.playWhenReady = false
      exoPlayer.prepare()
    }
}