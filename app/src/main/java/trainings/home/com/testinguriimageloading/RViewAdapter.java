package trainings.home.com.testinguriimageloading;

import java.util.List;

import android.net.Uri;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

class RViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_HOLDER_LIST_TYPE = 0;

    private List<ImageContent> imageContentList;
    private ImageHandler imageHandler;

    RViewAdapter(final List<ImageContent> imageContentList, final ImageHandler imageHandler) {
        this.imageContentList = imageContentList;
        this.imageHandler = imageHandler;
    }

    private static class RViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageString;
        ImageView imageUri;

        private RViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.result_title_textview);
            imageString = (ImageView) itemView.findViewById(R.id.result_uri_imageview);
            imageUri = (ImageView) itemView.findViewById(R.id.result_string_imageview);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        switch (viewType) {

            default :
            case VIEW_HOLDER_LIST_TYPE :
                return new RViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item,
                            parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        switch (getItemViewType(position)) {

            default :
            case VIEW_HOLDER_LIST_TYPE :

                final String imageTitle = imageContentList.get(position).imageTitle;
                final String imagePath = imageContentList.get(position).imagePath;
                final String imageUri = imageContentList.get(position).imageUri;

                if (!imageTitle.isEmpty()) {
                    ((RViewHolder) viewHolder).title.setText(imageTitle);
                }

                if (imagePath != null) {
                    loadImageByString(composeImageIrl(imageContentList.get(position).imagePath),
                        ((RViewHolder) viewHolder).imageString);
                }

                if (imageUri != null) {
                    loadImage(getImageUri(imageContentList.get(position).imageUri),
                        ((RViewHolder) viewHolder).imageUri);
                }

                break;
        }
    }

    private Uri getImageUri(final String imageStringPath) {
        return Uri.parse(imageStringPath);
    }

    private String composeImageIrl(final String imageStringPath) {
        return "file://" + imageStringPath;
    }

    void swapRecords(final List<ImageContent> imageContents) {
        imageContentList.clear();

        for (ImageContent result : imageContents) {
            imageContentList.add(result);
        }

        notifyDataSetChanged();
    }

    private void loadImage(final Uri imageUri, final ImageView imageView) {
        imageHandler.loadByUri(imageUri, imageView);
    }

    private void loadImageByString(final String imageUrl, final ImageView imageView) {
        imageHandler.loadByUrl(imageUrl, imageView);
    }

    @Override
    public int getItemCount() {
        return imageContentList == null || imageContentList.isEmpty() ? 0 : imageContentList.size();
    }

    @Override
    public int getItemViewType(final int position) {
        return VIEW_HOLDER_LIST_TYPE;
    }
}
