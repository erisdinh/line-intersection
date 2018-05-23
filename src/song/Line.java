///////////////////////////////////////////////////////////////////////////////
// Line.java
// ==========
// Line class using parametric form
// Line = p + aV (a point and a direction vector on the line)
//
// Dependency: Vector2
//
//  AUTHOR: Song Ho Ahn (song.ahn@gmail.com)
// CREATED: 2018-02-05
// UPDATED: 2018-02-11
///////////////////////////////////////////////////////////////////////////////

package song;


public class Line
{
    // member vars
    private Vector2 point;      // any point on the line
    private Vector2 direction;  // p1 - p0

    // ctors
    public Line()
    {
        point = new Vector2(0, 0);
        direction = new Vector2(0, 0);
    }
    public Line(Vector2 point, Vector2 direction)
    {
        this.point = new Vector2(point.x, point.y);
        this.direction = new Vector2(direction.x, direction.y);
    }
    public Line(float x1, float y1, float x2, float y2)
    {
        point = new Vector2(x1, y1);
        direction = new Vector2(x2-x1, y2-y1);
    }
    public Line(float slope, float intercept)
    {
        point = new Vector2(0, intercept);
        direction = new Vector2(1, slope);
    }



    // setters/getters
    public void set(Vector2 point, Vector2 direction)
    {
        this.point.set(point);
        this.direction.set(direction);
    }
    public void set(float x1, float y1, float x2, float y2)
    {
        this.point.set(x1, y1);
        this.direction.set(x2-x1, y2-y1);
    }
    public void set(float slope, float intercept)
    {
        this.point.set(0, intercept);
        this.direction.set(1, slope);
    }

    public Vector2 getPoint()
    {
        return point;
    }
    public void setPoint(Vector2 point)
    {
        this.point.set(point);
    }

    public Vector2 getDirection()
    {
        return direction;
    }
    public void setDirection(Vector2 direction)
    {
        this.direction.set(direction);
    }

    // override toString()
    public String toString()
    {
        return "Line\n" +
               "====\n" +
               "    Point: (" + point.x + ", " + point.y + ")\n" +
               "Direction: (" + direction.x + ", " + direction.y + ")\n";
    }


    ///////////////////////////////////////////////////////////////////////////
    // find the insersection point with the other line using standar form
    // If no intersection, return a point with NaN in it.
    //
    // Line1: ax + by = c (this)
    // Line2: dx + ef = g (other)
    // Intersect point:
    // x = (ce - bf) / (ae - bd)
    // y = (af - cd) / (ae - bd)
    // where a = Vy, b = -Vx, c = VyPx - VxPy
    ///////////////////////////////////////////////////////////////////////////
    public Vector2 intersect(Line line)
    {
        // find coefficients (a,b,c) of line1
        float a = direction.y;
        float b = -direction.x;
        float c = direction.y * point.x - direction.x * point.y;

        // find coefficients (d,e,f) of line2
        float d = line.getDirection().y;
        float e = -line.getDirection().x;
        float f = line.getDirection().y * line.getPoint().x - line.getDirection().x * line.getPoint().y;

        // find determinant: ae - bd
        float determinant = a * e - b * d;

        // find the intersect point (x,y) if det != 0
        // if det=0, return a Vector2 with Float.NaN
        Vector2 intersectPoint = new Vector2(Float.NaN, Float.NaN); // default point with NAN
        if(determinant != 0)
        {
            intersectPoint.x = (c * e - b * f) / determinant;
            intersectPoint.y = (a * f - c * d) / determinant;
        }

        // return the intersected point
        return intersectPoint;
    }



    ///////////////////////////////////////////////////////////////////////////
    // determine if it intersects with the other line
    ///////////////////////////////////////////////////////////////////////////
    public boolean isIntersected(Line line)
    {
        float a = direction.y;
        float b = -direction.x;
        float d = line.getDirection().y;
        float e = -line.getDirection().x;

        return ((a * e - b * d) != 0);
    }
}






