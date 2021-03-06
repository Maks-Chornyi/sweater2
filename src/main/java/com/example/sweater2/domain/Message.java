package com.example.sweater2.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please, fill this field")
    @Length(max = 2048, message = "Unfortunately, your message too long")
    private String text;

    @Length(max = 255, message = "Unfortunately, your tag is too long")
    private String tag;
    //in this case we have Message class and User variable. our user could has few messages
    //but main class is Message, so here is ManyToOne connection. If we will create the same connection
    //but in User class, so there would be OneToMany (one user, many messages);
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")//name of our column of our user(instead of column name "author"
    private User author;

    private String filename;

    public Message(String text, String tag, User user) {
        this.author = user;
        this.text = text;
        this.tag = tag;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<none>";
    }

}
