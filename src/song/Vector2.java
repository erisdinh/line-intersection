///////////////////////////////////////////////////////////////////////////////
// Vector2.java
// ============
// a general purpose Vector2 class
//
//  AUTHOR: Song Ho Ahn (song.ahn@gmail.com)
// CREATED: 2018-02-02
// UPDATED: 2018-02-02
///////////////////////////////////////////////////////////////////////////////

package song;

public class Vector2
{
    // instance vars
    public float x;
    public float y;

    // ctors
    public Vector2()
    {
        set(0, 0);
    }

    public Vector2(float x, float y)
    {
        set(x, y);
    }

    // setters
    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2 v)
    {
        if(v != null)
            set(v.x, v.y);
    }

    // override toString()
    @Override
    public String toString()
    {
        return "Vector2(" + x + ", " + y + ")";
    }

    // arithmetics
    public Vector2 add(Vector2 v)
    {
        if(v != null)
        {
            x += v.x;  // x = x + v.x
            y += v.y;
        }
        return this;
    }

    public Vector2 subtract(Vector2 v)
    {
        if(v != null)
        {
            x -= v.x;
            y -= v.y;
        }
        return this;
    }

    public Vector2 scale(float s)
    {
        x *= s;
        y *= s;
        return this;
    }

    // make a copy
    public Vector2 clone()
    {
        return new Vector2(x, y);
    }

    // dot product
    public float dot(Vector2 v)
    {
        if(v != null)
            return (x * v.x) + (y * v.y);
        else
            return 0.0f;
    }

    // return the length of the vector
    public float getLength()
    {
        return (float)Math.sqrt(x * x + y * y);
    }

    public Vector2 normalize()
    {
        float length = getLength();
        if(length != 0.0)
        {
            x /= length;
            y /= length;
        }
        return this;
    }

    public boolean equals(Object rhs)
    {
        if(rhs instanceof Vector2)
        {
           Vector2 v = (Vector2)rhs;
           return (x == v.x && y == v.y);
        }
        else
        {
            return false;
        }
    }



    ///////////////////////////////////////////////////////////////////////////
    // static methods
    public static Vector2 add(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x + b.x, a.y + b.y);
    }
    public static Vector2 subtract(Vector2 a, Vector2 b)
    {
        return new Vector2(a.x - b.x, a.y - b.y);
    }
    public static Vector2 scale(Vector2 a, float s)
    {
        return new Vector2(a.x * s, a.y * s);
    }
    public static Vector2 normalize(Vector2 a)
    {
        return a.clone().normalize();
    }
}
