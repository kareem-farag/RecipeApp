package com.example.recipeapp.utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonRecipePlaceHolder {
    @GET("baking.json")
    Call<List<Recipe>> getRecipeList();
}
