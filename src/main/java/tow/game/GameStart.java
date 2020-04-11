package tow.game;

import tow.engine.resources.JsonContainerLoader;
import tow.game.client.Storage;
import tow.game.client.map.Wall;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GameStart {

    public static void main(String[] args) throws IOException {
        //Loader.start(new Game(), new NetGameRead(), new Storage(), new Server(), new NetServerRead());

        //File f = new File("maps/small.maptest");
        //convert(f, new File("new-maps/" + f.getName()));

        for(File f : new File("maps").listFiles()){
            convert(f, new File("new-maps/" + f.getName()));
        }
    }

    public static void convert(File input, File output){
        System.out.println("Convert: " + input.getName());

        try (Scanner scanner = new Scanner(new FileReader(input))){

            int w = scanner.nextInt();
            int h = scanner.nextInt();
            String background = scanner.next();

            MapContainer mapContainer = new MapContainer();
            List<MapObjectContainer> mapObjectContainerList = new ArrayList<>();
            mapContainer.width = w;
            mapContainer.height = h;

            MapObjectContainer mapObjectContainerBackground = new MapObjectContainer();
            mapObjectContainerBackground.type = "scaled";
            mapObjectContainerBackground.x = 0;
            mapObjectContainerBackground.y = 0;
            mapObjectContainerBackground.z = 0;
            mapObjectContainerBackground.parameters = new TreeMap<>();
            mapObjectContainerBackground.parameters.put("direction", 0);
            mapObjectContainerBackground.parameters.put("texture", background);
            mapObjectContainerBackground.parameters.put("width", w);
            mapObjectContainerBackground.parameters.put("height", h);
            mapObjectContainerList.add(mapObjectContainerBackground);

            while (scanner.hasNext()){
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int dir = scanner.nextInt();
                String texture = scanner.next();

                dir = ((dir%360) + 360) % 360;
                int z = getDepth(texture);
                String type = getType(texture);
                if (type.equals("home") || type.equals("tree")) type = "destroyed";

                MapObjectContainer mapObjectContainer = new MapObjectContainer();
                mapObjectContainer.type = type;
                mapObjectContainer.x = x;
                mapObjectContainer.y = y;
                mapObjectContainer.z = z;
                mapObjectContainer.parameters = new TreeMap<>();
                mapObjectContainer.parameters.put("direction", dir);
                mapObjectContainer.parameters.put("texture", texture);
                if (type.equals("destroyed")) mapObjectContainer.parameters.put("stability", Wall.getStabilityByType(getType(texture)));

                mapObjectContainerList.add(mapObjectContainer);
            }


            mapContainer.mapObjectContainers = mapObjectContainerList.toArray(new MapObjectContainer[0]);
            JsonContainerLoader.saveExternalFile(mapContainer, output.getPath());
        } catch (RuntimeException | IOException e){
            System.err.println("Exception in map: " + input.getName());
            e.printStackTrace();
        }

    }

    public static int getDepth(String texture){
        String[][] images = new Storage().getImages();
        for(String[] image : images){
            if (image[0].substring(image[0].lastIndexOf("/")+1, image[0].lastIndexOf(".")).equals(texture)){
                return Integer.parseInt(image[2]);
            }
        }

        throw new RuntimeException("Texture not found: " + texture);
    }

    public static String getType(String texture){
        String[][] images = new Storage().getImages();
        for(String[] image : images){
            if (image[0].substring(image[0].lastIndexOf("/")+1, image[0].lastIndexOf(".")).equals(texture)){
                return image[1];
            }
        }

        throw new RuntimeException("Texture not found: " + texture);
    }

    private static class MapContainer{
        public int width, height;
        public MapObjectContainer[] mapObjectContainers;
    }

    private static class MapObjectContainer{
        public int x, y, z;
        public String type;
        public Map<String, Object> parameters;
    }
}
