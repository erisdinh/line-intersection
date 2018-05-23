package quynh;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import song.*;

public class MainFormController implements Initializable {

    // constants
    private static final int PADDING = 10;      // pixels
    private static final int UNIT_COUNT = 10;   // # of units only on positive side

    // member vars
    private int width;                  // width of drawing area
    private int height;                 // height of drawing area
    private int centerX;                // center X in screen space
    private int centerY;                // center y in screen space
    private double mouseX;              // screen coordinate x
    private double mouseY;              // screen coordinate y
    private double coordRatio;          // map screen coord to logical coord, s/l
    private double coordX;              // logical coordinate x
    private double coordY;              // logical coordinate y
    private Line[] hLines;              // horizontal grid lines
    private Line[] vLines;              // vertical grid lines

    // JavaFX controls
    private Rectangle rectClip;         // clipping rectangle
    @FXML
    private Pane paneView;
    @FXML
    private Pane paneControl;
    @FXML
    private Label labelCoord;
    @FXML
    private Line line1a;
    @FXML
    private Line line1b;
    @FXML
    private Line line1c;
    @FXML
    private Line line2a;
    @FXML
    private Line line2b;
    @FXML
    private Line line2c;
    @FXML
    private Circle point1a;
    @FXML
    private Circle point1b;
    @FXML
    private Circle point2a;
    @FXML
    private Circle point2b;
    @FXML
    private Circle pointIntersect;
    @FXML
    private Slider sliderL1X1;
    @FXML
    private Slider sliderL1Y1;
    @FXML
    private Label labelL1X1;
    @FXML
    private Slider sliderL1X2;
    @FXML
    private Slider sliderL1Y2;
    @FXML
    private Label labelL1Y1;
    @FXML
    private Label labelL1X2;
    @FXML
    private Label labelL1Y2;
    @FXML
    private Slider sliderL2X1;
    @FXML
    private Slider sliderL2Y1;
    @FXML
    private Slider sliderL2X2;
    @FXML
    private Slider sliderL2Y2;
    @FXML
    private Label labelL2X1;
    @FXML
    private Label labelL2Y1;
    @FXML
    private Label labelL2X2;
    @FXML
    private Label labelL2Y2;

    MainModel model = new MainModel();
    @FXML
    private Label labelIntersection;
    @FXML
    private Button buttonReset;

    private ChangeListener listener;
    @FXML
    private Label labelL1;
    @FXML
    private Label labelL2;

    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // init line arrays
        initGrid();

        // set clip region for drawing area
        rectClip = new Rectangle(500, 500);
        paneView.setClip(rectClip);

        // update width and height of drawing area
        ChangeListener resizeListener = (ov, oldV, newV) -> handleViewResized();
        paneView.widthProperty().addListener(resizeListener);
        paneView.heightProperty().addListener(resizeListener);

        // set the default value for sliders
        setDefaultValue();

        // property binding for the sliders
        // textProperty just contains string value
        // valueProperty contains number from -10 to 10
        // -> convert valueProperty to String
        // Bindings.format() = String.format()
        labelL1X1.textProperty().bind(sliderL1X1.valueProperty().asString("%.1f"));
        labelL1Y1.textProperty().bind(sliderL1Y1.valueProperty().asString("%.1f"));
        labelL1X2.textProperty().bind(sliderL1X2.valueProperty().asString("%.1f"));
        labelL1Y2.textProperty().bind(sliderL1Y2.valueProperty().asString("%.1f"));
        labelL2X1.textProperty().bind(sliderL2X1.valueProperty().asString("%.1f"));
        labelL2Y1.textProperty().bind(sliderL2Y1.valueProperty().asString("%.1f"));
        labelL2X2.textProperty().bind(sliderL2X2.valueProperty().asString("%.1f"));
        labelL2Y2.textProperty().bind(sliderL2Y2.valueProperty().asString("%.1f"));

        // register ChangeListener for the sliders
        listener = ((ov, oldValue, newValue) -> updateLines());

