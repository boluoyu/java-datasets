/*
 * Original code: BSD 2-Clause "Simplified" License
 * http://opensource.org/licenses/BSD-2-Clause
 * Copyright 2006 Wilhelm Burger and Mark J. Burge
 * http://www.imagingbook.com
 *
 * Modified by Thibault Debatty
 */
package info.debatty.java.datasets.sift;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularMatrixException;

/**
 * This class contains a collection of static methods for calculations with
 * vectors and matrices using native Java arrays without any enclosing objects
 * structures. Matrices are simply two-dimensional array M[r][c], where r is the
 * row index and c is the column index (as common in linear algebra). This means
 * that matrices are really vectors of row vectors. TODO: need to differentiate
 * functional and side-effect methods!
 *
 * @author WB
 * @version 2014/12/04
 */
abstract class Matrix {

    public static final double EPSILON_DOUBLE = 2e-16;	// 2.22 x 10^-16

    static {
        Locale.setDefault(Locale.US);
    }

    // Vector and matrix creation
    public static double[] createDoubleVector(int length) {
        return new double[length];
    }

    public static float[] createFloatVector(int length) {
        return new float[length];
    }

    public static double[][] createDoubleMatrix(int rows, int columns) {
        return new double[rows][columns];
    }

    public static float[][] createFloatMatrix(int rows, int columns) {
        return new float[rows][columns];
    }

    // Matrix properties -------------------------------------
    public static int getNumberOfRows(double[][] A) {
        return A.length;
    }

    public static int getNumberOfColumns(double[][] A) {
        return A[0].length;
    }

    public static int getNumberOfRows(float[][] A) {
        return A.length;
    }

    public static int getNumberOfColumns(float[][] A) {
        return A[0].length;
    }

    // Matrix and vector duplication ------------------------------
    public static double[] duplicate(final double[] A) {
        return A.clone();
    }

    public static float[] duplicate(final float[] A) {
        return A.clone();
    }

    public static double[][] duplicate(final double[][] A) {
        final int m = A.length;
        final double[][] B = new double[m][];
        for (int i = 0; i < m; i++) {
            B[i] = A[i].clone();
        }
        return B;
    }

