package com.ng.tselebro.bakingapp.recipe;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ng.tselebro.bakingapp.Model.POJO.Recipe;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.recipe.MainActivity.RecipeItemListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private final RecipeItemListener mItemListener;
    private List<Recipe> mRecipes;
    private Context mContext;


    public RecipeAdapter(List<Recipe> recipes, RecipeItemListener recipeItemListener) {
        setList(recipes);
        mItemListener = recipeItemListener;
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        int layoutForlistItem = R.layout.bakinglist;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view  = inflater.inflate(layoutForlistItem, parent, false);
        return  new RecipeAdapterViewHolder(view, mItemListener);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        String imageUrl;


        switch (position){
            case 0 :
                imageUrl = recipe.getImage();
                imageUrl = imageUrl.isEmpty() ? "http://noImage" : imageUrl;
                Picasso.with(mContext).load(imageUrl)
                        .placeholder(holder.nuellaPie)
                        .error(holder.nuellaPie)
                        .into(holder.RecipeImage);
                holder.recipeServings.setText(String.format(Locale.ENGLISH, "%2d", recipe.getServings()));

                break;
            case 1 :
                imageUrl = recipe.getImage();
                imageUrl = imageUrl.isEmpty() ? "http://noImage" : imageUrl;
                Picasso.with(mContext).load(imageUrl)
                        .placeholder(holder.brownie)
                        .error(holder.brownie)
                        .into(holder.RecipeImage);
                break;
            case 2 :
                imageUrl = recipe.getImage();
                imageUrl = imageUrl.isEmpty() ? "http://noImage" : imageUrl;
                Picasso.with(mContext).load(imageUrl)
                        .placeholder(holder.yellocake)
                        .error(holder.yellocake)
                        .into(holder.RecipeImage);
                break;
            case 3 :
                imageUrl = recipe.getImage();
                imageUrl = imageUrl.isEmpty() ? "http://noImage" : imageUrl;
                Picasso.with(mContext).load(imageUrl)
                        .placeholder(holder.cheeseCake)
                        .error(holder.cheeseCake)
                        .into(holder.RecipeImage);
                break;
            default:
                imageUrl = recipe.getImage();
                imageUrl = imageUrl.isEmpty() ? "http://noImage" : imageUrl;
                Picasso.with(mContext).load(imageUrl)
                        .placeholder(holder.cheeseCake)
                        .error(holder.cheeseCake)
                        .into(holder.RecipeImage);

        }
        holder.recipeName.setText(recipe.getName());
        holder.recipeServings.setText(String.format(Locale.ENGLISH, "%2d", recipe.getServings()));



    }

    public void replaceData(List<Recipe> recipes){
        setList(recipes);
        notifyDataSetChanged();
    }

   private  void setList (List<Recipe> recipes){
       mRecipes = checkNotNull(recipes);
   }
    @Override
    public int getItemCount() {
        if (null == mRecipes){
            return 0;
        }
        return mRecipes.size();
    }

    private Recipe getItem(int position){
        return mRecipes.get(position);
    }

    class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recipeImage)
        ImageView RecipeImage;

        @BindView(R.id.recipeName)
        TextView recipeName;

        @BindView(R.id.recipeDescription)
        TextView recipeServings;

        @BindDrawable(R.drawable.demoimage)
        Drawable cheeseCake;

        @BindDrawable(R.drawable.nupie1)
        Drawable nuellaPie;

        @BindDrawable(R.drawable.yellowcake1)
        Drawable yellocake;

        @BindDrawable(R.drawable.brownie1)
        Drawable brownie;



        private final RecipeItemListener mItemListener;

        public RecipeAdapterViewHolder(View itemView, RecipeItemListener listener) {
            super(itemView);
            mItemListener = listener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Recipe recipe = getItem(position);
            mItemListener.onRecipeClick(recipe);

        }
    }
}

