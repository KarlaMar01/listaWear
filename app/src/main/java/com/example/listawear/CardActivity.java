package com.example.listawear;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class CardActivity extends Activity {
    private TextView titulo;
    private TextView descripcion;
    private Button cerrar;
    private Button registro;
    private int imagen;

    // Variables para la notificación //////////////////////////
    NotificationCompat.Builder notificacion;
    private static final int idUnico = 1234;
    String CHANNEL_ID = "Descarga";

    private NotificationManagerCompat notificationManager;
    private Intent intent;
    private PendingIntent pendingIntent;
    private NotificationCompat.WearableExtender wearableExtender;
    /////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);

        titulo = (TextView) findViewById(R.id.txtTitulo);
        descripcion = (TextView) findViewById(R.id.txtDescripcion);
        cerrar = (Button) findViewById(R.id.btnCerrar);
        registro = (Button) findViewById(R.id.btnRegistro);

        Bundle extras = getIntent().getExtras();
        if( extras != null){
            titulo.setText(extras.getString("titulo"));
            descripcion.setText(extras.getString("descripcion"));
            imagen = extras.getInt("imagen");
        }

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNotification();
            }
        });
    }

    public void launchNotification(){
        notificacion = new NotificationCompat.Builder(CardActivity.this, CHANNEL_ID);

        notificationManager = NotificationManagerCompat.from(CardActivity.this);

        intent = new Intent(CardActivity.this, CardActivity.class);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            String name = "Descarga";

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            notificationManager.createNotificationChannel(notificationChannel);

            pendingIntent =
                    PendingIntent.getActivity(CardActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            wearableExtender = new NotificationCompat.WearableExtender();

            notificacion.setSmallIcon(R.drawable.ic_stat_name)
                    .setTicker("Descarga Exitosa")
                    //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_name))
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle("Descarga")
                    .setContentText("Se ha descargado correctamente el libro de " + titulo.getText())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setAutoCancel(true)
                    //.setContentIntent(pendingIntent)
                    .extend(wearableExtender);

            notificationManager.notify(idUnico, notificacion.build());
        }
    }
}
