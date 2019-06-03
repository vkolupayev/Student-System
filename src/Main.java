import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.control.CheckBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application implements UIFunkcijos{
	
	
    private final String studentFile = "E:\\Studies\\UNI\\Uni 2 semestras\\OOP\\3labDarbas\\src\\data\\students.txt"; //reikia pakeisti i reikiama direktorija
    private final String LankFile = "E:\\Studies\\UNI\\Uni 2 semestras\\OOP\\3labDarbas\\src\\data\\attendance.txt"; //reikia pakeisti i reikiama direktorija
    private int grupesNr = 1;
    private String grupesNrPavadinimas = "group 1";
    private int naujaGrupe = 0;

    private TextField vardTextField = new TextField();
    private TextField pavardTextField = new TextField();
    private Button naujStudButton = new Button("Naujas studentas");
    private Button delStudButton = new Button("Istrinti studenta");
    
    private TextField keistVardTextField = new TextField();
    private TextField keistPavardTextField = new TextField();
    private Button keistStudButton = new Button("Keisti studenta");

    private ChoiceBox grupesChoiceBox = new ChoiceBox();
    private Button naujGrupButton = new Button("Prideti grupe");
    private Button delGrupButton = new Button("Istrinti grupe");
    private Button keistGrupButton = new Button("#");
    
    private Label lankDateLabel = new Label("Data:");
    private Button lankButton = new Button("Zymeti Lankomuma");
    private  DatePicker lankDataPicker = new DatePicker();
    
    private Label lankPerziurNuoDateLabel = new Label("Nuo:");
    private  Label lankPerziurIkiDateLabel = new Label("Iki:");
    private DatePicker lankPerziurNuoDatePicker = new DatePicker();
    private DatePicker lankPerziurIkiDatePicker = new DatePicker();
    private Button lankPerziurButton = new Button("Grupes lankomumas");
    
    private Button pdfButton = new Button("Sukurti PDF");

    private final String pattern = "yyyy-MM-dd";
    
    private void init(Stage stage) throws Exception{


        StringConverter<LocalDate> dateConvert = new StringConverter<LocalDate>() 
        {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            
            public String toString(LocalDate date) 
            {
                if (date != null) 
                {
                    return dateFormatter.format(date);
                } 
                else 
                {
                    return "";
                }
            }
            public LocalDate fromString(String string)
            {
                if (string != null && !string.isEmpty()) 
                {
                    return LocalDate.parse(string, dateFormatter);
                } 
                else 
                {
                    return null;
                }
            }
        };
        
        lankDataPicker.setConverter(dateConvert);
        lankPerziurNuoDatePicker.setConverter(dateConvert);
        lankPerziurIkiDatePicker.setConverter(dateConvert);
        
        lankDataPicker.setPromptText(pattern.toLowerCase());
        lankPerziurNuoDatePicker.setPromptText(pattern.toLowerCase());
        lankPerziurIkiDatePicker.setPromptText(pattern.toLowerCase());
        
        lankDataPicker.requestFocus();
        lankPerziurNuoDatePicker.requestFocus();
        lankPerziurIkiDatePicker.requestFocus();


        List<Student> list = readFile(grupesChoiceBox, 1, true);
        
        TableView<Student> table = new TableView<Student>();
        
        TableColumn<Student, String> nameColumn = new TableColumn<Student, String>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        nameColumn.setMinWidth(100);

        TableColumn<Student, String> lastNameColumn = new TableColumn<Student, String>("LastName");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
        lastNameColumn.setMinWidth(100);

        ObservableList<Student> obsList = getUserList(list, grupesNr);

        table.setItems(obsList);

        table.getColumns().addAll(nameColumn, lastNameColumn);

        vardTextField.setPromptText("Name");
        pavardTextField.setPromptText("LastName");
        keistVardTextField.setPromptText("Name");
        keistPavardTextField.setPromptText("LastName");

        GridPane gridpane1 = new GridPane();
        GridPane gridpane2 = new GridPane();
        GridPane gridpane3 = new GridPane();
        GridPane allGridPane = new GridPane();

        gridpane1.add(vardTextField, 0, 0);
        gridpane1.add(pavardTextField, 1, 0);
        gridpane1.add(naujStudButton, 2, 0);
        gridpane1.add(delStudButton, 2, 1);
        gridpane1.add(keistVardTextField, 0, 2);
        gridpane1.add(keistPavardTextField, 1, 2);
        gridpane1.add(keistStudButton, 2, 2);
        gridpane1.add(lankDateLabel, 0, 3);
        gridpane1.add(lankDataPicker, 1, 3);
        gridpane1.add(lankButton, 2, 3);
        gridpane1.add(lankPerziurNuoDateLabel, 0, 4);
        gridpane1.add(lankPerziurIkiDateLabel, 1, 4);
        gridpane1.add(lankPerziurNuoDatePicker, 0, 5);
        gridpane1.add(lankPerziurIkiDatePicker, 1, 5);
        gridpane1.add(lankPerziurButton, 2, 5);

        gridpane2.add(grupesChoiceBox, 0, 0);
        gridpane2.add(naujGrupButton, 1, 0);
        gridpane2.add(delGrupButton, 2, 0);
        
        gridpane3.add(pdfButton, 2, 0);

        allGridPane.add(gridpane2, 0, 0);
        allGridPane.add(gridpane3, 1, 0);
        allGridPane.add(gridpane1, 1, 1);
        allGridPane.add(table, 0, 1);

        gridpane1.setHgap(10);
        gridpane1.setVgap(10);
        gridpane1.setPadding(new Insets(10, 10, 10, 10));
        gridpane2.setHgap(5);
        gridpane2.setPadding(new Insets(10, 10, 0, 0));
        gridpane3.setHgap(96);
        gridpane3.setPadding(new Insets(10, 10, 10, 10));
        allGridPane.setPadding(new Insets(10, 10 ,10 ,10));        

        Scene scene = new Scene(allGridPane, 800, 300);
        stage.setScene(scene);
        stage.setTitle("Studentu registracijos sistema");
        stage.show();

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e)
            {
                if(vardTextField.getText()!= null && pavardTextField.getText()!= null)
                {
                    grupesNr = Integer.parseInt(String.valueOf(grupesNrPavadinimas.charAt(6)));

                    addNewStudent(vardTextField.getText(), pavardTextField.getText(), grupesNr);

                    list.add(0, new Student(vardTextField.getText(), pavardTextField.getText(), grupesNr));

                    ObservableList<Student> list3 = getUserList(readFile(grupesChoiceBox, grupesNr, false), grupesNr);

                    table.setItems(list3);
                }
                vardTextField.clear();
                pavardTextField.clear();
            }
        };
        naujStudButton.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        
        EventHandler<MouseEvent> eventHandler2 = new EventHandler<MouseEvent>() {
            
            public void handle(MouseEvent e){
                Student student = table.getSelectionModel().getSelectedItem();
                if(student != null){
                    removeFromList(list, student);
                    deleteStudent(student);
                    ObservableList<Student> list3 = getUserList(readFile(grupesChoiceBox, grupesNr, false), grupesNr);
                    table.setItems(list3);
                }

            }
        };
        delStudButton.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler2);
        
        EventHandler<MouseEvent> eventHandler3 = new EventHandler<MouseEvent>() {
            
            public void handle(MouseEvent e){
                Student student = table.getSelectionModel().getSelectedItem();
                Student student1 = new Student(keistVardTextField.getText(), keistPavardTextField.getText(), grupesNr);//pakeistas


                if(student != null){
                    list.set(getIndex(list, student),student1);
                    editStudent(student, student1);
                    ObservableList<Student> list3 = getUserList(readFile(grupesChoiceBox, grupesNr, false), grupesNr);
                    table.setItems(list3);
                }
                keistVardTextField.clear();
                keistPavardTextField.clear();
            }
        };
        keistStudButton.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler3);

        EventHandler<MouseEvent> eventHandler4 = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e){
                naujaGrupe = addGroup();
                ObservableList<Student> list3 = getUserList(readFile(grupesChoiceBox, naujaGrupe, true), naujaGrupe);
                table.setItems(list3);
            }
        };
        naujGrupButton.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler4);

        EventHandler<MouseEvent> eventHandler5 = new EventHandler<MouseEvent>() {
            
            public void handle(MouseEvent e){
                deleteGroup(grupesNr);
                deleteGroupFromList(list, grupesNr);
                ObservableList<Student> list3 = getUserList(readFile(grupesChoiceBox, 1, true), grupesNr);
                table.setItems(list3);

            }
        };
        delGrupButton.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler5);
        
        EventHandler<MouseEvent> eventHandler6 = new EventHandler<MouseEvent>() {
            
            public void handle(MouseEvent e)
            {
                LocalDate lankData = lankDataPicker.getValue();
                startAttendanceMark(lankData, grupesNr, list);
            }
        };
        lankButton.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler6);

        EventHandler<MouseEvent> eventHandler7 = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e)
            {
                LocalDate perziurNuoData = lankPerziurNuoDatePicker.getValue();
                LocalDate perziurIkiData = lankPerziurIkiDatePicker.getValue();
                if(perziurNuoData.isBefore(perziurIkiData) || perziurNuoData.isEqual(perziurIkiData))
                {
                    showAttendanceReview(perziurNuoData, perziurIkiData, grupesNr, list);
                }
            }
        };
        lankPerziurButton.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler7);

        grupesChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                grupesNrPavadinimas = (String) grupesChoiceBox.getItems().get((Integer) number2);
                grupesNr = Integer.parseInt(String.valueOf(grupesNrPavadinimas.charAt(6)));

                ObservableList<Student> list3 = getUserList(readFile(grupesChoiceBox, grupesNr, false), grupesNr);

                table.setItems(list3);
            }
        });
        
        EventHandler<MouseEvent> eventHandler8 = new EventHandler<MouseEvent>() 
        {
            public void handle(MouseEvent e)
            {
                generatePDF();

            }
        };
        pdfButton.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler8);

    }
    
    public void start(Stage primaryStage) throws Exception
    {
        init(primaryStage);
    }

   //Functions

    private void removeFromList(List<Student> list, Student student)
    {

        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getName().equals(student.getName()) && list.get(i).getLastName().equals(student.getLastName()))
            {
                list.remove(i);
            }
        }
    }

    private void startAttendanceMark(LocalDate date, int grupesNr, List<Student> list)
    {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        Button button = new Button("Enter");
        List<Student> listGroup = new ArrayList<>();
        int b = 1;

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = date.format(pattern);

        gridPane.add(new Text("Date: " + dateString), 0, 0);
        gridPane.add(button, 1, 0);
        gridPane.getColumnConstraints().add(new ColumnConstraints(200));
        gridPane.getColumnConstraints().add(new ColumnConstraints(100));
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getGroup() == grupesNr)
            {
                listGroup.add(list.get(i));
                gridPane.add(new Text(list.get(i).getName() + " " + list.get(i).getLastName()), 0, b);
                gridPane.getRowConstraints().add(new RowConstraints(50));
                b++;
            }
        }
        gridPane.getRowConstraints().add(new RowConstraints(30));

        b--;
        CheckBox[] checkboxes = new CheckBox[b+1];
        for(int i=0;i<b;i++)
        {
            checkboxes[i] = new CheckBox();
        }

        for(int i=1;i<b+1;i++)
        {
            gridPane.add(checkboxes[i-1], 1, i);
        }

        Scene scene = new Scene(gridPane, 300, 100 + 50 * listGroup.size());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Lankomumo zymejimas");
        stage.show();

        EventHandler<MouseEvent> eventHandler9 = new EventHandler<MouseEvent>() 
        {
            public void handle(MouseEvent e)
            {
                markAttendance(listGroup, checkboxes, dateString);
                stage.close();
            }
        };
        button.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler9);
    }

    private void addToLines(List<Student> listGroup, List<String> lines, CheckBox[] checkboxes)
    {
        for (int a = 0; a < listGroup.size(); a++)
        {

            lines.add(listGroup.get(a).getName() + " " + listGroup.get(a).getLastName());
            if (checkboxes[a].isSelected())
            {
                lines.add(" buvo.\n");
            }
            else 
            {
                lines.add(" nebuvo.\n");
            }
        }
    }

    
    public int addGroup()
    {

        List<String> lines = new ArrayList<String>();

        int i=0;
        Charset charset = Charset.forName("US-ASCII");
        Path file = Paths.get(studentFile);
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                if(line.length()==1)
                    i=Integer.parseInt(String.valueOf(line));
                lines.add(line + "\n");

            }
            lines.add(String.valueOf(i+1));
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        Charset charset1 = Charset.forName("US-ASCII");
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset1)) {
            for(int a=0;a<lines.size();a++)
            {
                writer.write(lines.get(a));
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        return i+1;
    }

    private void deleteGroupFromList(List<Student> list, int grupesNr)
    {
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getGroup() == grupesNr)
            {
                list.remove(i);
            }
        }
    }

    private int getIndex(List<Student> listt, Student student)
    {
        for(int i=0;i<listt.size();i++)
        {
            if(listt.get(i).getName() .equals(student.getName()) && listt.get(i).getLastName().equals(student.getLastName()))
            {
                return i;
            }
        }
        return -1;
    }

    private ObservableList<Student> getUserList(List<Student> list, int grupesNr)
    {

        List<Student> list2 = new ArrayList<>();
        
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getGroup() == grupesNr)
            {
                list2.add(list.get(i));
            }
        }
        ObservableList<Student> obsList = FXCollections.observableArrayList(list2);
        return obsList;
    }

    
    public List<Student> readFile(ChoiceBox group, int desiredGroup, boolean changeChoiceBox) 
    {
        List<Student> list = new ArrayList<Student>();

        int i=1;
        Charset charset = Charset.forName("US-ASCII");
        Path file = Paths.get(studentFile);
        
        if(changeChoiceBox)
        {
            group.getItems().clear();
        }

        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                if(line.contains(" "))
                {
                    String[]splited = line.split(" ");
                    list.add(new Student(splited[0], splited[1], i));
                } 
                else if(!line.equals("1"))
                {
                    i++;
                }
                if(line.length() == 1 && changeChoiceBox)
                {
                    group.getItems().add("Group " + line);
                }
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        group.setValue("Group " + Integer.toString(desiredGroup));

        return list;
    }

    
    public void addNewStudent(String name, String lastName, int grupesNr)
    {
        List<String> lines = new ArrayList<String>();
        String newStudent = name + " " + lastName;
        int i=1;
        Charset charset = Charset.forName("US-ASCII");
        Path file = Paths.get(studentFile);
        
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                if(line.contains(" "))
                {
                    lines.add(line + "\n");
                    String[] splited = line.split(" ");
                } 
                else
                {
                    lines.add(line + "\n");
                    if(Integer.parseInt(String.valueOf(line)) == grupesNr)
                    {
                        lines.add(newStudent + "\n");
                    }
                    i++;
                }
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        Charset charset1 = Charset.forName("US-ASCII");
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset1)) {
            for(i=0;i<lines.size();i++)
            {
                writer.write(lines.get(i));
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    
    public void deleteStudent(Student student){
        List<String> lines = new ArrayList<String>();
        String newStudent = student.getName() + " " + student.getLastName();
        int group = student.getGroup();

        int i=1;
        Charset charset = Charset.forName("US-ASCII");
        Path file = Paths.get(studentFile);
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                if(line.contains(" "))
                {
                    if(!line.equals(newStudent))
                    {
                        lines.add(line + "\n");
                    }
                } 
                else
                {
                    lines.add(line + "\n");
                    i++;
                }

            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        Charset charset1 = Charset.forName("US-ASCII");
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset1)) {
            for(i=0;i<lines.size();i++)
            {
                writer.write(lines.get(i));
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    
    public void editStudent(Student student, Student student1)
    {
        List<String> lines = new ArrayList<String>();
        String oldStudent = student.getName() + " " + student.getLastName();
        String newStudent = student1.getName() + " " + student1.getLastName();
        int group = student.getGroup();

        int i=1;
        Charset charset = Charset.forName("US-ASCII");
        Path file = Paths.get(studentFile);
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                if(line.contains(" "))
                {
                    if(line.equals(oldStudent))
                    {
                        lines.add(newStudent + "\n");
                    } 
                    else 
                    {
                        lines.add(line + "\n");
                    }
                } 
                else 
                {
                    lines.add(line + "\n");
                    i++;
                }

            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        Charset charset1 = Charset.forName("US-ASCII");
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset1)) {
            for(i=0;i<lines.size();i++)
            {
                writer.write(lines.get(i));
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    
    public void deleteGroup(int grupesNr)
    {

        List<String> lines = new ArrayList<String>();
        Boolean dontPrintThisGroup=false;

        int i=1;
        Charset charset = Charset.forName("US-ASCII");
        Path file = Paths.get(studentFile);
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null)
            {

                if(line.contains(" "))
                {
                    if(!dontPrintThisGroup)
                        lines.add(line + "\n");

                }
                else
                {
                    if(Integer.parseInt(String.valueOf(line)) != grupesNr)
                    {
                        lines.add(line + "\n");
                        dontPrintThisGroup = false;
                    }
                    else
                    {
                        dontPrintThisGroup = true;
                    }
                }

            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        Charset charset1 = Charset.forName("US-ASCII");
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset1)) {
            for(i=0;i<lines.size();i++)
            {
                writer.write(lines.get(i));
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }

    
    public void markAttendance(List<Student> listGroup, CheckBox[] checkboxes, String dateString)//check
    {
        List<String> lines = new ArrayList<String>();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate newDate = LocalDate.parse(dateString, pattern);
        LocalDate scanDate;

        int i=0;
        Boolean thisGroup = false;
        Boolean newLines = false;
        Boolean naujLankPazym = false;
        Boolean noOldLank = false;
        Charset charset = Charset.forName("US-ASCII");
        Path file = Paths.get(LankFile);
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                if(line.length() == 1)//grupe
                {
                    noOldLank = false;
                    if(Integer.parseInt(String.valueOf(line)) == listGroup.get(0).getGroup())
                    {
                        thisGroup = true;
                        lines.add(line + "\n");
                    }
                    else
                    {
                    	if(naujLankPazym)
                        {
                            naujLankPazym = false;
                            newLines = true;
                            lines.add(dateString + "\n");
                            addToLines(listGroup, lines, checkboxes);
                        }

                        if(Integer.parseInt(String.valueOf(line)) > listGroup.get(0).getGroup() && !newLines)
                        {
                            lines.add(String.valueOf(listGroup.get(0).getGroup()) + "\n");
                            lines.add(dateString + "\n");
                            addToLines(listGroup, lines, checkboxes);
                            newLines = true;
                            }
                        thisGroup = false;
                        lines.add(line + "\n");
                        }
                    } 
                else
                {
                    if (line.contains("-"))//data
                    {
                        noOldLank = false;
                        if (thisGroup)
                        {
                            scanDate = LocalDate.parse(line, pattern);
                            if (scanDate.isEqual(newDate) || scanDate.isAfter(newDate))
                            {
                                lines.add(dateString + "\n");
                                addToLines(listGroup, lines, checkboxes);
                                newLines = true;
                                naujLankPazym = false;
                                thisGroup = false;

                                if(scanDate.isAfter(newDate))
                                {
                                    lines.add(line + "\n");
                                }
                                if(scanDate.isEqual(newDate))
                                {
                                    noOldLank = true;
                                }
                            } 
                            else
                            {
                                naujLankPazym = true;
                                lines.add(line + "\n");
                            }
                        }
                        else
                        {
                            lines.add(line + "\n");
                        }
                    }
                    else
                    {
                        if(!noOldLank)
                        {
                            lines.add(line + "\n");
                        }
                    }
                }

            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
        if(!newLines)
        {
            if(!thisGroup) 
            {
                lines.add(listGroup.get(i).getGroup() + "\n");
            }
            lines.add(dateString + "\n");
            addToLines(listGroup, lines, checkboxes);
            newLines = true;
        }

        Charset charset1 = Charset.forName("US-ASCII");
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset1)) {
            for(i=0;i<lines.size();i++)
            {
                writer.write(lines.get(i));
            }
        } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
        }
    }

    
    public void showAttendanceReview(LocalDate perziurNuoData, LocalDate perziurIkiData, int grupesNr, List<Student> list)//check
    {

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String start = perziurNuoData.format(pattern);
        String end = perziurIkiData.format(pattern);

        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Student> listGroup = new ArrayList<>();//atskirai grupem listai
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getGroup() == grupesNr)
            {
                listGroup.add(list.get(i));
            }
        }

        TreeItem<String> vaizdavimas = new TreeItem <String> (grupesNr + " grupes lankomumas nuo " + start + " iki " + end);

        vaizdavimas.setExpanded(true);

        List<String> lines = new ArrayList<String>();

        int i=0;
        Charset charset = Charset.forName("US-ASCII");
        Path file = Paths.get(LankFile);

        boolean nauduojamData = false;
        boolean startGrowingTree = false;
        boolean pridetiData = false;
        
        TreeItem<String> dateNode = new TreeItem<String>("test");

        try (BufferedReader reader = Files.newBufferedReader(file, charset)) 
        {
            String line = null;
            while ((line = reader.readLine()) != null) 
            {

                if(line.length() == 1 && Integer.parseInt(String.valueOf(line)) == grupesNr)
                {
                    startGrowingTree = true;
                }
                
                if(line.contains("-") && startGrowingTree)
                {
                    if(nauduojamData && pridetiData)
                    {
                        vaizdavimas.getChildren().add(dateNode);
                        pridetiData=false;
                    }
                    
                    LocalDate date = LocalDate.parse(line, formatter1);
                    if((date.isAfter(perziurNuoData) && date.isBefore(perziurIkiData)) || date.isEqual(perziurNuoData) || date.isEqual(perziurIkiData))
                    {
                        pridetiData = true;
                    } 
                    else 
                    {
                        pridetiData = false;
                    }
                    dateNode = new TreeItem<String>(line);
                }
                if(line.contains("buvo") && startGrowingTree && pridetiData)
                {
                    TreeItem<String> attendanceLeaf = new TreeItem<String>(line);
                    dateNode.getChildren().add(attendanceLeaf);
                    nauduojamData = true;
                }
                if(line.length() == 1 && Integer.parseInt(String.valueOf(line)) != grupesNr)
                {
                    startGrowingTree = false;
                    nauduojamData = false;
                    if(pridetiData)
                        vaizdavimas.getChildren().add(dateNode);
                    pridetiData=false;
                }

            }
            if(pridetiData)
            {
                vaizdavimas.getChildren().add(dateNode);
            }

        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        Stage stage = new Stage();
        stage.setTitle("Attendance review");
        VBox box = new VBox();
        final Scene scene = new Scene(box, 400, 300);

        TreeView<String> treeView = new TreeView<String>(vaizdavimas);

        box.getChildren().add(treeView);
        stage.setScene(scene);
        stage.show();
    }
    
    @SuppressWarnings("deprecation")
	private void generatePDF()
	{
        Path file = Paths.get(LankFile);
        List<String> lines = new ArrayList<String>();
        Charset charset = Charset.forName("US-ASCII");

        try{

            String fileName = "Lankomumas.pdf";
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);

            PDPageContentStream content = new PDPageContentStream(doc, page);

            int i=0;
            try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            	
                String line = null;
                while ((line = reader.readLine()) != null) 
                {
                    if(line.length()==1)
                    {
                        content.beginText();
                        content.setFont(PDType1Font.HELVETICA, 12);
                        content.moveTextPositionByAmount(80, 750- i *20);
                        content.drawString(line + " group");
                        content.endText();
                        i++;
                    }
                    
                    if(line.contains("-"))
                    {
                        content.beginText();
                        content.setFont(PDType1Font.HELVETICA, 12);
                        content.moveTextPositionByAmount(100, 750- i *20);
                        content.drawString(line);
                        content.endText();
                        i++;
                    }
                    
                    if(line.contains("buvo"))
                    {
                        content.beginText();
                        content.setFont(PDType1Font.HELVETICA, 12);
                        content.moveTextPositionByAmount(120, 750- i * 20);
                        content.drawString(line);
                        content.endText();
                        i++;
                    }
                }
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
            }


            content.close();
            doc.save(fileName);
            doc.close();

        }
        catch(IOException e) {

            System.out.println(e.getMessage());

        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}




