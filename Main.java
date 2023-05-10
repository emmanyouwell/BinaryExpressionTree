import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/* Node class */
class Node {
	char data;
	Node left, right;

	public Node(char data) {
		this.data = data;
		left = right = null;
	}
}

/* Expression tree class */
class ExpressionTree {
	Node root; // Node instance
	public int level = 0; // Levels of binary tree
	public ArrayList<Character> leaves; // Leaves of binary tree to allow changes to the leaves
	public String given; // Given expression

	public ExpressionTree() {
		// Constructor
		root = null;
		leaves = new ArrayList<>();
	}

	public int getLevel() {
		// Gets the attribute level of this class
		return this.level;
	}

	public void constructTree(String infix) {
		// Creates the first binary tree from the given infix
		Stack<Node> st = new Stack<>();
		Stack<Character> opStack = new Stack<>();

		for (int i = 0; i < infix.length(); i++) {
			char ch = infix.charAt(i);

			if (Character.isLetterOrDigit(ch)) {
				Node t = new Node(ch);
				st.push(t);
			} else if (ch == '(') {
				opStack.push(ch);
			} else if (ch == ')') {
				while (!opStack.isEmpty() && opStack.peek() != '(') {
					char op = opStack.pop();
					Node t = new Node(op);
					t.right = st.pop();
					t.left = st.pop();
					st.push(t);
				}

				opStack.pop();
			} else {
				while (!opStack.isEmpty() && precedence(ch) <= precedence(opStack.peek())) {
					char op = opStack.pop();
					Node t = new Node(op);
					t.right = st.pop();
					t.left = st.pop();
					st.push(t);
				}

				opStack.push(ch);
			}
		}

		while (!opStack.isEmpty()) {
			char op = opStack.pop();
			Node t = new Node(op);
			t.right = st.pop();
			t.left = st.pop();
			st.push(t);
		}

		root = st.peek();
	}

	public int precedence(char ch) {
		// Checks for the precedence of the Operators and Operands
		switch (ch) {
		case '+':
		case '-':
			return 1;
		case '*':
		case '/':
			return 2;
		}

		return -1;
	}

	public void printTree(Node node, int level) {
		// Recursive method for printing the Binary tree

		if (node == null) {

			return;

		}

		printTree(node.right, level + 1);

		for (int i = 0; i < level; i++) {

			System.out.print("  ");

		}

		System.out.println(node.data);

		printTree(node.left, level + 1);

	}

	public void printTree() {
		//Call method for the recursive printTree in the main method
		setLevel(root, 0);
		for (int i = 0; i <= getLevel(); i++) {
			if (i == 0) {
				System.out.print("R ");
			} else {
				System.out.print(i + " ");
			}

		}
		System.out.println("\n");
		printTree(root, 0);
	}

	public void setLevel(Node node, int level) {
		//Gets the maximum number of levels in the tree
		if (node == null) {

			return;

		}

		setLevel(node.right, level + 1);

		this.level = level;

		setLevel(node.left, level + 1);
	}

	public void printInorder(Node t) {
		//Recursive method to print the nodes left to right
		if (t != null) {
			printInorder(t.left);
			System.out.print(t.data + " ");
			printInorder(t.right);
		}
	}

	public void printInorder() {
		//Call method for the recursive printInorder in the main method
		printInorder(root);
	}

	public int evaluate(Node node) {
		//Computes for the resulting integer of the expression
		if (node == null) {
			return 0;
		}

		if (node.left == null && node.right == null) {
			return Character.getNumericValue(node.data);
		}

		int left = evaluate(node.left);
		int right = evaluate(node.right);

		switch (node.data) {
		case '+':
			return left + right;
		case '-':
			return left - right;
		case '*':
			return left * right;
		case '/':
			return left / right;
		}

		return 0;
	}

	public void printEval() {
		//Prints evaluation
		System.out.println("The value associated with the root is : " + evaluate(root));
	}

