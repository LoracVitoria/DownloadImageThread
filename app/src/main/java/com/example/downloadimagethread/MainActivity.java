package com.example.downloadimagethread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnEnviar;
    private EditText txtLink;
    private ProgressBar pgbCarregar;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEnviar = findViewById(R.id.btnEnviar);
        txtLink = findViewById(R.id.edtLink);
        pgbCarregar = findViewById(R.id.pgbCarregar);
        imgView = findViewById(R.id.imageView);

        btnEnviar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        new ImageDownloader(pgbCarregar).execute(txtLink.getText().toString());
    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        HttpURLConnection httpURLConnection;
        ProgressBar pgbCarregar;

        public ImageDownloader(ProgressBar pgbCarregar) {
            this.pgbCarregar = pgbCarregar;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                Bitmap temp = BitmapFactory.decodeStream(inputStream);
                return temp;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null)
                httpURLConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null) {
                pgbCarregar.setVisibility(View.INVISIBLE);
                imgView.setImageBitmap(bitmap);
                imgView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            SystemClock.sleep(3000);
        }

        @Override
        protected void onPreExecute() {
            pgbCarregar.setVisibility(View.VISIBLE);
            pgbCarregar.setIndeterminate(true);
        }
    }
}