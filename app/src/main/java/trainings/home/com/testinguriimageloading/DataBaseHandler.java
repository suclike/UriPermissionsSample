package trainings.home.com.testinguriimageloading;

import java.util.List;

interface DataBaseHandler {

    void addResults(final ImageContent imageContent);

    List<ImageContent> getAllResults();
}
