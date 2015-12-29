package be.krivi.plutus.android.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Krivi on 26/12/15.
 */

public class CollapseAnimation extends Animation{

    public enum CollapseAnimationAction{
        COLLAPSE,
        EXPAND
    }

    private View view;
    private CollapseAnimationAction action;

//    public CollapseAnimation( View view, CollapseAnimationAction action ){
//        super();
//        this.view = view;
//        this.action = action;
//
//        view.measure( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
//
//        final int targetHeigtMin = wrapperGaugeMin.getMeasuredHeight();
//    }
//
//    @Override
//    protected void applyTransformation( float interpolatedTime, Transformation t ){
//        view.getLayoutParams().height = interpolatedTime == 1
//                ? ViewGroup.LayoutParams.WRAP_CONTENT
//                : (int)( targetHeigtMin * interpolatedTime );
//        view.requestLayout();
//    }
//
//
//    @Override
//    protected void applyTransformation( float interpolatedTime, Transformation t ){
//        if( interpolatedTime == 1 ){
//            view.setVisibility( View.GONE );
//        }else{
//            view.getLayoutParams().height = initialHeightMin - (int)( initialHeightMin * interpolatedTime );
//            view.requestLayout();
//        }
//    }
//
//    @Override
//    public boolean willChangeBounds(){
//        return true;
//    }
//
//
//
//
//
//
//
//
//
//
//
//    public void expand(){
//
//        wrapperGaugeMin.measure( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
//        wrapperGaugeMin.measure( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
//
//        final int targetHeigtMin = wrapperGaugeMin.getMeasuredHeight();
//        final int targetHeigtMax = wrapperGaugeMin.getMeasuredHeight();
//
//        wrapperGaugeMin.getLayoutParams().height = 1;
//        wrapperGaugeMin.setVisibility( View.VISIBLE );
//
//        wrapperGaugeMax.getLayoutParams().height = 1;
//        wrapperGaugeMax.setVisibility( View.VISIBLE );
//
//        Animation a = new Animation(){
//            @Override
//            protected void applyTransformation( float interpolatedTime, Transformation t ){
//                wrapperGaugeMin.getLayoutParams().height = interpolatedTime == 1
//                        ? ViewGroup.LayoutParams.WRAP_CONTENT
//                        : (int)( targetHeigtMin * interpolatedTime );
//                wrapperGaugeMin.requestLayout();
//
//                wrapperGaugeMax.getLayoutParams().height = interpolatedTime == 1
//                        ? ViewGroup.LayoutParams.WRAP_CONTENT
//                        : (int)( targetHeigtMax * interpolatedTime );
//                wrapperGaugeMax.requestLayout();
//            }
//
//            @Override
//            public boolean willChangeBounds(){
//                return true;
//            }
//        };
//
//        // 1dp/ms
//        a.setDuration( (int)( targetHeigtMin / wrapperGaugeMin.getContext().getResources().getDisplayMetrics().density ) );
//        a.setDuration( (int)( targetHeigtMax / wrapperGaugeMax.getContext().getResources().getDisplayMetrics().density ) );
//        wrapperGaugeMin.startAnimation( a );
//        wrapperGaugeMax.startAnimation( a );
//    }
//
//    public void collapse(){
//
//        final int initialHeightMin = wrapperGaugeMin.getMeasuredHeight();
//        final int initialHeightMax = wrapperGaugeMax.getMeasuredHeight();
//
//        Animation a = new Animation(){
//            @Override
//            protected void applyTransformation( float interpolatedTime, Transformation t ){
//                if( interpolatedTime == 1 ){
//                    wrapperGaugeMin.setVisibility( View.GONE );
//                    wrapperGaugeMax.setVisibility( View.GONE );
//                }else{
//                    wrapperGaugeMin.getLayoutParams().height = initialHeightMin - (int)( initialHeightMin * interpolatedTime );
//                    wrapperGaugeMax.getLayoutParams().height = initialHeightMax - (int)( initialHeightMax * interpolatedTime );
//                    wrapperGaugeMin.requestLayout();
//                    wrapperGaugeMax.requestLayout();
//                }
//            }
//
//            @Override
//            public boolean willChangeBounds(){
//                return true;
//            }
//        };
//
//        // 1dp/ms
//        a.setDuration( (int)( initialHeightMin / wrapperGaugeMin.getContext().getResources().getDisplayMetrics().density ) );
//        a.setDuration( (int)( initialHeightMax / wrapperGaugeMax.getContext().getResources().getDisplayMetrics().density ) );
//        wrapperGaugeMin.startAnimation( a );
//        wrapperGaugeMax.startAnimation( a );
//    }

}