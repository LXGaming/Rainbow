/*
 * Copyright 2018 Alex Thomson
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

package io.github.lxgaming.rainbow.util;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.util.Color;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class Toolbox {
    
    public static Text getTextPrefix() {
        Text.Builder textBuilder = Text.builder();
        textBuilder.onHover(TextActions.showText(getPluginInformation()));
        textBuilder.append(Text.of(TextColors.BLUE, TextStyles.BOLD, "[", Reference.NAME, "]"));
        return Text.of(textBuilder.build(), TextStyles.RESET, " ");
    }
    
    public static Text getPluginInformation() {
        Text.Builder textBuilder = Text.builder();
        textBuilder.append(Text.of(TextColors.BLUE, TextStyles.BOLD, Reference.NAME, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Version: ", TextColors.WHITE, Reference.VERSION, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Authors: ", TextColors.WHITE, Reference.AUTHORS, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Source: ", TextColors.BLUE, getURLTextAction(Reference.SOURCE), Reference.SOURCE, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Website: ", TextColors.BLUE, getURLTextAction(Reference.WEBSITE), Reference.WEBSITE));
        return textBuilder.build();
    }
    
    public static TextAction<?> getURLTextAction(String url) {
        try {
            return TextActions.openUrl(new URL(url));
        } catch (MalformedURLException ex) {
            return TextActions.suggestCommand(url);
        }
    }
    
    public static Optional<String> getOptionFromSubject(Subject subject, String... keys) {
        for (String key : keys) {
            Optional<String> option = subject.getOption(subject.getActiveContexts(), key);
            if (option.isPresent()) {
                return option;
            }
            
            option = subject.getOption(key);
            if (option.isPresent()) {
                return option;
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Removes non-printable characters (excluding new line and carriage return) in the provided {@link java.lang.String String}.
     *
     * @param string The {@link java.lang.String String} to filter.
     * @return The filtered {@link java.lang.String String}.
     */
    public static String filter(String string) {
        return StringUtils.replaceAll(string, "[^\\x20-\\x7E\\x0A\\x0D]", "");
    }
    
    // https://stackoverflow.com/questions/22973532/java-creating-a-discrete-rainbow-colour-array
    public static Color HueToColor(float hue) {
        return HSVToColor(hue, 1.0F, 1.0F);
    }
    
    public static Color HSVToColor(float hue, float saturation, float brightness) {
        return Color.of(java.awt.Color.getHSBColor(hue / 360, saturation, brightness));
    }
    
    public static Optional<Float> parseFloat(String string) {
        try {
            return Optional.of(Float.parseFloat(string));
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }
    
    public static boolean containsIgnoreCase(Collection<String> list, String targetString) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        
        for (String string : list) {
            if (StringUtils.equalsIgnoreCase(string, targetString)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static <T> T[] fillArray(T[] type, T defaultValue) {
        Arrays.fill(type, defaultValue);
        return type;
    }
    
    public static <T> T cast(Object object, Class<? extends T> type) {
        return type.cast(object);
    }
    
    public static <T> Optional<T> newInstance(Class<? extends T> type) {
        try {
            return Optional.of(type.newInstance());
        } catch (Throwable ex) {
            return Optional.empty();
        }
    }
}