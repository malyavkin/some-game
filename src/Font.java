public class Font {
    public static Resources fnt = new Resources("res/font2.png", 8, 8);

    
    
    public static void render(String s, Point location, int scale, Color color) {
        Point coords = location.same();
        for (int i = 0; i < s.length(); i++) {
            int cp = s.codePointAt(i);
            cp -= "A".codePointAt(0);
            DrawShit.shittySquare(coords, fnt.size.mul(scale), fnt.textures[cp],color);
            coords = coords.add(fnt.size.onlyX().mul(scale));
        }
    }
}
