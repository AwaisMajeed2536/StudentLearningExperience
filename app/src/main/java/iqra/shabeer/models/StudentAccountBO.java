package iqra.shabeer.models;

/**
 * Created by Iqra on 2/22/2017.
 */


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentAccountBO implements Parcelable{
    @SerializedName("fName")
    @Expose
    private String fName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("program")
    @Expose
    private String program;
    @SerializedName("semester")
    @Expose
    private Long semester;

    public StudentAccountBO(){
        this.fName =null;
        this.password =null;
        this.program =null;
        this.semester = null;
    }
    public StudentAccountBO(String fName, String password, String program, Long semester) {
        this.fName = fName;
        this.password = password;
        this.program = program;
        this.semester = semester;
    }

    public StudentAccountBO(StudentAccountBO userAccountBO){
        this.fName = userAccountBO.fName;
        this.password = userAccountBO.password;
        this.program = userAccountBO.program;
        this.semester = userAccountBO.semester;
    }

    protected StudentAccountBO(Parcel in) {
        fName = in.readString();
        password = in.readString();
        program = in.readString();
        semester = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fName);
        dest.writeString(password);
        dest.writeString(program);
        dest.writeLong(semester);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StudentAccountBO> CREATOR = new Creator<StudentAccountBO>() {
        @Override
        public StudentAccountBO createFromParcel(Parcel in) {
            return new StudentAccountBO(in);
        }

        @Override
        public StudentAccountBO[] newArray(int size) {
            return new StudentAccountBO[size];
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

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public Long getSemester() {
        return semester;
    }

    public void setSemester(Long semester) {
        this.semester = semester;
    }

}
