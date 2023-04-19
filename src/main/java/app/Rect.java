package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import misc.Vector2d;
import misc.Vector2i;

import java.util.Objects;

public class Rect { //Прямоугольник задается двумя вершинами одной из сторон, а также точкой, лежащей на прямой, проходящей через две другие вершины.

    /**
     * Вершины, прямоугольника и точка, нужная для его задания
     */
    public final Vector2d pointA;
    public final Vector2d pointB;
    public final Vector2d pointC;
    public final Vector2d pointD;
    public final Vector2d pointP;
    /**
     * Конструктор прямоугольника
     *
     * @param pointA положение первой вершины прямоугольника
     * @param pointB положение второй вершины прямоугольника
     * @param pointP положение точки на противоположной стороне прямоугольника
     */
    @JsonCreator
    public Rect(@JsonProperty("pointA") Vector2d pointA, @JsonProperty("pointB") Vector2d pointB, @JsonProperty("pointP") Vector2d pointP) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.pointP = pointP;
        Line line = new Line(this.pointA, this.pointB);
        // рассчитываем расстояние от прямой до точки
        double dist = line.getDistance(this.pointP);
        // рассчитываем векторы для векторного умножения
        Vector2d AB = Vector2d.subtract(this.pointB, this.pointA);
        Vector2d AP = Vector2d.subtract(this.pointP, this.pointA);
        // определяем направление смещения
        double direction = Math.signum(AB.cross(AP));
        // получаем вектор смещения
        Vector2d offset = AB.rotated(Math.PI / 2 * direction).norm().mult(dist);
        this.pointC = Vector2d.sum(this.pointB, offset);
        this.pointD = Vector2d.sum(this.pointA, offset);
    }
    /**
     * Получить положение первой вершины прямоугольника
     *
     * @return положение первой вершины прямоугольника
     */
    public Vector2d getpointA(){
        return pointA;
    }
    /**
     * Получить положение второй вершины прямоугольника
     *
     * @return положение второй вершины прямоугольника
     */
    public Vector2d getpointB(){
        return pointB;
    }
    /**
     * Получить положение точки на противоположной стороне прямоугольника
     *
     * @return положение точки на противоположной стороне прямоугольника
     */
    public Vector2d getpointP(){
        return pointP;
    }

    /**
     * Строковое представление объекта
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Rectangle{" +
                "pointA=" + pointA +
                ", pointB=" + pointB +
                ", pointP=" + pointP +
                '}';
    }
    /**
     * Получить хэш-код объекта
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(pointA, pointB, pointP);
    }
}
