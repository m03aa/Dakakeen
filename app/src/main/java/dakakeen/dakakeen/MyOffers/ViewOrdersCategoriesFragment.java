package dakakeen.dakakeen.MyOffers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import dakakeen.dakakeen.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewOrdersCategoriesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewOrdersCategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewOrdersCategoriesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ImageView food,handcrafts, fashion, accessories, paintings;
    private Intent intent;

    public ViewOrdersCategoriesFragment() {
        // Required empty public constructor
    }


    public static ViewOrdersCategoriesFragment newInstance() {
        ViewOrdersCategoriesFragment fragment = new ViewOrdersCategoriesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_view_orders_categories, container, false);
        intent = new Intent(getContext(),ViewOrdersInCategory.class);

        //to get customer orders in food category
        food = (ImageView)v.findViewById(R.id.food);
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category",0);
                startActivity(intent);
            }
        });

        //to get customer orders in handcrafts category
        handcrafts = (ImageView)v.findViewById(R.id.handcrafts);
        handcrafts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category",1);
                startActivity(intent);
            }
        });

        //to get customer orders in fashion category
        fashion = (ImageView)v.findViewById(R.id.fashion);
        fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category",2);
                startActivity(intent);
            }
        });

        //to get customer orders in accessory category
        accessories = (ImageView)v.findViewById(R.id.accessory);
        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category",3);
                startActivity(intent);
            }
        });

        //to get customer orders in painting category
        paintings = (ImageView)v.findViewById(R.id.painting);
        paintings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("category",4);
                startActivity(intent);
            }
        });

         return v;
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
