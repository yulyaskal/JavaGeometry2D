package app;

public class Rect {

    double width;
    double height;
    public double getArea() {
        return width*height;
    }
    public double getPerimeter() {
        return  width+height+width+height;
    }
    public void modify(double width, double height) {
        this.width=width;
        this.height=height;
    }
    public void scale(double d) {
        width*=d;
        height*=d;
    }
    public String toString() {
        return "Rect{width=" + String.format("%.3f", width) + ",height=" + String.format("%.3f", height) + "}";
    }
    public Rect getAFourth() {
        Rect rect = new Rect();
        rect.modify(width/2,height/2);
        return rect;
    }
    public Rect devide(double w,double h) {
        Rect rect = new Rect();
        rect.modify(width*w,height*h);
        return rect;
    }
}
