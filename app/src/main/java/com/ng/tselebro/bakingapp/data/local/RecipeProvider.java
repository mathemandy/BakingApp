package com.ng.tselebro.bakingapp.data.local;

import android.content.ContentResolver;
import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) to create a content provider and
 * define
 * URIs for the provider
 */


@ContentProvider(
        authority = RecipeProvider.AUTHORITY,
        database =  RecipeDb.class)
public class RecipeProvider {



    public  static  final String AUTHORITY = "com.ng.tselebro.bakingapp.data.local.provider";

    @TableEndpoint(table = RecipeDb.RECIPE) public static class Recipes {

        @ContentUri(
                path = "recipes",
                type = "vnd.android.cursor.dir/recipes",
                defaultSort = RecipeColumns.id + " ASC")
        public  static  final Uri CONTENT_RECIPES_URI = Uri.parse("content://" + AUTHORITY + "/recipes");



    }
}
