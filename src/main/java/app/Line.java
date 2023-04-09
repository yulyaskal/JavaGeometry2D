package app;

import misc.Vector2d;

/**
 * Класс линии
 */
public class Line {
    /**
     * Первая опорная точка
     */
    final Vector2d pointA;
    /**
     * Вторая опорная точка
     */
    final Vector2d pointB;
    /**
     * Первый коэффициент канонического уравнения прямой
     */
    private final double a;
    /**
     * Второй коэффициент канонического уравнения прямой
     */
    private final double c;
    /**
     * Третий коэффициент канонического уравнения прямой
     */
    private final double b;

    /**
     * Конструктор линии
     *
     * @param pointA первая опорная точка
     * @param pointB вторая опорная точка
     */
    public Line(Vector2d pointA, Vector2d pointB) {
        this.pointA = pointA;
        this.pointB = pointB;

        a = pointA.y - pointB.y;
        b = pointB.x - pointA.x;
        c = pointA.x * pointB.y - pointB.x * pointA.y;
    }
    /**
     * Точка пересечения данной прямой с передаваемой в параметрах
     *
     * @return точка пересечения прямых
     */
    public Vector2d intersection(Line l)
    {
        if (0.001 > Math.abs(this.a * l.b - this.b * l.a))
        {
            return null;
        }
        else
        {
            double d = this.a * l.b - this.b * l.a;
            double d1 = -this.c * l.b + this.b * l.c;
            double d2 = -l.c * this.a + this.c * l.a;
            return new Vector2d(d1/d, d2/d);
        }
    }
    /**
     * Получить расстояние до точки
     *
     * @param pos координаты точки
     * @return расстояние
     */
    public double getDistance(Vector2d pos) {
        return Math.abs(a * pos.x + b * pos.y + c) / Math.sqrt(a * a + b * b);
    }
}