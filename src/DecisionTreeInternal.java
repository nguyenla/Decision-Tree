import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents an internal node in a decision tree.
 * 
 * @author jmac
 */
public class DecisionTreeInternal extends DecisionTree {

	// A map consisting of the children of this internal node in the decision
	// tree. The key is a possible value of this node's split attribute, and the
	// corresponding value is a DecisionTree for classifying instances that
	// agree with the key. For example, if this node's split attribute is
	// "color", and the possible values of the attribute include "red", then the
	// key "red" maps to a DecisionTree for instances whose "color" is "red".
	HashMap<String, DecisionTree> children;

	// The attribute on which this internal node splits its instances. In the
	// conventional way of drawing decision trees, the node would also be
	// labeled with this attribute. See figure 18.6 of Russell and Norvig for an
	// example.
	Attribute splitAttribute;

	/**
	 * This protected constructor cannot be called by external code; decision
	 * trees should be constructed using the constructDecisionTree factory
	 * method in the DecisionTree class.
	 * 
	 * @param examples
	 *            The examples from which this tree should be learned.
	 * @param attributes
	 *            A list of attributes on which this tree is permitted to make
	 *            decisions.
	 * @param label
	 *            The label on the edge leading to this DecisionTree node, or
	 *            <code>DecisionTree.ROOT_LABEL</code> for the root.
	 * @param depth
	 *            The depth of this node in the full decision tree.
	 * @throws DecisionTreeException 
	 */
	@SuppressWarnings("unchecked")
	protected DecisionTreeInternal(InstanceSet examples,
			ArrayList<Attribute> attributes, String label, int depth) throws DecisionTreeException {
		super(label, depth);
		assert attributes.size() > 0;

		// compute and store the split attribute
		splitAttribute = getSplitAttribute(examples, attributes);

		// Make a list of valid attributes for child nodes, which consists of
		// all the valid attributes for this node except the split attribute.
		ArrayList<Attribute> childAttributes = (ArrayList<Attribute>) attributes
				.clone();
		childAttributes.remove(splitAttribute);

		// compute the children of this node, using recursion
		children = makeChildren(examples, childAttributes);
	}

	/**
	 * Compute the attribute on which this internal node will split its
	 * instances, using the criterion of maximum information gain.
	 * 
	 * @param examples
	 *            A set of instances that will be used to determine the split
	 *            attribute.
	 * @param attributes
	 *            A list of attributes that are valid candidates for the split
	 *            attribute.
	 * @return The chosen split attribute.
	 * @throws DecisionTreeException 
	 */
	private Attribute getSplitAttribute(InstanceSet examples,
			ArrayList<Attribute> attributes) throws DecisionTreeException {
		// TODO: fill in the body of this method and fix the return statement
		// HINT: the expectedEntropy() method will be needed
		return null;
	}

	/**
	 * Compute the matching instances from a given set of instances, where a
	 * match is determined by having a given value on a given attribute. For
	 * example, we might return all instances that have the value "red" on the
	 * attribute "color".
	 * 
	 * @param attribute
	 *            The attribute to which matching will apply (e.g. "color").
	 * @param value
	 *            The value of the attribute which is considered a match (e.g.
	 *            "red").
	 * @param examples
	 *            The set of instances from which matches will be found.
	 * @return A set of all matching instances.
	 */
	private InstanceSet getMatches(Attribute attribute, String value, InstanceSet examples) {
		ArrayList<Instance> matches = new ArrayList<Instance>();

		// find out the index of the attribute to be matched
		AttributeSet attributes = examples.getAttributeSet();
		int attributeIndex = attributes.getAttributeIndex(attribute);

		// Loop through the examples, looking for matching instances and adding
		// them to our list of matches
		for (Instance instance : examples.getInstances()) {
			String instanceValue = instance.getValues()[attributeIndex];
			if (instanceValue.equals(value)) {
				matches.add(instance);
			}
		}

		return new InstanceSet(examples.getAttributeSet(), matches);
	}

