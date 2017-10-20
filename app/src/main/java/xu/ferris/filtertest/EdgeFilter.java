/* 
 * HaoRan ImageFilter Classes v0.1
 * Copyright (C) 2012 Zhenjun Dai
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation.
 */

package xu.ferris.filtertest;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * 主要是这个效果
 */
public class EdgeFilter implements IImageFilter {

    //@Override
    public Image process(Image imageIn) {
        // Image size
        int width = imageIn.getWidth();
        int height = imageIn.getHeight();
        boolean[][] mask = null;
        Paint grayMatrix[] = new Paint[256];

        // Init gray matrix
        for (int i = 0; i <= 255; i++) {
            Paint p = new Paint();
            p.setColor(Color.rgb(i, i, i));
            grayMatrix[i] = p;
        }

        int [][] luminance = new int[width][height];
        for (int y = 0; y < height ; y++) {
            for (int x = 0; x < width ; x++) {
                if(mask != null && !mask[x][y]){
                        continue;
                }
                luminance[x][y] = (int) luminance(imageIn.getRComponent(x, y), imageIn.getGComponent(x, y), imageIn.getBComponent(x, y));
            }
        }


        int grayX, grayY;
        int magnitude;
        for (int y = 1; y < height-1; y++) {
            for (int x = 1; x < width-1; x++) {

                if(mask != null && !mask[x][y]){
                    continue;
                }

                grayX = - luminance[x-1][y-1] + luminance[x-1][y-1+2] - 2* luminance[x-1+1][y-1] + 2* luminance[x-1+1][y-1+2] - luminance[x-1+2][y-1]+ luminance[x-1+2][y-1+2];
                grayY = luminance[x-1][y-1] + 2* luminance[x-1][y-1+1] + luminance[x-1][y-1+2] - luminance[x-1+2][y-1] - 2* luminance[x-1+2][y-1+1] - luminance[x-1+2][y-1+2];

                // Magnitudes sum
                magnitude = 255 - Image.SAFECOLOR(Math.abs(grayX) + Math.abs(grayY));
                Paint grayscaleColor = grayMatrix[magnitude];
                // Apply the color into a new image
                int value=grayscaleColor.getColor();

                int a=imageIn.getAComponent(x, y);
                int r=imageIn.getRComponent(value);
                int g=imageIn.getGComponent(value);
                int b=imageIn.getBComponent(value);
                int finalColor=Color.argb(a,r,g,b);

                if(r>=255&&g>=255&&b>=255){
                    finalColor=Color.BLACK;
                }
                imageIn.setPixelColor(x, y, finalColor);
            }
        }

        return imageIn;
    }


   

    /**
     * Apply the luminance
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    private int luminance(int r, int g, int b) {
        return (int) ((0.299 * r) + (0.58 * g) + (0.11 * b));
    }

}

