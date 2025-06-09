import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenerateXml {
    private DocumentBuilderFactory documentFactory;
    private DocumentBuilder dBuilder;
    private Document document;
    private InstantiateXml1 instantiateXml;

    public GenerateXml() {}

    public GenerateXml(String paramFilePath, String xmlFilePath)
    {
        System.out.println("hi");
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("GSN");
            document.appendChild(root);
            //
            instantiateXml = new InstantiateXml1("./"+paramFilePath);
            
            LoadParamsForXml SAC = new LoadParamsForXml(document, root, instantiateXml);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("./"+xmlFilePath));

            transformer.transform(domSource, streamResult);
         // Collect the supportedByLinks relationships
            ArrayList<SupportedBy> supportedByLinks = instantiateXml.getSupportedByLinks();
            System.out.println("Number of SupportedBy Links: " + supportedByLinks.size());
            instantiateXml.printNameTypeTable();  // Print the result in table form
//            // Loop through the supportedByLinks and print the relationships
//            for (SupportedBy link : supportedByLinks) {
//                System.out.println("SupportedBy Source: " + link.getSource() + " Destination: " + link.getDestination());
//                // Optionally, you can append these links to the XML as elements
//                
//            }
            //ArrayList<SupportedBy> supportedByLinks = instantiateXml.getSupportedByLinks();
            String rootNode = "SI-4 Main"; // Your starting point (root)

         // Get all paths from root to leaves
            ArrayList<ArrayList<String>> paths = TreeTraversalNonRecursive.traverseTreeNonRecursive("SI-4 Main", supportedByLinks);
            ArrayList<ArrayList<String>> path_1 = TreeTraversalNonRecursive.traverseTreeNonRecursive("SC-5(3) Main", supportedByLinks);
            ArrayList<ArrayList<String>> allPaths = new ArrayList<>();
            allPaths.addAll(paths);
            allPaths.addAll(path_1);
//            allPaths.
            // Print the paths
            for (ArrayList<String> path : allPaths) {
                System.out.println(path);
            }
            // Name of the output CSV file
            String fileName = "All_possible_path.txt";

            // Writing data into the CSV file
            try (FileWriter writer = new FileWriter(fileName)) {
                // Iterate over all paths
            	 // Iterate over all paths
                for (ArrayList<String> path : allPaths) {
                    // Join all elements of the path into a single string
                    String pathAsString = String.join("|", path);  // Using space between elements
                    writer.append(pathAsString);  // Write the single string to the file
                    writer.append("\n");  // New line after each row
                }
            } catch (IOException e) {
                System.err.println("Error writing to CSV file.");
                e.printStackTrace();
            }
         // Instantiate PathFinder and search for the path to the first Strategy node
          
            System.out.println(instantiateXml.getGoalList());
            for (Goal opGoal : instantiateXml.getGoalList()) {
            	
            	System.out.println(opGoal.getAchievementWeight());
            }
            PathFinder pathFinder = new PathFinder(instantiateXml.getNameTypeTable(),instantiateXml.getSolutionList(),instantiateXml.getStrategyList(),instantiateXml.getGoalList(), allPaths);
            ArrayList<ArrayList<String>> result1 = pathFinder.findAllPathsToStrategy("Sn14");
            ArrayList<ArrayList<String>> result2=pathFinder.findAllPathsToStrategy("Sn11");
            ArrayList<ArrayList<String>> result3=pathFinder.findAllPathsToStrategy("Sn6");
            ArrayList<ArrayList<String>> All_results=new ArrayList<>();
            
            All_results.addAll(result1);
            All_results.addAll(result2);
            All_results.addAll(result3);

            // Output the result
            if (All_results != null) {
            	System.out.println(All_results);
            } 
            else {
                System.out.println("No path found.");
            }
            // Get the CSV content
            String csvContent = pathFinder.getPathsAsCSV(All_results);

            // Write the CSV content to a file
            pathFinder.writeCSVToFile("output_paths.csv", csvContent);
            
            System.out.println("Done creating XML File");
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public Document getDocument() {return document; }

    public InstantiateXml1 getInstantiateXml() {
        return instantiateXml;
    }
}
