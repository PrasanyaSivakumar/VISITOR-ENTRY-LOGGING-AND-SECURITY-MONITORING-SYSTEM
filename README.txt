VISITOR ENTRY LOGGING AND SECURITY MONITORING SYSTEM
Java Swing Working Model

REQUIREMENTS
- JDK 8 or later
- Windows, macOS, or Linux
- VS Code (with Java extensions), IntelliJ IDEA, Eclipse, or a terminal

HOW TO RUN IN VS CODE
1. Install a JDK if one is not installed.
2. Open VS Code.
3. Choose File > Open Folder and select the VisitorEntryWorkingModel folder.
4. Open VisitorEntrySystem.java.
5. If prompted, install the Java Extension Pack.
6. Click Run above the main method, or open Terminal > New Terminal.
7. In the terminal, run:
      javac VisitorEntrySystem.java
      java VisitorEntrySystem

HOW TO USE
1. Enter a visitor name.
2. Enter the purpose of the visit.
3. Click Record Entry.
4. The current date and time appear in the table.
5. Use Clear Fields to clear the input boxes.
6. Select a table row and click Delete Selected Entry to remove it.
7. Click Export CSV to save a report.

DATA STORAGE
The program saves records to visitor_entries.csv in the current working directory, so entries normally remain available after closing and reopening the program. Keep the CSV file with the project. This is a classroom prototype, not a hardened security system. For real deployment, add authentication, access permissions, backups, and appropriate privacy controls.

NOTE
The CSV storage is intended for a small demonstration. Do not store sensitive personal information in an unsecured file.
