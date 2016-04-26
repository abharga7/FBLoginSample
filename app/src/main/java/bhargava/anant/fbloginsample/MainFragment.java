package bhargava.anant.fbloginsample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    private TextView mTextDetails, textView1;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private ImageView profileImage;
    private Uri profileImageURI;
    private String uriString;
    private Button button1;
    private Bitmap b3;


    public void publishImage() {
        Bitmap b1 = BitmapFactory.decodeResource(getResources(),R.drawable.emergency);
        SharePhoto photo = new SharePhoto.Builder().setBitmap(b1).setCaption("Emergency Photo from first android App").build();

        SharePhotoContent content1 = new SharePhotoContent.Builder().addPhoto(photo).build();
        ShareApi.share(content1,null);
    }

    public void displayWelcomeMessage(Profile profile) throws IOException {
        if(profile!=null){


            String s = profile.getProfilePictureUri(50, 50).toString();
           // Log.e("URL:",s);
            //Bitmap  mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), profile.getProfilePictureUri(200, 200));
            //InputStream pictureInputStream = getActivity().getContentResolver().openInputStream(profile.getProfilePictureUri(50, 50));
            //Bitmap mBitmap = BitmapFactory.decodeStream(pictureInputStream);

            mTextDetails.setText("Welcome" + profile.getName());
            //profileImage.setImageBitmap(b2);
            //profileImage.setImageURI(null);
            //profileImage.setImageURI(profile.getProfilePictureUri(200, 200));


        }
    }


    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            System.out.print("success");
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            try {
                displayWelcomeMessage(profile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // publishImage();

        }


        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager=CallbackManager.Factory.create();
         mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };
         mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                try {
                    profileImageURI = newProfile.getProfilePictureUri(200, 200);
                    uriString = profileImageURI.toString();
                    //profileImage.setImageURI(null);
                    //profileImage.setImageURI();
                      //b3 = getPicture(uriString);
                     b3= BitmapFactory.decodeResource(getResources(), R.drawable.google);
                    profileImage.setImageBitmap(b3);
                    displayWelcomeMessage(newProfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mTokenTracker.startTracking();
        mProfileTracker.startTracking();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        //List<String> permissions = Arrays.asList("user_friends", "publish_actions");
        //loginButton.setReadPermissions("user_events"); //this is where we ask for user vents permission
        loginButton.setPublishPermissions("publish_actions");

        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mCallback);
        mTextDetails= (TextView) view.findViewById(R.id.welcomebutton);
        profileImage =(ImageView) view.findViewById(R.id.imageView);
        textView1 = (TextView) view.findViewById(R.id.textView);
        button1 = (Button) view.findViewById(R.id.button);

button1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        textView1.setText("Hello");
    }
});



    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        try {
            displayWelcomeMessage(profile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }
    public static Bitmap getPicture(String s1) throws IOException {
        URL imageURL = new URL(s1);
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        return bitmap;
    }
}
