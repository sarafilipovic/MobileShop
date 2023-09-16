package mobile.model;

import jakarta.persistence.*;

@Entity
@Table(name="watch")
public class Kosarica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "mobile_id")  // Naziv stupca koji će povezivati Course s Category
    private Mobile mobile;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Naziv stupca koji će povezivati Course s Category
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "category_id")  // Naziv stupca koji će povezivati Course s Category
    private Category category;


    public Kosarica(Long id) {
        this.id = id;

    }

    public Kosarica() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Mobile getMobile() {
        return mobile;
    }

    public void setMobile(Mobile mobile) {
        this.mobile = mobile;
    }

    public Kosarica(User createdBy) {
        this.createdBy = createdBy;
    }



    public User getUser() {
        return createdBy;
    }

    public void setUser(User createdBy) {
        this.createdBy = createdBy;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


}
