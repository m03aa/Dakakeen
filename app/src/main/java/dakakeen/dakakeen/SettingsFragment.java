package dakakeen.dakakeen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import dakakeen.dakakeen.Authentication.ChangePassword;
import dakakeen.dakakeen.Authentication.EditProfile;
import dakakeen.dakakeen.Communication.Communication;
import dakakeen.dakakeen.Communication.ResponseHandler;
import dakakeen.dakakeen.Enities.Account;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements ResponseHandler {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Communication communication;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListView settingsListView;
    private Account account;

    //i will delete this later
    String [] set = {"Edit profile","Change password"};


    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


            account=(Account) getArguments().getSerializable("account");
            Log.d("username",account.getUsername());
            communication=new Communication(getActivity().getApplicationContext());

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        /*
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,android.R.id.text1,set);
        Log.d("d",set[0]);
        */

        settingsListView = (ListView)view.findViewById(R.id.settingsList);
        Log.d("d","ddddddddddddddddddddd");

        //settingsListView.setAdapter(adapter);
        //Log.d("d","ddddddddddddddddddddd");

        settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                Intent i ;


                switch (position) {

                    case 0:
                        i = new Intent(getContext(),EditProfile.class);
                        i.putExtra("account",account);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(getContext(),ChangePassword.class);

                        i.putExtra("username",account.getUsername());
                        i.putExtra("password",account.getPassword());
                        startActivity(i);
                        break;
                    case 2:

                        new AlertDialog.Builder(getContext())
                                .setTitle(getString(R.string.logout))
                                .setMessage(getString(R.string.logout_message))
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {



                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        try {
                                            communication.get(communication.getUrl()+"/auth/logout",SettingsFragment.this);
                                            Log.d("communication","Succes");
                                            getActivity().finish();

                                            Toast.makeText(getContext(), getString(R.string.logout_successful), Toast.LENGTH_SHORT).show();
                                        }catch (Exception e){
                                            Log.e("error","faild to log out");

                                        }
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();


                }
             /* if(position==0){
                  Log.d("editprofile","selected");

              }
              if(position==1){
                  Log.d("changepassword","Selected");

              }
              Intent i = new Intent(getContext(), CreateOrder.class);
              startActivity(i);
              */

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

    @Override
    public void onSuccess(byte[] responseBody) {
        try {
            Toast.makeText(getContext(),communication.handelError(responseBody),Toast.LENGTH_LONG).show();
            JSONObject jsonObject = new JSONObject(new String(responseBody));


        }catch (Exception e){


        }

    }

    @Override
    public void onFailure(byte[] responseBody) {
        Toast.makeText(getContext(),communication.handelError(responseBody),Toast.LENGTH_LONG).show();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
