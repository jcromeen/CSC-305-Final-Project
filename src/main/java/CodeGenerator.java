import java.util.List;

class CodeGenerator {
    /**
     * Generates Java code for a given class name based on its relationships,
     * decorations, and additional properties.
     *
     * @param boxName        The name of the box to generate code for.
     * @param squares        List of given Square objects (representing classes).
     * @param umlConnections List of UML connections representing relationships between classes.
     * @return A string containing the generated Java code.
     */
    public String generateCode(String boxName, List<Square> squares, List<UMLConnection> umlConnections) {
        // Find the Square object corresponding to the specified boxName.
        Square selectedBox = squares.stream()
                .filter(box -> box.getName().equals(boxName))
                .findFirst()
                .orElse(null);

        if (selectedBox == null) {
            return "No code available for the selected file.";
        }

        // Initialize the code generation components.
        StringBuilder code = new StringBuilder();
        String classDeclaration = "public class " + boxName;
        String inheritancePart = "";
        String realizationPart = "";

        // Generate class relationships (inheritance or realization)
        for (UMLConnection connection : selectedBox.getConnections(umlConnections)) {
            switch (connection.getConnectionType()) {
                case "Inheritance":
                    inheritancePart = " extends " + connection.getEndSquare().getName();
                    break;
                case "Realization":
                    realizationPart = " implements " + connection.getEndSquare().getName();
                    break;
            }
        }

        classDeclaration += inheritancePart + realizationPart + " {\n\n";
        code.append(classDeclaration);

        // // Add code for other relationships
        for (String decoration : selectedBox.getDecorations()) {
            code.append("    // Applied Decoration: ").append(decoration).append("\n");
            switch (decoration) {
                case "Singleton":
                    code.append("    private static ").append(boxName).append(" instance;\n\n");
                    code.append("    public static ").append(boxName).append(" getInstance() {\n");
                    code.append("        if (instance == null) {\n");
                    code.append("            instance = new ").append(boxName).append("();\n");
                    code.append("        }\n");
                    code.append("        return instance;\n");
                    code.append("    }\n\n");
                    break;
                case "Decorator":
                    code.append("    private ").append(boxName).append(" component;\n\n");
                    code.append("    public ").append(boxName).append("(").append(boxName).append(" component) {\n");
                    code.append("        this.component = component;\n");
                    code.append("    }\n\n");
                    break;
                case "Observer":
                    code.append("    private List<Observer> observers = new ArrayList<>();\n\n");
                    code.append("    public void addObserver(Observer observer) {\n");
                    code.append("        observers.add(observer);\n");
                    code.append("    }\n\n");
                    code.append("    public void notifyObservers() {\n");
                    code.append("        for (Observer observer : observers) {\n");
                    code.append("            observer.update();\n");
                    code.append("        }\n");
                    code.append("    }\n\n");
                    break;
                case "Observable":
                    code.append("    private List<Observer> observers = new ArrayList<>();\n\n");
                    code.append("    public void addObserver(Observer observer) {\n");
                    code.append("        observers.add(observer);\n");
                    code.append("    }\n\n");
                    code.append("    public void notifyObservers() {\n");
                    code.append("        for (Observer observer : observers) {\n");
                    code.append("            observer.update();\n");
                    code.append("        }\n");
                    code.append("    }\n\n");
                    break;
                case "Decorateable":
                    code.append("    public void decorate() {\n");
                    code.append("        // Add decoration logic here\n");
                    code.append("        System.out.println(\"Decorating ").append(boxName).append("\");\n");
                    code.append("    }\n\n");
                    break;
                case "Chain Member":
                    code.append("    private ChainMember nextMember;\n\n");
                    code.append("    public void setNext(ChainMember next) {\n");
                    code.append("        this.nextMember = next;\n");
                    code.append("    }\n\n");
                    code.append("    public void handleRequest(Request request) {\n");
                    code.append("        if (nextMember != null) {\n");
                    code.append("            nextMember.handleRequest(request);\n");
                    code.append("        }\n");
                    code.append("    }\n\n");
                    break;
                case "Strategy":
                    code.append("    private Strategy strategy;\n\n");
                    code.append("    public void setStrategy(Strategy strategy) {\n");
                    code.append("        this.strategy = strategy;\n");
                    code.append("    }\n\n");
                    code.append("    public void executeStrategy() {\n");
                    code.append("        strategy.execute();\n");
                    code.append("    }\n\n");
                    break;
                case "Factory":
                    code.append("    public static ").append(boxName).append(" createInstance() {\n");
                    code.append("        // Add instance creation logic here\n");
                    code.append("        return new ").append(boxName).append("();\n");
                    code.append("    }\n\n");
                    break;
                case "Product":
                    code.append("    public void displayProductDetails() {\n");
                    code.append("        // Display product details logic here\n");
                    code.append("        System.out.println(\"Product details for ").append(boxName).append("\");\n");
                    code.append("    }\n\n");
                    break;
            }
        }

        code.append("    public ").append(boxName).append("() {\n");
        code.append("        // Initialization code\n");
        code.append("    }\n\n");

        code.append("}\n");

        return code.toString();
    }
}