        sliderL1X1.valueProperty().addListener(listener);
        sliderL1Y1.valueProperty().addListener(listener);
        sliderL1X2.valueProperty().addListener(listener);
        sliderL1Y2.valueProperty().addListener(listener);
        sliderL2X1.valueProperty().addListener(listener);
        sliderL2Y1.valueProperty().addListener(listener);
        sliderL2X2.valueProperty().addListener(listener);
        sliderL2Y2.valueProperty().addListener(listener);
    }

    ///////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleMouseMoved(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
        coordX = (mouseX - centerX) / coordRatio;
        coordY = (height - mouseY - centerY) / coordRatio;
        labelCoord.setText(String.format("(%.1f, %.1f)", coordX, coordY));
    }

    ///////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleMouseDragged(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
        coordX = (mouseX - centerX) / coordRatio;
        coordY = (height - mouseY - centerY) / coordRatio;
        labelCoord.setText(String.format("(%.1f, %.1f)", coordX, coordY));
    }

    ///////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleMousePressed(MouseEvent event) {
    }

    ///////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleMouseReleased(MouseEvent event) {
        Circle c = (Circle) event.getSource();

        c.setStrokeWidth(0);
    }

    ///////////////////////////////////////////////////////////////////////////
    @FXML
    private void handleMouseExited(MouseEvent event) {
        labelCoord.setText("");
    }

    ///////////////////////////////////////////////////////////////////////////
    private void initGrid() {
        int lineCount = UNIT_COUNT * 2 + 1; // both side plus 1 at enter
        hLines = new Line[lineCount];
        vLines = new Line[lineCount];

        // create line objects
        for (int i = 0; i < lineCount; ++i) {
            hLines[i] = new Line();
            hLines[i].setStrokeWidth(0.2);
            hLines[i].setStroke(Color.GRAY);
            paneView.getChildren().add(hLines[i]);
            hLines[i].toBack();

            vLines[i] = new Line();
            vLines[i].setStrokeWidth(0.2);
            vLines[i].setStroke(Color.GRAY);
            paneView.getChildren().add(vLines[i]);
            vLines[i].toBack();
        }

        // for center line
        hLines[lineCount / 2].setStroke(Color.BLACK);
        hLines[lineCount / 2].setStrokeWidth(0.4);
        vLines[lineCount / 2].setStroke(Color.BLACK);
        vLines[lineCount / 2].setStrokeWidth(0.4);

        // layout grid lines
        updateGrid();
    }

    ///////////////////////////////////////////////////////////////////////////
    private void handleViewResized() {
        width = (int) paneView.getWidth();
        height = (int) paneView.getHeight();

        // compute the ratio of scrren to virtual = s / v
        double dim = Math.min(width, height) - (PADDING * 2);
        coordRatio = dim / (UNIT_COUNT * 2.0);

        centerX = (int) (width * 0.5 + 0.5);
        centerY = (int) (height * 0.5 + 0.5);
        //System.out.printf("center: (%d, %d)\n", centerX, centerY);

        // update clipping region
        rectClip.setWidth(width);
        rectClip.setHeight(height);

        updateGrid();
        updateLines();
    }

    ///////////////////////////////////////////////////////////////////////////
    private void updateGrid() {
        int dim;    // square dimension
        int xGap, yGap;

        if (width > height) {
            dim = height - (PADDING * 2);
            xGap = (int) ((width - dim) * 0.5 + 0.5);
            yGap = PADDING;
        } else {
            dim = width - (PADDING * 2);
            xGap = PADDING;
            yGap = (int) ((height - dim) * 0.5 + 0.5);
        }
        double step = dim / (UNIT_COUNT * 2.0);

        for (int i = 0; i < hLines.length; ++i) {
            hLines[i].setStartX(xGap);
            hLines[i].setStartY(yGap + (int) (step * i + 0.5));
            hLines[i].setEndX(width - xGap);
            hLines[i].setEndY(yGap + (int) (step * i + 0.5));

            vLines[i].setStartX(xGap + (int) (step * i + 0.5));
            vLines[i].setStartY(yGap);
            vLines[i].setEndX(xGap + (int) (step * i + 0.5));
            vLines[i].setEndY(height - yGap);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    private void updateLines() {

        // get position in logical space
        float l1X1 = (float) sliderL1X1.getValue();
        float l1Y1 = (float) sliderL1Y1.getValue();
        float l1X2 = (float) sliderL1X2.getValue();
        float l1Y2 = (float) sliderL1Y2.getValue();
        float l2X1 = (float) sliderL2X1.getValue();
        float l2Y1 = (float) sliderL2Y1.getValue();
        float l2X2 = (float) sliderL2X2.getValue();
        float l2Y2 = (float) sliderL2Y2.getValue();

        // for extended points
        // Line1
        // direction vector 
        Vector2 v1 = new Vector2();
        Vector2 v2 = new Vector2();
        // the extended point
        Vector2 p1;
        Vector2 p2;

        // set position of it in screen space 
        point1a.setCenterX(l1X1 * coordRatio + centerX);
        point1a.setCenterY(-l1Y1 * coordRatio + centerY);
        point1b.setCenterX(l1X2 * coordRatio + centerX);
        point1b.setCenterY(-l1Y2 * coordRatio + centerY);

        point2a.setCenterX(l2X1 * coordRatio + centerX);
        point2a.setCenterY(-l2Y1 * coordRatio + centerY);
        point2b.setCenterX(l2X2 * coordRatio + centerX);
        point2b.setCenterY(-l2Y2 * coordRatio + centerY);
        //System.out.println(centerX + coordRatio);

        // draw line segment between 2 points in screen space
        line1a.setStartX(point1a.getCenterX());
        line1a.setStartY(point1a.getCenterY());
        line1a.setEndX(point1b.getCenterX());
        line1a.setEndY(point1b.getCenterY());

        line2a.setStartX(point2a.getCenterX());
        line2a.setStartY(point2a.getCenterY());
        line2a.setEndX(point2b.getCenterX());
        line2a.setEndY(point2b.getCenterY());

        // extension lines
        v1.set(l1X2 - l1X1, l1Y2 - l1Y1); // Line 1: p2 - p1
        v1.normalize();
        p1 = v1.clone().scale(50).add(new Vector2(l1X2, l1Y2));
        line1b.setStartX(point1b.getCenterX());
        line1b.setStartY(point1b.getCenterY());
        line1b.setEndX(p1.x * coordRatio + centerX);
        line1b.setEndY(-p1.y * coordRatio + centerY);

        p1 = v1.clone().scale(-50).add(new Vector2(l1X1, l1Y1));
        line1c.setStartX(point1a.getCenterX());
        line1c.setStartY(point1a.getCenterY());
        line1c.setEndX(p1.x * coordRatio + centerX);
        line1c.setEndY(-p1.y * coordRatio + centerY);

        v2.set(l2X2 - l2X1, l2Y2 - l2Y1);
        v2.normalize();
        p2 = v2.clone().scale(50).add(new Vector2(l2X2, l2Y2));
        line2b.setStartX(point2b.getCenterX());
        line2b.setStartY(point2b.getCenterY());
        line2b.setEndX(p2.x * coordRatio + centerX);
        line2b.setEndY(-p2.y * coordRatio + centerY);

        p2 = v2.clone().scale(-50).add(new Vector2(l2X1, l2Y1));
        line2c.setStartX(point2a.getCenterX());
        line2c.setStartY(point2a.getCenterY());
        line2c.setEndX(p2.x * coordRatio + centerX);
        line2c.setEndY(-p2.y * coordRatio + centerY);

        // find the intersection
        model.setLine1(l1X1, l1Y1, l1X2, l1Y2);
        model.setLine2(l2X1, l2Y1, l2X2, l2Y2);
        Vector2 intersection = model.getIntersection();

        labelIntersection.setText(String.format("(%.3f, %.3f)", intersection.x, intersection.y));
        pointIntersect.setCenterX(intersection.x * coordRatio + centerX);
        pointIntersect.setCenterY(-intersection.y * coordRatio + centerY);

        // display slope-intercept form of each line
        String line1 = model.getLineForm(l1X1, l1Y1, l1X2, l1Y2);
        labelL1.setText(line1);

        String line2 = model.getLineForm(l2X1, l2Y1, l2X2, l2Y2);
        labelL2.setText(line2);
    }

    @FXML
    private void handleButtonReset(ActionEvent event) {
        setDefaultValue();
    }

    // set the default value for sliders
    private void setDefaultValue() {
        sliderL1X1.setValue(-5.0);
        sliderL1Y1.setValue(5.0);
        sliderL1X2.setValue(5.0);
        sliderL1Y2.setValue(-5.0);
        sliderL2X1.setValue(-5.0);
        sliderL2Y1.setValue(-5.0);
        sliderL2X2.setValue(5.0);
        sliderL2Y2.setValue(5.0);
    }

    ////////////////////////////////////////////////////////////////////////////
    
    // Mouse drag the point
    @FXML
    private void handleMouseDraggedPoint(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
        coordX = (mouseX - centerX) / coordRatio;
        coordY = (height - mouseY - centerY) / coordRatio;

        // get the id of the point
        Circle c = (Circle) event.getSource();
        String id = c.getId();

        // match the id to the point
        // enable the border of the points
        // point will move following the coordinator of the mouse
        if (id.equals("point1a")) {
            point1a.setStrokeWidth(3);
            point1a.setStroke(Color.BLACK);
            sliderL1X1.setValue(coordX);
            sliderL1Y1.setValue(coordY);
        }
        if (id.equals("point1b")) {
            point1b.setStrokeWidth(3);
            point1b.setStroke(Color.BLACK);
            sliderL1X2.setValue(coordX);
            sliderL1Y2.setValue(coordY);
        }
        if (id.equals("point2a")) {
            point2a.setStrokeWidth(3);
            point2a.setStroke(Color.BLACK);
            sliderL2X1.setValue(coordX);
            sliderL2Y1.setValue(coordY);
        }
        if (id.equals("point2b")) {
            point2b.setStrokeWidth(3);
            point2b.setStroke(Color.BLACK);
            sliderL2X2.setValue(coordX);
            sliderL2Y2.setValue(coordY);
        }
    }

    // Mouse move in the points
    // show the border of the points
    @FXML
    private void handleMouseMovedPoint(MouseEvent event) {
        Circle c = (Circle) event.getSource();
        String id = c.getId();

        if (id.equals("point1a")) {
            point1a.setStrokeWidth(3);
            point1a.setStroke(Color.BLACK);
        }
        if (id.equals("point1b")) {
            point1b.setStrokeWidth(3);
            point1b.setStroke(Color.BLACK);
        }
        if (id.equals("point2a")) {
            point2a.setStrokeWidth(3);
            point2a.setStroke(Color.BLACK);
        }
        if (id.equals("point2b")) {
            point2b.setStrokeWidth(3);
            point2b.setStroke(Color.BLACK);
        }
    }

    
    // if the mouse out the point
    // disable the border of the points 
    @FXML
    private void handleMouseExitedPoint(MouseEvent event) {
        Circle c = (Circle) event.getSource();
        c.setStrokeWidth(0);
    }   
}