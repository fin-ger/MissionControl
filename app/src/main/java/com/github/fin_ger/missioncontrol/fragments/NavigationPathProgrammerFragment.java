package com.github.fin_ger.missioncontrol.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.github.fin_ger.missioncontrol.ControlActivity;
import com.github.fin_ger.missioncontrol.R;
import com.github.fin_ger.missioncontrol.events.OnReset;

public
class NavigationPathProgrammerFragment extends Fragment
{
    public    Canvas          Canvas;
    protected Bitmap          image;
    protected Paint           paint;
    protected ImageView       imageView;
    protected ControlActivity parent;
    protected int             sizeX;
    protected int             sizeY;
    protected OnReset         onResetListener;

    public
    NavigationPathProgrammerFragment ()
    {
        paint = new Paint ();
        paint.setColor (Color.BLACK);
        paint.setAntiAlias (true);
        paint.setStrokeWidth (5);
    }

    public
    void reset ()
    {
        image = Bitmap.createBitmap (sizeX, sizeY, Bitmap.Config.ARGB_8888);
        Canvas = new Canvas (image);
        imageView.setImageBitmap (image);
        drawPoint = true;

        if (onResetListener != null)
            onResetListener.onReset ();
    }

    protected float   lastX     = Float.NaN;
    protected float   lastY     = Float.NaN;
    protected boolean drawPoint = true;

    public
    void onTouchUp (float posX, float posY)
    {
        if (drawPoint)
        {
            drawPoint = false;
            Canvas.drawPoint (posX, posY, paint);
        } else Canvas.drawLine (lastX, lastY, posX, posY, paint);

        parent.Points.add (new Point ((int) (posX / 3), (int) (posY / 3)));

        lastX = posX;
        lastY = posY;

        imageView.setImageBitmap (image);
    }

    @Override
    public
    void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
    }

    @Override
    public
    View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (onResetListener != null)
            onResetListener.onReset ();

        parent = (ControlActivity) (this.getActivity ());

        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_navigation_path_programmer, container, false);
        imageView = ((ImageView) view.findViewById (R.id.imageView));
        imageView.getViewTreeObserver ().addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener ()
        {
            @Override
            public
            void onGlobalLayout ()
            {
                sizeX = imageView.getMeasuredWidth ();
                sizeY = imageView.getMeasuredHeight ();
                image = Bitmap.createBitmap (sizeX, sizeY, Bitmap.Config.ARGB_8888);
                Canvas = new Canvas (image);

                imageView.setOnTouchListener (new View.OnTouchListener ()
                {
                    @Override
                    public
                    boolean onTouch (View v, MotionEvent event)
                    {
                        if (event.getAction () == MotionEvent.ACTION_UP)
                        {
                            float viewX = event.getX () - v.getLeft ();
                            float viewY = event.getY () - v.getLeft ();
                            onTouchUp (viewX, viewY);
                        }
                        return true;
                    }
                });
            }
        });
        return view;
    }

    @Override
    public
    void onAttach (Activity activity)
    {
        super.onAttach (activity);
    }

    @Override
    public
    void onDetach ()
    {
        super.onDetach ();
    }

    public
    void setOnResetListener (OnReset listener)
    {
        onResetListener = listener;
    }
}
