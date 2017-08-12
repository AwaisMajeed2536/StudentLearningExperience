package iqra.shabeer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Iqra on 2/26/2017.
 */

public class TeacherAccountBO implements Parcelable {

    @SerializedName("courses")
    @Expose
    private List<String> courses = null;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("fName")
    @Expose
    private String fName;
    @SerializedName("password")
    @Expose
    private String password;

    public TeacherAccountBO() {
        this.courses = null;
        this.department = null;
        this.designation = null;
        this.fName = null;
        this.password = null;
    }
public TeacherAccountBO(String fName,String department, String designation,String password, List course){
    this.courses = course;
    this.department = department;
    this.designation = designation;
    this.fName = fName;
    this.password = password;
}
    protected TeacherAccountBO(Parcel in) {
        courses = in.createStringArrayList();
        department = in.readString();
        designation = in.readString();
        fName = in.readString();
        password = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(courses);
        dest.writeString(department);
        dest.writeString(designation);
        dest.writeString(fName);
        dest.writeString(password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeacherAccountBO> CREATOR = new Creator<TeacherAccountBO>() {
        @Override
        public TeacherAccountBO createFromParcel(Parcel in) {
            return new TeacherAccountBO(in);
        }

        @Override
        public TeacherAccountBO[] newArray(int size) {
            return new TeacherAccountBO[size];
        }
    };

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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
