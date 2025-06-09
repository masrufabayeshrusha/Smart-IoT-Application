import java.util.ArrayList;
import java.util.HashSet;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
public class PathFinder {
    ArrayList<String[]> nameTypeTable;  // Contains node name and type
    ArrayList<Context> contextList;
    ArrayList<Solution> solutionList;
    ArrayList<Goal> goalList;
    ArrayList<Strategy> strategyList;
    ArrayList<ArrayList<String>> allPaths;

    public PathFinder(ArrayList<String[]> nameTypeTable,  ArrayList<Solution> solutionList,
                       ArrayList<Strategy> strategyList, ArrayList<Goal> GoalList,ArrayList<ArrayList<String>> allPaths) {
        this.nameTypeTable = nameTypeTable;
        this.contextList = contextList;
        this.solutionList = solutionList;
        this.goalList = GoalList;
        this.strategyList = strategyList;
        this.allPaths = allPaths;
    }

    // Method to get the type of a node from nameTypeTable
    private String getNodeType(String nodeName) {
        for (String[] entry : nameTypeTable) {
            if (entry[0].equals(nodeName)) {
                return entry[1];  // Return the type (e.g., "Solution", "Goal", "Strategy")
            }
        }
        return null;  // Node type not found
    }

    // Method to get the description of a node based on its type
    private String getNodeDescription(String nodeName, String nodeType) {
    	System.out.print("nodeType "+nodeName+ nodeType);
        switch (nodeType) {
            case "Solution":
                for (Solution solution : solutionList) {
                    if (solution.getIdentifier().equals(nodeName)) {
                        return solution.getAssessment();
                    }
                }
                break;
            case "opGoal":
            	
                for (Goal opGoal : goalList) {
                	
                    if (opGoal.getIdentifier().equals(nodeName)) {
                    	
                    	OppGoal opGoal1=(OppGoal) opGoal;
                        return opGoal1.getVariable();
                    }
                }
                break;
            case "Strategy":
                for (Strategy strategy : strategyList) {
                    if (strategy.getIdentifier().equals(nodeName)) {
                        return strategy.getAssessmentProcess();
                    }
                }
                break;
            // Add similar cases for other types (e.g., Context) if needed
        }
        return "Description not found";
    }

    // Method to find all paths from a node to the first Strategy node and remove duplicates
    public ArrayList<ArrayList<String>> findAllPathsToStrategy(String solutionNode) {
        HashSet<ArrayList<String>> uniquePaths = new HashSet<>();  // HashSet to ensure uniqueness

        for (ArrayList<String> path : allPaths) {
            if (path.contains(solutionNode)) {
                int solutionIndex = path.indexOf(solutionNode);

                // Traverse backwards to find the first Strategy node
                for (int i = solutionIndex - 1; i >= 0; i--) {
                    String node = path.get(i);
                    String nodeType = getNodeType(node);

                    if (nodeType != null && nodeType.equals("Strategy")) {
                        uniquePaths.add(new ArrayList<>(path.subList(i, solutionIndex + 1)));
                        break;  // Stop searching this path once the first Strategy node is found
                    }
                }
            }
        }
        return new ArrayList<>(uniquePaths);
    }

 // Method to return the table content as a CSV string
    public String getPathsAsCSV(ArrayList<ArrayList<String>> paths) {
        StringBuilder csvContent = new StringBuilder();

        // Add the CSV headers
        csvContent.append("Solution,Solution Name,Goal,Goal Name,Strategy,Strategy Name\n");

        // Iterate over the paths and build each row
        for (ArrayList<String> path : paths) {
            String strategy = null;
            String goal = null;
            String solution = null;

            String strategyDescription = null;
            String goalDescription = null;
            String solutionDescription = null;

            for (String node : path) {
                String nodeType = getNodeType(node);
                System.out.print("node "+node+nodeType);
                // Check for strategy
                if (nodeType != null && nodeType.equals("Strategy")) {
                    strategy = node;
                    strategyDescription = getNodeDescription(node, nodeType);
                }
                // Check for goal
                else if (nodeType != null && nodeType.equals("opGoal")) {
                    goal = node;
                    goalDescription = getNodeDescription(node, nodeType);
                }
                // Check for solution
                else if (nodeType != null && nodeType.equals("Solution")) {
                    solution = node;
                    solutionDescription = getNodeDescription(node, nodeType);
                }
            }

            // Append the row for this path in CSV format
            csvContent.append(String.format("%s,%s,%s,%s,%s,%s\n",
                    solution, solutionDescription, goal, goalDescription, strategy, strategyDescription));
        }

        // Return the CSV content as a string
        return csvContent.toString();
    }


 // Method to write CSV content to a file
    public void writeCSVToFile(String fileName, String csvContent) {
        try {
            File file = new File(fileName);

            // Get the absolute path and log it
            String absolutePath = file.getAbsolutePath();
            System.out.println("Writing CSV file to: " + absolutePath);

            // Write to the file
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(csvContent);
                System.out.println("CSV file written successfully: " + absolutePath);
            }
        } catch (IOException e) {
            System.out.println("Error while writing CSV file: " + e.getMessage());
        }
    }
}
