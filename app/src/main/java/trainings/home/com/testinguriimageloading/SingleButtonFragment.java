package trainings.home.com.testinguriimageloading;

import java.net.URISyntaxException;

import org.greenrobot.eventbus.EventBus;

import android.Manifest;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.Fragment;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;

import android.database.Cursor;

import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v13.app.FragmentCompat;

import android.support.v4.app.ActivityCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SingleButtonFragment extends Fragment {

    private static final int REQUEST_SELECT_PICTURE = 0x88;
    private static final String SELECT_FILE_TITLE = "Select File";

    private static final int REQUEST_STORAGE_PERMISSION = 0x10;

    // Storage permissions
    private static String[] PERMISSIONS_STORAGE = {
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public SingleButtonFragment() { }

    // newInstance constructor for creating fragment with arguments
    static SingleButtonFragment newInstance() {
        return new SingleButtonFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.single_button_layout, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.click_me_button)
    void clickOnButton() {
        galleryIntent();
    }

    private void galleryIntent() {
        if (hasStoragePermissions(getActivity())) {

            // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
            // browser.
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // Filter to show only images, using the image MIME data type.
            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
            // To search for all documents available via installed storage providers,
            // it would be "*/*".
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // GRANT TEMPORARY READ PERMISSION

            startActivityForResult(Intent.createChooser(intent, SELECT_FILE_TITLE), REQUEST_SELECT_PICTURE);
        } else {

            // We don't have permission so prompt the user
            FragmentCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_STORAGE_PERMISSION);
        }
    }

    /**
     * Checks if the app has permission to write to device storage. If the app does not has permission then the user
     * will be prompted to grant permissions
     *
     * @param  activity
     */
    private static boolean hasStoragePermissions(final Activity activity) {

        // Check if we have write permission
        int permissionWrite = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRead = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        return !(permissionWrite != PackageManager.PERMISSION_GRANTED
                    && permissionRead != PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
            @NonNull final int[] grantResults) {
        switch (requestCode) {

            case REQUEST_STORAGE_PERMISSION : {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    galleryIntent();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // Permission Denied
                    Toast.makeText(getActivity(), "Denied", Toast.LENGTH_SHORT).show();
                }

                return;
            }

            default :
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * The default Android camera application returns a non-null intent only when passing back a thumbnail in the
     * returned Intent. If you pass EXTRA_OUTPUT with a URI to write to, it will return a null intent and the picture is
     * in the URI that you passed in.
     */
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                Uri outputImageFileUri = data.getData();
                if (outputImageFileUri != null) {

                    String stringTestReal = null;

                    try {
                        stringTestReal = getFilePath(getActivity().getApplicationContext(), outputImageFileUri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    ((MainActivity) getActivity()).getDb().addResults(new ImageContent(outputImageFileUri.getPath(),
                            stringTestReal, String.valueOf(outputImageFileUri)));

                    EventBus.getDefault().post(new DataBaseUpdatedEmptyEvent());

                    Toast.makeText(getActivity().getApplicationContext(), "AAAwEEEEsome", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Failed, Damn it!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public String getRealPathFromURI(final Context context, final Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * Gets the corresponding path to a file from the given content:// URI.
     *
     * @param   selectedVideoUri  The content:// URI to find the file path from
     * @param   contentResolver   The content resolver to use to perform the query.
     *
     * @return  the file path as a string
     */
    private String getFilePathFromContentUri(final Uri selectedVideoUri, final ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    @SuppressLint("NewApi")
    public static String getFilePath(final Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;

        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                selection = "_id=?";
                selectionArgs = new String[] {split[1]};
            }
        }

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);

                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) { }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isExternalStorageDocument(final Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(final Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(final Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
