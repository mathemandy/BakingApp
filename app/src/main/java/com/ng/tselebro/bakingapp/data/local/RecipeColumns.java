package com.ng.tselebro.bakingapp.data.local;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;


public interface RecipeColumns {


    @DataType(DataType.Type.INTEGER)
    @PrimaryKey (onConflict = ConflictResolutionType.REPLACE)
    @AutoIncrement String _ID = "_id";

    @DataType(INTEGER)
    @NotNull @Unique (onConflict =  ConflictResolutionType.REPLACE)
    String ID = "id";

    @DataType(DataType.Type.TEXT)
    @NotNull String ingredient =  "ingredient";

    @DataType(DataType.Type.INTEGER)
    @NotNull String recipeName = "name";

    @DataType(DataType.Type.REAL)
    @NotNull String servings = "servings";

    @DataType(DataType.Type.TEXT)
    String image_url = "imageUrl";

    @DataType(DataType.Type.TEXT )
    String steps  = "step";

}
