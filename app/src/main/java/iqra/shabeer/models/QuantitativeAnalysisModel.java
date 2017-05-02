package iqra.shabeer.models;

/**
 * Created by Devprovider on 4/11/2017.
 */

public class QuantitativeAnalysisModel {
    private String questionNumber;
    private String question;
    private int [] scoreCount;
    private double mean;
    private double median;
    private double stdDev;

    public QuantitativeAnalysisModel() {
    }

    public QuantitativeAnalysisModel(String questionNumber, String question, int[] scoreCount, int[] score, double mean, double median, double stdDev) {
        this.questionNumber = questionNumber;
        this.question = question;
        this.scoreCount = scoreCount;
        this.mean = mean;
        this.median = median;
        this.stdDev = stdDev;
    }

    public String getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(String questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int[] getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(int[] scoreCount) {
        this.scoreCount = scoreCount;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getStdDev() {
        return stdDev;
    }

    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }
}
