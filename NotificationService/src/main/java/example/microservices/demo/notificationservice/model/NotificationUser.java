package example.microservices.demo.notificationservice.model;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Table("notification_users")
public class NotificationUser {

    @PrimaryKey
    private String id;
    @Column("first_name")
    private String firstName;
    private String email;

    public NotificationUser() {
    }

    public NotificationUser(String id, String firstName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public String toString() {
        return String.format("NotificationUser [id=%d, firstName='%s', email='%s'", this.id,
                this.firstName, this.email);
    }
}
