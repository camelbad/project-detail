package com.mobilewellbeingkit.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilewellbeingkit.R;

public class TutorialImageAdapter extends PagerAdapter {

    //Array of image resources being used for the tutorial system
    private int[] imageResources = {R.drawable.card_screen, R.drawable.card_screen_menu, R.drawable.journal_screen, R.drawable.helpful_contacts_screen, R.drawable.settings_screen};

    //Declarations for context and layoutInflator variables needed through the class
    private Context context;
    private LayoutInflater layoutInflater;

    /**
     * Class constructor to be accessed in TutorialActivity.java
     * @param context Context of the current view
     */
    public TutorialImageAdapter(Context context){
        this.context = context;
    }

    /**
     * Returns the length of the array of image resources to set the number of items in ViewPager
     * @return int
     */
    @Override
    public int getCount(){
        return imageResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o){
        return view == o;
    }

    /**
     * This method instantiates the viewpager
     * @param container ViewGroup that contains the viewpager
     * @param position integer position of the item
     * @return Object
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.tutorial_screens, null);
        ImageView imageView = (ImageView)itemView.findViewById(R.id.image_view);
        //Text for information about images
        TextView textView = (TextView)itemView.findViewById(R.id.image_title);
        //Text for hint to swipe/press back
        TextView hintView = (TextView)itemView.findViewById(R.id.screen_hint);
        imageView.setImageResource(imageResources[position]);
        ViewPager vp = (ViewPager) container;
        vp.addView(itemView, 0);

        //Makes viewPager show correct text for the tutorial image being viewed
        String tutorialText = "";
        String hint = "";
        switch(position){
            case 0: tutorialText = "This is the <b>Cards</b> screen <br><br>Tap a card to turn it over and write on the other side <br><br>Long press a card to duplicate or delete it";
//context.getResources().getString(R.string.tutorial_case_0);
                    hint = "Swipe left for more";
                break;
            case 1: tutorialText = "Select the button in the bottom left to open the <b>menu</b> <br><br>Select it again to close it";
                hint = "Swipe left for more";
                break;
            case 2: tutorialText = "This is the <b>Journal</b> screen <br><br>Select an entry to view or edit it <br><br>Long press an entry to delete it <br><br>Tap the <b>Filter</b> button to only see entries of a certain category";
                hint = "Swipe left for more";
                break;
            case 3: tutorialText = "This is the <b>Helpful Contacts</b> screen <br><br>Tap a phone number to call it <br><br>Tap a URL to open it in browser <br><br>Edit or delete your links by long pressing them";
                hint = "Swipe left for more";
                break;
            case 4: tutorialText = "This is the <b>Settings</b> screen <br><br>See more information on the <b>About the Kit</b> page <br><br>View this tutorial again by selecting <b>How to Use this Kit</b>";
                hint = "Press back to return to the application";
                break;
            default: tutorialText = "Error";
                break;
        }
        textView.setText(Html.fromHtml(tutorialText));
        hintView.setText(hint);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }


}
