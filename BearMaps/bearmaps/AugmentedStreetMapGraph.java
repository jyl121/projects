package bearmaps;

import bearmaps.utils.graph.streetmap.Node;
import bearmaps.utils.graph.streetmap.StreetMapGraph;
import bearmaps.utils.ps.KDTree;
import bearmaps.utils.ps.MyTrieSet;
import bearmaps.utils.ps.Point;
import bearmaps.utils.ps.WeirdPointSet;

import java.util.*;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private List<Point> points = new ArrayList<Point>();
    private HashMap<Point, Node> nodePointHashMap = new HashMap<>();
    private HashMap<String, List<Node>> nameNodeHashMap = new HashMap<>();
    private KDTree thisTree;
    private WeirdPointSet weird;
    private MyTrieSet trie = new MyTrieSet();

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        List<Node> nodes = this.getNodes();
        for (Node n : nodes) {
            if (!this.neighbors(n.id()).isEmpty()) {
                Point nPoint = new Point(n.lon(), n.lat());
                nodePointHashMap.put(nPoint, n);
                points.add(nPoint);
            }
        }
        for (Node n: nodes) {
            if (n.name() != null) {
                String cleanName = cleanString(n.name());
                trie.add(cleanName);
                if (!nameNodeHashMap.containsKey(cleanName)) {
                    nameNodeHashMap.put(cleanName, new LinkedList<>());
                }
                nameNodeHashMap.get(cleanName).add(n);
            }
        }
        thisTree = new KDTree(points);
        weird = new WeirdPointSet(points);
    }

    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        return nodePointHashMap.get(weird.nearest(lon, lat)).id();
    }


    /**
     * For Project Part III (extra credit)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> output = new LinkedList<>();
        for (String cleanName: trie.keysWithPrefix(cleanString(prefix))) {
            for (Node n : nameNodeHashMap.get(cleanName)) {
                if (!output.contains(n.name())) {
                    output.add(n.name());
                }
            }
        }
        return output;
    }

    /**
     * For Project Part III (extra credit)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searcihed for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> output = new LinkedList<>();
        String cleanName = cleanString(locationName);
        if (nameNodeHashMap.containsKey(cleanName)) {
            for (Node n : nameNodeHashMap.get(cleanName)) {
                Map<String, Object> information = new HashMap<>();
                information.put("lat", n.lat());
                information.put("lon", n.lon());
                information.put("name", n.name());
                information.put("id", n.id());
                output.add(information);
            }
        }
        return output;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
