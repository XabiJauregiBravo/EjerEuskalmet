package com.example.ejereuskalmet.Api;

import android.os.Handler;
import android.os.HandlerThread;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ejereuskalmet.DB.Balizas;
import com.example.ejereuskalmet.DB.Datos;
import com.example.ejereuskalmet.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Api {

    public static MainActivity mainActivity;
    public static RequestQueue queue;

    public Api(MainActivity ma, RequestQueue queue) {
        this.mainActivity = ma;
        this.queue = queue;
    }

    public static void getBalizasApi() {

        HandlerThread ht = new HandlerThread("HandleThread");
        ht.start();

        Handler handlerLeer = new Handler(ht.getLooper());

        handlerLeer.post(new Runnable() {
            @Override
            public void run() {

                /** RECOGER BALIZAS EUSKALMET **/

                RequestQueue queue = Volley.newRequestQueue(mainActivity);
                String url = "https://www.euskalmet.euskadi.eus/vamet/stations/stationList/stationList.json";

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    // private JSONArray responseBalizas;

                    @Override
                    public void onResponse(JSONArray response) {
                        getDatosbalizasApi(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Ha ocurrido un error al realizar la peticion a Euskalmet");
                    }
                });

                // Access the RequestQueue through your singleton class.
                queue.add(jsonArrayRequest);

            }
        });
    }

    public static void getDatosbalizasApi(JSONArray responseBalizas) {


        HandlerThread ht = new HandlerThread("HandleThread");
        ht.start();

        Handler handlerLeer = new Handler(ht.getLooper());

        handlerLeer.post(new Runnable() {
            @Override
            public void run() {

                /** Manejar JSON Datos meteorologicos Euskalmet **/

                for (int i = 0; i < responseBalizas.length(); i++) {

                    JSONObject object = null;
                    Balizas baliza = new Balizas();

                    try {
                        object = (JSONObject) responseBalizas.get(i);

                        baliza.id = object.get("id").toString();

                        baliza.name = object.get("name").toString();

                        baliza.nameEus = object.get("nameEus").toString();

                        baliza.municipality = object.get("municipality").toString();

                        baliza.province = object.get("province").toString();

                        baliza.altitude = object.get("altitude").toString();

                        baliza.x = Double.parseDouble(object.get("y").toString());

                        baliza.y = Double.parseDouble(object.get("x").toString());

                        baliza.stationType = object.get("stationType").toString();

                        baliza.activated = "false";

                    } catch (JSONException e) {
                        //  e.printStackTrace();
                    }
                    if (baliza.stationType.equals("METEOROLOGICAL")) {

                        if (mainActivity.db.balizasDao().Existe(baliza.id)) {
                            System.out.println("Nombre baliza:" + baliza.name);
                            System.out.println(baliza.name + " ya existe");
                        } else {
                            System.out.println("Nombre baliza:" + baliza.name);
                            mainActivity.db.balizasDao().insert(baliza);
                        }

                        /** RECOGER DATOS METEOROLOGICOS BALIZAS **/

                        int year = Calendar.getInstance().get(Calendar.YEAR);

                        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

                        String month1 = "" + month;

                        if (month < 10) {
                            month1 = "0" + month;

                        }

                        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                        String day1 = "" + day;

                        if (day < 10) {
                            day1 = "0" + day;

                        }
                        Datos lectura = new Datos();

                        lectura.id = baliza.id;

                        RequestQueue queue2 = Volley.newRequestQueue(mainActivity);

                        String url2 = "https://www.euskalmet.euskadi.eus/vamet/stations/readings/" + baliza.id + "/" + year + "/" + month1 + "/" + day1 + "/readingsData.json";
                        System.out.println(baliza.name+ " y su url "+url2);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String a[] = {"11", "12", "14", "21", "31", "40", "70"};

                                    for (int i = 0; i < a.length; i++) {

                                        JSONObject obj1 = (JSONObject) response.get(a[i]);
                                        JSONObject obj2 = (JSONObject) obj1.get("data");
                                        obj2.get(obj2.names().get(0).toString());
                                        JSONObject objfinal = (JSONObject) obj2.get(obj2.names().get(0).toString());
                                        ArrayList<String> horas = new ArrayList<>();

                                        for (int j = 0; j < objfinal.names().length(); j++) {
                                            horas.add(objfinal.names().get(j).toString());
                                        }
                                        Collections.sort(horas);

                                        lectura.hora = horas.get(horas.size() - 1);

                                        lectura.name = baliza.name;

                                        switch (a[i]) {
                                            case "11":
                                                // System.out.println("mean_speed de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                                lectura.mean_speed = (double) objfinal.get(horas.get(horas.size() - 1));
                                                break;
                                            case "12":
                                                //  System.out.println("mean_direction de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                                lectura.mean_direction = (double) objfinal.get(horas.get(horas.size() - 1));

                                                break;
                                            case "14":
                                                // System.out.println("max_speed de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                                lectura.max_speed = (double) objfinal.get(horas.get(horas.size() - 1));
                                                break;
                                            case "21":
                                                //System.out.println("temperature de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                                lectura.temperature = (double) objfinal.get(horas.get(horas.size() - 1));
                                                break;
                                            case "31":
                                                //System.out.println("humidity de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                                lectura.humidity = (double) objfinal.get(horas.get(horas.size() - 1));
                                                break;
                                            case "40":
                                                //  System.out.println("precipitation de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                                lectura.precipitation = (double) objfinal.get(horas.get(horas.size() - 1));
                                                break;
                                        }
                                    }

                                    HandlerThread ht = new HandlerThread("HandleThread");
                                    ht.start();

                                    Handler handlerLeer = new Handler(ht.getLooper());

                                    handlerLeer.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mainActivity.db.datosDao().Existe(lectura.id)) {

                                                //  System.out.println("Nombre lectura: " + lectura.name);
                                                mainActivity.db.datosDao().update(lectura.id, lectura.mean_direction, lectura.mean_speed, lectura.max_speed, lectura.temperature, lectura.humidity, lectura.precipitation, lectura.hora, lectura.name);
                                            } else {
                                                //  System.out.println("Nombre lectura: " + lectura.name);
                                                mainActivity.db.datosDao().insert(lectura);
                                            }
                                        }
                                    });
                                } catch (JSONException e) {
                                    HandlerThread ht = new HandlerThread("HandleThread");
                                    ht.start();

                                    Handler handlerLeer = new Handler(ht.getLooper());

                                    handlerLeer.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mainActivity.db.datosDao().Existe(lectura.id)) {

                                                //  System.out.println("Nombre lectura: " + lectura.name);
                                                mainActivity.db.datosDao().update(lectura.id, lectura.mean_direction, lectura.mean_speed, lectura.max_speed, lectura.temperature, lectura.humidity, lectura.precipitation, lectura.hora, lectura.name);
                                            } else {
                                                //  System.out.println("Nombre lectura: " + lectura.name);
                                                mainActivity.db.datosDao().insert(lectura);
                                            }
                                        }
                                    });
                                    // e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //System.out.println(error);
                                System.out.println("Ha ocurrido un error al realizar la peticion a Euskalmet");
                                HandlerThread ht = new HandlerThread("HandleThread");
                                ht.start();

                                Handler handlerLeer = new Handler(ht.getLooper());

                                handlerLeer.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mainActivity.db.balizasDao().delete(baliza);
                                    }

                                });
                            }
                        });
                        queue2.add(jsonObjectRequest);
                    }
                }
            }
        });
    }

    public static void actualizarBalizas(ArrayList<Balizas> misbalizas) {

        for (Balizas baliza : misbalizas) {

            /* Crear todas las lecturas */

            int year = Calendar.getInstance().get(Calendar.YEAR);

            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

            String month1 = "" + month;

            if (month < 10) {
                month1 = "0" + month;
                ;
            }

            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            Datos lectura = new Datos();

            lectura.id = baliza.id;

            //System.out.println("Size de balizas response : " + response.length());

            String url = "https://www.euskalmet.euskadi.eus/vamet/stations/readings/" + baliza.id + "/" + year + "/" + month1 + "/" + day + "/readingsData.json";
            //System.out.println(baliza.name + " y su url " + url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        String a[] = {"11", "12", "14", "21", "31", "40", "70"};

                        for (int i = 0; i < a.length; i++) {

                            JSONObject obj1 = (JSONObject) response.get(a[i]);
                            JSONObject obj2 = (JSONObject) obj1.get("data");
                            obj2.get(obj2.names().get(0).toString());
                            JSONObject objfinal = (JSONObject) obj2.get(obj2.names().get(0).toString());
                            ArrayList<String> horas = new ArrayList<>();

                            for (int j = 0; j < objfinal.names().length(); j++) {
                                horas.add(objfinal.names().get(j).toString());
                            }
                            Collections.sort(horas);

                            lectura.hora = horas.get(horas.size() - 1);

                            lectura.name = baliza.name;

                            switch (a[i]) {
                                case "11":
                                    // System.out.println("mean_speed de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                    lectura.mean_speed = (double) objfinal.get(horas.get(horas.size() - 1));
                                    break;
                                case "12":
                                    //  System.out.println("mean_direction de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                    lectura.mean_direction = (double) objfinal.get(horas.get(horas.size() - 1));

                                    break;
                                case "14":
                                    // System.out.println("max_speed de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                    lectura.max_speed = (double) objfinal.get(horas.get(horas.size() - 1));
                                    break;
                                case "21":
                                    //System.out.println("temperature de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                    lectura.temperature = (double) objfinal.get(horas.get(horas.size() - 1));
                                    break;
                                case "31":
                                    //System.out.println("humidity de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                    lectura.humidity = (double) objfinal.get(horas.get(horas.size() - 1));
                                    break;
                                case "40":
                                    //  System.out.println("precipitation de " + baliza.id + " en la hora " + horas.get(horas.size() - 1) + " : " + objfinal.get(horas.get(horas.size() - 1)));
                                    lectura.precipitation = (double) objfinal.get(horas.get(horas.size() - 1));
                                    break;
                            }
                        }

                        HandlerThread ht = new HandlerThread("HandleThread");
                        ht.start();

                        Handler handlerLeer = new Handler(ht.getLooper());

                        handlerLeer.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mainActivity.db.datosDao().Existe(lectura.id)) {
                                    //System.out.println("Nombre lectura: " + lectura.name);
                                    mainActivity.db.datosDao().update(lectura.id, lectura.mean_direction, lectura.mean_speed, lectura.max_speed, lectura.temperature, lectura.humidity, lectura.precipitation, lectura.hora, lectura.name);
                                } else {
                                    //System.out.println("Nombre lectura: " + lectura.name);
                                    mainActivity.db.datosDao().insert(lectura);
                                }
                            }
                        });/*a*/
                    } catch (JSONException e) {
                        HandlerThread ht = new HandlerThread("HandleThread");
                        ht.start();

                        Handler handlerLeer = new Handler(ht.getLooper());

                        handlerLeer.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mainActivity.db.datosDao().Existe(lectura.id)) {

                                    //System.out.println("Nombre lectura: " + lectura.name);
                                    mainActivity.db.datosDao().update(lectura.id, lectura.mean_direction, lectura.mean_speed, lectura.max_speed, lectura.temperature, lectura.humidity, lectura.precipitation, lectura.hora, lectura.name);
                                } else {
                                    //System.out.println("Nombre lectura: " + lectura.name);
                                    mainActivity.db.datosDao().insert(lectura);
                                }
                            }
                        });
                        //  e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //System.out.println(error);
                    System.out.println("Ha ocurrido un error al realizar la peticion a Euskalmet enla funcion actualizar balizas");
                }
            });
            queue.add(jsonObjectRequest);
        }
    }
}
