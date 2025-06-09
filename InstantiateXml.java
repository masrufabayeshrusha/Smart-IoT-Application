import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class InstantiateXml {
	private String paramFilePath;
    private ArrayList<Context> contextList;//Context|identifier|tailoring 
    private ArrayList<Solution> solutionList;//Solution|identifier|assessment
    private ArrayList<Goal> goalList;
    /*MainGoal|family|number|identifier|implemented by|control|baseline allocation|provision set|1
	ReqGoal|identifier|assessment process|functional requirement|1
	moduleGoal|identifier|assessment process|1
	opGoal|identifier|variable|condition|1*/
    private ArrayList<Strategy> strategyList; //strategy|identifier|assessment process
   
    private ArrayList<InContextOf> inContextOfLinks;//InContextOf|contextSourceGoal|contextDestinationContext
    
    private ArrayList<SupportedBy> supportedByLinks;
    //SupportedBy|supportSourceWithProvideRequires|supportDestinationWithProvideRequires|provision set


    public InstantiateXml(String paramFilePath) {
        this.paramFilePath = paramFilePath;
        contextList = new ArrayList<>();
        solutionList = new ArrayList<>();
        goalList = new ArrayList<>();
        strategyList = new ArrayList<>();
        inContextOfLinks = new ArrayList<>();
        supportedByLinks = new ArrayList<>();
    }

    public void collectAllNodes() {
        File file = new File(this.paramFilePath);
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] params = line.split("\\|");
                switch (params[0].toLowerCase()) {
                    case "context":
                        collectContextNodes(params);
                        break;
                    case "solution":
                        collectSolutionNodes(params);
                        break;
                    case "maingoal":
                    case "reqgoal":
                    case "modulegoal":
                    case "opgoal":
                        collectGoalNodes(params);
                        break;
                    case "strategy":
                        collectStrategyNodes(params);
                        break;
                    case "incontextof":
                        collectInContextOfLinks(params);
                        break;
                    case "supportedby":
                        collectSupportedByLinks(params);
                        break;
                    default:
                        // Handle unknown types or ignore
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Context|identifier|tailoring 
   
    private void collectContextNodes(String[] params) {
        if (params.length == 3) {
            Context context = new Context(params[1], params[2]);
            contextList.add(context);
        }
    }
    //Solution|identifier|assessment
   
    private void collectSolutionNodes(String[] params) {
        if (params.length == 3) {
            Solution solution = new Solution(params[1], params[2]);
            solutionList.add(solution);
        }
    }
    /*MainGoal|family|number|identifier|implemented by|control|baseline allocation|provision set|1
    ReqGoal|identifier|assessment process|functional requirement|1
    moduleGoal|identifier|assessment process|1
    opGoal|identifier|variable|condition|1*/
    
    private void collectGoalNodes(String[] params) {
        switch (params[0].toLowerCase()) {
            case "maingoal":
                if (params.length == 9) {
                    MainGoal mainGoal = new MainGoal(params[1], params[2], params[3], params[4],
                            params[5], params[6], params[7], params[8]);
                    goalList.add(mainGoal);
                }
                break;
            case "reqgoal":
                if (params.length == 5) {
                    ReqGoal reqGoal = new ReqGoal(params[1], params[2], params[3], params[4]);
                    goalList.add(reqGoal);
                }
                break;
            case "modulegoal":
                if (params.length == 4) {
                    ModuleGoal modGoal = new ModuleGoal(params[1], params[2], params[3]);
                    goalList.add(modGoal);
                }
                break;
            case "opgoal":
                if (params.length == 5) {
                    OppGoal oppGoal = new OppGoal(params[1], params[2], params[3], params[4]);
                    goalList.add(oppGoal);
                }
                break;
        }
    }
    //strategy|identifier|assessment process
    
    private void collectStrategyNodes(String[] params) {
        if (params.length == 3) {
            Strategy strategy = new Strategy(params[1], params[2]);
            strategyList.add(strategy);
        }
    }
    //InContextOf|contextSourceGoal|contextDestinationContext
   
    
    private void collectInContextOfLinks(String[] params) {
        if (params.length == 3) {
            InContextOf inContextOf = new InContextOf(params[1], params[2]);
            inContextOfLinks.add(inContextOf);
        }
    }
    // SupportedBy|supportSource|supportDestination
    //SupportedBy|supportSourceWithProvideRequires|supportDestinationWithProvideRequires|provision set
    private void collectSupportedByLinks(String[] params) {
        if (params.length >= 3) {
            SupportedBy supportedBy = params.length == 3 ?
                    new SupportedBy(params[1], params[2]) :
                    new ProvidesRequires(params[1], params[2], params[3]);
            supportedByLinks.add(supportedBy);
        }
    }

    // Getters
    public ArrayList<Context> getContextList() {
        return contextList;
    }

    public ArrayList<Solution> getSolutionList() {
        return solutionList;
    }

    public ArrayList<Goal> getGoalList() {
        return goalList;
    }

    public ArrayList<Strategy> getStrategyList() {
        return strategyList;
    }

    public ArrayList<InContextOf> getInContextOfLinks() {
        return inContextOfLinks;
    }

    public ArrayList<SupportedBy> getSupportedByLinks() {
        return supportedByLinks;
    }
}
