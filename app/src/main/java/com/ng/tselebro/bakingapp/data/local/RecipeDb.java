package com.ng.tselebro.bakingapp.data.local;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Uses the Schematic (https://github.com/SimonVT/schematic) library to create a database with one
 * table for recipes
 */


@Database(version = RecipeDb.DATABASE_VERSION)
public class RecipeDb {
    public static final int DATABASE_VERSION = 4;

    @Table(RecipeColumns.class)
    public static final String RECIPE = "recipes_list";

}
