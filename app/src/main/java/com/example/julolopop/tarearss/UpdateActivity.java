package com.example.julolopop.tarearss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.julolopop.tarearss.api.ApiAdapter;
import com.example.julolopop.tarearss.pojo.Tarea;

import java.io.IOException;
import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener, Callback<Tarea> {

    public static final int OK = 1;

    TextView idTarea;
    TextView fechaTarea;
    EditText nombre;
    EditText descripcion;
    EditText importancia;
    EditText enlace;
    EditText imagen;
    Button accept;
    Button cancel;
    //EditText nameSite, linkSite, emailSite;
    //TextView idTarea;
    //Button accept, cancel;
    ProgressDialog progreso;
    Tarea tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        ButterKnife.bind(this);
        idTarea = (TextView) findViewById(R.id.idTarea);
        nombre = (EditText) findViewById(R.id.edtNombre);
        descripcion = (EditText) findViewById(R.id.edtDescripcion);
        fechaTarea = (TextView) findViewById(R.id.fechaTarea);
        importancia = (EditText) findViewById(R.id.edtImportancia);
        enlace = (EditText) findViewById(R.id.edtenlace);
        imagen = (EditText) findViewById(R.id.edtimagen);
        accept = (Button) findViewById(R.id.accept);
        cancel = (Button) findViewById(R.id.cancel);
        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);

        Intent i = getIntent();
        tarea = (Tarea) i.getSerializableExtra("tarea");
        idTarea.setText(String.valueOf(tarea.getId()));
        nombre.setText(tarea.getNombre());
        descripcion.setText(tarea.getDescripcion());
        importancia.setText(tarea.getImportancia());
        fechaTarea.setText(new SimpleDateFormat("MM/dd/yyyy").format(tarea.getFecha()));
        enlace.setText(tarea.getEnlace());
        imagen.setText(tarea.getImagen());
    }

    @Override
    public void onClick(View v) {
        String n, d,e,img;
        int i;

        if (v == accept) {
            n = nombre.getText().toString();
            d = descripcion.getText().toString();
            e = enlace.getText().toString();
            img = imagen.getText().toString();
            i = Integer.parseInt(importancia.getText().toString());
            if (n.isEmpty() || d.isEmpty())
                Toast.makeText(this, "argumentos vacios", Toast.LENGTH_SHORT).show();
            else {
                tarea.setNombre(n);
                tarea.setDescripcion(d);
                tarea.setImportancia(i);
                tarea.setEnlace(e);
                tarea.setImagen(img);
                connection(tarea);
            }
        }
        if (v == cancel) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void connection(Tarea tarea) {
        progreso = new ProgressDialog(this);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Connecting . . .");
        progreso.setCancelable(false);
        progreso.show();

        Call<Tarea> call = ApiAdapter.getInstance().updateSite(tarea, tarea.getId());
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Tarea> call, Response<Tarea> response) {
        progreso.dismiss();
        if (response.isSuccessful()) {
            Tarea tarea = response.body();
            Intent i = new Intent();
            Bundle mBundle = new Bundle();
            mBundle.putString("nombre", tarea.getNombre());
            mBundle.putString("descripcion", tarea.getDescripcion());
            mBundle.putString("importancia", tarea.getImportancia());
            mBundle.putString("fecha", new SimpleDateFormat("dd/MM/yyyy").format(tarea.getFecha()));
            mBundle.putString("enlace", tarea.getEnlace());
            mBundle.putString("imagen", tarea.getImagen());
            i.putExtras(mBundle);
            setResult(OK, i);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else {
            StringBuilder message = new StringBuilder();
            message.append("Download error: " + response.code());
            if (response.body() != null)
                message.append("\n" + response.body());
            if (response.errorBody() != null)
                try {
                    message.append("\n" + response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void onFailure(Call<Tarea> call, Throwable t) {
        progreso.dismiss();
    }

}
