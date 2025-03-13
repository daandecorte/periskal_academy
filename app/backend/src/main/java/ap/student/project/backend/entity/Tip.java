package ap.student.project.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name="tip")
public class Tip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String text;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="module_id")
    private Module module;

    public Tip() {}

    public Tip(String text, Module module) {
        this.text = text;
        this.module = module;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return "Tip{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", module=" + module +
                '}';
    }
}
