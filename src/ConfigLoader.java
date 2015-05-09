import java.io.FileReader;
import java.lang.*;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.newdawn.slick.opengl.Texture;

public class ConfigLoader {
    public static JSONObject getJSON(String path) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        try {
            Object obj = parser.parse(new FileReader(path));
            jsonObject = (JSONObject) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

class FontLoader {

    public static Map<String, Integer> chars = new HashMap<>();

    public static Texture get(java.lang.Character c){
        Resources resources =ResourceLoader.resCache.get("font");
        int ch = 0;
        try {
            ch= chars.get(c.toString());
        } catch (Exception x) {
            System.out.println("Character " + c.toString() + " is not declared");
            //x.printStackTrace();
        }

        return resources.textures[ch];
    }
    public FontLoader(JSONObject jsonObject) {
        String texture = (String) jsonObject.get("texture");
        JSONArray frame_size = (JSONArray)jsonObject.get("frame_size");
        JSONObject mapping = ((JSONObject) jsonObject.get("symbols"));
        for (Object o1 : mapping.keySet()) {
            String key = (String) o1;
            chars.put(key, ((Long) mapping.get(key)).intValue());
        }
        mapping = ((JSONObject) jsonObject.get("map"));
        for (Object o : mapping.keySet()) {
            String key = (String) o;
            String mapTo = (String)mapping.get(key);
            int pos = chars.get(mapTo);
            chars.put(key, pos);
        }
        new ResourceLoader(
            "font",
            new Resources(
                texture,
                ((Long)frame_size.get(0)).intValue(),
                ((Long)frame_size.get(1)).intValue()
            )
        );


    }
}


class ResourceLoader {
    static Map<String, Resources> resCache = new HashMap<>();

    public ResourceLoader (String name, Resources resources) {
        resCache.put(name,resources);
    }
}

class Stats{
    public int max_hp;
    public int max_mp;
    public int movementSpeed;
    public int attackSpeed;
    public int ad;
    public int ap;
    public int armor;
    public int magic_resistance;

    public Stats(int max_hp, int max_mp, int movementSpeed, int attackSpeed, int ad, int ap, int armor, int magic_resistance) {
        this.max_hp = max_hp;
        this.max_mp = max_mp;
        this.movementSpeed = movementSpeed;
        this.attackSpeed = attackSpeed;
        this.ad = ad;
        this.ap = ap;
        this.armor = armor;
        this.magic_resistance = magic_resistance;
    }
}

class StatLoader {
    private static Map<String, Stats> statCache= new HashMap<>();
    public StatLoader(String name, JSONObject jsonObject) {
        Stats stats = new Stats(
                ((Long)jsonObject.get("max_hp")).intValue(),
                ((Long)jsonObject.get("max_mp")).intValue(),
                ((Long)jsonObject.get("movementSpeed")).intValue(),
                ((Long)jsonObject.get("attackSpeed")).intValue(),
                ((Long)jsonObject.get("ad")).intValue(),
                ((Long)jsonObject.get("ap")).intValue(),
                ((Long)jsonObject.get("armor")).intValue(),
                ((Long)jsonObject.get("magic_resistance")).intValue()
        );
        statCache.put(name, stats);


    }
    public static Stats get(String s) {
        Stats stats = statCache.get(s);
        if (stats == null) {
            System.out.printf("No stats info exist for: " + s);
        }
        return stats;

    }
}

class ModelLoader {
    private static Map<String, Model>  modelCache= new HashMap<>();
    public static Model get(String s) {
        Model model = modelCache.get(s);
        if (model == null) {
            System.out.printf("Nonexistent model requested: " + s);
        }
        return model;

    }
    public ModelLoader(String name, JSONObject jsonObject) {
        String texture = (String) jsonObject.get("texture");
        JSONArray frame_size = (JSONArray)jsonObject.get("frame_size");
        JSONArray origin = (JSONArray)jsonObject.get("origin");
        JSONArray model_size = (JSONArray)jsonObject.get("model_size");

        Resources resources = ResourceLoader.resCache.get(texture);

        if  (resources == null) {
            new ResourceLoader(
                texture,
                new Resources(
                    texture,
                    ((Long)frame_size.get(0)).intValue(),
                    ((Long)frame_size.get(1)).intValue()
                )
            );
            resources = ResourceLoader.resCache.get(texture);
        }
        Model model = new Model(
            resources,
            new Rectangle(
                ((Long)origin.get(0)).intValue(),
                ((Long)origin.get(1)).intValue(),
                ((Long) model_size.get(0)).intValue(),
                ((Long) model_size.get(1)).intValue()
            )
        );
        //animations
        JSONObject animations = ((JSONObject) jsonObject.get("animations"));
        String key;

        boolean interrupting;
        int[] timings;
        int[] tiles;
        JSONArray JSONTimings;
        JSONArray JSONTiles;
        JSONObject obj;
        // = (JSONArray)jsonObject.get("frame_size");
        for (Object o : animations.keySet()) {
            key = (String) o;
            obj = ((JSONObject) animations.get(key));

            JSONTiles = (JSONArray)obj.get("tiles");
            tiles = new int[JSONTiles.size()];
            for (int i = 0; i < JSONTiles.size(); i++) {
                tiles[i] = ((Long)JSONTiles.get(i)).intValue();
            }

            JSONTimings = (JSONArray)obj.get("timings");
            timings = new int[JSONTimings.size()];
            for (int i = 0; i < JSONTimings.size(); i++) {
                timings[i] = ((Long)JSONTimings.get(i)).intValue();
            }
            interrupting = (boolean)obj.get("interrupting");

            model.animations.put(key, new Animation(key, interrupting, timings, tiles));
        }

        modelCache.put(name, model);

    }
}