	public String infixToPostfix(String infix) {
		//Converts infix to postfix
		Stack<Character> stack = new Stack<>();
		StringBuilder postfix = new StringBuilder();
		for (char c : infix.toCharArray()) {
			if (Character.isDigit(c)) {
				postfix.append(c);
			} else if (c == '(') {
				stack.push(c);
			} else if (c == ')') {
				while (!stack.isEmpty() && stack.peek() != '(') {
					postfix.append(stack.pop());
				}
				stack.pop();
			} else {
				while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
					postfix.append(stack.pop());
				}
				stack.push(c);
			}
		}
		while (!stack.isEmpty()) {
			postfix.append(stack.pop());
		}
		return postfix.toString();
	}

	public String infixToPrefix(String infix) {
		//converts infix to prefix
		Stack<Character> stack = new Stack<>();
		StringBuilder prefix = new StringBuilder();
		infix = new StringBuilder(infix).reverse().toString();
		for (char c : infix.toCharArray()) {
			if (Character.isDigit(c)) {
				prefix.append(c);
			} else if (c == ')') {
				stack.push(c);
			} else if (c == '(') {
				while (!stack.isEmpty() && stack.peek() != ')') {
					prefix.append(stack.pop());
				}
				stack.pop();
			} else {
				while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
					prefix.append(stack.pop());
				}
				stack.push(c);
			}
		}
		while (!stack.isEmpty()) {
			prefix.append(stack.pop());
		}
		return prefix.reverse().toString();
	}

	public void isLeaf(Node node) {
		//Recursive method that adds all the leaf nodes to the ArrayList
		if (node != null) {
			if (node.right == null && node.left == null) {
				leaves.add(node.data);
			}
			isLeaf(node.left);
			isLeaf(node.right);
		}

	}

	public void checkLeaf() {
		//Call method for the recursive isLeaf in the main method
		leaves.clear();
		isLeaf(root);
	}

	public void setGiven(String infix) {
		//Sets the given attribute to user input
		this.given = infix;
	}

	public void updateInfix(char key, char value) {
		/*When changing leaf nodes this will also 
		 * change the previous expression to the 
		 * updated one
		 */
		char arr[] = this.given.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == key) {
				arr[i] = value;
				break;
			}
		}
		StringBuilder builder = new StringBuilder();
		for (Character ch : arr) {
			builder.append(ch);
		}
		this.given = builder.toString();
	}

	public ArrayList<Character> getArray() {
		//Returns the array of leaves in the tree
		return leaves;
	}

	public void update(Node root, char key, char value) {
		//Recursive method to update the leaf node of the user's choice
		if (root != null) {
			if (root.left == null && root.right == null && root.data == key) {
				updateInfix(root.data, value);
				root.data = value;
				return;
			}
			update(root.left, key, value);
			update(root.right, key, value);

		}
	}

	public void updateNode(char value, int index) {
		//Call method for the recursive update method in the main method
		update(root, leaves.get(index), value);
	}
}

//Main class
public class Main { 
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String infix = "";
		System.out.print("Enter expression: ");
		infix = sc.next();
		ExpressionTree tree = new ExpressionTree();
		tree.setGiven(infix);
		tree.constructTree(infix);
		char value;
		int index = 0;
		boolean change = true;
		while (change) {
			tree.printTree();

			System.out.println();
			tree.printEval();

			System.out.println("Postfix: " + tree.infixToPostfix(tree.given));
			System.out.println("Prefix: " + tree.infixToPrefix(tree.given));
//	        tree.printInorder();
			tree.checkLeaf();

			System.out.println("Do you want to change leaf nodes? [Y/N]");
			char choice = Character.toUpperCase(sc.next().charAt(0));

			if (choice == 'Y') {
				System.out.println("Leaf nodes: " + tree.getArray());
				System.out.print("Enter new value: ");
				value = sc.next().charAt(0);
				System.out.print("Choose index to change: ");
				index = sc.nextInt();

				tree.updateNode(value, index);
			} else {
				System.out.println("Exiting program...");
				change = false;
			}

		}

	}
}
