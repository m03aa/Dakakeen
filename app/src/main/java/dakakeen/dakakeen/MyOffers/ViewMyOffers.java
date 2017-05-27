package dakakeen.dakakeen.MyOffers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Offer;
import dakakeen.dakakeen.R;


public class ViewMyOffers extends Fragment implements ResponseHandler {

    private OnFragmentInteractionListener mListener;

    private Spinner stateSpinner;
    private ListView myOffersList;
    private ArrayAdapter<Offer> adapter;

    private ArrayList<Offer> activeOffers = new ArrayList<>();
    private ArrayList<Offer> closedOffers = new ArrayList<>();
    private ArrayList<Offer> acceptedOffers = new ArrayList<>();
    private ArrayList<Offer> deliveredOffers = new ArrayList<>();
    private ArrayList<Offer> onrouteOffers = new ArrayList<>();

    private Communication communication;


    public ViewMyOffers() {
        // Required empty public constructor
    }

    public static ViewMyOffers newInstance(String param1, String param2) {
        ViewMyOffers fragment = new ViewMyOffers();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        communication = new Communication(getContext());
    }

    @Override
    public void onResume(){
        super.onResume();
        updateOffersList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_my_offers, container, false);


        myOffersList = (ListView)view.findViewById(R.id.MyoffersList);
        stateSpinner = (Spinner)view.findViewById(R.id.offerStateSٍpinner);

        myOffersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getContext(), ViewMyOfferDetails.class);
                intent.putExtra("state",stateSpinner.getSelectedItemPosition());

                switch (stateSpinner.getSelectedItemPosition()){
                    case 0:
                        intent.putExtra("offer", activeOffers.get(position));
                        break;
                    case 1:
                        intent.putExtra("offer", closedOffers.get(position));
                        break;
                    case 2:
                        intent.putExtra("offer", acceptedOffers.get(position));
                        break;
                    case 3:
                        intent.putExtra("offer", deliveredOffers.get(position));
                        break;
                    case 4:
                        intent.putExtra("offer", onrouteOffers.get(position));
                        break;
                }
                startActivity(intent);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void updateOffersList(){
        activeOffers.clear();
        closedOffers.clear();
        acceptedOffers.clear();
        onrouteOffers.clear();
        deliveredOffers.clear();

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        adapter= new ArrayAdapter<Offer>(getContext(),R.layout.item,
                                android.R.id.text1,activeOffers);
                        myOffersList.setAdapter(adapter);
                        break;
                    case 1:
                        adapter= new ArrayAdapter<Offer>(getContext(),R.layout.item,
                                android.R.id.text1,closedOffers);
                        myOffersList.setAdapter(adapter);
                        break;
                    case 2:
                        adapter= new ArrayAdapter<Offer>(getContext(),R.layout.item,
                                android.R.id.text1,acceptedOffers);
                        myOffersList.setAdapter(adapter);
                        break;
                    case 3:
                        adapter= new ArrayAdapter<Offer>(getContext(),R.layout.item,
                                android.R.id.text1,deliveredOffers);
                        myOffersList.setAdapter(adapter);
                        break;
                    case 4:
                        adapter= new ArrayAdapter<Offer>(getContext(),R.layout.item,
                                android.R.id.text1,onrouteOffers);
                        myOffersList.setAdapter(adapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // really!! then nothing will happen
            }
        });

        try {
            communication.get(communication.getUrl()+"/myoffers",this);
        } catch (Exception e){
            Log.d("Communication Exception",e.getMessage());
        }
    }

    @Override
    public void onSuccess(byte[] responseBody){
        Log.d("onSuccess", new String(responseBody));

        try {
            JSONArray jsonArray = new JSONArray(new String(responseBody));

            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Offer offer = new Offer();

                offer.setId(jsonObject.getString("_id"));
                offer.getProvider().setName("providerUsername");
                offer.setPrice(jsonObject.getDouble("price"));
                offer.setState(jsonObject.getInt("state"));

                offer.order.setTitle(jsonObject.getJSONArray("orderId").getJSONObject(0).getString("title"));

                int state = jsonObject.getInt("state");
                switch (state){
                    case 0:
                        activeOffers.add(offer);
                        break;
                    case 1:
                        closedOffers.add(offer);
                        break;
                    case 2:
                        acceptedOffers.add(offer);
                        break;
                    case 3:
                        deliveredOffers.add(offer);
                        break;
                    case 4:
                        onrouteOffers.add(offer);
                        break;
                }

                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(byte[] responseBody){
        Log.d("onFailure!!", new String(responseBody));
        //Toast.makeText(getContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }
}
