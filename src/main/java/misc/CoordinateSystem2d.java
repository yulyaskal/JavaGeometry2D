package misc;

import java.util.Objects;

/**
 * ограниченной двумерной вещественной системы координат
 */
public class CoordinateSystem2d {
    /**
     * максимальная координата
     */

    private Vector2d max;
    /**
     * минимальная координата
     */

    private Vector2d min;
    /**
     * размер СК
     */
    private Vector2d size;

    /**
     * Конструктор ограниченной двумерной вещественной системы координат
     *
     * @param minX  минимальная X координата
     * @param minY  минимальная Y координата
     * @param sizeX размер по оси X
     * @param sizeY размер по оси Y
     */
    public CoordinateSystem2d(double minX, double minY, double sizeX, double sizeY) {
        set(minX, minY, sizeX, sizeY);
    }

    /**
     * Конструктор ограниченной двумерной вещественной системы координат
     *
     * @param sizeX размер по оси X
     * @param sizeY размер по оси Y
     */
    public CoordinateSystem2d(double sizeX, double sizeY) {
        this(0, 0, sizeX, sizeY);
    }


    /**
     * Конструктор ограниченной двумерной вещественной системы координат
     *
     * @param min минимальные координаты
     * @param max максимальные координаты
     */
    public CoordinateSystem2d(Vector2d min, Vector2d max) {
        this(min.x, min.y, max.x - min.x, max.y - min.y);
    }


    /**
     * Задать новые границы
     *
     * @param minX  минимальная x-координата
     * @param minY  максимальная x-координата
     * @param sizeX размер по оси X
     * @param sizeY размер по оси Y
     */
    public void set(double minX, double minY, double sizeX, double sizeY) {
        min = new Vector2d(minX, minY);
        size = new Vector2d(sizeX, sizeY);
        max = Vector2d.sum(size, min);
    }


    /**
     * Получить случайные координаты внутри СК
     *
     * @return случайные координаты внутри СК
     */
    public Vector2d getRandomCoords() {
        Vector2d r = Vector2d.rand(min, max);
        return r;
    }

    /**
     * Проверить, попадают ли координаты в границы СК
     *
     * @param coords координаты вектора
     * @return флаг, попадают ли координаты в границы СК
     */
    public boolean checkCoords(Vector2d coords) {
        return coords.x >= min.x && coords.y >= min.y && coords.x <= max.x && coords.y <= max.y;
    }

    /**
     * Проверить, попадают ли координаты в границы СК
     *
     * @param x координата X
     * @param y координата Y
     * @return флаг, попадают ли координаты в границы СК
     */
    public boolean checkCoords(double x, double y) {
        return checkCoords(new Vector2d(x, y));
    }


    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    public Vector2d getCoords(Vector2d coords, CoordinateSystem2d coordinateSystem) {
        return getCoords(coords.x, coords.y, coordinateSystem);
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    public Vector2d getCoords(double x, double y, CoordinateSystem2d coordinateSystem) {
        return new Vector2d(
                (x - coordinateSystem.min.x) * size.x / coordinateSystem.size.x + min.x,
                (y - coordinateSystem.min.y) * size.y / coordinateSystem.size.y + min.y
        );
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param coords           координаты вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    public Vector2d getCoords(Vector2i coords, CoordinateSystem2i coordinateSystem) {
        return this.getCoords(coords.x, coords.y, coordinateSystem);
    }

    /**
     * Получить координаты вектора в текущей систему координат
     *
     * @param x                координата X вектора в другой системе координат
     * @param y                координата Y вектора в другой системе координат
     * @param coordinateSystem система координат, в которой заданы координаты вектора
     * @return координаты вектора в текущей системе координат
     */
    public Vector2d getCoords(int x, int y, CoordinateSystem2i coordinateSystem) {
        return new Vector2d(
                (x - coordinateSystem.getMin().x) * size.x / (coordinateSystem.getSize().x - 1) + min.x,
                (y - coordinateSystem.getMin().y) * size.y / (coordinateSystem.getSize().y - 1) + min.y
        );
    }

    /**
     * Получить максимальную координата
     *
     * @return максимальная координата
     */
    public Vector2d getMax() {
        return max;
    }

    /**
     * Получить минимальную координата
     *
     * @return минимальная координата
     */
    public Vector2d getMin() {
        return min;
    }

    /**
     * Получить размер СК
     *
     * @return размер СК
     */
    public Vector2d getSize() {
        return size;
    }

    /**
     * Строковое представление объекта вида:
     *
     * @return "CoordinateSystem2d{min, max}"
     */
    @Override
    public String toString() {
        return "CoordinateSystem2d{" + min + ", " + max + '}';
    }

    /**
     * Проверка двух объектов на равенство
     *
     * @param o объект, с которым сравниваем текущий
     * @return флаг, равны ли два объекта
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoordinateSystem2d that = (CoordinateSystem2d) o;

        if (!Objects.equals(max, that.max)) return false;
        return Objects.equals(min, that.min);
    }

    /**
     * Получить хэш-код объекта
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        int result = max != null ? max.hashCode() : 0;
        result = 31 * result + (min != null ? min.hashCode() : 0);
        return result;
    }
}