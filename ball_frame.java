
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Henke
 */
public class ball_frame extends javax.swing.JFrame {

    private Timer t1;
    private int delay = 5;
    private int FRAME_WIDTH;
    
    private double b_rad = 7.0; //7.0
    private int nr_of_balls;
    private int balls_size;
    private Ball[]b;
    
    private double impact_radius = b_rad*2.6;
    private double resolve_dist = 1.5;  //1.5 , 5.5
    private double dy_resolve = 0.97;
    private double pressure_val = 2.0;  //0.2   , 2.0
    
    private int button;
    
    private double last_x = -1;
    private double last_y = -1;
    
    private boolean add_balls = true;
    private boolean add_lines = false;
    private boolean move_lines = false;
    
    private double line_x0;
    private double line_y0;
    private double line_part = 80;
    
    private Line[]l;
    private int nr_of_lines;
    private int lines_size;
    
    /**
     * Creates new form ball_frame
     */
    public ball_frame() {
        initComponents();
        init();
        init_balls();
        init_lines();
        act_perf(delay);
    }
    
    public void act_perf(int delay)
    {
        ActionListener taskPerformer = new ActionListener()
                {
                    
                    public void actionPerformed(ActionEvent evt)
                    {
                        update();
                        draw();
                    }
                };
        
        
        
        t1 = new Timer(delay,taskPerformer);
        t1.start();
    }
    
    public void init()
    {
        delay = 5;
        FRAME_WIDTH = this.jPanel1.getWidth();
    }
    
    public void init_balls()
    {
        nr_of_balls = 0;
        balls_size = 200;
        b = new Ball[balls_size];
        dy_resolve = 0.97;
        pressure_val = 0.21;    //0.21
    }
    
    public void expand_lines()
    {
        lines_size += 5;
        Line [] t = new Line[lines_size];
        for(int ix = 0; ix < lines_size; ix++)
        {
            t[ix] = null;
        }
        for(int ix = 0; ix < nr_of_lines; ix++)
        {
            t[ix] = l[ix];
        }
        l = t;
    }
    
    public void add_line(double x0, double y0, double x1, double y1)
    {
        if(nr_of_lines >= lines_size)
        {
            expand_lines();
        }
        
        l[nr_of_lines++] = new Line(x0,y0,x1,y1);
    }
    
    public void init_lines()
    {
        nr_of_lines = 0;
        lines_size = 10;
        l = new Line[lines_size];
    }
    
    public void draw()
    {
        Graphics g = this.jPanel1.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_WIDTH);
        g.setColor(Color.red);
        for(int ix = 0; ix < nr_of_balls; ix++)
        {
            double shade = (double)(get_pressure(ix))/(double)(get_max_pressure());
            int s = (int)(shade*255);
            if(s < 0)
            {
                s = 0;
                
            }
            if(s > 255)
            {
                s = 255;
            }
            g.setColor(new Color(s,100,100));
            
            
            int x = (int)b[ix].get_x();
            int y = (int)b[ix].get_y();
            int r = (int)b[ix].get_radius();
            g.fillOval(x-r,y-r,2*r,2*r);
            
            //g.drawOval(x-(int)(impact_radius),y-(int)(impact_radius),(int)(2*impact_radius),(int)(2*impact_radius));
        }
        
