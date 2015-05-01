import java.io.FileReader;
import java.lang.*;
import java.util.HashMap;
import java.util.Iterator;
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
            x.printStackTrace();
        }

        return resources.textures[ch];
    }
    public FontLoader(JSONObject jsonObject) {
        String texture = (String) jsonObject.get("texture");
        JSONArray frame_size = (JSONArray)jsonObject.get("frame_size");
        JSONObject mapping = ((JSONObject) jsonObject.get("symbols"));
        for(Iterator iterator = mapping.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            chars.put(key, ((Long) mapping.get(key)).intValue());
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

        if (resources == null) {
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
        modelCache.put(name, model);

    }
}