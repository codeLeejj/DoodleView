package com.lee.boodlelib.doodle.config;

import android.graphics.Color;

/**
 * @author lee
 * @title: DoodleConfig
 * @description:
 * @date 2021/2/9  15:16
 */
public class DoodleConfig {
    private int paintSize;
    private int paintColor;

    private DoodleConfig() {
    }

    public int getPaintSize() {
        return paintSize;
    }

    public int getPaintColor() {
        return paintColor;
    }

    public static class Builder {
        int size = 3;
        int color = Color.BLACK;

        public Builder() {
        }

        public Builder setPaintSize(int size) {
            this.size = size;
            return this;
        }

        public Builder setPaintColor(int color) {
            this.color = color;
            return this;
        }

        public DoodleConfig build() {
            DoodleConfig doodleConfig = new DoodleConfig();
            doodleConfig.paintColor = color;
            doodleConfig.paintSize = size;
            return doodleConfig;
        }
    }

    public static DoodleConfig getDefaultConfig() {
        return new Builder().build();
    }
}
