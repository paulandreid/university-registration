package lab3.repository;
import lab3.model.Course;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository implements ICrudRepository {

    private List<Course> courses = new ArrayList<>();

    /**
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     */
    @Override
    public Object findOne(Long id) {
        for (Course course : this.courses) {
            if (course.getCourseId() == id)
                return course;
        }
        return null;
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable findAll() {
        return this.courses;
    }

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     */
    @Override
    public Object save(Object entity) {
        for (Course course : this.courses) {
            if (course.getCourseId() == ((Course) entity).getCourseId())
                return entity;
        }
        this.courses.add((Course) entity);
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
        for (Course course : this.courses) {
            if (course.getCourseId() == id) {
                this.courses.remove(course);
                return course;
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
        for (int i = 0; i < this.courses.size(); i++) {
            if (courses.get(i).getCourseId() == ((Course) entity).getCourseId()) {
                this.courses.set(i, (Course) entity);
                return null;
            }
        }
        return entity;
    }
}
