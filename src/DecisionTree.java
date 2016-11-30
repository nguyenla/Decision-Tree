import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A DecisionTree object represents a decision tree, as described in, for
 * example, the book "Artificial Intelligence" by Russell and Norvig (3rd
 * edition). A DecisionTree is constructed using a set of training examples, and
 * is capable of deciding the class of a novel example. Because decision trees
 * are recursive data structures, any given DecisionTree object could be a node
 * in a larger decision tree, referred to as the <i>full decision tree</i> in
 * the documentation below.
 * 
 * @author jmac
 */
public abstract class DecisionTree {

	/**
	 * The label assigned to the root node of a decision tree.
	 */
	public static final String ROOT_LABEL = "root";

	// The label on the edge leading to this DecisionTree node.
	// This corresponds to one of the possible values of the attribute on which
	// the parent node was split. (See figure 18.6 of Russell and Norvig for an
	// example.) Exception: the label on the root node of a DecisionTree is
	// the constant ROOT_LABEL.
	private String edgeLabel;

	// The depth of this object in the full decision tree, with the
	// root node having depth 0 by convention.
	protected int depth;

	/**
	 * Construct a decision tree according to the recursive algorithm given in
	 * figure 18.5 of Russell and Norvig (third edition).
	 * 
	 * @param examples
	 *            The examples from which this tree should be learned.
	 * @param attributes
	 *            A list of attributes on which this tree is permitted to make
	 *            decisions.
	 * @param parentExamples
	 *            The examples from which the parent node of this DecisionTree
	 *            object were learned. to construct the root node,
	 *            <code>parentExamples</code> should be null.
	 * @param label
	 *            The label on the edge leading to this DecisionTree node, or
	 *            <code>DecisionTree.ROOT_LABEL</code> for the root.
	 * @param depth
	 *            The depth of this node in the full decision tree.
	 * @return The constructed DecisionTree.
	 * @throws DecisionTreeException
	 */
	public static DecisionTree constructDecisionTree(InstanceSet examples,
			ArrayList<Attribute> attributes, InstanceSet parentExamples,
			String label, int depth) throws DecisionTreeException {
		// The algorithm closely mimics figure 18.5 of Russell and Norvig.
		if (examples.getNumInstances() == 0) {
			// TODO: fix the following line.
			// HINT: it should begin "return new ..."
			return new DecisionTreeLeaf(parentExamples, label, depth + 1);
		} 
		else if (isPure(examples) || attributes.size() == 0) {
			// TODO: fix the following line.
			// HINT: it should begin "return new ..."
			return new DecisionTreeLeaf(examples, label, depth + 1);
		} else {
			// TODO: fix the following line.
			// HINT: it should begin "return new ..."
			// Need review
			return new DecisionTreeInternal(examples, attributes, label, depth + 1);
		}
	}

	// NEED REVIEW
	// Return true if the given set of instances is pure, and false otherwise.
	private static boolean isPure(InstanceSet instances) {
		// TODO: fill in the body of this method and fix the return statement
		boolean check = true;
		int classIndex = instances.getAttributeSet().getClassAttributeIndex();
		String attr = null;
		for (Instance in: instances.getInstances()) {
			if (attr == null) {
				attr =  in.getValues()[classIndex];
			}
			else {
				String temp = in.getValues()[classIndex];
				if (!temp.equals(attr)) {
					check = false;
				}
			}
		}
		return check;
	}

	/**
	 * Remove the attribute that defines an instance's classification, and
	 * return the result in a new list
	 * 
	 * @param attributeSet
	 *            The original set of attributes from which the list
	 *            <code>attributes</code> was drawn (this will be used to define
	 *            which attribute is the class attribute -- the one to be
	 *            removed).
	 * @param attributes
	 *            A list of attributes which is a subset of the attributes in
	 *            <code>attributeSet</code>. This list will be left undisturbed.
	 * @return A new list, which is the same as <code>attributes</code>, but
	 *         with the classification attribute removed.
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<Attribute> removeClassAttribute(
			AttributeSet attributeSet, ArrayList<Attribute> attributes) {
		Attribute classAttribute = attributeSet.getClassAttribute();
		ArrayList<Attribute> newAttributes = (ArrayList<Attribute>) attributes
				.clone();
		newAttributes.remove(classAttribute);
		return newAttributes;
	}

	/**
	 * This protected constructor cannot be called by external code; decision
	 * trees should be constructed using the constructDecisionTree factory
	 * method.
	 * 
	 * @param label
	 *            The label on the edge leading to this DecisionTree node, or
	 *            <code>DecisionTree.ROOT_LABEL</code> for the root.
	 * @param depth
	 *            The depth of this node in the full decision tree.
	 */
	protected DecisionTree(String label, int depth) {
		this.edgeLabel = label;
		this.depth = depth;
	}

