package iqra.shabeer.models;

import java.util.List;

/**
 * Created by Iqra on 20/04/2017.
 */

public class QualitativeAnalysisModel {
    private String question;
    private List<String> answerList;

    public QualitativeAnalysisModel() {
    }

    public QualitativeAnalysisModel(String question, List<String> answerList) {
        this.question = question;
        this.answerList = answerList;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }
}
