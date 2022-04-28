
package kurz;

public class Point {

    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Matrix asVerticalVector() {
        return Matrix.createVectorVertical(x, y);
    }

    public Matrix asHorizontalVector() {
        return Matrix.createVectorHorizontal(x, y);
    }

    public void transform(Matrix m) {
        Matrix r = m.multiply(asVerticalVector());
        x = Matrix.getVectorX(r);
        y = Matrix.getVectorY(r);
    }
}
