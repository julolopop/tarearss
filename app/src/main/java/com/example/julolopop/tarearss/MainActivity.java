package com.example.julolopop.tarearss;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.julolopop.tarearss.api.ApiAdapter;
import com.example.julolopop.tarearss.pojo.Email;
import com.example.julolopop.tarearss.pojo.Tarea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int ADD_CODE = 100;
    public static final int UPDATE_CODE = 200;
    public static final int OK = 1;
    public static final String MAIL = "mail";

    FloatingActionButton fab;
    ListView listView;

    int positionClicked;
    //private SitesAdapter adapter;
    private ArrayList<Tarea> tareas;
    private ArrayAdapter<Tarea> adapter;
    ProgressDialog progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tareas = new ArrayList<>();
        fab = findViewById(R.id.fab);
        listView = findViewById(android.R.id.list);


        fab.setOnClickListener(this);

        adapter = new ArrayAdapter<Tarea>(this, android.R.layout.simple_list_item_1, tareas);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getBaseContext(), UpdateActivity.class);
                i.putExtra("tarea", tareas.get(position));
                startActivity(i);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Alert(tareas.get(position).getId(), tareas.get(position).getNombre(), position);
                return false;
            }
        });

        downloadTareas();
    }

    private void downloadTareas() {
        progreso = new ProgressDialog(this);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("conectando . . .");
        progreso.setCancelable(false);
        progreso.show();

        Call<ArrayList<Tarea>> call = ApiAdapter.getInstance().getTareas();
        call.enqueue(new Callback<ArrayList<Tarea>>() {
            @Override
            public void onResponse(Call<ArrayList<Tarea>> call, Response<ArrayList<Tarea>> response) {
                progreso.dismiss();
                if (response.isSuccessful()) {
                    tareas = response.body();
                    adapter.clear();
                    adapter.addAll(tareas);

                } else {
                    StringBuilder message = new StringBuilder();
                    if (response.body() != null)
                        message.append("\n" + response.body());
                    if (response.errorBody() != null)
                        try {
                            message.append("\n" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    Toast.makeText(getBaseContext(), "Descarga error: " + response.code(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Tarea>> call, Throwable t) {
                progreso.dismiss();
                if (t != null)
                    Toast.makeText(getBaseContext(), "Fallo en la comunicación\n" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Connecting . . .");
        progreso.setCancelable(false);
        progreso.show();
    }

    @Override
    public void onClick(View view) {
        if (view == fab) {
            Intent i = new Intent(this, addActivity.class);
            startActivityForResult(i, ADD_CODE);
        }
    }


    private void Alert(final int idTarea, String name, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(name + "\nQuieres eliminarlo?")
                .setTitle("Delete")
                .setPositiveButton("confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        connection(idTarea, position);
                    }
                })
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void connection(int idTarea, final int position) {
        Call<ResponseBody> call = ApiAdapter.getInstance().deleteSite(idTarea);
        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Connecting . . .");
        progreso.setCancelable(false);
        progreso.show();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progreso.dismiss();
                if (response.isSuccessful()) {
                    tareas.remove(position);
                    adapter.clear();
                    adapter.addAll(tareas);
                } else {
                    StringBuilder message = new StringBuilder();
                    message.append("Error al eliminar a tarea: " + response.code());
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progreso.dismiss();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.guardar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuguardar) {
            progreso = new ProgressDialog(this);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Connecting . . .");
            progreso.setCancelable(false);
            progreso.show();

            Call<ResponseBody> call = ApiAdapter.getInstance().sendEmail(new Email("julolopop@gmail.com", "guardado"));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progreso.dismiss();
                    if (response.isSuccessful()) {
                        Intent i = new Intent();
                        setResult(OK, i);

                        Toast.makeText(getBaseContext(), "mensaje enviado", Toast.LENGTH_LONG).show();
                    } else {
                        StringBuilder message = new StringBuilder();
                        Toast.makeText(getBaseContext(), "error mensage no enviado", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progreso.dismiss();
                }
            });
        }


        return super.onOptionsItemSelected(item);
    }
}
