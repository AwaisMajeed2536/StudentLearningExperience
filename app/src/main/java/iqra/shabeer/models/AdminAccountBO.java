package iqra.shabeer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Iqra on 6/16/2017.
 */

public class AdminAccountBO implements Parcelable {
    @SerializedName("fName")
    @Expose
    private String fName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("DB_Admin")
    @Expose
    private String DB_Admin;

    public AdminAccountBO() {
        this.fName = null;
        this.password = null;
        this.DB_Admin = "DataBase Administrator";
    }

    public AdminAccountBO(String fName, String password, String DB_Admin) {
        this.fName = fName;
        this.password = password;
        this.DB_Admin = DB_Admin;
    }

    public AdminAccountBO(AdminAccountBO userAccountBO) {
        this.fName = userAccountBO.fName;
        this.password = userAccountBO.password;
        this.DB_Admin = userAccountBO.DB_Admin;
    }

    public AdminAccountBO(Parcel in) {
        fName = in.readString();
        password = in.readString();
        DB_Admin = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fName);
        dest.writeString(password);
        dest.writeString(DB_Admin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AdminAccountBO> CREATOR = new Creator<AdminAccountBO>() {
        @Override
        public AdminAccountBO createFromParcel(Parcel in) {
            return new AdminAccountBO(in);
        }

        @Override
        public AdminAccountBO[] newArray(int size) {
            return new AdminAccountBO[size];
        }
    };

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getDB_Admin() {
        return "DataBase Administrator";
    }
}