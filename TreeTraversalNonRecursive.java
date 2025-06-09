import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import java.util.LinkedList;
import java.util.Queue;

public class TreeTraversalNonRecursive {

	public static ArrayList<ArrayList<String>> traverseTreeNonRecursive(String rootNode, ArrayList<SupportedBy> supportedByLinks) {
        // Map to store children based on their parents
        HashMap<String, ArrayList<String>> adjacencyList = new HashMap<>();

        // Build the adjacency list from the SupportedBy links
        for (SupportedBy link : supportedByLinks) {
            adjacencyList
                .computeIfAbsent(link.getSource(), k -> new ArrayList<>())
                .add(link.getDestination());
        }

        // List to store all paths
        ArrayList<ArrayList<String>> paths = new ArrayList<>();

        // Stack to hold nodes and their current path
        Stack<ArrayList<String>> stack = new Stack<>();
        ArrayList<String> initialPath = new ArrayList<>();
        initialPath.add(rootNode);
        stack.push(initialPath);

        // Perform the non-recursive DFS
        while (!stack.isEmpty()) {
            // Get the current path
            ArrayList<String> currentPath = stack.pop();
            // Get the last node in the current path
            String currentNode = currentPath.get(currentPath.size() - 1);

            // Check if the current node is a leaf (no children)
            if (!adjacencyList.containsKey(currentNode)) {
                // If it's a leaf node, save the path
                paths.add(new ArrayList<>(currentPath));
            } else {
                // Otherwise, continue to explore the children
                for (String child : adjacencyList.get(currentNode)) {
                    ArrayList<String> newPath = new ArrayList<>(currentPath);
                    newPath.add(child);
                    stack.push(newPath);
                }
            }
        }

        return paths;
}
}