package sfa.transformation;

import net.seninp.jmotif.sax.SAXException;
import net.seninp.jmotif.sax.TSProcessor;
import sfa.timeseries.TimeSeries;

import java.util.Arrays;

public class SAX implements TSAproximation{

    TSProcessor tsProcessor = new TSProcessor();
    int windowSize;

    public SAX() {
    }

    public SAX(int windowSize) {
        this.windowSize = windowSize;
    }

    @Override
    public double[] transform(TimeSeries timeSeries, int l) {
        double[] paa = null;
        try {
            if (timeSeries.getData().length <= l) {
                return timeSeries.getData();
            }
            paa = tsProcessor.paa(timeSeries.getData(), l);
        } catch (SAXException e) {
            e.printStackTrace();
            System.err.println("Timeseries length = " + timeSeries.getData().length + " l = " + l);
        }

        return paa;
    }

    @Override
    public double[][] transformWindowing(TimeSeries timeSeries, int l) {
        int newWindow = Math.max(windowSize, l);

        double[][] words = new double[timeSeries.getData().length - newWindow + 1][];


        for (int i = 0; i <= timeSeries.getData().length - newWindow; ++i) {
            double[] subSection = Arrays.copyOfRange(timeSeries.getData(), i, i + newWindow);

            try {
                words[i] = tsProcessor.paa(subSection, l);
            } catch (SAXException e) {
                e.printStackTrace();
                System.err.println("Timeseries length = " + timeSeries.getData().length + " l = " + l + " window = " + newWindow);
                System.exit(9);
            }
        }

        return words;
    }
}
