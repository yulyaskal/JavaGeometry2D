import app.Point;
import app.Rect;
import app.Segment;
import app.Task;
import misc.CoordinateSystem2d;
import misc.Vector2d;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Класс тестирования
 */
public class UnitTest {
    /**
     * Первый тест
     */
    @Test
    public void test1() {
        ArrayList<Rect> rects = new ArrayList<>();
        rects.add(new Rect(new Vector2d(1,1), new Vector2d(8,0), new Vector2d(3,3)));
        rects.add(new Rect(new Vector2d(-9,-9), new Vector2d(-8,-9), new Vector2d(-8,-5)));
        rects.add(new Rect(new Vector2d(9,-5), new Vector2d(6,-3), new Vector2d(7,-7)));
        // эти прямоугольники не пересекаются, ответа быть не должно
        Task task = new Task(new CoordinateSystem2d(10, 10, 20, 20), rects);
        task.solve();
        // проверка, есть ли ответ
        assert !task.isAnswer();
    }

    /**
     * Второй тест
     */
    @Test
    public void test2() {
        ArrayList<Rect> rects = new ArrayList<>();
        rects.add(new Rect(new Vector2d(0,-9), new Vector2d(0,9), new Vector2d(2,0)));
        rects.add(new Rect(new Vector2d(1,-8), new Vector2d(1,8), new Vector2d(-2,0)));
        // очевидно, что самое длинное пересечение проходит по оси Y, от точки (0, -8) до точки (0, 8)
        rects.add(new Rect(new Vector2d(1,1), new Vector2d(8,0), new Vector2d(3,3)));
        rects.add(new Rect(new Vector2d(-9,-9), new Vector2d(-8,-9), new Vector2d(-8,-5)));
        rects.add(new Rect(new Vector2d(9,-5), new Vector2d(6,-3), new Vector2d(7,-7)));
        Task task = new Task(new CoordinateSystem2d(10, 10, 20, 20), rects);
        task.solve();
        // проверка самого длинного пересечения
        assert (Objects.equals(task.getResult().pointA, new Vector2d(0, -8)) || Objects.equals(task.getResult().pointA, new Vector2d(0, 8)));
        assert (Objects.equals(task.getResult().pointB, new Vector2d(0, -8)) || Objects.equals(task.getResult().pointB, new Vector2d(0, 8)));
    }

    /**
     * Третий тест
     */
    @Test
    public void test3() {
        ArrayList<Rect> rects = new ArrayList<>();
        Rect r1 = new Rect(new Vector2d(-5,0), new Vector2d(0,5), new Vector2d(2.5,-2.5));
        Rect r2 = new Rect(new Vector2d(-1,8), new Vector2d(8,9), new Vector2d(-2,-7));
        rects.add(r1);
        rects.add(r2);
        // очевидно, что самое длинное пересечение у этих двух прямоугольников
        rects.add(new Rect(new Vector2d(1,1), new Vector2d(8,0), new Vector2d(3,3)));
        rects.add(new Rect(new Vector2d(-9,-9), new Vector2d(-8,-9), new Vector2d(-8,-5)));
        rects.add(new Rect(new Vector2d(9,-5), new Vector2d(6,-3), new Vector2d(7,-7)));
        Task task = new Task(new CoordinateSystem2d(10, 10, 20, 20), rects);
        task.solve();
        // проверка самого длинного пересечения
        assert (Objects.equals(task.getResult().rect1, r1));
        assert (Objects.equals(task.getResult().rect2, r2));
    }
}
