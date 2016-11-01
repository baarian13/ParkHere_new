package com.lazeebear.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lazeebear.parkhere.dummy.DummyContent;

/**
 * A fragment representing a single Spot detail screen.
 * This fragment is either contained in a {@link SpotListActivity}
 * in two-pane mode (on tablets) or a {@link SpotDetailActivity}
 * on handsets.
 */
public class SpotDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SpotDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.spot_detail, container, false);

        // Show the dummy content as text in a TextView.
        /*
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.spot_description)).setText(mItem.details);
        }*/

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.spot_detail, container, false);

        //return rootView;
    }

    Button button; //rate user button
    Button spot_back_button;
    String spot_price, spot_address, spot_owner_label;
    //Image spot_photos (takes src="")
    String spot_description, spot_owner_phone_number, spot_owner_rating, spot_date_range;
    String spotID;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        button = (Button) getView().findViewById(R.id.button);
        spot_back_button = (Button) getView().findViewById(R.id.spot_back_button);

        //get intent for specific spot
        Intent intent = getActivity().getIntent();
        spotID = intent.getStringExtra("id");
        //get information from the server about that specific spot

        //rate user button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddReview.class);
                //add extra info here later.
                intent.putExtra("id",spotID);
                getActivity().startActivity(intent);
            }
        });

        spot_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Search.class);
                intent.putExtra("id", spotID);
                getActivity().startActivity(intent);
            }
        });
    }
}
