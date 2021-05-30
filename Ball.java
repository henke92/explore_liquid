/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */
public class Ball 
{
    private double x;
    private double y;
    private double radius;
    private double gravity;
    private double dy;
    private double dx;
    private boolean is_dragged;
    
    public Ball(double x, double y, double r)
    {
        this.x = x;
        this.y = y;
        this.radius = r;
        this.gravity = 2.5;
        this.dy = 0;
        this.dx = 0;
        this.is_dragged = false;
    }
    
    public boolean is_dragged()
    {
        return this.is_dragged;
    }
    
    public void set_dragged(boolean status)
    {
        this.is_dragged = status;
    }
    
    public void update()
    {
        this.dx *= 0.985;   //1.0; 0.975
        this.dy += gravity;
        
        this.x += dx;
        this.y += dy;
    }
    
    public double get_dx()
    {
        return dx;
    }
    
    public double get_dy()
    {
        return dy;
    }
    
    public double get_gravity()
    {
        return gravity;
    }
    
    public double get_radius()
    {
        return radius;
    }
    
    public double get_y()
    {
        return y;
    }
    
    public double get_x()
    {
        return x;
    }
    
    public void set_dx(double dx)
    {
        this.dx = dx;
    }
    
    public void set_dy(double dy)
    {
        this.dy = dy;
    }
    
    public void set_gravity(double g)
    {
        this.gravity = g;
    }
    
    public void set_radius(double r)
    {
        this.radius = r;
    }
    
    public void set_y(double y)
    {
        this.y = y;
    }
    
    public void set_x(double x)
    {
        this.x = x;
    }
    
}
