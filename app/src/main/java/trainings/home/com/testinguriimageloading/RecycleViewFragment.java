package trainings.home.com.testinguriimageloading;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.app.Fragment;

import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecycleViewFragment extends Fragment {

    @Bind(R.id.result_recycler_view)
    RecyclerView resultsRecycleView;

    private PicassoImageHandler imageHandler;
    private List<ImageContent> contentList = new ArrayList<>();

    public RecycleViewFragment() { }

    // newInstance constructor for creating fragment with arguments
    static RecycleViewFragment newInstance() {
        return new RecycleViewFragment();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        imageHandler = new PicassoImageHandler(getActivity().getApplicationContext());
        
        if (savedInstanceState != null) {
            contentList = (List<ImageContent>) savedInstanceState.getSerializable("savedList");
        } else {
            contentList = getContentList();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            contentList = (List<ImageContent>) savedInstanceState.getSerializable("savedList");
        } else {
            contentList = getContentList();
        }
    }
    
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("savedList", (Serializable) contentList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final DataBaseUpdatedEmptyEvent event) {
        refreshRView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final NoDataBaseEmptyEvent event) {
        refreshRView();
    }
    
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycleview_layout, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        initRecycleView(contentList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    void initRecycleView(final List<ImageContent> imageContentList) {

        // adapter cannot change size of rView itself, it fits content only
        resultsRecycleView.setHasFixedSize(true);
        resultsRecycleView.setItemViewCacheSize(10);
        resultsRecycleView.setDrawingCacheEnabled(true);
        resultsRecycleView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        resultsRecycleView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        resultsRecycleView.setLayoutManager(mLayoutManager);

        RViewAdapter mAdapter = new RViewAdapter(imageContentList, imageHandler);

        // swap adapter, making sure that old one is removed, if existed
        resultsRecycleView.swapAdapter(mAdapter, true);
    }

    void refreshRView() {
        ((RViewAdapter) resultsRecycleView.getAdapter()).swapRecords(getContentList());
    }

    List<ImageContent> getContentList() {
        return ((MainActivity) getActivity()).getDb().getAllResults();
    }
}
