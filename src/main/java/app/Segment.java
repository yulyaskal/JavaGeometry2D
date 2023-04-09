package app;

import misc.Vector2d;

import java.util.Objects;

public class Segment { //класс отрезков, нужен для решения задачи и рисования ответа
    /**
     * Начало и конец отрезка, а также прямоугольники, между точками пересечения которых этот отрезок проведен
     */
    final Vector2d pointA;
    final Vector2d pointB;
    final Rect rect1;
    final Rect rect2;
    /**
     * Конструктор отрезка
     *
     * @param pointA положение первой точки отрезка
     * @param pointB положение второй точки отрезка
     * @param rect1 положение точки на противоположной стороне прямоугольника
     * @param rect2 положение точки на противоположной стороне прямоугольника
     */
    public Segment(Vector2d pointA, Vector2d pointB, Rect rect1, Rect rect2) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.rect1 = rect1;
        this.rect2 = rect2;
    }
    /**
     * Получить длину отрезка
     *
     * @return длина отрезка
     */
    public double length (){
        return Math.sqrt((this.pointA.x-this.pointB.x)*(this.pointA.x-this.pointB.x)+(this.pointA.y-this.pointB.y)*(this.pointA.y-this.pointB.y));
    }
    /**
     * Строковое представление объекта
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Segment{" +
                "pointA=" + pointA +
                ", pointB=" + pointB +
                ", rect1=" + rect1 +
                ", rect2=" + rect2 +
                '}';
    }
    /**
     * Получить хэш-код объекта
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(pointA, pointB, rect1, rect2);
    }
}