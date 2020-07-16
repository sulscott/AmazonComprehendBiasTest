package runtest;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.FastMath;

public class RunTTest {

    /**
     * Performs a
     * http://www.itl.nist.gov/div898/handbook/eda/section3/eda353.htm"
     * two-sided t-test evaluating the null hypothesis that sample1
     * and sample2 are drawn from populations with the same mean,
     * with significance level alpha.  This test does not assume
     * that the subpopulation variances are equal.
     * Returns true iff the null hypothesis that the means are
     * equal can be rejected with confidence 1 - alpha.
     *
     * Degrees of freedom are approximated using the
     * http://www.itl.nist.gov/div898/handbook/prc/section3/prc31.htm
     * Welch-Satterthwaite approximation
     *
     * Examples:
     *
     * To test the (2-sided) hypothesis mean 1 = mean 2  at
     * the 95% level,  use
     * tTest(sample1, sample2, 0.05).
     *
     * To test the (one-sided) hypothesis  mean 1 < mean 2 ,
     * at the 99% level, first verify that the measured  mean of sample 1
     * is less than the mean of sample 2 and then use
     * tTest(sample1, sample2, 0.02)
     *
     * Usage Note:
     *
     * The validity of the test depends on the assumptions of the parametric
     * t-test procedure, as discussed
     * http://www.basic.nwu.edu/statguidefiles/ttest_unpaired_ass_viol.html
     * here
     *
     * Preconditions:
     *
     * The observed array lengths must both be at least 2.
     * 0 < alpha < 0.5
     *
     * @param sample1 array of sample data values
     * @param sample2 array of sample data values
     * @param alpha significance level of the test
     * @return true if the null hypothesis can be rejected with
     * confidence 1 - alpha
     * @throws NullArgumentException if the arrays are <code>null
     * @throws NumberIsTooSmallException if the length of the arrays is < 2
     * @throws OutOfRangeException if <code>alpha is not in the range (0, 0.5]
     * @throws MaxCountExceededException if an error occurs computing the p-value
     */
    public boolean tTest(final double[] sample1, final double[] sample2,
                         final double alpha)
        throws NullArgumentException, NumberIsTooSmallException,
        OutOfRangeException, MaxCountExceededException {

        checkSignificanceLevel(alpha);
        return tTest(sample1, sample2) < alpha;

    }


    /**
     * Returns the observed significance level, or
     * p-value, associated with a two-sample, two-tailed t-test
     * comparing the means of the input arrays.
     *.
     * The number returned is the smallest significance level
     * at which one can reject the null hypothesis that the two means are
     * equal in favor of the two-sided alternative that they are different.
     * For a one-sided test, divide the returned value by 2.
     *
     * The test does not assume that the underlying popuation variances are
     * equal  and it uses approximated degrees of freedom computed from the
     * sample data to compute the p-value.  The t-statistic used is as defined in
     * t(double[], double[]) and the Welch-Satterthwaite approximation
     * to the degrees of freedom is used,
     * as described
     * <a href="http://www.itl.nist.gov/div898/handbook/prc/section3/prc31.htm">
     * here.
     *
     * Usage Note:
     *
     * The validity of the p-value depends on the assumptions of the parametric
     * t-test procedure, as discussed
     * http://www.basic.nwu.edu/statguidefiles/ttest_unpaired_ass_viol.html
     * here.
     *
     * Preconditions:
     *
     * The observed array lengths must both be at least 2.
     *
     * @param sample1 array of sample data values
     * @param sample2 array of sample data values
     * @return p-value for t-test
     * @throws NullArgumentException if the arrays are <code>null
     * @throws NumberIsTooSmallException if the length of the arrays is < 2
     * @throws MaxCountExceededException if an error occurs computing the p-value
     */
    public double tTest(final double[] sample1, final double[] sample2)
        throws NullArgumentException, NumberIsTooSmallException,
        MaxCountExceededException {

        verifyData(sample1);
        verifyData(sample2);

        return tTest(StatUtils.mean(sample1), StatUtils.mean(sample2),
            StatUtils.variance(sample1), StatUtils.variance(sample2),
            sample1.length, sample2.length);

    }

    /**
     * Computes p-value for 2-sided, 2-sample t-test.
     *
     * Does not assume subpopulation variances are equal. Degrees of freedom
     * are estimated from the data.
     *
     * @param m1 first sample mean
     * @param m2 second sample mean
     * @param v1 first sample variance
     * @param v2 second sample variance
     * @param n1 first sample n
     * @param n2 second sample n
     * @return p-value
     * @throws MaxCountExceededException if an error occurs computing the p-value
     * @throws NotStrictlyPositiveException if the estimated degrees of freedom is not
     * strictly positive
     */
    protected double tTest(final double m1, final double m2,
                           final double v1, final double v2,
                           final double n1, final double n2)
        throws MaxCountExceededException, NotStrictlyPositiveException {

        final double t = FastMath.abs(t(m1, m2, v1, v2, n1, n2));
        final double degreesOfFreedom = df(v1, v2, n1, n2);
        // pass a null rng to avoid unneeded overhead as we will not sample from this distribution
        final TDistribution distribution = new TDistribution(null, degreesOfFreedom);
        return 2.0 * distribution.cumulativeProbability(-t);

    }

    /**
     * Computes t test statistic for 2-sample t-test.
     *
     * Does not assume that subpopulation variances are equal.
     *
     * @param m1 first sample mean
     * @param m2 second sample mean
     * @param v1 first sample variance
     * @param v2 second sample variance
     * @param n1 first sample n
     * @param n2 second sample n
     * @return t test statistic
     */
    protected double t(final double m1, final double m2,
                       final double v1, final double v2,
                       final double n1, final double n2)  {
        return (m1 - m2) / FastMath.sqrt((v1 / n1) + (v2 / n2));
    }

    /**
     * Computes approximate degrees of freedom for 2-sample t-test.
     *
     * @param v1 first sample variance
     * @param v2 second sample variance
     * @param n1 first sample n
     * @param n2 second sample n
     * @return approximate degrees of freedom
     */
    protected double df(double v1, double v2, double n1, double n2) {
        return (((v1 / n1) + (v2 / n2)) * ((v1 / n1) + (v2 / n2))) /
            ((v1 * v1) / (n1 * n1 * (n1 - 1d)) + (v2 * v2) /
                (n2 * n2 * (n2 - 1d)));
    }

    /**
     * Check significance level.
     *
     * @param alpha significance level
     * @throws OutOfRangeException if the significance level is out of bounds.
     */
    private void checkSignificanceLevel(final double alpha)
        throws OutOfRangeException {

        if (alpha <= 0 || alpha > 0.5) {
            throw new OutOfRangeException(LocalizedFormats.SIGNIFICANCE_LEVEL,
                alpha, 0.0, 0.5);
        }

    }

    /**
     * Helper method to verify that input is not null and length is greater than 2.
     * @param data input data.
     * @throws NullPointerException
     * @throws NumberIsTooSmallException
     */
    private void verifyData(final double[] data)
        throws NullPointerException, NumberIsTooSmallException{

        if (data == null) {
            throw new NullArgumentException(LocalizedFormats.NULL_NOT_ALLOWED);
        }

        if (data.length < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.INSUFFICIENT_DATA_FOR_T_STATISTIC, data.length, 2, true);
        }
    }

}
