package firebase.demo.firebaseapplicationdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Atrijit on 25-01-2018.
 */

public class UserDetails implements Parcelable {
    String userToken;
    String userName;
    String userEmail;
    String userPh;
    String userImage;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPh() {
        return userPh;
    }

    public void setUserPh(String userPh) {
        this.userPh = userPh;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userToken);
        dest.writeString(this.userName);
        dest.writeString(this.userEmail);
        dest.writeString(this.userPh);
        dest.writeString(this.userImage);
    }

    public UserDetails() {
    }

    protected UserDetails(Parcel in) {
        this.userToken = in.readString();
        this.userName = in.readString();
        this.userEmail = in.readString();
        this.userPh = in.readString();
        this.userImage = in.readString();
    }

    public static final Parcelable.Creator<UserDetails> CREATOR = new Parcelable.Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel source) {
            return new UserDetails(source);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };
}
