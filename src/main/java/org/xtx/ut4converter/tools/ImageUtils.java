/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xtx.ut4converter.tools;

import org.xtx.ut4converter.t3d.T3DLight.HSVColor;
import org.xtx.ut4converter.t3d.T3DLight.RGBColor;

/**
 * Utility class related to image (such as extracting data from textures, ...)
 * or colors
 * @author XtremeXp
 */
public class ImageUtils {
    

    /**
     * Convert color in HSV (Hue, Saturation, Value) format to RGB (Red, Green, Blue) format
     * @param hue Hue
     * @param saturation Saturation
     * @param brightness Brightness
     * @return
     */
    public static RGBColor HSVToLinearRGB(float hue, float saturation, float brightness)
    {
        return HSVToLinearRGB(new HSVColor(hue, saturation, brightness));
    }
    
    /**
     * Convert color in HSV (Hue, Saturation, Value) format to RGB (Red, Green, Blue) format
     * @param hsv Color in HSV format
     * @return Color in RGB format
     */
    public static RGBColor HSVToLinearRGB(HSVColor hsv)
    {
        // Hue normally in 0-360 range

        // In this color, R = H, G = S, B = V
        // UE1/2 'range' to UE3/4 range conversion
        // In Unreal Engine 1/2 to UE3/UE4:
        // 0 - Hue -> 255 ===>  0 - Hue -> 360
        // 0 - Sat -> 255 ===>  0 - Sat -> 1.0
        // 0 - Val -> 255 ===>  0 - Val -> 1.0
        
        float Hue = hsv.H * 360f/255f;
        float Saturation = hsv.S/255f;
        float Value = hsv.V/255f;


        float HDiv60 = Hue / 60.0f;
        float HDiv60_Floor = (float) Math.floor(HDiv60);
        float HDiv60_Fraction = HDiv60 - HDiv60_Floor;


        float RGBValues[];
        RGBValues = new float[]{
                Value,
                Value * (1.0f - Saturation),
                Value * (1.0f - (HDiv60_Fraction * Saturation)),
                Value * (1.0f - ((1.0f - HDiv60_Fraction) * Saturation)),
        };

        int RGBMatrix[][];


        RGBMatrix = new int[][]{
                {0, 3, 1},
                {2, 0, 1},
                {1, 0, 3},
                {1, 2, 0},
                {3, 1, 0},
                {0, 1, 2},
        };

        int SwizzleIndex = ((int)HDiv60_Floor) % 6;

        RGBColor rgb = new RGBColor();
        rgb.R = RGBValues[RGBMatrix[SwizzleIndex][0]] * 255f;
        rgb.G = RGBValues[RGBMatrix[SwizzleIndex][1]] * 255f;
        rgb.B = RGBValues[RGBMatrix[SwizzleIndex][2]] * 255f;
        rgb.A = 255f;

        return rgb;
    }
}