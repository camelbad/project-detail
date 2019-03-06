package com.mobilewellbeingkit.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mobilewellbeingkit.R;
import com.mobilewellbeingkit.adapter.ExpandListAdapter;

import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Intent;
import android.view.View;

public class ExpandableListActivity extends AppCompatActivity {

    private ExpandableListView listView;
    private ExpandListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_bank);

        //set list view to their id listener's
        listView = (ExpandableListView)findViewById(R.id.lvExp);

        initData();
        listAdapter = new ExpandListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter);

        /*
        * the list would be expandable, and the child of the expandable list could be typed and choose.
        * */
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {


                String childName = listAdapter.getChild(groupPosition, childPosition).toString();

                Intent intent = new Intent(ExpandableListActivity.this, JournalEntryActivity.class);

                intent.putExtra("emotion", childName);

                setResult(1,intent);
                finish();

                return true;

            }
        });




    }




    // initiate the data and input the special emotion in the emotion bank.
    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        //Populate listDataHeaderArray
        listDataHeader.add("Happy");
        listDataHeader.add("Sad");
        listDataHeader.add("Angry");
        listDataHeader.add("Fear");

        // typed in listHash.
        List<String> happyEmotions = new ArrayList<>();
        happyEmotions.add("Happy");
        happyEmotions.add("Accepted");
        happyEmotions.add("Calm");
        happyEmotions.add("Capable");
        happyEmotions.add("Certain");
        happyEmotions.add("Cheerful");
        happyEmotions.add("Clear");
        happyEmotions.add("Comfortable");
        happyEmotions.add("Complete");
        happyEmotions.add("Confident");
        happyEmotions.add("Content");
        happyEmotions.add("Delighted");
        happyEmotions.add("Determined");
        happyEmotions.add("Ecstatic");
        happyEmotions.add("Encouraged");
        happyEmotions.add("Energetic");
        happyEmotions.add("Excited");
        happyEmotions.add("Fantastic");
        happyEmotions.add("Free");
        happyEmotions.add("Generous");
        happyEmotions.add("Grateful");
        happyEmotions.add("Healthy");
        happyEmotions.add("Heard");
        happyEmotions.add("Hopeful");
        happyEmotions.add("Interested");
        happyEmotions.add("Invigorated");
        happyEmotions.add("Joyful");
        happyEmotions.add("Kind");
        happyEmotions.add("Knowledgeable");
        happyEmotions.add("Light-hearted");
        happyEmotions.add("Loved");
        happyEmotions.add("Optimistic");
        happyEmotions.add("Passionate");
        happyEmotions.add("Peaceful");
        happyEmotions.add("Playful");
        happyEmotions.add("Refreshed");
        happyEmotions.add("Relaxed");
        happyEmotions.add("Resourceful");
        happyEmotions.add("Self-confident");
        happyEmotions.add("Successful");
        happyEmotions.add("Surprised");
        happyEmotions.add("Terrific");
        happyEmotions.add("Thankful");
        happyEmotions.add("Valued");
        happyEmotions.add("Zany");


        // typed in listHash.
        List<String> sadEmotions = new ArrayList<>();
        sadEmotions.add("Sad");
        sadEmotions.add("Abandoned");
        sadEmotions.add("Bored");
        sadEmotions.add("Broken");
        sadEmotions.add("Crushed");
        sadEmotions.add("Defeated");
        sadEmotions.add("Depressed");
        sadEmotions.add("Deserted");
        sadEmotions.add("Disappointed");
        sadEmotions.add("Disconnected");
        sadEmotions.add("Discouraged");
        sadEmotions.add("Drained");
        sadEmotions.add("Excluded");
        sadEmotions.add("Fatigued");
        sadEmotions.add("Forgotten");
        sadEmotions.add("Grieving");
        sadEmotions.add("Heartbroken");
        sadEmotions.add("Helpless");
        sadEmotions.add("Hopeless");
        sadEmotions.add("Ignored");
        sadEmotions.add("Inferior");
        sadEmotions.add("Isolated");
        sadEmotions.add("Lonely");
        sadEmotions.add("Melancholy");
        sadEmotions.add("Numb");
        sadEmotions.add("Tearful");
        sadEmotions.add("Troubled");
        sadEmotions.add("Unappreciated");

        // typed in listHash.
        List<String> angryEmotions = new ArrayList<>();
        angryEmotions.add("Angry");
        angryEmotions.add("Agitated");
        angryEmotions.add("Belittled");
        angryEmotions.add("Betrayed");
        angryEmotions.add("Bewildered");
        angryEmotions.add("Bitter");
        angryEmotions.add("Cross");
        angryEmotions.add("Defiant");
        angryEmotions.add("Disturbed");
        angryEmotions.add("Envious");
        angryEmotions.add("Evil");
        angryEmotions.add("Furious");
        angryEmotions.add("Grumpy");
        angryEmotions.add("Humiliated");
        angryEmotions.add("Hurt");
        angryEmotions.add("Insulted");
        angryEmotions.add("Jealous");
        angryEmotions.add("Mad");
        angryEmotions.add("Offended");
        angryEmotions.add("Shaken");
        angryEmotions.add("Violated");
        angryEmotions.add("Wounded");

        List<String> fearEmotions = new ArrayList<>();
        fearEmotions.add("Afraid");
        fearEmotions.add("Anxious");
        fearEmotions.add("Apologetic");
        fearEmotions.add("Ashamed");
        fearEmotions.add("Awkward");
        fearEmotions.add("Burdened");
        fearEmotions.add("Concerned");
        fearEmotions.add("Confused");
        fearEmotions.add("Distressed");
        fearEmotions.add("Embarrassed");
        fearEmotions.add("Exposed");
        fearEmotions.add("Fearful");
        fearEmotions.add("Flustered");
        fearEmotions.add("Frightened");
        fearEmotions.add("Guilty");
        fearEmotions.add("Indecisive");
        fearEmotions.add("Nervous");
        fearEmotions.add("Self-conscious");
        fearEmotions.add("Sorry");
        fearEmotions.add("Trapped");
        fearEmotions.add("Worried");

        //strutured the list.
        listHash.put(listDataHeader.get(0),happyEmotions);
        listHash.put(listDataHeader.get(1),sadEmotions);
        listHash.put(listDataHeader.get(2),angryEmotions);
        listHash.put(listDataHeader.get(3),fearEmotions);
    }
}

