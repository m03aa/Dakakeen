package dakakeen.dakakeen.MyOrders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Enities.Offer;
import dakakeen.dakakeen.Enities.Order;
import dakakeen.dakakeen.R;
import dakakeen.dakakeen.Communication.ResponseHandler;


public class ViewMyOrders extends Fragment implements ResponseHandler {

    private OnFragmentInteractionListener mListener;


    private ListView ordersList;
    private Spinner orderState;
    private Button createOrder;

    public static ArrayList<Order> orders = new ArrayList<>();
    public static ArrayList<Order> acceptedOrders =  new ArrayList<>();
    public static ArrayList<Order> deliveredOrders =  new ArrayList<>();
    public static ArrayList<Order> onRouteOrders = new ArrayList<>();

    public static ArrayAdapter<Order> adapter;
    private String username;

    private Communication communication;

    public ViewMyOrders() {
        // Required empty public constructor
    }

    public static ViewMyOrders newInstance(String param1, String param2) {
        ViewMyOrders fragment = new ViewMyOrders();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        communication = new Communication(getContext());
    }

    //to refresh the orders list continuously
    @Override
    public void onResume() {
        super.onResume();
        updateOrdersList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_my_orders, container, false);

        // عشان اروح للاكتفيتي من الفراقمنت
        createOrder = (Button) v.findViewById(R.id.createOrderButton);
        createOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),CreateOrder.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        ordersList = (ListView) v.findViewById(R.id.ordersList);
        orderState = (Spinner) v.findViewById(R.id.orderStateSpinner);

        //to pass an order from ordersList to viewOrderDetails
        ordersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                Intent intent;
                if (orderState.getSelectedItemPosition() == 0) {
                    intent = new Intent(getContext(), ViewOrderDetails.class);
                    intent.putExtra("order", orders.get(position));

                } else{
                    intent = new Intent(getContext(),ViewClosedOrderDetails.class);
                    if (orderState.getSelectedItemPosition() == 1)
                        intent.putExtra("order",acceptedOrders.get(position));
                    else if (orderState.getSelectedItemPosition() == 2)
                        intent.putExtra("order",deliveredOrders.get(position));
                    else if (orderState.getSelectedItemPosition() == 3)
                        intent.putExtra("order",onRouteOrders.get(position));
                }

                startActivity(intent);
            }
        });


        return  v ;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    //request the orders list from the server
    public void updateOrdersList(){
        orders.clear();
        acceptedOrders.clear();
        deliveredOrders.clear();
        onRouteOrders.clear();

        orderState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0){
                    adapter = new ArrayAdapter<Order>(getContext(),R.layout.item,
                            android.R.id.text1, orders);
                }
                else if (position == 1){
                    adapter = new ArrayAdapter<Order>(getContext(),R.layout.item,
                            android.R.id.text1, acceptedOrders);
                }
                else if(position == 2){
                    adapter = new ArrayAdapter<Order>(getContext(),R.layout.item,
                            android.R.id.text1, deliveredOrders);
                }
                else if (position == 3){
                    adapter = new ArrayAdapter<Order>(getContext(),R.layout.item,
                            android.R.id.text1, onRouteOrders);
                }

                ordersList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //then nothing happen
            }
        });

        try {
            communication.get(communication.getUrl() + "/myorders/", this);
        }
        catch (Exception e){
            Log.e("Communication Exception", e.getMessage());
        }

    }

    @Override
    public void onSuccess(byte[] responseBody){
        try {
            JSONArray jsonArray = new JSONArray(new String(responseBody));
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Order order = new Order();

                order.setId(jsonObject.getString("_id"));
                order.setUsername(username);
                order.setTitle(jsonObject.getString("title"));
                order.setState(jsonObject.getInt("state"));

                if (jsonObject.getInt("state") == 0)
                    orders.add(order);
                else{
                    JSONObject offerObject = jsonObject.getJSONObject("offer");
                    Offer offer = new Offer();

                    offer.setId(offerObject.getString("_id"));
                    offer.setState(offerObject.getInt("state"));
                    order.setOffer(offer);

                    if (offer.getState() == 2){
                        acceptedOrders.add(order);
                    }
                    else if (offer.getState() == 3){
                        deliveredOrders.add(order);
                    }
                    else if (offer.getState() == 4){
                        onRouteOrders.add(order);
                    }

                }
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailure(byte[] responseBody) {
        Toast.makeText(getContext(),communication.handelError(responseBody),Toast.LENGTH_SHORT).show();
    }
}
