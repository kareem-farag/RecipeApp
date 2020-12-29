package com.example.recipeapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.recyclers.RecipeAdapter;
import com.example.recipeapp.utils.Ingredient;
import com.example.recipeapp.utils.JsonRecipePlaceHolder;
import com.example.recipeapp.utils.Recipe;
import com.example.recipeapp.utils.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public String JSON_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private List<Recipe> recipeList = new ArrayList<Recipe>();

    public static Recipe recipe = null;
    private RecyclerView recipeRecycler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(JSON_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonRecipePlaceHolder jsonRecipePlaceHolder = retrofit.create(JsonRecipePlaceHolder.class);

        Call<List<Recipe>>call = jsonRecipePlaceHolder.getRecipeList();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipeList = response.body();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });


      //  GetRecipeData getRecipeData = new GetRecipeData();
     //   getRecipeData.execute(JSON_URL);



    }


    public class GetRecipeData extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            URL url = null ;
            try {
                url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream() ;

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String ln ;
                StringBuffer stringBuffer = new StringBuffer();

                while ((ln = bufferedReader.readLine())!= null) {
                    stringBuffer.append(ln + "\n");
                }

                //JSONObject jsonObject =new JSONObject(stringBuffer.toString());
                JSONArray jsonArray = new JSONArray(stringBuffer.toString());

                for(int i=0  ; i < jsonArray.length() ; i++) {
                    JSONObject recipeObject = jsonArray.getJSONObject(i);
                    JSONArray ingredientsJsonArray = recipeObject.getJSONArray("ingredients");
                     List<Ingredient> ingredientList = new ArrayList<Ingredient>() ;
                     List<Step> stepList = new ArrayList<Step>();

                    for (int k = 0 ; k <ingredientsJsonArray.length();k++) {
                        JSONObject ingredientJsonObject = ingredientsJsonArray.getJSONObject(k);
                        ingredientList.add(new Ingredient(ingredientJsonObject.getString("quantity"),ingredientJsonObject.getString("measure"),ingredientJsonObject.getString("ingredient")));
                    }

                    JSONArray stepsJsonArray = recipeObject.getJSONArray("steps");
                    for (int k = 0 ; k <stepsJsonArray.length();k++) {
                        JSONObject stepsJsonObject = stepsJsonArray.getJSONObject(k);
                        stepList.add(new Step(stepsJsonObject.getString("id"),stepsJsonObject.getString("shortDescription"),stepsJsonObject.getString("description"),stepsJsonObject.getString("videoURL"),stepsJsonObject.getString("thumbnailURL")));
                    }


                    Recipe recipe = new Recipe(recipeObject.getString("id"),recipeObject.getString("name"),
                            recipeObject.getString("servings"),recipeObject.getString("image"),ingredientList,stepList);
                    recipeList.add(recipe);
                    ingredientList = null ;
                    stepList = null ;

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null ;
        };

        @Override
        protected void onPostExecute(Void aVoid) {

            RecyclerView recipeview = findViewById(R.id.recipe_recycler_view);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(),ResponsiveLayout.calculateNoOfColumns(getBaseContext()) );
            RecipeAdapter recipeAdapter = new RecipeAdapter(getBaseContext(), recipeList, new RecipeAdapter.OnRecipeClickListener() {
                @Override
                public void onRecipeClickListener(Recipe recipe) {
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getBaseContext(), RecipeWidgetProvider.class));
                    com.example.recipeapp.MainActivity.recipe= recipe;
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.appwidget_list);

                    for (int appWidgetId : appWidgetIds) {
                        RecipeWidgetProvider.updateAppWidget(getBaseContext(), appWidgetManager, appWidgetId);
                    }

                    Intent intent = new Intent(getBaseContext(),RecipeDetails.class);

                    intent.putExtra("recipe", recipe);
                    startActivity(intent);






                }
            }) ;

            recipeview.setAdapter(recipeAdapter);
            recipeview.setLayoutManager(gridLayoutManager);
        }
    }

    
}
