package lab3.repository;
import lab3.model.Teacher;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepository implements ICrudRepository {

    private List<Teacher> teachers = new ArrayList<>();

    /**
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     */
    @Override
    public Object findOne(Long id) {
        for (Teacher teacher : this.teachers) {
            if (teacher.getTeacherId() == id)
                return teacher;
        }
        return null;
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable findAll() {
        return this.teachers;
    }

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     */
    @Override
    public Object save(Object entity) {
        for (Teacher teacher : this.teachers) {
            if (teacher.getTeacherId() == ((Teacher) entity).getTeacherId())
                return entity;
        }
        this.teachers.add((Teacher) entity);
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
        for (Teacher teacher : this.teachers) {
            if (teacher.getTeacherId() == id) {
                this.teachers.remove(teacher);
                return teacher;
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
        for (int i = 0; i < this.teachers.size(); i++) {
            if (teachers.get(i).getTeacherId() == ((Teacher) entity).getTeacherId()) {
                this.teachers.set(i, (Teacher) entity);
                return null;
            }
        }
        return entity;
    }
}
