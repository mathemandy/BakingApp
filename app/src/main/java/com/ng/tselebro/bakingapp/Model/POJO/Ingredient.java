package com.ng.tselebro.bakingapp.Model.POJO;


import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


    public class Ingredient implements Parcelable
    {

        @SerializedName("quantity")
        @Expose
        private float quantity;
        @SerializedName("measure")
        @Expose
        private String measure;
        @SerializedName("ingredient")
        @Expose
        private String ingredient;
        public final static Parcelable.Creator<Ingredient> CREATOR = new Creator<Ingredient>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Ingredient createFromParcel(Parcel in) {
                Ingredient instance = new Ingredient();
                instance.quantity = ((float) in.readValue((float.class.getClassLoader())));
                instance.measure = ((String) in.readValue((String.class.getClassLoader())));
                instance.ingredient = ((String) in.readValue((String.class.getClassLoader())));
                return instance;
            }

            public Ingredient[] newArray(int size) {
                return (new Ingredient[size]);
            }

        }
                ;

        public float getQuantity() {
            return quantity;
        }

        public void setQuantity(float quantity) {
            this.quantity = quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(quantity);
            dest.writeValue(measure);
            dest.writeValue(ingredient);
        }

        public int describeContents() {
            return 0;
        }

    }
