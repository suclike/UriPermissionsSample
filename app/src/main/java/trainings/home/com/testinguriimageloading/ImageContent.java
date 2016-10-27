package trainings.home.com.testinguriimageloading;

import java.io.Serializable;

class ImageContent implements Serializable {

    String imagePath;
    String imageTitle;
    String imageUri;

    ImageContent(final String imageTitle, final String imagePath, final String imageUri) {
        this.imageTitle = imageTitle;
        this.imagePath = imagePath;
        this.imageUri = imageUri;
    }
}
