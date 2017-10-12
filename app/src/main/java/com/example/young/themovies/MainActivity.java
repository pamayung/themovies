package com.example.young.themovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GridView gvItem;
    private RequestQueue requestQueue;
    private ArrayList<MovieModel> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Populer Movies");

        gvItem = (GridView)findViewById(R.id.gv_item);
        items = new ArrayList<>();

        getData();
    }
    private void getData(){
        String api_key="11205cc7ae47e77dc908bfd0785845d1";
        String url ="https://api.themoviedb.org/3/movie/popular?api_key="+api_key;
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("succes",response.toString());

                            String page = response.getString("page");
                            JSONArray results = response.getJSONArray("results");
                            MovieModel movie = null;

                            for (int x = 0; x < results.length(); x++){
                                movie = new MovieModel();
                                movie.setId(results.getJSONObject(x).getString("id"));
                                movie.setImage("http://image.tmdb.org/t/p/w185"+results.getJSONObject(x).getString("poster_path"));
                                movie.setRilis(results.getJSONObject(x).getString("release_date"));
                                movie.setTitle(results.getJSONObject(x).getString("title"));
                                movie.setKeterangan(results.getJSONObject(x).getString("overview"));
                                movie.setVote(results.getJSONObject(x).getLong("vote_average"));
                                items.add(movie);
                            }
                            MovieAdapter adapter = new MovieAdapter(MainActivity.this, items);
                            gvItem.setAdapter(adapter);

                            gvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    MovieModel mvi = items.get(position);

                                    Intent intent = new Intent(MainActivity.this, DetailMovie.class);
                                    intent.putExtra("poster_path", mvi.getImage());
                                    intent.putExtra("release_date", mvi.getRilis());
                                    intent.putExtra("title", mvi.getTitle());
                                    intent.putExtra("overview", mvi.getKeterangan());
                                    intent.putExtra("vote_average", mvi.getVote());
                                    intent.putExtra("id", mvi.getId());
                                    startActivity(intent);
                                }
                            });
                        }catch (JSONException e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("volleyError",error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
    }
}
