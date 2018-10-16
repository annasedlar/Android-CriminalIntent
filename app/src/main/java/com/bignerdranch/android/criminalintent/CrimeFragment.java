package com.bignerdranch.android.criminalintent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.List;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    public static final String ARG_CRIME_ID = "crime_id";

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private List<Crime> mCrimes;
    private Button mJumpToFirst;
    private Button mJumpToLast;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mCrime = new Crime();
//        UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.getCrimeLab(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });


        final ViewPager vp = (ViewPager) getActivity().findViewById(R.id.crime_view_pager);
        mCrimes = CrimeLab.getCrimeLab(getActivity()).getCrimes();

        mJumpToFirst = (Button) v.findViewById(R.id.jump_to_first_crime);
        mJumpToFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(vp.getCurrentItem() != 0){
                    vp.setCurrentItem(0);
                } else {
                    mJumpToFirst.setEnabled(false);
                }
            }
        });

        mJumpToLast = (Button) v.findViewById(R.id.jump_to_last_crime);
        mJumpToLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vp.getCurrentItem() != (mCrimes.size() - 1) ) {
                    vp.setCurrentItem(mCrimes.size() - 1);
                } else {
                    mJumpToLast.setEnabled(false);
                }
            }
        });

        return v;
    }
}
