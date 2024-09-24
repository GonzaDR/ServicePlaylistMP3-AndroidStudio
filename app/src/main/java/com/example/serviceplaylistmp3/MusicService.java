package com.example.serviceplaylistmp3;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

public class MusicService extends Service {

    private ExoPlayer player;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String songUrl = intent.getStringExtra("SONG_URL");

        // Iniciar ExoPlayer
        player = new ExoPlayer.Builder(this).build();
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(songUrl));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

        // Crear una notificación persistente con controles de reproducción
        Notification notification = createNotification("Reproduciendo música");
        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    private Notification createNotification(String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "music_channel")
                .setContentTitle(text)
                .setSmallIcon(R.drawable.ic_music_note)
                .addAction(R.drawable.ic_pause, "Pause", null); // Añadir más acciones según sea necesario

        return builder.build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
