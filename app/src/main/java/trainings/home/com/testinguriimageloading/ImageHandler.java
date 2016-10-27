package trainings.home.com.testinguriimageloading;

import android.net.Uri;

import android.support.annotation.NonNull;

import android.widget.ImageView;

interface ImageHandler {
    void loadByUrl(@NonNull final String imageUrl, @NonNull final ImageView imageView);

    void loadByUri(@NonNull final Uri imageUrl, @NonNull final ImageView imageView);
}