            for(int ix2 = 0; ix2 < nr_of_lines; ix2++)
            {
                
                g.drawLine((int)(l[ix2].get_x0()),(int)(l[ix2].get_y0()),(int)(l[ix2].get_x1()),(int)(l[ix2].get_y1()));
            }
    }
    
    public boolean is_horizontal(int line_ix)
    {
        boolean status = false;
        if(l[line_ix].get_n0_y() < l[line_ix].get_n1_y())
        {
            double a0 = l[line_ix].get_n0_angle();
            double a1 = 3*Math.PI/2;
            while(a0 > 2*Math.PI)
            {
                a0 -= 2*Math.PI;
            }
            while(a0 < 0)
            {
                a0 += 2*Math.PI;
            }
            if(Math.abs(a0-a1) < Math.PI/16)
            {
                status = true;
            }
        }
        else
        {
            double a0 = l[line_ix].get_n1_angle();
            double a1 = 3*Math.PI/2;
            while(a0 > 2*Math.PI)
            {
                a0 -= 2*Math.PI;
            }
            while(a0 < 0)
            {
                a0 += 2*Math.PI;
            }
            if(Math.abs(a0-a1) < Math.PI/16)
            {
                status = true;
            }
        }
        return status;
    }
    
    public void lines()
    {
        Graphics g = jPanel1.getGraphics();
        g.setColor(Color.red);
        for(int ix = 0; ix < nr_of_lines; ix++)
        {
            
            double x0 = l[ix].get_x0();
            double y0 = l[ix].get_y0();
            double dx = l[ix].get_x1() - l[ix].get_x0();
            double dy = l[ix].get_y1() - l[ix].get_y0();
            for(double p = 0; p < line_part; p++)
            {
                double x_cord = x0 + p/line_part * dx;
                double y_cord = y0 + p/line_part * dy;
                
                for(int z = 0; z < nr_of_balls; z++)
                {
                    double dx2 = b[z].get_x() - x_cord;
                    double dy2 = b[z].get_y() - y_cord;
                    double d2 = Math.sqrt((dx2*dx2)+(dy2*dy2));
                    if(d2 < b[z].get_radius()*3)
                    {
                        if(l[ix].get_n1_y() <= l[ix].get_n0_y())
                        {
                            double ball_d = Math.sqrt((b[z].get_dx()*b[z].get_dx())+(b[z].get_dy()*b[z].get_dy()));
                            double ball_dx = b[z].get_dx();
                            double ball_dy = b[z].get_dy();
                            double ball_angle = 0;
                            if(ball_d != 0)
                            {
                                ball_angle = Math.acos(ball_dx/ball_d);
                                if(ball_dy < 0)
                                {
                                    ball_angle *= -1;
                                }
                                ball_angle += Math.PI;
                                
                            }
                            
                            
                            double normal_angle = l[ix].get_n1_angle();
                            double delta_angle = normal_angle - ball_angle;
                            double new_angle = normal_angle + delta_angle;
                            
                            if(is_horizontal(ix))
                            {
                                b[z].set_dy(b[z].get_dy()*0.05);
                                b[z].set_y(b[z].get_y()-b_rad*2.0);
                                double dx_val = ball_d * Math.cos(new_angle);
                                b[z].set_dx(dx_val);
                                b[z].set_dx(b[z].get_dx()*0.5); //0.2
                            }
                            else if(is_slope(l[ix].upwards_angle()))
                            {
                                b[z].set_dy(b[z].get_dy()*0.25); //0.2
                                //b[z].set_y(b[z].get_y()-b[z].get_radius()*0.5);
                                if(b[z].get_x() >= x_cord)
                                {
                                    b[z].set_dx(b[z].get_dx()+2.5);
                                    b[z].set_x(b[z].get_x()+b[z].get_radius()*1.5);
                                }
                                else
                                {
                                    b[z].set_dx(b[z].get_dx()-2.5);
                                    b[z].set_x(b[z].get_x()-b[z].get_radius()*1.5);
                                }
                            }
                            else
                            {
                                double dx_val = ball_d * Math.cos(new_angle);
                                double dy_val = ball_d * Math.sin(new_angle);
                                b[z].set_dx(dx_val);
                                b[z].set_dy(dy_val);
                                b[z].set_y(y_cord-b[z].get_radius()*3.5);   //3.5
                                b[z].set_dy(b[z].get_dy()*0.5); //0.5
                            }
                            
                            
                        }
                        else
                        {
                            double ball_d = Math.sqrt((b[z].get_dx()*b[z].get_dx())+(b[z].get_dy()*b[z].get_dy()));
                            double ball_dx = b[z].get_dx();
                            double ball_dy = b[z].get_dy();
                            double ball_angle = 0;
                            if(ball_d != 0)
                            {
                                ball_angle = Math.acos(ball_dx/ball_d);
                                if(ball_dy < 0)
                                {
                                    ball_angle *= -1;
                                }
                            }
                            ball_angle += Math.PI;
                            
                            double normal_angle = l[ix].get_n0_angle();
                            double delta_angle = normal_angle - ball_angle;
                            double new_angle = normal_angle + delta_angle;
                            
                            if(is_horizontal(ix))
                            {
                                b[z].set_dy(b[z].get_dy()*0.05);
                                b[z].set_y(b[z].get_y()-b_rad*2.0);
                                double dx_val = ball_d * Math.cos(new_angle);
                                b[z].set_dx(dx_val);
                                b[z].set_dx(b[z].get_dx()*0.2);
                            }
                            else if(is_slope(l[ix].upwards_angle()))
                            {
                                b[z].set_dy(b[z].get_dy()*0.25);
                                //b[z].set_y(b[z].get_y()-b[z].get_radius()*0.5);
                                if(b[z].get_x() >= x_cord)
                                {
                                    b[z].set_dx(b[z].get_dx()+2.5);
                                    b[z].set_x(b[z].get_x()+b[z].get_radius()*1.5);
                                }
                                else
                                {
                                    b[z].set_dx(b[z].get_dx()-2.5);
                                    b[z].set_x(b[z].get_x()-b[z].get_radius()*1.5);
                                }
                            }
                            else
                            {
                                double dx_val = ball_d * Math.cos(new_angle);
                                double dy_val = ball_d * Math.sin(new_angle);
                                b[z].set_dx(dx_val);
                                b[z].set_dy(dy_val);
                                b[z].set_y(y_cord-b[z].get_radius()*3.5);
                                b[z].set_dy(b[z].get_dy()*0.5); //0.5
                            }
                            
                        }
            
                    }
                }
                
                //g.fillOval((int)(x_cord-8),(int)(y_cord-8),17,17);
            }
            
            
        }
    }
    
    
    
    public boolean is_slope(double angle)
    {
        Graphics g = jPanel1.getGraphics();
        g.setColor(Color.red);
        boolean status = false;
        double top = 3*Math.PI/2;
        while(angle > 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        while(angle < 0)
        {
            angle += 2*Math.PI;
        }
        
        
        double delta_angle = Math.abs(angle-top);
        if(delta_angle < Math.PI/8)
        {
            status = true;
        }
        return status;
    }
    
    public void update()
    {
        lines();
        pressure_balls_resolve();
        dx_sum();
        wall_collision();
        balls_update();
        ball_ball_resolve();
        //get_average_surface();
    }
    
    public void wall_collision()
    {
        for(int ix = 0; ix < nr_of_balls; ix++)
        {
            if(b[ix].get_y() + b[ix].get_radius() > FRAME_WIDTH)
            {
                b[ix].set_dy(b[ix].get_dy()*(-0.9));
                b[ix].set_y(FRAME_WIDTH-b[ix].get_radius());
            }
            if(b[ix].get_x() - b[ix].get_radius() < 0)
            {
                //b[ix].set_dy(Math.abs(b[ix].get_dy())*-(1.02));
                b[ix].set_dx(b[ix].get_dx()*-1);
                b[ix].set_x(b[ix].get_radius());
            }
            if(b[ix].get_x() + b[ix].get_radius() > FRAME_WIDTH)
            {
                //b[ix].set_dy(Math.abs(b[ix].get_dy())*-(1.02));
                b[ix].set_dx(b[ix].get_dx()*-1);
                b[ix].set_x(FRAME_WIDTH-b[ix].get_radius());
            }
        }
    }
    public void balls_update()
    {
        for(int ix = 0; ix < nr_of_balls; ix++)
        {
            b[ix].update();
        }
    }

    public double largest_speed() // ~30
    {
        double max = 0;
        for(int ix = 0; ix < nr_of_balls; ix++)
        {
            double dx = b[ix].get_dx();
            double dy = b[ix].get_dy();
            double speed = Math.sqrt((dx*dx)+(dy*dy));
            if(speed > max)
            {
                max = speed;
            }
        }
        return max;
    }
    
    public double get_pressure(int ix)
    {
        double count = 0;
            for(int ix2 = 0; ix2 < nr_of_balls; ix2++)
            {
                if(ix != ix2)
                {
                    double dx = b[ix].get_x() - b[ix2].get_x();
                    double dy = b[ix].get_y() - b[ix2].get_y();
                    double d = Math.sqrt((dx*dx)+(dy*dy));
                    if(d < impact_radius)
                    {
                        count++;
                    }
                }
            }
            if(count == 0)
            {
                count = 1;
            }
            return count;
    }
    
    public double get_max_pressure()
    {
        double max = 0;
        for(int ix = 0; ix < nr_of_balls; ix++)
        {
            double count = 0;
            for(int ix2 = 0; ix2 < nr_of_balls; ix2++)
            {
                if(ix != ix2)
                {
                    double dx = b[ix].get_x() - b[ix2].get_x();
                    double dy = b[ix].get_y() - b[ix2].get_y();
                    double d = Math.sqrt((dx*dx)+(dy*dy));
                    if(d < impact_radius)
                    {
                        count++;
                    }
                }
            }
            if(count > max)
            {
                max = count;
            }
            if(max == 0)
            {
                max = 1;
            }
        }
        return max;
    }
    
    public void dx_sum()
    {
        Graphics g = jPanel1.getGraphics();
        g.setColor(Color.blue);
        for(int ix = 0; ix < nr_of_balls; ix++)
        {
            double dx_sum = 0;
            int count = 0;
            for(int ix2 = 0; ix2 < nr_of_balls; ix2++)
            {
                if(ix != ix2)
                {
                    double dx = b[ix].get_x() - b[ix2].get_x();
                    double dy = b[ix].get_y() - b[ix2].get_y();
                    double d = Math.sqrt((dx*dx)+(dy*dy));
                    
                    if(d < impact_radius*0.5)   //0.8
                    {
                        //g.drawOval((int)(b[ix].get_x())-(int)(impact_radius*3.5),(int)(b[ix].get_y())-(int)(impact_radius*3.5),(int)(impact_radius*7),(int)(impact_radius*5));
                        dx_sum += b[ix2].get_dx();
                        count++;
                    }
                        
                    
                }
            }
            if(count != 0)
            {
                dx_sum/=count;
            }
            else
            {
                dx_sum = b[ix].get_dx();
            }
            b[ix].set_dx(b[ix].get_dx()+dx_sum*0.0);
            
        }
    }
   
    
    public void eval_impact_pressure(int ix, int ix2, int x_cord, int y_cord)
    {
        double max = 35;
        double current = 0;
        
        double ix_speed = Math.sqrt((b[ix].get_dx()*b[ix].get_dx())+ (b[ix].get_dy()*b[ix].get_dy()));
        double ix2_speed = Math.sqrt((b[ix2].get_dx()*b[ix2].get_dx())+(b[ix2].get_dy()*b[ix2].get_dy()));
        
        if(ix_speed > ix2_speed)
        {
            current = ix_speed;
        }
        else if(ix2_speed >= ix_speed)
        {
            current = ix2_speed;
        }
        
        double init_ix_dy = b[ix].get_dy();
        double init_ix2_dy = b[ix2].get_dy();
        
        b[ix].set_dy(b[ix].get_dy() * dy_resolve);
        b[ix2].set_dy(b[ix2].get_dy()* dy_resolve);
        
        
        
        for(int z = 0; z < nr_of_balls; z++)
        {
            double dx = x_cord - b[z].get_x();
            double dy = y_cord - b[z].get_y();
            double d = Math.sqrt((dx*dx)+(dy*dy));
            if(d < impact_radius*2.5 && current > 18)   //current > 16  impact_radius * 2.5
            {
                double a = 0;
                if(d != 0)
                {
                    a = Math.acos(dx/d);
                    if(dy < 0)
                    {
                        a *= -1;
                    }
                    double ratio = current/max;
                    //double speed = current*1.2 * ratio;
                    
                    double dx0 = current * 0.97 * Math.cos(a);
                    double dy0 = current * 0.97 * Math.sin(a);
                    
                    
                    double max_surround = get_max_pressure();
                    double surround_z = get_pressure(z);
                    double surround_ratio = surround_z / max_surround;
                    
                    
                    double dist_ratio = d/(impact_radius*2.5);
                    
                    b[z].set_y(b[z].get_y()+b_rad* 5.0 * ratio);// * dist_ratio); //4.0
                    
                    //bounce upwards
                    if(init_ix_dy > init_ix2_dy)
                    {
                        b[z].set_dy(init_ix_dy*-0.5*(dist_ratio));      //-0.5
                        //b[z].set_y(b[z].get_y()-b_rad*3.0*dist_ratio);
                    }
                    else
                    {
                        b[z].set_dy(init_ix2_dy*-0.5*(dist_ratio));
                        //b[z].set_y(b[z].get_y()-b_rad*3.0 * dist_ratio);
                        
                    }
                    
                    
                    
                    
                    double dx1 = 0;
                    if(b[z].get_x() < x_cord)
                    {
                        dx1 = -ratio * b_rad * 3.5 * dist_ratio;    //3.5
                    }
                    else
                    {
                        dx1 = ratio * b_rad * 3.5 * dist_ratio;
                    }
                   
                    b[z].set_dx(dx0+dx1);
                    b[z].set_dy(b[z].get_dy()+dy0); //b[z].get_dy()
                    b[z].set_x(b[z].get_x() + dx0);
                    b[z].set_y(b[z].get_y() + dy0);
                }
            }
        }
        
    }
    
    public void pressure_balls_resolve()
    {
        for(int ix = 0; ix < nr_of_balls; ix++)
        {
            double pressure = get_pressure(ix);
            double max = get_max_pressure();
            double p_ratio = pressure/max; 
            
            for(int ix2 = 0; ix2 < nr_of_balls; ix2++)
            {
                if(ix != ix2)
                {
                    double dx = b[ix].get_x() - b[ix2].get_x();
                    double dy = b[ix].get_y() - b[ix2].get_y();
                    double d = Math.sqrt((dx*dx)+(dy*dy));
                    if(d != 0 && d < impact_radius)
                    {
                        double angle = 0;
                        angle = Math.acos(dx/d);
                        if(dy < 0)
                        {
                            angle *= -1;
                            
                        }
                        angle += Math.PI;
                        double dx0 = b_rad * pressure_val * p_ratio * Math.cos(angle);
                        double dy0 = b_rad * pressure_val * p_ratio * Math.sin(angle);
                        b[ix2].set_dx(b[ix2].get_dx()+dx0);
                        b[ix2].set_dy(b[ix2].get_dy()+dy0);
                        b[ix2].set_x(b[ix2].get_x()+dx0);
                        b[ix2].set_y(b[ix2].get_y()+dy0);
                    }
                }
            }
            
        }
    }
    
    public void ball_ball_resolve()
    {
        Graphics g = jPanel1.getGraphics();
        g.setColor(Color.blue);
        for(int ix = 0; ix < nr_of_balls; ix++)
        {
            for(int ix2 = 0; ix2 < nr_of_balls; ix2++)
            {
                if(ix != ix2)
                {
                    double dx = b[ix].get_x() - b[ix2].get_x();
                    double dy = b[ix].get_y() - b[ix2].get_y();
                    double d = Math.sqrt((dx*dx)+(dy*dy));
                    if(d < - resolve_dist + b[ix].get_radius() + b[ix2].get_radius())
                    {
                        double offset = 0.5 * (- resolve_dist + b[ix].get_radius()+b[ix2].get_radius() - d);
                        double a = 0;
                        if(d != 0)
                        {
                            a = Math.acos(dx/d);
                            if(dy < 0)
                            {
                                a *= -1;
                            }
                            
                            offset *= 0.96;
                            
                            
                            
                            b[ix].set_x(b[ix].get_x() + (offset) * Math.cos(a));
                            b[ix].set_y(b[ix].get_y() + (offset) * Math.sin(a));
                            b[ix2].set_x(b[ix2].get_x() - (offset) * Math.cos(a));
                            b[ix2].set_y(b[ix2].get_y() - (offset) * Math.sin(a));
                            
                            
                            
                            b[ix].set_dy(b[ix].get_dy()*0.92);  //0.92
                            b[ix2].set_dy(b[ix2].get_dy()*0.92);
                            
                            
                            
                            double intersect_x = b[ix].get_x() + dx/2;
                            double intersect_y = b[ix].get_y() + dy/2;
                           
                           
                            
                            
                            
                            
                            double dx_sum = b[ix].get_dx() + b[ix2].get_dx();
                            dx_sum *= 1.0035;    //1.005
                            b[ix].set_dx(dx_sum/2);
                            b[ix2].set_dx(dx_sum/2);
                            
                            /*
                            double temp = b[ix].get_dx();
                            b[ix].set_dx(b[ix2].get_dx());
                            b[ix2].set_dx(temp);
                            
                            b[ix].set_dx(b[ix].get_dx()*0.98);
                            b[ix2].set_dx(b[ix2].get_dx()*0.98);
                            */
                            
                            /*
                            if(b[ix].get_x() < b[ix2].get_x())
                            {
                                b[ix].set_dx(b[ix].get_dx()-0.15*Math.abs(b[ix].get_dy()));
                                b[ix2].set_dx(b[ix2].get_dx()+0.15*Math.abs(b[ix2].get_dy()));
                            }
                            else
                            {
                                b[ix].set_dx(b[ix].get_dx()+0.15*Math.abs(b[ix].get_dy()));
                                b[ix2].set_dx(b[ix2].get_dx()-0.15*Math.abs(b[ix2].get_dy()));
                            }
                            */
                            
                            
                            
                            eval_impact_pressure(ix,ix2,(int)intersect_x,(int)intersect_y);
                            
                            
                            //g.drawOval((int)b[ix].get_x()-(int)impact_radius,(int)b[ix].get_y()-(int)impact_radius,(int)(2*impact_radius),(int)(2*impact_radius));
                            
                            
                        }
                    }
                }
            }
        }
    }
    
    public double get_average_surface()
    {
        double lowest = 0;
        for(int ix = 0; ix < nr_of_balls; ix++)
        {
            if(b[ix].get_y() > lowest)
            {
                lowest = b[ix].get_y();
            }
        }
        double highest = FRAME_WIDTH;
        for(int ix = 0; ix < nr_of_balls; ix++)
        {
            if(b[ix].get_y() < highest)
            {
                highest = b[ix].get_y();
            }
        }
        Graphics g = jPanel1.getGraphics();
        g.setColor(Color.blue);
       
        double average = 0;
        average = lowest + (highest-lowest)/2;
        g.drawLine(0,(int)average,FRAME_WIDTH,(int)average);
        
        return average;
        
    }
    
    public void expand_balls()
    {
        balls_size += 50;
        Ball [] temp = new Ball[balls_size];
        for(int ix = 0; ix < balls_size; ix++)
        {
            temp[ix] = null;
        }
        for(int ix = 0; ix < nr_of_balls; ix++)
        {
            temp[ix] = b[ix];
        }
        b = temp;
    }
    
    public void add_ball(double x,double y, double rad)
    {
        if(nr_of_balls >= balls_size)
        {
            expand_balls();
        }
        b[nr_of_balls++] = new Ball(x,y,rad);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSlider2 = new javax.swing.JSlider();
        jButton3 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 400));
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel1MouseDragged(evt);
            }
        });
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel1MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        jButton1.setText("init balls");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("radius");

        jSlider1.setMaximum(120);
        jSlider1.setMinimum(50);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });

        jButton2.setText("random balls");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setText("resolve dist");

        jSlider2.setMaximum(70);
        jSlider2.setMinimum(-50);
        jSlider2.setValue(15);
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });

        jButton3.setText("init lines");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("add balls");
        jCheckBox1.setFocusable(false);
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("move lines");
        jCheckBox2.setFocusable(false);
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addComponent(jButton3)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2))
                .addContainerGap(212, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox2)
                        .addGap(41, 41, 41)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(81, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged

        Graphics g = jPanel1.getGraphics();
        g.setColor(Color.blue);

        
        if(move_lines)
        {
            for(int ix = 0; ix < nr_of_lines; ix++)
            {
                double dx = evt.getX() - l[ix].get_x0();
                double dy = evt.getY() - l[ix].get_y0();
                double d = Math.sqrt((dx*dx)+(dy*dy));
                if(d < 50)
                {
                    l[ix].set_x0(evt.getX());
                    l[ix].set_y0(evt.getY());
                }
                double dx2 = l[ix].get_x1() - evt.getX();// - l[ix].get_x1();
                double dy2 = l[ix].get_y1() - evt.getY();// - l[ix].get_y1();
                double d2 = Math.sqrt((dx2*dx2)+(dy2*dy2));
                if(d2 < 50)
                {
                    l[ix].set_x1(evt.getX());
                    l[ix].set_y1(evt.getY());
                }
            }
        }
        else if(add_balls && !add_lines)
        {
            add_ball(evt.getX(),evt.getY(),b_rad);
        }
        if(!add_balls)  //move balls with mouse
        {
            g.setColor(Color.blue);
            g.drawLine(evt.getX(),evt.getY(),(int)last_x,(int)last_y);
            
            double dx = evt.getX() - last_x;
            double dy = evt.getY() - last_y;
            double d = Math.sqrt((dx*dx)+(dy*dy));
            
            for(int ix = 0; ix < nr_of_balls; ix++)
            {
                double dx0 = b[ix].get_x() - evt.getX();
                double dy0 = b[ix].get_y() - evt.getY();
                double d0 = Math.sqrt((dx0*dx0)+(dy0*dy0));
                if(d0 < impact_radius*4.2)
                {
                    b[ix].set_dx(b[ix].get_dx()+dx*0.5);
                    b[ix].set_dy(b[ix].get_dy()+dy*0.5);
                    b[ix].set_x(b[ix].get_x()+b[ix].get_dx());
                    b[ix].set_y(b[ix].get_y()+b[ix].get_dy());
                }
            }
            
            
            last_x = evt.getX();
            last_y = evt.getY();
        }
        if(add_lines)
        {
            int x1 = evt.getX();
            int y1 = evt.getY();
            g.drawLine((int)line_x0,(int)line_y0,x1,y1);
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseDragged

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed


        button = evt.getButton();
        last_x = evt.getX();
        last_y = evt.getY();
        
        if(button == 3)
        {
            add_lines = true;
        }
        if(add_lines)
        {
            line_x0 = evt.getX();
            line_y0 = evt.getY();
        }
        
        
        
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseReleased

        if(add_lines)
        {
            add_line(evt.getX(),evt.getY(),line_x0,line_y0);
        }
        add_lines = false;
        
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        b_rad = jSlider1.getValue()/10.0;
        impact_radius = b_rad * 2.6;
        resolve_dist = (jSlider2.getValue())/10.0;
        init_balls();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged

        b_rad = jSlider1.getValue()/10.0;
        impact_radius = b_rad * 2.6;
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider1StateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        Random rand = new Random();
        for(int ix = 0; ix < 30; ix++)
        {
            double x_cord = rand.nextInt(FRAME_WIDTH);
            double y_cord = rand.nextInt(FRAME_WIDTH/7);
            double radius = b_rad;
            double dx = -2.0 + rand.nextInt(41)/10.0;
            double dy = 5.0;
            add_ball(x_cord,y_cord,radius);
            b[nr_of_balls-1].set_dx(dx);
            b[nr_of_balls-1].set_dy(dy);
            
        }
        
        /*
        int x_size = (int)(FRAME_WIDTH/(b_rad*1.5));
        for(int ix = 0; ix < x_size; ix++)
        {
                double x_cord = ix*b_rad*1.5;
                double y_cord = b_rad*5;
                double radius = b_rad;
                add_ball(x_cord,y_cord,radius);
                
        }
        */

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged


        resolve_dist = (jSlider2.getValue())/10.0;
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jSlider2StateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        init_lines();
         
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed


        if(jCheckBox2.isSelected())
        {
            move_lines = true;
        }
        else
        {
            move_lines = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed

        if(jCheckBox1.isSelected())
        {
            add_balls = true;
        }
        else if(!jCheckBox1.isSelected())
        {
            add_balls = false;
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ball_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ball_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ball_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ball_frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ball_frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    // End of variables declaration//GEN-END:variables
}
