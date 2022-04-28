package kurz;

public class Matrix {

    public static Matrix createVectorHorizontal(double x, double y) {
        Matrix m = new Matrix(1, 3);
        m.m[0][0] = x;
        m.m[0][1] = y;
        m.m[0][2] = 1;
        return m;
    }

    public static Matrix createVectorVertical(double x, double y) {
        // return createVectorHorizontal(x, y).transpose();
        Matrix m = new Matrix(3, 1);
        m.m[0][0] = x;
        m.m[1][0] = y;
        m.m[2][0] = 1;
        return m;
    }

    public static Matrix createTranslationTransform(double x, double y) {
        Matrix m = new Matrix(3, 3);
        m.m[0][0] = 1;
        m.m[1][1] = 1;
        m.m[2][2] = 1;
        m.m[0][2] = x;
        m.m[1][2] = y;
        return m;
    }

    public static Matrix createScaleTransform(double k) {
        return createScaleTransform(k, k);
    }

    public static Matrix createScaleTransform(double x, double y) {
        Matrix m = new Matrix(3, 3);
        m.m[0][0] = x;
        m.m[1][1] = y;
        m.m[2][2] = 1;
        return m;
    }

    public static Matrix createSymmetryXTransform() {
        return createScaleTransform(1, -1);
    }

    public static Matrix createSymmetryYTransform() {
        return createScaleTransform(-1, 1);
    }

    public static Matrix createSymmetryOriginTransform() {
        return createScaleTransform(-1, -1);
    }

    public static Matrix createRotateTransform(double a) {
        Matrix m = new Matrix(3, 3);
        double cos = Math.cos(a);
        double sin = Math.sin(a);
        m.m[0][0] = cos;
        m.m[0][1] = -sin;
        m.m[1][0] = sin;
        m.m[1][1] = cos;
        m.m[2][2] = 1;
        return m;
    }

    public static double getVectorX(Matrix m) {
        if (m.width == 1) {
            if (m.height != 3) {
                throw new IllegalArgumentException("not 3x1");
            }
            return m.m[0][0];
        } else if(m.height == 1){
            if (m.width != 3) {
                throw new IllegalArgumentException("not 1x3");
            }
            return m.m[0][0];
        }
        throw new IllegalArgumentException("not a vector");
    }
    
    public static double getVectorY(Matrix m) {
        if (m.width == 1) {
            if (m.height != 3) {
                throw new IllegalArgumentException("Not 3x1");
            }
            return m.m[1][0];
        } else if (m.height == 1) {
            if (m.width != 3) {
                throw new IllegalArgumentException("Not 3x1");

            }
            return m.m[1][0];
        }
        throw new IllegalArgumentException("Not 3x1");
    }
    
    private final int height;
    private final int width;
    private  double[][] m;

    private Matrix(int height, int width) {
        this.height = height;
        this.width = width;
        this.m = new double[height][width];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        double gap = 0;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                gap = Math.max(gap, String.valueOf(m[r][c]).length());
            }
        }
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int length = String.valueOf(m[r][c]).length();
                sb.append(m[r][c]);
                for (int x = 0; x < gap - length; x++) {
                    sb.append(' ');
                }
                sb.append('\t');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public Matrix transpose() {
        Matrix m = new Matrix(width, height);
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                m.m[c][r] = this.m[r][c];
            }
        }
        return m;
    }

    public Matrix multiply(double k) {
        Matrix m = new Matrix(height, width);
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                m.m[r][c] = k * this.m[r][c];
            }
        }
        return m;
    }

    /**
     * Vol�n�: A.multiply(B) - A * B
     *
     * @param x
     * @return this * x
     */
    public Matrix multiply(Matrix x) {
        if (this.width != x.height) {
            throw new IllegalArgumentException("width vs height");
        }
        int height = this.height;
        int width = x.width;
        int common = this.width; // nebo x.height
        Matrix m = new Matrix(height, width);
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                for (int i = 0; i < common; i++) {
                    m.m[r][c] += this.m[r][i] * x.m[i][c];
                }
            }
        }
        return m;
    }

    public Matrix add(Matrix a) {
        if ((a.width != this.width) || (a.height != this.height)) {
            throw new IllegalArgumentException("Matrix sizes do not match");
        }
        Matrix m = new Matrix(height, width);
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                m.m[r][c] = this.m[r][c] + a.m[r][c];
            }
        }
        return m;
    }
    
    public static void main(String[] args) {
//        Matrix m = Matrix.createTranslationTransform(-1, 2);
//        Matrix p = Matrix.createVectorVertical(2, 1);
//        System.out.println(m.multiply(p).transpose()); // M * P
        Matrix m = new Matrix(3, 3);
        m.m = new double[][]{{1.00001001, 1.31203910293091023, 31231}, {1.0001, 1.3023, 331}, {1.01001, 1.312023213123910293091023, 3123}};
        System.out.println(m);
    }

}