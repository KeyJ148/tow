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

        if (! new File("new-maps/").exists()) new File("new-maps/").mkdir();
        for(File f : new File("maps").listFiles()){
            String outputFileName = "new-maps/" + f.getName();
            if (!outputFileName.contains(".maptest")) outputFileName = outputFileName.substring(0, outputFileName.indexOf(".map")) + ".json";
            convert(f, new File(outputFileName));
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
            mapObjectContainerBackground.type = "repeated";
            mapObjectContainerBackground.x = w/2;
            mapObjectContainerBackground.y = h/2;
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
                if (type.equals("road")) mapObjectContainer.parameters.put("width", getRoadSize(texture));
                if (type.equals("road")) mapObjectContainer.parameters.put("height", getRoadSize(texture));

                mapObjectContainerList.add(mapObjectContainer);
            }


            mapContainer.mapObjectContainers = mapObjectContainerList.toArray(new MapObjectContainer[0]);
            JsonContainerLoader.saveExternalFile(mapContainer, output.getPath());
        } catch (RuntimeException | IOException e){
            System.err.println("Exception in map: " + input.getName());
            e.printStackTrace();
        }

    }

    public static int getRoadSize(String texture){
        String[][] images = new Storage().getRoadSize();
        for(String[] image : images){
            if (image[0].substring(image[0].lastIndexOf("/")+1, image[0].lastIndexOf(".")).equals(texture)){
                return Integer.parseInt(image[1]);
            }
        }

        throw new RuntimeException("Texture not found: " + texture);
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