	/**
	 * Return the decision tree's decision for the given instance: that is, the
	 * classification that should be assigned to the instance.
	 * 
	 * @param attributes
	 *            The set of attributes employed by the instance.
	 * @param instance
	 *            The instance to be classified.
	 * @return The classification of the given instance.
	 */
	public abstract String decide(AttributeSet attributes, Instance instance);

	/**
	 * Print out the DecisionTree in a human-readable form
	 */
	public void print() {
		// indent this node according to its depth in the full decision tree
		for (int i = 0; i < depth; i++) {
			System.out.print("    ");
		}
		System.out.print("---" + edgeLabel + "---");
	}

	/**
	 * Compute the error rate of the decision tree on the given test set.
	 * 
	 * @param testSet
	 *            A set of examples on which the error rate will be computed.
	 * @return The error rate of the decision tree on the given test set.
	 */
	public double computeErrorRate(InstanceSet testSet) {
		int num_errors = 0;
		AttributeSet attributes = testSet.getAttributeSet();
		int classAttributeIndex = attributes.getClassAttributeIndex();
		for (Instance instance : testSet.getInstances()) {
			String decision = decide(attributes, instance);
			String classification = instance.getValues()[classAttributeIndex];
			if (!decision.equals(classification))
				num_errors++;
		}
		return (double) num_errors / testSet.getNumInstances();
	}

	/**
	 * Print out the decision of this decision tree on every instance in the
	 * given test set.
	 * 
	 * @param testSet
	 *            The set of instances whose decisions will be printed.
	 */
	public void printDecisions(InstanceSet testSet) {
		AttributeSet attributes = testSet.getAttributeSet();
		for (Instance instance : testSet.getInstances()) {
			String decision = decide(attributes, instance);
			System.out.print("instance: ");
			instance.print();
			System.out.println("decision: " + decision);
			System.out.println();
		}
	}

	/**
	 * Constructs a decision tree from the data in a .arff file, prints out the
	 * tree, the error rate on the training set, and the decisions on each
	 * instance in the training set.
	 * 
	 * @param arguments
	 *            Requires a single command line argument, what should be the
	 *            name of a data file in .arff format.
	 * @throws DecisionTreeException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main(String[] arguments) throws DecisionTreeException,
			FileNotFoundException, IOException {
		if (arguments.length != 1) {
			System.out.println("usage: java DecisionTree datafilename");
			System.exit(0);
		}

		// read the training set from disk
		String inputFilename = arguments[0];
		InstanceSet trainingSet = new InstanceSet(inputFilename);

		// Construct the list of attributes that will be used by the decision
		// tree, but make sure to remove the class attribute, which obviously
		// should not be used for classification!
		AttributeSet attributeSet = trainingSet.getAttributeSet();
		ArrayList<Attribute> attributes = attributeSet.getAttributes();
		attributes = removeClassAttribute(attributeSet, attributes);

		// Construct the decision tree itself
		DecisionTree decisionTree = DecisionTree.constructDecisionTree(
				trainingSet, attributes, null, DecisionTree.ROOT_LABEL, 0);

		// print the tree, its error rate on the training set, and its decisions
		// on the training set
		decisionTree.print();
		double error_rate = decisionTree.computeErrorRate(trainingSet);
		System.out.println();
		System.out.println("Error rate on training set: " + error_rate);
		System.out.println();
		decisionTree.printDecisions(trainingSet);
	}

}
