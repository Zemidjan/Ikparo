package bj.zemidjan.dio.activities;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import bj.zemidjan.dio.R;

public class OnboardingActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ImageButton mNextBtn;
    Button mFinishBtn;
    Button intro_btn_skip1;
    ImageView [] indicators;
    ImageView indicator0,indicator1,indicator2,indicator3;
    private ViewPager mViewPager;
    int page=0;
    ArgbEvaluator evaluator = new ArgbEvaluator();
    String nom, prenom, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        nom = preferences.getString("nom","");
        prenom = preferences.getString("prenom","");
        email = preferences.getString("email","");
        if (nom.equals("")) {

        }else{
            startActivity(new Intent(OnboardingActivity.this, OnboardingActivity.class));
            finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_onboarding);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        }*/
        indicator0=(ImageView) findViewById(R.id.intro_indicator_0);
        indicator1=(ImageView) findViewById(R.id.intro_indicator_1);
        indicator2=(ImageView) findViewById(R.id.intro_indicator_2);
        indicator3=(ImageView) findViewById(R.id.intro_indicator_3);

        indicators= new ImageView[]{indicator0,indicator1,indicator2,indicator3};
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        final int color1 = ContextCompat.getColor(this, R.color.color1);
        final int color2 = ContextCompat.getColor(this, R.color.color2);
        final int color3 = ContextCompat.getColor(this, R.color.slide_4);
        final int color4 = ContextCompat.getColor(this, R.color.color3);

        final int[] colorList = new int[]{color1, color2, color3,color4};
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mNextBtn =(ImageButton)  findViewById(R.id.intro_btn_next);
        mFinishBtn=(Button) findViewById(R.id.intro_btn_finish) ;
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page += 1;
                mViewPager.setCurrentItem(page, true);
            }
        });
        intro_btn_skip1 = (Button)  findViewById(R.id.intro_btn_skip);
        intro_btn_skip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), ConnexionActivity.class).putExtra("action","1"));
                finish();
                //Intent i=new Intent(OnboardingActivity.this,ConnexionActivity.class);
                //startActivity(i);
            }
        });





        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

         /*
         color update
          */
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position], colorList[position == 3 ? position : position + 1]);
                mViewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {

                page = position;
                updateIndicators(page);

                switch (position) {
                    case 0:
                        mViewPager.setBackgroundColor(color1);
                        break;
                    case 1:
                        mViewPager.setBackgroundColor(color2);
                        break;
                    case 2:
                        mViewPager.setBackgroundColor(color3);
                        break;
                    case 3:
                        mViewPager.setBackgroundColor(color4);
                        intro_btn_skip1.setTextColor(getResources().getColor(R.color.gris_5));
                        mFinishBtn.setTextColor(getResources().getColor(R.color.gris_5));
                        break;

                }

                mNextBtn.setVisibility(position == 3 ? View.GONE : View.VISIBLE);
                mFinishBtn.setVisibility(position == 3 ? View.VISIBLE : View.GONE);


            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(v.getContext(), ConnexionActivity.class).putExtra("action","1"));
                finish();

            }
        });
    }


    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_onboarding, menu);
        return true;
    }

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_onboarding, container, false);
            ImageView image_fragment= (ImageView) rootView.findViewById(R.id.image1);
            TextView titre=(TextView) rootView.findViewById(R.id.titre_description);
            TextView soustitre=(TextView) rootView.findViewById(R.id.soustitre_description);

            int[] images = new int[]{R.mipmap.image1,R.mipmap.image2,R.mipmap.motivation,R.mipmap.image3};
            if(getArguments().getInt(ARG_SECTION_NUMBER)==1)
            {
                titre.setText("Bienvenue sur EducArsenal.");
                soustitre.setText("L'Arsenal de cours en ligne pour booster votre carrière.");
                image_fragment.setBackgroundResource(images[0]);
            }
                else if(getArguments().getInt(ARG_SECTION_NUMBER)==2) {
                titre.setText("Garantissez-vous un emploi!");
                soustitre.setText("EducArsenal vous propose des cours en ligne avec certifications.");
                image_fragment.setBackgroundResource(images[1]);

            }  else if(getArguments().getInt(ARG_SECTION_NUMBER)==3)
            {
                titre.setText("Inspiration");
                soustitre.setText("EducArsenal vous fait découvrir des personnalités ayant excélées dans votre domaine.");
                image_fragment.setBackgroundResource(images[2]);
            }
            else {
                titre.setText("Forum d'échanges");
                soustitre.setText("Discutez avec les abonnés du même domaine d'étude que vous.");
                image_fragment.setBackgroundResource(images[3]);
            }
            return rootView;
        }
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
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return  "SECTION 4";
            }
            return null;
        }
    }
}
