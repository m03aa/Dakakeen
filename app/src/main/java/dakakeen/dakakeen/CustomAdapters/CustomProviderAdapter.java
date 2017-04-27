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

import dakakeen.dakakeen.Enities.Provider;
import dakakeen.dakakeen.R;

/**
 * Created by moath on 4/24/2017.
 */

public class CustomProviderAdapter extends ArrayAdapter<Provider> {

    private Context context;
    private ArrayList<Provider> providers;


    public CustomProviderAdapter(Context context, ArrayList<Provider> providers) {
        super(context, R.layout.customadapter);

        this.context = context;
        this.providers = providers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.customadapter,parent,false);

        TextView textView1 = (TextView) view.findViewById(R.id.customAdapterText1);
        TextView textView2 = (TextView) view.findViewById(R.id.customAdapterText2);

        textView1.setText(providers.get(position).getName());
        textView2.setText(Double.toString(providers.get(position).getAverageRating()));

        return super.getView(position, convertView, parent);
    }
}
