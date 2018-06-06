/*
 * Copyright 2018 lolnet.co.nz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.co.lolnet.rainbow.util;

import java.awt.Color;

public class HSLColor {
    
    private final Color color;
    private final float[] hsl;
    private final float alpha;
    
    private HSLColor(Color color) {
        this.color = color;
        this.hsl = fromRGB();
        this.alpha = getColor().getAlpha() / 255.0f;
    }
    
    public static HSLColor of(Color color) {
        return new HSLColor(color);
    }
    
    public Color getIncremented(float increment) {
        return adjustHue(getHue() + increment);
    }
    
    public Color adjustHue(float degrees) {
        return toRGB(degrees, getSaturation(), getLuminance(), getAlpha());
    }
    
    public float[] fromRGB() {
        float[] rgb = getColor().getRGBColorComponents(null);
        float red = rgb[0];
        float green = rgb[1];
        float blue = rgb[2];
        
        float max = Math.max(red, Math.max(green, blue));
        float min = Math.min(red, Math.min(green, blue));
        
        float hue = 0;
        if (max == min) {
            hue = 0;
        } else if (max == red) {
            hue = ((60 * (green - blue) / (max - min)) + 360) % 360;
        } else if (max == green) {
            hue = (60 * (blue - red) / (max - min)) + 120;
        } else if (max == blue) {
            hue = (60 * (red - green) / (max - min)) + 240;
        }
        
        float luminance = (max + min) / 2;
        float saturation = 0;
        
        if (max == min) {
            saturation = 0;
        } else if (luminance <= 0.5f) {
            saturation = (max - min) / (max + min);
        } else {
            saturation = (max - min) / (2 - max - min);
        }
        
        return new float[]{hue, saturation * 100, luminance * 100};
    }
    
    public Color toRGB() {
        return toRGB(getHue(), getSaturation(), getLuminance(), getAlpha());
    }
    
    public Color toRGB(float hue, float saturation, float luminance, float alpha) throws IllegalArgumentException {
        if (saturation < 0.0f || saturation > 100.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Saturation");
        }
        
        if (luminance < 0.0f || luminance > 100.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Luminance");
        }
        
        if (alpha < 0.0f || alpha > 1.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Alpha");
        }
        
        hue = hue % 360.0f;
        hue /= 360f;
        saturation /= 100f;
        luminance /= 100f;
        
        float q = 0;
        
        if (luminance < 0.5) {
            q = luminance * (1 + saturation);
        } else {
            q = (luminance + saturation) - (saturation * luminance);
        }
        
        float p = 2 * luminance - q;
        float red = Math.max(0, HueToRGB(p, q, hue + (1.0f / 3.0f)));
        float green = Math.max(0, HueToRGB(p, q, hue));
        float blue = Math.max(0, HueToRGB(p, q, hue - (1.0f / 3.0f)));
        
        red = Math.min(red, 1.0f);
        green = Math.min(green, 1.0f);
        blue = Math.min(blue, 1.0f);
        return new Color(red, green, blue, alpha);
    }
    
    public float HueToRGB(float p, float q, float h) {
        if (h < 0) {
            h += 1;
        }
        
        if (h > 1) {
            h -= 1;
        }
        
        if (6 * h < 1) {
            return p + ((q - p) * 6 * h);
        }
        
        if (2 * h < 1) {
            return q;
        }
        
        if (3 * h < 2) {
            return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
        }
        
        return p;
    }
    
    public Color getColor() {
        return color;
    }
    
    public float[] getHSL() {
        return hsl;
    }
    
    public float getHue() {
        if (getHSL() != null && getHSL().length == 3) {
            return getHSL()[0];
        }
        
        return 0.0f;
    }
    
    public float getSaturation() {
        if (getHSL() != null && getHSL().length == 3) {
            return getHSL()[1];
        }
        
        return 0.0f;
    }
    
    public float getLuminance() {
        if (getHSL() != null && getHSL().length == 3) {
            return getHSL()[2];
        }
        
        return 0.0f;
    }
    
    public float getAlpha() {
        return alpha;
    }
}