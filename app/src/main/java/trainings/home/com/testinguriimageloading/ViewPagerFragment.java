package trainings.home.com.testinguriimageloading;

import android.app.Fragment;

import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import android.util.TypedValue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewPagerFragment extends Fragment {

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Bind(R.id.viewPager_tabs_layout)
    PagerTabStrip viewPagerTabs;

    public ViewPagerFragment() { }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0, true);

        viewPagerTabs.setTabIndicatorColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        viewPagerTabs.setDrawFullUnderline(false);
        viewPagerTabs.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        viewPagerTabs.setTextSpacing(20);
    }
}
