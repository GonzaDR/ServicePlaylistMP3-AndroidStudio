package com.example.serviceplaylistmp3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity {
    private ExoPlayer player;
    private String songUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Recibir URL de la canción desde la Intent
        Intent intent = getIntent();
        songUrl = intent.getStringExtra("SONG_URL");

        // Inicializar ExoPlayer
        player = new ExoPlayer.Builder(this).build();
        PlayerView playerView = findViewById(R.id.player_view);
        playerView.setPlayer(player);

        // Configurar el MediaItem con la URL de la canción
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(Uri.parse(songUrl))
                .build();

        // Preparar el player con la canción
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play(); // Iniciar la reproducción
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release(); // Liberar recursos cuando se cierre la actividad
        }
    }
}
