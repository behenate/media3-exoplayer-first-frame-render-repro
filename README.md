# media3-exoplayer-first-frame-render-repro
Contains a reproduction of an intermittent issue in media3 exoplayer

## How to reproduce

### Step 1 - Reproduce load failure
Open the project in Android Studio, and run it in an emulator (it's also reproducible on a physical device, but easier in an emulator). If you are "lucky" you'll see that one or more of the players has stayed black, while the rest had successfully loaded the first frame of the video.
Although, most likely all the players will load successfully. In that case you need to close your app and open it again until you see it happen. This can take upward of 20-30 tries, so you have to be a little bit patient.
I have a script prepared that makes reproduciton easier:

```
while true; do
    adb shell am start -n com.wojciechdrozdz.exoplayer.repro/com.wojciechdrozdz.exoplayer.repro.MainActivity -a android.intent.action.VIEW
    sleep 1.5
    adb shell am force-stop com.wojciechdrozdz.exoplayer.repro
    sleep 0.2
done
```
you can paste it into your terminal and run it. Now watch carefully. When you notice that more than one of the players didn't load the first frame, press ctrl+c in order to stop the script.

### Step 2 - Inspect the logs

In the logs you will find:

```
2025-09-30 16:04:05.304 23258-23297 CCodecBufferChannel     com.wojciechdrozdz.exoplayer.repro   I  [c2.goldfish.h264.decoder#152] queueBuffer failed: -32
2025-09-30 16:04:05.304 23258-23297 MediaCodec              com.wojciechdrozdz.exoplayer.repro   I  rendring output error -32
```

which is logged only when a player fails to load the first frame

### Step 3 - ExoPlayer can recover from the failure

While the screens are black, you can press the play button on the bottom, all players will start playing correctly, even those had the black screen issue.

## Other important factors

1. This bug is only reproducible with decoder fallback enabled.

```
.setRenderersFactory(
    DefaultRenderersFactory(context).setEnableDecoderFallback(true)
)
```

When the decorder fallback is disabled another non-recoverable exception shows for some of the players: 

```
    androidx.media3.exoplayer.mediacodec.MediaCodecRenderer$DecoderInitializationException: Decoder init failed: c2.goldfish.h264.decoder, Format(1, null, video/mp4, video/avc, avc1.4D401F, 1993826, en, [400, 400, 30.000011, ColorInfo(Unset color space, Unset color range, Unset color transfer, false, 8bit Luma, 8bit Chroma)], [-1, -1])
```

It's weird that it only happens to some of the players, but I'm not sure if it's related to this issue.

2. The bug also happens with a single player
The example contains multiple players to make reproduction faster, but it also happens when only one `PlayerView` and one `ExoPlayer` is present in the `Activity`.