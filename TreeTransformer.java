import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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

    // copies iterated over to avoid Concurrent Modification
    for (Integer node : nodesCopy) {
      ArrayList<Integer> initialLeaves = tree1.get(node);
      ArrayList<Integer> finalLeaves = tree2.get(node);
      if (finalLeaves != null && initialLeaves != null) {
        for (Integer leaf : new ArrayList<>(initialLeaves)) {
          if (!finalLeaves.contains(leaf)) {
            removeNode(tree1, leaf, sb);
          }
        }
      }
    }

    // Adds the ADDs if the nodes/leaves are not already in the tree
    for (Integer node : finalNodesCopy) {
      ArrayList<Integer> initialLeaves = tree1.get(node);
      ArrayList<Integer> finalLeaves = tree2.get(node);
      for (Integer leaf : finalLeaves) {
        if (initialLeaves == null || !initialLeaves.contains(leaf)) {
          sb.append("ADD(" + node.toString() + "," + leaf.toString() + "), ");
        }
      }
    }
    int len = sb.length();
    if (len > 0) {
      sb.deleteCharAt(len - 2);
    }
    return sb.toString();
  }

  public static void removeNode(HashMap<Integer, ArrayList<Integer>> map, Integer node, StringBuilder sb) {
    if (map.containsKey(node)) {
      ArrayList<Integer> nodes = new ArrayList<>(map.get(node)); // create a copy of the list
      for (Integer n : nodes) {
        removeNode(map, n, sb);
      }
    }
    sb.append("REMOVE(" + node.toString() + "), ");
    map.remove(node);
    // remove the node from all values in the map
    for (ArrayList<Integer> leaves : map.values()) {
      leaves.remove(node);
    }
  }




}