    public static float[][] duplicateToFloat(final double[][] A) {
        final int m = A.length;
        final int n = A[0].length;
        final float[][] B = new float[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                B[i][j] = (float) A[i][j];
            }
        }
        return B;
    }

    public static float[][] duplicate(final float[][] A) {
        final int m = A.length;
        float[][] B = new float[m][];
        for (int i = 0; i < m; i++) {
            B[i] = A[i].clone();
        }
        return B;
    }

    public static double[][] duplicateToDouble(final float[][] A) {
        final int m = A.length;
        final int n = A[0].length;
        final double[][] B = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                B[i][j] = A[i][j];
            }
        }
        return B;
    }

	// Element-wise arithmetic -------------------------------
    //TODO: change to multiple args: public static int[] add(int[]... as)
    public static int[] add(int[] a, int[] b) {
        int[] c = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = c[i] + b[i];
        }
        return c;
    }

    public static double[] add(double[] a, double[] b) {
        double[] c = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] + b[i];
        }
        return c;
    }

    public static double[][] add(double[][] A, double[][] B) {
        final int m = A.length;
        final int n = A[0].length;
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }

    public static double[] subtract(double[] a, double[] b) {
        double[] c = a.clone();
        for (int i = 0; i < a.length; i++) {
            c[i] = c[i] - b[i];
        }
        return c;
    }

    public static double[] subtract(double[] a, int[] b) {
        double[] c = a.clone();
        for (int i = 0; i < a.length; i++) {
            c[i] = c[i] - b[i];
        }
        return c;
    }

    public static int[] floor(double[] a) {
        int[] b = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = (int) Math.floor(a[i]);
        }
        return b;
    }

    // Scalar multiplications -------------------------------
    // non-destructive
    public static double[] multiply(final double s, final double[] a) {
        double[] b = a.clone();
        multiplyD(s, b);
        return b;
    }

    // destructive
    public static void multiplyD(final double s, final double[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = a[i] * s;
        }
    }

    // non-destructive
    public static double[][] multiply(final double s, final double[][] A) {
        double[][] B = duplicate(A);
        multiplyD(s, B);
        return B;
    }

    public static void multiplyD(final double s, final double[][] A) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                A[i][j] = A[i][j] * s;
            }
        }
    }

    // non-destructive
    public static float[] multiply(final float s, final float[] A) {
        float[] B = duplicate(A);
        multiplyD(s, B);
        return B;
    }

    // destructive
    public static void multiplyD(final float s, final float[] A) {
        for (int i = 0; i < A.length; i++) {
            A[i] = A[i] * s;
        }
    }

    // non-destructive
    public static float[][] multiply(final float s, final float[][] A) {
        float[][] B = duplicate(A);
        for (int i = 0; i < B.length; i++) {
            for (int j = 0; j < B[i].length; j++) {
                B[i][j] = B[i][j] * s;
            }
        }
        return B;
    }

    // destructive
    public static void multiplyD(final float s, final float[][] A) {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                A[i][j] = A[i][j] * s;
            }
        }
    }

    // matrix-vector multiplications ----------------------------------------
    // non-destructive
    public static double[] multiply(final double[] x, final double[][] A) {
        double[] Y = new double[getNumberOfColumns(A)];
        multiplyD(x, A, Y);
        return Y;
    }

    /*
     * Implements a right (post-) matrix-vector multiplication: x . A -> y
     * x is treated as a row vector of length m, matrix A is of size (m,n).
     * y (a row vector of length n) is modified.
     * destructive
     */
    public static void multiplyD(final double[] x, final double[][] A, double[] y) {
        final int m = getNumberOfRows(A);
        final int n = getNumberOfColumns(A);
        if (x.length != m || y.length != n) {
            throw new IllegalArgumentException("incompatible vector-matrix dimensions");
        }
        for (int i = 0; i < n; i++) {
            double s = 0;
            for (int j = 0; j < m; j++) {
                s = s + x[j] * A[j][i];
            }
            y[i] = s;
        }
    }


    /*
     * implements a left (pre-) matrix-vector multiplication:  A . x -> y
     * Matrix A is of size (m,n), column vector x is of length n.
     * The result y is a column vector of length m.
     * non-destructive
     */
    public static double[] multiply(final double[][] A, final double[] x) {
        double[] y = new double[getNumberOfRows(A)];
        multiplyD(A, x, y);
        return y;
    }

    // destructive
    public static void multiplyD(final double[][] A, final double[] x, double[] y) {
        final int m = getNumberOfRows(A);
        final int n = getNumberOfColumns(A);
        if (x.length != n || y.length != m) {
            throw new IllegalArgumentException("incompatible matrix-vector dimensions");
        }
        for (int i = 0; i < m; i++) {
            double s = 0;
            for (int j = 0; j < n; j++) {
                s = s + A[i][j] * x[j];
            }
            y[i] = s;
        }
    }

    // non-destructive
    public static float[] multiply(final float[][] A, final float[] x) {
        float[] y = new float[getNumberOfRows(A)];
        multiplyD(A, x, y);
        return y;
    }

    /**
     * Matrix-vector product: A . x = y All arguments must be appropriately
     * sized. destructive
     *
     * @param A matrix of size m,n (input)
     * @param x vector of length n (input)
     * @param y vector of length m (result)
     */
    public static void multiplyD(final float[][] A, final float[] x, float[] y) {
        final int m = getNumberOfRows(A);
        final int n = getNumberOfColumns(A);
        if (x.length != n || y.length != m) {
            throw new IllegalArgumentException("incompatible matrix-vector dimensions");
        }
        for (int i = 0; i < m; i++) {
            double s = 0;
            for (int j = 0; j < n; j++) {
                s = s + A[i][j] * x[j];
            }
            y[i] = (float) s;
        }
    }

    // Matrix-matrix products ---------------------------------------
    // returns A * B (non-destructive)
    public static double[][] multiply(final double[][] A, final double[][] B) {
        int m = getNumberOfRows(A);
        int q = getNumberOfColumns(B);
        double[][] C = createDoubleMatrix(m, q);
        multiplyD(A, B, C);
        return C;
    }

    // A * B -> C (destructive)
    public static void multiplyD(final double[][] A, final double[][] B, final double[][] C) {
        final int mA = getNumberOfRows(A);
        final int nA = getNumberOfColumns(A);
        final int nB = getNumberOfColumns(B);
        for (int i = 0; i < mA; i++) {
            for (int j = 0; j < nB; j++) {
                double s = 0;
                for (int k = 0; k < nA; k++) {
                    s = s + A[i][k] * B[k][j];
                }
                C[i][j] = s;
            }
        }
    }

    // returns A * B (non-destructive)
    public static float[][] multiply(final float[][] A, final float[][] B) {
        // TODO: also check nA = mB
        final int mA = getNumberOfRows(A);
        final int nB = getNumberOfColumns(B);
        float[][] C = createFloatMatrix(mA, nB);
        multiply(A, B, C);
        return C;
    }

    // A * B -> C (destructive)
    public static void multiply(final float[][] A, final float[][] B, final float[][] C) {
        final int mA = getNumberOfRows(A);
        final int nA = getNumberOfColumns(A);
        final int mB = getNumberOfRows(B);
        final int nB = getNumberOfColumns(B);
        if (nA != mB) {
            throw new IllegalArgumentException("Matrix.multiply: wrong row/col dimensions");
        }
        for (int i = 0; i < mA; i++) {
            for (int j = 0; j < nB; j++) {
                float s = 0;
                for (int k = 0; k < nA; k++) {
                    s = s + A[i][k] * B[k][j];
                }
                C[i][j] = s;
            }
        }
    }

	// Vector-vector products ---------------------------------------
    // A is considered a row vector, B is a column vector, both of length n.
    // returns a scalar vale.
    public static double dotProduct(final double[] A, final double[] B) {
        double sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum = sum + A[i] * B[i];
        }
        return sum;
    }

    // A is considered a column vector, B is a row vector, of length m, n, resp.
    // returns a matrix M of size (m,n).
    public static double[][] outerProduct(final double[] A, final double[] B) {
        final int m = A.length;
        final int n = B.length;
        final double[][] M = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                M[i][j] = A[i] * B[j];
            }
        }
        return M;
    }

    //  Vector norms ---------------------------------------------------
    public static double normL1(final double[] A) {
        double sum = 0;
        for (double x : A) {
            sum = sum + Math.abs(x);
        }
        return sum;
    }

    public static double normL2(final double[] A) {
        return Math.sqrt(normL2squared(A));
    }

    public static double normL2squared(final double[] A) {
        double sum = 0;
        for (double x : A) {
            sum = sum + (x * x);
        }
        return sum;
    }

    public static float normL1(final float[] A) {
        double sum = 0;
        for (double x : A) {
            sum = sum + Math.abs(x);
        }
        return (float) sum;
    }

    public static float normL2(final float[] A) {
        return (float) Math.sqrt(normL2squared(A));
    }

    public static float normL2squared(final float[] A) {
        double sum = 0;
        for (double x : A) {
            sum = sum + (x * x);
        }
        return (float) sum;
    }

    // Summation --------------------------------------------------
    public static double sum(final double[] A) {
        double sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum = sum + A[i];
        }
        return sum;
    }

    public static double sum(final double[][] A) {
        double sum = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                sum = sum + A[i][j];
            }
        }
        return sum;
    }

    public static float sum(final float[] A) {
        double sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum = sum + A[i];
        }
        return (float) sum;
    }

    public static double sum(final float[][] A) {
        double sum = 0;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                sum = sum + A[i][j];
            }
        }
        return sum;
    }

    // Min/max of vectors ------------------------
    public static float min(final float[] A) {
        float minval = Float.POSITIVE_INFINITY;
        for (float val : A) {
            if (val < minval) {
                minval = val;
            }
        }
        return minval;
    }

    public static double min(final double[] A) {
        double minval = Double.POSITIVE_INFINITY;
        for (double val : A) {
            if (val < minval) {
                minval = val;
            }
        }
        return minval;
    }

    public static float max(final float[] A) {
        float maxval = Float.NEGATIVE_INFINITY;
        for (float val : A) {
            if (val > maxval) {
                maxval = val;
            }
        }
        return maxval;
    }

    public static double max(final double[] A) {
        double maxval = Double.NEGATIVE_INFINITY;
        for (double val : A) {
            if (val > maxval) {
                maxval = val;
            }
        }
        return maxval;
    }

    // Vector concatenation -----------------------
    public static float[] concatenate(float[]
        ... as) {
        List<Float> vlist = new ArrayList<Float>();
        for (float[] a : as) {
            for (float val : a) {
                vlist.add(val);
            }
        }

        float[] va = new float[vlist.size()];
        int i = 0;
        for (float val : vlist) {
            va[i] = val;
            i++;
        }
        return va;
    }

    public static double[] concatenate(double[]
        ... as) {

        List<Double> vlist = new ArrayList<Double>();
        for (double[] a : as) {
            for (double val : a) {
                vlist.add(val);
            }
        }

        double[] va = new double[vlist.size()];
        int i = 0;
        for (double val : vlist) {
            va[i] = val;
            i++;
        }
        return va;
    }

    // Determinants --------------------------------------------
    public static float determinant2x2(final float[][] A) {
        return A[0][0] * A[1][1] - A[0][1] * A[1][0];
    }

    public static double determinant2x2(final double[][] A) {
        return A[0][0] * A[1][1] - A[0][1] * A[1][0];
    }

    public static float determinant3x3(final float[][] A) {
        return A[0][0] * A[1][1] * A[2][2]
                + A[0][1] * A[1][2] * A[2][0]
                + A[0][2] * A[1][0] * A[2][1]
                - A[2][0] * A[1][1] * A[0][2]
                - A[2][1] * A[1][2] * A[0][0]
                - A[2][2] * A[1][0] * A[0][1];
    }

    public static double determinant3x3(final double[][] A) {
        return A[0][0] * A[1][1] * A[2][2]
                + A[0][1] * A[1][2] * A[2][0]
                + A[0][2] * A[1][0] * A[2][1]
                - A[2][0] * A[1][1] * A[0][2]
                - A[2][1] * A[1][2] * A[0][0]
                - A[2][2] * A[1][0] * A[0][1];
    }

    // Matrix transposition ---------------------------------------
    public static float[][] transpose(float[][] A) {
        final int m = getNumberOfRows(A);
        final int n = getNumberOfColumns(A);
        float[][] At = new float[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                At[j][i] = A[i][j];
            }
        }
        return At;
    }

    public static double[][] transpose(double[][] A) {
        final int m = getNumberOfRows(A);
        final int n = getNumberOfColumns(A);
        double[][] At = new double[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                At[j][i] = A[i][j];
            }
        }
        return At;
    }

    // Matrix Froebenius norm ---------------------------------------
    public static double froebeniusNorm(final double[][] A) {
        final int m = getNumberOfRows(A);
        final int n = getNumberOfColumns(A);
        double s = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                s = s + A[i][j] * A[i][j];
            }
        }
        return Math.sqrt(s);
    }

    // Matrix trace ---------------------------------------
    public static double trace(final double[][] A) {
        final int m = getNumberOfRows(A);
        final int n = getNumberOfColumns(A);
        if (m != n) {
            throw new IllegalArgumentException("square matrix expected");
        }
        double s = 0;
        for (int i = 0; i < m; i++) {
            s = s + A[i][i];
        }
        return s;
    }

       // Matrix inversion ---------------------------------------
    /**
     * @param A a square matrix.
     * @return the inverse of A or null if A is non-square or singular.
     */
    public static double[][] inverse(final double[][] A) {
        RealMatrix M = MatrixUtils.createRealMatrix(A);
        if (!M.isSquare()) {
            return null;
        } else {
            double[][] Ai = null;
            try {
                RealMatrix Mi = MatrixUtils.inverse(M); //new LUDecomposition(M).getSolver().getInverse();
                Ai = Mi.getData();
            } catch (SingularMatrixException e) {
            }
            return Ai;
        }
    }

    public static float[][] inverse(final float[][] A) {
        double[][] B = duplicateToDouble(A);
        double[][] Bi = inverse(B);
        if (Bi == null) {
            return null;
        } else {
            return duplicateToFloat(Bi);
        }
    }

    // ------------------------------------------------------------------------
    // Finds the EXACT solution x for A.x = b
    public static double[] solve(final double[][] A, double[] b) {
        RealMatrix AA = MatrixUtils.createRealMatrix(A);
        RealVector bb = MatrixUtils.createRealVector(b);
        DecompositionSolver solver = new LUDecomposition(AA).getSolver();
        double[] x = null;
        try {
            x = solver.solve(bb).toArray();
        } catch (SingularMatrixException e) {
        }
        return x;
    }

}
