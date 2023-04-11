package panels;

import app.Point;
import app.Rect;
import app.Task;

import java.util.ArrayList;

import controls.*;
import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.Event;
import io.github.humbleui.jwm.Window;
import io.github.humbleui.skija.Canvas;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;

import java.util.List;

import static app.Application.PANEL_PADDING;
import static app.Colors.FIELD_BACKGROUND_COLOR;
import static app.Colors.FIELD_TEXT_COLOR;

/**
 * Панель управления
 */
public class PanelControl extends GridPanel {
    /**
     * Текст задания
     */
    MultiLineLabel task;
    /**
     * Заголовки
     */
    public List<Label> labels;
    /**
     * Поля ввода
     */
    public List<Input> inputs;
    /**
     * Кнопки
     */
    public List<Button> buttons;
    /**
     * Кнопка "решить"
     */
    private final Button solve;
    /**
     * Панель управления
     *
     * @param window     окно
     * @param drawBG     флаг, нужно ли рисовать подложку
     * @param color      цвет подложки
     * @param padding    отступы
     * @param gridWidth  кол-во ячеек сетки по ширине
     * @param gridHeight кол-во ячеек сетки по высоте
     * @param gridX      координата в сетке x
     * @param gridY      координата в сетке y
     * @param colspan    кол-во колонок, занимаемых панелью
     * @param rowspan    кол-во строк, занимаемых панелью
     */
    public PanelControl(
            Window window, boolean drawBG, int color, int padding, int gridWidth, int gridHeight,
            int gridX, int gridY, int colspan, int rowspan
    ) {
        super(window, drawBG, color, padding, gridWidth, gridHeight, gridX, gridY, colspan, rowspan);

        // создаём списки
        inputs = new ArrayList<>();
        labels = new ArrayList<>();
        buttons = new ArrayList<>();
        // задание
        task = new MultiLineLabel(
                window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 0, 6, 2, Task.TASK_TEXT,
                false, true);
        // добавление вручную
        Label xALabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 2, 1, 1, "xA", true, true);
        labels.add(xALabel);
        Input xAField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 9, 1, 2, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(xAField);
        Label yALabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 9, 3, 2, 1, 1, "yA", true, true);
        labels.add(yALabel);
        Input yAField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 9, 4, 2, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(yAField);
        Label xBLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 3, 1, 1, "xB", true, true);
        labels.add(xBLabel);
        Input xBField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 9, 1, 3, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(xBField);
        Label yBLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 9, 3, 3, 1, 1, "yB", true, true);
        labels.add(yBLabel);
        Input yBField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 9, 4, 3, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(yBField);
        Label xPLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 4, 1, 1, "xP", true, true);
        labels.add(xPLabel);
        Input xPField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 9, 1, 4, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(xPField);
        Label yPLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 9, 3, 4, 1, 1, "yP", true, true);
        labels.add(yPLabel);
        Input yPField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 9, 4, 4, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(yPField);

        Button addRect = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 5, 6, 1, "Добавить прямоугольник",
                true, true);
        addRect.setOnClick(() -> {
            // если числа введены верно
            if (!xAField.hasValidDoubleValue()) {
                PanelLog.warning("xA координата введена неверно");
            } else if (!yAField.hasValidDoubleValue())
                PanelLog.warning("yA координата введена неверно");
            else if (!xBField.hasValidDoubleValue())
                PanelLog.warning("xB координата введена неверно");
            else if (!yBField.hasValidDoubleValue())
                PanelLog.warning("yB координата введена неверно");
            else if (!xPField.hasValidDoubleValue())
                PanelLog.warning("xP координата введена неверно");
            else if (!yPField.hasValidDoubleValue())
                PanelLog.warning("yP координата введена неверно");
            else {
                PanelRendering.task.addRect(
                        new Vector2d(xAField.doubleValue(), yAField.doubleValue()), new Vector2d(xBField.doubleValue(), yBField.doubleValue()), new Vector2d(xPField.doubleValue(), yPField.doubleValue())
                );
            }
        });
        buttons.add(addRect);
        // случайное добавление
        Label cntLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 6, 1, 1, "Кол-во", true, true);
        labels.add(cntLabel);

        Input cntField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 9, 1, 6, 2, 1, "5", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(cntField);

        Button addRects = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 9, 3, 6, 3, 1, "Добавить\nслучайные прямоугольники",
                true, true);
        addRects.setOnClick(() -> {
            // если числа введены верно
            if (!cntField.hasValidIntValue()) {
                PanelLog.warning("кол-во прямоугольников указано неверно");
            } else
                PanelRendering.task.addRandomRects(cntField.intValue());
        });
        buttons.add(addRects);
        // управление
        Button load = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 7, 3, 1, "Загрузить",
                true, true);
        load.setOnClick(() -> {
            PanelRendering.load();
            cancelTask();
        });
        buttons.add(load);

        Button save = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 9, 3, 7, 3, 1, "Сохранить",
                true, true);
        save.setOnClick(PanelRendering::save);
        buttons.add(save);

        Button clear = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 8, 3, 1, "Очистить",
                true, true);
        clear.setOnClick(() -> PanelRendering.task.clear());
        buttons.add(clear);

        solve = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 9, 3, 8, 3, 1, "Решить",
                true, true);
        solve.setOnClick(() -> {
            if (!PanelRendering.task.isSolved()) {
                PanelRendering.task.solve();
                String s = "Задача решена\n" +
                        "Пересечений: " + PanelRendering.task.getCrossed().size() / 2 + "\n" +
                        "Отдельных точек: " + PanelRendering.task.getSingle().size();
                PanelLog.success(s);
                solve.text = "Сбросить";
            } else {
                cancelTask();
            }
            window.requestFrame();
        });
        buttons.add(solve);
    }

    /**
     * Обработчик событий
     *
     * @param e событие
     */
    @Override
    public void accept(Event e) {
        // вызываем обработчик предка
        super.accept(e);
        // событие движения мыши
        if (e instanceof EventMouseMove ee) {
            for (Input input : inputs)
                input.accept(ee);

            for (Button button : buttons) {
                if (lastWindowCS != null)
                    button.checkOver(lastWindowCS.getRelativePos(new Vector2i(ee)));
            }
            // событие нажатия мыши
        } else if (e instanceof EventMouseButton) {
            if (!lastInside)
                return;

            Vector2i relPos = lastWindowCS.getRelativePos(lastMove);
            // пробуем кликнуть по всем кнопкам
            for (Button button : buttons) {
                button.click(relPos);
            }

            // перебираем поля ввода
            for (Input input : inputs) {
                // если клик внутри этого поля
                if (input.contains(relPos)) {
                    // переводим фокус на это поле ввода
                    input.setFocus();
                }
            }
            // перерисовываем окно
            window.requestFrame();
            // обработчик ввода текста
        } else if (e instanceof EventTextInput ee) {
            for (Input input : inputs) {
                if (input.isFocused()) {
                    input.accept(ee);
                }
            }
            // перерисовываем окно
            window.requestFrame();
            // обработчик ввода клавиш
        } else if (e instanceof EventKey ee) {
            for (Input input : inputs) {
                if (input.isFocused()) {
                    input.accept(ee);
                }
            }
            // перерисовываем окно
            window.requestFrame();
        }
    }

    /**
     * Метод под рисование в конкретной реализации
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    @Override
    public void paintImpl(Canvas canvas, CoordinateSystem2i windowCS) {
        task.paint(canvas, windowCS);
        // выводим кнопки
        for (Button button : buttons) {
            button.paint(canvas, windowCS);
        }
        // выводим поля ввода
        for (Input input : inputs) {
            input.paint(canvas, windowCS);
        }
        // выводим поля ввода
        for (Label label : labels) {
            label.paint(canvas, windowCS);
        }
    }
    /**
     * Сброс решения задачи
     */
    private void cancelTask() {
        PanelRendering.task.cancel();
        // Задаём новый текст кнопке решения
        solve.text = "Решить";
    }
}
