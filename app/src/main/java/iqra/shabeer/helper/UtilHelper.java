package iqra.shabeer.helper;

import java.util.ArrayList;

/**
 * Created by Devprovider on 02/05/2017.
 */

public class UtilHelper {

    public static double findStdDev(double mean, ArrayList<Long> scores) {
        double total =  scores.get(0) + scores.get(1) + scores.get(2) + scores.get(3) +scores.get(4);
        return ((5*5*scores.get(0) + 4*4*scores.get(1) + 3*3*scores.get(2) + 2*2*scores.get(3) + scores.get(4))/total) - (mean*mean);
    }

    public static double findMean(ArrayList<Long> scores) {
        double total =  scores.get(0) + scores.get(1) + scores.get(2) + scores.get(3) +scores.get(4);
        return (5*scores.get(0) + 4*scores.get(1) +3*scores.get(2) + 2*scores.get(3) + scores.get(4))/total;
    }
}
