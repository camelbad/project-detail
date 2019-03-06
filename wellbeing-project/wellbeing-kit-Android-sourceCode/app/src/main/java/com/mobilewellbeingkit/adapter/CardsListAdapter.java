package com.mobilewellbeingkit.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.*;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mobilewellbeingkit.R;
import com.mobilewellbeingkit.helper.DatabaseHelper;
import com.mobilewellbeingkit.view.CardsActivity;
import com.mobilewellbeingkit.view.ViewCardActivity;
import com.mobilewellbeingkit.model.Card;

import java.util.List;


     /**
     * adapter for recyclerView of cards view
	 *
	 *
     */
public class CardsListAdapter extends RecyclerView.Adapter<CardsListAdapter.MyViewHolder> {

    private Context context;
    private List<Card> sCards;
    private DatabaseHelper db;
    private boolean showAsGrid;

    public CardsListAdapter(Context context, List<Card> cards, boolean gridView) {
        this.context = context;
        this.sCards = cards;
        db = new DatabaseHelper(context);
        showAsGrid = gridView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(context);

        if(showAsGrid) {
            view = mInflater.inflate(R.layout.cardview_wellbeing_card, parent, false);
        }
        else {
            view = mInflater.inflate(R.layout.cardview_wellbeing_card_grid, parent, false);
        }
        return new MyViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.bind(position, holder);
    }



    @Override
    public int getItemCount() {
        return sCards.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView cardImage;
        CardView cardView;
        ToggleButton fav;

        private MyViewHolder(View view) {
            super(view);

            cardImage = (ImageView) view.findViewById(R.id.card_img);
            cardView = (CardView) view.findViewById(R.id.cardview_id);
            fav = (ToggleButton) view.findViewById(R.id.toggleFavourite);
            fav.setText(null);
            fav.setTextOff(null);
            fav.setTextOn(null);

            // set click listener for card
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    int pos = getAdapterPosition();
                    Log.d("MyViewHolder", String.valueOf(pos));


                    Intent i = new Intent(context, ViewCardActivity.class);
                    i.putExtra("cardId", sCards.get(getAdapterPosition()).getCardId());
                    i.putExtra("cardName", sCards.get(getAdapterPosition()).getCardName());
                    i.putExtra("cardImage", sCards.get(getAdapterPosition()).getCardImage());
                    i.putExtra("cardNote", sCards.get(getAdapterPosition()).getCardNote());
                    context.startActivity(i);

                }
            });

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    // Create dialog menu with delete and duplicate card options
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Card Options");

                    // Add the duplicate button
                    builder.setNeutralButton("Duplicate", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked duplicate button
                            AlertDialog.Builder duplicateBuilder = new AlertDialog.Builder(context);
                            duplicateBuilder.setTitle("Duplicate This Card?");

                            duplicateBuilder.setPositiveButton("Duplicate", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    // Copy card name and image to new duplicate card object.
                                    Card dupCard_temp=new Card(sCards.get(getAdapterPosition()).getCardName(),
                                            "",
                                            0,
                                            1,
                                            sCards.get(getAdapterPosition()).getCardImage());

                                    db.createCard(dupCard_temp);

                                    Intent i = new Intent(context, CardsActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    context.startActivity(i);
                                }
                            });
                            duplicateBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            AlertDialog duplicateDialog = duplicateBuilder.create();
                            duplicateDialog.show();
                        }
                    });
					  /**
						* delete card by long press
						*
						*
						*/
                    builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(context);
                            deleteBuilder.setTitle("Delete this card?");
                            deleteBuilder.setMessage("The card will be permanently removed.");
                            //avoid user delete original card
                            deleteBuilder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    if((sCards.get(getAdapterPosition()).getCardName().equals("achieve something")
                                            ||sCards.get(getAdapterPosition()).getCardName().equals("eat the good stuff")
                                            ||sCards.get(getAdapterPosition()).getCardName().equals("embrace the beauty in nature")
                                            ||sCards.get(getAdapterPosition()).getCardName().equals("find a furry friend")
                                            ||sCards.get(getAdapterPosition()).getCardName().equals("find comfort")
                                            ||sCards.get(getAdapterPosition()).getCardName().equals("find my safe place")
                                            ||sCards.get(getAdapterPosition()).getCardName().equals("get some help")
                                            ||sCards.get(getAdapterPosition()).getCardName().equals("have a good sleep routine")
                                            ||sCards.get(getAdapterPosition()).getCardName().equals("have a laugh")
                                            ||sCards.get(getAdapterPosition()).getCardName().equals("just breathe")
                                            ||sCards.get(getAdapterPosition()).getCardName().equals("keep moving")
                                            ||sCards.get(getAdapterPosition()).getCardName().equals("take care of myself")
                                            ||sCards.get(getAdapterPosition()).getCardName().equals("take time out for me")
                                            ) && sCards.get(getAdapterPosition()).getDuplicate() != 1){
                                        Toast.makeText(context,"Sorry, you can't delete default cards.",Toast.LENGTH_LONG).show();
                                    }else {
                                        Log.d("ListAdapter", "deleting Card...");
                                        db.delete_Image_By_id((int)sCards.get(getAdapterPosition()).getCardId());

                                        Intent i = new Intent(context, CardsActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        context.startActivity(i);
                                    }
                                }
                            });

                            deleteBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            final AlertDialog deleteDialog = deleteBuilder.create();
                            deleteDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface arg0) {
                                    deleteDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                                }
                            });
                            deleteDialog.show();
                        }
                    });

                    // Get the AlertDialog from create() and show it
                    final AlertDialog dialog = builder.create();
                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                        }
                    });
                    dialog.show();

                    return true;
                }
            });


            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    int adapterPosition = getAdapterPosition();
                    if (sCards.get(adapterPosition).getFavourite() == 1) {
                        fav.setChecked(false);
                        sCards.get(adapterPosition).setFavourite(0);
                        db.updateCardFav(sCards.get(adapterPosition));
                    } else {
                        fav.setChecked(true);
                        sCards.get(adapterPosition).setFavourite(1);
                        db.updateCardFav(sCards.get(adapterPosition));
                    }
                }
            });

        }


        private void bind(int position, MyViewHolder holder) { //added in holder as a parameter to this function as it is needed by Glide
            if (sCards.get(position).getFavourite() == 1) {
                fav.setChecked(true);
            } else {
                fav.setChecked(false);
            }

            // I'll be honest, I'm not 100% sure how this is working, but so far as I can tell, it is
            Card cards = sCards.get(position);
            Glide.with(context).load(cards.getCardImage())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.cardImage);
        }

        /**
         * Insert card into local array and notify recyclerview adapter of a change in dataset
         * @param card Card to be added
         */
        public void addItem(Card card) {
            sCards.add(card);
            notifyItemInserted(sCards.size());
        }
    }
}
