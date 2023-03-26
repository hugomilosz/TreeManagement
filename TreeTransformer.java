import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Set;

public class TreeTransformer {

  public static void main(String[] args) throws Exception {

    if (args.length < 2) {
      System.out.println("Not enough command-line arguments provided");
      return;
    }

    String inputFile1 = args[0];
    String inputFile2 = args[1];

    String input1AsString = readFileAsString(inputFile1);
    String input2AsString = readFileAsString(inputFile2);

    // create hash maps which maps a node to its leaves
    HashMap<Integer, ArrayList<Integer>> tree1 = new HashMap<Integer, ArrayList<Integer>>();
    HashMap<Integer, ArrayList<Integer>> tree2 = new HashMap<Integer, ArrayList<Integer>>();
    stringToMap(input1AsString, tree1);
    stringToMap(input2AsString, tree2);

    System.out.println(checkTrees(tree1, tree2));

  }

  // Read the files
  public static String readFileAsString(String inputFile) throws FileNotFoundException {
    String data = "";
    try {
      File myObj = new File(inputFile);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        data = myReader.nextLine();
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
      e.printStackTrace();
    }
    return data;
  }

  // Translates file string into a map from nodes to leaves
  public static void stringToMap(String inputFile, HashMap<Integer, ArrayList<Integer>> map) {
    String[] array = inputFile.split("\\[");
    for (String s : array) {
      if (!s.isEmpty()){
        int node = Integer.parseInt(String.valueOf(s.charAt(0)));
        int leaf = Integer.parseInt(String.valueOf(s.charAt(s.length()-2)));
        ArrayList<Integer> leafs = new ArrayList<>();
        if (map.containsKey(node)) {
          leafs = map.get(node);
        }
        leafs.add(leaf);
        map.put(node, leafs);
      }
    }
  }

  public static String checkTrees(HashMap<Integer, ArrayList<Integer>> tree1, HashMap<Integer, ArrayList<Integer>> tree2) {
    Set<Integer> initialNodes = tree1.keySet();
    Set<Integer> finalNodes = tree2.keySet();
    Set<Integer> nodesCopy = Set.copyOf(initialNodes);
    Set<Integer> finalNodesCopy = Set.copyOf(finalNodes);

    StringBuilder sb = new StringBuilder ();

    // Goes through each of the leaves in the first tree and if they're not leaves in the second tree, they're removed from the tree 
    for (Integer node : nodesCopy) {
      ArrayList<Integer> initialLeaves = tree1.get(node);
      ArrayList<Integer> finalLeaves = tree2.get(node);
      if (finalLeaves != null) {
        for (Integer leaf : initialLeaves) {
          if (!finalLeaves.contains(leaf)) {
            removeNode(tree1, leaf, sb);
          }
        }
      }
    }

    // Goes through each of the leaves in the second tree and if they're not leaves in the first tree, they're added to the tree
    for (Integer node : finalNodesCopy) {
      ArrayList<Integer> initialLeaves = tree1.get(node);
      ArrayList<Integer> finalLeaves = tree2.get(node);
      if (initialLeaves != null) {
        for (Integer leaf : finalLeaves) {
          if (!initialLeaves.contains(leaf)) {
            sb.append("ADD(" + node.toString() + "," + leaf.toString() + "), ");
          }
        }
      }
    }
     // deletes the final ", " from the string builder
    int len = sb.length();
    if (len > 0) {
      sb.deleteCharAt(len - 2);
    }
    return sb.toString();
  }

  // Recurses through the leaves of the node to be removed
  public static void removeNode(HashMap<Integer, ArrayList<Integer>> map, Integer node, StringBuilder sb) {
    if (map.containsKey(node)) {
      ArrayList<Integer> nodes = map.get(node);
      for (Integer n : nodes) {
        removeNode(map, n, sb);
      }
    }
    map.remove(node);
    sb.append("REMOVE(" + node.toString() + "), ");
  }


}
