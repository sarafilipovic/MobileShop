package mobile.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;


    @Entity
    @Table(name="categories")
    public class Category {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        @Column(nullable = false)
        @NotBlank(message = "Unesite naziv kategorije.")
        String name;

        @OneToMany(mappedBy = "category")
        List<Mobile> mobile;


        @OneToMany(mappedBy = "category")
        List<Kosarica> kosaricas;




        public Category(Long id, String name ) {
            this.id = id;
            this.name = name;

        }

        public Category() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Mobile> getCourses() {
            return mobile;
        }

        public void setCourses(List<Mobile> cours) {
            this.mobile = cours;
        }

        public List<Kosarica> getWatches() {
            return kosaricas;
        }

        public void setWatches(List<Kosarica> cours) {
            this.kosaricas = kosaricas;
        }



    }