	/**
	 * Create and compute the children of this node.
	 * 
	 * @param examples
	 *            A list of all training examples provided to this node
	 * @param attributes
	 *            A list of attributes valid for children of this node
	 * @return A map consisting of the children of this internal node in the
	 *         decision tree. The key is a possible value of this node's split
	 *         attribute, and the corresponding value is a DecisionTree for
	 *         classifying instances that agree with the key. For example, if
	 *         this node's split attribute is "color", and the possible values
	 *         of the attribute include "red", then the key "red" maps to a
	 *         DecisionTree for instances whose "color" is "red".
	 * @throws DecisionTreeException 
	 */
	private HashMap<String, DecisionTree> makeChildren(InstanceSet examples,
			ArrayList<Attribute> attributes) throws DecisionTreeException {
		// TODO: fill in the body of this method and fix the return statement
		// HINT: the getMatches() method will be useful
		return null;
	}


	/**
	 * Compute the expected entropy of the given attribute, based on the given
	 * examples.
	 * 
	 * @param attribute
	 *            The attribute whose expected entropy will be computed.
	 * @param examples
	 *            The examples used to compute the expected entropy of the
	 *            attribute.
	 * @return The expected entropy of the given attribute.
	 * @throws DecisionTreeException 
	 */
	private double expectedEntropy(Attribute attribute, InstanceSet examples) throws DecisionTreeException {
		// TODO: fill in the body of this method and fix the return statement
		// HINT: use the Distribution class
		
		Attribute classAttribute = examples.getAttributeSet().getClassAttribute();
		int classAttributeIndex = examples.getAttributeSet().getAttributeIndex(classAttribute);
		int curAttributeIndex = examples.getAttributeSet().getAttributeIndex(attribute);
		HashMap<String, Distribution> distArray = new HashMap<String, Distribution>();
		
		// Create one Distribution object for each value of the given attribute
		// Initialize each Distribution object
		for (String val: attribute.getValues()) {
			Distribution dist = new Distribution(classAttribute);
			distArray.put(val, dist);
		}
		
		// Incrementing the frequency of the corresponding Distribution objects
		for (Instance ex: examples.getInstances()) {
			String classValue = ex.getValues()[classAttributeIndex];
			String curValue = ex.getValues()[curAttributeIndex];
			distArray.get(curValue).incrementFrequency(classValue);
		}
		
		// Compute probabilities for each Distribution object
		for (String key: distArray.keySet()) {
			distArray.get(key).computeProbabilitiesFromFrequencies();
//			dist.printFrequencies(); // for debugging
		}
		
		double totalEntropy = 0.00;
		int totalFrequency = 0;
		
		for (String key: distArray.keySet()) {
			Distribution temp = distArray.get(key);
			totalFrequency += temp.getTotalFrequencies();
			totalEntropy += temp.getEntropy() * temp.getTotalFrequencies();
		}
		
		return totalEntropy / totalFrequency;
	}

	/* (non-Javadoc)
	 * @see DecisionTree#decide(AttributeSet, Instance)
	 */
	@Override
	public String decide(AttributeSet attributes, Instance instance) {
		// TODO: fill in the body of this method and fix the return statement
		// HINT: use the Distribution class
		return null;
	}

	/* (non-Javadoc)
	 * @see DecisionTree#print()
	 */
	@Override
	public void print() {
		super.print();
		System.out.println("[attribute " + splitAttribute.getName() + "]");
		for (DecisionTree child : children.values()) {
			child.print();
		}
	}
	
	// This main method is created solely to test the expectedEntropy method.
	// To run the test, make the expectedEntropy method static first
	public static void main(String[] args) throws DecisionTreeException, IOException {
		String[] values = {"rough", "smooth"}; 
		Attribute attr = new Attribute("texture", values);
		InstanceSet trainingSet = new InstanceSet("test.arff");
		// System.out.println("Expected entropy is: " + expectedEntropy(attr, trainingSet));
	}
}
