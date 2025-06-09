import java.util.ArrayList;

public class TreeTraversal {

    public static ArrayList<ArrayList<String>> findPaths(String root, ArrayList<SupportedBy> supportedByLinks) {
        ArrayList<ArrayList<String>> paths = new ArrayList<>();
        ArrayList<String> currentPath = new ArrayList<>();
        traverseTree(root, supportedByLinks, currentPath, paths);
        return paths;
    }

    private static void traverseTree(String node, ArrayList<SupportedBy> supportedByLinks, ArrayList<String> currentPath, ArrayList<ArrayList<String>> paths) {
        currentPath.add(node);
        System.out.println("Current Path: " + currentPath);  // Debugging output to track path updates
        
        boolean isLeaf = true;

        for (SupportedBy link : supportedByLinks) {
            // Print the node and source with quotes to see if they are equal
            System.out.println("Comparing: \"" + node + "\" with \"" + link.getSource() + "\"");

            if (link.getSource().equals(node.trim())) {  // Use trim() to avoid whitespace issues
                isLeaf = false;
                traverseTree(link.getDestination(), supportedByLinks, new ArrayList<>(currentPath), paths);
            }
        }
        
        // If it's a leaf node, add the path to the list
        if (isLeaf) {
            System.out.println("Adding path to paths: " + currentPath);  // Debugging output to track when we add a path
            paths.add(currentPath);
        }
    }


}
