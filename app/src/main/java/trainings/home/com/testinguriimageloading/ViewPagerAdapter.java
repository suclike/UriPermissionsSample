package trainings.home.com.testinguriimageloading;

import java.lang.ref.WeakReference;

import android.app.Fragment;
import android.app.FragmentManager;

import android.support.annotation.Nullable;

import android.support.v13.app.FragmentStatePagerAdapter;

import android.util.SparseArray;

import android.view.ViewGroup;

class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private SparseArray<String> fragmentTags = new SparseArray<>();

    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();

    ViewPagerAdapter(final FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return 2;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(final int position) {
        switch (position) {

            case 0 :
                if (getFragment(position) == null) {
                    return RecycleViewFragment.newInstance();
                }

            case 1 :
                if (getFragment(position) == null) {
                    return SingleButtonFragment.newInstance();
                }

            default :
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {

            case 0 :
                return "TAB WITH RVIEW";

            case 1 :
                return "TAB WITH FAB";

            default :
                return "";
        }
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);

        if (fragment != null) {
            fragmentTags.put(position, fragment.getTag());
        }

        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }
}
