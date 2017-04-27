package dakakeen.dakakeen.CustomAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import dakakeen.dakakeen.Enities.Offer;
import dakakeen.dakakeen.R;

/**
 * Created by moath on 4/24/2017.
 */

public class CustomOfferAdapter extends ArrayAdapter<Offer> {

    private Context context;
    private ArrayList<Offer> offers;

    public CustomOfferAdapter(Context context, ArrayList<Offer> offers){
        super(context, R.layout.customadapter);

        this.context = context;
        this.offers = offers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.customadapter,parent,false);

        TextView textView1 = (TextView)view.findViewById(R.id.customAdapterText1);
        TextView textView2 = (TextView)view.findViewById(R.id.customAdapterText2);

        textView1.setText(offers.get(position).getProvider().getName());
        textView2.setText(Double.toString(offers.get(position).getPrice()));

        return super.getView(position, convertView, parent);
    }
}
