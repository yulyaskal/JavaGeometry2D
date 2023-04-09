package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.humbleui.jwm.MouseButton;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Rect;
import lombok.Getter;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;
import panels.PanelLog;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static app.Colors.*;

/**
 * Класс задачи
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class Task {
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
            ПОСТАНОВКА ЗАДАЧИ:
            На плоскости задано множество прямоугольников. Найти такую пару пересекающихся прямоугольников,
            что длина отрезка, проведенного от одной точки пересечения этих двух прямоугольников до другой,
            максимальна. Если прямоугольники имеют более двух точек пересечения, выбирать среди них такую пару,
            расстояние между которыми максимально. В качестве ответа: выделить эту пару прямоугольников,
            нарисовать отрезок между найденными точками пересечения.""";

    /**
     * Вещественная система координат задачи
     */
    @Getter
    private final CoordinateSystem2d ownCS;
    /**
     * Список точек
     */
    @Getter
    private final ArrayList<Point> points;
    /**
     * Список прямоугольников
     */
    @Getter
    private final ArrayList<app.Rect> rects;
    /**
     * Размер точки
     */
    private static final int POINT_SIZE = 3;

    /**
     * последняя СК окна
     */
    protected CoordinateSystem2i lastWindowCS;
    /**
     * Флаг, решена ли задача
     */
    private boolean solved;
    /**
     * Список точек в пересечении
     */
    @Getter
    @JsonIgnore
    private final ArrayList<Point> crossed;
    /**
     * Список точек в разности
     */
    @Getter
    @JsonIgnore
    private final ArrayList<Point> single;
    /**
     * Список точек пересечения
     */
    @Getter
    @JsonIgnore
    private final ArrayList<Vector2d> interPoints;
    /**
     * Задача
     *
     * @param ownCS  СК задачи
     * @param points массив точек
     */
    @JsonCreator
    public Task(
            @JsonProperty("ownCS") CoordinateSystem2d ownCS,
            @JsonProperty("points") ArrayList<Point> points,
            @JsonProperty("rects") ArrayList<app.Rect> rects
    ) {
        this.ownCS = ownCS;
        this.points = points;
        this.rects = rects;
        this.crossed = new ArrayList<>();
        this.single = new ArrayList<>();
        this.interPoints = new ArrayList<>();
    }

    /**
     * Рисование задачи
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        // Сохраняем последнюю СК
        lastWindowCS = windowCS;

        canvas.save();
        // создаём перо
        try (var paint = new Paint()) {
            for (app.Rect r : rects) {
                // y-координату разворачиваем, потому что у СК окна ось y направлена вниз,
                // а в классическом представлении - вверх
                Vector2i windowPosA = windowCS.getCoords(r.pointA.x, r.pointA.y, ownCS);
                Vector2i windowPosB = windowCS.getCoords(r.pointB.x, r.pointB.y, ownCS);
                Vector2i windowPosP = windowCS.getCoords(r.pointP.x, r.pointP.y, ownCS);
                // создаём линию
                Line line = new Line(new Vector2d(windowPosA), new Vector2d(windowPosB));
                // рассчитываем расстояние от прямой до точки
                double dist = line.getDistance(new Vector2d(windowPosP));
                // рассчитываем векторы для векторного умножения
                Vector2d AB = Vector2d.subtract(new Vector2d(windowPosB), new Vector2d(windowPosA));
                Vector2d AP = Vector2d.subtract(new Vector2d(windowPosP), new Vector2d(windowPosA));
                // определяем направление смещения
                double direction = Math.signum(AB.cross(AP));
                // получаем вектор смещения
                Vector2i offset = AB.rotated(Math.PI / 2 * direction).norm().mult(dist).intVector();

                // находим координаты вторых двух вершин прямоугольника
                Vector2i pointC = Vector2i.sum(windowPosB, offset);
                Vector2i pointD = Vector2i.sum(windowPosA, offset);
                //запоминаем координаты всех четырех вершин
                r.pA = windowPosA;
                r.pB = windowPosB;
                r.pC = pointC;
                r.pD = pointD;
                paint.setColor(LABEL_TEXT_COLOR);
                // рисуем его стороны
                canvas.drawLine(windowPosA.x, windowPosA.y, windowPosB.x, windowPosB.y, paint);
                canvas.drawLine(windowPosB.x, windowPosB.y, pointC.x, pointC.y, paint);
                canvas.drawLine(pointC.x, pointC.y, pointD.x, pointD.y, paint);
                canvas.drawLine(pointD.x, pointD.y, windowPosA.x, windowPosA.y, paint);
            }
        }
        canvas.restore();
    }

    /**
     * Добавить точку
     *
     * @param pos      положение
     * @param pointSet множество
     */
    public void addPoint(Vector2d pos, Point.PointSet pointSet) {
        solved = false;
        Point newPoint = new Point(pos, pointSet);
        points.add(newPoint);
        // Добавляем в лог запись информации
        PanelLog.info("точка " + newPoint + " добавлена в " + newPoint.getSetName());
    }
    /**
     * Добавить прямоугольник
     *
     * @param pointA    угол прямоугольника
     * @param pointB    угол прямоугольника
     * @param pointP    точка на другой стороне прямоугольника
     */
    public void addRect(Vector2d pointA, Vector2d pointB, Vector2d pointP) {
        solved = false;
        app.Rect newRect = new app.Rect(pointA, pointB, pointP);
        rects.add(newRect);
        // Добавляем в лог запись информации
        PanelLog.info("Прямоугольник " + newRect + " добавлен");
    }
    /**
     * Клик мыши по пространству задачи
     *
     * @param pos         положение мыши
     * @param mouseButton кнопка мыши
     */
    public void click(Vector2i pos, MouseButton mouseButton) {
        if (lastWindowCS == null) return;
        // получаем положение на экране
        Vector2d taskPos = ownCS.getCoords(pos, lastWindowCS);
        // если левая кнопка мыши, добавляем в первое множество
        if (mouseButton.equals(MouseButton.PRIMARY)) {
            addPoint(taskPos, Point.PointSet.FIRST_SET);
            // если правая, то во второе
        } else if (mouseButton.equals(MouseButton.SECONDARY)) {
            addPoint(taskPos, Point.PointSet.SECOND_SET);
        }
    }
    /**
     * Добавить случайные точки
     *
     * @param cnt кол-во случайных точек
     */
    public void addRandomPoints(int cnt) {
        // если создавать точки с полностью случайными координатами,
        // то вероятность того, что они совпадут крайне мала
        // поэтому нужно создать вспомогательную малую целочисленную ОСК
        // для получения случайной точки мы будем запрашивать случайную
        // координату этой решётки (их всего 30х30=900).
        // после нам останется только перевести координаты на решётке
        // в координаты СК задачи
        CoordinateSystem2i addGrid = new CoordinateSystem2i(30, 30);

        // повторяем заданное количество раз
        for (int i = 0; i < cnt; i++) {
            // получаем случайные координаты на решётке
            Vector2i gridPos = addGrid.getRandomCoords();
            // получаем координаты в СК задачи
            Vector2d pos = ownCS.getCoords(gridPos, addGrid);
            // сработает примерно в половине случаев
            if (ThreadLocalRandom.current().nextBoolean())
                addPoint(pos, Point.PointSet.FIRST_SET);
            else
                addPoint(pos, Point.PointSet.SECOND_SET);
        }
    }

    /**
     * Очистить задачу
     */
    public void clear() {
        points.clear();
        solved = false;
    }
    /**
     * Решить задачу
     */
    public void solve() {
        // очищаем списки
        crossed.clear();
        single.clear();

        // перебираем пары точек
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                // сохраняем точки
                Point a = points.get(i);
                Point b = points.get(j);
                // если точки совпадают по положению
                if (a.pos.equals(b.pos) && !a.pointSet.equals(b.pointSet)) {
                    if (!crossed.contains(a)){
                        crossed.add(a);
                        crossed.add(b);
                    }
                }
            }
        }

        /// добавляем вс
        for (Point point : points)
            if (!crossed.contains(point))
                single.add(point);

        // задача решена
        solved = true;
    }
    /**
     * Отмена решения задачи
     */
    public void cancel() {
        solved = false;
    }
    /**
     * проверка, решена ли задача
     *
     * @return флаг
     */
    public boolean isSolved() {
        return solved;
    }
}