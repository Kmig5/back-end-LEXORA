package com.example.lexora.client;

import com.example.lexora.user.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Miguel
 */
@Entity
@DiscriminatorValue("CLIENT")
@AllArgsConstructor
@Setter
@Getter
public class Client extends User {

}
