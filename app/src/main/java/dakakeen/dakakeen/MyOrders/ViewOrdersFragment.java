package dakakeen.dakakeen.MyOrders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import dakakeen.dakakeen.Enities.Order;
import dakakeen.dakakeen.R;
import dakakeen.dakakeen.Communication.ResponseHandler;



public class ViewOrdersFragment extends Fragment implements ResponseHandler {

    private OnFragmentInteractionListener mListener;


    private FloatingActionButton FAB;
    private ListView ordersList;
    private Spinner orderState;

    public static ArrayList<Order> orders = new ArrayList<>();
    public static ArrayList<Order> closedOrders = new ArrayList<>();
    public static ArrayAdapter<Order> adapter;
    private String username;

    private Communication communication;

    public ViewOrdersFragment() {
        // Required empty public constructor
    }

    public static ViewOrdersFragment newInstance(String param1, String param2) {
        ViewOrdersFragment fragment = new ViewOrdersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }

        communication = new Communication();
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
        View v = inflater.inflate(R.layout.fragment_view_orders, container, false);

        // عشان اروح للاكتفيتي من الفراقمنت
        FAB = (FloatingActionButton)v.findViewById(R.id.createOrderFAB);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),CreateOrder.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        ordersList = (ListView) v.findViewById(R.id.ordersList);
        //to pass an order from ordersList to viewOrderDetails
        ordersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                Intent intent = new Intent(getContext(),ViewOrderDetails.class);
                intent.putExtra("order",orders.get(position));
                startActivity(intent);
            }
        });


        orderState = (Spinner) v.findViewById(R.id.orderStateSpinner);
        //to change the list view content based on the order state
        orderState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0){
                    adapter = new ArrayAdapter<Order>(getContext(),android.R.layout.simple_list_item_1,
                            android.R.id.text1, orders);
                    ordersList.setAdapter(adapter);
                }
                else {
                    adapter = new ArrayAdapter<Order>(getContext(),android.R.layout.simple_list_item_1,
                            android.R.id.text1, closedOrders);
                    ordersList.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //then nothing happen
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
        adapter = new ArrayAdapter<Order>(getContext(),android.R.layout.simple_list_item_1,
                android.R.id.text1, orders);
        ordersList.setAdapter(adapter);

        try {
            communication.get(communication.getUrl() + "/myorders/" + username, this);
        }
        catch (Exception e){
            //Toast.makeText(getContext(),R.string.no_connection,Toast.LENGTH_SHORT).show();
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
                else
                    closedOrders.add(order);


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
