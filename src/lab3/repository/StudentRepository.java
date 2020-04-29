package lab3.repository;
import lab3.model.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository implements ICrudRepository {

    private List<Student> students = new ArrayList<>();

    /**
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     */
    @Override
    public Object findOne(Long id) {
        for (Student student : this.students) {
            if (student.getStudentId() == id)
                return student;
        }
        return null;
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable findAll() {
        return this.students;
    }

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     */
    @Override
    public Object save(Object entity) {
        for (Student student : this.students) {
            if (student.getStudentId() == ((Student) entity).getStudentId())
                return entity;
        }
        this.students.add((Student) entity);
        return null;
    }

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     */
    @Override
    public Object delete(Long id) {
        for (Student student : this.students) {
            if (student.getStudentId() == id) {
                this.students.remove(student);
                return student;
            }
        }
        return null;
    }

    /**
     * @param entity entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     */
    @Override
    public Object update(Object entity) {
        for (int i = 0; i < this.students.size(); i++) {
            if (students.get(i).getStudentId() == ((Student) entity).getStudentId()) {
                this.students.set(i, (Student) entity);
                return null;
            }
        }
        return entity;
    }
}
