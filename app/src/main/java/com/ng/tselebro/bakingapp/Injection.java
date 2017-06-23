package com.ng.tselebro.bakingapp;

import com.ng.tselebro.bakingapp.data.repositories.RecipeRepositories;
import com.ng.tselebro.bakingapp.data.repositories.RecipeServiceApiImpl;
import com.ng.tselebro.bakingapp.data.repositories.RepositoryContract;



public class Injection {
    public static RepositoryContract.RecipeRepository provideRecipeRepository (){
        return RecipeRepositories.getInMemoryRepoInstance(new RecipeServiceApiImpl());

    }
}
