package com.example.young.themovies;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailMovie extends AppCompatActivity {

    public static String KEY_IMAGE = "image";
    private ImageView imgDetail;
    private TextView txtTahun, txtKeterangan, txtRilis, txtRating, txtTrailer;
    private Button btnFavorite;
    private ArrayList<MovieModel> urls;
    private RequestQueue requestQueue;
    int movie_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Intent intent = getIntent();

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            actionBar.setTitle("Movie Detail");
        }
        urls = new ArrayList<>();

        imgDetail = (ImageView)findViewById(R.id.img_detail);
        txtTahun = (TextView)findViewById(R.id.txt_title);
        txtRating = (TextView)findViewById(R.id.txt_rating);
        txtRilis = (TextView)findViewById(R.id.txt_rilis);
        txtKeterangan = (TextView)findViewById(R.id.txt_keterangan);
        txtTrailer = (TextView)findViewById(R.id.txt_tittle);
        btnFavorite = (Button)findViewById(R.id.btn_favorite);

        Picasso.with(DetailMovie.this).load(intent.getStringExtra("poster_path")).into(imgDetail);

        txtTahun.setText(intent.getStringExtra("title"));
        txtRating.setText("Rating : "+String.valueOf(intent.getFloatExtra("vote_average", 0))+"/10");
        txtRilis.setText("Release on "+intent.getStringExtra("release_date"));
        txtKeterangan.setText(intent.getStringExtra("overview"));
        movie_id = intent.getIntExtra("id", 0);

        getTrailer();
    }

    private void getTrailer() {
        String id = getIntent().getStringExtra("id");
        String api_key="11205cc7ae47e77dc908bfd0785845d1";
        String url = "https://api.themoviedb.org/3/movie/"+id+"/videos?api_key="+api_key +"&language=en-US";

        requestQueue = Volley.newRequestQueue(DetailMovie.this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("succes",response.toString());
                            String page = response.getString("id");
                            JSONArray results = response.getJSONArray("results");

                            MovieModel trailer = null;

                            for (int x=0; x < results.length(); x++){
                                trailer = new MovieModel();
                                trailer.setUrl("http://www.youtube.com/watch?v="+results.getJSONObject(x).getString("key"));
                                urls.add(trailer);
                            }
                            final MovieModel finalTrailer = trailer;
                            txtTrailer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(finalTrailer.getUrl())));
                                }
                            });

                        }catch (JSONException e){}

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
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
