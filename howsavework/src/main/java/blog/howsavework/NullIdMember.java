package blog.howsavework;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class NullIdMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;


    public NullIdMember(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    protected NullIdMember() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
