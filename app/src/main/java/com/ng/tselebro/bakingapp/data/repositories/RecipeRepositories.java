package com.ng.tselebro.bakingapp.data.repositories;
import static com.google.common.base.Preconditions.checkNotNull;


public class RecipeRepositories {
    private static  RepositoryContract.RecipeRepository repository = null;

    private  RecipeRepositories(){
//        no instance
    }

    public static RepositoryContract.RecipeRepository getInMemoryRepoInstance (RecipeServiceApi recipeServiceApi){
        checkNotNull(recipeServiceApi);
        if (null == repository){
            repository = new CachedRecipesRepository(recipeServiceApi);
        }
    return  repository;
    }
}
