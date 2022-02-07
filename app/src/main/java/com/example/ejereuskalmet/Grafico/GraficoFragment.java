package com.example.ejereuskalmet.Grafico;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ejereuskalmet.Balizas.MasBalizasRVAdapter;
import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.MainActivity;
import com.example.ejereuskalmet.R;
import com.example.ejereuskalmet.ui.main.SectionsPagerAdapter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GraficoFragment extends Fragment {

    private MainActivity mainActivity;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private Button btnFecha;
    private TextView tvFecha;
    private GraphView gvGrafico;
    private Spinner cbBaliza;

    public GraficoFragment() {
        // Required empty public constructor
    }

    public GraficoFragment(MainActivity mainActivity, SectionsPagerAdapter sectionsPagerAdapter) {
        this.mainActivity = mainActivity;
        this.sectionsPagerAdapter = sectionsPagerAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inf = inflater.inflate(R.layout.fragment_grafico, container, false);

        final DatePickerDialog[] picker = new DatePickerDialog[1];

        tvFecha = inf.findViewById(R.id.tvfecha);
        btnFecha = inf.findViewById(R.id.btnFecha);
        gvGrafico = inf.findViewById(R.id.idGraphView);
        cbBaliza = inf.findViewById(R.id.cbBaliza);

        String[] balizas = new String[MasBalizasRVAdapter.balizas.size()];

        for (int i = 0; i < MasBalizasRVAdapter.balizas.size(); i++) {
            balizas[i] = MasBalizasRVAdapter.balizas.get(i).name;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inf.getContext(),R.layout.support_simple_spinner_dropdown_item,balizas);

        cbBaliza.setAdapter(adapter);

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker[0] = new DatePickerDialog(inf.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        monthOfYear = monthOfYear +1;

                        String month1 = "" + monthOfYear;
                        String day1 = "" + day;

                        if (monthOfYear < 10) {
                            month1 = "0" + monthOfYear;
                        }
                        if (dayOfMonth < 10) {
                            day1 = "0" + dayOfMonth;
                        }

                        int yearHoy = Calendar.getInstance().get(Calendar.YEAR);

                        int monthHoy = Calendar.getInstance().get(Calendar.MONTH) + 1;

                        String month1Hoy = "" + monthHoy;

                        if (monthHoy < 10) {
                            month1Hoy = "0" + monthHoy;

                        }

                        int dayHoy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                        String day1Hoy = "" + dayHoy;

                        if (dayHoy < 10) {
                            day1Hoy = "0" + dayHoy;

                        }
                        if (year > yearHoy || month > monthHoy && year > yearHoy || day > dayHoy && month > monthHoy && year > yearHoy ){
                            Toast.makeText(inf.getContext(), "Error al recoger elos datos", Toast.LENGTH_SHORT).show();
                        }else {

                            tvFecha.setText("Fecha seleccionada: " + day1 + "/" + month1 + "/" + year);

                            String idBaliza = "C069";
                            for (int i = 0; i < MasBalizasRVAdapter.balizas.size(); i++) {
                                if (MasBalizasRVAdapter.balizas.get(i).name.equals(cbBaliza.getSelectedItem().toString())) {
                                    idBaliza = MasBalizasRVAdapter.balizas.get(i).id;
                                }
                            }

                            gvGrafico.removeAllSeries();

                            RequestQueue queue = Volley.newRequestQueue(mainActivity);

                            String url = "https://www.euskalmet.euskadi.eus/vamet/stations/readings/" + idBaliza + "/" + year + "/" + month1 + "/" + day1 + "/readingsData.json";

                            System.out.println(idBaliza + " y su url " + url + " y la fecha " + "/" + year + "/" + month1 + "/" + day1);

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    ArrayList<String> horas = new ArrayList<>();
                                    ArrayList<String> datos = new ArrayList<>();

                                    try {

                                        JSONObject obj1 = (JSONObject) response.get("21");
                                        JSONObject obj2 = (JSONObject) obj1.get("data");
                                        obj2.get(obj2.names().get(0).toString());
                                        JSONObject objfinal = (JSONObject) obj2.get(obj2.names().get(0).toString());

                                        for (int j = 0; j < objfinal.names().length(); j++) {
                                            horas.add(objfinal.names().get(j).toString());
                                        }
                                        Collections.sort(horas);

                                        for (int i = 0; i < horas.size(); i++) {
                                            datos.add(objfinal.get(horas.get(i)).toString());
                                        }

                                        for (int i = 0; i < horas.size(); i++) {
                                            System.out.println("Hora: " + horas.get(i));
                                            System.out.println("Dato: " + datos.get(i));
                                        }

                                        DataPoint[] dataPoints = new DataPoint[horas.size()];

                                        for (int i = 0; i < horas.size(); i++) {
                                            DataPoint values = new DataPoint(i, (Math.round(Double.parseDouble(datos.get(i).toString()))));
                                            dataPoints[i] = values;
                                        }

                                        LineGraphSeries<DataPoint> series;

                                        gvGrafico = (GraphView) inf.findViewById(R.id.idGraphView);
                                        series = new LineGraphSeries<DataPoint>(dataPoints);

                                        series.setDrawBackground(true);
                                        series.setAnimated(true);

                                        gvGrafico.addSeries(series);

                                        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(gvGrafico);

                                        String[] horasGrafico = new String[4];

                                        horasGrafico[0] = horas.get(0);
                                        horasGrafico[1] = horas.get(Math.round(horas.size() / 3));
                                        horasGrafico[2] = horas.get((int) Math.round(horas.size() / 1.5));
                                        horasGrafico[3] = horas.get(horas.size() - 1);

                                        staticLabelsFormatter.setHorizontalLabels(horasGrafico);

                                        gvGrafico.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println(error);
                                    System.out.println("Ha ocurrido un error al realizar la peticion a Euskalmet");
                                    Toast.makeText(inf.getContext(), "Error al recoger los datos, prueba otra fecha", Toast.LENGTH_SHORT).show();
                                }
                            });
                            queue.add(jsonObjectRequest);
                        }
                    }
                }, year, month, day);
                picker[0].show();
            }
        });
        return inf;
    }
}