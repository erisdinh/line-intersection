package quynh;

import song.Line;
import song.Vector2;

public class MainModel {

    private Line line1;
    private Line line2;

    private Vector2 intersection = new Vector2();

    public MainModel() {
        line1 = new Line(0, 0, 0, 0);
        line2 = new Line(0, 0, 0, 0);
    }

    // set the first line
    public void setLine1(float x1, float y1, float x2, float y2) {
        this.line1.set(x1, y1, x2, y2);
    }

    // set the second line
    public void setLine2(float x1, float y1, float x2, float y2) {
        this.line2.set(x1, y1, x2, y2);
    }

    // Get the intersection of 2 lines
    public Vector2 getIntersection() {
        intersection = line1.intersect(line2);
        return intersection;
    }
    
    // get the line in slope-intercept form
    public String getLineForm(float x1, float y1, float x2, float y2) {
        float slope = (y2 - y1) / (x2 - x1);
        float intercept = (y1 - slope * x1);
        return String.format("y = %.1fx+ %.1f", slope, intercept);
    }
}
