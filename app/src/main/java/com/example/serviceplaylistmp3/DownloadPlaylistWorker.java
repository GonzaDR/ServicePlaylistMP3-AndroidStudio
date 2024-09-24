package com.example.serviceplaylistmp3;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DownloadPlaylistWorker extends Worker {
    public DownloadPlaylistWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String jsonUrl = "http://www.rafaelamorim.com.br/mobile2/musicas/list.json";
        List<Song> songList = downloadAndProcessJson(jsonUrl);

        if (songList != null && !songList.isEmpty()) {
            for (Song song : songList) {
                downloadSong(song.url); // Descargar cada canción
            }
            return Result.success();
        }
        return Result.failure();
    }

    // Método para descargar y procesar el JSON de la playlist
    private List<Song> downloadAndProcessJson(String jsonUrl) {
        List<Song> songList = new ArrayList<>();
        try {
            URL url = new URL(jsonUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String jsonStr = readStream(in);

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject songObject = jsonArray.getJSONObject(i);
                String title = songObject.getString("title");
                String author = songObject.getString("author");
                String urlSong = songObject.getString("url");
                String duration = songObject.getString("duration");
                songList.add(new Song(title, author, urlSong, duration));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songList;
    }

    // Método para leer el InputStream y convertirlo en String
    private String readStream(InputStream in) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    // Método para descargar la canción (debes implementar la lógica de descarga aquí)
    private void downloadSong(String fileUrl) {
        // Implementa la lógica de descarga de cada archivo MP3 aquí
    }
}
