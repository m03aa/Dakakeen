package dakakeen.dakakeen;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import dakakeen.dakakeen.CustomerFunctions.ViewTopProvidersFragment;
import dakakeen.dakakeen.Enities.Account;
import dakakeen.dakakeen.MyOrders.ViewMyOrders;
import dakakeen.dakakeen.ProviderFunctions.ViewOrdersCategoriesFragment;
import dakakeen.dakakeen.MyOffers.ViewMyOffers;

//i added the implements part
public class TabsActivity extends AppCompatActivity implements android.app.ActionBar.TabListener,
        ViewMyOrders.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //i created new action bar object
    private android.app.ActionBar actionBar;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        account = (Account) getIntent().getSerializableExtra("account");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //create new tab layout object
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        try{
            // i initialized the action bar object
            actionBar= getActionBar();
            actionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_TABS);
        }
        catch (Exception e){
            Log.i("Exception",e.getMessage());
        }



    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabs, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //i generated this three methods onTab selected, unselected, Reselected
    @Override
    public void onTabSelected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            Bundle bundle = new Bundle();

            if (account.getRole() == 1){
                switch (position){
                    case 0:
                        ViewMyOrders orders = new ViewMyOrders();
                        bundle.putString("username",account.getUsername());
                        orders.setArguments(bundle);
                        return orders;
                    case 1:
                        return new ViewTopProvidersFragment();
                    case 2:
                        SettingsFragment settings = new SettingsFragment();
                        bundle.putSerializable("account",account);
                        settings.setArguments(bundle);
                        return settings;
                    default:
                        return null;
                }
            }
            else {
                switch (position){
                    case 0:
                        return new ViewOrdersCategoriesFragment();
                    case 1:
                        return new ViewMyOffers();
                    case 2:
                        return new SettingsFragment();
                    default:
                        return null;
                }
            }

        }

        @Override
        public int getCount() {
                return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(account.getRole() == 1){
                switch (position) {
                    case 0:
                        return getString(R.string.orders);
                    case 1:
                        return getString(R.string.top_providers);
                    case 2:
                        return getString(R.string.settings);
                }
            }
            else {
                switch (position) {
                    case 0:
                        return getString(R.string.category);
                    case 1:
                        return getString(R.string.my_offers);
                    case 2:
                        return getString(R.string.settings);
                }
            }
            return null;
        }
    }
}
