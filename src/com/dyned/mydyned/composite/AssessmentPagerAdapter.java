package com.dyned.mydyned.composite;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.dyned.mydyned.fragment.AssessmentConfirmationFragment;
import com.dyned.mydyned.fragment.AssessmentFragment;
import com.dyned.mydyned.model.Assessment;

public class AssessmentPagerAdapter extends FragmentStatePagerAdapter {

	private List<Assessment> assessments;
	private SparseArray<SherlockFragment> registeredFragments;
	
	public AssessmentPagerAdapter(FragmentManager fm, List<Assessment> assessments) {
		super(fm);
		this.assessments = assessments;
		registeredFragments = new SparseArray<SherlockFragment>();		
		if (assessments == null) {
			assessments = new ArrayList<Assessment>();
		}
	}

	@Override
	public int getCount() {
		return assessments.size() + 1;
	}

	@Override
	public SherlockFragment getItem(int position)
	{
		if (position < assessments.size()) {
			AssessmentFragment fragment = AssessmentFragment.newInstance(assessments.get(position), position, getCount());
			registeredFragments.put(position, fragment);
			return fragment;
		} else {
			AssessmentConfirmationFragment fragment = AssessmentConfirmationFragment.newInstance();
			registeredFragments.put(position, fragment);
			return fragment;
		}
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		registeredFragments.remove(position);
        super.destroyItem(container, position, object);
	}
	
	public SherlockFragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
