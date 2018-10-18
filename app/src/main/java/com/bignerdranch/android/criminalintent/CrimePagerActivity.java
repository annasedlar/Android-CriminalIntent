package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button mJumpToFirst;
    private Button mJumpToLast;
//    private UUID mCrimeId;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.fragment_crime_detail, menu);
//        MenuItem subtitleItem = menu.findItem(R.id.delete_crime);
        return true;
    }

    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        switch (item.getItemId()) {
            case R.id.delete_crime:
                CrimeLab.getCrimeLab(this).deleteCrime(crimeId);
                Intent intent = CrimePagerActivity.newIntent(this, crimeId);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);


        mCrimes = CrimeLab.getCrimeLab(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mJumpToFirst = (Button) findViewById(R.id.jump_to_first_crime);
        mJumpToFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mViewPager.getCurrentItem() != 0) {
                    mViewPager.setCurrentItem(0);
                } else {
                    mJumpToFirst.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "You are already viewing first crime",
                            Toast.LENGTH_LONG).show();
                }
                mJumpToFirst.setEnabled(true);
            }
        });

        mJumpToLast = (Button) findViewById(R.id.jump_to_last_crime);
        mJumpToLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem() != mCrimes.size() - 1) {
                    mViewPager.setCurrentItem(mCrimes.size() - 1);
                } else {
                    mJumpToLast.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "You are already viewing last crime",
                            Toast.LENGTH_SHORT).show();
                }
                mJumpToLast.setEnabled(true);
            }

        });
    }
}