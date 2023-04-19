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
     * Список прямоугольников
     */
    @Getter
    private final ArrayList<app.Rect> rects;
    /**
     * Счетчик кликов
     */
    private static int MouseCount = 0;
    /**
     * Массив кликов, после его заполнения создается прямоугольник
     */
    private final Vector2d[] clicks = new Vector2d[3];
    /**
     * последняя СК окна
     */
    protected CoordinateSystem2i lastWindowCS;
    /**
     * Флаг, решена ли задача
     */
    private boolean solved;
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
     * @param rects массив прямоуголь
     */
    @JsonCreator
    public Task(
            @JsonProperty("ownCS") CoordinateSystem2d ownCS,
            @JsonProperty("rects") ArrayList<app.Rect> rects
    ) {
        this.ownCS = ownCS;
        this.rects = rects;
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
                paint.setColor(LABEL_TEXT_COLOR);
                // координаты прямоугольника в СК окна
                // y-координату разворачиваем, потому что у СК окна ось y направлена вниз,
                // а в классическом представлении - вверх
                Vector2i windowPosA = windowCS.getCoords(r.pointA.x, r.pointA.y, ownCS);
                Vector2i windowPosB = windowCS.getCoords(r.pointB.x, r.pointB.y, ownCS);
                Vector2i windowPosC = windowCS.getCoords(r.pointC.x, r.pointC.y, ownCS);
                Vector2i windowPosD = windowCS.getCoords(r.pointD.x, r.pointD.y, ownCS);
                // рисуем его стороны
                canvas.drawLine(windowPosA.x, windowPosA.y, windowPosB.x, windowPosB.y, paint);
                canvas.drawLine(windowPosB.x, windowPosB.y, windowPosC.x, windowPosC.y, paint);
                canvas.drawLine(windowPosC.x, windowPosC.y, windowPosD.x, windowPosD.y, paint);
                canvas.drawLine(windowPosD.x, windowPosD.y, windowPosA.x, windowPosA.y, paint);
                if ((solved) && (answer)){
                    paint.setColor(SUBTRACTED_COLOR);
                    // координаты отрезка в СК окна
                    Vector2i windowResultA = windowCS.getCoords(result.pointA.x, result.pointA.y, ownCS);
                    Vector2i windowResultB = windowCS.getCoords(result.pointB.x, result.pointB.y, ownCS);
                    // рисуем отрезок-ответ
                    canvas.drawLine(windowResultA.x, windowResultA.y, windowResultB.x, windowResultB.y, paint);
                    paint.setColor(CROSSED_COLOR);
                    // координаты прямоугольника-ответа в СК окна
                    Vector2i windowPosA1 = windowCS.getCoords(result.rect1.pointA.x, result.rect1.pointA.y, ownCS);
                    Vector2i windowPosB1 = windowCS.getCoords(result.rect1.pointB.x, result.rect1.pointB.y, ownCS);
                    Vector2i windowPosC1 = windowCS.getCoords(result.rect1.pointC.x, result.rect1.pointC.y, ownCS);
                    Vector2i windowPosD1 = windowCS.getCoords(result.rect1.pointD.x, result.rect1.pointD.y, ownCS);
                    // рисуем ответ
                    canvas.drawLine(windowPosA1.x, windowPosA1.y, windowPosB1.x, windowPosB1.y, paint);
                    canvas.drawLine(windowPosB1.x, windowPosB1.y, windowPosC1.x, windowPosC1.y, paint);
                    canvas.drawLine(windowPosC1.x, windowPosC1.y, windowPosD1.x, windowPosD1.y, paint);
                    canvas.drawLine(windowPosD1.x, windowPosD1.y, windowPosA1.x, windowPosA1.y, paint);
                    // координаты прямоугольника-ответа в СК окна
                    Vector2i windowPosA2 = windowCS.getCoords(result.rect2.pointA.x, result.rect2.pointA.y, ownCS);
                    Vector2i windowPosB2 = windowCS.getCoords(result.rect2.pointB.x, result.rect2.pointB.y, ownCS);
                    Vector2i windowPosC2 = windowCS.getCoords(result.rect2.pointC.x, result.rect2.pointC.y, ownCS);
                    Vector2i windowPosD2 = windowCS.getCoords(result.rect2.pointD.x, result.rect2.pointD.y, ownCS);
                    // рисуем ответ
                    canvas.drawLine(windowPosA2.x, windowPosA2.y, windowPosB2.x, windowPosB2.y, paint);
                    canvas.drawLine(windowPosB2.x, windowPosB2.y, windowPosC2.x, windowPosC2.y, paint);
                    canvas.drawLine(windowPosC2.x, windowPosC2.y, windowPosD2.x, windowPosD2.y, paint);
                    canvas.drawLine(windowPosD2.x, windowPosD2.y, windowPosA2.x, windowPosA2.y, paint);
                }
            }
        }
        canvas.restore();
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
        clicks[MouseCount % 3] = ownCS.getCoords(pos, lastWindowCS);
        //PanelLog.info("(" + clicks[(MouseCount+1) % 2] + ")");
        if (mouseButton.equals(MouseButton.PRIMARY)) {
            MouseCount++;
            if ((MouseCount % 3 == 0) && (MouseCount > 0))
                addRect(clicks[0], clicks[1], clicks[2]);
        }
    }
    /**
     * Добавить случайные точки
     *
     * @param cnt кол-во случайных точек
     */
    public void addRandomRects(int cnt) {
        // если создавать точки с полностью случайными координатами,
        // то вероятность того, что они совпадут крайне мала
        // поэтому нужно создать вспомогательную малую целочисленную ОСК
        // для получения случайной точки мы будем запрашивать случайную
        // координату этой решётки (их всего 100х100=10000).
        // после нам останется только перевести координаты на решётке
        // в координаты СК задачи
        CoordinateSystem2i addGrid = new CoordinateSystem2i(100, 100);

        // повторяем заданное количество раз
        for (int i = 0; i < cnt; i++) {
            // получаем случайные координаты на решётке
            Vector2i gridPos1 = addGrid.getRandomCoords();
            // получаем координаты в СК задачи
            Vector2d pos1 = ownCS.getCoords(gridPos1, addGrid);
            // получаем случайные координаты на решётке
            Vector2i gridPos2 = addGrid.getRandomCoords();
            // получаем координаты в СК задачи
            Vector2d pos2 = ownCS.getCoords(gridPos2, addGrid);
            // получаем случайные координаты на решётке
            Vector2i gridPos3 = addGrid.getRandomCoords();
            // получаем координаты в СК задачи
            Vector2d pos3 = ownCS.getCoords(gridPos3, addGrid);
            addRect(pos1, pos2, pos3);
        }
    }
    /**
     * Список точек пересечения прямоугольников
     */
    ArrayList<Vector2d> InterPoints = new ArrayList<>();
    /**
     * Список отрезков, из которых будем искать максимальный
     */
    ArrayList<Segment> segments = new ArrayList<>();
    /**
     * Отрезок - ответ в задаче
     */
    @Getter
    @JsonIgnore
    Segment result = null;
    /**
     * Существует ли ответ
     */
    boolean answer = false;
    /**
     * Очистить задачу
     */
    public void clear() {
        rects.clear();
        solved = false;
    }
    /**
     * Решить задачу
     */
    public void solve() {
        // очищаем списки
        InterPoints.clear();
        segments.clear();
        answer = false;
        double maxlength = 0.0;  //максимальная длина отрезка
        for (int i = 0; i < rects.size(); i++) {
            for (int j = i + 1; j < rects.size(); j++) {  //перебираем все пары прямоугольников
                app.Rect first = rects.get(i);
                app.Rect second = rects.get(j);
                Line[] lines = new Line[8];
                lines[0] = new Line(first.pointA, first.pointB);
                lines[1] = new Line(first.pointA, first.pointD);
                lines[2] = new Line(first.pointB, first.pointC);
                lines[3] = new Line(first.pointC, first.pointD);
                lines[4] = new Line(second.pointA, second.pointB);
                lines[5] = new Line(second.pointA, second.pointD);
                lines[6] = new Line(second.pointB, second.pointC);
                lines[7] = new Line(second.pointC, second.pointD);  //8 прямых, построенных по сторонам прямоугольников
                for (int k = 0; k < 4; k++) {
                    for (int l = 4; l < 8; l++) {  //перебираем все пары прямых
                        Vector2d InterCandidate = lines[k].intersection(lines[l]);  //точка пересечения прямых, она может лежать либо на отрезках, либо вне их
                        if (InterCandidate != null) {  //если точка пересечения есть
                            if ((((InterCandidate.x >= lines[k].pointA.x) && (InterCandidate.x <= lines[k].pointB.x))
                                    || (InterCandidate.x <= lines[k].pointA.x) && (InterCandidate.x >= lines[k].pointB.x))
                                    && (((InterCandidate.y >= lines[k].pointA.y) && (InterCandidate.y <= lines[k].pointB.y))
                                    || (InterCandidate.y <= lines[k].pointA.y) && (InterCandidate.y >= lines[k].pointB.y)))  //принадлежит ли точка первому отрезку
                            {
                                if ((((InterCandidate.x >= lines[l].pointA.x) && (InterCandidate.x <= lines[l].pointB.x))
                                        || (InterCandidate.x <= lines[l].pointA.x) && (InterCandidate.x >= lines[l].pointB.x))
                                        && (((InterCandidate.y >= lines[l].pointA.y) && (InterCandidate.y <= lines[l].pointB.y))
                                        || (InterCandidate.y <= lines[l].pointA.y) && (InterCandidate.y >= lines[l].pointB.y))) //принадлежит ли точка второму отрезку
                                {
                                    InterPoints.add(InterCandidate);  //если лежит на обоих отрезках, добавляем в список точек пересечения
                                }
                            }
                        }
                    }
                }
                for (int k = 0; k < InterPoints.size(); k++) {
                    for (int l = k; l < InterPoints.size(); l++) {  //перебираем все точки пересечения (для данной пары прямоугольников)
                        segments.add(new Segment(InterPoints.get(k), InterPoints.get(l), first, second));  //добавляем все отрезки в список
                    }
                }
                InterPoints.clear();
            }
        }
        for (Segment segment : segments) {
            if (segment.length() > maxlength) {
                maxlength = segment.length();
                result = segment;  //ищем отрезок максимальной длины
            }
        }
        if (result != null)
            answer = true;
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
    /**
     * проверка, есть ли ответ
     *
     * @return флаг
     */
    public boolean isAnswer() {
        return answer;
    }
}