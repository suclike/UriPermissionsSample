package trainings.home.com.testinguriimageloading;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import android.content.Context;

import android.net.Uri;

import android.support.annotation.NonNull;

import android.support.v4.content.ContextCompat;

import android.widget.ImageView;

public class PicassoImageHandler implements ImageHandler {

    private Context context;

    public PicassoImageHandler(final Context context) {
        this.context = context;
    }

    @Override
    public void loadByUrl(@NonNull final String imageUrl, @NonNull final ImageView imageView) {

        // clear image view before loading an image
        clearImageView(imageView);

        loadImageByUrl(imageUrl, imageView);
    }

    @Override
    public void loadByUri(@NonNull final Uri imageUrl, @NonNull final ImageView imageView) {

        // clear image view before loading an image
        clearImageView(imageView);

        loadImageByUri(imageUrl, imageView);
    }

    private void clearImageView(@NonNull final ImageView imageView) {
        imageView.setImageResource(0);
        imageView.destroyDrawingCache();
        imageView.setImageDrawable(null);
    }

    private void loadImageByUri(@NonNull final Uri imageUri, @NonNull final ImageView imageView) {
        Picasso.with(context)                                                       //
               .load(imageUri)                                                      //
               .error(ContextCompat.getDrawable(context, R.drawable.ic_alert_icon)) //
               .noFade()                                                            //
               .fit()                                                               //
               .memoryPolicy(MemoryPolicy.NO_CACHE)                                 //
               .into(imageView);

        Picasso.with(context).invalidate(imageUri);
    }

    private void loadImageByUrl(@NonNull final String imageUrl, @NonNull final ImageView imageView) {
        Picasso.with(context)                                                       //
               .load(imageUrl)                                                      //
               .noFade()                                                            //
               .fit()                                                               //
               .error(ContextCompat.getDrawable(context, R.drawable.ic_alert_icon)) //
               .into(imageView);

        Picasso.with(context).invalidate(imageUrl);
    }
}
