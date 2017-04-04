package iqra.shabeer.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Awais on 2/21/2017.
 */

public class EvaluationDataBO {
    @SerializedName("courseCode")
    @Expose
    private String courseCode;
    @SerializedName("coursefName")
    @Expose
    private String coursefName;
    @SerializedName("questions")
    @Expose
    private List<String> questions = null;

    public EvaluationDataBO() {
        this.courseCode = null;
        this.coursefName = null;
        this.questions = null;
    }

    public EvaluationDataBO(String courseCode, String coursefName, List<String> questions) {
        this.courseCode = courseCode;
        this.coursefName = coursefName;
        this.questions = questions;
    }


    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCoursefName() {
        return coursefName;
    }

    public void setCoursefName(String coursefName) {
        this.coursefName = coursefName;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

}
