/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */
public class Line 
{
    private double x0;
    private double y0;
    private double x1;
    private double y1;
    
    public Line(double x0, double y0, double x1, double y1)
    {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        
        if(this.y1 <= this.y0)
        {
            double temp = this.y1;
            this.y1 = this.y0;
            this.y0 = temp;
            
            temp = this.x1;
            this.x1 = this.x0;
            this.x0 = temp;
            
        }
    }
    
    public double get_n0_y()
    {
        return mid_y() + 50 * Math.sin(get_n0_angle());
    }
    
    public double get_n0_x()
    {
        return mid_x() + 50 * Math.cos(get_n0_angle());
    }
    
    public double get_n1_y()
    {
        return mid_y() + 50 * Math.sin(get_n1_angle());
    }
    
    public double get_n1_x()
    {
        return mid_x() + 50 * Math.cos(get_n1_angle());
    }
    
    public double get_n1_angle()
    {
        return get_angle() - Math.PI/2;
    }
    
    public double get_n0_angle()
    {
        return get_angle() + Math.PI/2;
    }
    
    public double upwards_angle()
    {
        double angle = 0;
        if(this.y1 < this.y0)
        {
            double dx = this.x1 - this.x0;
            double dy = this.y1 - this.y0;
            double d = Math.sqrt((dx*dx)+(dy*dy));
            if(d != 0)
            {
                angle = Math.acos(dx/d);
                if(dy < 0)
                {
                    angle *= -1;
                }
            }
        }
        else
        {
            double dx = this.x0 - this.x1;
            double dy = this.y0 - this.y1;
            double d = Math.sqrt((dx*dx)+(dy*dy));
            if(d != 0)
            {
                angle = Math.acos(dx/d);
                if(dy < 0)
                {
                    angle *= -1;
                }
            }
        }
        return angle;
    }
    
    public double get_angle()
    {
        double angle = 0;
        double dx = this.x1 - this.x0;
        double dy = this.y1 - this.y0;
        double d = Math.sqrt((dx*dx)+(dy*dy));
        if(d != 0)
        {
            angle = Math.acos(dx/d);
            if(dy < 0)
            {
                angle *= -1;
            }
        }
        return angle;
    }
    
    public double mid_y()
    {
        return this.y0 + (this.y1-this.y0)/2;
    }
    
    public double mid_x()
    {
        return this.x0 + (this.x1-this.x0)/2;
    }
    
    public double get_y1()
    {
        return y1;
    }
    
    public double get_x1()
    {
        return x1;
    }
    
    public double get_y0()
    {
        return y0;
    }
    
    public double get_x0()
    {
        return x0;
    }
    
    public void set_y1(double y)
    {
        this.y1 = y;
    }
    
    public void set_x1(double x)
    {
        this.x1 = x;
    }
    
    public void set_y0(double y)
    {
        this.y0 = y;
    }
    
    public void set_x0(double x)
    {
        this.x0 = x;
    }
    
}
