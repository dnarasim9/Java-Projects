package objectrecognition;

public class EigenFaceComputation {

    private final static int MAGIC_NR = 11;

    public static FaceBundle submit(double[][] face_v, int width, int height, String[] id,
                                    boolean debug) {

        int length = width * height;
        int nrfaces = face_v.length;
        int i, j, col, rows, pix, image;
        double temp = 0.0;
        double[][] faces = new double[nrfaces][length];
        double[] avgF = new double[length];

   	 /*
  	   Compute average face of all of the faces. 1xN^2
    	 */

        for (pix = 0; pix < length; pix++) {
            temp = 0;
            for (image = 0; image < nrfaces; image++) {
                temp += face_v[image][pix];
            }
            avgF[pix] = temp / nrfaces;
        }

        /* compute difference images*/

        for (image = 0; image < nrfaces; image++) {
            for (pix = 0; pix < length; pix++) {
                face_v[image][pix] = face_v[image][pix] - avgF[pix];
            }
        }

        Matrix faceM = new Matrix(face_v, nrfaces, length);
        Matrix faceM_transpose = faceM.transpose();
        Matrix covarM = faceM.times(faceM_transpose);

    /*
     Compute eigenvalues and eigenvector. Both are MxM
    */

        EigenvalueDecomposition E = covarM.eig();

        double[] eigValue = diag(E.getD().getArray());
        double[][] eigVector = E.getV().getArray();

        /*
         * We only need the largest associated values of the eigenvalues.
         * Thus we sort them (and keep an index of them)
         */

        int[] index = new int[nrfaces];
        double[][] tempVector = new double[nrfaces][nrfaces];  /* Temporary new eigVector */

        for (i = 0; i < nrfaces; i++) /* Enumerate all the entries */
            index[i] = i;

        doubleQuickSort(eigValue, index, 0, nrfaces - 1);
        int[] tempV = new int[nrfaces];
        for (j = 0; j < nrfaces; j++)
            tempV[nrfaces - 1 - j] = index[j];
        index = tempV;

        for (col = nrfaces - 1; col >= 0; col--) {
            for (rows = 0; rows < nrfaces; rows++) {
                tempVector[rows][col] = eigVector[rows][index[col]];
            }
        }
        eigVector = tempVector;
        tempVector = null;
        eigValue = null;

        Matrix eigVectorM = new Matrix(eigVector, nrfaces, nrfaces);
        eigVector = eigVectorM.times(faceM).getArray();

        for (image = 0; image < nrfaces; image++) {
            temp = max(eigVector[image]);
            for (pix = 0; pix < eigVector[0].length; pix++)
                eigVector[image][pix] = Math.abs(eigVector[image][pix] / temp);
        }

        double[][] wk = new double[nrfaces][MAGIC_NR]; // M rows, 11 columns




/*
  	Compute wk.
  	*/

        for (image = 0; image < nrfaces; image++) {
            for (j = 0; j < MAGIC_NR; j++) {
                temp = 0.0;
                for (pix = 0; pix < length; pix++)
                    temp += eigVector[j][pix] * faces[image][pix];
                wk[image][j] = Math.abs(temp);
            }
        }

        FaceBundle b = new FaceBundle(avgF, wk, eigVector, id);

        double[] inputFace = new double[length];
        System.arraycopy(faces[14], 0, inputFace, 0, length);

        return b;
    }


    static double[] diag(double[][] m) {

        double[] d = new double[m.length];
        for (int i = 0; i < m.length; i++)
            d[i] = m[i][i];
        return d;
    }

    static void divide(double[] v, double b) {
        for (int i = 0; i < v.length; i++)
            v[i] = v[i] / b;
    }

    static double sum(double[] a) {
        double b = a[0];
        for (int i = 0; i < a.length; i++)
            b += a[i];
        return b;
    }

    static double max(double[] a) {
        double b = a[0];
        for (int i = 0; i < a.length; i++)
            if (a[i] > b) b = a[i];
        return b;
    }

// Quick Sort

    static void doubleQuickSort(double a[], int index[], int lo0, int hi0) {
        int lo = lo0;
        int hi = hi0;
        double mid;

        if (hi0 > lo0) {
            mid = a[(lo0 + hi0) / 2];
            while (lo <= hi) {
                while ((lo < hi0) && (a[lo] < mid))
                    ++lo;
                while ((hi > lo0) && (a[hi] > mid))
                    --hi;

                if (lo <= hi) {
                    swap(a, index, lo, hi);
                    ++lo;
                    --hi;
                }
            }
            if (lo0 < hi) {
                doubleQuickSort(a, index, lo0, hi);
            }
            if (lo < hi0) {
                doubleQuickSort(a, index, lo, hi0);
            }
        }
    }
}