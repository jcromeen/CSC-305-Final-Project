# CSC 305 Final Project

## Overview
This application is a Java-based tool designed for creating and managing UML-like diagrams and generating corresponding Java code. It includes features for creating, editing, and connecting boxes (squares), adding decorations (design patterns), and generating Java source files.

This project demonstrates the use of object-oriented programming principles and UML-inspired connections.

---

## Project Instructions
1. **Draw Area**:
   - Left-click to create a square.
   - Click a square to rename it.
   - Drag squares to reposition.
2. **Decoration Popup**:
   - Right-click a square to add/remove decorations.
   - Drag between decorations to create connections.
3. **Box Connections**:
   - Select a connection type from the "Box Connector" menu.
   - Click two squares to connect them.
4. **Code Tab**:
   - View generated code for each square in the "Code" tab.
   - Java files appear in a `src` directory structure.
5. **File Options**:
   - Save or load diagrams and their corresponding code.

---

## Features

### 1. Draw Area
- **Create Squares**: Left-click anywhere in the draw area to create a square with a default arbitrary name.
- **Rename Squares**: Left-click on a square to open a dialog for renaming.
- **Drag and Drop**: Move squares by dragging them with the mouse.

### 2. Decorations
- **Decoration Menu**: Right-click on a square to open a popup menu of checkboxes for adding decorations.
- **Available Decorations**:
  - Observer
  - Observable
  - Singleton
  - Decoration
  - Decorateable
  - Chain Member
  - Strategy
  - Factory
  - Product
- **Multiple Decorations**: Apply several decorations to a square. Each decoration is visually displayed above the square.

### 3. Connections
- **Decoration Connections**: Connect decorations between squares by dragging from one decoration to another.
- **Box Connections**: Connect squares using UML-inspired relationships:
  - Aggregation
  - Composition
  - Association
  - Inheritance
  - Realization
- **Box Connector Menu**: Use the "Box Connector" dropdown in the menu bar to select a connection type. Click one square, then another, to establish a connection.

### 4. Code Tab
- **Tabbed Interface**: Switch between the Draw Area and Code tab using the tabbed menu.
- **Src Directory**: The left panel displays a directory structure of Java files (one file per square).
- **Code Generation**: The right panel displays generated Java code based on the decorations and UML connections applied to each square.

### 5. File Menu
- **New**: Clear the current diagram and start fresh.
- **Open**: Load a previously saved diagram and code.
- **Save As**: Save the current diagram as an SVG and the corresponding Java files in a `src` folder, bundled into a `.zip` file.
- **Save**: Save the current diagram and code to the previously chosen file.

### 6. Help Menu
- **About**: Displays information about the project and its author.

---

## How to Run
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/jcromeen/CSC-305-Final-Project.git
   cd CSC-305-Final-Project
   ```

2. **Compile the Project**:
   Ensure you have a Java Development Kit (JDK) installed.
   ```bash
   javac *.java
   ```

3. **Run the Application**:
   ```bash
   java Main
   ```

---

## Dependencies
- **Java 8 or later**: Ensure you have a compatible JDK installed.

---

## Author
- **Name**: Jimmy Cromeenes
- **Year**: 2024

---

## Known Bugs/Incomplete Implementations
- **Decoration Connections**: The first time you try to connect two decorations, the box will snap into your cursor. any other attempts work fine.
- **Save File**: Not currently accounting for the last saved file (when passed into Save As)
- **Association Arrowhead**: The arrowhead for Association is not correct.
- **Refactor SquareCreatorFrame**: File handling should be done in some separate files.
- **Connections When Moving**: Decorator connections do not move when the boxes are moved; UML connections do.
- **Open File**: Unable to finish saving, so couldn't start open. 

For any questions or issues, please contact the author via the GitHub repository.

