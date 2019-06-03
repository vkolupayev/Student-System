import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

import java.time.LocalDate;
import java.util.List;

public interface UIFunkcijos {

    List<Student>readFile(ChoiceBox group, int desiredGroup, boolean changeChoiceBox);

    void addNewStudent(String name, String surname, int groupNum);

    void deleteStudent(Student student);

    void editStudent(Student student, Student student1);

    int addGroup();

    void deleteGroup(int groupNum);

    void markAttendance(List<Student> listGroup, CheckBox[] checkboxes, String dateString);

    void showAttendanceReview(LocalDate reviewStartDate, LocalDate reviewEndDate, int groupNum, List<Student> list1);

}
