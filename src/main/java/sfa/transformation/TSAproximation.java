package sfa.transformation;

import sfa.timeseries.TimeSeries;

public interface TSAproximation {





    //TSAproximation(int windowSize, boolean normMean, boolean lowerBounding, boolean useMinOrMax);

    /**
     * Transforms a timeseries int
     * @param timeSeries the time series to be transformed
     * @param l          the length of the transformed timeseries
     * @return the transformed timeseries
     */
    double[] transform(TimeSeries timeSeries, int l);

    /**
     *
     * Transform the timeseries
     *
     * @param timeSeries the time series to be transformed
     * @param l          the number of Fourier values to use (equal to l/2 Fourier
     *                   coefficients). If l is uneven, l+1 Fourier values are returned. If
     *                   windowSize is smaller than l, only the first windowSize Fourier
     *                   values are set.
     * @return returns transformed time series
     */
    double[][] transformWindowing(TimeSeries timeSeries, int l);



}
