package org.example.Model.ConfigTemplates;

import java.util.*;

public class FarmTemplate {
    public String type;
    public int width;
    public int height;
    public Block house;
    public Block greenHouse;
    public Block quarry;
    public List<Block> lake; 

    public static class Block {
        public int startX;
        public int startY;
        public int width;
        public int height;
    }
}
