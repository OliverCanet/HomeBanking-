package com.mindhub.homebanking.controllers;




import com.mindhub.homebanking.configurations.WebAuthentication;
import com.mindhub.homebanking.dtos.ClientDTO;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;

import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {


    @Autowired
    private ClientService clientService;
    @Autowired

    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {

       return clientService.getClientsDTO();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id)
    {
        return clientService.getClientDTO(id);
    }




    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @PostMapping(path = "/clients")

    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {



        if (firstName.isEmpty())  {

            return new ResponseEntity<>("Missing First Name", HttpStatus.BAD_REQUEST);

        }
        if (lastName.isEmpty())  {

            return new ResponseEntity<>("Missing Last Name", HttpStatus.BAD_REQUEST);

        }
        if (email.isEmpty())  {

            return new ResponseEntity<>("Missing Email", HttpStatus.BAD_REQUEST);

        }
        if (password.isEmpty())  {

            return new ResponseEntity<>("Missing Password", HttpStatus.BAD_REQUEST);

        }



        if (clientService.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        }


        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(client);
        Account newAccount = new Account("VIN" + getRandomNumber(100000,10000000), 0.0, LocalDateTime.now(), AccountType.CHECKING,true);
        client.addAccount(newAccount);
        accountService.saveAccount(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);


    }




    @GetMapping("/clients/current")
    public ClientDTO getAuthenticatedClient(Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());
        return new ClientDTO(client);

    }



}