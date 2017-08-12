package iqra.shabeer.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Iqra on 07-Aug-17.
 */

public class HodAccountBO implements Parcelable {

    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("fName")
    @Expose
    private String fName;
    @SerializedName("password")
    @Expose
    private String password;

    public HodAccountBO() {
        this.department = null;
        this.fName = null;
        this.password = null;
    }
    public HodAccountBO(String fName, String department, String password){
        this.department = department;
        this.fName = fName;
        this.password = password;
    }
    protected HodAccountBO(Parcel in) {
        department = in.readString();
        fName = in.readString();
        password = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(department);
        dest.writeString(fName);
        dest.writeString(password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HodAccountBO> CREATOR = new Creator<HodAccountBO>() {
        @Override
        public HodAccountBO createFromParcel(Parcel in) {
            return new HodAccountBO(in);
        }

        @Override
        public HodAccountBO[] newArray(int size) {
            return new HodAccountBO[size];
        }
    };

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

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
}
